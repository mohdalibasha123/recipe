package com.recipe.util;

import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.StringJoiner;

public class QueryParamSerializerUtil {

    public static String toQueryParams(Object object) {
        StringJoiner queryParamJoiner = new StringJoiner("&");

        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            try {
                Object value = field.get(object);
                if (value != null) { // Exclude null values
                    String name = field.getName().equalsIgnoreCase("recipeName") ? "query" : field.getName();

                    if (value instanceof List<?>) {
                        // Join list elements with a comma
                        String listValue = String.join(",", ((List<?>) value).stream()
                                .map(Object::toString)
                                .toList());
                        queryParamJoiner.add(name + "=" + encode(listValue));
                    } else {
                        queryParamJoiner.add(name + "=" + encode(value.toString()));
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return queryParamJoiner.toString();
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
