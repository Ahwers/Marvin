package com.ahwers.marvin.framework.tools;

public class ResourceRepository {

	private static ResourceRepository instance;

	public static ResourceRepository getInstance() {
		if (instance == null) {
			instance = new ResourceRepository();
		}
		
		return instance;
	}

	public String getResourcePath(String resourceName) {
		return this.getClass().getClassLoader().getResource("resources/" + resourceName).getPath().toString();
	}

}
