package br.com.snake.api.domain;

import java.util.List;
import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Snake {

	@Id
	private String id;
	private String label;
	private List<String> popularNames;
	private ConservationState conservationState;
	private String antivenom;
	private String etymology;
	private Boolean venomous;
	private String species;
	private String genre;
	private String characteristics;
	private AccidentSymptom accidentSymptom;
	private String urlImage;
	 
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
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
	public AccidentSymptom getAccidentSymptom() {
		return accidentSymptom;
	}
	public void setAccidentSymptom(AccidentSymptom accidentSymptom) {
		this.accidentSymptom = accidentSymptom;
	}
	public String getUrlImage() {
		return urlImage;
	}
	public void setUrlImage(String urlImage) {
		this.urlImage = urlImage;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Snake other = (Snake) obj;
		return Objects.equals(id, other.id);
	}
	
	
}