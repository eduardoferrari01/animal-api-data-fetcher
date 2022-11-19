package br.com.animal.api.controller;

import org.springframework.http.ResponseEntity;

import br.com.animal.api.dto.LoginFormDTO;
import br.com.animal.api.dto.TokenDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name =  "Endpoint para autentificar usuário")
public interface AuthenticationlSwagger {

	@Operation(summary = "Autenticar usuário",
			responses = {
					@ApiResponse(responseCode = "200", description = "Autenticado com sucesso"),
					@ApiResponse(responseCode = "400", description = "Não foi possivel autenticar usuário"),
					@ApiResponse(responseCode = "500", description = "Ocorreu um erro inesperado")
			})
	public ResponseEntity<TokenDTO> authenticate(LoginFormDTO loginForm);
	 
}
