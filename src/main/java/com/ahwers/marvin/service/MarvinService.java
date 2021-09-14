package com.ahwers.marvin.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.ahwers.marvin.framework.Marvin;
import com.ahwers.marvin.framework.application.ActionInvocation;
import com.ahwers.marvin.framework.application.ApplicationAction;
import com.ahwers.marvin.framework.command.Command;
import com.ahwers.marvin.framework.response.MarvinResponse;
import com.ahwers.marvin.framework.response.RequestOutcome;

@Path("/command")
public class MarvinService {
	
	private Marvin marvin;
	
	public MarvinService() {
		marvin = new Marvin();
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public Response command() {
		return Response.ok("worked!").build();
	}
	
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response command(Command command) {
		String commandText = command.getCommand();
		return command(commandText);
	}
	
	@POST
	@Consumes("text/plain")
	@Produces("application/json")
	public Response command(String command) {
		MarvinResponse marvinResponse = marvin.processCommand(command);
		Response serviceResponse = constructServiceResponseFromMarvinResponse(marvinResponse);
		return serviceResponse;
	}

	@POST
	@Path("/execute")
	@Consumes("application/json")
	@Produces("application/json")
	public Response executeActionInvocation(ActionInvocation action) {
		MarvinResponse marvinResponse = marvin.processActionInvocation(action);
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
		else if (requestOutcome.equals(RequestOutcome.CONFLICTED)) {
			builder = Response.status(Response.Status.CONFLICT);
		}
		else if (requestOutcome.equals(RequestOutcome.UNMATCHED)) {
			builder = Response.status(Response.Status.NOT_FOUND);
		}
	
		Response response = builder.entity(marvinResponse).build();
		
		return response;
	}
	
}
