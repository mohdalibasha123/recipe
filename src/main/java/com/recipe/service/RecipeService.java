package com.recipe.service;

import com.recipe.exception.AbstractException;
import com.recipe.exception.NoSuchElementFoundException;
import com.recipe.model.dto.recipe.*;
import com.recipe.service.client.SpoonacularRestService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class RecipeService {

    private final SpoonacularRestService spoonacularRestService;

    public RecipeSearchRes searchRecipes(RecipeSearchReq recipeSearchReq) {

        if(Objects.isNull(recipeSearchReq)) {
            throw new AbstractException("Recipe Search Request is Null");
        }

        RecipeSearchRes recipeSearchRes = spoonacularRestService.recipesComplexSearch(recipeSearchReq);

        if(Objects.isNull(recipeSearchRes) || CollectionUtils.isEmpty(recipeSearchRes.getResults())) {
            throw new NoSuchElementFoundException("Recipe not found for the search request {}", recipeSearchReq);
        }
        return recipeSearchRes;
    }

    public RecipeSearchRes getRecipeInformation(RecipeSearchReq recipeSearchReq, boolean excludeIngredients) {

        log.info("getRecipeInformation :: excludeIngredients flag {}", excludeIngredients);
        RecipeSearchRes recipeSearchRes = searchRecipes(recipeSearchReq);
        calculateRecipesTotalCalories(recipeSearchRes.getResults(), recipeSearchReq ,excludeIngredients);
        return recipeSearchRes;
    }

    public void calculateRecipesTotalCalories(List<RecipeDto> recipes,
                                              RecipeSearchReq recipeSearchReq,
                                              boolean excludeIngredients) {

        if (CollectionUtils.isEmpty(recipes)) {
            return;
        }

        Set<String> ingredientsToExclude = prepareIngredientsToExclude(recipeSearchReq, excludeIngredients);
        log.info("calculateRecipesTotalCalories :: ingredientsToExclude values {}", ingredientsToExclude);


        for (RecipeDto recipe : recipes) {
            double totalCalories = calculateRecipeCalories(recipe, ingredientsToExclude);
            recipe.setTotalCalories(totalCalories);
        }
    }

    /**
     * Prepares the set of ingredients to exclude based excludeIngredients flag.
     */
    private Set<String> prepareIngredientsToExclude(RecipeSearchReq recipeSearchReq, boolean excludeIngredients) {
        if (!excludeIngredients) {
            return Collections.emptySet();
        }

        Set<String> ingredientsToExclude = ((RecipeNutritionInformationReq) recipeSearchReq).getIngredientsToExclude();

        if (CollectionUtils.isEmpty(ingredientsToExclude)) {
            return Collections.emptySet();
        }

        return ingredientsToExclude.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }

    /**
     * Calculates the total calories for the recipe, exclude ingredient if ingredientsToExclude contain elements
     */
    private double calculateRecipeCalories(RecipeDto recipe, Set<String> ingredientsToExclude) {
        if (Objects.isNull(recipe.getNutrition()) || CollectionUtils.isEmpty(recipe.getNutrition().getIngredients())) {
            return 0.0;
        }

        return recipe.getNutrition().getIngredients()
                .stream()
                .filter(ingredient -> ingredientsToExclude.stream()
                        .noneMatch(excluded -> ingredient.getName().toLowerCase().contains(excluded)))
                .flatMap(ingredient -> ingredient.getNutrients().stream())
                .filter(nutrient -> "calories".equalsIgnoreCase(nutrient.getName()))
                .mapToDouble(Nutrient::getAmount)
                .sum();
    }

}