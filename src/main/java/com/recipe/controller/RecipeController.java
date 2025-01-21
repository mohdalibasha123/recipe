package com.recipe.controller;

import com.recipe.dto.SortOption;
import com.recipe.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @GetMapping("/search")
    @Operation(summary = "Search recipes")
    public ResponseEntity<?> searchRecipes(

            @RequestParam String recipeName,
            @RequestParam(required = false) String cuisine,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) SortOption sortOption) {
        return ResponseEntity.ok(recipeService.searchRecipes(recipeName, cuisine, sortBy, sortOption));
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<?> getRecipeDetails(@PathVariable String id) {
        return ResponseEntity.ok(recipeService.getRecipeDetails(id));
    }

    @PostMapping("/{id}/calories")
    public ResponseEntity<?> getCustomizedCalories(
            @PathVariable String id,
            @RequestBody List<String> excludeIngredients) {
        return ResponseEntity.ok(recipeService.getCustomizedCalories(id, excludeIngredients));
    }
}