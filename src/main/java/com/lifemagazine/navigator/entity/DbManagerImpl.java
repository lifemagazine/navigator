package com.lifemagazine.navigator.entity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lifemagazine.navigator.config.NavigatorConfig;
import com.lifemagazine.navigator.exception.DbManagerNotReadyException;
import com.lifemagazine.navigator.exception.InvalidPortTypeException;
import com.lifemagazine.navigator.exception.InvalidTransportTypeException;
import com.lifemagazine.navigator.port.IPort;
import com.lifemagazine.navigator.port.PortEnum;
import com.lifemagazine.navigator.port.PortFactoryImpl;
import com.lifemagazine.navigator.transport.ITransport;
import com.lifemagazine.navigator.transport.StatusEnum;
import com.lifemagazine.navigator.transport.TransEnum;
import com.lifemagazine.navigator.transport.TransportFactoryImpl;
import com.lifemagazine.navigator.util.Utility;

public class DbManagerImpl implements IDbManager {
	
	private Logger LOG = LoggerFactory.getLogger(DbManagerImpl.class);
	
	private static final String PORT_AIR = "#AIRPORT";
	private static final String PORT_SEA = "#SEAPORT";
	private static final String PORT_ROAD = "#ROADPORT";
	private static final String PORT_RAIL = "#RAILPORT";

	private static final String TRANSPORT_AIR = "#AIRTRANSPORT";
	private static final String TRANSPORT_SEA = "#VESSELTRANSPORT";
	private static final String TRANSPORT_ROAD = "#TRUCKTRANSPORT";
	private static final String TRANSPORT_RAIL = "#TRAINTRANSPORT";
	
	public static final String TRANSPORTKEY_PREFIX_AIRCRAFT = "AIRCRAFT";
	public static final String TRANSPORTKEY_PREFIX_VESSEL = "VESSEL";
	public static final String TRANSPORTKEY_PREFIX_TRUCK = "TRUCK";
	public static final String TRANSPORTKEY_PREFIX_TRAIN = "TRAIN";
	
	private ConcurrentNavigableMap<String, IPort> portMap = new ConcurrentSkipListMap<String, IPort>();
	private Map<String, Integer> hubMap = new HashMap<String, Integer>();
	private ConcurrentNavigableMap<String, ITransport> transportMap = new ConcurrentSkipListMap<String, ITransport>();
	private Map<TransEnum, Object> lastTransportKeyMap = new HashMap<TransEnum, Object>(); 
	private Object lastPortKey = 0;
	private int hubCount = 0;
	
	private static IDbManager dbManager = null;
	
	public static void init(String dbFilePath, String insertDataFilePath) {
		dbManager = new DbManagerImpl(dbFilePath, insertDataFilePath);
	}
	
	public static IDbManager get() throws DbManagerNotReadyException {
		if (dbManager == null) {
			throw new DbManagerNotReadyException("DbManager is not intialized. Calling DbManagerImpl.init(filePath) is first.");
		}
		return dbManager;
	}
	
	private DbManagerImpl(String filePath, String insertDataFilePath) {
		loadDatabase(filePath, insertDataFilePath);
	}

	@Override
	public void loadDatabase(String filePath, String insertDataFilePath) {
		// TODO Auto-generated method stub
		if (insertDataFilePath != null) {
			insertData(insertDataFilePath);
		}
	}
	
	@Override
	public Map<String, Integer> getHubPortMap() {
		return this.hubMap;
	}
	
	public void setHubPort() {
		
		JSONParser parser = new JSONParser();
		 
        try {
            Object obj = parser.parse(new FileReader("./config/config.json"));
            JSONObject jsonObject = (JSONObject) obj;
            
            double airDefaultCost = (double) jsonObject.get("airDefaultCost");
            double seaDefaultCost = (double) jsonObject.get("seaDefaultCost");
            double roadDefaultCost = (double) jsonObject.get("roadDefaultCost");
            double railDefaultCost = (double) jsonObject.get("railDefaultCost");
            
            NavigatorConfig.get().setCost(TransEnum.AIRCRAFT, airDefaultCost);
            NavigatorConfig.get().setCost(TransEnum.VESSEL, seaDefaultCost);
            NavigatorConfig.get().setCost(TransEnum.TRUCK, roadDefaultCost);
            NavigatorConfig.get().setCost(TransEnum.TRAIN, railDefaultCost);
            
            int airHubBranchNum = ((Long) jsonObject.get("airHubBranchNum")).intValue();
            int seaHubBranchNum = ((Long) jsonObject.get("seaHubBranchNum")).intValue();
            int roadHubBranchNum = ((Long) jsonObject.get("roadHubBranchNum")).intValue();
            int railHubBranchNum = ((Long) jsonObject.get("railHubBranchNum")).intValue();
            List<String> portKeyList = new ArrayList<String>();
            
            JSONArray hubPortList = (JSONArray) jsonObject.get("hubPortList");
            Iterator<String> iterator = hubPortList.iterator();
            while (iterator.hasNext()) {
            	portKeyList.add(iterator.next());
            }
            
            LOG.info("airDefulatCost: " + NavigatorConfig.get().getCost(TransEnum.AIRCRAFT));
            LOG.info("seaDefulatCost: " + NavigatorConfig.get().getCost(TransEnum.VESSEL));
            LOG.info("roadDefulatCost: " + NavigatorConfig.get().getCost(TransEnum.TRUCK));
            LOG.info("railDefulatCost: " + NavigatorConfig.get().getCost(TransEnum.TRAIN));
            
            LOG.info("airHubBranchNum: " + airHubBranchNum);
            LOG.info("seaHubBranchNum: " + seaHubBranchNum);
            LOG.info("roadHubBranchNum: " + roadHubBranchNum);
            LOG.info("railHubBranchNum: " + railHubBranchNum);
            
            System.out.println("airDefulatCost: " + NavigatorConfig.get().getCost(TransEnum.AIRCRAFT));
            System.out.println("seaDefulatCost: " + NavigatorConfig.get().getCost(TransEnum.VESSEL));
            System.out.println("roadDefulatCost: " + NavigatorConfig.get().getCost(TransEnum.TRUCK));
            System.out.println("railDefulatCost: " + NavigatorConfig.get().getCost(TransEnum.TRAIN));
            
            System.out.println("airHubBranchNum: " + airHubBranchNum);
            System.out.println("seaHubBranchNum: " + seaHubBranchNum);
            System.out.println("roadHubBranchNum: " + roadHubBranchNum);
            System.out.println("railHubBranchNum: " + railHubBranchNum);
            
            resetHubPortByCount(airHubBranchNum, seaHubBranchNum, roadHubBranchNum, railHubBranchNum);
            
            resetHubPortByKey(portKeyList);
            
            recalculateCost();
            
        } catch (Exception e) {
            LOG.error("", e);
        }
		
        LOG.info("********************* portMap size: " + this.portMap.size());
		LOG.info("********************* transportMap size: " + this.transportMap.size());
//		LOG.info("********************* hubMap size: " + this.hubMap.size());
	}
	
	@Override
	public void resetHubPortByCount(int airHubBranchNum, int seaHubBranchNum, int roadHubBranchNum, int railHubBranchNum) {
		
		this.hubMap.clear();
		LOG.info("********************* [inited hubMap size: " + this.hubMap.size());
		
		Map<String, Integer> mapAir = new HashMap<String, Integer>();
		Map<String, Integer> mapSea = new HashMap<String, Integer>();
		
		Iterator<String> keys = this.portMap.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			IPort port = this.portMap.get(key);
			port.setHubInfo(false);
//			int airCount = 0;
//			int seaCount = 0;
			port.getArrivalTransportList(); 
			/*for (int i=0; i<list.size(); i++) {
				if (list.get(i).getType() == TransEnum.AIRCRAFT) {
					airCount++;
				} else if (list.get(i).getType() == TransEnum.VESSEL) {
					seaCount++;
				}
			}*/
			if (port.getType() == PortEnum.AIRPORT) {
				mapAir.put(port.getKey().toString(), port.getArrivalTransportList().size());
			} else if (port.getType() == PortEnum.SEAPORT) {
				mapSea.put(port.getKey().toString(), port.getArrivalTransportList().size());
			}
		}
		
		List<String> hubPortArrayList = new ArrayList<String>();
		
		Iterator<String> it = sortByValue(mapAir).iterator();
        while(it.hasNext()) {
            String key = (String) it.next();
            int departureCount = mapAir.get(key);
            if (departureCount > airHubBranchNum) {
            	hubPortArrayList.add(key);
            	LOG.info("------[AIR]" + key + " added to hub port - " + departureCount);
            }
        }
        
        it = sortByValue(mapSea).iterator();
        while(it.hasNext()) {
            String key = (String) it.next();
            int departureCount = mapSea.get(key);
            if (departureCount > seaHubBranchNum) {
            	hubPortArrayList.add(key);
            	LOG.info("------[SEA]" + key + " added to hub port - " + departureCount);
            }
        }
        
        for (int i=0; i<hubPortArrayList.size(); i++) {
        	IPort port = this.getPortByKey("" + hubPortArrayList.get(i));
        	hubMap.put(hubPortArrayList.get(i), port.getDepartureTransportList().size());
        	port.setHubInfo(true);
//        	if (port.getType() == PortEnum.AIRPORT)
//        		LOG.info("[AIRPORT] " + hubPortArrayList.get(i) + ":" + port.getName() + " is hub: " + port.getDepartureTransportList().size());
//        	else if (port.getType() == PortEnum.SEAPORT)
//        		LOG.info("[SEAPORT] " + hubPortArrayList.get(i) + ":" + port.getName() + " is hub: " + port.getDepartureTransportList().size());
        }
        
        hubMap = Utility.sortReverseByValue(hubMap);
		Iterator<Map.Entry<String, Integer>> entries = hubMap.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry entry = (Map.Entry) entries.next();
		    String key = (String)entry.getKey();
		    Integer value = (Integer)entry.getValue();
		    IPort port = this.getPortByKey(key);
		    port.setHubInfo(true);
        	if (port.getType() == PortEnum.AIRPORT)
        		LOG.info("[AIRPORT] " + key + ":" + port.getName() + " is hub: " + port.getDepartureTransportList().size());
        	else if (port.getType() == PortEnum.SEAPORT)
        		LOG.info("[SEAPORT] " + key + ":" + port.getName() + " is hub: " + port.getDepartureTransportList().size());
		}
        	
        LOG.info("********************* [after hubMap size: " + this.hubMap.size());
	}
	
	
	
	@Override
	public void resetHubPortByKey(List<String> keyList) {
		LOG.info("********************* [before hubMap size: " + this.hubMap.size());
		
        for (int i=0; i<keyList.size(); i++) {
        	IPort port = this.getPortByKey(keyList.get(i));
        	hubMap.put(keyList.get(i), port.getDepartureTransportList().size());
        	port.setHubInfo(true);
        	if (port.getType() == PortEnum.AIRPORT)
        		LOG.info("[AIRPORT] " + keyList.get(i) + ":" + port.getName() + " is hub: " + port.getDepartureTransportList().size());
        	else if (port.getType() == PortEnum.SEAPORT)
        		LOG.info("[SEAPORT] " + keyList.get(i) + ":" + port.getName() + " is hub: " + port.getDepartureTransportList().size());
        }
        	
        LOG.info("********************* [after hubMap size: " + this.hubMap.size());
	}
	
	@Override
	public List<IPort> addHubPortByKey(List<String> keyList) {
		LOG.info("********************* [before hubMap size: " + this.hubMap.size());
		
		List<IPort> addedList = new ArrayList<IPort>();
        for (int i=0; i<keyList.size(); i++) {
        	IPort port = this.getPortByKey(keyList.get(i));
        	if (port == null) {
        		LOG.info(keyList.get(i) + " is empty key.");
        		continue;
        	}
        	addedList.add(port);
        	hubMap.put(keyList.get(i), port.getDepartureTransportList().size());
        	port.setHubInfo(true);
        	if (port.getType() == PortEnum.AIRPORT)
        		LOG.info("[AIRPORT] " + keyList.get(i) + ":" + port.getName() + " is hub: " + port.getDepartureTransportList().size());
        	else if (port.getType() == PortEnum.SEAPORT)
        		LOG.info("[SEAPORT] " + keyList.get(i) + ":" + port.getName() + " is hub: " + port.getDepartureTransportList().size());
        }
        	
        LOG.info("********************* [after hubMap size: " + this.hubMap.size());
        
        return addedList;
	}
	
	@Override
	public List<IPort> removeHubPortByKey(List<String> keyList) {
		LOG.info("********************* [before hubMap size: " + this.hubMap.size());
		
		List<IPort> removedList = new ArrayList<IPort>();
		
        for (int i=0; i<keyList.size(); i++) {
        	IPort port = this.getPortByKey(keyList.get(i));
        	if (port == null) {
        		LOG.info(keyList.get(i) + " is empty key.");
        		continue;
        	}
        	this.hubMap.remove(keyList.get(i));
        	port.setHubInfo(false);
        	removedList.add(port);
        	if (port.getType() == PortEnum.AIRPORT)
        		LOG.info("[AIRPORT] " + keyList.get(i) + ":" + port.getName() + " is not hub: " + port.getDepartureTransportList().size());
        	else if (port.getType() == PortEnum.SEAPORT)
        		LOG.info("[SEAPORT] " + keyList.get(i) + ":" + port.getName() + " is not hub: " + port.getDepartureTransportList().size());
        }
        	
        LOG.info("********************* [after hubMap size: " + this.hubMap.size());
        
        return removedList;
	}
	
	private List<String> sortByValue(final Map map) {
        List<String> list = new ArrayList<String>();
        list.addAll(map.keySet());
         
        Collections.sort(list,new Comparator() {
             
            public int compare(Object o1,Object o2) {
                Object v1 = map.get(o1);
                Object v2 = map.get(o2);
                 
                return ((Comparable) v2).compareTo(v1);
            }
             
        });
        return list;
    }
	
	private void insertData(String insertDataFilePath) {
		if (insertDataFilePath == null) return;
		
		FileReader fr = null;
		BufferedReader br = null;
		
		try{
			fr = new FileReader(insertDataFilePath);
			br = new BufferedReader(fr);
			
			String line = null;
			String lineType = null;
//			double cost = 1;

			while((line = br.readLine()) != null) {
				if (line.length() == 0) continue;
				
				if (line.startsWith(PORT_AIR)) {
					lineType = PORT_AIR;
					continue;
				} else if (line.startsWith(TRANSPORT_AIR)) {
					lineType = TRANSPORT_AIR;
//					String[] tmp = line.split(";");
//					NavigatorConfig.get().setCost(TransEnum.AIRCRAFT, Double.parseDouble(tmp[1]));
					continue;
				} else if (line.startsWith(PORT_SEA)) {
					lineType = PORT_SEA;
					continue;
				} else if (line.startsWith(TRANSPORT_SEA)) {
					lineType = TRANSPORT_SEA;
//					String[] tmp = line.split(";");
//					NavigatorConfig.get().setCost(TransEnum.VESSEL, Double.parseDouble(tmp[1]));
					continue;
				} else if (line.startsWith(PORT_ROAD)) {
					lineType = PORT_ROAD;
					continue;
				} else if (line.startsWith(TRANSPORT_ROAD)) {
					lineType = TRANSPORT_ROAD;
//					String[] tmp = line.split(";");
//					NavigatorConfig.get().setCost(TransEnum.TRUCK, Double.parseDouble(tmp[1]));
					continue;
				}
				
				String[] temp = line.split(";");
				if (lineType.equals(PORT_AIR)) {
					boolean isHub = false;
					String[] tmp = temp[4].split(",");
					IPort port = PortFactoryImpl.get().createPort(PortEnum.AIRPORT, 
							temp[0], temp[3], isHub, temp[1], temp[2], Double.parseDouble(tmp[0]), Double.parseDouble(tmp[1]));
					this.portMap.put(port.getKey().toString(), port);
					this.lastPortKey = Integer.parseInt(temp[0]);
				} else if (lineType.equals(TRANSPORT_AIR)) {
					String[] tmp = temp[1].split("-");
					String departtureKey = tmp[0];
					String arrivalKey = tmp[1];
					IPort sourcePort = this.portMap.get(departtureKey);
					IPort destinationPort = this.portMap.get(arrivalKey);
					if (sourcePort == null) {
						LOG.error("sourcePort is null: " + tmp[0]);
						continue;
					}
					if (destinationPort == null) {
						LOG.error("sourcePort is null: " + tmp[0]);
						continue;
					}
					String name = "[" + sourcePort.getName() + "-" + destinationPort.getName() + "]";
					List<String> scheduleList = new ArrayList<String>();
					scheduleList.add(name);
					BitSet cycle = Utility.convertStr2BitSet(temp[2]);
					double requiredTime = Double.parseDouble(temp[3]);
					ITransport transport = TransportFactoryImpl.get().createTransport(TransEnum.AIRCRAFT, temp[0], name, 
							sourcePort, destinationPort, scheduleList, cycle, requiredTime, StatusEnum.RUNNABLE);
					this.transportMap.put(transport.getKey().toString(), transport);
					this.lastTransportKeyMap.put(TransEnum.AIRCRAFT, temp[0]);
				} else if (lineType.equals(PORT_SEA)) {
					boolean isHub = false;
					String[] tmp = temp[4].split(",");
					IPort port = PortFactoryImpl.get().createPort(PortEnum.SEAPORT, 
							temp[0], temp[3], isHub, temp[1], temp[2], Double.parseDouble(tmp[0]), Double.parseDouble(tmp[1]));
					this.portMap.put(port.getKey().toString(), port);
					this.lastPortKey = Integer.parseInt(temp[0]);
				} else if (lineType.equals(TRANSPORT_SEA)) {
					String[] tmp = temp[1].split("-");
					String departtureKey = tmp[0];
					String arrivalKey = tmp[1];
					IPort sourcePort = this.portMap.get(departtureKey);
					IPort destinationPort = this.portMap.get(arrivalKey);
					if (sourcePort == null) {
						LOG.error("sourcePort is null: " + tmp[0]);
						continue;
					}
					if (destinationPort == null) {
						LOG.error("sourcePort is null: " + tmp[0]);
						continue;
					}
					String name = "[" + sourcePort.getName() + "-" + destinationPort.getName() + "]";
					List<String> scheduleList = new ArrayList<String>();
					scheduleList.add(name);
					BitSet cycle = new BitSet();
					for (int i=0; i<7; i++) {
						if (temp[2].substring(i, i+1).equals("1"))
							cycle.set(i, true);
						else
							cycle.set(i, false);
					}
					double requiredTime = Double.parseDouble(temp[3]);
					ITransport transport = TransportFactoryImpl.get().createTransport(TransEnum.VESSEL, temp[0], name, 
							sourcePort, destinationPort, scheduleList, cycle, requiredTime, StatusEnum.RUNNABLE);
					this.transportMap.put(transport.getKey().toString(), transport);
					this.lastTransportKeyMap.put(TransEnum.VESSEL, temp[0]);
				} else if (lineType.equals(TRANSPORT_ROAD)) {
					String[] tmp = temp[1].split("-");
					String departtureKey = tmp[0];
					String arrivalKey = tmp[1];
					IPort sourcePort = this.portMap.get(departtureKey);
					IPort destinationPort = this.portMap.get(arrivalKey);
					if (sourcePort == null) {
						LOG.error("sourcePort is null: " + tmp[0]);
						continue;
					}
					if (destinationPort == null) {
						LOG.error("sourcePort is null: " + tmp[0]);
						continue;
					}
					String name = "[" + sourcePort.getName() + "-" + destinationPort.getName() + "]";
					List<String> scheduleList = new ArrayList<String>();
					scheduleList.add(name);
					BitSet cycle = new BitSet();
					for (int i=0; i<7; i++) {
						if (temp[2].substring(i, i+1).equals("1"))
							cycle.set(i, true);
						else
							cycle.set(i, false);
					}
					double requiredTime = Double.parseDouble(temp[3]);
					ITransport transport = TransportFactoryImpl.get().createTransport(TransEnum.TRUCK, temp[0], name, 
							sourcePort, destinationPort, scheduleList, cycle, requiredTime, StatusEnum.RUNNABLE);
					this.transportMap.put(transport.getKey().toString(), transport);
					this.lastTransportKeyMap.put(TransEnum.TRUCK, temp[0]);
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		
		} finally {
			if(br != null) try{br.close();}catch(IOException e){}
			if(fr != null) try{fr.close();}catch(IOException e){}
			System.out.println("total port size: " + this.portMap.size());
			System.out.println("total transport size: " + this.transportMap.size());
		}
	}
	
	@Override
	public void recalculateCost() {
		LOG.info("reset transport costs. " + this.transportMap.size());
		Iterator<String> keys = this.transportMap.keySet().iterator();
		while (keys.hasNext()) {
			String key = (String) keys.next();
            ITransport transport = transportMap.get(key);
            if (transport != null)
            	transport.recalculateCost();
		}
	}
	
	@Override
	public List<IPort> getAllPort() {
		List<IPort> result = this.portMap.entrySet().stream()
                .map(x -> x.getValue())
                .collect(Collectors.toList());
		return result;
	}

	@Override
	public void addPort(IPort port) throws InvalidPortTypeException {
		// TODO Auto-generated method stub
		String name = port.getName().toString();
		String key = port.getKey().toString();
		this.portMap.put(key, port);
		this.lastPortKey = Integer.parseInt(port.getKey().toString());
	}

	@Override
	public IPort getPortByKey(Object key) {
		// TODO Auto-generated method stub
		return this.portMap.get(key);
	}

	@Override
	public List<IPort> getPortByName(String name) {
		// TODO Auto-generated method stub
		List<IPort> list = new ArrayList<IPort>();
		Iterator<String> keys = this.portMap.keySet().iterator();
		IPort port = null;
		while (keys.hasNext()) {
			String key = keys.next();
			port = this.portMap.get(key);
			if (name.equals(port.getName().toString()))
				list.add(port);
		}
		return list;
	}

	@Override
	public List<IPort> getPortByType(PortEnum portEnum) {
		// TODO Auto-generated method stub
		List<IPort> list = new ArrayList<IPort>();
		Iterator<String> keys = this.portMap.keySet().iterator();
		IPort port = null;
		while (keys.hasNext()) {
			String key = keys.next();
			port = this.portMap.get(key);
			if (portEnum == port.getType())
				list.add(port);
		}
		return list;
	}

	@Override
	public IPort removePort(IPort port) {
		// TODO Auto-generated method stub
		IPort removedPort = this.portMap.remove(port.getKey());;
		return removedPort;
	}

	@Override
	public IPort updatePort(IPort port) {
		// TODO Auto-generated method stub
		String key = port.getKey().toString();
		this.portMap.put(key, port);
		return this.portMap.get(port.getKey().toString());
	}
	
	
	@Override
	public List<ITransport> getAllTransport() {
		List<ITransport> result = this.transportMap.entrySet().stream()
                .map(x -> x.getValue())
                .collect(Collectors.toList());
		return result;
	}

	@Override
	public void addTransport(ITransport transport) {
		// TODO Auto-generated method stub
		String key = transport.getKey().toString();
		String type = transport.getType().toString();
		this.transportMap.put(key, transport);
		
		this.lastTransportKeyMap.put(transport.getType(), transport.getKey());
	}

	@Override
	public ITransport getTransportByKey(Object key) {
		// TODO Auto-generated method stub
		return this.transportMap.get(key.toString());
	}

	@Override
	public List<ITransport> getTransportByType(TransEnum transEnum) {
		// TODO Auto-generated method stub
		List<ITransport> list = new ArrayList<ITransport>();
		Iterator<String> keys = this.transportMap.keySet().iterator();
		ITransport transport = null;
		while (keys.hasNext()) {
			String key = keys.next();
			transport = this.transportMap.get(key);
			if (transEnum == transport.getType())
				list.add(transport);
		}
		return list;
	}

	@Override
	public ITransport removeTransport(ITransport transport) {
		// TODO Auto-generated method stub
		ITransport removedTransport = this.transportMap.remove(transport);
		return removedTransport;
	}

	@Override
	public ITransport updateTransport(ITransport newTransport) {
		// TODO Auto-generated method stub
		ITransport removedTransport = this.transportMap.remove(newTransport.getKey().toString());
		this.transportMap.put(newTransport.getKey().toString(), newTransport);
		return this.transportMap.get(removedTransport.getKey().toString());
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
//		db.close();
	}

	@Override
	public Object createPortKey(PortEnum portEnum) {
		// TODO Auto-generated method stub
		String lastKey = null;
		String prefix = "";
		try {
			if (portEnum == PortEnum.AIRPORT) {
				prefix = TRANSPORTKEY_PREFIX_AIRCRAFT;
			} else if (portEnum == PortEnum.SEAPORT) {
				prefix = TRANSPORTKEY_PREFIX_VESSEL;
			} else if (portEnum == PortEnum.ROADPORT) {
				prefix = TRANSPORTKEY_PREFIX_TRUCK;
			} else if (portEnum == PortEnum.RAILPORT) {
				prefix = TRANSPORTKEY_PREFIX_TRAIN;
			}
			lastKey = (String) this.lastPortKey;
			int num = Integer.parseInt(lastKey.replaceFirst(prefix, ""));
			lastKey = "" + (++num);
			this.lastPortKey = lastKey;
		} catch (Exception e) {
			lastKey = "" + (this.portMap.size() + 1);
			this.lastPortKey = lastKey;
		}
		return lastKey;
	}

	@Override
	public Object createTransportKey(TransEnum transEnum) {
		// TODO Auto-generated method stub
		String lastKey = null;
		String prefix = "";
		try {
			lastKey = (String) this.lastTransportKeyMap.get(transEnum);
			int num = Integer.parseInt(lastKey.replaceFirst(prefix, ""));
			lastKey = prefix + (++num);
		} catch (Exception e) {
			lastKey = prefix + (this.transportMap.size() + 1);
		}
		return lastKey;
	}

}
