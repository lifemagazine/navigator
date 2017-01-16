package com.lifemagazine.navigator.transport;

public enum TransEnum {
	ALL(1), VESSEL(101), AIRCRAFT(201), TRUCK(301), TRAIN(310);
	private int code;
	private TransEnum(int code) {
		this.code = code;
	}
	public int getCode() {
		return this.code;
	}
}
