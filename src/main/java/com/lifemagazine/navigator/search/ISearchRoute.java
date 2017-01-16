package com.lifemagazine.navigator.search;

import java.util.List;

import com.lifemagazine.navigator.exception.RoutingErrorException;
import com.lifemagazine.navigator.port.IPort;
import com.lifemagazine.navigator.route.IRoute;
import com.lifemagazine.navigator.sorter.ISorter;

public interface ISearchRoute {

	public IPort getSourcePort();
	public IPort getDestinationPort();
	public int getLimitTransportCount();
	public double getLimitRequiredTime();
	public double getLimitCost();
	public List<IRoute> getResultRouteList();
	public void searchPath() throws RoutingErrorException;
	public List<IRoute> doSort(ISorter sorter);
}
