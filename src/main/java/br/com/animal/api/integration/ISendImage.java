package br.com.animal.api.integration;

public interface ISendImage {

	public IntegrationResponse post(byte[] bytes) throws IntegrationResponseException;
}
