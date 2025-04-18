package br.com.animal.api.domain;

import java.io.Serializable;

public class AccidentSymptom implements Serializable {

	public enum TypeAccident {

		BOTHROPIC, LACETIC, CROTALIC, ELIPID, ARACHNID, OTHERS, NONE
	}

	private static final long serialVersionUID = 721140409944969591L;
	private String id;
	private String description;
	private TypeAccident typeAccident;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TypeAccident getTypeAccident() {
		return typeAccident;
	}

	public void setTypeAccident(TypeAccident typeAccident) {
		this.typeAccident = typeAccident;
	}

}
