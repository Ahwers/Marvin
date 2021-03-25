package com.ahwers.marvin.applications;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Doesn't yet support stuff like this: regex of "(6)+" matching "666" produces groups {6, 6, 6}
public class FuzzyMatcher {
	
	public static void main(String args[]) {
		Pattern pattern = Pattern.compile("(6)+$", Pattern.CASE_INSENSITIVE);
	    FuzzyMatcher matcher = new FuzzyMatcher(pattern.matcher("666"));
	    if (matcher.find()) {
	    	for (int i = 1; i < matcher.groupCount() + 1; i++) {
	    		System.out.println(matcher.groupName(i) + " : " + matcher.group(i));
	    	}
	    }
	    else {
	    	System.out.println("cry");
	    }
	}
	
	private Matcher matcher;
	private List<String> groupNames = new ArrayList<>();
	
	public FuzzyMatcher(Matcher matcher) {
		this.matcher = matcher;
		populateGroupNames();
	}
	
	private void populateGroupNames() {
		String regex = this.matcher.pattern().toString();
		
		this.groupNames.add(regex);

		String groupNameCaptureRegex = "\\(\\?<(.*?)>";
		Pattern pattern = Pattern.compile(groupNameCaptureRegex, Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(regex);
	    while (matcher.find()) {
		    this.groupNames.add(this.groupNames.size(), matcher.group(1));
	    }
	}
	
	public boolean find() {
		return this.matcher.find();
	}
	
	public int groupCount() {
		return this.matcher.groupCount();
	}
	
	public String group(int i) {
		return this.matcher.group(i);
	}
	
	public String group(String name) {
		return this.matcher.group(name);
	}
	
	public String groupName(int i) {
		return this.groupNames.get(i);
	}
	
}
