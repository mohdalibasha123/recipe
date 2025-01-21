package com.recipe.dto.recipe;

import com.recipe.dto.SortOption;
import lombok.NonNull;

import java.util.List;

public class RecipeSearchReq {

    @NonNull
    private String recipeName;

    private String sortBy;
    private SortOption sortOrder;

    /**
     * This Attribute
     */
    private List<String> intolerances;


}
