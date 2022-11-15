package br.com.animal.api.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.animal.api.dto.LoginFormDTO;
import br.com.animal.api.dto.TokenDTO;

@Service
public class AutenticacaoTokenService {

	@Autowired
	private TokenService tokenService;
	
	public TokenDTO authenticate(LoginFormDTO loginForm, AuthenticationManager authManager) {
		
		UsernamePasswordAuthenticationToken dadosLogin = new UsernamePasswordAuthenticationToken(loginForm.getLogin(),
				loginForm.getPassword());
		
		Authentication authentication = authManager.authenticate(dadosLogin);
		
		String token = tokenService.generateToken(authentication);
		
		return new TokenDTO(token, "Bearer");
	}

	public boolean tokenIsValid(String token) {
		
		return tokenService.tokenIsValid(token);
	}

	public String getUserId(String token) {
		return tokenService.getUserId(token);
	}
}
