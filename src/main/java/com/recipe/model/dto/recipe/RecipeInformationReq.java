package com.recipe.model.dto.recipe;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RecipeInformationReq extends RecipeSearchReq {

    @Schema(defaultValue = "true", allowableValues = "true")
    private final boolean addRecipeNutrition = true;

    @Schema(defaultValue = "true", allowableValues = "true")
    private final boolean addRecipeInformation = true;
}
