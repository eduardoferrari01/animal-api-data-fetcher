package br.com.animal.api.repository.adpater.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import br.com.animal.api.domain.AccidentSymptom.TypeAccident;

@Document
public class AccidentSymptomDocument {

	@Id
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
