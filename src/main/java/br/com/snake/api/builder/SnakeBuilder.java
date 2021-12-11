package br.com.snake.api.builder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.snake.api.domain.Snake;
import br.com.snake.api.dto.SnakeTo;

public class SnakeBuilder {

	private Snake snake;
	private SnakeTo snakeTo;
	private static final Logger LOG = LoggerFactory.getLogger(SnakeBuilder.class);
	
	public SnakeBuilder(Snake snake) {

		this.snake = snake;
		this.snakeTo = new SnakeTo();
	}

	public SnakeTo builder() {
		
		snakeTo.setLabel(snake.getLabel());
		snakeTo.setAccidentSymptom(snake.getAccidentSymptom().getDescription());
		snakeTo.setAntivenom(snake.getAntivenom());
		snakeTo.setVenomous(snake.getVenomous());
		snakeTo.setCanCauseSeriousAccident(snake.getCanCauseSeriousAccident());
		snakeTo.setSpecies(snake.getSpecies());
		snakeTo.setPopularNames(String.join(", ", snake.getPopularNames()));
		snakeTo.setFamily(snake.getFamily());
		snakeTo.setGenre(snake.getGenre());
		snakeTo.setDentition(snake.getDentition());
		snakeTo.setHabitat(snake.getHabitat());
		snakeTo.setEtymology(snake.getEtymology());
		snakeTo.setConservationState(snake.getConservationState());
		snakeTo.setCharacteristics(snake.getCharacteristics());
		snakeTo.setUrlImage(snake.getUrlImage());
		
		LOG.info("DTO created: {}", snakeTo);
		
		return snakeTo;
	}
}
