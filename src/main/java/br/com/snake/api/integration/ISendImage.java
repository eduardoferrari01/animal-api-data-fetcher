package br.com.snake.api.integration;

public interface ISendImage {

	public IntegrationResponse post(byte[] bytes) throws IntegrationResponseException;
}
