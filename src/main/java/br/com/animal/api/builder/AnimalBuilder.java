package br.com.animal.api.builder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.animal.api.domain.AccidentSymptom;
import br.com.animal.api.domain.Animal;
import br.com.animal.api.dto.AnimalInfo;
import br.com.animal.api.dto.AnimalDto;

public class AnimalBuilder {

	private static final Logger LOG = LoggerFactory.getLogger(AnimalBuilder.class);
	
	public AnimalInfo toAnimalInfo(Animal animal) {
		
		AnimalInfo animalResponse = new AnimalInfo();
		
		animalResponse.setLabel(animal.getLabel());
		animalResponse.setAccidentSymptom(animal.getAccidentSymptom().getDescription());
		animalResponse.setAntivenom(animal.getAntivenom());
		animalResponse.setVenomous(animal.getVenomous() ? "Sim" : "Não");
		animalResponse.setCanCauseSeriousAccident(animal.getCanCauseSeriousAccident() ? "Sim" : "Não");
		animalResponse.setSpecies(animal.getSpecies());
		animalResponse.setPopularNames(String.join(", ", animal.getPopularNames()));
		animalResponse.setFamily(animal.getFamily());
		animalResponse.setGenre(animal.getGenre());
		animalResponse.setDentition(animal.getDentition());
		animalResponse.setHabitat(animal.getHabitat());
		animalResponse.setEtymology(animal.getEtymology());
		animalResponse.setConservationState(animal.getConservationState().getLabel());
		animalResponse.setCharacteristics(animal.getCharacteristics());
		animalResponse.setUrlImage(animal.getUrlImage());
		
		LOG.info("DTO response created: {}", animalResponse);
		
		return animalResponse;
	}
	
	public Animal toAnimal(AnimalDto dto) {
		
		Animal animal = new Animal();
		animal.setId(dto.getId());
		animal.setLabel(dto.getLabel());
		animal.setPopularNames(dto.getPopularNames());
		animal.setConservationState(dto.getConservationState());
		animal.setAntivenom(dto.getAntivenom());
		animal.setEtymology(dto.getEtymology());
		animal.setVenomous(dto.getVenomous());
		animal.setCanCauseSeriousAccident(dto.getCanCauseSeriousAccident());
		animal.setSpecies(dto.getSpecies());
		animal.setFamily(dto.getFamily());
		animal.setGenre(dto.getGenre());
		animal.setDentition(dto.getDentition());
		animal.setHabitat(dto.getHabitat());
		animal.setCharacteristics(dto.getCharacteristics());
		animal.setTypeOfAnimal(dto.getTypeOfAnimal());
		animal.setUrlImage(dto.getUrlImage());
		AccidentSymptom accidentSymptom = new AccidentSymptom();
		accidentSymptom.setDescription(dto.getAccidentSymptom());
		animal.setAccidentSymptom(accidentSymptom);
		
		return animal;
	}
	
	public Animal editAnimal(AnimalDto dto, Animal animal) {
		
		animal.setPopularNames(dto.getPopularNames());
		animal.setConservationState(dto.getConservationState());
		animal.setAntivenom(dto.getAntivenom());
		animal.setEtymology(dto.getEtymology());
		animal.setVenomous(dto.getVenomous());
		animal.setCanCauseSeriousAccident(dto.getCanCauseSeriousAccident());
		animal.setSpecies(dto.getSpecies());
		animal.setFamily(dto.getFamily());
		animal.setGenre(dto.getGenre());
		animal.setDentition(dto.getDentition());
		animal.setTypeOfAnimal(dto.getTypeOfAnimal());
		animal.setHabitat(dto.getHabitat());
		animal.setCharacteristics(dto.getCharacteristics());
		animal.setUrlImage(dto.getUrlImage());
		AccidentSymptom accidentSymptom = new AccidentSymptom();
		accidentSymptom.setId(animal.getAccidentSymptom().getId());
		accidentSymptom.setDescription(dto.getAccidentSymptom());
		animal.setAccidentSymptom(accidentSymptom);
		
		return animal;
	}
	
	public AnimalDto toAnimalDto(Animal animal) {
		
		AnimalDto animalDto = new AnimalDto();
		animalDto.setId(animal.getId());
		animalDto.setLabel(animal.getLabel());
		animalDto.setPopularNames(animal.getPopularNames());
		animalDto.setConservationState(animal.getConservationState());
		animalDto.setAntivenom(animal.getAntivenom());
		animalDto.setEtymology(animal.getEtymology());
		animalDto.setVenomous(animal.getVenomous());
		animalDto.setCanCauseSeriousAccident(animal.getCanCauseSeriousAccident());
		animalDto.setSpecies(animal.getSpecies());
		animalDto.setFamily(animal.getFamily());
		animalDto.setGenre(animal.getGenre());
		animalDto.setDentition(animal.getDentition());
		animalDto.setHabitat(animal.getHabitat());
		animalDto.setCharacteristics(animal.getCharacteristics());
		animalDto.setTypeOfAnimal(animal.getTypeOfAnimal());
		animalDto.setAccidentSymptom(animal.getAccidentSymptom().getDescription());
		animalDto.setUrlImage(animal.getUrlImage());
		
		return animalDto;
	}
}
