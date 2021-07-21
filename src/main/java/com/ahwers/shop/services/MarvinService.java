package com.ahwers.shop.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.ahwers.marvin.Marvin;
import com.ahwers.marvin.command.Command;
import com.ahwers.marvin.response.MarvinResponse;

@Path("/command")
public class MarvinService {
	
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public MarvinResponse command(Command command) {
		Marvin marvin = new Marvin();
		return marvin.processCommand(command.getCommand());
	}
	
	@POST
	@Consumes("text/plain")
	@Produces("application/json")
	public MarvinResponse command(String command) {
		Marvin marvin = new Marvin();
		return marvin.processCommand(command);
	}

}
