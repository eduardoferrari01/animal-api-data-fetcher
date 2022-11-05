package br.com.animal.api.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.animal.api.dto.LoginFormDTO;
import br.com.animal.api.dto.TokenDTO;
import br.com.animal.api.service.AutenticacaoTokenService;

@RestController
@RequestMapping("/api/auth/")
public class AuthenticationController {

	@Autowired
	private AutenticacaoTokenService autenticacaoService;

	@PostMapping
	public ResponseEntity<?> authenticate(@RequestBody @Valid LoginFormDTO loginForm) {

		try {
			TokenDTO tokenDTO = autenticacaoService.authenticate(loginForm);
			return ResponseEntity.ok().body(tokenDTO);

		} catch (AuthenticationException e) {
			return ResponseEntity.badRequest().build();
		}

	}
}
