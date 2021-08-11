package com.ahwers.rest.security;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import com.ahwers.marvin.applications.ApplicationResourcePathRepository;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class OneTimePasswordAuthenticator implements ContainerRequestFilter {

	// TODO: Move this to the OTP class
	private final boolean testing = true;
	
	private Map<String, String> userSecretMap;
	
	public OneTimePasswordAuthenticator() {
		userSecretMap = new HashMap<>();
		
		loadUserSecretMap();
	}
	
	private void loadUserSecretMap() {
		ApplicationResourcePathRepository appResourcePathRepo = ApplicationResourcePathRepository.getInstance();
		String loginFilePath = appResourcePathRepo.getApplicationResourcePathForKey("login");
		
		try {
			Scanner loginFileScanner = new Scanner(new File(loginFilePath));
			String username = loginFileScanner.nextLine();
			String secret = loginFileScanner.nextLine();
			
			userSecretMap.put(username, secret);
		} catch (FileNotFoundException e) {
			throw new WebApplicationException("Server side user credentials could not be found.", Response.Status.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		String authorization = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		if (authorization == null) throw new WebApplicationException("OTP", Response.Status.UNAUTHORIZED);
		
		String[] split = authorization.split(" ");
		final String user = split[0];
		String otp = split[1];
		
		String secret = userSecretMap.get(user);
		if (secret == null) throw new WebApplicationException("OTP", Response.Status.UNAUTHORIZED);
	
		OTP otpGenerator = OTP.getInstance();
		String regen = otpGenerator.generateToken(secret, testing);
		if (!regen.equals(otp)) throw new WebApplicationException("OTP", Response.Status.UNAUTHORIZED);
		
		final SecurityContext securityContext = requestContext.getSecurityContext();
		
		requestContext.setSecurityContext(new SecurityContext() {

			@Override
			public Principal getUserPrincipal() {
				return new Principal() {
					@Override
					public String getName() {
						return user;
					}
				};
			}

			@Override
			public boolean isUserInRole(String role) {
				return true;
			}

			@Override
			public boolean isSecure() {
				return securityContext.isSecure();
			}

			@Override
			public String getAuthenticationScheme() {
				return "OTP";
			}
			
		});
	}

}
