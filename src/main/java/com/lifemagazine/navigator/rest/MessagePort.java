package com.lifemagazine.navigator.rest;

public class MessagePort {

	private int status;
	private String id;
	private String message;
	private Port result;
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Port getResult() {
		return result;
	}
	public void setResult(Port result) {
		this.result = result;
	}
}
