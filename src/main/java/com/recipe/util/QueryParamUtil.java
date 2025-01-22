package com.recipe.util;

import java.util.List;
import java.util.stream.Collectors;

public class QueryParamUtil {

    public static String convertValueToString(Object value) {
        if (value instanceof List<?>) {
            return ((List<?>) value).stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
        }
        return value.toString();
    }
}
