package br.com.animal.api.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.animal.api.builder.AnimalBuilder;
import br.com.animal.api.domain.Animal;
import br.com.animal.api.domain.Animal.TypeOfAnimal;
import br.com.animal.api.domain.ConservationState;
import br.com.animal.api.dto.AnimalDto;
import br.com.animal.api.dto.AnimalInfo;
import br.com.animal.api.repository.AnimalRepository;
import br.com.animal.api.util.AnimalUtil;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestIntegration {

	@Autowired
	private AnimalRepository animalRepository;
	@Autowired
	private MockMvc mockMvc;
	private static MockMultipartFile file;
	private String router = "/api/animal/information";
	private static ObjectMapper objectMapper;
	
	@BeforeAll
	void setup() throws IOException {

		URL url = new URL("https://en.wikipedia.org/wiki/Bothrops_jararaca#/media/File:Bothrops_jararaca.jpg");
		InputStream is = url.openStream();
		file = new MockMultipartFile("file", "filename.png", "image/jpeg", is.readAllBytes());
		objectMapper = new ObjectMapper();
		
		animalRepository.save(AnimalUtil.createAnimalDomain());
	}

	@Test
	void mustCreateNewAnimal() throws Exception {
		
		AnimalDto animalDto = AnimalUtil.createAnimalDto();
		animalDto.setLabel("ophiophagus-hannah");
		
		String json = objectMapper.writeValueAsString(animalDto);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/api/animal/create").content(json)
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$").hasJsonPath())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("$.label").value(animalDto.getLabel()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.popularNames[0]").value(animalDto.getPopularNames().get(0)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.conservationState").value(animalDto.getConservationState().name()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.antivenom").value(animalDto.getAntivenom()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.etymology").value(animalDto.getEtymology()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.venomous").value(animalDto.getVenomous()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.canCauseSeriousAccident").value(animalDto.getCanCauseSeriousAccident()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.species").value(animalDto.getSpecies()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.family").value(animalDto.getFamily()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(animalDto.getGenre()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.dentition").value(animalDto.getDentition()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.habitat").value(animalDto.getHabitat()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.urlImage").value(animalDto.getUrlImage()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.characteristics").value(animalDto.getCharacteristics()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.accidentSymptom").value(animalDto.getAccidentSymptom()));
	}
	
	@Test
	void mustNotCreateAnimalWhenCnnDoesNotRecognizeLabel() throws Exception {
		
		AnimalDto animalDto = AnimalUtil.createAnimalDto();
		animalDto.setLabel("XXX");
		
		String json = objectMapper.writeValueAsString(animalDto);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/api/animal/create").content(json)
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.date").isNotEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Cnn não reconhece a label: XXX"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.detail").value("uri=/api/animal/create"));	
						 
	}
	
	@Test
	void mustNotBreedAnimalWhenLabelIsRegistered() throws Exception {
		
		AnimalDto animalDto = AnimalUtil.createAnimalDto();
		animalDto.setLabel("dendroaspis-polylepis");
		
		String json = objectMapper.writeValueAsString(animalDto);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/api/animal/create").content(json)
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.date").isNotEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Label já cadastrada"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.detail").value("uri=/api/animal/create"));	
	}
	
	@Test
	void mustNotBreedArachnidTypeAnimalWhenDentitionIsDefined() throws Exception {
	 
		AnimalDto animalDto = AnimalUtil.createAnimalDto();
		animalDto.setLabel("theraphosa-blondi");
		animalDto.setTypeOfAnimal(TypeOfAnimal.ARACHNID);
		 
		String json = objectMapper.writeValueAsString(animalDto);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/api/animal/create").content(json)
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.date").isNotEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Aracnídeo não pode ter dentição definida"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.detail").value("uri=/api/animal/create"));	
	}
	
	@Test
	void mustEditAnAnimal() throws Exception {
		
		Animal animal= AnimalUtil.createAnimalDomain();
		animal.setDentition("");
		animal.setLabel("malayopython-reticulatus");
		animal.setTypeOfAnimal(TypeOfAnimal.ARACHNID);
		
		animal = animalRepository.save(animal);
		
		AnimalDto animalDto = new AnimalBuilder().toAnimalDto(animal);
		animalDto.setPopularNames(Arrays.asList("popular edit"));
		animalDto.setConservationState(ConservationState.DD);
		animalDto.setAntivenom("antivenom edit");
		animalDto.setEtymology("etymology edit");
		animalDto.setVenomous(false);
		animalDto.setCanCauseSeriousAccident(false);
		animalDto.setSpecies("species edit");
		animalDto.setFamily("family edit");
		animalDto.setGenre("genre edit");
		animalDto.setDentition("dentition edit");
		animalDto.setHabitat("habitat edit");
		animalDto.setCharacteristics("characteristics edit");
		animalDto.setTypeOfAnimal(TypeOfAnimal.SNAKE);
		animalDto.setAccidentSymptom("accidentSymptom edit");
		animalDto.setUrlImage("http://localhost:80");
	
		String json = objectMapper.writeValueAsString(animalDto);
		
		mockMvc.perform(MockMvcRequestBuilders.put("/api/animal/update").content(json)
			    .contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$").hasJsonPath())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("$.label").value(animalDto.getLabel()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.popularNames[0]").value(animalDto.getPopularNames().get(0)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.conservationState").value(animalDto.getConservationState().name()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.antivenom").value(animalDto.getAntivenom()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.etymology").value(animalDto.getEtymology()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.venomous").value(animalDto.getVenomous()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.canCauseSeriousAccident").value(animalDto.getCanCauseSeriousAccident()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.species").value(animalDto.getSpecies()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.family").value(animalDto.getFamily()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(animalDto.getGenre()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.dentition").value(animalDto.getDentition()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.habitat").value(animalDto.getHabitat()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.urlImage").value(animalDto.getUrlImage()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.characteristics").value(animalDto.getCharacteristics()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.accidentSymptom").value(animalDto.getAccidentSymptom()));
	}
	
	@Test
	void mustNotEditAnimalsWhenIdIsNullOrEmpty() throws Exception {
		
		final String message = "Id não pode ser null ou vazio";
		
		AnimalDto animalDto = AnimalUtil.createAnimalDto();
		animalDto.setId(null);
		
		String json = objectMapper.writeValueAsString(animalDto);
		
		mockMvc.perform(MockMvcRequestBuilders.put("/api/animal/update").content(json)
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.date").isNotEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message))
				.andExpect(MockMvcResultMatchers.jsonPath("$.detail").value("uri=/api/animal/update"));
	
	}
	
	@Test
	void mustThrowNotFoundExceptionWhenIdDoesNotExistInEdit() throws Exception {
		
		final String id = "XXXX9999";
		final String messsage ="Nenhum animal encontrado com o id: "+id;
		
		AnimalDto animalDto = AnimalUtil.createAnimalDto();
		animalDto.setId(id);
		
		String json = objectMapper.writeValueAsString(animalDto);
		
		mockMvc.perform(MockMvcRequestBuilders.put("/api/animal/update").content(json)
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isNotFound())
				.andExpect(MockMvcResultMatchers.jsonPath("$.date").isNotEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value(messsage))
				.andExpect(MockMvcResultMatchers.jsonPath("$.detail").value("uri=/api/animal/update"));	
	}
	
	@Test
	void mustThrowNotFoundException() throws Exception {

		MockMultipartFile unknown = new MockMultipartFile("file", "unknown.png", "image/jpeg", "XXX".getBytes());
		
		mockMvc.perform(MockMvcRequestBuilders.multipart(router).file(unknown)).andExpect(status().isNotFound())
				.andReturn();
	}
	
	@Test
	void mustReturnAnimalTo() throws Exception {

		AnimalInfo animalInfo = AnimalUtil.createAnimalInfo();

		mockMvc.perform(MockMvcRequestBuilders.multipart(router).file(file)).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$").hasJsonPath())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("$.label").value(animalInfo.getLabel()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.popularNames").value(animalInfo.getPopularNames()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.conservationState").value(animalInfo.getConservationState()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.antivenom").value(animalInfo.getAntivenom()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.etymology").value(animalInfo.getEtymology()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.venomous").value(animalInfo.getVenomous()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.canCauseSeriousAccident").value(animalInfo.getCanCauseSeriousAccident()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.species").value(animalInfo.getSpecies()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.family").value(animalInfo.getFamily()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(animalInfo.getGenre()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.dentition").value(animalInfo.getDentition()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.habitat").value(animalInfo.getHabitat()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.urlImage").value(animalInfo.getUrlImage()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.characteristics").value(animalInfo.getCharacteristics()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.accidentSymptom").value(animalInfo.getAccidentSymptom()));
	}

	@Test
	void whenPassingLabelItMustReturnAnimalTo() throws Exception {

		AnimalInfo animalInfo = AnimalUtil.createAnimalInfo();
		
		mockMvc.perform(MockMvcRequestBuilders.get(router + "/find/{label}", AnimalUtil.getLabel()))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$").hasJsonPath())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("$.label").value(animalInfo.getLabel()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.popularNames").value(animalInfo.getPopularNames()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.conservationState").value(animalInfo.getConservationState()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.antivenom").value(animalInfo.getAntivenom()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.etymology").value(animalInfo.getEtymology()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.venomous").value(animalInfo.getVenomous()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.canCauseSeriousAccident").value(animalInfo.getCanCauseSeriousAccident()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.species").value(animalInfo.getSpecies()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.family").value(animalInfo.getFamily()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(animalInfo.getGenre()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.dentition").value(animalInfo.getDentition()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.habitat").value(animalInfo.getHabitat()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.urlImage").value(animalInfo.getUrlImage()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.characteristics").value(animalInfo.getCharacteristics()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.accidentSymptom").value(animalInfo.getAccidentSymptom()));
	}

	@Test
	void whenPassingLabelThatDoesnExistShouldThrowNotFoundException() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get(router + "/find/{label}", "AAA")).andExpect(status().isNotFound())
				.andExpect(MockMvcResultMatchers.jsonPath("$.date").isNotEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No animals were found"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.detail").value("uri=/api/animal/information/find/AAA"));

	}
	
	@Test
	void mustReturnLabelsAvailableForRegistration() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/api/animal/find/labels/available/")).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$").hasJsonPath())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("$.[0]").value("oxyuranus-microlepidotus"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.[1]").value("titanoboa-cerrejonensis"));
	}
	
	@Test
	void mustReturnAnimalShort() throws Exception {

		Animal animal = animalRepository.findAll().stream().findFirst().get();
		
		String id = animal.getId();
		String label = animal.getLabel();
		
		mockMvc.perform(MockMvcRequestBuilders.get("/api/animal/list?page=0&size=30&sort=id,asc"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$").hasJsonPath())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(id))
				.andExpect(MockMvcResultMatchers.jsonPath("$.content[0].label").value(label))
				.andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("$.sort.sorted").value(true))
				.andExpect(MockMvcResultMatchers.jsonPath("$.size").value(30))
				.andExpect(MockMvcResultMatchers.jsonPath("$.number").value(0));
		
		mockMvc.perform(MockMvcRequestBuilders.get("/api/animal/list"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$").hasJsonPath())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(id))
				.andExpect(MockMvcResultMatchers.jsonPath("$.content[0].label").value(label))
				.andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("$.sort.sorted").value(true))
				.andExpect(MockMvcResultMatchers.jsonPath("$.size").value(15))
				.andExpect(MockMvcResultMatchers.jsonPath("$.number").value(0));
	 }

}