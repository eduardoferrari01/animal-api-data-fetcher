package br.com.animal.api.integration.classify;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Profile("default")
public class ClassifyApiProd implements ClassifyApi {

	private static final Logger LOG = LoggerFactory.getLogger(ClassifyApiProd.class);
	
	@Autowired
	private ClassifyApiClient classifyApiClient;
	@Autowired
	private ObjectMapper objectMapper;

	public List<ClassificationResponse> classify(MultipartFile file) {

		LOG.info("Enviando a imagem para classificação");
		List<String> json = classifyApiClient.classify(file);

		LOG.info("Responses json: {}", json);
		
		List<ClassificationResponse> classificationResponsesStream = json.stream().map(response -> {
			try {
				return objectMapper.readValue(response, ClassificationResponse.class);
			} catch (IOException e) {

				LOG.error(e.getLocalizedMessage());
			}
			return null;
		}).filter(Objects::nonNull).toList();

		return classificationResponsesStream;
	}

}
