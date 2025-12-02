package com.recipe;

import com.recipe.exception.AbstractException;
import com.recipe.exception.NoSuchElementFoundException;
import com.recipe.model.dto.recipe.RecipeInformationReq;
import com.recipe.model.dto.recipe.RecipeNutritionInformationReq;
import com.recipe.model.dto.recipe.RecipeSearchReq;
import com.recipe.model.dto.recipe.RecipeSearchRes;
import com.recipe.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class RecipeApplicationTests {


    @Autowired
    private RecipeService recipeService;


    /*
     * Test Scenarios on api/recipe/search endpoint, searchRecipes Start block
     */

    @Test
    void searchRecipes_NormalCase() {
        RecipeSearchReq request = new RecipeSearchReq();
        request.setRecipeName("Pizza");

        RecipeSearchRes response = recipeService.searchRecipes(request);

        assertThat(response).isNotNull();
        assertThat(response.getResults()).isNotEmpty();
    }

    @Test
    void searchRecipes_EmptyResult() {
        RecipeSearchReq request = new RecipeSearchReq();
        request.setRecipeName("NonExistentRecipe");

        NoSuchElementFoundException exception = assertThrows(NoSuchElementFoundException.class, () -> recipeService.searchRecipes(request));
        assertThat(exception.getDetailedMessage()).contains("Recipe not found");
    }

    @Test
    void searchRecipes_NullRequest() {
        RecipeSearchReq request = null;
        AbstractException exception = assertThrows(AbstractException.class, () -> recipeService.searchRecipes(request));
        assertThat(exception.getDetailedMessage()).contains("Recipe Search Request is Null");
    }

    /*
     * Test Scenarios on api/recipe/search endpoint, searchRecipes End block
     */



    /*
     * Test Scenarios on api/recipe/information endpoint, getRecipeInformation Start block
     */

    @Test
    void getRecipeInformation_WithCalories() {
        RecipeInformationReq request = new RecipeInformationReq();
        request.setRecipeName("Burger");

        RecipeSearchRes response = recipeService.getRecipeInformation(request, false);

        assertThat(response).isNotNull();
        assertThat(response.getResults()).isNotEmpty();
        assertThat(response.getResults().get(0).getTotalCalories()).isGreaterThan(0.0);
    }

    @Test
    void getRecipeInformation_WithSummary() {
        RecipeInformationReq request = new RecipeInformationReq();
        request.setRecipeName("pizza");

        RecipeSearchRes response = recipeService.getRecipeInformation(request, false);

        assertThat(response).isNotNull();
        assertThat(response.getResults().get(0).getSummary()).isNotNull();
    }

    @Test
    void getRecipeInformation_NullNutrition() {
        RecipeInformationReq request = new RecipeInformationReq();
        request.setRecipeName("pizza");
        request.setAddRecipeInformation(false);

        RecipeSearchRes response = recipeService.getRecipeInformation(request, false);

        assertThat(response).isNotNull();
        assertThat(response.getResults().get(0).getNutrition()).isNull();
    }

    @Test
    void getRecipeInformation_EmptyResult() {
        RecipeInformationReq request = new RecipeInformationReq();
        request.setRecipeName("NonExistentRecipe");

        NoSuchElementFoundException exception = assertThrows(NoSuchElementFoundException.class, () -> recipeService.getRecipeInformation(request, false));
        assertThat(exception.getDetailedMessage()).contains("Recipe not found");
    }

    /*
     * Test Scenarios on api/recipe/information endpoint, getRecipeInformation End block
     */



    /*
     * Test Scenarios on api/recipe/nutrition/info endpoint, getRecipeNutritionInformation Start block
     */

    @Test
    void getRecipeNutritionInformation_ExcludeCheese() {
        RecipeNutritionInformationReq request = new RecipeNutritionInformationReq();
        request.setRecipeName("Spinach Feta Pizza");
        // This recipe total calories 390.4, cheese calories is 198.75
        request.setIngredientsToExclude(Set.of("Cheese"));

        RecipeSearchRes response = recipeService.getRecipeInformation(request, true);

        assertThat(response).isNotNull();
        assertThat(response.getResults()).isNotEmpty();
        assertThat(response.getResults().get(0).getTotalCalories()).isEqualTo(191.65);
    }

    @Test
    void getRecipeNutritionInformation_ExcludeAllIngredients() {
        RecipeNutritionInformationReq request = new RecipeNutritionInformationReq();
        request.setRecipeName("Spinach Feta Pizza");
        // This recipe total calories 390.4
        request.setIngredientsToExclude(Set.of("Tomato", "Cheese", "onion", "spinach leaves", "flatbread", "garlic", "pepper"));

        RecipeSearchRes response = recipeService.getRecipeInformation(request, true);

        assertThat(response).isNotNull();
        assertThat(response.getResults()).isNotEmpty();
        assertThat(response.getResults().get(0).getTotalCalories()).isEqualTo(0.0);
    }

    @Test
    void getRecipeNutritionInformation_NoIngredientsToExclude() {
        RecipeNutritionInformationReq request = new RecipeNutritionInformationReq();
        request.setRecipeName("Spinach Feta Pizza");
        // This recipe total calories 390.4
        request.setIngredientsToExclude(Collections.emptySet());

        RecipeSearchRes response = recipeService.getRecipeInformation(request, true);

        assertThat(response).isNotNull();
        assertThat(response.getResults()).isNotEmpty();
        assertThat(response.getResults().get(0).getTotalCalories()).isEqualTo(390.4);
    }

    /*
     * Test Scenarios on api/recipe/nutrition/info endpoint, getRecipeNutritionInformation End block
     */
}
