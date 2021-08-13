package com.ahwers.rest.security;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class OTP {
	
	private final boolean testing = true;
	
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
	public String generateToken(String secret) {
		String token = null;
		if (testing) {
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
		
		// Convert byte array into signum representation
        BigInteger no = new BigInteger(1, hash);

        // Convert message digest into hex value
        String hashtext = no.toString(16);
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        return hashtext;
		
//		return Base64.encodeBytes(hash);
	}
	
	private String generateTestToken() {
		return "test_password";
	}
	
}
