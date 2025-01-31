package br.com.animal.api.builder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.animal.api.domain.Animal;
import br.com.animal.api.dto.AnimalDto;
import br.com.animal.api.dto.AnimalInfo;

public class AnimalBuilder {

	private static final Logger LOG = LoggerFactory.getLogger(AnimalBuilder.class);
	
	public AnimalInfo toAnimalInfo(Animal animal) {
		
		AnimalInfo animalInfo = new AnimalInfo(animal.getLabel(), 
				String.join(", ", animal.getPopularNames()), 
				animal.getConservationState().getLabel(), 
				animal.getAntivenom(),
				animal.getEtymology(), 
				animal.getVenomous() ? "Sim" : "Não", 
				animal.getCanCauseSeriousAccident() ? "Sim" : "Não", 
				animal.getSpecies(), 
				animal.getFamily(),
				animal.getGenre(),
				animal.getDentition(), 
				animal.getHabitat(), 
				animal.getUrlImage(),
				animal.getCharacteristics(), 
				animal.getAccidentSymptom().getDescription());
		
		LOG.info("DTO response created: {}", animalInfo);
		
		return animalInfo;
	}
	
	public AnimalDto toAnimalDto(Animal animal) {

		return new AnimalDto(animal.getId(), 
				animal.getLabel(), 
				animal.getPopularNames(), 
				animal.getConservationState(),
				animal.getAntivenom(), 
				animal.getEtymology(), 
				animal.getVenomous(), 
				animal.getCanCauseSeriousAccident(),
				animal.getSpecies(), 
				animal.getFamily(), 
				animal.getGenre(), 
				animal.getDentition(), 
				animal.getHabitat(),
				animal.getUrlImage(), 
				animal.getCharacteristics(), 
				animal.getAccidentSymptom().getDescription(),
				animal.getTypeOfAnimal());
	}
	
}
