package com.lifemagazine.navigator.route;

import com.lifemagazine.navigator.exception.DbManagerNotReadyException;
import com.lifemagazine.navigator.exception.RoutingErrorException;

public interface IRoute {
	
	public boolean isPushable(ITransport2Port t2port);
	public boolean isPopable();
	public ITransport2Port get(int i);
	public int size();
	public void push(ITransport2Port t2port) throws RoutingErrorException;
	public ITransport2Port pop() throws RoutingErrorException;
	public int getTotalTransportCount();
	public double getTotalRequiredTime();
	public double getTotalCost() throws DbManagerNotReadyException;
	public boolean isValid();
	public String toString();
	public IRoute cloneCurrentRoute();
}
