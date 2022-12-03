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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.animal.api.dto.AnimalDto;
import br.com.animal.api.dto.AnimalInfo;
import br.com.animal.api.dto.AnimalShort;
import br.com.animal.api.integration.CnnApi;
import br.com.animal.api.service.AnimalService;

@RestController
@RequestMapping("/api/animal/")
public class AnimalController implements AnimalSwagger {
	
	@Autowired
	private AnimalService animalService;
	private static final Logger LOG = LoggerFactory.getLogger(AnimalController.class);
	@Autowired
	private CnnApi cnnApi;
	
	@PostMapping(value = "/create")
	public ResponseEntity<AnimalDto> create(@RequestBody AnimalDto dto){
		
		LOG.info("Create new animal {}", dto.label());
		AnimalDto animalDto = animalService.createNewAnimal(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(animalDto);
	}
	
	@PutMapping(value = "/update")
	public ResponseEntity<AnimalDto> update(@RequestBody AnimalDto dto){
		
		LOG.info("Update animal {}", dto.id());
		AnimalDto animalDto = animalService.update(dto);
		return ResponseEntity.ok(animalDto);
	}
	
	@GetMapping(value = "/list")
	public ResponseEntity<Page<AnimalShort>> list(
			@PageableDefault(sort = "id", direction = Direction.ASC, page = 0, size = 15) Pageable pagination) {

		LOG.info("Find all animal short");
		Page<AnimalShort> lista = animalService.findAllShort(pagination);
		return ResponseEntity.ok(lista);

	}
	
	@GetMapping(value = "/find/description/{description}")
	public ResponseEntity<Page<AnimalShort>> findAnimalByDescription(
			@PageableDefault(sort = "label", direction = Direction.ASC, page = 0, size = 15) Pageable pagination,
			@PathVariable  String description) {

		Page<AnimalShort> lista = animalService.findAnimalByDescription(pagination, description);
		return ResponseEntity.ok(lista);
	}
	
	@PostMapping(value = "/information", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<AnimalInfo> information(@RequestParam("file") MultipartFile file) throws IOException {

		    LOG.info("Image received {}", file.getOriginalFilename());
		    String label = cnnApi.classify(file).getLabel();
			AnimalInfo animalInfo = animalService.findInfoByLabel(label);
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
	public ResponseEntity<List<String>> findLabelsAvailableToRegister(){
		
		List<String> labels =  animalService.findLabelsAvailableToRegister();
		return ResponseEntity.ok(labels);
	}
	
}