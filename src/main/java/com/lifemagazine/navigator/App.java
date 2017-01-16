package com.lifemagazine.navigator;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lifemagazine.navigator.entity.DbManagerImpl;
import com.lifemagazine.navigator.exception.DbManagerNotReadyException;
import com.lifemagazine.navigator.exception.RoutingErrorException;
import com.lifemagazine.navigator.port.IPort;
import com.lifemagazine.navigator.rest.NavigatorController;
import com.lifemagazine.navigator.rest.NavigatorService;
import com.lifemagazine.navigator.route.IRoute;
import com.lifemagazine.navigator.route.ITransport2Port;
import com.lifemagazine.navigator.search.ISearchRoute;
import com.lifemagazine.navigator.search.SearchRouteImpl;
import com.lifemagazine.navigator.session.ISession;
import com.lifemagazine.navigator.session.SessionImpl;
import com.lifemagazine.navigator.sorter.SortTypeEnum;
import com.lifemagazine.navigator.sorter.SorterImpl;

/**
 * Hello world!
 *
 */
public class App {
	
	private static Logger LOG = LoggerFactory.getLogger(App.class);
	
	private static int port = 4567;
	
	public static void main(String[] args) throws Exception {
//		test();
		startDatabase();
		startRestServer(port);
	}
	
	private static void startDatabase() throws DbManagerNotReadyException {
		String dbFilePath = "db/navigatordb";
		String insertDataFilePath = "config/testdata1.txt";
		DbManagerImpl.init(dbFilePath, insertDataFilePath);
		DbManagerImpl.get().setHubPort();
		
		LOG.debug("*************************************************");
		LOG.debug("*************************************************");
	}
	
	private static void startRestServer(int port) throws Exception {
		if (port < 1)
			port = 4567;
		
		String origin = "*";
		String method = "GET,PUT,POST,DELETE,OPTIONS";
		String headers = "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,";
		NavigatorController.enableCORS(origin, method, headers);
		
		new NavigatorController(new NavigatorService());
	}

	public static void test() throws DbManagerNotReadyException, RoutingErrorException {
		String dbFilePath = "db/navigatordb";
		String insertDataFilePath = "config/testdata1.txt";
		DbManagerImpl.init(dbFilePath, insertDataFilePath);
		DbManagerImpl.get().setHubPort();
		
		LOG.debug("*************************************************");
		LOG.debug("*************************************************");
		
		ISession session = new SessionImpl("localhost");
		int transportCountLimit = 5;
		double requiredTimeLimit = 30;
		double costLimit = 200000000;
		detectPath(session, transportCountLimit, requiredTimeLimit, costLimit, SortTypeEnum.MIN_COST, 0);
		
		DbManagerImpl.get().close();
	}
	
	private static void detectPath(ISession session, 
			int transportCountLimit, double requiredTimeLimit, double costLimit, 
			SortTypeEnum sortType, int resultListCount) throws DbManagerNotReadyException, RoutingErrorException {
		
		IPort sourcePort = DbManagerImpl.get().getPortByKey("AIR4");
		IPort destinationPort = DbManagerImpl.get().getPortByKey("AIR10");
		
		LOG.info("Detect from [" + sourcePort.getName() + "] to [" + destinationPort.getName() + "]");
		
		ISearchRoute searchRoute = new SearchRouteImpl(session, sourcePort, destinationPort, transportCountLimit, requiredTimeLimit, costLimit);
		searchRoute.searchPath();
		searchRoute.doSort(new SorterImpl(sortType, resultListCount));
		
		List<ISearchRoute> searchRouteList = session.getSearchRouteList();
		LOG.info("result: " + searchRouteList.size());
		for (ISearchRoute resultSearchRoute: searchRouteList) {
			List<IRoute> list = resultSearchRoute.getResultRouteList();
			LOG.info("sub result: " + list.size());
			int index = 0;
			for (IRoute route: list) {
				StringBuilder sb = new StringBuilder(++index + "th: ");
				for (int i=0; i<route.size(); i++) {
					ITransport2Port t2p = route.get(i);
					if (t2p.getTransport() != null) {
						sb.append("->").append(t2p.getTransport().getKey().toString()).append("->");
					}
					sb.append("[").append(t2p.getDestinationPort().getName()).append("]");
				}
				sb.append(" => time: ").append(route.getTotalRequiredTime()).append(", cost: ").append(route.getTotalCost());
				LOG.info(sb.toString());
			}
		}
	}
	
}
