package br.com.animal.api.repository.adpater.mongo;

import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AnimalRepositoryMongo extends MongoRepository<AnimalDocument,String> {

	@Cacheable(value = "animals_document", unless="#result == null")
	Optional<AnimalDocument> findByLabel(String label);
	
}
