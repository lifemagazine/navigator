package com.lifemagazine.navigator.sorter;

public enum SortTypeEnum {

	MIN_COST(11), MIN_TIME(21);
	private int code;
	private SortTypeEnum(int code) {
		this.code = code;
	}
	public int getCode() {
		return this.code;
	}
}
