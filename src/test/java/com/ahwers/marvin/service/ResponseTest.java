package com.ahwers.marvin.service;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.ahwers.marvin.TestClient;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ResponseTest {
 
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

}
