package br.com.animal.api.dto;

import java.util.List;

import br.com.animal.api.domain.Animal.TypeOfAnimal;
import br.com.animal.api.domain.ConservationState;

public record AnimalDto(String id, String label, List<String> popularNames, ConservationState conservationState,
		String antivenom, String etymology, Boolean venomous, Boolean canCauseSeriousAccident, String species,
		String family, String genre, String dentition, String habitat, String urlImage, String characteristics,
		String accidentSymptom, TypeOfAnimal typeOfAnimal) {
}
