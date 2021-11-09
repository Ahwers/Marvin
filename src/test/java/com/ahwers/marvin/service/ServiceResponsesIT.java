package com.ahwers.marvin.service;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ServiceResponsesIT {

    // TODO: How can i mock this application adaptor????
	// 		 Maybe it's in the definition of what packages reflections searches in, or maybe i should inject custom actions into the class that deals with them

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
	public void serverConnectionTest() {
		Response response = client.makeGetRequest(TestClient.SERVER_ADDRESS);
		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
	}

    @Test
    public void successfulCommandTest() {
        Response response = client.postCommandRequest("successful marvin request test");
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void failedCommandTest() {
        Response response = client.postCommandRequest("failed marvin request test");
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus()); 
    } 

	@Test
	public void invalidCommandTest() {
		Response response = client.postCommandRequest("invalid marvin request test");
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());	
	}
	
	@Test
	public void unmatchedCommandTest() {
        Response response = client.postCommandRequest("unmatched marvin request test");
		assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());	
	}
	
	@Test
	public void outdatedCommandTest() {
        Response response = client.postCommandRequest("outdated marvin request test");
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());	
	}
	
	@Test
	public void conflictedCommandTest() {
		Response response = client.postCommandRequest("conflicted marvin request test");
		assertEquals(Response.Status.CONFLICT.getStatusCode(), response.getStatus());	
	}

}
