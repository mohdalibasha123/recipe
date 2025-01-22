package com.recipe.model.dto.recipe;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.recipe.model.enums.Intolerances;
import com.recipe.model.enums.SortDirection;
import com.recipe.model.enums.SortingOption;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class RecipeSearchReq {

    @NotNull
    @JsonProperty("query")
    private String recipeName;

    /**
     * If set to true, you get more information about the recipes returned.
     */
    @Schema(defaultValue = "false", allowableValues = {"false", "true"})
    private boolean addRecipeInformation;

    /**
     * The minimum amount of servings the recipe is for.
     */
    @Schema(defaultValue = "1")
    private int minServings;

    /**
     * list of intolerances. All recipes returned must not contain ingredients
     * that are not suitable for people with the intolerances entered.
     */
    private List<Intolerances> intolerances;

    /**
     * The strategy to sort recipes by e.g.(calories).
     */
    private SortingOption sort;

    /**
     * The direction in which to sort.
     * Must be either 'asc' (ascending) or 'desc' (descending).
     */
    private SortDirection sortDirection;

    /**
     * The number of results to skip(between 0 and 900).
     */
    @Schema(defaultValue = "0")
    private int offset;

    /**
     * The number of expected results(between 1 and 100).
     */
    @Schema(defaultValue = "10")
    private int number;
}
