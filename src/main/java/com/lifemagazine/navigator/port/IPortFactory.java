package com.lifemagazine.navigator.port;

import java.util.List;

import com.lifemagazine.navigator.exception.InvalidPortTypeException;

public interface IPortFactory {

	public IPort createPort(PortEnum portEnum, String key, String name, boolean isHub, String countryCode, String locationCode, double latitude, double longitude) throws InvalidPortTypeException;
	
}
