package com.lifemagazine.navigator.entity;

import java.util.List;
import java.util.Map;

import com.lifemagazine.navigator.exception.InvalidPortTypeException;
import com.lifemagazine.navigator.exception.InvalidTransportTypeException;
import com.lifemagazine.navigator.port.IPort;
import com.lifemagazine.navigator.port.PortEnum;
import com.lifemagazine.navigator.transport.ITransport;
import com.lifemagazine.navigator.transport.TransEnum;

public interface IDbManager {

	public void loadDatabase(String filePath, String insertDataFilePath);
	public void setHubPort();
	public Map<String, Integer> getHubPortMap();
	public void resetHubPortByCount(int airHubBranchNum, int seaHubBranchNum, int roadHubBranchNum, int railHubBranchNum);
	public void resetHubPortByKey(List<String> keyList);
	public List<IPort> addHubPortByKey(List<String> keyList);
	public List<IPort> removeHubPortByKey(List<String> keyList);
	public void recalculateCost();
	public void close();
	
	public Object createPortKey(PortEnum portEnum);
	public List<IPort> getAllPort();
	public void addPort(IPort port) throws InvalidPortTypeException;
	public IPort getPortByKey(Object key);
	public List<IPort> getPortByName(String name);
	public List<IPort> getPortByType(PortEnum portEnum);
	public IPort removePort(IPort port);
	public IPort updatePort(IPort port);
	
	public Object createTransportKey(TransEnum transEnum);
	public List<ITransport> getAllTransport();
	public void addTransport(ITransport transport) throws InvalidTransportTypeException;
	public ITransport getTransportByKey(Object key);
	public List<ITransport> getTransportByType(TransEnum transEnum);
	public ITransport removeTransport(ITransport transport);
	public ITransport updateTransport(ITransport transport);
	
}
