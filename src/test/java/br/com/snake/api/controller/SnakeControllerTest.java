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
	private static String json = "{\"label\":\"XXX\"}";
	private static MockMultipartFile file;
	private String router = "/api/snake/information";

	@BeforeAll
	public static void setup() throws IOException {

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

		when(snakeService.findByLabel("XXX")).thenReturn(SnakeUtil.createSnakeDto());

		when(env.getProperty("integration.cnn.url")).thenReturn("host-cnn");
		
		ResponseEntity<String> myEntity = new ResponseEntity<String>(json, HttpStatus.CREATED);

		when(restTemplate.exchange(ArgumentMatchers.anyString(), ArgumentMatchers.any(HttpMethod.class),
				ArgumentMatchers.any(), ArgumentMatchers.<Class<String>>any())).thenReturn(myEntity);

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart(router).file(file))
				.andExpect(status().isOk()).andReturn();

		// verify(sendImage).post(Mockito.any());
		verify(snakeService).findByLabel("XXX");

		mvcResult.getResponse().setCharacterEncoding("UTF-8");
		String jsonResturn = mvcResult.getResponse().getContentAsString();

		ObjectMapper objectMapper = new ObjectMapper();

		SnakeTo snakeToResponse = objectMapper.readValue(jsonResturn, SnakeTo.class);

		SnakeTo snakteDtoExpected = SnakeUtil.createSnakeDto();
		
		Assertions.assertNotNull(snakeToResponse);
		Assertions.assertEquals(snakteDtoExpected.getAccidentSymptom(), snakeToResponse.getAccidentSymptom());
		Assertions.assertEquals(snakteDtoExpected.getAntivenom(), snakeToResponse.getAntivenom());
		Assertions.assertEquals(snakteDtoExpected.getCharacteristics(), snakeToResponse.getCharacteristics());
		Assertions.assertEquals(snakteDtoExpected.getConservationState(), snakeToResponse.getConservationState());
		Assertions.assertEquals(snakteDtoExpected.getEtymology(), snakeToResponse.getEtymology());
		Assertions.assertEquals(snakteDtoExpected.getGenre(), snakeToResponse.getGenre());
		Assertions.assertEquals(snakteDtoExpected.getSpecies(), snakeToResponse.getSpecies());
		Assertions.assertEquals(snakteDtoExpected.getUrlImage(), snakeToResponse.getUrlImage());
		Assertions.assertEquals(snakteDtoExpected.getVenomous(), snakeToResponse.getVenomous());
		Assertions.assertEquals(snakteDtoExpected.getPopularNames(), snakeToResponse.getPopularNames());

	}
}