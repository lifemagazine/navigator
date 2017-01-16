package com.lifemagazine.navigator.rest;

import static spark.Spark.*;
import static com.lifemagazine.navigator.rest.JsonUtil.*;

public class NavigatorController {

	public NavigatorController(final NavigatorService naviService) {
		
		get("/ports", (req, res) -> naviService.getAllPorts(req, res), json());
		
		get("/ports/search", (req, res) -> naviService.getPort(req, res), json());
		
//		post("/ports", (req, res) -> naviService.addPort(req, res), json());
		
//		put("/ports/:id", (req, res) -> naviService.updatePort(req, res), json());
		
//		delete("/ports/:id", (req, res) -> naviService.removePort(req, res), json());
		
		get("/transports", (req, res) -> naviService.getAllTransports(req, res), json());
		
		get("/transports/search", (req, res) -> naviService.getTransport(req, res), json());
		
		post("/transports", (req, res) -> naviService.addTransport(req, res), json());
		
		put("/transports/:id", (req, res) -> naviService.updateTransport(req, res), json());
		
		delete("/transports/:id", (req, res) -> naviService.removeTransport(req, res), json());
		
		get("/searchroutes/routes", (req, res) -> naviService.getRoutes(req, res), json());
		
		get("/costs", (req, res) -> naviService.getCosts(req, res), json());
		
		put("/costs/:id", (req, res) -> naviService.updateCosts(req, res), json());
		
		get("/hubports", (req, res) -> naviService.getHubPorts(req, res), json());
		
		delete("/hubports/:id", (req, res) -> naviService.removeHubPort(req, res), json());
		
		put("/hubports/:id", (req, res) -> naviService.addHubPort(req, res), json());
		
		after((req, res) -> {
			res.type("application/json");
		});

		exception(IllegalArgumentException.class, (e, req, res) -> {
			res.status(400);
			res.body(toJson(new ResponseError(e)));
		});
	}
	
	// Enables CORS on requests. This method is an initialization method and should be called once.
	public static void enableCORS(final String origin, final String methods, final String headers) {

	    options("/*", (request, response) -> {

	        String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
	        if (accessControlRequestHeaders != null) {
	            response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
	        }

	        String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
	        if (accessControlRequestMethod != null) {
	            response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
	        }

	        return "OK";
	    });

	    before((request, response) -> {
	        response.header("Access-Control-Allow-Origin", origin);
	        response.header("Access-Control-Request-Method", methods);
	        response.header("Access-Control-Allow-Headers", headers);
	        // Note: this may or may not be necessary in your particular application
	        response.type("application/json");
	    });
	}
}
