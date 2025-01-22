package com.recipe.service;

import com.recipe.model.dto.recipe.RecipeSearchReq;
import com.recipe.model.dto.recipe.RecipeSearchRes;
import com.recipe.exception.handler.NoSuchElementFoundException;
import com.recipe.service.client.SpoonacularRestService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class RecipeService {


    private final SpoonacularRestService spoonacularRestService;



    //    public Map<String, Object> searchRecipes(String query, String cuisine, String diet, int number) {
//        String url = String.format("%s/recipes/complexSearch?query=%s&cuisine=%s&diet=%s&number=%d&apiKey=%s",
//                API_BASE_URL, query, cuisine, diet, number, API_KEY);
//        return restTemplate.getForObject(url, Map.class);
//    }

    public RecipeSearchRes searchRecipes(RecipeSearchReq recipeSearchReq) {
        RecipeSearchRes recipeSearchRes = spoonacularRestService.recipesComplexSearch(recipeSearchReq);
        if(Objects.isNull(recipeSearchRes)) {
            throw new NoSuchElementFoundException("Recipe not found for the search request {}", recipeSearchReq);
        }
        return recipeSearchRes;
    }

//    public Map<String, Object> getRecipeDetails(String id) {
//        String url = String.format("%s/recipes/%s/information?apiKey=%s", API_BASE_URL, id, API_KEY);
//        return restTemplate.getForObject(url, Map.class);
//    }

//    public Map<String, Object> getCustomizedCalories(String id, List<String> excludeIngredients) {
//        Map<String, Object> recipeDetails = getRecipeDetails(id);
//        if (recipeDetails == null) return null;
//
//        double totalCalories = 0;
//        List<Map<String, Object>> ingredients = (List<Map<String, Object>>) recipeDetails.get("extendedIngredients");
//
//        for (Map<String, Object> ingredient : ingredients) {
//            String ingredientName = (String) ingredient.get("name");
//            if (!excludeIngredients.contains(ingredientName)) {
//                totalCalories += (double) ingredient.get("amount");
//            }
//        }
//        recipeDetails.put("customizedCalories", totalCalories);
//        return recipeDetails;
//    }
}