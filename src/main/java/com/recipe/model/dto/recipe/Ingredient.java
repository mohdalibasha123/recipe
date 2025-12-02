package com.recipe.model.dto.recipe;

import lombok.Data;

import java.util.List;

@Data
public class Ingredient {

    public int id;
    public String name;
    public double amount;
    public String unit;
    public List<Nutrient> nutrients;
}
