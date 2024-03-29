package br.com.animal.api.exception;

import java.time.LocalDateTime;

public class ExceptionResponse {

	private LocalDateTime date;
	private String message;
	private String detail;

	public ExceptionResponse(LocalDateTime date, String message, String detail) {
		this.date = date;
		this.message = message;
		this.detail = detail;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public String getMessage() {
		return message;
	}

	public String getDetail() {
		return detail;
	}

}
