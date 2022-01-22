package br.com.snake.api.util;

import java.util.Arrays;

import br.com.snake.api.builder.AnimalBuilder;
import br.com.snake.api.domain.AccidentSymptom;
import br.com.snake.api.domain.ConservationState;
import br.com.snake.api.domain.Animal;
import br.com.snake.api.domain.TypeAccident;
import br.com.snake.api.dto.AnimalTo;

public class AnimalUtil {

	public static AnimalTo createSnakeDto() {
		
		return new AnimalBuilder(createAnimalDomain()).builder();
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
		animal.setLabel("textlabel");
		animal.setSpecies("text dpecies");
		animal.setVenomous(true);
		animal.setCanCauseSeriousAccident(true);
		animal.setAntivenom("text antivenom");
		animal.setHabitat("text habitat");
		animal.setDentition("text dentition");
		animal.setUrlImage("localhost");
		return animal;
	}
}
