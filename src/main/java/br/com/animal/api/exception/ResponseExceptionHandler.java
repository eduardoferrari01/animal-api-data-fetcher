package br.com.animal.api.exception;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.animal.api.controller.NotFoundException;
import br.com.animal.api.integration.IntegrationResponseException;

@ControllerAdvice
@RestController
public class ResponseExceptionHandler  extends ResponseEntityExceptionHandler{

	private static final Logger LOG = LoggerFactory.getLogger(ResponseExceptionHandler.class);
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionResponse> handleExceptions(Exception exception, WebRequest request){
		
		LOG.error(exception.getMessage());
		ExceptionResponse exceptionResponse = new ExceptionResponse(LocalDateTime.now(), "Ocorreu um erro inesperado", request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ExceptionResponse> handleNotFoundException(Exception exception, WebRequest request){
		
		LOG.warn(exception.getMessage());
		ExceptionResponse exceptionResponse = new ExceptionResponse(LocalDateTime.now(), exception.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
	}
 
	@ExceptionHandler(IntegrationResponseException.class)
	public ResponseEntity<ExceptionResponse> handleIntegrationResponseException(IntegrationResponseException exception, WebRequest request){
		
		LOG.error(exception.getMessage());
		ExceptionResponse exceptionResponse = new ExceptionResponse(LocalDateTime.now(), exception.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse, exception.getHttpStatus());
	}
	
}
