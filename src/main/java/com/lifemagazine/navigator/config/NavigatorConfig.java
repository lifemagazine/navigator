package com.lifemagazine.navigator.config;

import java.util.HashMap;
import java.util.Map;

import com.lifemagazine.navigator.exception.DbManagerNotReadyException;
import com.lifemagazine.navigator.rest.Cost;
import com.lifemagazine.navigator.transport.TransEnum;

public class NavigatorConfig {
	
	private static final String DEFAULT_KEY = "DEFAULT_KEY";
	
	private static NavigatorConfig config = null;
	
	static {
		config = new NavigatorConfig();
	}
	
	
	
	public static NavigatorConfig get() throws DbManagerNotReadyException {
		if (config == null)
			throw new DbManagerNotReadyException("Config is not inited.");
		return config;
	}
	

//	private Map<String, Double> airCostMap = new HashMap<String, Double>();
//	private Map<String, Double> seaCostMap = new HashMap<String, Double>();
//	private Map<String, Double> roadCostMap = new HashMap<String, Double>();
//	private Map<String, Double> railCostMap = new HashMap<String, Double>();
	private double airCost;
	private double seaCost;
	private double roadCost;
	private double railCost;
	
	private NavigatorConfig() {
//		airCostMap.put(DEFAULT_KEY, (double) 0);
//		seaCostMap.put(DEFAULT_KEY, (double) 0);
//		roadCostMap.put(DEFAULT_KEY, (double) 0);
//		railCostMap.put(DEFAULT_KEY, (double) 0);
	}
	
	public void setCost(TransEnum transEnum, double cost) {
		if (transEnum == TransEnum.AIRCRAFT) airCost = cost;
		else if (transEnum == TransEnum.VESSEL) seaCost = cost;
		else if (transEnum == TransEnum.TRUCK) roadCost = cost;
		else if (transEnum == TransEnum.TRAIN) railCost = cost;
		else {
			System.out.println("!!!!!!!!!!!!!!!!!!!!! RRROROROOROROR");
		}
	}
	
	public double getCost(TransEnum transEnum) {
		if (transEnum == TransEnum.AIRCRAFT) return airCost;
		else if (transEnum == TransEnum.VESSEL) return seaCost;
		else if (transEnum == TransEnum.TRUCK) return roadCost;
		else if (transEnum == TransEnum.TRAIN) return railCost;
		else {
			System.out.println("!!!!!!!!!!!!!!!!!!!!! RRROROROOROROR");
			return 0;
		}
	}
	
	public void setCost(TransEnum transEnum, String key, double cost) {
		if (transEnum == TransEnum.AIRCRAFT) airCost = cost;
		else if (transEnum == TransEnum.VESSEL) seaCost = cost;
		else if (transEnum == TransEnum.TRUCK) roadCost = cost;
		else if (transEnum == TransEnum.TRAIN) railCost = cost;
		else {
			System.out.println("!!!!!!!!!!!!!!!!!!!!! RRROROROOROROR");
		}
	}
	
	public double getCost(TransEnum transEnum, String key) {
		if (transEnum == TransEnum.AIRCRAFT) return airCost;
		else if (transEnum == TransEnum.VESSEL) return seaCost;
		else if (transEnum == TransEnum.TRUCK) return roadCost;
		else if (transEnum == TransEnum.TRAIN) return railCost;
		else {
			System.out.println("!!!!!!!!!!!!!!!!!!!!! RRROROROOROROR");
			return 0;
		}
	}
	
	public Cost getCostInfo() {
		Cost cost = new Cost();
		cost.setAir(getCost(TransEnum.AIRCRAFT));
		cost.setSea(getCost(TransEnum.VESSEL));
		cost.setRoad(getCost(TransEnum.TRUCK));
		cost.setRail(getCost(TransEnum.TRAIN));
		return cost;
	}
	
	public void setCostInfo(Cost cost) {
		setCost(TransEnum.AIRCRAFT, cost.getAir());
		setCost(TransEnum.VESSEL, cost.getSea());
		setCost(TransEnum.TRUCK, cost.getRoad());
		setCost(TransEnum.TRAIN, cost.getRail());
	}
	
}
