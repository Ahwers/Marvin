package com.ahwers.marvin;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ahwers.marvin.applications.ApplicationResourcePathRepository;
import com.ahwers.rest.security.OTP;

public class MarvinServiceTest {

	private final String MARVIN_ENDPOINT = "http://localhost:8080/services/command";
	
	private Client client = null;
	private String userSecret = null;
	private String username = null;
	
	@Before
	public void setUp() {
		client = ClientBuilder.newClient();
		loadUserCredentials();
	}
	
	private void loadUserCredentials() {
		ApplicationResourcePathRepository appResourcePathRepo = ApplicationResourcePathRepository.getInstance();
		String loginFilePath = appResourcePathRepo.getApplicationResourcePathForKey("login");
		
		try {
			Scanner loginFileScanner = new Scanner(new File(loginFilePath));
			String username = loginFileScanner.nextLine();
			String secret = loginFileScanner.nextLine();
			
			this.username = username;
			userSecret = secret;
		} catch (FileNotFoundException e) {
			throw new WebApplicationException("Server side user credentials could not be found.", Response.Status.INTERNAL_SERVER_ERROR);
		}
	}
	
	@After
	public void tearDown() {
		client.close();
	}

	// TODO: Security tests
	
	@Test
	public void successfulCommandTest() {
		WebTarget target = client.target(MARVIN_ENDPOINT);
		
//		String otp = OTP.getInstance().generateToken(userSecret);
		Response response = target.request()
				.get();
		
//		Response response = target.request()
//			.header("Authorization", (username + " " + otp))
//			.accept("application/json")
//			.post(Entity.entity("successful marvin request test", "text/plain"));
////			.post(Entity.text("successful marvin request test"));
		
		assertEquals(Response.Status.OK, response.getStatus());
	}
	
//	@Test
//	public void failedCommandTest() {
//		WebTarget target = client.target(MARVIN_ENDPOINT);
//		
//		String otp = OTP.getInstance().generateToken(userSecret);
//		Response response = target.request()
//			.header("Authorization", (username + " " + otp))
//			.accept("application/json")
//			.post(Entity.text("failed marvin request test"));
//		
//		assertEquals(Response.Status.INTERNAL_SERVER_ERROR, response.getStatus());	
//	}
//	
//	@Test
//	public void invalidCommandTest() {
//		WebTarget target = client.target(MARVIN_ENDPOINT);
//		
//		String otp = OTP.getInstance().generateToken(userSecret);
//		Response response = target.request()
//			.header("Authorization", (username + " " + otp))
//			.accept("application/json")
//			.post(Entity.text("invalid marvin request test"));
//		
//		assertEquals(Response.Status.BAD_REQUEST, response.getStatus());	
//	}
//	
//	@Test
//	public void unmatchedCommandTest() {
//		WebTarget target = client.target(MARVIN_ENDPOINT);
//		
//		String otp = OTP.getInstance().generateToken(userSecret);
//		Response response = target.request()
//			.header("Authorization", (username + " " + otp))
//			.accept("application/json")
//			.post(Entity.text("unmatched marvin request test"));
//		
//		assertEquals(Response.Status.NOT_FOUND, response.getStatus());	
//	}
//	
//	@Test
//	public void outdatedCommandTest() {
//		WebTarget target = client.target(MARVIN_ENDPOINT);
//		
//		String otp = OTP.getInstance().generateToken(userSecret);
//		Response response = target.request()
//			.header("Authorization", (username + " " + otp))
//			.accept("application/json")
//			.post(Entity.text("outdated marvin request test"));
//		
//		assertEquals(Response.Status.BAD_REQUEST, response.getStatus());	
//	}
//	
//	@Test
//	public void conflictedCommandTest() {
//		WebTarget target = client.target(MARVIN_ENDPOINT);
//		
//		String otp = OTP.getInstance().generateToken(userSecret);
//		Response response = target.request()
//			.header("Authorization", (username + " " + otp))
//			.accept("application/json")
//			.post(Entity.text("conflicted marvin request test"));
//		
//		assertEquals(Response.Status.CONFLICT, response.getStatus());	
//	}
	
}
