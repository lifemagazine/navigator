package com.lifemagazine.navigator.rest;

import java.util.List;

public class Transport {

	private String id;
	private String name;
	private String type;
	private String sourcePort;
	private String destinationPort;
	private List<String> scheduleList;
	private String cycle;
	private double cost;
	private double requiredTime;
	private double distance;
	private String status;
	
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
	public String getSourcePort() {
		return sourcePort;
	}
	public void setSourcePort(String sourcePort) {
		this.sourcePort = sourcePort;
	}
	public String getDestinationPort() {
		return destinationPort;
	}
	public void setDestinationPort(String destinationPort) {
		this.destinationPort = destinationPort;
	}
	public List<String> getScheduleList() {
		return scheduleList;
	}
	public void setScheduleList(List<String> scheduleList) {
		this.scheduleList = scheduleList;
	}
	public String getCycle() {
		return cycle;
	}
	public void setCycle(String cycle) {
		this.cycle = cycle;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public double getRequiredTime() {
		return requiredTime;
	}
	public void setRequiredTime(double requiredTime) {
		this.requiredTime = requiredTime;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
