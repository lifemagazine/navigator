package com.lifemagazine.navigator.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lifemagazine.navigator.entity.DbManagerImpl;
import com.lifemagazine.navigator.exception.RoutingErrorException;
import com.lifemagazine.navigator.port.IPort;
import com.lifemagazine.navigator.route.IRoute;
import com.lifemagazine.navigator.route.IRoutingRule;
import com.lifemagazine.navigator.route.ITransport2Port;
import com.lifemagazine.navigator.route.RouteImpl;
import com.lifemagazine.navigator.route.RoutingRuleImpl;
import com.lifemagazine.navigator.route.Transport2PortImpl;
import com.lifemagazine.navigator.session.ISession;
import com.lifemagazine.navigator.sorter.ISorter;
import com.lifemagazine.navigator.transport.ITransport;
import com.lifemagazine.navigator.transport.TransEnum;

public class SearchRouteImpl implements ISearchRoute {
	
	private Logger LOG = LoggerFactory.getLogger(SearchRouteImpl.class);

	private IPort sourcePort;
	private IPort destinationPort;
	private int transportCountLimit;
	private double requiredTimeLimit;
	private double costLimit;
	private ISession session;
	private List<IRoute> routeList;
	private Map<String, IRoute> routeMap;
	private IRoutingRule routingRule;
	
	public SearchRouteImpl(ISession session, IPort sourcePort, IPort destinationPort,
			int transportCountLimit, double requiredTimeLimit, double costLimit) {
		this.session = session;
		this.routeList = new ArrayList<IRoute>();
		this.routeMap = new HashMap<String, IRoute>();
		this.sourcePort = sourcePort;
		this.destinationPort = destinationPort;
		this.transportCountLimit = transportCountLimit;
		this.requiredTimeLimit = requiredTimeLimit;
		this.costLimit = costLimit;
	}
	
	@Override
	public void searchPath() throws RoutingErrorException {
		// TODO Auto-generated method stub
		
		IRoute route = new RouteImpl(this.transportCountLimit, this.requiredTimeLimit, this.costLimit, this.destinationPort);
		
		ITransport2Port t2port = new Transport2PortImpl(null, sourcePort); 
		try {
//			this.visitedTransportMap.put(t2port.getTransport().getKey().toString(), t2port.getTransport());
//			this.visitedPortMap.put(t2port.getDestinationPort().getKey().toString(), t2port.getDestinationPort());
			this.routingRule = new RoutingRuleImpl(this.sourcePort, this.destinationPort);
			route.push(t2port);
		} catch (RoutingErrorException e) {
			// TODO Auto-generated catch block
			LOG.error("", e);
		}
		
		doDetectPath(route);
		
		this.session.addSearchRoute(this);
	}
	
	int index = 0;
	
	private boolean doDetectPath(IRoute route) throws RoutingErrorException {
		/*try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		
		if (LOG.isDebugEnabled()) LOG.debug(route.toString());
		
		
		List<ITransport> departureList = this.routingRule.filterDepartureTransportList(route);
		if (LOG.isDebugEnabled()) {
			IPort lastPort = route.get(route.size()-1).getDestinationPort();
			StringBuilder sb = new StringBuilder();
			sb.append("---- From [" + lastPort.getName() + "] departureList.size(): " + departureList.size() + " ==> ");
			int idx = 0;
			for (ITransport transport: departureList) {
				if (idx > 0)
					sb.append(",");
				idx++;
				sb.append("[").append(transport.getDestinationPort().getName()).append(":").append(transport.getKey()).append("]");
			}
			LOG.debug(sb.toString());
		}
		
		/*for (ITransport transport: departureList) {
			if (this.destinationPort.getKey().toString().equals(transport.getDestinationPort().getKey().toString())) {
				LOG.debug(route.toString() + " ===> find destination port: " + transport.getDestinationPort().getName());
				ITransport2Port newTransport2Port = new Transport2PortImpl(transport, transport.getDestinationPort());
				boolean result = false;
				try {
					if (route.isPushable(newTransport2Port)) {
						route.push(newTransport2Port);
						LOG.debug(route.toString() + " ===> find destination port: " + transport.getDestinationPort().getName() + " $$$$$ pushed to route");
						IRoute cloneRoute = route.cloneCurrentRoute();
//						this.routeList.add(cloneRoute);
						addDetectedRoute(cloneRoute);
						LOG.debug(route.toString() + " ===> find destination port: " + transport.getDestinationPort().getName() + " $$$$$ addDetectedRoute");
//						LOG.info("detected the right route:" + cloneRoute.toString());
						result = true;
					}
				} catch (RoutingErrorException e) {
					// TODO Auto-generated catch block
					System.out.println(e.getMessage());
					LOG.debug("", e);
				} finally {
					if (result && route.isPopable()) {
						ITransport2Port removedT2P;
						try {
							removedT2P = route.pop();
							LOG.debug("---- removed " + removedT2P.getDestinationPort().getName());
						} catch (RoutingErrorException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
//					if (result && route.size() > 2) {
//						return true;
//					}
				}
			}
		}*/
		
		for (ITransport transport: departureList) {
			ITransport2Port newTransport2Port = new Transport2PortImpl(transport, transport.getDestinationPort()); 
			boolean isSkipped = false;
			boolean result = false;
			try {
				if (newTransport2Port.getDestinationPort().getKey().toString().equals(this.destinationPort.getKey().toString())
						&& route.isPushable(newTransport2Port)) {
					route.push(newTransport2Port);
					IRoute cloneRoute = route.cloneCurrentRoute();
					addDetectedRoute(cloneRoute);
					result = true;
				} else {
					if (!this.routingRule.isValidRoute(route, newTransport2Port)) {
//						LOG.debug("---- removed: visited " + newTransport2Port.getTransport().getKey() + ";" + newTransport2Port.getDestinationPort().getKey());
						isSkipped = true;
					} else if (!route.isPushable(newTransport2Port)) {
//						LOG.debug("---- removed: cannot pushable " + newTransport2Port.getTransport().getKey() + ";" + newTransport2Port.getDestinationPort().getKey());
						isSkipped = true;
					} else {
						route.push(newTransport2Port);
						isSkipped = false;
//						if (doDetectPath(route)) {
//							this.routeList.add(route.cloneCurrentRoute());
//						}
						doDetectPath(route);
					}	
				}
				
			} catch (RoutingErrorException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
				LOG.debug("", e);
			} finally {
				if (!isSkipped) {
					if (route.isPopable()) {
						ITransport2Port removedT2P;
						try {
							removedT2P = route.pop();
							LOG.debug("---- removed " + removedT2P.getDestinationPort().getName());
						} catch (RoutingErrorException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
//						if (result) 
//							return true;
					}
				}
			}
		}
		
		return false;
	}
	
	private void addDetectedRoute(IRoute cloneRoute) {
		String key = cloneRoute.toString();
		if (!this.routeMap.containsKey(key)) {
			this.routeMap.put(key, cloneRoute);
			this.routeList.add(cloneRoute);
			LOG.info("detected the right route:" + cloneRoute.toString());
		}
	}
	
	/*private boolean isVisited(ITransport2Port t2port) {
		if (this.visitedTransportMap.containsKey(t2port.getTransport().getKey().toString()) || 
				this.visitedPortMap.containsKey(t2port.getDestinationPort().getKey().toString())) {
			return true;
		} else {
			this.visitedTransportMap.put(t2port.getTransport().getKey().toString(), t2port.getTransport());
			this.visitedPortMap.put(t2port.getDestinationPort().getKey().toString(), t2port.getDestinationPort());
			return false;
		}
	}*/

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
	public int getLimitTransportCount() {
		// TODO Auto-generated method stub
		return this.transportCountLimit;
	}

	@Override
	public double getLimitRequiredTime() {
		// TODO Auto-generated method stub
		return this.requiredTimeLimit;
	}

	@Override
	public double getLimitCost() {
		// TODO Auto-generated method stub
		return this.costLimit;
	}

	@Override
	public List<IRoute> getResultRouteList() {
		// TODO Auto-generated method stub
		return this.routeList;
	}

	@Override
	public List<IRoute> doSort(ISorter sorter) {
		// TODO Auto-generated method stub
		return sorter.doSort(this.routeList);
	}
	
}
