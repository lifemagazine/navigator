package com.lifemagazine.navigator.rest;

import java.util.List;

public class DetectMessage {

	private int status;
	private String message;
	private int resultCount;
	private List<List<String>> routes;
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getResultCount() {
		return resultCount;
	}
	public void setResultCount(int resultCount) {
		this.resultCount = resultCount;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<List<String>> getRoutes() {
		return routes;
	}
	public void setRoutes(List<List<String>> routes) {
		this.routes = routes;
	}
}
