package br.com.animal.api.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.animal.api.controller.dto.AnimalDTOMapper;
import br.com.animal.api.controller.dto.AnimalInfo;
import br.com.animal.api.domain.Animal;
import br.com.animal.api.service.AnimalService;

@RestController
@RequestMapping("/api/animal/")
public class AnimalController implements AnimalSwagger {

	@Autowired
	private AnimalService animalService;
	private static final Logger LOG = LoggerFactory.getLogger(AnimalController.class);
	@Autowired
	private AnimalDTOMapper animalDTOMapper;

	@Override
	@PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<AnimalInfo> findInfoByImage(@RequestParam("file") final MultipartFile file)
			throws IOException {

		final Animal animal = animalService.findInfoByImage(file);

		LOG.info("Reply sent to customer");

		final AnimalInfo animalInfo = animalDTOMapper.animalToAnimalInfo(animal);
		return ResponseEntity.ok(animalInfo);
	}

	@Override
	@GetMapping("/{label}")
	public ResponseEntity<AnimalInfo> findInfoByLabel(@PathVariable final String label) {

		LOG.info("Label {}", label);
		final Animal animal = animalService.findInfoByLabel(label);

		final AnimalInfo animalInfo = animalDTOMapper.animalToAnimalInfo(animal);
		return ResponseEntity.ok(animalInfo);
	}
}