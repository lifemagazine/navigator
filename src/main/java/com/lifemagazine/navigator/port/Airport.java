package com.lifemagazine.navigator.port;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.lifemagazine.navigator.transport.ITransport;

public class Airport implements IPort, Serializable {
	
	private static final long serialVersionUID = -8798205991877440851L; 
	
	private String key;
	private PortEnum type;
	private String name;
	private boolean isHub;
	private String countryCode;
	private String locationCode;
	private List<ITransport> arrivalList;
	private List<ITransport> departureList;
	private double latitude;
	private double longitude;
	
	public Airport(String key, String name, boolean isHub, String countryCode, String locationCode, double latitude, double longitude) {
		this.key = key;
		this.type = PortEnum.AIRPORT;
		this.name = name;
		this.isHub = isHub;
		this.countryCode = countryCode;
		this.locationCode = locationCode;
		this.arrivalList = new ArrayList<ITransport>();
		this.departureList = new ArrayList<ITransport>();
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	@Override
	public Object getKey() {
		// TODO Auto-generated method stub
		return this.key;
	}
	
	@Override
	public Object getName() {
		// TODO Auto-generated method stub
		return this.name;
	}

	@Override
	public List<ITransport> getArrivalTransportList() {
		// TODO Auto-generated method stub
		return this.arrivalList;
	}
	
	@Override
	public List<ITransport> getDepartureTransportList() {
		// TODO Auto-generated method stub
		return this.departureList;
	}

	@Override
	public PortEnum getType() {
		// TODO Auto-generated method stub
		return this.type;
	}

	@Override
	public String toString() {
		return this.key + ";" + this.name + ";" + this.countryCode + ";" + this.locationCode + ";" + this.latitude + ";" + this.longitude;
	}

	@Override
	public double getLatitude() {
		// TODO Auto-generated method stub
		return this.latitude;
	}

	@Override
	public double getLongitude() {
		// TODO Auto-generated method stub
		return this.longitude;
	}

	@Override
	public String getCountryCode() {
		// TODO Auto-generated method stub
		return this.countryCode;
	}

	@Override
	public String getLocationCode() {
		// TODO Auto-generated method stub
		return this.locationCode;
	}

	@Override
	public boolean isHub() {
		// TODO Auto-generated method stub
		return this.isHub;
	}

	@Override
	public void setHubInfo(boolean isHub) {
		// TODO Auto-generated method stub
		this.isHub = isHub;
	}
}
