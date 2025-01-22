package com.recipe.exception.handler;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApiErrorResponse {
	private String code;
	private String message;
	private String detailedMessage;

	public ApiErrorResponse(String code, String message, String detailedMessage) {
		this.code = code;
		this.message = message;
		this.detailedMessage = detailedMessage;
	}
}