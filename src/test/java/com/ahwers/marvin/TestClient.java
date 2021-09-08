package com.ahwers.marvin;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import com.azure.core.http.ContentType;

public class TestClient {

	public final static String SERVER_ADDRESS = "http://127.0.0.1:8080/";
	public final static String MARVIN_ENDPOINT = (SERVER_ADDRESS + "RestfulMarvin/");
	public final static String MARVIN_COMMAND_ENDPOINT = (MARVIN_ENDPOINT + "services/command");
   
    private Client client = null;

    public TestClient() {
       client = ClientBuilder.newClient(); 
    }

    public Response makeGetRequest(String address) {
        WebTarget target = client.target(address);

        Response response = target.request()
            .header(HttpHeaders.AUTHORIZATION, getAuthToken())
            .accept(ContentType.APPLICATION_JSON)
            .get();

        response.close();

        return response;
    }

    public Response postCommandRequest(String command) {
        WebTarget target = client.target(MARVIN_COMMAND_ENDPOINT);

        Response response = target.request()
            .header(HttpHeaders.AUTHORIZATION, getAuthToken())
            .accept(ContentType.APPLICATION_JSON)
            .post(Entity.text(command));
        
        response.close();

        return response;
    }

    private String getAuthToken() {
        return ""; // TODO: Implement
    }

    public void close() {
        client.close();
    }

}
