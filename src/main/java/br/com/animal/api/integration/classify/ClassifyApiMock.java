package br.com.animal.api.integration.classify;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@Profile("test")
public class ClassifyApiMock implements ClassifyApi {

	public ClassifyApiMock() {

	}

	@Override
	public List<ClassificationResponse> classify(MultipartFile file) {

		if ("unknown.png".equals(file.getOriginalFilename())) {

			return List.of(new ClassificationResponse("unknown", 0.0));
		}

		return List.of(new ClassificationResponse("dendroaspis-polylepis", 99.99));
	}

}
