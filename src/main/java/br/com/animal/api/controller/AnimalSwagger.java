package br.com.animal.api.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import br.com.animal.api.controller.dto.AnimalInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Endpoint para animal api")
public interface AnimalSwagger {

	@Operation(summary = "Busca a informação do animal pela imagem", responses = {
			@ApiResponse(responseCode = "200", description = "Encontrou a informação do animal"),
			@ApiResponse(responseCode = "404", description = "Animal não encontrado"),
			@ApiResponse(responseCode = "500", description = "Ocorreu um erro inesperado"),
			@ApiResponse(responseCode = "502", description = "Não foi possivel conectar com o serviço de classificação") })
	ResponseEntity<AnimalInfo> findInfoByImage(MultipartFile file) throws IOException;

	@Operation(summary = "Busca a informação do animal pela label", responses = {
			@ApiResponse(responseCode = "200", description = "Encontrou a informação do animal"),
			@ApiResponse(responseCode = "404", description = "Animal não encontrado"),
			@ApiResponse(responseCode = "500", description = "Ocorreu um erro inesperado") })
	ResponseEntity<AnimalInfo> findInfoByLabel(String label);

}