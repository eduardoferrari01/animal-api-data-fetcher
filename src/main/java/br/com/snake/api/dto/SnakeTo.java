package br.com.snake.api.dto;

import java.util.List;

import br.com.snake.api.domain.ConservationState;
 
public class SnakeTo {
 
	private List<String> popularNames;
	private ConservationState conservationState;
	private String antivenom;
	private String etymology;
	private Boolean venomous;
	private String species;
	private String genre;
	private String urlImage;
	private String characteristics;
	private String accidentSymptom;
	
	public List<String> getPopularNames() {
		return popularNames;
	}
	public void setPopularNames(List<String> popularNames) {
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
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
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
