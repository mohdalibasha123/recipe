package com.recipe.model.dto.recipe;

import lombok.Data;

import java.util.List;

@Data
public class RecipeDto {

    private boolean vegetarian;
    private boolean vegan;
    private boolean glutenFree;
    private boolean dairyFree;
    private boolean veryHealthy;
    private boolean cheap;
    private boolean veryPopular;
    private boolean sustainable;
    private boolean lowFodmap;
    private int weightWatcherSmartPoints;
    private String gaps;
    private Object preparationMinutes;
    private Object cookingMinutes;
    private int aggregateLikes;
    private int healthScore;
    private String creditsText;
    private String sourceName;
    private double pricePerServing;
    private int id;
    private String title;
    private int readyInMinutes;
    private int servings;
    private String sourceUrl;
    private String image;
    private String imageType;
    private String summary;
    private List<String> cuisines;
    private List<String> dishTypes;
    private List<String> diets;
    private List<String> occasions;
    private double spoonacularScore;
    private String spoonacularSourceUrl;
    private String license;
    private Nutrition nutrition;
}
