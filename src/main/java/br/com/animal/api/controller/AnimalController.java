package br.com.animal.api.controller;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.animal.api.dto.AnimalInfo;
import br.com.animal.api.service.AnimalService;

@RestController
@RequestMapping("/api/animal/")
public class AnimalController implements AnimalSwagger {
	
	@Autowired
	private AnimalService animalService;
	private static final Logger LOG = LoggerFactory.getLogger(AnimalController.class);
	
	@GetMapping(value = "/find/description/{description}")
	public ResponseEntity<Page<AnimalInfo>> findAnimalByDescription(
			@PageableDefault(sort = "label", direction = Direction.ASC, page = 0, size = 15) Pageable pagination,
			@PathVariable  String description) {

		Page<AnimalInfo> lista = animalService.findAnimalByDescription(pagination, description);
		return ResponseEntity.ok(lista);
	}
	
	@PostMapping(value = "/information", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<AnimalInfo> information(@RequestParam("file") MultipartFile file) throws IOException {

		    LOG.info("Image received {}", file.getOriginalFilename());
		    AnimalInfo animalInfo = animalService.findInfoByImage(file);
			LOG.info("Reply sent to customer");
			return ResponseEntity.ok(animalInfo);
	}

	@GetMapping(value = "/information/find/{label}")
	public ResponseEntity<AnimalInfo> findByLabel(@PathVariable String label) {

		LOG.info("Label {}", label);
		AnimalInfo animalInfo = animalService.findInfoByLabel(label);
		return ResponseEntity.ok(animalInfo);
	}
	
	@GetMapping(value = "/find/labels/available")
	public ResponseEntity<List<String>> findLabelsAvailable(){
		
		List<String> labels =  animalService.findLabelsAvailable();
		return ResponseEntity.ok(labels);
	}
	
}