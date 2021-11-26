package com.ahwers.marvin.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;

import com.ahwers.marvin.framework.application.action.ActionInvocation;
import com.ahwers.marvin.framework.command.Command;
import com.ahwers.marvin.framework.response.RequestOutcome;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ServiceEndpointsIT {
    
    private TestClient client = null;

    @BeforeAll
    public void setUp() {
        client = new TestClient();
    }

    @AfterAll
    public void tearDown() {
        client.close();
    }

    @Test
    public void commandStringRequestTest() {
        Response response = client.postCommandRequest("successful marvin request test");
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void commandObjectRequestTest() {
        Command command = new Command("successful marvin request test");
        Response response = client.postCommandRequest(command);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void applicationInvocationObjectRequestTest() {
        ActionInvocation invocation = new ActionInvocation("Service Tester", "successfulMarvinRequestTest", new HashMap<String, String>());
        Response response = client.postActionInvocationExecutionRequest(invocation);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void applicationInvocationArgumentsParsingTest() {
        Map<String, String> arguments = new HashMap<String, String>();
        arguments.put("response", RequestOutcome.SUCCESS.name());
        ActionInvocation invocation = new ActionInvocation("Service Tester", "forceResponse", arguments);
        Response response = client.postActionInvocationExecutionRequest(invocation);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

}
