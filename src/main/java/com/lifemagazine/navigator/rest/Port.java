package com.lifemagazine.navigator.rest;

import java.util.List;

public class Port {

	private String id;
	private String name;
	private String type;
	private String countryCode;
	private String locationCode;
	private double latitude;
	private double longitude;
	private List<String> arrival_list;
	private List<String> departure_list;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getLocationCode() {
		return locationCode;
	}
	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public List<String> getArrival_list() {
		return arrival_list;
	}
	public void setArrival_list(List<String> arrival_list) {
		this.arrival_list = arrival_list;
	}
	public List<String> getDeparture_list() {
		return departure_list;
	}
	public void setDeparture_list(List<String> departure_list) {
		this.departure_list = departure_list;
	}
}
