package com.recipe.model.dto.recipe;

import lombok.Data;

import java.util.List;

@Data
public class Nutrition {

    private List<Nutrient> nutrients;
    private List<Ingredient> ingredients;
}
