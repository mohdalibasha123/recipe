package com.recipe.service;

import com.recipe.dto.SortOption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class RecipeService {

    @Value("${spoonacular.API_BASE_URL}")
    private String API_BASE_URL;

    @Value("${spoonacular.api_key}")
    private String API_KEY;


    private final RestTemplate restTemplate;

    public RecipeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Object> searchRecipes(String query, String cuisine, String diet, int number) {
        String url = String.format("%s/recipes/complexSearch?query=%s&cuisine=%s&diet=%s&number=%d&apiKey=%s",
                API_BASE_URL, query, cuisine, diet, number, API_KEY);
        return restTemplate.getForObject(url, Map.class);
    }

    public Map<String, Object> searchRecipes(String query, String cuisine, String sortBy, SortOption sortOption) {
        String url =
                API_BASE_URL, query, cuisine, sortBy, sortOption.name(), API_KEY);
        return restTemplate.getForObject(url, Map.class);
    }

    public Map<String, Object> getRecipeDetails(String id) {
        String url = String.format("%s/recipes/%s/information?apiKey=%s", API_BASE_URL, id, API_KEY);
        return restTemplate.getForObject(url, Map.class);
    }

    public Map<String, Object> getCustomizedCalories(String id, List<String> excludeIngredients) {
        Map<String, Object> recipeDetails = getRecipeDetails(id);
        if (recipeDetails == null) return null;

        double totalCalories = 0;
        List<Map<String, Object>> ingredients = (List<Map<String, Object>>) recipeDetails.get("extendedIngredients");

        for (Map<String, Object> ingredient : ingredients) {
            String ingredientName = (String) ingredient.get("name");
            if (!excludeIngredients.contains(ingredientName)) {
                totalCalories += (double) ingredient.get("amount");
            }
        }
        recipeDetails.put("customizedCalories", totalCalories);
        return recipeDetails;
    }
}