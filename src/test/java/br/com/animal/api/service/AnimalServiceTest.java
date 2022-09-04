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

import br.com.animal.api.controller.NotFoundException;
import br.com.animal.api.domain.Animal;
import br.com.animal.api.dto.AnimalTo;
import br.com.animal.api.integration.CnnApi;
import br.com.animal.api.repository.AnimalRepository;
import br.com.animal.api.util.AnimalUtil;

@ExtendWith(MockitoExtension.class)
public class AnimalServiceTest {

	@InjectMocks
	private AnimalService animalService;
	@Mock
	private AnimalRepository animalRepository;
	@Mock
	private CnnApi cnnApi;
	private static Animal animal;
	private static String label;
	
	@BeforeAll
	public static void setup() {
		
		label = "dendroaspis-polylepis";
		animal = AnimalUtil.createAnimalDomain();
	}
	
	@Test
	void mustReturnAnimalTo() {
		
		when(animalRepository.findByLabel(label)).thenReturn(Optional.of(animal));
		
		AnimalTo animalTo =  animalService.findByLabel(label);
		verify(animalRepository).findByLabel(label);
		
		Assertions.assertNotNull(animalTo);
		Assertions.assertEquals(animal.getAccidentSymptom().getDescription(), animalTo.getAccidentSymptom());
		Assertions.assertEquals(animal.getAntivenom(), animalTo.getAntivenom());
		Assertions.assertEquals(animal.getCharacteristics(), animalTo.getCharacteristics());
		Assertions.assertEquals(animal.getConservationState().getLabel(), animalTo.getConservationState());
		Assertions.assertEquals(animal.getEtymology(), animalTo.getEtymology());
		Assertions.assertEquals(animal.getGenre(), animalTo.getGenre());
		Assertions.assertEquals(animal.getSpecies(), animalTo.getSpecies());
		Assertions.assertEquals(animal.getUrlImage(), animalTo.getUrlImage());
		Assertions.assertEquals(animal.getVenomous() ? "Sim" : "Não", animalTo.getVenomous());
		String popularNames = String.join(", ", animal.getPopularNames());
		Assertions.assertEquals(popularNames, animalTo.getPopularNames());
	}
	
	@Test
	void shouldThrowNotFoundException() {
		 
		assertThrows(NotFoundException.class, () -> {
			animalService.findByLabel(label);
		});
		
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
