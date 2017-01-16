package com.lifemagazine.navigator.route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lifemagazine.navigator.exception.RoutingErrorException;
import com.lifemagazine.navigator.port.IPort;
import com.lifemagazine.navigator.port.PortEnum;
import com.lifemagazine.navigator.search.SearchRouteImpl;
import com.lifemagazine.navigator.transport.ITransport;
import com.lifemagazine.navigator.transport.TransEnum;

public class RoutingRuleImpl implements IRoutingRule {

	private Logger LOG = LoggerFactory.getLogger(RoutingRuleImpl.class);

	private IPort sourcePort;
	private IPort destinationPort;
	//	private Map<String, ITransport2Port> visitedMap = new HashMap<String, ITransport2Port>();
	//	private Map<String, ITransport> visitedTransportMap = new HashMap<String, ITransport>();
	//	private Map<String, IPort> visitedPortMap = new HashMap<String, IPort>();

	public RoutingRuleImpl(IPort sourcePort, IPort destinationPort) {
		this.sourcePort = sourcePort;
		this.destinationPort = destinationPort;
	}

	@Override
	public List<ITransport> filterDepartureTransportList(IRoute route) throws RoutingErrorException {

		IPort lastPort = route.get(route.size()-1).getDestinationPort();

		List<ITransport> departureList = new ArrayList<ITransport>();

		/*if (route.size() == 1) {
			for (ITransport transport: lastPort.getDepartureTransportList()) {
				if (transport.getType() == TransEnum.VESSEL) {
					if (this.destinationPort.getKey().toString().equals(transport.getDestinationPort().getKey().toString())) {
						departureList.add(transport);
					}
				} else {
					departureList.add(transport);
				}
			}
		} else if (route.size() > 1) {
			for (ITransport transport: lastPort.getDepartureTransportList()) {
				if (transport.getType() == TransEnum.AIRCRAFT || transport.getType() == TransEnum.VESSEL) {
					if (this.destinationPort.getKey().toString().equals(transport.getDestinationPort().getKey().toString())) {
						departureList.add(transport);
					}
				} else {
					departureList.add(transport);
				}
			}
		} else {
			throw new RoutingErrorException("Route is empty error!");
		}*/

		
//		boolean isLast = false;
		for (ITransport transport: lastPort.getDepartureTransportList()) {
			if (!transport.getDestinationPort().isHub() && this.destinationPort.getKey().toString().equals(transport.getDestinationPort().getKey().toString())) {
//				isLast = true;
				departureList.add(transport);
//				return departureList;
			}
		}

//		if (isLast) {
//			for (ITransport transport: lastPort.getDepartureTransportList()) {
//				departureList.add(transport);
//			}
//		} else {
			if (route.size() > 1) {
//				List<String> countryList = getCountryList(route);
				for (ITransport transport: lastPort.getDepartureTransportList()) {
					if (transport.getDestinationPort().isHub()) {
						departureList.add(transport);
					}
					/*if (checkCountryOverlapValidation(countryList, transport)) {
						if (transport.getDestinationPort().getType() == PortEnum.ROADPORT && lastPort.getType() != PortEnum.ROADPORT) {
							departureList.add(transport);
						} else {
							if (transport.getDestinationPort().isHub()) {
								departureList.add(transport);
							}
						}
					}*/
				}
			} else {
				for (ITransport transport: lastPort.getDepartureTransportList()) {
					/*if (transport.getDestinationPort().getType() == PortEnum.ROADPORT && lastPort.getType() != PortEnum.ROADPORT) {
						departureList.add(transport);
					} else {
						if (transport.getDestinationPort().isHub()) {
							departureList.add(transport);
						}
					}*/
					if (transport.getDestinationPort().isHub()) {
						departureList.add(transport);
					}
				}
			}
//		}

		return departureList;
	}
	
	private List<String> getCountryList(IRoute route) {
		List<String> countryList = new ArrayList<String>();
		for (int i=0; i<route.size(); i++) {
			countryList.add(route.get(i).getDestinationPort().getCountryCode());
		}
		return countryList;
	}
	
	private boolean checkCountryOverlapValidation(List<String> countryList, ITransport transport) {
		int sameCount = 0;
		for (int i=0; i<countryList.size(); i++) {
			if (countryList.get(i).equals(transport.getDestinationPort().getCountryCode())) {
				sameCount++;
				if (countryList.size() - 1 - i > 0) {
					return false;
				}
			}
		}
		if (sameCount > 1) 
			return false;
		else
			return true;
	}
	
	@Override
	public boolean isValidRoute(IRoute route, ITransport2Port newT2Port) {
		// TODO Auto-generated method stub

		if (this.sourcePort.getKey().toString().equals(newT2Port.getDestinationPort().getKey().toString())) {
			//			LOG.debug("---- invalid - source port same: " + newT2Port.getTransport().getKey() + ";" + newT2Port.getDestinationPort().getKey());
			return false;
		}


		for (int i=0; i<route.size(); i++) {
			IPort port = route.get(i).getDestinationPort();
			if (port.getKey().toString().equals(newT2Port.getDestinationPort().getKey().toString())) {
				return false;
			}
		}

		/*String key = newT2Port.getTransport() == null ? "null" : newT2Port.getTransport().getKey().toString() + ":" + newT2Port.getDestinationPort().getKey().toString();
		if (this.visitedMap.containsKey(key)) {
			LOG.debug("---- invalid - already visited : " + newT2Port.getTransport().getKey() + ";" + newT2Port.getDestinationPort().getKey());
			return false;
		} else {
			this.visitedMap.put(key, newT2Port);
		}*/

		if (route.size() > 1) {
			ITransport2Port lastT2Port = route.get(route.size() - 1);

			// check Truck2Truck prohibition
			if (lastT2Port.getTransport().getType() == TransEnum.TRUCK &&
					newT2Port.getTransport().getType() == TransEnum.TRUCK) {
				//LOG.debug("---- invalid - Truck2Truck: " + newT2Port.getTransport().getKey() + ";" + newT2Port.getDestinationPort().getKey());
				return false;
			}

			// check escaping from the country of the destination
			if (this.destinationPort.getCountryCode().equals(lastT2Port.getDestinationPort().getCountryCode()) &&
					!this.destinationPort.getCountryCode().equals(newT2Port.getDestinationPort().getCountryCode())) {
				//LOG.debug("---- invalid - destination port same: " + newT2Port.getTransport().getKey() + ";" + newT2Port.getDestinationPort().getKey());
				return false;
			}
		} 


		// check 3 times transport in same country
		if (route.size() >= 3) {
			String lastPortCountry = route.get(route.size() - 1).getDestinationPort().getCountryCode();
			String second2LastPortCountry = route.get(route.size() - 2).getDestinationPort().getCountryCode();
			String third2LastPortCountry = route.get(route.size() - 3).getDestinationPort().getCountryCode();
			if (lastPortCountry.equals(second2LastPortCountry) &&
					lastPortCountry.equals(second2LastPortCountry) &&
					lastPortCountry.equals(third2LastPortCountry) &&
					lastPortCountry.equals(newT2Port.getDestinationPort().getCountryCode()))
				//LOG.debug("---- invalid - 3 times transport in same country: " + newT2Port.getTransport().getKey() + ";" + newT2Port.getDestinationPort().getKey());
				return false;
		}


		return true;
	}

}
