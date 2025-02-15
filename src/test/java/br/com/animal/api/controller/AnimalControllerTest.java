package br.com.animal.api.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import br.com.animal.api.controller.dto.AnimalDTOMapper;
import br.com.animal.api.controller.dto.AnimalInfo;
import br.com.animal.api.repository.adpater.mongo.AnimalRepositoryMongo;
import br.com.animal.api.util.AnimalUtil;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureDataMongo
@SpringBootTest
@AutoConfigureMockMvc
public class AnimalControllerTest {

	@Autowired
	private AnimalRepositoryMongo animalRepository;
	@Autowired
	private MockMvc mockMvc;
	private static MockMultipartFile file;
	private String router = "/api/animal/";
	@Autowired
	private AnimalDTOMapper animalDTOMapper;

	@BeforeAll
	void setup() throws IOException {

		URI uri = URI.create("https://en.wikipedia.org/wiki/Bothrops_jararaca#/media/File:Bothrops_jararaca.jpg");

		InputStream is = uri.toURL().openStream();

		file = new MockMultipartFile("file", "filename.png", "image/jpeg", is.readAllBytes());

		animalRepository.save(AnimalUtil.createAnimalDocument());

	}

	@Test
	void mustThrowNotFoundException() throws Exception {

		MockMultipartFile unknown = new MockMultipartFile("file", "unknown.png", "image/jpeg", "XXX".getBytes());

		mockMvc.perform(MockMvcRequestBuilders.multipart(router).file(unknown)).andExpect(status().isNotFound())
				.andReturn();
	}

	@Test
	void mustReturnAnimalTo() throws Exception {

		AnimalInfo animalInfo = animalDTOMapper.animalToAnimalInfo(AnimalUtil.createAnimalDomain());

		mockMvc.perform(MockMvcRequestBuilders.multipart(router).file(file)).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$").hasJsonPath())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("$.label").value(animalInfo.label()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.popularNames").value(animalInfo.popularNames()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.conservationState").value(animalInfo.conservationState()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.antivenom").value(animalInfo.antivenom()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.etymology").value(animalInfo.etymology()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.venomous").value(animalInfo.venomous()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.canCauseSeriousAccident")
						.value(animalInfo.canCauseSeriousAccident()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.species").value(animalInfo.species()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.family").value(animalInfo.family()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(animalInfo.genre()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.inoculatingStructure")
						.value(animalInfo.inoculatingStructure()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.habitat").value(animalInfo.habitat()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.urlImage").value(animalInfo.urlImage()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.characteristics").value(animalInfo.characteristics()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.accidentSymptom").value(animalInfo.accidentSymptom()));
	}

	@Test
	void whenPassingLabelItMustReturnAnimalTo() throws Exception {

		AnimalInfo animalInfo = animalDTOMapper.animalToAnimalInfo(AnimalUtil.createAnimalDomain());

		mockMvc.perform(MockMvcRequestBuilders.get(router + "/{label}", AnimalUtil.getLabel()))
				.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$").hasJsonPath())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("$.label").value(animalInfo.label()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.popularNames").value(animalInfo.popularNames()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.conservationState").value(animalInfo.conservationState()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.antivenom").value(animalInfo.antivenom()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.etymology").value(animalInfo.etymology()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.venomous").value(animalInfo.venomous()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.canCauseSeriousAccident")
						.value(animalInfo.canCauseSeriousAccident()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.species").value(animalInfo.species()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.family").value(animalInfo.family()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(animalInfo.genre()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.inoculatingStructure")
						.value(animalInfo.inoculatingStructure()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.habitat").value(animalInfo.habitat()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.urlImage").value(animalInfo.urlImage()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.characteristics").value(animalInfo.characteristics()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.accidentSymptom").value(animalInfo.accidentSymptom()));
	}

	@Test
	void whenPassingLabelThatDoesnExistShouldThrowNotFoundException() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get(router + "/{label}", "AAA")).andExpect(status().isNotFound())
				.andExpect(MockMvcResultMatchers.jsonPath("$.date").isNotEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Nenhum animal foi encontrado"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.detail").value("uri=/api/animal/AAA"));

	}

}