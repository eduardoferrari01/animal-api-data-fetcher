package br.com.animal.api.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import br.com.animal.api.domain.Animal;
import br.com.animal.api.domain.Animal.TypeOfAnimal;
import br.com.animal.api.dto.AnimalDto;
import br.com.animal.api.dto.AnimalInfo;
import br.com.animal.api.dto.AnimalShort;
import br.com.animal.api.exception.NotFoundException;
import br.com.animal.api.exception.RuleException;
import br.com.animal.api.integration.CnnApiProd;
import br.com.animal.api.integration.IntegrationResponse;
import br.com.animal.api.repository.AnimalDAL;
import br.com.animal.api.repository.AnimalRepository;
import br.com.animal.api.util.AnimalUtil;

@ExtendWith(MockitoExtension.class)
public class AnimalServiceTest {

	@InjectMocks
	private AnimalService animalService;
	@Mock
	private AnimalRepository animalRepository;
	@Mock
	private CnnApiProd cnnApi;
	private static Animal animal;
	private static String label;
	@Mock
	private AnimalDAL animalDAL;
	
	@BeforeAll
	public static void setup() {
		
		label =  AnimalUtil.getLabel();
		animal = AnimalUtil.createAnimalDomain();
	}
	
	@Test
	void mustCreateNewAnimal() {

		when(cnnApi.existLabel(label)).thenReturn(Boolean.TRUE);

		when(animalRepository.existsAnimalByLabel(label)).thenReturn(Boolean.FALSE);

		Animal returnOfTheSave = AnimalUtil.createAnimalDomainWithId();
		
		when(animalRepository.save(Mockito.any())).thenReturn(returnOfTheSave);

		AnimalDto animalDto = animalService.createNewAnimal(AnimalUtil.animalDto().build());

		verify(cnnApi).existLabel(label);
		verify(animalRepository).existsAnimalByLabel(label);
		verify(animalRepository).save(Mockito.any(Animal.class));

		Assertions.assertNotNull(animalDto);
		Assertions.assertNotNull(animalDto.id());
		Assertions.assertFalse(animalDto.id().isEmpty());
		Assertions.assertEquals(returnOfTheSave.getAccidentSymptom().getDescription(), animalDto.accidentSymptom());
		Assertions.assertEquals(returnOfTheSave.getAntivenom(), animalDto.antivenom());
		Assertions.assertEquals(returnOfTheSave.getCharacteristics(), animalDto.characteristics());
		Assertions.assertEquals(returnOfTheSave.getConservationState(), animalDto.conservationState());
		Assertions.assertEquals(returnOfTheSave.getEtymology(), animalDto.etymology());
		Assertions.assertEquals(returnOfTheSave.getGenre(), animalDto.genre());
		Assertions.assertEquals(returnOfTheSave.getSpecies(), animalDto.species());
		Assertions.assertEquals(returnOfTheSave.getUrlImage(), animalDto.urlImage());
		Assertions.assertEquals(returnOfTheSave.getVenomous(), animalDto.venomous());
		Assertions.assertEquals(returnOfTheSave.getPopularNames(), animalDto.popularNames());
	}
	
	@Test
	void mustCreateArachnidAnimal() {

		when(cnnApi.existLabel(label)).thenReturn(Boolean.TRUE);

		when(animalRepository.existsAnimalByLabel(label)).thenReturn(Boolean.FALSE);

		Animal returnOfTheSave = AnimalUtil.createAnimalDomainWithId();
		returnOfTheSave.setDentition("");
		returnOfTheSave.setTypeOfAnimal(TypeOfAnimal.ARACHNID);
		
		when(animalRepository.save(Mockito.any())).thenReturn(returnOfTheSave);

		Animal animal = AnimalUtil.createAnimalDomain();
		animal.setDentition(null);
		animal.setTypeOfAnimal(TypeOfAnimal.ARACHNID);
		
		animalService.createNewAnimal(AnimalUtil.animalDto(animal).build());
		
		animal.setDentition("");
		
		animalService.createNewAnimal(AnimalUtil.animalDto(animal).build());
	}
	
	@Test
	void mustNotCreateAnimalWhenCnnDoesNotRecognizeLabel() {
		
		when(cnnApi.existLabel(label)).thenReturn(Boolean.FALSE);
		
		RuleException ruleCnn = assertThrows(RuleException.class, () -> {
			animalService.createNewAnimal(AnimalUtil.animalDto().build());
		});
		
		Assertions.assertEquals("Cnn não reconhece a label: " + label, ruleCnn.getMessage());
	}
	
	@Test
	void mustNotBreedAnimalWhenLabelIsRegistered() {
		
		when(cnnApi.existLabel(label)).thenReturn(Boolean.TRUE);
		
		when(animalRepository.existsAnimalByLabel(label)).thenReturn(Boolean.TRUE);
		
		RuleException ruleExistLabel = assertThrows(RuleException.class, () -> {
			animalService.createNewAnimal(AnimalUtil.animalDto().build());
		});
		
		Assertions.assertEquals("Label já cadastrada", ruleExistLabel.getMessage());
	}
	
	@Test
	void mustNotBreedAnimalWhenTheTypeIsArachnidTheDentitionIsNotNull() {
		
		when(cnnApi.existLabel(label)).thenReturn(Boolean.TRUE);
		when(animalRepository.existsAnimalByLabel(label)).thenReturn(Boolean.FALSE);
		
		Animal animal = AnimalUtil.createAnimalDomain();
		String dentition = animal.getDentition();
		animal.setDentition(null);
		animal.setTypeOfAnimal(Animal.TypeOfAnimal.ARACHNID);
		animal.setDentition(dentition);
		
		RuleException rule = assertThrows(RuleException.class, () -> {
			 animalService.createNewAnimal(AnimalUtil.animalDto(animal).build());
		});
		
		Assertions.assertEquals("Aracnídeo não pode ter dentição definida", rule.getMessage());
	}
	
	@Test
	void mustEditAnAnimal() {
		
		when(animalRepository.findById(AnimalUtil.getId())).thenReturn(Optional.of(AnimalUtil.createAnimalDomainWithId()));
		when(animalRepository.save(Mockito.any())).thenReturn(AnimalUtil.createAnimalDomainWithId());
		
		AnimalDto animalReturn = animalService.update(AnimalUtil.animalDto(AnimalUtil.createAnimalDomainWithId()).build());
		
		Animal animalExpected = AnimalUtil.createAnimalDomainWithId();
		
		Assertions.assertNotNull(animalExpected);
		Assertions.assertNotNull(animalExpected.getId());
		Assertions.assertFalse(animalExpected.getId().isEmpty());
		Assertions.assertEquals(animalExpected.getAccidentSymptom().getDescription(), animalReturn.accidentSymptom());
		Assertions.assertEquals(animalExpected.getAntivenom(), animalReturn.antivenom());
		Assertions.assertEquals(animalExpected.getCharacteristics(), animalReturn.characteristics());
		Assertions.assertEquals(animalExpected.getConservationState(), animalReturn.conservationState());
		Assertions.assertEquals(animalExpected.getEtymology(), animalReturn.etymology());
		Assertions.assertEquals(animalExpected.getGenre(), animalReturn.genre());
		Assertions.assertEquals(animalExpected.getSpecies(), animalReturn.species());
		Assertions.assertEquals(animalExpected.getUrlImage(), animalReturn.urlImage());
		Assertions.assertEquals(animalExpected.getVenomous(), animalReturn.venomous());
		Assertions.assertEquals(animalExpected.getPopularNames(), animalReturn.popularNames());
		
		verify(animalRepository).save(Mockito.any(Animal.class));
		verify(animalRepository).findById(AnimalUtil.getId());
	}
	
	@Test
	void mustNotEditAnimalsWhenIdIsNullOrEmpty() {
		
		final String message = "Id não pode ser null ou vazio";
		
		RuleException ruleNull = assertThrows(RuleException.class, () -> {
			animalService.update(AnimalUtil.animalDto().build());
		});
		
		Assertions.assertEquals(message, ruleNull.getMessage());
		
		RuleException ruleEmpty = assertThrows(RuleException.class, () -> {
			
			Animal animal = AnimalUtil.createAnimalDomain();
			animal.setId("");
			
			animalService.update(AnimalUtil.animalDto(animal).build());
		});
		
		Assertions.assertEquals(message, ruleEmpty.getMessage());
	}
	
	@Test
	void mustThrowNotFoundExceptionWhenIdDoesNotExistInEdit(){
		
		NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> {
			
			animalService.update(AnimalUtil.animalDto(AnimalUtil.createAnimalDomainWithId()).build());
		});
		
		Assertions.assertEquals("Nenhum animal encontrado com o id: "+ AnimalUtil.getId(), notFoundException.getMessage());
	}
	
	@Test
	void mustReturnAnimalInfo() {
		
		when(animalRepository.findByLabel(label)).thenReturn(Optional.of(animal));
		
		AnimalInfo animalInfo =  animalService.findInfoByLabel(label);
		verify(animalRepository).findByLabel(label);
		
		Assertions.assertNotNull(animalInfo);
		Assertions.assertEquals(animal.getAccidentSymptom().getDescription(), animalInfo.accidentSymptom());
		Assertions.assertEquals(animal.getAntivenom(), animalInfo.antivenom());
		Assertions.assertEquals(animal.getCharacteristics(), animalInfo.characteristics());
		Assertions.assertEquals(animal.getConservationState().getLabel(), animalInfo.conservationState());
		Assertions.assertEquals(animal.getEtymology(), animalInfo.etymology());
		Assertions.assertEquals(animal.getGenre(), animalInfo.genre());
		Assertions.assertEquals(animal.getSpecies(), animalInfo.species());
		Assertions.assertEquals(animal.getUrlImage(), animalInfo.urlImage());
		Assertions.assertEquals(animal.getVenomous() ? "Sim" : "Não", animalInfo.venomous());
		String popularNames = String.join(", ", animal.getPopularNames());
		Assertions.assertEquals(popularNames, animalInfo.popularNames());
	}
	
	@Test
	void shouldThrowNotFoundException() {
		 
		NotFoundException notFoundException  = assertThrows(NotFoundException.class, () -> {
			animalService.findInfoByLabel(label);
		});
		
		Assertions.assertEquals("No animals were found", notFoundException.getMessage());
	}
	
	@Test
	void mustReturnLabelsAvailableForRegistration() {
		
		when(cnnApi.getAllLabels()).thenReturn(Arrays.asList("label1", "label2", "label3", "label4"));
		
		Animal animal1 = new Animal();
		animal1.setLabel("label1");
		Animal animal2 = new Animal();
		animal2.setLabel("label2");
		
		when(animalRepository.findByLabelIn(Mockito.anyList())).thenReturn(Arrays.asList(animal1, animal2));
		
		List<String> labels = animalService.findLabelsAvailableToRegister();
	
		verify(cnnApi).getAllLabels();
		verify(animalRepository).findByLabelIn(Mockito.anyList());
		
		Assertions.assertNotNull(labels);
		Assertions.assertFalse(labels.isEmpty());
		Assertions.assertEquals(2, labels.size());
		Assertions.assertIterableEquals(Arrays.asList("label3", "label4"), labels);
	}
	
	@Test
	void mustReturnAnimalShort() {
		
		PageImpl<Animal> pageImpl = new PageImpl<>(Arrays.asList(AnimalUtil.createAnimalDomainWithId()), PageRequest.of(0, 10, Direction.ASC, "id"), 0);
		
		when(animalRepository.findAll(Mockito.any(Pageable.class))).thenReturn(pageImpl);
		
		Page<AnimalShort> page = animalService.findAllShort(PageRequest.of(0, 10, Direction.ASC, "id"));
		
		verify(animalRepository).findAll(Mockito.any(Pageable.class));
		
		Assertions.assertNotNull(page);
		Assertions.assertFalse(page.isEmpty());
		Assertions.assertEquals(0, page.getNumber());
		Assertions.assertEquals(10, page.getSize());
		Assertions.assertEquals(1, page.getTotalElements());
		Assertions.assertEquals(1, page.getTotalPages());
		Assertions.assertTrue(page.getSort().isSorted());
		
		AnimalShort animalShort = page.getContent().get(0);
		Assertions.assertEquals(AnimalUtil.getId(), animalShort.id());
		Assertions.assertEquals(AnimalUtil.getLabel(), animalShort.label());
	
	}
	
	@Test
	void whenPassingDescriptionItMustReturnAnimal() {
		
		PageImpl<Animal> pageImpl = new PageImpl<>(Arrays.asList(AnimalUtil.createAnimalDomainWithId()), PageRequest.of(0, 10, Direction.ASC, "id"), 0);
		
		when(animalDAL.findAnimalByDescription(Mockito.any(Pageable.class), Mockito.anyString())).thenReturn(pageImpl);

		String description = "dendroaspis";
		
		Page<AnimalShort> page = animalService.findAnimalByDescription(PageRequest.of(0, 10, Direction.ASC, "id"), description);
		
		verify(animalDAL).findAnimalByDescription(Mockito.any(Pageable.class), Mockito.anyString());
		
		Assertions.assertNotNull(page);
		Assertions.assertFalse(page.isEmpty());
		Assertions.assertEquals(0, page.getNumber());
		Assertions.assertEquals(10, page.getSize());
		Assertions.assertEquals(1, page.getTotalElements());
		Assertions.assertEquals(1, page.getTotalPages());
		Assertions.assertTrue(page.getSort().isSorted());
		
		AnimalShort animalShort = page.getContent().get(0);
		Assertions.assertEquals(AnimalUtil.getId(), animalShort.id());
		Assertions.assertEquals(AnimalUtil.getLabel(), animalShort.label());
	}
	
	@Test
	void teste() throws IOException {
		
		IntegrationResponse response = new IntegrationResponse();
		response.setLabel(animal.getLabel());
		
		when(cnnApi.classify(Mockito.any())).thenReturn(response);
		when(animalRepository.findByLabel(label)).thenReturn(Optional.of(animal));
		 
		MultipartFile multipartFile = new MockMultipartFile("file", "filename.png", "image/jpeg", "img".getBytes());
		
		AnimalInfo animalInfo =  animalService.findInfoByImage(multipartFile);
		verify(cnnApi).classify(Mockito.any());
		verify(animalRepository).findByLabel(label);
		
		Assertions.assertNotNull(animalInfo);
		Assertions.assertEquals(animal.getAccidentSymptom().getDescription(), animalInfo.accidentSymptom());
		Assertions.assertEquals(animal.getAntivenom(), animalInfo.antivenom());
		Assertions.assertEquals(animal.getCharacteristics(), animalInfo.characteristics());
		Assertions.assertEquals(animal.getConservationState().getLabel(), animalInfo.conservationState());
		Assertions.assertEquals(animal.getEtymology(), animalInfo.etymology());
		Assertions.assertEquals(animal.getGenre(), animalInfo.genre());
		Assertions.assertEquals(animal.getSpecies(), animalInfo.species());
		Assertions.assertEquals(animal.getUrlImage(), animalInfo.urlImage());
		Assertions.assertEquals(animal.getVenomous() ? "Sim" : "Não", animalInfo.venomous());
		String popularNames = String.join(", ", animal.getPopularNames());
		Assertions.assertEquals(popularNames, animalInfo.popularNames());
	}
	
}
