package com.ahwers.marvin;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;

public class ResourceTemplate {
	
	private String content;
	
	public ResourceTemplate(String resourcePath) {
		this.content = getContentFromResourceFile(resourcePath);
	}
	
	private String getContentFromResourceFile(String resourcePath) {
		String resourceContent = "";
		
		String resourceFilePath = getClass().getResource(resourcePath).getPath().toString();
		Scanner resourceScanner = null;
		try {
			resourceScanner = new Scanner(new File(resourceFilePath));
		} catch (FileNotFoundException e) {
			// TODO: Log resource not found
			e.printStackTrace();
		}
		while (resourceScanner.hasNext()) {
			resourceContent += resourceScanner.nextLine();
		}
		
		return resourceContent;
 	}
	
	public String mergeDataWithResourceTemplate(Map<String, String> data) {
		for (String dataName : data.keySet()) {
			this.content = this.content.replaceAll(("\\[" + dataName + "\\]"), data.get(dataName));
		}
		
		return this.content;
	}

}
