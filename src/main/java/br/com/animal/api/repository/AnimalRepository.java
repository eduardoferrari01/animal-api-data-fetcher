package br.com.animal.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.animal.api.domain.Animal;

public interface AnimalRepository extends MongoRepository<Animal,String> {

	@Cacheable(value = "animals", unless="#result == null")
	Optional<Animal> findByLabel(String label);
	
	@Cacheable(value = "animals-short")
	Page<Animal> findAll(Pageable pagination);
	
	List<Animal> findByLabelIn(List<String> labels);
	
	Boolean existsAnimalByLabel(String label);
}
