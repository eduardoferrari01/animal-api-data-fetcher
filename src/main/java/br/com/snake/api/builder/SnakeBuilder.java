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

	public SnakeBuilder accidentSymptom() {
		snakeTo.setAccidentSymptom(snake.getAccidentSymptom().getDescription());
		return this;
	}

	public SnakeBuilder antivenom() {

		snakeTo.setAntivenom(snake.getAntivenom());
		return this;
	}

	public SnakeBuilder venomous() {

		snakeTo.setVenomous(snake.getVenomous());
		return this;
	}

	public SnakeBuilder species() {

		snakeTo.setSpecies(snake.getSpecies());
		return this;
	}

	public SnakeBuilder popularNames() {

		snakeTo.setPopularNames(snake.getPopularNames());
		return this;
	}

	public SnakeBuilder genre() {

		snakeTo.setGenre(snake.getGenre());
		return this;
	}

	public SnakeBuilder etymology() {
		snakeTo.setEtymology(snake.getEtymology());
		return this;
	}

	public SnakeBuilder conservationState() {

		snakeTo.setConservationState(snake.getConservationState());
		return this;
	}

	public SnakeBuilder characteristics() {

		snakeTo.setCharacteristics(snake.getCharacteristics());
		return this;
	}

	public SnakeBuilder imagem() {
		
		snakeTo.setUrlImage(snake.getUrlImage());
		return this;
	}
	
	public SnakeBuilder imageGeographicDistribution() {
		
		snakeTo.setUrlImageGeographicDistribution(snake.getUrlImageGeographicDistribution());
		return this;
	}
	
	public SnakeTo builder() {

		LOG.info("DTO created: {}", snakeTo);
		return snakeTo;
	}
}
