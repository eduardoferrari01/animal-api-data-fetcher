package br.com.animal.api.dto;

public record AnimalInfo(String label, String popularNames, String conservationState, String antivenom,
		String etymology, String venomous, String canCauseSeriousAccident, String species, String family, String genre,
		String dentition, String habitat, String urlImage, String characteristics, String accidentSymptom) {}