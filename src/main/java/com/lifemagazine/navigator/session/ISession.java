package com.lifemagazine.navigator.session;

import java.util.List;

import com.lifemagazine.navigator.route.IRoute;
import com.lifemagazine.navigator.search.ISearchRoute;
import com.lifemagazine.navigator.sorter.ISorter;

public interface ISession {

	public String getSessionId();
	public void init();
	public void close();
	public void touch();
	public void addSearchRoute(ISearchRoute searchRoute);
	public List<ISearchRoute> getSearchRouteList();
}
