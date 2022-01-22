package br.com.snake.api.repository;

import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.snake.api.domain.Animal;

public interface AnimalRepository extends MongoRepository<Animal,String> {

	@Cacheable("animals")
	Optional<Animal> findByLabel(String label);

}
