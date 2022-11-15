package br.com.animal.api.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.animal.api.domain.User;
import br.com.animal.api.dto.LoginFormDTO;

public class AuthenticateUserUtil {

	private final static String login = "admin";
	private final static String password = "123";
	private final static String bearer = "Bearer";
	private final static String headerName = "authorization";
	
	public static User userCreate() {
		
		PasswordEncoder a = new BCryptPasswordEncoder();
		return new User(login, a.encode(password));
	}

	public static LoginFormDTO LoginFormDTOCreate() {

		return new LoginFormDTO(login, password);
	}

	public static String getBearer() {
		return bearer;
	}

	public static String getHeaderName() {
		return headerName;
	}
	
	public static String getHeaderValues(String toke) {
		
		return "Bearer " + toke;
	}

}
