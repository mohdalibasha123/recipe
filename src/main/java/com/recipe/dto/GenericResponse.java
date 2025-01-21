package com.recipe.dto;

public class GenericResponse<T> {

    private T results;
    private int offset;
    private int number;
    private int totalResults;
}
