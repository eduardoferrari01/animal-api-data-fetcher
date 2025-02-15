package br.com.animal.api.controller;

import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.animal.api.integration.classify.ClassificationResponse;
import br.com.animal.api.integration.classify.ClassifyApiClient;
import br.com.animal.api.integration.classify.ClassifyApiProd;

@ExtendWith(MockitoExtension.class)
public class ClassifyApiTest {

	@Mock
	private ClassifyApiClient cnnApiClient;

	@InjectMocks
	private ClassifyApiProd cnnApi;
	@Mock
	private ObjectMapper objectMapper;

	@Test
	void mustClassify() throws IOException {

		URI uri = URI.create("https://en.wikipedia.org/wiki/Bothrops_jararaca#/media/File:Bothrops_jararaca.jpg");

		InputStream is = uri.toURL().openStream();

		MockMultipartFile file = new MockMultipartFile("file", "filename.png", "image/jpeg", is.readAllBytes());

		String label = "bothrops-jararaca";

		when(cnnApiClient.classify(Mockito.any())).thenReturn(List.of(label));

		when(objectMapper.readValue(Mockito.any(String.class), Mockito.eq(ClassificationResponse.class)))
				.thenReturn(new ClassificationResponse(label, 99.99));

		ClassificationResponse integrationResponse = cnnApi.classify(file).getFirst();

		Assertions.assertNotNull(integrationResponse);
		Assertions.assertEquals(label, integrationResponse.label());
	}
	
}
