package com.recipe.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AbstractException extends RuntimeException {

	private String message;
	private String detailedMessage;
	private HttpStatus httpStatus;
	private Object[] values;


	public AbstractException(String detailedMessage, Object... values) {
		this.detailedMessage = detailedMessage;
		this.values = values;
	}

	public AbstractException(String message, String detailedMessage) {
		this.message = message;
		this.detailedMessage = detailedMessage;
	}
}