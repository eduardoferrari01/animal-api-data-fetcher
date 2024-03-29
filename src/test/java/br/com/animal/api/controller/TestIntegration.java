package br.com.animal.api.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.animal.api.builder.AnimalBuilder;
import br.com.animal.api.configuration.security.AutenticacaoTokenService;
import br.com.animal.api.domain.AccidentSymptom;
import br.com.animal.api.domain.Animal;
import br.com.animal.api.domain.Animal.TypeOfAnimal;
import br.com.animal.api.domain.ConservationState;
import br.com.animal.api.dto.AnimalDto;
import br.com.animal.api.dto.AnimalInfo;
import br.com.animal.api.dto.TokenDTO;
import br.com.animal.api.repository.AnimalRepository;
import br.com.animal.api.repository.UserRepository;
import br.com.animal.api.util.AnimalUtil;
import br.com.animal.api.util.AuthenticateUserUtil;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestIntegration {

	private static final Logger LOG = LoggerFactory.getLogger(TestIntegration.class);
	
	@Autowired
	private AnimalRepository animalRepository;
	@Autowired
	private MockMvc mockMvc;
	private static MockMultipartFile file;
	private String router = "/api/animal/information";
	private static ObjectMapper objectMapper;
	//Spring security
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AutenticacaoTokenService autenticacaoService;
	@Autowired
	private AuthenticationManager authManager;
	private String headerName;
	private String headerValues;
	
	@BeforeAll
	void setup() throws IOException {

		URL url = new URL("https://en.wikipedia.org/wiki/Bothrops_jararaca#/media/File:Bothrops_jararaca.jpg");
		InputStream is = url.openStream();
		file = new MockMultipartFile("file", "filename.png", "image/jpeg", is.readAllBytes());
		objectMapper = new ObjectMapper();
		
		animalRepository.save(AnimalUtil.createAnimalDomain());
		
		authentication();
	}
	
	private void authentication() {

		try {
			userRepository.deleteAll();
			userRepository.save(AuthenticateUserUtil.userCreate());
			TokenDTO tokenDTO = autenticacaoService.authenticate(AuthenticateUserUtil.LoginFormCreate().loginForm().build(),
					authManager);
			headerName = AuthenticateUserUtil.getHeaderName();
			headerValues = AuthenticateUserUtil.getHeaderValues(tokenDTO.toke());

		} catch (BadCredentialsException e) {
			LOG.error(e.getLocalizedMessage(), e);
		}
	}
	
	@Test
	void mustCreateNewAnimal() throws Exception {
		
		Animal animal = AnimalUtil.createAnimalDomain();
		animal.setLabel("ophiophagus-hannah");
		
		AnimalDto animalDto = AnimalUtil.animalDto(animal).build();
		
		String json = objectMapper.writeValueAsString(animalDto);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/api/animal/create").content(json)
				.contentType(MediaType.APPLICATION_JSON_VALUE).header(headerName, headerValues))
				.andExpect(status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$").hasJsonPath())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("$.label").value(animalDto.label()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.popularNames[0]").value(animalDto.popularNames().get(0)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.conservationState").value(animalDto.conservationState().name()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.antivenom").value(animalDto.antivenom()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.etymology").value(animalDto.etymology()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.venomous").value(animalDto.venomous()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.canCauseSeriousAccident").value(animalDto.canCauseSeriousAccident()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.species").value(animalDto.species()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.family").value(animalDto.family()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(animalDto.genre()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.dentition").value(animalDto.dentition()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.habitat").value(animalDto.habitat()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.urlImage").value(animalDto.urlImage()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.characteristics").value(animalDto.characteristics()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.accidentSymptom").value(animalDto.accidentSymptom()));
	}
	
	@Test
	void mustNotCreateAnimalWhenCnnDoesNotRecognizeLabel() throws Exception {
		
		Animal animal = AnimalUtil.createAnimalDomain();
		animal.setLabel("XXX");
		
		String json = objectMapper.writeValueAsString(AnimalUtil.animalDto(animal).build());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/api/animal/create").content(json)
				.contentType(MediaType.APPLICATION_JSON_VALUE).header(headerName, headerValues))
				.andExpect(status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.date").isNotEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Cnn não reconhece a label: XXX"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.detail").value("uri=/api/animal/create"));	
						 
	}
	
	@Test
	void mustNotBreedAnimalWhenLabelIsRegistered() throws Exception {
		
		String label = "dendroaspis-polylepis";
		
		Animal animal = AnimalUtil.createAnimalDomain();
		animal.setLabel(label);
		
		String json = objectMapper.writeValueAsString(AnimalUtil.animalDto(animal).build());
		
 		mockMvc.perform(MockMvcRequestBuilders.post("/api/animal/create").content(json)
				.contentType(MediaType.APPLICATION_JSON_VALUE).header(headerName, headerValues))
				.andExpect(status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.date").isNotEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Label: "+label+" já cadastrada"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.detail").value("uri=/api/animal/create"));	
	}
	
	@Test
	void mustNotBreedArachnidTypeAnimalWhenDentitionIsDefined() throws Exception {
	 
		Animal animal = AnimalUtil.createAnimalDomain();
		animal.setLabel("theraphosa-blondi");
		String dentition = animal.getDentition();
		animal.setDentition(null);
		animal.setTypeOfAnimal(TypeOfAnimal.ARACHNID);
		animal.setDentition(dentition);
		
		String json = objectMapper.writeValueAsString(AnimalUtil.animalDto(animal).build());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/api/animal/create").content(json)
				.contentType(MediaType.APPLICATION_JSON_VALUE).header(headerName, headerValues))
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
		
		animal.setPopularNames(Arrays.asList("popular edit"));
		animal.setConservationState(ConservationState.DD);
		animal.setAntivenom("antivenom edit");
		animal.setEtymology("etymology edit");
		animal.setVenomous(false);
		animal.setCanCauseSeriousAccident(false);
		animal.setSpecies("species edit");
		animal.setFamily("family edit");
		animal.setGenre("genre edit");
		animal.setDentition("dentition edit");
		animal.setHabitat("habitat edit");
		animal.setCharacteristics("characteristics edit");
		animal.setTypeOfAnimal(TypeOfAnimal.SNAKE);
		AccidentSymptom accidentSymptom = animal.getAccidentSymptom();
		accidentSymptom.setDescription("accidentSymptom edit");
		animal.setAccidentSymptom(accidentSymptom);
		animal.setUrlImage("http://localhost:80");
	
		AnimalDto animalDto = AnimalUtil.animalDto(animal).build();
		
		String json = objectMapper.writeValueAsString(animalDto);
		
		mockMvc.perform(MockMvcRequestBuilders.put("/api/animal/update").content(json)
			    .contentType(MediaType.APPLICATION_JSON_VALUE).header(headerName, headerValues))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$").hasJsonPath())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("$.label").value(animalDto.label()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.popularNames[0]").value(animalDto.popularNames().get(0)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.conservationState").value(animalDto.conservationState().name()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.antivenom").value(animalDto.antivenom()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.etymology").value(animalDto.etymology()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.venomous").value(animalDto.venomous()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.canCauseSeriousAccident").value(animalDto.canCauseSeriousAccident()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.species").value(animalDto.species()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.family").value(animalDto.family()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(animalDto.genre()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.dentition").value(animalDto.dentition()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.habitat").value(animalDto.habitat()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.urlImage").value(animalDto.urlImage()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.characteristics").value(animalDto.characteristics()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.accidentSymptom").value(animalDto.accidentSymptom()));
	}
	
	@Test
	void mustNotEditAnimalsWhenIdIsNullOrEmpty() throws Exception {
		
		final String message = "Id não pode ser null ou vazio";
		
		String json = objectMapper.writeValueAsString(AnimalUtil.animalDto(AnimalUtil.createAnimalDomain()).build());

		mockMvc.perform(MockMvcRequestBuilders.put("/api/animal/update").content(json)
				.contentType(MediaType.APPLICATION_JSON_VALUE).header(headerName, headerValues))
				.andExpect(status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.date").isNotEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message))
				.andExpect(MockMvcResultMatchers.jsonPath("$.detail").value("uri=/api/animal/update"));
	
	}
	
	@Test
	void mustThrowNotFoundExceptionWhenIdDoesNotExistInEdit() throws Exception {
		
		final String id = "XXXX9999";
		final String messsage ="Nenhum animal encontrado com o id: "+id;
		
		Animal animal = AnimalUtil.createAnimalDomain();
		animal.setId(id);
		 
		String json = objectMapper.writeValueAsString(AnimalUtil.animalDto(animal).build());
		
		mockMvc.perform(MockMvcRequestBuilders.put("/api/animal/update").content(json)
				.contentType(MediaType.APPLICATION_JSON_VALUE).header(headerName, headerValues))
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
				.andExpect(MockMvcResultMatchers.jsonPath("$.label").value(animalInfo.label()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.popularNames").value(animalInfo.popularNames()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.conservationState").value(animalInfo.conservationState()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.antivenom").value(animalInfo.antivenom()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.etymology").value(animalInfo.etymology()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.venomous").value(animalInfo.venomous()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.canCauseSeriousAccident").value(animalInfo.canCauseSeriousAccident()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.species").value(animalInfo.species()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.family").value(animalInfo.family()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(animalInfo.genre()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.dentition").value(animalInfo.dentition()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.habitat").value(animalInfo.habitat()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.urlImage").value(animalInfo.urlImage()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.characteristics").value(animalInfo.characteristics()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.accidentSymptom").value(animalInfo.accidentSymptom()));
	}

	@Test
	void whenPassingLabelItMustReturnAnimalTo() throws Exception {

		AnimalInfo animalInfo = AnimalUtil.createAnimalInfo();
		
		mockMvc.perform(MockMvcRequestBuilders.get(router + "/find/{label}", AnimalUtil.getLabel()))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$").hasJsonPath())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("$.label").value(animalInfo.label()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.popularNames").value(animalInfo.popularNames()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.conservationState").value(animalInfo.conservationState()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.antivenom").value(animalInfo.antivenom()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.etymology").value(animalInfo.etymology()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.venomous").value(animalInfo.venomous()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.canCauseSeriousAccident").value(animalInfo.canCauseSeriousAccident()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.species").value(animalInfo.species()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.family").value(animalInfo.family()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(animalInfo.genre()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.dentition").value(animalInfo.dentition()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.habitat").value(animalInfo.habitat()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.urlImage").value(animalInfo.urlImage()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.characteristics").value(animalInfo.characteristics()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.accidentSymptom").value(animalInfo.accidentSymptom()));
	}

	@Test
	void whenPassingLabelThatDoesnExistShouldThrowNotFoundException() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get(router + "/find/{label}", "AAA")).andExpect(status().isNotFound())
				.andExpect(MockMvcResultMatchers.jsonPath("$.date").isNotEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Nenhum animal foi encontrado"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.detail").value("uri=/api/animal/information/find/AAA"));

	}
	
	@Test
	void mustReturnLabelsAvailableForRegistration() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/api/animal/find/labels/available/")
				.header(headerName, headerValues))
				.andExpect(status().isOk())
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
	 
		mockMvc.perform(MockMvcRequestBuilders.get("/api/animal/list?page=0&size=30&sort=id,asc")
				.header(headerName, headerValues))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$").hasJsonPath())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(id))
				.andExpect(MockMvcResultMatchers.jsonPath("$.content[0].label").value(label))
				.andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("$.sort.sorted").value(true))
				.andExpect(MockMvcResultMatchers.jsonPath("$.size").value(30))
				.andExpect(MockMvcResultMatchers.jsonPath("$.number").value(0));
		
		mockMvc.perform(MockMvcRequestBuilders.get("/api/animal/list")
				.header(headerName, headerValues))
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
	
	@Test
	void whenPassingDescriptionItMustReturnAnimal() throws Exception {

		Animal animal = animalRepository.findAll().stream().findFirst().get();
		
		String id = animal.getId();
		String label = animal.getLabel();
		String genre = animal.getGenre();
		mockMvc.perform(MockMvcRequestBuilders.get("/api/animal/find/description/"+genre+"?page=0&size=15&sort=label")
				.header(headerName, headerValues))
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