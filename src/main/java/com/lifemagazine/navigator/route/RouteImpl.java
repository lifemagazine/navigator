package com.lifemagazine.navigator.route;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lifemagazine.navigator.entity.DbManagerImpl;
import com.lifemagazine.navigator.exception.DbManagerNotReadyException;
import com.lifemagazine.navigator.exception.RoutingErrorException;
import com.lifemagazine.navigator.port.IPort;
import com.lifemagazine.navigator.search.SearchRouteImpl;
import com.lifemagazine.navigator.transport.ITransport;
import com.lifemagazine.navigator.transport.TransEnum;
import com.lifemagazine.navigator.util.Utility;

public class RouteImpl implements IRoute {
	
	private Logger LOG = LoggerFactory.getLogger(RouteImpl.class);
	
	private final int limitTransportCount;
	private final double limitTotalCost;
	private final double limitTotalRequiredTime;
	
//	private Map<Object, ITransport> visitedTransportMap;
//	private Map<Object, IPort> visitedPportMap;
	private Stack<ITransport2Port> stack;
//	private LinkedList<ITransport> transportList;
//	private LinkedList<IPort> portList;
	
//	private double totalCost;
//	private double totalRequiredTime;
	private boolean isValid;
	private IPort destinationPort;
	
	
	public RouteImpl(int limitTransportCount, double limitTotalRequiredTime, double limitTotalCost, IPort destinationPort) {
//		this.visitedTransportMap = new HashMap<Object, ITransport>();
//		this.visitedPportMap = new HashMap<Object, IPort>();
		this.stack = new Stack<ITransport2Port>();
//		this.transportList = new LinkedList<ITransport>();
//		this.portList = new LinkedList<IPort>();
		this.isValid = true;
		this.limitTransportCount = limitTransportCount;
		this.limitTotalCost = limitTotalCost;
		this.limitTotalRequiredTime = limitTotalRequiredTime;
		this.destinationPort = destinationPort;
	}
	
	public RouteImpl(Stack<ITransport2Port> stack, int limitTransportCount, double limitTotalRequiredTime, double limitTotalCost, IPort destinationPort) {
//		this.visitedTransportMap = new HashMap<Object, ITransport>();
//		this.visitedPportMap = new HashMap<Object, IPort>();
		this.stack = stack;
//		this.transportList = new LinkedList<ITransport>();
//		this.portList = new LinkedList<IPort>();
		this.isValid = false;
		this.limitTransportCount = limitTransportCount;
		this.limitTotalCost = limitTotalCost;
		this.limitTotalRequiredTime = limitTotalRequiredTime;
		this.destinationPort = destinationPort;
		
//		calculate();
	}
	
	/*private void calculate() {
		for (int i=0; i<this.stack.size(); i++) {
			ITransport2Port t2p = this.stack.get(i);
			ITransport transport = t2p.getTransport();
			if (transport != null) {
				this.totalCost += transport.getCost();
				this.totalRequiredTime += transport.getRequiredTime();
			}
		}
	}*/
	
	/*@Override
	public boolean isVisited(ITransport2Port t2port) {
		if (t2port.getTransport() == null)
			return false;
		
		ITransport2Port transport2Port = null;
		ITransport transport = null;
		IPort port = null;
		for (int i=0; i<this.stack.size(); i++) {
			transport2Port = this.stack.get(i);
			
			port = transport2Port.getDestinationPort();
			if (t2port.getDestinationPort().getKey().toString().equals(port.getKey().toString()))
				return true;
			
			transport = transport2Port.getTransport();
			if (transport != null && t2port.getTransport().getKey().toString().equals(transport.getKey().toString()))
				return true;
		}
		
//		if (this.visitedTransportMap.containsKey(t2port.getTransport().getKey())) {
//			return true;
//		}
//		
//		if (this.visitedPportMap.containsKey(t2port.getDestinationPort().getKey())) {
//			return true;
		}
		
		return false;
	}*/
	
	@Override
	public boolean isPushable(ITransport2Port t2port) {
		if (this.stack.size() < this.limitTransportCount + 1) {
			try {
				precheckValidation(t2port);
				return true;
			} catch (RoutingErrorException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
				LOG.info("RoutingErrorException", e.getMessage());
				return false;
			} catch (DbManagerNotReadyException e) {
				LOG.error("DbManagerNotReadyException", e.getMessage());
				return false;
			}
		} else {
			return false;
		}
	}
	
	@Override
	public boolean isPopable() {
		if (this.stack.size() > 1)
			return true;
		else
			return false;
	}
	
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return this.stack.size();
	}
	
	@Override
	public ITransport2Port get(int i) {
		// TODO Auto-generated method stub
		return this.stack.get(i);
	}
	
	@Override
	public void push(ITransport2Port t2port) throws RoutingErrorException {
		// TODO Auto-generated method stub
		
		this.stack.push(t2port);
		
		/*if (t2port.getTransport() != null && this.visitedTransportMap.containsKey(t2port.getTransport().getKey())) {
			throw new RoutingErrorException("Duplicated transport: " + t2port.getTransport().getName());
		} else {
			if (t2port.getTransport() != null)
				this.visitedTransportMap.put(t2port.getTransport().getKey(), t2port.getTransport());
		}
		
		if (this.destinationPort.getKey().toString().equals(t2port.getDestinationPort().getKey().toString())) {
			this.visitedPportMap.put(t2port.getDestinationPort().getKey(), t2port.getDestinationPort());
		} else {
			if (this.visitedPportMap.containsKey(t2port.getDestinationPort().getKey())) {
				throw new RoutingErrorException("Duplicated port: " + t2port.getDestinationPort().getName());
			} else {
				this.visitedPportMap.put(t2port.getDestinationPort().getKey(), t2port.getDestinationPort());
			}
		}*/
		
		/*if (t2port.getTransport() != null) {
			this.transportList.add(t2port.getTransport());
			this.totalCost += t2port.getTransport().getCost();
			this.totalRequiredTime += t2port.getTransport().getRequiredTime();
		}*/
		
		/*if (this.portList.contains(t2port.getDestinationPort())) {
			this.portList.add(t2port.getDestinationPort());
			throw new RoutingErrorException("Duplicated port: " + t2port.getDestinationPort().getName());
		} else {
			this.portList.add(t2port.getDestinationPort());
		}*/
		
//		checkValidation(t2port.getDestinationPort());
	}

	@Override
	public ITransport2Port pop() throws RoutingErrorException {
		// TODO Auto-generated method stub
		if (this.stack.size() == 1)
			throw new RoutingErrorException("cannot remove root");
		ITransport2Port t2port = this.stack.pop();
//		if (this.stack.size() == 0) {
//			System.out.println("!!!!!!!!!!!removed " + t2port.getDestinationPort().getName() + ":" + t2port.getDestinationPort().getKey());
//		}
		try {
//			this.transportList.removeLast();
//			this.portList.removeLast();
//			this.totalCost -= t2port.getTransport().getCost();
//			this.totalRequiredTime -= t2port.getTransport().getRequiredTime();
		} catch (Exception e) {
			
		}
//		LOG.debug("[pop] " + this.transportList.size() + ", " + this.portList.size());
		return t2port;
	}
	
	@Override
	public int getTotalTransportCount() {
		// TODO Auto-generated method stub
//		return this.transportList.size();
		return this.stack.size() - 1;
	}

	@Override
	public double getTotalRequiredTime() {
		// TODO Auto-generated method stub
		double totalRequiredTime = 0;
		for (int i=0; i<this.stack.size(); i++) {
			ITransport2Port t2p = this.stack.get(i);
			ITransport transport = t2p.getTransport();
			if (transport != null) {
				if (transport.getType() == TransEnum.AIRCRAFT) {
					totalRequiredTime += Utility.convertHour2Day(transport.getRequiredTime());
				} else {
					totalRequiredTime += transport.getRequiredTime();
				}
			}
		}
		return totalRequiredTime;
	}

	@Override
	public double getTotalCost() throws DbManagerNotReadyException {
		// TODO Auto-generated method stub
		double totalCost = 0;
		for (int i=0; i<this.stack.size(); i++) {
			ITransport2Port t2p = this.stack.get(i);
			ITransport transport = t2p.getTransport();
			if (transport != null) {
				totalCost += transport.getCost();
			}
		}
		return totalCost;
	}
	
	private boolean precheckValidation(ITransport2Port t2port) throws RoutingErrorException, DbManagerNotReadyException {
		ITransport newTransport = t2port.getTransport();
		IPort newPort = t2port.getDestinationPort();
		
		for (int i=0; i<this.stack.size(); i++) {
			if (this.stack.get(i).getDestinationPort().getKey().toString().equals(newPort.getKey().toString())) {
				throw new RoutingErrorException("Already visited port. " + t2port.getDestinationPort().getName() + ": " + t2port.getDestinationPort().getKey());
			}
		}
		
		if (this.limitTransportCount == 0 && this.limitTotalCost == 0 && this.limitTotalRequiredTime == 0)
			return true;
		
		double totalCost = this.getTotalCost() + newTransport.getCost();
		if (totalCost > this.limitTotalCost) {
			throw new RoutingErrorException("Total costs over error. " + totalCost + " is over limitTotalCost:" + this.limitTotalCost);
		}
		
		double totalRequiredTime = this.getTotalRequiredTime() + newTransport.getRequiredTime(); 
		if (totalRequiredTime > this.limitTotalRequiredTime) {
			throw new RoutingErrorException("Total required time over error. " + totalRequiredTime + " is over limitTotalRequiredTime:" + this.limitTotalRequiredTime);
		}
		
		return true;
	}
	
	private boolean checkValidation(IPort newPort) throws RoutingErrorException, DbManagerNotReadyException {		
		if (this.limitTransportCount == 0 && this.limitTotalCost == 0 && this.limitTotalRequiredTime == 0)
			return true;
		
//		if (this.transportList.size() - 1 > this.limitTransportCount) {
		int currentTransport = this.stack.size() - 2;
		if (currentTransport > this.limitTransportCount) {
			throw new RoutingErrorException("Total transport count over error. " + currentTransport + " is over limitTransportCount:" + this.limitTransportCount);
		}
		
		double totalCost = this.getTotalCost();
		if (totalCost > this.limitTotalCost) {
			throw new RoutingErrorException("Total costs over error. " + totalCost + " is over limitTotalCost:" + this.limitTotalCost);
		}
		
		double totalRequiredTime = this.getTotalRequiredTime();
		if (totalRequiredTime > this.limitTotalRequiredTime) {
			throw new RoutingErrorException("Total required time over error. " + totalRequiredTime + " is over limitTotalRequiredTime:" + this.limitTotalRequiredTime);
		}
		
		return true;
	}
	
	@Override
	public boolean isValid() {
		return this.isValid;
	}

	@Override
	public IRoute cloneCurrentRoute() {
		// TODO Auto-generated method stub
		Stack<ITransport2Port> cloneStack = new Stack<ITransport2Port>();
		for (int i=0; i<this.stack.size(); i++) {
			cloneStack.push(this.stack.get(i));
		}
		return new RouteImpl(cloneStack, this.limitTransportCount, this.limitTotalCost, this.limitTotalRequiredTime, this.destinationPort);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<this.stack.size(); i++) {
			if (i > 0) {
				try {
					ITransport transport;
					transport = DbManagerImpl.get().getTransportByKey(stack.get(i).getTransport().getKey());
					sb.append("--").append(transport.getKey()).append(":").append(transport.getName()).append("-->");
				} catch (DbManagerNotReadyException e) {
					// TODO Auto-generated catch block
					LOG.error("", e);
					sb.append("--").append(stack.get(i).getTransport().getKey()).append("-->");
				}
			}
			sb.append("[").append(stack.get(i).getDestinationPort().getName()).append("]");
		}
		return sb.toString();
	}
}
