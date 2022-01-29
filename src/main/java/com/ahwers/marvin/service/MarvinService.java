package com.ahwers.marvin.service;

import java.util.Map;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.ahwers.marvin.framework.Marvin;
import com.ahwers.marvin.framework.application.Application;
import com.ahwers.marvin.framework.application.ApplicationRepository;
import com.ahwers.marvin.framework.application.action.ActionInvocation;
import com.ahwers.marvin.framework.application.state.ApplicationState;
import com.ahwers.marvin.framework.application.state.ApplicationStateFactory;
import com.ahwers.marvin.framework.command.Command;
import com.ahwers.marvin.framework.response.MarvinResponse;
import com.ahwers.marvin.framework.response.RequestOutcome;

@Path("/command")
public class MarvinService {

	private final String APPLICATION_STATES_HEADER_KEY = "application_states";

	private Marvin marvin;
	
	// TODO: This instantiates the standard LIVE applications into Marvin, can we (maybe with maven) detect when we are in a DEV environment and load test apps instead? This does mean though that if the test phase of a build always runs, the integration tests in a LIVE environment will fail because the test applications weren't loaded to Marvin.
	public MarvinService() {
		Set<Application> standardApps = ApplicationRepository.getInstance().getStandardApplications();
		Set<Application> testApps = ApplicationRepository.getInstance().getTestApplications();

		marvin = new Marvin(standardApps); // TODO: Add test apps too when in DEV
	}

	public MarvinService(Marvin marvin) {
		this.marvin = marvin;
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public Response command() {
		return Response.ok("worked!").build();
	}
	
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response command(Command command, @HeaderParam(APPLICATION_STATES_HEADER_KEY) String marshalledAppStates) {
		String commandText = command.getCommand();
		return handleRequest(commandText, marshalledAppStates);
	}
	
	// TODO: Need to encrypt request and response bodys as well as the app states header
	// TODO: Is there any chance that json versions of existing classes will automatically be marshalled even if their abstract parent is the one who was referenced????? Try it. 
	// TODO: Pre-processors will work on the headers, decrypting and decoding, so this class doesn't need to worry about that.
	@POST
	@Consumes("text/plain")
	@Produces("application/json")
	public Response command(String command, @HeaderParam(APPLICATION_STATES_HEADER_KEY) String marshalledAppStates) {
		return handleRequest(command, marshalledAppStates);
	}

	private Response handleRequest(String command, String marshalledAppStates) {
		Map<String, ApplicationState> appStates = unmarshallAppStates(marshalledAppStates);

		marvin.updateApplicationStates(appStates);
		MarvinResponse marvinResponse = marvin.processCommand(command);

		Response serviceResponse = constructServiceResponseFromMarvinResponse(marvinResponse);

		return serviceResponse;
	}

	@POST
	@Path("/execute")
	@Consumes("application/json")
	@Produces("application/json")
	public Response executeActionInvocation(ActionInvocation action, @HeaderParam("application_states") String marshalledAppStates) {
		Map<String, ApplicationState> appStates = unmarshallAppStates(marshalledAppStates);
		
		marvin.updateApplicationStates(appStates);
		MarvinResponse marvinResponse = marvin.processActionInvocation(action);
		Response serviceResponse = constructServiceResponseFromMarvinResponse(marvinResponse);

		return serviceResponse;
	}

	private Map<String, ApplicationState> unmarshallAppStates(String marshalledAppStates) {
		ApplicationStateFactory appStateFactory = this.marvin.getApplicationStateFactory();
		ApplicationStatesMarshaller statesMarshaller = new ApplicationStatesMarshaller(appStateFactory);

		return statesMarshaller.unmarshallJSONAppStates(marshalledAppStates);
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
		else if (requestOutcome.equals(RequestOutcome.INVALID)) {
			builder = Response.status(Response.Status.BAD_REQUEST);
		}
		else if (requestOutcome.equals(RequestOutcome.UNMATCHED)) {
			builder = Response.status(Response.Status.NOT_FOUND);
		}
	
		Response response = builder.entity(marvinResponse).build();
		
		return response;
	}
	
}
