package br.com.snake.api.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.snake.api.controller.NotFoundException;
import br.com.snake.api.domain.Animal;
import br.com.snake.api.dto.AnimalTo;
import br.com.snake.api.repository.AnimalRepository;
import br.com.snake.api.util.AnimalUtil;

@ExtendWith(MockitoExtension.class)
public class AnimalServiceTest {

	@InjectMocks
	private AnimalService animalService;
	@Mock
	private AnimalRepository animalRepository;
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
		Assertions.assertEquals(animal.getConservationState(), animalTo.getConservationState());
		Assertions.assertEquals(animal.getEtymology(), animalTo.getEtymology());
		Assertions.assertEquals(animal.getGenre(), animalTo.getGenre());
		Assertions.assertEquals(animal.getSpecies(), animalTo.getSpecies());
		Assertions.assertEquals(animal.getUrlImage(), animalTo.getUrlImage());
		Assertions.assertEquals(animal.getVenomous(), animalTo.getVenomous());
		String popularNames = String.join(", ", animal.getPopularNames());
		Assertions.assertEquals(popularNames, animalTo.getPopularNames());
	}
	
	@Test
	void shouldThrowNotFoundException() {
		 
		assertThrows(NotFoundException.class, () -> {
			animalService.findByLabel(label);
		});
		
	}
	
}
