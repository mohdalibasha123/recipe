package com.recipe.util;

import java.util.Collection;
import java.util.stream.Collectors;

public class ConvertUtil {

    public static String convertValueToString(Object value) {
        if (value instanceof Collection<?>) {
            return ((Collection<?>) value).stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
        }
        return value.toString();
    }
}
