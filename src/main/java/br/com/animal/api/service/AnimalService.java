package br.com.animal.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.animal.api.builder.AnimalBuilder;
import br.com.animal.api.controller.NotFoundException;
import br.com.animal.api.domain.Animal;
import br.com.animal.api.dto.AnimalTo;
import br.com.animal.api.repository.AnimalRepository;

@Service
public class AnimalService {

	@Autowired
	private AnimalRepository animalRepository;
	private static final Logger LOG = LoggerFactory.getLogger(AnimalService.class);

	public AnimalTo findByLabel(String label) {

		LOG.info("Find by label: {}", label);

		Animal animal = animalRepository.findByLabel(label)
				.orElseThrow(() -> new NotFoundException("No animals were found"));

		LOG.info("{} found!!!", label);

		return new AnimalBuilder(animal).builder();
				
	}
}
