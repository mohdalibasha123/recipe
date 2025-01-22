package com.recipe.model.enums;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Intolerances {
    DAIRY,
    EGG,
    GLUTEN,
    GRAIN,
    PEANUT,
    SEAFOOD,
    SESAME,
    SHELLFISH,
    SOY,
    SULFITE,
    TREE_NUT,
    WHEAT;

    public final static String intoleranceValues = Arrays.stream(Intolerances.values())
            .map(Enum::name)
            .collect(Collectors.joining(","));
}
