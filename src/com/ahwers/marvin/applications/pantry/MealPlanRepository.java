package com.ahwers.marvin.applications.pantry;

public class MealPlanRepository {
	
	private WeeklyMealPlan currentMealPlan;
	
	public MealPlanRepository() {
		this.currentMealPlan = instantiateCurrentMealPlan();
	}
	
	private WeeklyMealPlan instantiateCurrentMealPlan() {
		// TODO: Implement
		return null;
	}
	
	public WeeklyMealPlan getCurrentMealPlan() {
		return this.currentMealPlan;
	}
	
	public void setCurrentMealPlan(WeeklyMealPlan mealPlan) {
		this.currentMealPlan = mealPlan;
	}
	
}
