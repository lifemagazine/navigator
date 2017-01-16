package com.lifemagazine.navigator.port;

import com.lifemagazine.navigator.exception.InvalidPortTypeException;

public class PortFactoryImpl implements IPortFactory {
	
	private static PortFactoryImpl factory = null;
	
	public static IPortFactory get() {
		if (factory == null) {
			factory = new PortFactoryImpl();
		}
		return factory;
	}

	private PortFactoryImpl() {
		
	}
	
	@Override
	public IPort createPort(PortEnum portEnum, String key, String name, boolean isHub, String countryCode, String locationCode, double latitude, double longitude) 
			throws InvalidPortTypeException {
		// TODO Auto-generated method stub
		IPort port = null;
		if (portEnum == PortEnum.AIRPORT) {
			port = new Airport(key, name, isHub, countryCode, locationCode, latitude, longitude);
		} else if (portEnum == PortEnum.SEAPORT) {
			port = new Seaport(key, name, isHub, countryCode, locationCode, latitude, longitude);
		} else if (portEnum == PortEnum.RAILPORT) {
			port = new Railport(key, name, isHub, countryCode, locationCode, latitude, longitude);
		} else if (portEnum == PortEnum.ROADPORT) {
			port = new Roadport(key, name, isHub, countryCode, locationCode, latitude, longitude);
		} else {
			throw new InvalidPortTypeException(portEnum + " is invalid port type.");
		}
		return port;
	}

}
