package br.com.animal.api.integration;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class IntegrationResponseException extends HttpStatusCodeException{

	private static final long serialVersionUID = 3287766040532132551L;

	public IntegrationResponseException(HttpStatus statusCode) {
		
		super(statusCode);
	}
}
