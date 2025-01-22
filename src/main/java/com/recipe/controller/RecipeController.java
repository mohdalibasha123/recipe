package com.recipe.controller;

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
    public ResponseEntity<RecipeSearchRes> searchRecipes(@ParameterObject @ModelAttribute @Valid RecipeSearchReq recipeSearchReq) {
        return ResponseEntity.ok(recipeService.searchRecipes(recipeSearchReq));
    }

//    @GetMapping("/{id}/details")
//    public ResponseEntity<?> getRecipeDetails(@PathVariable String id) {
//        return ResponseEntity.ok(recipeService.getRecipeDetails(id));
//    }

//    @PostMapping("/{id}/calories")
//    public ResponseEntity<?> getCustomizedCalories(
//            @PathVariable String id,
//            @RequestBody List<String> excludeIngredients) {
//        return ResponseEntity.ok(recipeService.getCustomizedCalories(id, excludeIngredients));
//    }
}