package br.com.animal.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import br.com.animal.api.dto.LoginFormDTO;
import br.com.animal.api.dto.TokenDTO;

@Service
public class AutenticacaoTokenService {

	@Autowired
	private TokenService tokenService;
	
	public TokenDTO authenticate(LoginFormDTO loginForm) {
		
		UsernamePasswordAuthenticationToken dadosLogin = new UsernamePasswordAuthenticationToken(loginForm.getLogin(),
				loginForm.getPassword());
		
		String token = tokenService.generateToken(dadosLogin);
		
		return new TokenDTO(token, "Bearer");
	}

	public boolean tokenIsValid(String token) {
		
		return tokenService.tokenIsValid(token);
	}

	public Long getUserId(String token) {
		return tokenService.getUserId(token);
	}
}
