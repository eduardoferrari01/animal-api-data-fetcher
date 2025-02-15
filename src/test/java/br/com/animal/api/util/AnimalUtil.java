package br.com.animal.api.util;

import java.util.Arrays;

import br.com.animal.api.domain.AccidentSymptom;
import br.com.animal.api.domain.AccidentSymptom.TypeAccident;
import br.com.animal.api.domain.Animal;
import br.com.animal.api.domain.Animal.ConservationState;
import br.com.animal.api.domain.Animal.TypeOfAnimal;
import br.com.animal.api.repository.adpater.mongo.AnimalDocument;

public class AnimalUtil {

	private static final String label = "dendroaspis-polylepis";
	private static final String id = "633cf76f9bbe3278bf394fe2";

	public static String getLabel() {
		return label;
	}

	public static String getId() {
		return id;
	}
	
	public static Animal createAnimalDomain() {

		Animal animal = new Animal();
		animal.setId("X1X1X1");
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
		animal.setInoculatingStructure("text dentition");
		animal.setUrlImage("localhost");
		animal.setTypeOfAnimal(TypeOfAnimal.SNAKE);
		return animal;
	}
	
	public static AnimalDocument createAnimalDocument() {

		AnimalDocument animalDocument = new AnimalDocument();
		animalDocument.setId("X1X1X1");
		AccidentSymptom accidentSymptom = new AccidentSymptom();
		accidentSymptom.setDescription("text description");
		accidentSymptom.setTypeAccident(TypeAccident.ELIPID);
		animalDocument.setAccidentSymptom(accidentSymptom);
		animalDocument.setCharacteristics("text characteristics");
		animalDocument.setConservationState(ConservationState.LC);
		animalDocument.setEtymology("text etymology");
		animalDocument.setFamily("text family");
		animalDocument.setGenre("text genre");
		animalDocument.setPopularNames(Arrays.asList("mamba-negra"));
		animalDocument.setLabel(label);
		animalDocument.setSpecies("text dpecies");
		animalDocument.setVenomous(true);
		animalDocument.setCanCauseSeriousAccident(true);
		animalDocument.setAntivenom("text antivenom");
		animalDocument.setHabitat("text habitat");
		animalDocument.setInoculatingStructure("text dentition");
		animalDocument.setUrlImage("localhost");
		animalDocument.setTypeOfAnimal(TypeOfAnimal.SNAKE);
		return animalDocument;
	}

	
	public static Animal createAnimalDomainWithId() {

		Animal animal = createAnimalDomain();
		animal.setId(id);
		return animal;
	}

}
