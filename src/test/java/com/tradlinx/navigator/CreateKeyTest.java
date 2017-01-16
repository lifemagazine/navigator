package com.tradlinx.navigator;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentNavigableMap;

import com.lifemagazine.navigator.port.IPort;
import com.lifemagazine.navigator.port.PortEnum;
import com.lifemagazine.navigator.transport.TransEnum;

public class CreateKeyTest {
	
	public static final String PORTKEY_PREFIX_AIR = "AIR";
	public static final String PORTKEY_PREFIX_SEA = "SEA";
	public static final String PORTKEY_PREFIX_ROAD = "ROAD";
	public static final String PORTKEY_PREFIX_RAIL = "RAIL";
	
	private static Map<String, String> portMap = new HashMap<String, String>();
	private static Map<PortEnum, String> lastPortKeyMap = new HashMap<PortEnum, String>();
	private static Map<TransEnum, String> lastTransportKeyMap = new HashMap<TransEnum, String>(); 

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		portMap.put("AIR1", "test");
		portMap.put("AIR2", "test");
		
		lastPortKeyMap.put(PortEnum.AIRPORT, "AIR2");
		
		System.out.println(createPortKey(PortEnum.AIRPORT));
		
		System.out.println(createPortKey(PortEnum.RAILPORT));
	}

	private static String createPortKey(PortEnum portEnum) {
		// TODO Auto-generated method stub
		String lastKey = null;
		String prefix = "";
		try {
			if (portEnum == PortEnum.AIRPORT) {
				prefix = PORTKEY_PREFIX_AIR;
			} else if (portEnum == PortEnum.SEAPORT) {
				prefix = PORTKEY_PREFIX_SEA;
			} else if (portEnum == PortEnum.ROADPORT) {
				prefix = PORTKEY_PREFIX_ROAD;
			} else if (portEnum == PortEnum.RAILPORT) {
				prefix = PORTKEY_PREFIX_RAIL;
			}
			lastKey = (String) lastPortKeyMap.get(portEnum);
			int num = Integer.parseInt(lastKey.replaceFirst(prefix, ""));
			lastKey = prefix + (++num);
		} catch (Exception e) {
			lastKey = prefix + (portMap.size() + 1);
		}
		return lastKey;
	}
}
