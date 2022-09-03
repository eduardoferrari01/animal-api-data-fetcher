package br.com.animal.api.builder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.animal.api.domain.Animal;
import br.com.animal.api.dto.AnimalTo;

public class AnimalBuilder {

	private Animal animal;
	private AnimalTo animalTo;
	private static final Logger LOG = LoggerFactory.getLogger(AnimalBuilder.class);
	
	public AnimalBuilder(Animal animal) {

		this.animal = animal;
		this.animalTo = new AnimalTo();
	}

	public AnimalTo builder() {
		
		animalTo.setLabel(animal.getLabel());
		animalTo.setAccidentSymptom(animal.getAccidentSymptom().getDescription());
		animalTo.setAntivenom(animal.getAntivenom());
		animalTo.setVenomous(animal.getVenomous() ? "Sim" : "Não");
		animalTo.setCanCauseSeriousAccident(animal.getCanCauseSeriousAccident() ? "Sim" : "Não");
		animalTo.setSpecies(animal.getSpecies());
		animalTo.setPopularNames(String.join(", ", animal.getPopularNames()));
		animalTo.setFamily(animal.getFamily());
		animalTo.setGenre(animal.getGenre());
		animalTo.setDentition(animal.getDentition());
		animalTo.setHabitat(animal.getHabitat());
		animalTo.setEtymology(animal.getEtymology());
		animalTo.setConservationState(animal.getConservationState().getLabel());
		animalTo.setCharacteristics(animal.getCharacteristics());
		animalTo.setUrlImage(animal.getUrlImage());
		
		LOG.info("DTO created: {}", animalTo);
		
		return animalTo;
	}
}
