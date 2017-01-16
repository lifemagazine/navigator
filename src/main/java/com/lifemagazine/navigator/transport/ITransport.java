package com.lifemagazine.navigator.transport;

import java.util.BitSet;
import java.util.List;

import com.lifemagazine.navigator.exception.DbManagerNotReadyException;
import com.lifemagazine.navigator.port.IPort;

public interface ITransport {

	public Object getKey();
	public String getName();
	public IPort getSourcePort();
	public IPort getDestinationPort();
	public TransEnum getType();
	public List<String> getScheduleList();
	public BitSet getCycle();
	public void setCycle(BitSet bSet);
	public double getRequiredTime();
	public double getCost();
	public void recalculateCost();
	public double getDistance();
	public void setRequiredTime(double time);
	public StatusEnum getStatus();
	public void setStatus(StatusEnum statusEnum, BitSet bSet, double requiredTime);
	public String toString();
}
