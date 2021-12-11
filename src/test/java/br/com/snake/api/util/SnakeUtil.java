package br.com.snake.api.util;

import java.util.Arrays;

import br.com.snake.api.builder.SnakeBuilder;
import br.com.snake.api.domain.AccidentSymptom;
import br.com.snake.api.domain.ConservationState;
import br.com.snake.api.domain.Snake;
import br.com.snake.api.domain.TypeAccident;
import br.com.snake.api.dto.SnakeTo;

public class SnakeUtil {

	public static SnakeTo createSnakeDto() {
		
		return new SnakeBuilder(createSnakeDomain()).builder();
	}
	
	public static Snake createSnakeDomain() {
		
		Snake snake = new Snake();
		AccidentSymptom accidentSymptom = new AccidentSymptom();
		accidentSymptom.setDescription("text description");
		accidentSymptom.setTypeAccident(TypeAccident.ELIPID);
		snake.setAccidentSymptom(accidentSymptom);
		snake.setCharacteristics("text characteristics");
		snake.setConservationState(ConservationState.LC);
		snake.setEtymology("text etymology");
		snake.setFamily("text family");
		snake.setGenre("text genre");
		snake.setPopularNames(Arrays.asList("mamba-negra"));
		snake.setLabel("textlabel");
		snake.setSpecies("text dpecies");
		snake.setVenomous(true);
		snake.setCanCauseSeriousAccident(true);
		snake.setAntivenom("text antivenom");
		snake.setHabitat("text habitat");
		snake.setDentition("text dentition");
		snake.setUrlImage("localhost");
		return snake;
	}
}
