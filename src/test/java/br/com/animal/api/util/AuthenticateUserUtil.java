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

	public static String getBearer() {
		return bearer;
	}

	public static String getHeaderName() {
		return headerName;
	}
	
	public static String getHeaderValues(String toke) {
		
		return "Bearer " + toke;
	}
	
	public static BuilderLoginForm LoginFormCreate() {

		return new BuilderLoginForm();
	}
	
	public static class BuilderLoginForm {

		private static String valueLogin;
		private static String Valuepassword;

		public BuilderLoginForm loginForm() {
			valueLogin = login;
			Valuepassword = password;
			return this;
		}

		public BuilderLoginForm loginFormWrongPassword() {
			valueLogin = login;
			Valuepassword = "321";
			return this;
		}

		public LoginFormDTO build() {

			return new LoginFormDTO(valueLogin, Valuepassword);
		}
	}
}
