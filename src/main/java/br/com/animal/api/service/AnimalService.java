package br.com.animal.api.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.animal.api.builder.AnimalBuilder;
import br.com.animal.api.controller.NotFoundException;
import br.com.animal.api.domain.Animal;
import br.com.animal.api.dto.AnimalTo;
import br.com.animal.api.integration.CnnApi;
import br.com.animal.api.repository.AnimalRepository;

@Service
public class AnimalService {

	@Autowired
	private AnimalRepository animalRepository;
	@Autowired
	private CnnApi cnnApi;
	private static final Logger LOG = LoggerFactory.getLogger(AnimalService.class);

	public AnimalTo findByLabel(String label) {

		LOG.info("Find by label: {}", label);

		Animal animal = animalRepository.findByLabel(label)
				.orElseThrow(() -> new NotFoundException("No animals were found"));

		LOG.info("{} found!!!", label);

		return new AnimalBuilder(animal).builder();
	}
	
	public List<String> findLabelsAvailableToRegister(){
		
		LOG.info("Searching labels available for registration");
		
		List<String> labels =  cnnApi.getAllLabels();
		
		List<String> registrationLabels = animalRepository.findByLabelIn(labels).stream().map(Animal::getLabel).toList();
		
		return labels.stream().filter(e -> !registrationLabels.contains(e)).toList();
	}
 }
