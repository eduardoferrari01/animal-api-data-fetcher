package br.com.animal.api.domain;

import java.io.Serializable;
import java.util.List;

public class Animal implements Serializable {

	public enum ConservationState {

		EX("Extinta"), EW("Extinta na natureza"), CR("Criticamente em perigo"), EN("Em perigo"), VU("Vulnerável"),
		NT("Quase ameaçada"), LC("Pouco preocupante"), DD("Dados Deficientes"), NE("Não avaliada"), NI("Não informado");

		public final String label;

		private ConservationState(String label) {

			this.label = label;
		}

		public String getLabel() {
			return this.label;
		}
	}

	public enum TypeOfAnimal {
		ARACHNID, SNAKE
	}

	private static final long serialVersionUID = 3651156997181600619L;
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