package br.com.snake.api.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.snake.api.dto.SnakeTo;
import br.com.snake.api.integration.SendImage;
import br.com.snake.api.service.SnakeService;
import br.com.snake.api.util.SnakeUtil;

@WebMvcTest(SnakeController.class)
public class SnakeControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private SnakeService snakeService;
	@Mock
	private Environment env;
	@Mock
	private SendImage sendImage;
	@MockBean
	private RestTemplate restTemplate;
	private final static String json = "{\"label\":\"XXX\"}";
	private static MockMultipartFile file;
	private String router = "/api/snake/information";
	private static ObjectMapper objectMapper;
	private final String label = "XXX";
	private final String encoding = "UTF-8";
	private final String urlKey = "integration.cnn.url";
	private final String urlValue = "host-cnn";
	
	@BeforeAll
	public static void setup() throws IOException {

		objectMapper = new ObjectMapper();
		
		System.setProperty("CNN_URL", "localhost");
		
		URL url = new URL(
				"https://pt.wikipedia.org/wiki/Jararacu%C3%A7u#/media/Ficheiro:Jararacu%C3%A7u_(Bothrops_jararacussu)_por_Rodrigo_Tetsuo_Argenton_(5).jpg");
		InputStream is = url.openStream();
		file = new MockMultipartFile("file", "filename.png", "image/jpeg", is.readAllBytes());
		
	}

	@Test
	void mustThrowHttpServerErrorException() throws Exception {

		when(restTemplate.exchange(ArgumentMatchers.anyString(), ArgumentMatchers.any(HttpMethod.class),
				ArgumentMatchers.any(), ArgumentMatchers.<Class<String>>any()))
						.thenThrow(HttpServerErrorException.class);

		mockMvc.perform(MockMvcRequestBuilders.multipart(router).file(file)).andExpect(status().isBadGateway())
				.andReturn();
	}

	@Test
	void mustThrowHttpRestClientException() throws Exception {

		when(restTemplate.exchange(ArgumentMatchers.anyString(), ArgumentMatchers.any(HttpMethod.class),
				ArgumentMatchers.any(), ArgumentMatchers.<Class<String>>any())).thenThrow(RestClientException.class);

		mockMvc.perform(MockMvcRequestBuilders.multipart(router).file(file)).andExpect(status().isBadGateway())
				.andReturn();
	}

	@Test
	void mustThrowJsonProcessingException() throws Exception {

		when(restTemplate.exchange(ArgumentMatchers.anyString(), ArgumentMatchers.any(HttpMethod.class),
				ArgumentMatchers.any(), ArgumentMatchers.<Class<String>>any())).thenAnswer(x -> {
					throw new JsonProcessingException("") {
					};
				});

		mockMvc.perform(MockMvcRequestBuilders.multipart(router).file(file)).andExpect(status().isBadGateway())
				.andReturn();
	}

	@Test
	void mustThrowNotFoundException() throws Exception {

		ResponseEntity<String> myEntity = new ResponseEntity<String>(json, HttpStatus.CREATED);

		when(restTemplate.exchange(ArgumentMatchers.anyString(), ArgumentMatchers.any(HttpMethod.class),
				ArgumentMatchers.any(), ArgumentMatchers.<Class<String>>any())).thenReturn(myEntity);

		when(snakeService.findByLabel(Mockito.anyString())).thenThrow(NotFoundException.class);

		mockMvc.perform(MockMvcRequestBuilders.multipart(router).file(file)).andExpect(status().isNotFound())
				.andReturn();
	}

	@Test
	void mustThrowException() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.multipart(router).file(file)).andExpect(status().is5xxServerError())
				.andReturn();
	}

	@Test
	void mustReturnSnakeTo() throws Exception {

		when(snakeService.findByLabel(label)).thenReturn(SnakeUtil.createSnakeDto());

		when(env.getProperty(urlKey)).thenReturn(urlValue);
		
		ResponseEntity<String> myEntity = new ResponseEntity<String>(json, HttpStatus.CREATED);

		when(restTemplate.exchange(ArgumentMatchers.anyString(), ArgumentMatchers.any(HttpMethod.class),
				ArgumentMatchers.any(), ArgumentMatchers.<Class<String>>any())).thenReturn(myEntity);

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart(router).file(file))
				.andExpect(status().isOk()).andReturn();

		verify(snakeService).findByLabel(label);

		mvcResult.getResponse().setCharacterEncoding(encoding);
		String jsonResturn = mvcResult.getResponse().getContentAsString();

		SnakeTo animalToResponse = objectMapper.readValue(jsonResturn, SnakeTo.class);

		validateAnimalTo(animalToResponse);
	 
	}
	
	@Test
	public void whenPassingLabelItMustReturnAnimalTo() throws Exception {
		
		when(snakeService.findByLabel(label)).thenReturn(SnakeUtil.createSnakeDto());

		when(env.getProperty(urlKey)).thenReturn(urlValue);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(router+"/find/{label}", label))
				.andExpect(status().isOk()).andReturn();

		verify(snakeService).findByLabel(label);

		mvcResult.getResponse().setCharacterEncoding(encoding);
		
		String jsonResturn = mvcResult.getResponse().getContentAsString();

		SnakeTo animalToResponse = objectMapper.readValue(jsonResturn, SnakeTo.class);
		
		validateAnimalTo(animalToResponse);
	}
	
	@Test
	void whenPassingLabelThatDoesnExistShouldThrowNotFoundException() throws Exception {

		when(snakeService.findByLabel(Mockito.anyString())).thenThrow(NotFoundException.class);

		mockMvc.perform(MockMvcRequestBuilders.get(router+"/find/{label}", "AAA"))
				.andExpect(status().isNotFound());
	
	}
	
	private void validateAnimalTo(SnakeTo animalToResponse) {
		
		SnakeTo animalDtoExpected = SnakeUtil.createSnakeDto();
		
		Assertions.assertNotNull(animalToResponse);
		Assertions.assertEquals(animalDtoExpected.getAccidentSymptom(), animalToResponse.getAccidentSymptom());
		Assertions.assertEquals(animalDtoExpected.getAntivenom(), animalToResponse.getAntivenom());
		Assertions.assertEquals(animalDtoExpected.getCharacteristics(), animalToResponse.getCharacteristics());
		Assertions.assertEquals(animalDtoExpected.getConservationState(), animalToResponse.getConservationState());
		Assertions.assertEquals(animalDtoExpected.getEtymology(), animalToResponse.getEtymology());
		Assertions.assertEquals(animalDtoExpected.getGenre(), animalToResponse.getGenre());
		Assertions.assertEquals(animalDtoExpected.getSpecies(), animalToResponse.getSpecies());
		Assertions.assertEquals(animalDtoExpected.getUrlImage(), animalToResponse.getUrlImage());
		Assertions.assertEquals(animalDtoExpected.getVenomous(), animalToResponse.getVenomous());
		Assertions.assertEquals(animalDtoExpected.getPopularNames(), animalToResponse.getPopularNames());
	}
	
}