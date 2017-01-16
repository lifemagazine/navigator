package com.lifemagazine.navigator.port;

import java.util.List;

import com.lifemagazine.navigator.transport.ITransport;

public interface IPort {
	
	public Object getKey();
	public Object getName();
	public PortEnum getType();
	public boolean isHub();
	public void setHubInfo(boolean isHub);
	public List<ITransport> getArrivalTransportList();
	public List<ITransport> getDepartureTransportList();
	public String getCountryCode();
	public String getLocationCode();
	public double getLatitude();
	public double getLongitude();
	public String toString();
}
