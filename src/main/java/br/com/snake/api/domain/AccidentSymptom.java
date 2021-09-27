package br.com.snake.api.domain;

public class AccidentSymptom {

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
