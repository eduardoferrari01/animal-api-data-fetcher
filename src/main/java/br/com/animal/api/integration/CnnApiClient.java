package br.com.animal.api.integration;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "cnn", url = "${integration.cnn.url}")
public interface CnnApiClient {

	@GetMapping(value = "/cnn/class/names")
	List<String> getAllLabels();
	
	@PostMapping(value = "/cnn/classify", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	IntegrationResponse classify(@RequestPart(value = "file") MultipartFile file);

	@GetMapping(value = "/cnn/exists/label/{label}")
	Boolean existLabel(@PathVariable String label);
}
