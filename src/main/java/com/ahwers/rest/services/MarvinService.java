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
import javax.ws.rs.core.Response.ResponseBuilder;

import com.ahwers.marvin.Marvin;
import com.ahwers.marvin.command.Command;
import com.ahwers.marvin.response.MarvinResponse;
import com.ahwers.marvin.response.RequestOutcome;

@Path("/command")
public class MarvinService {
	
	private Marvin marvin;
	
	public MarvinService() {
		marvin = new Marvin();
	}

	@GET
	@Produces("application/json")
	public Response command() {
		MarvinResponse marvinResponse = marvin.processCommand("test test test");
		Response serviceResponse = constructServiceResponseFromMarvinResponse(marvinResponse);
		return serviceResponse;
	}
	
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response command(Command command) {	
		MarvinResponse marvinResponse = marvin.processCommand(command.getCommand());
		Response serviceResponse = constructServiceResponseFromMarvinResponse(marvinResponse);
		return serviceResponse;
	}
	
	@POST
	@Consumes("text/plain")
	@Produces("application/json")
	public Response command(String command) {		
		MarvinResponse marvinResponse = marvin.processCommand(command);
		Response serviceResponse = constructServiceResponseFromMarvinResponse(marvinResponse);
		return serviceResponse;
	}

	private Response constructServiceResponseFromMarvinResponse(MarvinResponse marvinResponse) {
		ResponseBuilder builder = null;
		
		RequestOutcome requestOutcome = marvinResponse.getCommandStatus();
		if (requestOutcome.equals(RequestOutcome.SUCCESS)) {
			builder = Response.ok();
		}
		else if (requestOutcome.equals(RequestOutcome.FAILED)) {
			builder = Response.serverError();
		}
		else if (requestOutcome.equals(RequestOutcome.INVALID) || requestOutcome.equals(RequestOutcome.OUTDATED)) {
			builder = Response.status(Response.Status.BAD_REQUEST);
		}
		else if (requestOutcome.equals(RequestOutcome.UNMATCHED)) {
			builder = Response.status(Response.Status.CONFLICT);
		}
	
		Response response = builder.entity(marvinResponse).build();
		
		return response;
	}
	
}
