package com.recipe.controller;

import com.recipe.model.dto.recipe.RecipeInformationReq;
import com.recipe.model.dto.recipe.RecipeNutritionInformationReq;
import com.recipe.model.dto.recipe.RecipeSearchReq;
import com.recipe.model.dto.recipe.RecipeSearchRes;
import com.recipe.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recipes")
@AllArgsConstructor
public class RecipeController {


    private final RecipeService recipeService;

    @GetMapping("/search")
    @Operation(summary = "Search recipes")
    public ResponseEntity<RecipeSearchRes> searchRecipes(@ParameterObject @ModelAttribute @Valid RecipeSearchReq req) {
        return ResponseEntity.ok(recipeService.searchRecipes(req));
    }

    @GetMapping("/information")
    @Operation(summary = "Recipes Information")
    public ResponseEntity<RecipeSearchRes> getRecipeInformation(@ParameterObject @ModelAttribute @Valid RecipeInformationReq req) {
        return ResponseEntity.ok(recipeService.getRecipeInformation(req, false));
    }

    @GetMapping("/nutrition/info")
    @Operation(summary = "Recipes Nutrition Information")
    public ResponseEntity<RecipeSearchRes> getRecipeNutritionInformation(@ParameterObject @ModelAttribute @Valid RecipeNutritionInformationReq req) {
        return ResponseEntity.ok(recipeService.getRecipeInformation(req, true));
    }
}