package com.ahwers.marvin.applications.pantry;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ahwers.marvin.applications.Application;
import com.ahwers.marvin.applications.ApplicationAdaptor;
import com.ahwers.marvin.applications.CommandMatch;
import com.ahwers.marvin.applications.IntegratesApplication;
import com.ahwers.marvin.response.MarvinResponse;
import com.ahwers.marvin.response.RequestOutcome;
import com.ahwers.marvin.response.resource.Resource;
import com.ahwers.marvin.response.resource.ResourceRepresentationType;

// TODO: Better application name? Pantry is cool, but it's not obvious what it does based off of it's name.
@IntegratesApplication("Pantry")
public class PantryApplicationAdaptor extends ApplicationAdaptor {
	
	@Override
	protected Application instantiateApplication() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@CommandMatch("^(?:Whats my meal plan this week|Generate a meal plan for this week)$")
	public MarvinResponse getMealPlanForThisWeek(Map<String, String> arguments) {
		MarvinResponse response = new MarvinResponse(RequestOutcome.SUCCESS);
		
		MealPlanRepository mealPlanRepo = new MealPlanRepository();
		WeeklyMealPlan currentMealPlan = mealPlanRepo.getCurrentMealPlan();
		if (mealPlanIsOutdated(currentMealPlan)) {
			WeeklyMealPlan newMealPlan = generateMealPlanForThisWeek();
			mealPlanRepo.setCurrentMealPlan(newMealPlan);
			currentMealPlan = newMealPlan;
			updateShoppingListWithMealPlan(currentMealPlan);
		}

		response.setResource(new Resource(getApplicationName(), currentMealPlan.getId(), ResourceRepresentationType.HTML, generateHTMLResourceForMealPlan(currentMealPlan)));
		
		return response;
	}
	
	private boolean mealPlanIsOutdated(WeeklyMealPlan mealPlan) {
		boolean isOutdated = false;
		if ((mealPlan == null) || (mealPlan.getEndDate() < LocalDate.now().toEpochDay())) {
			isOutdated = true;
		}
		
		return isOutdated;
	}
	
	private WeeklyMealPlan generateMealPlanForThisWeek() {
		LocalDate todaysDate = LocalDate.now();
		
		int numberOfDaysUntilNextMonday = (8 - todaysDate.getDayOfWeek().getValue());
		LocalDate nextMondaysDate = LocalDate.of(todaysDate.getYear(), todaysDate.getMonth().getValue(), (todaysDate.getDayOfMonth() + numberOfDaysUntilNextMonday));
		
		WeeklyMealPlan mealPlan = new WeeklyMealPlan(1, todaysDate, nextMondaysDate);
//		mealPlan.setDailyMealPlans(generateBalancedSetOfDailyMealPlans());
		
		return mealPlan;
	}
	
	// TODO: Contributions to five a day
	private List<Meal> generateBalancedSetOfDailyMealPlans() {
		MealRepository mealRepo = new MealRepository();
		Set<Meal> possibleBreakfasts = mealRepo.getAllBreakfasts();
		Set<Meal> possibleDinners = mealRepo.getAllDinners();
		Set<Meal> possibleLunches = mealRepo.getAllLunches();
		Set<Meal> possibleSnacks = mealRepo.getAllSnacks();
		
		List<DailyMealPlan> mealPlans = new ArrayList<>();
	
		Set<Nutrient> mealPlanNutrients = new HashSet<>();
		Set<FoodGroup> mealPlanFoodGroups = new HashSet<>();
		
		Nutrient[] allNutrients = Nutrient.values();
		FoodGroup[] allFoodGroups = FoodGroup.values();
		
		for (Nutrient nutrient : allNutrients) {
			
		}
		
		return null;
	}
	
	private String generateHTMLResourceForMealPlan(WeeklyMealPlan mealPlan) {
		return "";
	}
	
	private void updateShoppingListWithMealPlan(WeeklyMealPlan mealPlan) {

	}

}
