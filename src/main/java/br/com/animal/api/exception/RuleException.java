package br.com.animal.api.exception;

public class RuleException extends RuntimeException {

	private static final long serialVersionUID = 3287766040532132551L;
	
	public RuleException(String msg) {
		super(msg);
	}
}