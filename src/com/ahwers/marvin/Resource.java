package com.ahwers.marvin;

public class Resource {
	
	private int stateId;
	private ResourceType type;
	private String content;
	private String applicationName;
	
	public Resource(int stateId, ResourceType type, String content, String applicationName) {
		this.stateId = stateId;
		this.type = type;
		this.content = content;
		this.applicationName = applicationName;
	}
	
	public Resource(int stateId, ResourceType type) {
		this.stateId = stateId;
		this.type = type;
	}
	
	public ResourceType getType() {
		return this.type;
	}
	
	public int getStateId() {
		return this.stateId;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getContent() {
		return this.content;
	}
	
	public String getApplicationName() {
		return this.applicationName;
	}
	
}
