package com.ahwers.marvin;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;

import com.ahwers.marvin.applications.ApplicationResourcePathRepository;

public class ResourceTemplate {
	
	private String content;
	
	public ResourceTemplate(String resourceKey) {
		this.content = getContentFromResourceFile(resourceKey);
	}
	
	private String getContentFromResourceFile(String resourceKey) {
		String resourceContent = "";
		
		String resourceFilePath = ApplicationResourcePathRepository.getInstance().getApplicationResourcePathForKey(resourceKey);
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