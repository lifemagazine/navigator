package com.lifemagazine.navigator.sorter;

import java.util.List;

import com.lifemagazine.navigator.route.IRoute;

public interface ISorter {

	public List<IRoute> doSort(List<IRoute> routeList);
	
}
