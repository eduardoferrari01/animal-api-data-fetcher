package br.com.animal.api.exception;

public class NotFoundException extends  RuntimeException{

	private static final long serialVersionUID = 8149454487384688539L;
	
	public NotFoundException(String msg) {
		
		super(msg);
	}
}
