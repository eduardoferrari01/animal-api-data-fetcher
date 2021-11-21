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
		accidentSymptom.setDescription(
				"Não causam inchaço, nem necrose local, uma sensação de formigamento na área da picada");
		accidentSymptom.setTypeAccident(TypeAccident.ELIPID);
		snake.setAccidentSymptom(accidentSymptom);
		snake.setCharacteristics("cobra extremamente venenosa pertencente à família Elapidae");
		snake.setConservationState(ConservationState.LC);
		snake.setEtymology("O nome mamba deriva do zulu, imamba");
		snake.setGenre("Dendroaspis");
		snake.setPopularNames(Arrays.asList("mamba-negra"));
		snake.setLabel("ZZZ");
		snake.setSpecies("Dendroaspis polylepis");
		snake.setVenomous(true);
		snake.setAntivenom("Antiveneno polivalente");
		snake.setUrlImage("localhost");
		return snake;
	}
}
