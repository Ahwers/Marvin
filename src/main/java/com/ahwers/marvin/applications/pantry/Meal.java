package com.ahwers.marvin.applications.pantry;

import java.util.Map;
import java.util.Set;

// Refactor so we can have another Snack class that doesn't have ingredients
public class Meal {
	
	private int id;
	private String name;
	private int portions;
	private Set<FoodGroup> foodGroups;
	private Set<Nutrient> nutrients;
	private Map<Ingredient, Double> ingredients;
	

}
