package com.recipe.model.dto.recipe;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.recipe.model.enums.Intolerances;
import com.recipe.model.enums.SortDirection;
import com.recipe.model.enums.SortOption;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.util.Set;

@Data
public class RecipeSearchReq {

    @JsonProperty("query")
    @Schema(description = "Recipe name")
    private String recipeName;

    @Schema(defaultValue = "false",
            allowableValues = {"false", "true"},
            description = "If set to true, you get more information about the recipes returned")
    private boolean addRecipeInformation;

    /**
     * list of intolerances. All recipes returned must not contain ingredients
     * that are not suitable for people with the intolerances entered.
     */
    private Set<Intolerances> intolerances;

    /**
     * The strategy to sort recipes by e.g.(calories).
     */
    private SortOption sort;

    /**
     * The direction in which to sort.
     * Must be either 'asc' (ascending) or 'desc' (descending).
     */
    private SortDirection sortDirection;

    @Schema(description = "The number of results to skip(between 0 and 900)", defaultValue = "0")
    private int offset;

    @Schema(description = "The number of expected results(between 1 and 100)", defaultValue = "10")
    @Min(1)
    private int number;

}
