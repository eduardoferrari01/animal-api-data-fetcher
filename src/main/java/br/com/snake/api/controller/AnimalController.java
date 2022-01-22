package br.com.snake.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import br.com.snake.api.dto.AnimalTo;
import br.com.snake.api.integration.IntegrationResponseException;
import br.com.snake.api.integration.SendImage;
import br.com.snake.api.service.AnimalService;

@RestController
@RequestMapping("/api/snake/")
public class AnimalController {

	@Autowired
	private AnimalService animalService;
	private static final Logger LOG = LoggerFactory.getLogger(AnimalController.class);
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private Environment env;

	@PostMapping(value = "/information", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<AnimalTo> information(@RequestParam("file") MultipartFile file) {

		try {

			LOG.info("Image received {}", file.getOriginalFilename());
			SendImage sendImage = new SendImage(restTemplate, env);
			String label = sendImage.post(file.getBytes()).getLabel();
			AnimalTo animalTo = animalService.findByLabel(label);
			LOG.info("Reply sent to customer");
			return ResponseEntity.ok(animalTo);

		} catch (NotFoundException e) {

			LOG.warn(e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		} catch (IntegrationResponseException e) {

			return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);

		} catch (Exception e) {

			LOG.warn(e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value = "/information/find/{label}")
	public ResponseEntity<AnimalTo> findByLabel(@PathVariable String label) {

		try {

			LOG.info("Label {}", label);
			AnimalTo animalTo = animalService.findByLabel(label);
			return ResponseEntity.ok(animalTo);

		} catch (NotFoundException e) {
			LOG.warn(e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

}
