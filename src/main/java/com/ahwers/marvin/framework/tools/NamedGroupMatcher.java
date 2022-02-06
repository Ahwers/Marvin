package com.ahwers.marvin.framework.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: Refactor to make the interface more intuative.
public class NamedGroupMatcher {

	private Matcher matcher;
	private List<String> groupNames = new ArrayList<>();
	
	public NamedGroupMatcher(Matcher matcher) {
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
