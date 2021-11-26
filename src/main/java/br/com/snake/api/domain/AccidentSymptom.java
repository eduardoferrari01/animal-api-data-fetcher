package br.com.snake.api.domain;

import java.io.Serializable;
import java.util.Objects;

public class AccidentSymptom implements Serializable {

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
		AccidentSymptom other = (AccidentSymptom) obj;
		return Objects.equals(id, other.id);
	}
	
}
