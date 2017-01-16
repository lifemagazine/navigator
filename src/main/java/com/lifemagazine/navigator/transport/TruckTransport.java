package com.lifemagazine.navigator.transport;

import java.io.Serializable;
import java.util.BitSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lifemagazine.navigator.config.NavigatorConfig;
import com.lifemagazine.navigator.exception.DbManagerNotReadyException;
import com.lifemagazine.navigator.port.IPort;
import com.lifemagazine.navigator.util.Utility;

public class TruckTransport implements ITransport, Serializable {
	
	private static final long serialVersionUID = -8798205991877440851L;
	
	private Logger LOG = LoggerFactory.getLogger(TruckTransport.class);
	
	private Object key;
	private String name;
	private IPort sourcePort;
	private IPort destinationPort;
	private TransEnum transType;
	private List<String> scheduleList;
	private BitSet cycle;
	private double cost;
	private double requiredTime;
	private double distance;
	private StatusEnum status;
	
	public TruckTransport(String key, String name, IPort sourcePort, IPort destinationPort, 
			List<String> scheduleList, BitSet cycle, double requiredTime, StatusEnum status) {
		this.key = key;
		this.name = name;
		this.sourcePort = sourcePort;
		this.destinationPort = destinationPort;
		this.scheduleList = scheduleList;
		this.cycle = cycle;
		this.distance = Utility.getDistanceBetween(sourcePort.getLatitude(), sourcePort.getLongitude(), destinationPort.getLatitude(), destinationPort.getLongitude());
		try {
			this.cost = Math.round(NavigatorConfig.get().getCost(TransEnum.TRUCK) * this.distance);
		} catch (DbManagerNotReadyException e) {
			// TODO Auto-generated catch block
			LOG.error("", e);
			this.cost = 0;
		}
		this.requiredTime = requiredTime;
		this.transType = TransEnum.TRUCK;
		this.status = status;
	}
	
	@Override
	public void recalculateCost() {
		try {
			this.cost = Math.round(NavigatorConfig.get().getCost(TransEnum.TRUCK) * this.distance);
		} catch (DbManagerNotReadyException e) {
			// TODO Auto-generated catch block
			LOG.error("", e);
			this.cost = 0;
		}
	}

	@Override
	public Object getKey() {
		// TODO Auto-generated method stub
		return this.key;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}

	@Override
	public IPort getSourcePort() {
		// TODO Auto-generated method stub
		return this.sourcePort;
	}

	@Override
	public IPort getDestinationPort() {
		// TODO Auto-generated method stub
		return this.destinationPort;
	}

	@Override
	public TransEnum getType() {
		// TODO Auto-generated method stub
		return this.transType;
	}

	@Override
	public List<String> getScheduleList() {
		// TODO Auto-generated method stub
		return this.scheduleList;
	}

	@Override
	public BitSet getCycle() {
		// TODO Auto-generated method stub
		return this.cycle;
	}

	@Override
	public void setCycle(BitSet bSet) {
		// TODO Auto-generated method stub
		this.cycle = bSet;
	}
	
	@Override
	public double getCost() {
		// TODO Auto-generated method stub
		return this.cost;
	}

	@Override
	public double getRequiredTime() {
		// TODO Auto-generated method stub
		return this.requiredTime;
	}
	
	@Override
	public void setRequiredTime(double time) {
		// TODO Auto-generated method stub
		this.requiredTime = time;
	}

	@Override
	public void setStatus(StatusEnum statusEnum, BitSet bSet, double requiredTime) {
		// TODO Auto-generated method stub
		this.status = statusEnum;
		this.cycle = bSet;
		this.requiredTime = requiredTime;
	}

	@Override
	public StatusEnum getStatus() {
		// TODO Auto-generated method stub
		return this.status;
	}
	
	@Override
	public String toString() {
		return this.key + ";" + this.name + ";" + this.sourcePort + ";" + this.destinationPort + ";" + (int)this.distance + ";" + (int)this.requiredTime + ";" + (int) this.cost;
	}

	@Override
	public double getDistance() {
		// TODO Auto-generated method stub
		return this.distance;
	}
	
}
