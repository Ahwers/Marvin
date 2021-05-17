package com.ahwers.marvin.applications.pantry;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WeeklyMealPlan {
	
	private int id;
	private long startDateEpoch;
	private long endDateEpoch;
	private List<DailyMealPlan> dailyMealPlans;
	
	public WeeklyMealPlan(int id, LocalDate startDate, LocalDate endDate) {
		this.id = id;
		this.startDateEpoch = startDate.toEpochDay();
		this.endDateEpoch = endDate.toEpochDay();
	}
	
	public int getId() {
		return this.id;
	}
	
	public long getEndDate() {
		return this.endDateEpoch;
	}
	
}
