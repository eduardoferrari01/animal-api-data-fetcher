package br.com.snake.api.dto;

import br.com.snake.api.domain.ConservationState;
 
public class SnakeTo {
 
	private String label;
	private String popularNames;
	private ConservationState conservationState;
	private String antivenom;
	private String etymology;
	private Boolean venomous;
	private Boolean canCauseSeriousAccident;
	private String species;
	private String family;
	private String genre;
	private String dentition;
	private String habitat;
	private String urlImage;
	private String characteristics;
	private String accidentSymptom;
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getPopularNames() {
		return popularNames;
	}
	public void setPopularNames(String popularNames) {
		this.popularNames = popularNames;
	}
	public ConservationState getConservationState() {
		return conservationState;
	}
	public void setConservationState(ConservationState conservationState) {
		this.conservationState = conservationState;
	}
	public String getAntivenom() {
		return antivenom;
	}
	public void setAntivenom(String antivenom) {
		this.antivenom = antivenom;
	}
	public Boolean getCanCauseSeriousAccident() {
		return canCauseSeriousAccident;
	}
	public void setCanCauseSeriousAccident(Boolean canCauseSeriousAccident) {
		this.canCauseSeriousAccident = canCauseSeriousAccident;
	}
	public String getEtymology() {
		return etymology;
	}
	public void setEtymology(String etymology) {
		this.etymology = etymology;
	}
	public Boolean getVenomous() {
		return venomous;
	}
	public void setVenomous(Boolean venomous) {
		this.venomous = venomous;
	}
	public String getSpecies() {
		return species;
	}
	public void setSpecies(String species) {
		this.species = species;
	}
	public String getFamily() {
		return family;
	}
	public void setFamily(String family) {
		this.family = family;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public String getDentition() {
		return dentition;
	}
	public void setDentition(String dentition) {
		this.dentition = dentition;
	}
	public String getHabitat() {
		return habitat;
	}
	public void setHabitat(String habitat) {
		this.habitat = habitat;
	}
	public String getCharacteristics() {
		return characteristics;
	}
	public void setCharacteristics(String characteristics) {
		this.characteristics = characteristics;
	}
	public String getAccidentSymptom() {
		return accidentSymptom;
	}
	public void setAccidentSymptom(String accidentSymptom) {
		this.accidentSymptom = accidentSymptom;
	}
	public String getUrlImage() {
		return urlImage;
	}
	public void setUrlImage(String urlImage) {
		this.urlImage = urlImage;
	}
	
	@Override
	public String toString() {
		return "SnakeTo [popularNames=" + popularNames + ", conservationState=" + conservationState + ", antivenom="
				+ antivenom + ", etymology=" + etymology + ", venomous=" + venomous + ", species=" + species
				+ ", genre=" + genre + ", urlImage=" + urlImage  + ", characteristics=" + characteristics + ", accidentSymptom="
				+ accidentSymptom + "]";
	}
}
