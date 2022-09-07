package br.com.animal.api.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.animal.api.dto.AnimalTo;
import br.com.animal.api.integration.CnnApi;
import br.com.animal.api.integration.CnnApiClient;
import br.com.animal.api.integration.IntegrationResponse;
import br.com.animal.api.service.AnimalService;
import br.com.animal.api.util.AnimalUtil;

@WebMvcTest(AnimalController.class)
public class AnimalControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private AnimalService animalService;
	@MockBean
	private CnnApi cnnApi;
	@Mock
	private CnnApiClient cnnApiClient;
	private static MockMultipartFile file;
	private String router = "/api/animal/information";
	private static ObjectMapper objectMapper;
	private final String label = "XXX";
	private final String encoding = "UTF-8";
	
	@BeforeAll
	public static void setup() throws IOException {

		objectMapper = new ObjectMapper();
		
		System.setProperty("CNN_URL", "localhost");
		
		URL url = new URL("https://en.wikipedia.org/wiki/Bothrops_jararaca#/media/File:Bothrops_jararaca.jpg");
		InputStream is = url.openStream();
		
		file = new MockMultipartFile("file", "filename.png", "image/jpeg", is.readAllBytes());
	}

	@Test
	void mustThrowNotFoundException() throws Exception {

		when(animalService.findByLabel(Mockito.anyString())).thenThrow(NotFoundException.class);
		when(cnnApi.classify(Mockito.any())).thenReturn(integrationResponseCreate());
		
		mockMvc.perform(MockMvcRequestBuilders.multipart(router).file(file)).andExpect(status().isNotFound())
				.andReturn();
		
		verify(animalService).findByLabel(Mockito.anyString());
		verify(cnnApi).classify(Mockito.any());
	}

	@Test
	void mustReturnAnimalTo() throws Exception {

		when(animalService.findByLabel(label)).thenReturn(AnimalUtil.createSnakeDto());
		
		when(cnnApi.classify(Mockito.any())).thenReturn(integrationResponseCreate());
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart(router).file(file))
				.andExpect(status().isOk()).andReturn();

		verify(animalService).findByLabel(label);

		mvcResult.getResponse().setCharacterEncoding(encoding);
		String jsonResturn = mvcResult.getResponse().getContentAsString();

		AnimalTo animalToResponse = objectMapper.readValue(jsonResturn, AnimalTo.class);

		validateAnimalTo(animalToResponse);
	 
	}
	
	@Test
	void whenPassingLabelItMustReturnAnimalTo() throws Exception {
		
		when(animalService.findByLabel(label)).thenReturn(AnimalUtil.createSnakeDto());

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(router+"/find/{label}", label))
				.andExpect(status().isOk()).andReturn();

		verify(animalService).findByLabel(label);

		mvcResult.getResponse().setCharacterEncoding(encoding);
		
		String jsonResturn = mvcResult.getResponse().getContentAsString();

		AnimalTo animalToResponse = objectMapper.readValue(jsonResturn, AnimalTo.class);
		
		validateAnimalTo(animalToResponse);
	}
	
	@Test
	void whenPassingLabelThatDoesnExistShouldThrowNotFoundException() throws Exception {

		when(animalService.findByLabel(Mockito.anyString())).thenThrow(NotFoundException.class);

		mockMvc.perform(MockMvcRequestBuilders.get(router+"/find/{label}", "AAA"))
				.andExpect(status().isNotFound());
	
	}
	
	private void validateAnimalTo(AnimalTo animalToResponse) {
		
		AnimalTo animalDtoExpected = AnimalUtil.createSnakeDto();
		
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
	
	@Test
	void mustReturnLabelsAvailableForRegistration() throws Exception  {
		
		when(animalService.findLabelsAvailableToRegister()).thenReturn(Arrays.asList("label3", "label4"));
		
		mockMvc.perform(MockMvcRequestBuilders.get("/api/animal/find/labels/available/"))
		.andExpect(status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").hasJsonPath())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
		.andExpect(MockMvcResultMatchers.jsonPath("$.[0]").value("label3"))
		.andExpect(MockMvcResultMatchers.jsonPath("$.[0]").value("label3"));

		verify(animalService).findLabelsAvailableToRegister();
	}
	
	private IntegrationResponse integrationResponseCreate() {
		IntegrationResponse integrationResponse = new IntegrationResponse();
		integrationResponse.setLabel(label);
		return integrationResponse;
	}
}