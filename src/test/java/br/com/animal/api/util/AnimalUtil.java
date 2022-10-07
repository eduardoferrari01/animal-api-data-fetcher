package br.com.animal.api.util;

import java.util.Arrays;

import br.com.animal.api.builder.AnimalBuilder;
import br.com.animal.api.domain.AccidentSymptom;
import br.com.animal.api.domain.Animal;
import br.com.animal.api.domain.Animal.TypeOfAnimal;
import br.com.animal.api.domain.ConservationState;
import br.com.animal.api.domain.TypeAccident;
import br.com.animal.api.dto.AnimalDto;
import br.com.animal.api.dto.AnimalInfo;

public class AnimalUtil {

	private static final String  label = "dendroaspis-polylepis";
	private static final String id = "633cf76f9bbe3278bf394fe2";
	
	public static String getLabel() {
		return label;
	}
	
	public static String getId() {
		return id;
	}
	
	public static AnimalInfo createAnimalInfo() {
		
		return new AnimalBuilder().toAnimalInfo(createAnimalDomain());
	}
	
	public static AnimalDto createAnimalDto() {
		
		return new AnimalBuilder().toAnimalDto(createAnimalDomain());
	}
	
	public static AnimalDto createAnimalDtoWithId() {
		
		AnimalDto animal =  new AnimalBuilder().toAnimalDto(createAnimalDomain());
		animal.setId(id);
		return animal;
	}
	
	public static Animal createAnimalDomain() {
		
		Animal animal = new Animal();
		AccidentSymptom accidentSymptom = new AccidentSymptom();
		accidentSymptom.setDescription("text description");
		accidentSymptom.setTypeAccident(TypeAccident.ELIPID);
		animal.setAccidentSymptom(accidentSymptom);
		animal.setCharacteristics("text characteristics");
		animal.setConservationState(ConservationState.LC);
		animal.setEtymology("text etymology");
		animal.setFamily("text family");
		animal.setGenre("text genre");
		animal.setPopularNames(Arrays.asList("mamba-negra"));
		animal.setLabel(label);
		animal.setSpecies("text dpecies");
		animal.setVenomous(true);
		animal.setCanCauseSeriousAccident(true);
		animal.setAntivenom("text antivenom");
		animal.setHabitat("text habitat");
		animal.setDentition("text dentition");
		animal.setUrlImage("localhost");
		animal.setTypeOfAnimal(TypeOfAnimal.SNAKE);
		return animal;
	}
	
	public static Animal createAnimalDomainWithId() {
		
		Animal animal = createAnimalDomain();
		animal.setId(id);
		return animal;
	}
}
