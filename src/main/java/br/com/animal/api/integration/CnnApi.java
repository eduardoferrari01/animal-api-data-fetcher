package br.com.animal.api.integration;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface CnnApi {

	List<String> getAllLabels();

	IntegrationResponse classify(MultipartFile file);

	Boolean existLabel(String label);
}
