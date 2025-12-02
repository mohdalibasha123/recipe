package com.recipe.exception;


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
