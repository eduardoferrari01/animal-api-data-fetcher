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
import br.com.snake.api.domain.Snake;
import br.com.snake.api.dto.SnakeTo;
import br.com.snake.api.repository.SnakeRepository;
import br.com.snake.api.util.SnakeUtil;

@ExtendWith(MockitoExtension.class)
public class SnakeServiceTest {

	@InjectMocks
	private SnakeService snakeService;
	@Mock
	private SnakeRepository snakeRepository;
	private static Snake snake;
	private static String label;
	
	@BeforeAll
	public static void setup() {
		
		label = "dendroaspis-polylepis";
		snake = SnakeUtil.createSnakeDomain();
	}
	
	@Test
	void mustReturnSnakeTo() {
		
		when(snakeRepository.findByLabel(label)).thenReturn(Optional.of(snake));
		
		SnakeTo snakeTo =  snakeService.findByLabel(label);
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
