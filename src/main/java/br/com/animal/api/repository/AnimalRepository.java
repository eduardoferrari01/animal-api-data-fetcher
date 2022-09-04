package br.com.animal.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.animal.api.domain.Animal;

public interface AnimalRepository extends MongoRepository<Animal,String> {

	@Cacheable(value = "animals", unless="#result == null")
	Optional<Animal> findByLabel(String label);
	
	List<Animal> findByLabelIn(List<String> labels);

}
