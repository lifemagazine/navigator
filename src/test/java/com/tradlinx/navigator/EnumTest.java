package com.tradlinx.navigator;

import com.lifemagazine.navigator.port.PortEnum;

public class EnumTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String type = PortEnum.AIRPORT.toString();
		System.out.println(type);
		
		print(PortEnum.valueOf(type));
	}

	private static void print(PortEnum portEnum) {
		if (portEnum == PortEnum.AIRPORT)
			System.out.println("go airport");
		else
			System.out.println("go others");
	}
}
