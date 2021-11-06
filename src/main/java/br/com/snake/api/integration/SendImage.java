package br.com.snake.api.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SendImage implements ISendImage{

	private RestTemplate restTemplate;
	private HttpHeaders headers;
	//cnn host
	private String host = "";
	private String property = "integration.cnn.url";
	private ObjectMapper objectMapper;
	private static final Logger LOG = LoggerFactory.getLogger(SendImage.class);
	
	public SendImage(RestTemplate restTemplate, Environment env) {
		this.restTemplate = restTemplate;
		this.host = env.getProperty(property);
		this.headers = new HttpHeaders();
		this.headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		this.objectMapper = new ObjectMapper();

	}

	@Override
	public IntegrationResponse post(byte[] bytes) throws IntegrationResponseException {

		MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
		ContentDisposition contentDisposition = ContentDisposition.builder("form-data").filename("image").name("file")
				.build();
		fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
		HttpEntity<byte[]> fileEntity = new HttpEntity<>(bytes, fileMap);

		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("file", fileEntity);

		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

		IntegrationResponse integrationResponse = null;

		try {
			LOG.info("Uploading image to API");
			ResponseEntity<String> response = restTemplate.exchange(host, HttpMethod.POST, requestEntity, String.class);
			LOG.info("API response: {} ", response.getBody());
			integrationResponse = objectMapper.readValue(response.getBody(), IntegrationResponse.class);

		} catch (HttpServerErrorException e) {

			LOG.error("API error: {}", e.getMessage());
			throw new IntegrationResponseException(HttpStatus.BAD_GATEWAY);

		} catch (RestClientException e) {
			LOG.error("Connection refused: {}", e.getMessage());
			throw new IntegrationResponseException(HttpStatus.BAD_GATEWAY);
		} catch (JsonProcessingException e) {
			LOG.error("Json processing error: {}", e.getMessage());
			throw new IntegrationResponseException(HttpStatus.BAD_GATEWAY);
		}

		return integrationResponse;

	}
}