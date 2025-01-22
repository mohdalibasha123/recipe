package com.recipe.model.dto.recipe;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RecipeSearchRes {

    private List<RecipeDto> results;

    private int offset;
    private int number;
    private int totalResults;
}
