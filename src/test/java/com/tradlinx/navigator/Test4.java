package com.tradlinx.navigator;

public class Test4 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println(String.format("%70s", "*"));
		System.out.println(String.format("%-70s", "*"));
		
		String deco = "**********";
		String a = "From [Seoul] To [Los Angeles]";
		String line0 = deco + String.format("%-50s", a) + deco;
		System.out.println(line0);
	}

}
