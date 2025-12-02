package com.recipe.model.dto.recipe;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecipeDto {

    private Integer id;
    private String title;
    private Double totalCalories;
    private String image;
    private Integer servings;
    private String summary;
    private Boolean vegetarian;
    private Boolean vegan;
    private Boolean glutenFree;
    private Boolean dairyFree;
    private Boolean veryHealthy;
    private Boolean cheap;
    private Boolean veryPopular;
    private Boolean sustainable;
    private Boolean lowFodmap;
    private Integer weightWatcherSmartPoints;
    private String gaps;
    private Object preparationMinutes;
    private Object cookingMinutes;
    private Integer aggregateLikes;
    private Integer healthScore;
    private String creditsText;
    private String sourceName;
    private Double pricePerServing;
    private Integer readyInMinutes;
    private String sourceUrl;
    private String imageType;
    private List<String> cuisines;
    private List<String> dishTypes;
    private List<String> diets;
    private List<String> occasions;
    private Double spoonacularScore;
    private String spoonacularSourceUrl;
    private String license;
    private Nutrition nutrition;
}
