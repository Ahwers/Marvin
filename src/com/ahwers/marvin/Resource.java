package com.ahwers.marvin;

public class Resource {
	
	private int stateId;
	private String applicationName;
	private ResourceType type;
	private String content;
	
	public Resource(int stateId, String applicationName, ResourceType type, String content) {
		this.stateId = stateId;
		this.applicationName = applicationName;
		this.type = type;
		this.content = content;
	}
	
	public Resource(ResourceType type, String content) {
		this.stateId = 1;
		this.applicationName = "Marvin";
		
		this.type = type;
		this.content = content;
	}
	
	
	public String getApplicationName() {
		return this.applicationName;
	}
	
	public int getStateId() {
		return this.stateId;
	}
	
	public ResourceType getType() {
		return this.type;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String getContent() {
		return this.content;
	}
	

	
}
