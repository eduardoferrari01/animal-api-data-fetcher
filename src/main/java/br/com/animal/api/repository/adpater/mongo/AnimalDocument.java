package br.com.animal.api.repository.adpater.mongo;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import br.com.animal.api.domain.AccidentSymptom;
import br.com.animal.api.domain.Animal.ConservationState;
import br.com.animal.api.domain.Animal.TypeOfAnimal;

@Document(collection = "animal")
public class AnimalDocument implements Serializable{

	private static final long serialVersionUID = 3312235917906418651L;
	@Id
	private String id;
	private String label;
	private List<String> popularNames;
	private ConservationState conservationState;
	private String antivenom;
	private String etymology;
	private Boolean venomous;
	private Boolean canCauseSeriousAccident;
	private String species;
	private String family;
	private String genre;
	private String inoculatingStructure;
	private String habitat;
	private String characteristics;
	private AccidentSymptom accidentSymptom;
	private String urlImage;
	private TypeOfAnimal typeOfAnimal;

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

	public Boolean getCanCauseSeriousAccident() {
		return canCauseSeriousAccident;
	}

	public void setCanCauseSeriousAccident(Boolean canCauseSeriousAccident) {
		this.canCauseSeriousAccident = canCauseSeriousAccident;
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

	public String getInoculatingStructure() {
		return inoculatingStructure;
	}

	public void setInoculatingStructure(String inoculatingStructure) {
		this.inoculatingStructure = inoculatingStructure;
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

	public TypeOfAnimal getTypeOfAnimal() {
		return typeOfAnimal;
	}

	public void setTypeOfAnimal(TypeOfAnimal typeOfAnimal) {
		this.typeOfAnimal = typeOfAnimal;
	}

}
