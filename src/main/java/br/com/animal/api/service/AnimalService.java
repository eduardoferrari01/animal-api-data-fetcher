package br.com.animal.api.service;

import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.animal.api.domain.Animal;
import br.com.animal.api.exception.NotFoundException;
import br.com.animal.api.exception.RuleException;
import br.com.animal.api.integration.classify.ClassificationResponse;
import br.com.animal.api.integration.classify.ClassifyApi;
import br.com.animal.api.repository.AnimalRepository;

@Service
public class AnimalService {

	@Autowired
	private AnimalRepository animalRepository;
	@Autowired
	private ClassifyApi classifyApi;
	private static final Logger LOG = LoggerFactory.getLogger(AnimalService.class);

	public Animal findInfoByImage(final MultipartFile file) {

		if (file == null) {
			throw new RuleException("Nenhuma imagem enviado");
		}

		if (MediaType.IMAGE_JPEG_VALUE.equals(file.getContentType())
				|| MediaType.IMAGE_PNG_VALUE.equals(file.getContentType())) {

			LOG.info("Imagem {} recebida", file.getOriginalFilename());

			List<ClassificationResponse> responses = classifyApi.classify(file);

			final ClassificationResponse classificationResponse = responses.stream()
					.max(Comparator.comparingDouble(ClassificationResponse::probability))
					.filter(c -> c.probability() > 90)
					.orElseThrow(() -> new NotFoundException("Nenhum animal foi encontrado"));

			LOG.info("Animal com a maior probabilidade: {}", classificationResponse.toString());

			return findInfoByLabel(classificationResponse.label());

		}
		throw new RuleException("O tipo " + file.getContentType() + " é inválido");

	}

	public Animal findInfoByLabel(final String label) {

		LOG.info("Buscar informação do animal por label: {}", label);

		final Animal animal = animalRepository.findByLabel(label)
				.orElseThrow(() -> new NotFoundException("Nenhum animal foi encontrado"));

		LOG.info("{} found!!!", label);

		return animal;
	}

}
