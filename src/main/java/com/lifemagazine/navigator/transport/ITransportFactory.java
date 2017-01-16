package com.lifemagazine.navigator.transport;

import java.util.BitSet;
import java.util.List;

import com.lifemagazine.navigator.exception.InvalidTransportTypeException;
import com.lifemagazine.navigator.port.IPort;

public interface ITransportFactory {

	public ITransport createTransport(TransEnum transEnum, String key, String name, IPort sourcePort, IPort destinationPort, 
			List<String> scheduleList, BitSet cycle, double requiredTime, StatusEnum status) throws InvalidTransportTypeException;
}
