package com.ahwers.rest.services;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.ahwers.marvin.Marvin;
import com.ahwers.marvin.command.Command;
import com.ahwers.marvin.response.MarvinResponse;

@Path("/command")
public class MarvinService {
	
	private Marvin marvin;
	
	public MarvinService() {
		marvin = new Marvin();
	}

	@GET
	@Produces("application/json")
	// TODO: Implement a Response builder method that depends on the request status of the MarvinResponse
	public Response command() {
		MarvinResponse marvinResponse = marvin.processCommand("test test test");
		
		return Response.ok(marvinResponse).build();
	}
	
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public MarvinResponse command(Command command) {
		return marvin.processCommand(command.getCommand());
	}
	
	@POST
	@Consumes("text/plain")
	@Produces("application/json")
	public MarvinResponse command(String command) {
		return marvin.processCommand(command);
	}

}
