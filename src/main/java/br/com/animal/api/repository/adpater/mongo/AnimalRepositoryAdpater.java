package br.com.animal.api.repository.adpater.mongo;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.animal.api.domain.Animal;
import br.com.animal.api.repository.AnimalRepository;

@Component
public class AnimalRepositoryAdpater implements AnimalRepository {

	@Autowired
	private AnimalRepositoryMongo animalRepositoryMongo;
	@Autowired
	private AnimalDocumentMapper animalDocumentMapper;

	public Optional<Animal> findByLabel(String label) {

		Optional<AnimalDocument> animal = animalRepositoryMongo.findByLabel(label);

		if (!animal.isPresent())
			return Optional.empty();

		return Optional.of(animalDocumentMapper.animalDocumentToAnimal(animal.get()));

	}

}