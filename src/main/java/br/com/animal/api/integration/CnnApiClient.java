package br.com.animal.api.integration;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "cnn", url = "${integration.cnn.url}")
public interface CnnApiClient {

	@GetMapping(value = "/cnn/class/names")
	List<String> getAllLabels();
}
