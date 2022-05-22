package com.sb.stock.service.web.controllers;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.sb.stock.service.exception.ErrorMessage;
import com.sb.stock.service.exception.NotFound;

@RestControllerAdvice
class ErrorHandlingControllerAdvice {


    @ExceptionHandler(NotFound.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage onNotFound(NotFound ex, WebRequest request) {
	ErrorMessage message = new ErrorMessage(HttpStatus.NOT_FOUND.value(), OffsetDateTime.now(), ex.getMessage(),
		request.getDescription(false));
	return message;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
	Map<String, String> errors = new HashMap<>();
	ex.getBindingResult().getAllErrors().forEach((error) -> {
	    String fieldName = ((FieldError) error).getField();
	    String errorMessage = error.getDefaultMessage();
	    errors.put(fieldName, errorMessage);
	});
	return errors;
    }

}
