package com.lifemagazine.navigator.transport;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import com.lifemagazine.navigator.exception.InvalidTransportTypeException;
import com.lifemagazine.navigator.port.IPort;

public class TransportFactoryImpl implements ITransportFactory {
	
	private static TransportFactoryImpl factory = null;
	
	public static ITransportFactory get() {
		if (factory == null) {
			factory = new TransportFactoryImpl();
		}
		return factory;
	}
	
	private TransportFactoryImpl() {
		
	}

	@Override
	public ITransport createTransport(TransEnum transEnum, String key, String name, IPort sourcePort, IPort destinationPort, 
			List<String> scheduleList, BitSet cycle, double requiredTime, StatusEnum status) throws InvalidTransportTypeException {
		// TODO Auto-generated method stub
		ITransport transport = null;
		if (transEnum == TransEnum.AIRCRAFT) {
			transport = new AircraftTransport(key, name, sourcePort, destinationPort, scheduleList, cycle, requiredTime, status);
		} else if (transEnum == TransEnum.VESSEL) {
			transport = new VesselTransport(key, name, sourcePort, destinationPort, scheduleList, cycle, requiredTime, status);
		} else if (transEnum == TransEnum.TRUCK) {
			transport = new TruckTransport(key, name, sourcePort, destinationPort, scheduleList, cycle, requiredTime, status);
		} else if (transEnum == TransEnum.TRAIN) {
			transport = new TrainTransport(key, name, sourcePort, destinationPort, scheduleList, cycle, requiredTime, status);
		} else {
			throw new InvalidTransportTypeException(transEnum + " is invalid transport type.");
		}
		connectTransport2Port(transport, sourcePort, destinationPort);
		return transport;
	}

	private void connectTransport2Port(ITransport transport, IPort sourcePort, IPort destinationPort) {
		sourcePort.getDepartureTransportList().add(transport);
		destinationPort.getArrivalTransportList().add(transport);
	}
}
