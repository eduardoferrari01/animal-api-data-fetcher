package br.com.animal.api.exception;

import org.springframework.http.HttpStatus;

import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder {

	@Override
	public Exception decode(String methodKey, Response response) {
		switch (response.status()) {
		case 400:
			return new IntegrationResponseException("Falha na hora de processar a requisição", HttpStatus.BAD_REQUEST);
		case 404:
			return new IntegrationResponseException("Não encontrado", HttpStatus.NOT_FOUND);
		case 502:
			return new IntegrationResponseException("Falha na comunicação entre servidores", HttpStatus.BAD_GATEWAY);
		default:
			return new Exception();
		}

	}

}
