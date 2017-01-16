package com.tradlinx.navigator;

import java.util.ArrayList;
import java.util.List;

import com.lifemagazine.navigator.port.Airport;
import com.lifemagazine.navigator.port.IPort;
import com.lifemagazine.navigator.rest.Port;

public class Test2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<IPort> portList = new ArrayList<IPort>();
		IPort port = new Airport("key1", "name1", false, "KR", "ICN", 1, 1);
		portList.add(port);
		IPort port1 = new Airport("key1", "name1", false, "KR", "ICN", 1, 1);
		
		System.out.println(portList.contains(port1));
	}

}
