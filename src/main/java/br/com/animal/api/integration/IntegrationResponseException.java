package br.com.animal.api.integration;

import org.springframework.http.HttpStatus;

public class IntegrationResponseException extends RuntimeException {

	private static final long serialVersionUID = 3287766040532132551L;
	private HttpStatus httpStatus;
	
	public IntegrationResponseException(String msg, HttpStatus httpStatus) {

		super(msg);
		this.httpStatus = httpStatus;
	}
	
	public HttpStatus getHttpStatus() {
		return this.httpStatus;
	}
	
}
