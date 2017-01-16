package com.lifemagazine.navigator.route;

import java.util.List;

import com.lifemagazine.navigator.exception.RoutingErrorException;
import com.lifemagazine.navigator.transport.ITransport;

public interface IRoutingRule {

	public boolean isValidRoute(IRoute route, ITransport2Port newT2Port);
	public List<ITransport> filterDepartureTransportList(IRoute route) throws RoutingErrorException;
}
