package br.com.animal.api.controller;

import java.io.IOException;
import java.util.List;

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

import br.com.animal.api.dto.AnimalTo;
import br.com.animal.api.integration.CnnApi;
import br.com.animal.api.service.AnimalService;

@RestController
@RequestMapping("/api/animal/")
public class AnimalController {

	@Autowired
	private AnimalService animalService;
	private static final Logger LOG = LoggerFactory.getLogger(AnimalController.class);
	@Autowired
	private CnnApi cnnApi;
	
	@PostMapping(value = "/information", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<AnimalTo> information(@RequestParam("file") MultipartFile file) throws IOException {

		    LOG.info("Image received {}", file.getOriginalFilename());
		    String label = cnnApi.classify(file).getLabel();
			AnimalTo animalTo = animalService.findByLabel(label);
			LOG.info("Reply sent to customer");
			return ResponseEntity.ok(animalTo);
	}

	@GetMapping(value = "/information/find/{label}")
	public ResponseEntity<AnimalTo> findByLabel(@PathVariable String label) {

		LOG.info("Label {}", label);
		AnimalTo animalTo = animalService.findByLabel(label);
		return ResponseEntity.ok(animalTo);
	}
	
	@GetMapping(value = "/find/labels/available")
	public ResponseEntity<List<String>> findLabelsAvailableToRegister(){
		
		List<String> labels =  animalService.findLabelsAvailableToRegister();
		return ResponseEntity.ok(labels);
	}

}
