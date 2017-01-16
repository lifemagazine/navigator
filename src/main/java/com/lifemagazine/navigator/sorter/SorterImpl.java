package com.lifemagazine.navigator.sorter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lifemagazine.navigator.exception.DbManagerNotReadyException;
import com.lifemagazine.navigator.rest.NavigatorService;
import com.lifemagazine.navigator.route.IRoute;

public class SorterImpl implements ISorter {
	
	private static final Logger LOG = LoggerFactory.getLogger(SorterImpl.class);
	
	private SortTypeEnum sortType;
	private int resultCount;
	
	public SorterImpl(SortTypeEnum sortType, int resultCount) {
		this.sortType = sortType;
		this.resultCount = resultCount;
	}

	@Override
	public List<IRoute> doSort(List<IRoute> routeList) {
		// TODO Auto-generated method stub
		if (this.sortType == SortTypeEnum.MIN_COST)
			return doSortByCost(routeList, resultCount);
		else
			return doSortByTime(routeList, resultCount);
	}
	
	private List<IRoute> doSortByCost(List<IRoute> routeList, int resultCount) {
		// TODO Auto-generated method stub
		Collections.sort(routeList, new CostAscCompare());
		return routeList;
	}
	
	private List<IRoute> doSortByTime(List<IRoute> routeList, int resultCount) {
		// TODO Auto-generated method stub
		Collections.sort(routeList, new TimeAscCompare());
		return routeList;
	}
	
	private static class CostAscCompare implements Comparator<IRoute> {
		@Override
		public int compare(IRoute route1, IRoute route2) {
			// TODO Auto-generated method stub
			try {
				return route1.getTotalCost() < route2.getTotalCost() ? -1 : route1.getTotalCost() > route2.getTotalCost() ? 1:0;
			} catch (DbManagerNotReadyException e) {
				// TODO Auto-generated catch block
				LOG.error("", e);
				return -1;
			}
		}
	}
	
	private static class TimeAscCompare implements Comparator<IRoute> {
		@Override
		public int compare(IRoute route1, IRoute route2) {
			// TODO Auto-generated method stub
			return route1.getTotalRequiredTime() < route2.getTotalRequiredTime() ? -1 : route1.getTotalRequiredTime() > route2.getTotalRequiredTime() ? 1:0;
		}
	}
}
