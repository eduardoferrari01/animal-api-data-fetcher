package br.com.animal.api.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import br.com.animal.api.domain.Animal;
import br.com.animal.api.domain.Animal.TypeOfAnimal;
import br.com.animal.api.dto.AnimalDto;
import br.com.animal.api.dto.AnimalInfo;
import br.com.animal.api.exception.NotFoundException;
import br.com.animal.api.exception.RuleException;
import br.com.animal.api.integration.CnnApiProd;
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

		AnimalDto animalDto = animalService.createNewAnimal(AnimalUtil.createAnimalDto());

		verify(cnnApi).existLabel(label);
		verify(animalRepository).existsAnimalByLabel(label);
		verify(animalRepository).save(Mockito.any(Animal.class));

		Assertions.assertNotNull(animalDto);
		Assertions.assertNotNull(animalDto.getId());
		Assertions.assertFalse(animalDto.getId().isEmpty());
		Assertions.assertEquals(returnOfTheSave.getAccidentSymptom().getDescription(), animalDto.getAccidentSymptom());
		Assertions.assertEquals(returnOfTheSave.getAntivenom(), animalDto.getAntivenom());
		Assertions.assertEquals(returnOfTheSave.getCharacteristics(), animalDto.getCharacteristics());
		Assertions.assertEquals(returnOfTheSave.getConservationState(), animalDto.getConservationState());
		Assertions.assertEquals(returnOfTheSave.getEtymology(), animalDto.getEtymology());
		Assertions.assertEquals(returnOfTheSave.getGenre(), animalDto.getGenre());
		Assertions.assertEquals(returnOfTheSave.getSpecies(), animalDto.getSpecies());
		Assertions.assertEquals(returnOfTheSave.getUrlImage(), animalDto.getUrlImage());
		Assertions.assertEquals(returnOfTheSave.getVenomous(), animalDto.getVenomous());
		Assertions.assertEquals(returnOfTheSave.getPopularNames(), animalDto.getPopularNames());
	}
	
	@Test
	void mustCreateArachnidAnimal() {

		when(cnnApi.existLabel(label)).thenReturn(Boolean.TRUE);

		when(animalRepository.existsAnimalByLabel(label)).thenReturn(Boolean.FALSE);

		Animal returnOfTheSave = AnimalUtil.createAnimalDomainWithId();
		returnOfTheSave.setDentition("");
		returnOfTheSave.setTypeOfAnimal(TypeOfAnimal.ARACHNID);
		
		when(animalRepository.save(Mockito.any())).thenReturn(returnOfTheSave);

		AnimalDto animal = AnimalUtil.createAnimalDto();
		animal.setDentition(null);
		animal.setTypeOfAnimal(TypeOfAnimal.ARACHNID);
		
		animalService.createNewAnimal(animal);
		
		animal.setDentition("");
		animalService.createNewAnimal(animal);
	}
	
	@Test
	void mustNotCreateAnimalWhenCnnDoesNotRecognizeLabel() {
		
		when(cnnApi.existLabel(label)).thenReturn(Boolean.FALSE);
		
		RuleException ruleCnn = assertThrows(RuleException.class, () -> {
			animalService.createNewAnimal(AnimalUtil.createAnimalDto());
		});
		
		Assertions.assertEquals("Cnn não reconhece a label: " + label, ruleCnn.getMessage());
	}
	
	@Test
	void mustNotBreedAnimalWhenLabelIsRegistered() {
		
		when(cnnApi.existLabel(label)).thenReturn(Boolean.TRUE);
		
		when(animalRepository.existsAnimalByLabel(label)).thenReturn(Boolean.TRUE);
		
		RuleException ruleExistLabel = assertThrows(RuleException.class, () -> {
			animalService.createNewAnimal(AnimalUtil.createAnimalDto());
		});
		
		Assertions.assertEquals("Label já cadastrada", ruleExistLabel.getMessage());
	}
	
	@Test
	void mustNotBreedAnimalWhenTheTypeIsArachnidTheDentitionIsNotNull() {
		
		when(cnnApi.existLabel(label)).thenReturn(Boolean.TRUE);
		when(animalRepository.existsAnimalByLabel(label)).thenReturn(Boolean.FALSE);
		
		AnimalDto  animal = AnimalUtil.createAnimalDto();
		animal.setTypeOfAnimal(Animal.TypeOfAnimal.ARACHNID);
		
		RuleException rule = assertThrows(RuleException.class, () -> {
			 animalService.createNewAnimal(animal);
		});
		
		Assertions.assertEquals("Aracnídeo não pode ter dentição definida", rule.getMessage());
	}
	
	@Test
	void mustEditAnAnimal() {
		
		when(animalRepository.findById(AnimalUtil.getId())).thenReturn(Optional.of(AnimalUtil.createAnimalDomainWithId()));
		when(animalRepository.save(Mockito.any())).thenReturn(AnimalUtil.createAnimalDomainWithId());
		
		AnimalDto animalReturn = animalService.update(AnimalUtil.createAnimalDtoWithId());
		
		Animal animalExpected = AnimalUtil.createAnimalDomainWithId();
		
		Assertions.assertNotNull(animalExpected);
		Assertions.assertNotNull(animalExpected.getId());
		Assertions.assertFalse(animalExpected.getId().isEmpty());
		Assertions.assertEquals(animalExpected.getAccidentSymptom().getDescription(), animalReturn.getAccidentSymptom());
		Assertions.assertEquals(animalExpected.getAntivenom(), animalReturn.getAntivenom());
		Assertions.assertEquals(animalExpected.getCharacteristics(), animalReturn.getCharacteristics());
		Assertions.assertEquals(animalExpected.getConservationState(), animalReturn.getConservationState());
		Assertions.assertEquals(animalExpected.getEtymology(), animalReturn.getEtymology());
		Assertions.assertEquals(animalExpected.getGenre(), animalReturn.getGenre());
		Assertions.assertEquals(animalExpected.getSpecies(), animalReturn.getSpecies());
		Assertions.assertEquals(animalExpected.getUrlImage(), animalReturn.getUrlImage());
		Assertions.assertEquals(animalExpected.getVenomous(), animalReturn.getVenomous());
		Assertions.assertEquals(animalExpected.getPopularNames(), animalReturn.getPopularNames());
		
		verify(animalRepository).save(Mockito.any(Animal.class));
		verify(animalRepository).findById(AnimalUtil.getId());
	}
	
	@Test
	void mustNotEditAnimalsWhenIdIsNullOrEmpty() {
		
		final String message = "Id não pode ser null ou vazio";
		
		RuleException ruleNull = assertThrows(RuleException.class, () -> {
			animalService.update(AnimalUtil.createAnimalDto());
		});
		
		Assertions.assertEquals(message, ruleNull.getMessage());
		
		RuleException ruleEmpty = assertThrows(RuleException.class, () -> {
			
			AnimalDto animal = AnimalUtil.createAnimalDto();
			animal.setId("");
			
			animalService.update(animal);
		});
		
		Assertions.assertEquals(message, ruleEmpty.getMessage());
	}
	
	@Test
	void mustThrowNotFoundExceptionWhenIdDoesNotExistInEdit(){
		
		NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> {
			animalService.update(AnimalUtil.createAnimalDtoWithId());
		});
		
		Assertions.assertEquals("Nenhum animal encontrado com o id: "+ AnimalUtil.getId(), notFoundException.getMessage());
	}
	
	@Test
	void mustReturnAnimalInfo() {
		
		when(animalRepository.findByLabel(label)).thenReturn(Optional.of(animal));
		
		AnimalInfo animalInfo =  animalService.findInfoByLabel(label);
		verify(animalRepository).findByLabel(label);
		
		Assertions.assertNotNull(animalInfo);
		Assertions.assertEquals(animal.getAccidentSymptom().getDescription(), animalInfo.getAccidentSymptom());
		Assertions.assertEquals(animal.getAntivenom(), animalInfo.getAntivenom());
		Assertions.assertEquals(animal.getCharacteristics(), animalInfo.getCharacteristics());
		Assertions.assertEquals(animal.getConservationState().getLabel(), animalInfo.getConservationState());
		Assertions.assertEquals(animal.getEtymology(), animalInfo.getEtymology());
		Assertions.assertEquals(animal.getGenre(), animalInfo.getGenre());
		Assertions.assertEquals(animal.getSpecies(), animalInfo.getSpecies());
		Assertions.assertEquals(animal.getUrlImage(), animalInfo.getUrlImage());
		Assertions.assertEquals(animal.getVenomous() ? "Sim" : "Não", animalInfo.getVenomous());
		String popularNames = String.join(", ", animal.getPopularNames());
		Assertions.assertEquals(popularNames, animalInfo.getPopularNames());
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
}
