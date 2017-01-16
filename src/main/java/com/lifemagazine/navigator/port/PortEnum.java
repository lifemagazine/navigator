package com.lifemagazine.navigator.port;


public enum PortEnum {
	ALL(1), SEAPORT(11), AIRPORT(21), ROADPORT(31), RAILPORT(41);
	private int code;
	private PortEnum(int code) {
		this.code = code;
	}
	public int getCode() {
		return this.code;
	}
}
