package com.lifemagazine.navigator.route;

import com.lifemagazine.navigator.port.IPort;
import com.lifemagazine.navigator.transport.ITransport;

public class Transport2PortImpl implements ITransport2Port {
	
	private ITransport transport;
	private IPort destinationPort;
	private String key;
	
	public Transport2PortImpl(ITransport transport, IPort port) {
		this.transport = transport;
		this.destinationPort = port;
		this.key = port == null ? "null" : port.getKey().toString() + this.destinationPort.getKey().toString();
	}
	
	@Override
	public String getKey() {
		return this.key;
	}
	
	@Override
	public ITransport getTransport() {
		// TODO Auto-generated method stub
		return this.transport;
	}

	@Override
	public IPort getDestinationPort() {
		// TODO Auto-generated method stub
		return this.destinationPort;
	}

}
