package br.com.animal.api.builder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.animal.api.domain.AccidentSymptom;
import br.com.animal.api.domain.Animal;
import br.com.animal.api.dto.AnimalDto;
import br.com.animal.api.dto.AnimalInfo;
import br.com.animal.api.dto.AnimalShort;

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
	
	public Animal toAnimal(AnimalDto dto) {
		
		Animal animal = new Animal();
		animal.setId(dto.id());
		animal.setLabel(dto.label());
		animal.setPopularNames(dto.popularNames());
		animal.setConservationState(dto.conservationState());
		animal.setAntivenom(dto.antivenom());
		animal.setEtymology(dto.etymology());
		animal.setVenomous(dto.venomous());
		animal.setCanCauseSeriousAccident(dto.canCauseSeriousAccident());
		animal.setSpecies(dto.species());
		animal.setFamily(dto.family());
		animal.setGenre(dto.genre());
		animal.setDentition(dto.dentition());
		animal.setHabitat(dto.habitat());
		animal.setCharacteristics(dto.characteristics());
		animal.setTypeOfAnimal(dto.typeOfAnimal());
		animal.setUrlImage(dto.urlImage());
		AccidentSymptom accidentSymptom = new AccidentSymptom();
		accidentSymptom.setDescription(dto.accidentSymptom());
		animal.setAccidentSymptom(accidentSymptom);
		
		return animal;
	}
	
	public Animal editAnimal(AnimalDto dto, Animal animal) {
		
		animal.setPopularNames(dto.popularNames());
		animal.setConservationState(dto.conservationState());
		animal.setAntivenom(dto.antivenom());
		animal.setEtymology(dto.etymology());
		animal.setVenomous(dto.venomous());
		animal.setCanCauseSeriousAccident(dto.canCauseSeriousAccident());
		animal.setSpecies(dto.species());
		animal.setFamily(dto.family());
		animal.setGenre(dto.genre());
		animal.setDentition(dto.dentition());
		animal.setTypeOfAnimal(dto.typeOfAnimal());
		animal.setHabitat(dto.habitat());
		animal.setCharacteristics(dto.characteristics());
		animal.setUrlImage(dto.urlImage());
		AccidentSymptom accidentSymptom = new AccidentSymptom();
		accidentSymptom.setId(animal.getAccidentSymptom().getId());
		accidentSymptom.setDescription(dto.accidentSymptom());
		animal.setAccidentSymptom(accidentSymptom);
		
		return animal;
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
	
	public static AnimalShort toAnimalShort(Animal animal) {
		
		return new AnimalShort(animal.getId(), animal.getLabel());
	}
}
