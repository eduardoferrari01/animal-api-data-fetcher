package br.com.animal.api.controller;

import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import br.com.animal.api.integration.CnnApiProd;
import br.com.animal.api.integration.CnnApiClient;
import br.com.animal.api.integration.IntegrationResponse;
import br.com.animal.api.util.AnimalUtil;

@ExtendWith(MockitoExtension.class)
public class CnnApiTest {

	@Mock
	private CnnApiClient cnnApiClient;
	
	@InjectMocks
	private CnnApiProd cnnApi;
	
	@Test
	void mustReturnLabels() {
		
		when(cnnApiClient.getAllLabels()).thenReturn(Arrays.asList("label1", "label2"));
		
		List<String> labels = cnnApi.getAllLabels();
		
		Assertions.assertNotNull(labels);
		Assertions.assertFalse(labels.isEmpty());
		Assertions.assertEquals(2, labels.size());
		Assertions.assertIterableEquals(Arrays.asList("label1", "label2"), labels);
	}
	
	@Test
	void mustReturnTrueWhenLabelExists() {
		
		when(cnnApiClient.existLabel(AnimalUtil.getLabel())).thenReturn(Boolean.TRUE);
		
		boolean exist = cnnApi.existLabel(AnimalUtil.getLabel());
		
		Assertions.assertNotNull(exist);
	}
	
	@Test
	void mustClassify() throws IOException {
		
		URL url = new URL("https://en.wikipedia.org/wiki/Bothrops_jararaca#/media/File:Bothrops_jararaca.jpg");
		InputStream is = url.openStream();
		
		MockMultipartFile file = new MockMultipartFile("file", "filename.png", "image/jpeg", is.readAllBytes());
		
		when(cnnApiClient.classify(Mockito.any())).thenReturn(new IntegrationResponse("label1"));
		
		IntegrationResponse integrationResponse = cnnApi.classify(file);
		
		Assertions.assertNotNull(integrationResponse);
		Assertions.assertEquals("label1", integrationResponse.getLabel());
	}
}
