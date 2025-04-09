package br.com.animal.api.integration.classify;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "cnn", url = "${integration.cnn.url}")
public interface ClassifyApiClient {

	@PostMapping(value = "/router/classify", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	List<String> classify(@RequestPart(value = "file") MultipartFile file);
}
