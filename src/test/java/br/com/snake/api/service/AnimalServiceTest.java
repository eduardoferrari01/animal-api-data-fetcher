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
	private AnimalService snakeService;
	@Mock
	private AnimalRepository snakeRepository;
	private static Animal snake;
	private static String label;
	
	@BeforeAll
	public static void setup() {
		
		label = "dendroaspis-polylepis";
		snake = AnimalUtil.createAnimalDomain();
	}
	
	@Test
	void mustReturnSnakeTo() {
		
		when(snakeRepository.findByLabel(label)).thenReturn(Optional.of(snake));
		
		AnimalTo snakeTo =  snakeService.findByLabel(label);
		verify(snakeRepository).findByLabel(label);
		
		Assertions.assertNotNull(snakeTo);
		Assertions.assertEquals(snake.getAccidentSymptom().getDescription(), snakeTo.getAccidentSymptom());
		Assertions.assertEquals(snake.getAntivenom(), snakeTo.getAntivenom());
		Assertions.assertEquals(snake.getCharacteristics(), snakeTo.getCharacteristics());
		Assertions.assertEquals(snake.getConservationState(), snakeTo.getConservationState());
		Assertions.assertEquals(snake.getEtymology(), snakeTo.getEtymology());
		Assertions.assertEquals(snake.getGenre(), snakeTo.getGenre());
		Assertions.assertEquals(snake.getSpecies(), snakeTo.getSpecies());
		Assertions.assertEquals(snake.getUrlImage(), snakeTo.getUrlImage());
		Assertions.assertEquals(snake.getVenomous(), snakeTo.getVenomous());
		String popularNames = String.join(", ", snake.getPopularNames());
		Assertions.assertEquals(popularNames, snakeTo.getPopularNames());
	}
	
	@Test
	void shouldThrowNotFoundException() {
		 
		assertThrows(NotFoundException.class, () -> {
			snakeService.findByLabel(label);
		});
		
	}
	
}
