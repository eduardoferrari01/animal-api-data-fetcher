package br.com.animal.api.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.animal.api.configuration.security.AutenticacaoTokenService;
import br.com.animal.api.dto.LoginFormDTO;
import br.com.animal.api.dto.TokenDTO;

@RestController
@RequestMapping("/api/auth/")
public class AuthenticationController implements AuthenticationlSwagger {

	@Autowired
	private AutenticacaoTokenService autenticacaoService;
	@Autowired
	private AuthenticationManager authManager;
	
	@PostMapping
	public ResponseEntity<TokenDTO> authenticate(@RequestBody @Valid LoginFormDTO loginForm) {

			TokenDTO tokenDTO = autenticacaoService.authenticate(loginForm, authManager);
			return ResponseEntity.ok().body(tokenDTO);
	}
}
