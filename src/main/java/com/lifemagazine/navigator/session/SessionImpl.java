package com.lifemagazine.navigator.session;

import java.util.ArrayList;
import java.util.List;

import com.lifemagazine.navigator.search.ISearchRoute;

public class SessionImpl implements ISession {

	private String sessionId;
	private List<ISearchRoute> searchRouteList;
	private long lastAcessTime;
	
	public SessionImpl(String addr) {
		this.sessionId = addr + "-" + System.currentTimeMillis();
		this.searchRouteList = new ArrayList<ISearchRoute>();
	}
	
	@Override
	public String getSessionId() {
		// TODO Auto-generated method stub
		return this.sessionId;
	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		lastAcessTime = System.currentTimeMillis();
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addSearchRoute(ISearchRoute searchRoute) {
		// TODO Auto-generated method stub
		this.searchRouteList.add(searchRoute);
	}

	@Override
	public List<ISearchRoute> getSearchRouteList() {
		// TODO Auto-generated method stub
		return this.searchRouteList;
	}

	@Override
	public void touch() {
		// TODO Auto-generated method stub
		lastAcessTime = System.currentTimeMillis();
	}

}
