package br.com.animal.api.integration.classify;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface ClassifyApi {

	List<ClassificationResponse> classify(MultipartFile file);
}
