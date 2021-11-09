package com.ahwers.marvin.service;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ahwers.marvin.framework.application.ActionInvocation;
import com.ahwers.marvin.framework.command.Command;
import com.ahwers.marvin.framework.response.RequestOutcome;

public class ServiceEndpointsIT {
    
    private TestClient client = null;

    @Before
    public void setUp() {
        client = new TestClient();
    }

    @After
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
