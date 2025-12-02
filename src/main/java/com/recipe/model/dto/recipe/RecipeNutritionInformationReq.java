package com.recipe.model.dto.recipe;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class RecipeNutritionInformationReq extends RecipeInformationReq{

    @Schema(description = "list of ingredients to exclude from the calories of the recipe.",
            type = "array",
            example = "[\"Oil\", \"Cheese\"]")
    private Set<String> ingredientsToExclude;
}
