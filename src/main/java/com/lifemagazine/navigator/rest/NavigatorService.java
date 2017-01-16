package com.lifemagazine.navigator.rest;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lifemagazine.navigator.config.NavigatorConfig;
import com.lifemagazine.navigator.entity.DbManagerImpl;
import com.lifemagazine.navigator.exception.DbManagerNotReadyException;
import com.lifemagazine.navigator.exception.InvalidPortKeyException;
import com.lifemagazine.navigator.exception.InvalidTransportTypeException;
import com.lifemagazine.navigator.exception.RoutingErrorException;
import com.lifemagazine.navigator.port.IPort;
import com.lifemagazine.navigator.port.PortEnum;
import com.lifemagazine.navigator.port.PortFactoryImpl;
import com.lifemagazine.navigator.route.IRoute;
import com.lifemagazine.navigator.route.ITransport2Port;
import com.lifemagazine.navigator.search.ISearchRoute;
import com.lifemagazine.navigator.search.SearchRouteImpl;
import com.lifemagazine.navigator.session.ISession;
import com.lifemagazine.navigator.session.SessionImpl;
import com.lifemagazine.navigator.sorter.SortTypeEnum;
import com.lifemagazine.navigator.sorter.SorterImpl;
import com.lifemagazine.navigator.transport.ITransport;
import com.lifemagazine.navigator.transport.StatusEnum;
import com.lifemagazine.navigator.transport.TransEnum;
import com.lifemagazine.navigator.transport.TransportFactoryImpl;
import com.lifemagazine.navigator.util.Utility;

import spark.Request;
import spark.Response;

public class NavigatorService {

	private Logger LOG = LoggerFactory.getLogger(NavigatorService.class);

	public List<Port> getAllPorts(Request req, Response res) {

		PortEnum portEnum = PortEnum.valueOf(req.queryParams("type"));
		String reqCountryCode = req.queryParams("countrycode");

		List<IPort> portList;
		try {
			if (portEnum == PortEnum.ALL)
				portList = DbManagerImpl.get().getAllPort();
			else
				portList = DbManagerImpl.get().getPortByType(portEnum);
		} catch (DbManagerNotReadyException e) {
			// TODO Auto-generated catch block
			LOG.error("", e);
			return null;
		}

		if (portList == null || portList.size() == 0)
			return null;

		List<Port> resultList = new ArrayList<Port>();
		Port port = null;
		for (int i=0; i<portList.size(); i++) {
			IPort originPort = portList.get(i);

			port = new Port();
			port.setId(originPort.getKey().toString());
			port.setName(originPort.getName().toString());
			if (!reqCountryCode.equals("ALL") && !reqCountryCode.equals(originPort.getCountryCode()))
				continue;
			port.setCountryCode(originPort.getCountryCode());
			port.setLocationCode(originPort.getLocationCode());
			port.setLatitude(originPort.getLatitude());
			port.setLongitude(originPort.getLongitude());
			port.setType(originPort.getType().toString());

			List<String> departure_list = new ArrayList<String>();
			List<ITransport> transportList = originPort.getDepartureTransportList();
			for (int k=0; k<transportList.size(); k++) {
				departure_list.add(transportList.get(k).getKey().toString());
			}
			port.setDeparture_list(departure_list);

			List<String> arrival_list = new ArrayList<String>();
			transportList = originPort.getArrivalTransportList();
			for (int k=0; k<transportList.size(); k++) {
				arrival_list.add(transportList.get(k).getKey().toString());
			}
			port.setArrival_list(arrival_list);
			resultList.add(port);
		}
		return resultList;
	}

	public MessagePort getPort(Request req, Response res) {

		MessagePort message = new MessagePort();
		try {
			String key = req.queryParams("id");
			IPort originPort = DbManagerImpl.get().getPortByKey(key);

			Port port = new Port();
			port.setId(originPort.getKey().toString());
			port.setName(originPort.getName().toString());
			port.setCountryCode(originPort.getCountryCode());
			port.setLocationCode(originPort.getLocationCode());
			port.setLatitude(originPort.getLatitude());
			port.setLongitude(originPort.getLongitude());
			port.setType(originPort.getType().toString());

			List<String> departure_list = new ArrayList<String>();
			List<ITransport> transportList = originPort.getDepartureTransportList();
			for (int k=0; k<transportList.size(); k++) {
				departure_list.add(transportList.get(k).getKey().toString());
			}
			port.setDeparture_list(departure_list);

			List<String> arrival_list = new ArrayList<String>();
			transportList = originPort.getArrivalTransportList();
			for (int k=0; k<transportList.size(); k++) {
				arrival_list.add(transportList.get(k).getKey().toString());
			}
			port.setArrival_list(arrival_list);

			message.setId(key);
			message.setStatus(200);
			message.setMessage("success");
			message.setResult(port);
			return message;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error("", e);
			res.status(400);
			message.setId(null);
			message.setStatus(400);
			message.setMessage(e.getMessage());
			return message;
		}
	}

	public Message addPort(Request req, Response res) {
		Message message = new Message();
		try {
			String type = req.queryParams("type");
			String name = req.queryParams("name");
			String countryCode = req.queryParams("countryCode");
			String locationCode = req.queryParams("locationCode");
			double latitude = Double.parseDouble(req.queryParams("latitude"));
			double longitude = Double.parseDouble(req.queryParams("longitude"));
			String key = (String) DbManagerImpl.get().createPortKey(PortEnum.valueOf(type));

			IPort port = PortFactoryImpl.get().createPort(PortEnum.valueOf(type), key, name, false, countryCode, locationCode, latitude, longitude);
			DbManagerImpl.get().addPort(port);

			message.setId(key);
			message.setStatus(200);
			message.setMessage("success");
			return message;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error("", e);
			res.status(400);
			message.setId(null);
			message.setStatus(400);
			message.setMessage(e.getMessage());
			return message;
		}
	}

	public Message updatePort(Request req, Response res) {
		Message message = new Message();
		try {
			String key = req.queryParams("id");
			String type = req.queryParams("type");
			String name = req.queryParams("name");
			String countryCode = req.queryParams("countryCode");
			String locationCode = req.queryParams("locationCode");
			double latitude = Double.parseDouble(req.queryParams("latitude"));
			double longitude = Double.parseDouble(req.queryParams("longitude"));

			IPort oldPort = DbManagerImpl.get().getPortByKey(key);
			IPort newPort = PortFactoryImpl.get().createPort(PortEnum.valueOf(type), key, name, oldPort.isHub(), countryCode, locationCode, latitude, longitude);
			for (ITransport transport: oldPort.getDepartureTransportList()) {
				newPort.getDepartureTransportList().add(transport);
			}
			for (ITransport transport: oldPort.getArrivalTransportList()) {
				newPort.getArrivalTransportList().add(transport);
			}
			DbManagerImpl.get().updatePort(newPort);

			message.setId(key);
			message.setStatus(200);
			message.setMessage("success");
			return message;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error("", e);
			res.status(400);
			message.setId(null);
			message.setStatus(400);
			message.setMessage(e.getMessage());
			return message;
		}
	}

	public Message removePort(Request req, Response res) {
		Message message = new Message();
		try {
			String key = req.queryParams("id");

			IPort port = DbManagerImpl.get().getPortByKey(key);

			IPort removedPort = DbManagerImpl.get().removePort(port);

			message.setId(key);
			message.setStatus(200);
			message.setMessage("success");
			return message;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error("", e);
			res.status(400);
			message.setId(null);
			message.setStatus(400);
			message.setMessage(e.getMessage());
			return message;
		}
	}

	public List<Transport> getAllTransports(Request req, Response res) {

		TransEnum transEnum = TransEnum.valueOf(req.queryParams("type"));

		List<ITransport> transportList;
		try {
			if (transEnum == TransEnum.ALL)
				transportList = DbManagerImpl.get().getAllTransport();
			else
				transportList = DbManagerImpl.get().getTransportByType(transEnum);
		} catch (DbManagerNotReadyException e) {
			// TODO Auto-generated catch block
			LOG.error("", e);
			res.status(400);
			return null;
		}

		if (transportList == null || transportList.size() == 0)
			return null;

		List<Transport> resultList = new ArrayList<Transport>();
		Transport transport = null;
		for (int i=0; i<transportList.size(); i++) {
			ITransport originTransport = transportList.get(i);

			transport = new Transport();
			transport.setId(originTransport.getKey().toString());
			transport.setName(originTransport.getName());
			transport.setType(originTransport.getType().toString());
			transport.setSourcePort(originTransport.getSourcePort().getKey().toString());
			transport.setDestinationPort(originTransport.getDestinationPort().getKey().toString());
			transport.setCycle(Utility.convertBitSet2Str(originTransport.getCycle()));
			transport.setCost(originTransport.getCost());
			transport.setRequiredTime(originTransport.getRequiredTime());
			transport.setDistance(originTransport.getDistance());
			transport.setStatus(originTransport.getStatus().toString());
			transport.setScheduleList(originTransport.getScheduleList());
			resultList.add(transport);

		}
		return resultList;
	}

	public MessageTransport getTransport(Request req, Response res) {
		MessageTransport message = new MessageTransport();
		try {
			String key = req.queryParams("id");
			ITransport originTransport = DbManagerImpl.get().getTransportByKey(key);

			Transport transport = new Transport();
			transport.setId(originTransport.getKey().toString());
			transport.setName(originTransport.getName());
			transport.setType(originTransport.getType().toString());
			transport.setSourcePort(originTransport.getSourcePort().getKey().toString());
			transport.setDestinationPort(originTransport.getDestinationPort().getKey().toString());
			transport.setCycle(Utility.convertBitSet2Str(originTransport.getCycle()));
			transport.setCost(originTransport.getCost());
			transport.setRequiredTime(originTransport.getRequiredTime());
			transport.setDistance(originTransport.getDistance());
			transport.setStatus(originTransport.getStatus().toString());
			transport.setScheduleList(originTransport.getScheduleList());

			message.setId(key);
			message.setStatus(200);
			message.setMessage("success");
			message.setResult(transport);
			return message;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error("", e);
			res.status(400);
			message.setId(null);
			message.setStatus(400);
			message.setMessage(e.getMessage());
			return message;
		}
	}

	public Message addTransport(Request req, Response res) {
		Message message = new Message();
		try {
			TransEnum type = TransEnum.valueOf(req.queryParams("type").toString());
			String name = req.queryParams("name");
			String sourcePortStr = req.queryParams("sourcePort");
			IPort sourcePort = DbManagerImpl.get().getPortByKey(sourcePortStr);
			String destinationPortStr = req.queryParams("destinationPort");
			IPort destinationPort = DbManagerImpl.get().getPortByKey(destinationPortStr);
			String scheduleListStr = req.queryParams("scheduleList");
			List<String> scheduleList = Utility.convertStr2List(scheduleListStr);
			String cycleStr = req.queryParams("cycle");
			BitSet cycle = Utility.convertStr2BitSet(cycleStr);
			double cost = Double.parseDouble(req.queryParams("cost"));
			LOG.debug("******************** cost: " + cost);
			double requiredTime = Double.parseDouble(req.queryParams("requiredTime"));
			StatusEnum status = StatusEnum.valueOf(req.queryParams("status").toString());
			String key = (String) DbManagerImpl.get().createTransportKey(type);

			ITransport transport = TransportFactoryImpl.get().createTransport(
					type, key, name, sourcePort, destinationPort, scheduleList, cycle, requiredTime, status);
			DbManagerImpl.get().addTransport(transport);

			res.status(200);
			message.setId(key);
			message.setStatus(200);
			message.setMessage("success");
			return message;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error("", e);
			res.status(400);
			message.setId(null);
			message.setStatus(400);
			message.setMessage(e.getMessage());
			return message;
		}
	}

	public Message updateTransport(Request req, Response res) {
		Message message = new Message();
		try {
			String key = req.params("id");
			TransEnum type = TransEnum.valueOf(req.queryParams("type").toString());
			String name = req.queryParams("name");
			String sourcePortStr = req.queryParams("sourcePort");
			IPort sourcePort = DbManagerImpl.get().getPortByKey(sourcePortStr);
			String destinationPortStr = req.queryParams("destinationPort");
			IPort destinationPort = DbManagerImpl.get().getPortByKey(destinationPortStr);
			String scheduleListStr = req.queryParams("scheduleList");
			List<String> scheduleList = Utility.convertStr2List(scheduleListStr);
			String cycleStr = req.queryParams("cycle");
			BitSet cycle = Utility.convertStr2BitSet(cycleStr);
			double cost = Double.parseDouble(req.queryParams("cost"));
			double requiredTime = Double.parseDouble(req.queryParams("requiredTime"));
			StatusEnum status = StatusEnum.valueOf(req.queryParams("status").toString());

			ITransport modifyTransport = TransportFactoryImpl.get().createTransport(
					type, key, name, sourcePort, destinationPort, scheduleList, cycle, requiredTime, status);
			DbManagerImpl.get().updateTransport(modifyTransport);

			message.setId(key);
			message.setStatus(200);
			message.setMessage("success");
			return message;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error("", e);
			res.status(400);
			message.setId(null);
			message.setStatus(400);
			message.setMessage(e.getMessage());
			return message;
		}
	}

	public Message removeTransport(Request req, Response res) {
		Message message = new Message();
		try {
			String key = req.params("id");
			System.out.println("************** remove id:" + key);
			ITransport transport = DbManagerImpl.get().getTransportByKey(key);

			ITransport removedTransort = DbManagerImpl.get().removeTransport(transport);

			if (removedTransort == null)
				throw new InvalidTransportTypeException("there is no Transport for ID:" + key);

			message.setId(key);
			message.setStatus(200);
			message.setMessage("success");
			return message;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error("", e);
			res.status(400);
			message.setId(null);
			message.setStatus(400);
			message.setMessage(e.getMessage());
			return message;
		}
	}

	public DetectMessage getRoutes(Request req, Response res) {

		long startTime = System.currentTimeMillis();

		String sourcePortId = req.queryParams("sport");
		String destinationPortId = req.queryParams("dport");
		int limitTransportCount = Integer.parseInt(req.queryParams("limitdepth"));
		double costLimit = Double.parseDouble(req.queryParams("costlimit"));
		double termLimit = Double.parseDouble(req.queryParams("termlimit"));
		String fromDate = req.queryParams("fromdate");

		LOG.info("******** sourcePortId: " + sourcePortId);
		LOG.info("******** destinationPortId: " + destinationPortId);
		LOG.info("******** limitTransportCount: " + limitTransportCount);
		LOG.info("******** costLimit: " + costLimit);
		LOG.info("******** termLimit: " + termLimit);
		LOG.info("******** fromDate: " + fromDate);


		ISession session = new SessionImpl("localhost");
		//		int transportCountLimit = 5;
		//		double requiredTimeLimit = 30;
		//		double costLimit = 200000000;
		DetectMessage message = new DetectMessage();
		List<List<String>> list = new ArrayList<List<String>>();
		try {
			detectPath(session, sourcePortId, destinationPortId, limitTransportCount, termLimit, costLimit, SortTypeEnum.MIN_COST, 0);
			
			ISearchRoute resultSearchRoute = session.getSearchRouteList().get(0);
			List<IRoute> routeList = resultSearchRoute.getResultRouteList();
			
			message.setStatus(200);
			message.setMessage("Good Job");
			message.setResultCount(routeList.size());
			
			for (IRoute route: routeList) {
				List<String> subList = new ArrayList<String>();
				Calendar startCalendar = Utility.convert2Calendar(fromDate);
				for (int i=0; i<route.size(); i++) {
					ITransport2Port t2p = route.get(i);
					if (t2p.getTransport() != null) {
						String transportKey = t2p.getTransport().getKey().toString();
						double duration = t2p.getTransport().getRequiredTime();
						if (t2p.getTransport().getType() == TransEnum.AIRCRAFT) {
							duration = Math.ceil(duration / 24);
						}
						startCalendar = Utility.carculateDate(startCalendar, (int) duration);
						subList.add(transportKey + ":" + Utility.convert2Str(startCalendar));
					}

					subList.add(t2p.getDestinationPort().getKey().toString());
				}
				if (checkRoute(sourcePortId, destinationPortId, subList)) {
					subList.add(0, Utility.doRoundN2Str(route.getTotalCost()));
					subList.add(1, Utility.doRoundN2Str(route.getTotalRequiredTime()));
					list.add(subList);
				}
			}
			message.setRoutes(list);
			return message;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error("", e);
			res.status(400);
			message.setStatus(400);
			message.setMessage(e.getMessage());
			message.setResultCount(0);
			return message;
		} finally {
			long endTime = System.currentTimeMillis();
			long duration = endTime - startTime;
			LOG.info("************************************************");
			LOG.info("**************** [ duration: " + duration + "ms ]******************");
			LOG.info("************************************************");
//			System.out.println("************************************************");
//			System.out.println("**************** [ duration: " + duration + "ms ]******************");
//			System.out.println("************************************************");
		}
	}

	private boolean checkRoute(String sourcePortId, String destinationPortId, List<String> list) throws RoutingErrorException {
		/*if (!list.get(0).equals(sourcePortId))
			throw new RoutingErrorException("The source port of results is different with the request. result:" + list.get(0) + ", request:" + sourcePortId);
		if (!list.get(list.size()-1).equals(destinationPortId))
			throw new RoutingErrorException("The destination port of results is different with the request. result:" + list.get(list.size()-1) + ", request:" + destinationPortId);*/
		if (!list.get(0).equals(sourcePortId) || !list.get(list.size()-1).equals(destinationPortId))
			return false;
		else
			return true;
	}

	private void detectPath(ISession session, String sourcePortId, String destinationPortId, 
			int transportCountLimit, double requiredTimeLimit, double costLimit, 
			SortTypeEnum sortType, int resultListCount) throws DbManagerNotReadyException, InvalidPortKeyException, RoutingErrorException {

		IPort sourcePort = DbManagerImpl.get().getPortByKey(sourcePortId);
		IPort destinationPort = DbManagerImpl.get().getPortByKey(destinationPortId);

		if (sourcePort == null) throw new InvalidPortKeyException("invalid port key: " + sourcePortId);
		if (destinationPort == null) throw new InvalidPortKeyException("invalid port key: " + destinationPortId);

		LOG.info("Detect from [" + sourcePort.getName() + ":" + sourcePort.getKey() + "] to [" + destinationPort.getName() + ":" + destinationPort.getKey() + "]");

		ISearchRoute searchRoute = new SearchRouteImpl(session, sourcePort, destinationPort, transportCountLimit, requiredTimeLimit, costLimit);
		searchRoute.searchPath();
		searchRoute.doSort(new SorterImpl(sortType, resultListCount));

		List<ISearchRoute> searchRouteList = session.getSearchRouteList();
		LOG.info("result1: " + searchRouteList.size());
		for (ISearchRoute resultSearchRoute: searchRouteList) {
			List<IRoute> list = resultSearchRoute.getResultRouteList();
			LOG.info("sub result: " + list.size());
			int index = 0;
			for (IRoute route: list) {
				StringBuilder sb = new StringBuilder(++index + "th: ");
				for (int i=0; i<route.size(); i++) {
					ITransport2Port t2p = route.get(i);
					if (t2p.getTransport() != null) {
						sb.append("->").append(t2p.getTransport().getKey().toString()).append("->");
					}
					sb.append("[").append(t2p.getDestinationPort().getName()).append("]");
				}
				sb.append(" => time: ").append(route.getTotalRequiredTime()).append(", cost: ").append(route.getTotalCost());
				LOG.info(sb.toString());
			}
		}
	}

	public List<Cost> getCosts(Request req, Response res) {

		Cost cost;
		List<Cost> list = new ArrayList<Cost>();
		try {
			cost = NavigatorConfig.get().getCostInfo();
			list.add(cost);
		} catch (DbManagerNotReadyException e) {
			// TODO Auto-generated catch block
			LOG.error("", e);
		}

		return list;
	}

	public Message updateCosts(Request req, Response res) {
		Message message = new Message();
		try {
			double airCost = Double.parseDouble(req.queryParams("air"));
			double seaCost = Double.parseDouble(req.queryParams("sea"));
			double roadCost = Double.parseDouble(req.queryParams("road"));
			double railCost = Double.parseDouble(req.queryParams("rail"));

			Cost cost = new Cost();
			cost.setAir(airCost);
			cost.setSea(seaCost);
			cost.setRoad(roadCost);
			cost.setRail(railCost);

			NavigatorConfig.get().setCostInfo(cost);
			DbManagerImpl.get().recalculateCost();
			
			message.setId("Default");
			message.setStatus(200);
			message.setMessage("success");
			return message;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error("", e);
			res.status(400);
			message.setId(null);
			message.setStatus(400);
			message.setMessage(e.getMessage());
			return message;
		}
	}

	public List<Port> getHubPorts(Request req, Response res) {

		try {

			Iterator<String> keys = DbManagerImpl.get().getHubPortMap().keySet().iterator();

			List<Port> resultList = new ArrayList<Port>();
			Port port = null;
			while (keys.hasNext()) {
				String key = keys.next();
				IPort originPort = DbManagerImpl.get().getPortByKey(key);

				port = new Port();
				port.setId(originPort.getKey().toString());
				port.setName(originPort.getName().toString());
				port.setCountryCode(originPort.getCountryCode());
				port.setLocationCode(originPort.getLocationCode());
				port.setLatitude(originPort.getLatitude());
				port.setLongitude(originPort.getLongitude());
				port.setType(originPort.getType().toString());

				List<String> departure_list = new ArrayList<String>();
				List<ITransport> transportList = originPort.getDepartureTransportList();
				for (int k=0; k<transportList.size(); k++) {
					departure_list.add(transportList.get(k).getKey().toString());
				}
				port.setDeparture_list(departure_list);

				List<String> arrival_list = new ArrayList<String>();
				transportList = originPort.getArrivalTransportList();
				for (int k=0; k<transportList.size(); k++) {
					arrival_list.add(transportList.get(k).getKey().toString());
				}
				port.setArrival_list(arrival_list);
				resultList.add(port);
			}
			return resultList;

		} catch (DbManagerNotReadyException e) {
			// TODO Auto-generated catch block
			LOG.error("", e);
			return null;
		}
	}
	
	public Message removeHubPort(Request req, Response res) {

		Message message = new Message();
		try {
			String key = req.queryParams("id");
			List<String> keyList = new ArrayList<String>();
			keyList.add(key);
			List<IPort> portList = DbManagerImpl.get().removeHubPortByKey(keyList);
			if (portList.size() > 0) {
				message.setId(key);
				message.setStatus(200);
				message.setMessage("success");
				return message;
			} else {
				LOG.info("there is no updated port for " + key);
				res.status(400);
				message.setId(null);
				message.setStatus(400);
				message.setMessage("there is no updated port for " + key);
				return message;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error("", e);
			res.status(400);
			message.setId(null);
			message.setStatus(400);
			message.setMessage(e.getMessage());
			return message;
		}
	}
	
	public Message addHubPort(Request req, Response res) {
		Message message = new Message();
		try {
			String key = req.queryParams("id");
			List<String> keyList = new ArrayList<String>();
			keyList.add(key);
			List<IPort> portList = DbManagerImpl.get().addHubPortByKey(keyList);
			if (portList.size() > 0) {
				message.setId(key);
				message.setStatus(200);
				message.setMessage("success");
				return message;
			} else {
				LOG.info("there is no updated port for " + key);
				res.status(400);
				message.setId(null);
				message.setStatus(400);
				message.setMessage("there is no updated port for " + key);
				return message;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error("", e);
			res.status(400);
			message.setId(null);
			message.setStatus(400);
			message.setMessage(e.getMessage());
			return message;
		}
	}
}
