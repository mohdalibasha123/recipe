package com.recipe.exception.handler;


import com.recipe.exception.AbstractException;

public class NoSuchElementFoundException extends AbstractException {

    public NoSuchElementFoundException() {
    }

    public NoSuchElementFoundException(String detailedMessage, Object... values) {
        super(detailedMessage, values);
    }

    public NoSuchElementFoundException(String message) {
        super(message);
    }
}
