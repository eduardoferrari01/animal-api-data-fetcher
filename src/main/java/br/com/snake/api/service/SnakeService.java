package br.com.snake.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.snake.api.builder.SnakeBuilder;
import br.com.snake.api.controller.NotFoundException;
import br.com.snake.api.domain.Snake;
import br.com.snake.api.dto.SnakeTo;
import br.com.snake.api.repository.SnakeRepository;

@Service
public class SnakeService {

	@Autowired
	private SnakeRepository snakeRepository;
	private static final Logger LOG = LoggerFactory.getLogger(SnakeService.class);

	public SnakeTo findByLabel(String label) {

		LOG.info("Find by label: {}", label);

		Snake snake = snakeRepository.findByLabel(label)
				.orElseThrow(() -> new NotFoundException("No animals were found"));

		LOG.info("{} found!!!", label);

		return new SnakeBuilder(snake)
				.accidentSymptom()
				.antivenom()
				.characteristics()
				.conservationState()
				.etymology()
				.genre()
				.popularNames()
				.species()
				.venomous()
				.imagem()
				.imageGeographicDistribution().builder();
	}
}
