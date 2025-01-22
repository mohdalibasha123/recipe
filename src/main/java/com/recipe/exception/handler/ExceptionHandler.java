package com.recipe.exception.handler;


import com.recipe.exception.AbstractException;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@ControllerAdvice
@AllArgsConstructor
@PropertySource(value = "classpath:exception/jakarta.exceptions.properties")
@PropertySource(value = "classpath:exception/exceptions.properties")
@Hidden
public class ExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ExceptionHandler.class);
    private static final String CODE_SUFFIX = ".code";
    private static final String MESSAGE_SUFFIX = ".msg";
    private final Environment environment;

    @org.springframework.web.bind.annotation.ExceptionHandler(AbstractException.class)
    public ResponseEntity<?> handleRuntimeException(AbstractException ex, HttpServletRequest request) {
        return getResponseEntity(ex, request);
    }

    private ResponseEntity<?> getResponseEntity(AbstractException ex, HttpServletRequest request) {
        ApiErrorResponse apiErrorResponse = assembleExceptionResponse(ex);
        ex.setMessage(apiErrorResponse.getMessage());
        log.warn("Error while executing " + requestToString(request), ex);
        HttpStatus httpStatus = Objects.isNull(ex.getHttpStatus()) ? HttpStatus.BAD_REQUEST : ex.getHttpStatus();
        return new ResponseEntity<>(apiErrorResponse, httpStatus);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleConstraintViolationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.warn("Error while executing " + requestToString(request), ex);
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String message = environment.getProperty(Objects.requireNonNull(error.getCodes())[error.getCodes().length - 1] + ".message");
            String errorMessage = message != null ? message : error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }



    private ApiErrorResponse assembleExceptionResponse(Exception ex){
        String code = environment.getProperty(ex.getClass().getSimpleName() + CODE_SUFFIX);
        String message = Objects.isNull(ex.getMessage()) ? environment.getProperty(ex.getClass().getSimpleName() + MESSAGE_SUFFIX) : ex.getMessage();
        String detailedMessage = null;
        if (ex instanceof AbstractException abstractException) {
            if(Objects.nonNull(abstractException.getDetailedMessage())){
                detailedMessage = abstractException.getDetailedMessage();
                if (ArrayUtils.isNotEmpty(abstractException.getValues())){
                    detailedMessage = formatMessage(abstractException.getDetailedMessage(),
                            abstractException.getValues());
                }
            }
        }
        return new ApiErrorResponse(code, message, detailedMessage);
    }

    private String formatMessage(String message, Object... values) {
        for (Object value : values) {
            message = message.replaceFirst("\\{}", value != null ? value.toString() : "null");
        }
        return message;
    }

    private String requestToString(HttpServletRequest request) {
        return String.format("%s %s", request.getMethod(), request.getRequestURL());
    }

}
