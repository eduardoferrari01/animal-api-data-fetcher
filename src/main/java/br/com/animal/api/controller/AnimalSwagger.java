package br.com.animal.api.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import br.com.animal.api.dto.AnimalDto;
import br.com.animal.api.dto.AnimalInfo;
import br.com.animal.api.dto.AnimalShort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Endpoint para animal api")
public interface AnimalSwagger {

	static final String name = "bearerAuth";

	@SecurityRequirement(name = name)
	@Operation(summary = "Criar animal", responses = {
			@ApiResponse(responseCode = "201", description = "Criado com sucesso"),
			@ApiResponse(responseCode = "400", description = "Não foi possivel criar"),
			@ApiResponse(responseCode = "403", description = "Não autorizado"),
			@ApiResponse(responseCode = "500", description = "Ocorreu um erro inesperado"),
			@ApiResponse(responseCode = "502", description = "Não foi possivel conectar com o serviço de classificação")})
	ResponseEntity<AnimalDto> create(AnimalDto dto);

	@SecurityRequirement(name = name)
	@Operation(summary = "Atualiza animal cadastrado", responses = {
			@ApiResponse(responseCode = "200", description = "Atualizado com sucesso"),
			@ApiResponse(responseCode = "400", description = "Não foi possivel atualizar"),
			@ApiResponse(responseCode = "403", description = "Não autorizado"),
			@ApiResponse(responseCode = "404", description = "Animal não encontrado"),
			@ApiResponse(responseCode = "500", description = "Ocorreu um erro inesperado") })
	ResponseEntity<AnimalDto> update(AnimalDto dto);
	
	@SecurityRequirement(name = name)
	@Operation(summary = "Lista os animais cadastro", responses = {
			@ApiResponse(responseCode = "200", description = "Listado com sucesso"),
			@ApiResponse(responseCode = "403", description = "Não autorizado"),
			@ApiResponse(responseCode = "500", description = "Ocorreu um erro inesperado") })
	ResponseEntity<Page<AnimalShort>> list(Pageable pagination);

	@Operation(summary = "Classifica o animal", responses = {
			@ApiResponse(responseCode = "200", description = "Classificou"),
			@ApiResponse(responseCode = "404", description = "Animal não encontrado"),
			@ApiResponse(responseCode = "500", description = "Ocorreu um erro inesperado"),
			@ApiResponse(responseCode = "502", description = "Não foi possivel conectar com o serviço de classificação")})
	ResponseEntity<AnimalInfo> information(MultipartFile file) throws IOException;

	@Operation(summary = "Busca a informação do animal pela label", responses = {
			@ApiResponse(responseCode = "200", description = "Encontrou a informação do animal"),
			@ApiResponse(responseCode = "404", description = "Animal não encontrado"),
			@ApiResponse(responseCode = "500", description = "Ocorreu um erro inesperado") })
	ResponseEntity<AnimalInfo> findByLabel(String label);

	@SecurityRequirement(name = name)
	@Operation(summary = "Lista labels disponíveis para cadastrar", responses = {
			@ApiResponse(responseCode = "200", description = "Listado com sucesso"),
			@ApiResponse(responseCode = "403", description = "Não autorizado"),
			@ApiResponse(responseCode = "500", description = "Ocorreu um erro inesperado"),
			@ApiResponse(responseCode = "502", description = "Não foi possivel conectar com o serviço de classificação")})
	ResponseEntity<List<String>> findLabelsAvailableToRegister();

}
