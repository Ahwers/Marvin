package com.ahwers.rest.security;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.picketbox.commons.cipher.Base64;

public class OTP {
	
	private static OTP otp;
	private OTP() {}
	public static OTP getInstance() {
		if (otp == null) {
			otp = new OTP();
		}
		
		return otp;
	}
	
	private long currentMinute;
	private long commandCountForCurrentMinute;

	// TODO: Test this method's reliabliity when a request is sent at 59 seconds
	public String generateToken(String secret, boolean test) {
		String token = null;
		if (test) {
			token = generateTestToken();
		}
		else {
			token = generateRealToken(secret);
		}
		
		return token;
	}

	private String generateRealToken(String secret) {
		long minutes = System.currentTimeMillis() / 1000 / 60;
		
		if (currentMinute == minutes) {
			commandCountForCurrentMinute++;
		}
		else {
			currentMinute = minutes;
			commandCountForCurrentMinute = 1;
		}
		
		String concat = secret + minutes + commandCountForCurrentMinute;
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("MD5");
		}
		catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
		byte[] hash = digest.digest(concat.getBytes(Charset.forName("UTF-8")));
		return Base64.encodeBytes(hash);
	}
	
	private String generateTestToken() {
		return "test_password";
	}
	
}
