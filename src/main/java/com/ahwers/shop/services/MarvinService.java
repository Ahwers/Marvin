package com.ahwers.shop.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.ahwers.marvin.Marvin;
import com.ahwers.marvin.ResourceRepresentationType;

@Path("/command")
public class MarvinService {
	
	@GET
	@Produces("text/html")
	public String command() {
		Marvin marvin = new Marvin();
		return marvin.processCommand("plot y = x ^ 2 + (3x + 5)/x").getResource().getRepresentation(ResourceRepresentationType.HTML);
	}

}
