package br.com.snake.api.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.snake.api.domain.Snake;

public interface SnakeRepository extends MongoRepository<Snake,String> {

	Optional<Snake> findByLabel(String label);

}
