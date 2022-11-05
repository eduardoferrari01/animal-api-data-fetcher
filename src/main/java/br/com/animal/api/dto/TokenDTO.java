package br.com.animal.api.dto;

public class TokenDTO {

	private String toke;
	private String type;

	public TokenDTO(String toke, String type) {
		this.toke = toke;
		this.type = type;
	}

	public String getToke() {
		return toke;
	}

	public String getType() {
		return type;
	}

}
