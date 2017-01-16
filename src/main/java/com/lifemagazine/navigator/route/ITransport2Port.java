package com.lifemagazine.navigator.route;

import com.lifemagazine.navigator.port.IPort;
import com.lifemagazine.navigator.transport.ITransport;

public interface ITransport2Port {

	public String getKey();
	public ITransport getTransport();
	public IPort getDestinationPort();
}
