package com.lifemagazine.navigator.transport;

public enum StatusEnum {
	RUNNABLE(11), PAUSED(21), UNUSUAL(31), CLOSURE(41), UNKNOWN(51);
	private int code;
	private StatusEnum(int code) {
		this.code = code;
	}
	public int getCode() {
		return this.code;
	}
}
