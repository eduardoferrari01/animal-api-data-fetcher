package br.com.animal.api.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.animal.api.builder.AnimalBuilder;
import br.com.animal.api.domain.Animal;
import br.com.animal.api.dto.AnimalDto;
import br.com.animal.api.dto.AnimalInfo;
import br.com.animal.api.dto.AnimalShort;
import br.com.animal.api.exception.NotFoundException;
import br.com.animal.api.exception.RuleException;
import br.com.animal.api.integration.CnnApi;
import br.com.animal.api.repository.AnimalDAL;
import br.com.animal.api.repository.AnimalRepository;

@Service
public class AnimalService {

	@Autowired
	private AnimalRepository animalRepository;
	@Autowired
	private CnnApi cnnApi;
	private static final Logger LOG = LoggerFactory.getLogger(AnimalService.class);
	@Autowired
	private AnimalDAL animalDAL;
	
	@CacheEvict(value = {"animals", "animals-short"} , allEntries = true)
	public AnimalDto createNewAnimal(AnimalDto dto) {
		
		boolean cnnRecognizesLabel = cnnApi.existLabel(dto.label());
		
		if(!cnnRecognizesLabel) {
			throw new RuleException("Cnn não reconhece a label: " + dto.label());
		}
		
		boolean labelExist = animalRepository.existsAnimalByLabel(dto.label());
		
		if(labelExist) {
			throw new RuleException("Label já cadastrada");
		}
		
		AnimalBuilder animalBuilder =  new AnimalBuilder();
		
		Animal animal = animalBuilder.toAnimal(dto);
		animal.setId(null);
		
		Animal animalCreate = animalRepository.save(animal);
	
		return animalBuilder.toAnimalDto(animalCreate);
	}
	
	@CacheEvict(value = {"animals", "animals-short"} , allEntries = true)
	public AnimalDto update(AnimalDto dto) {
		
		if(dto.id() == null || dto.id().isBlank()) {
			
			throw new RuleException("Id não pode ser null ou vazio");
		}
		
		Optional<Animal> foundAnimal = animalRepository.findById(dto.id());
		
		if(foundAnimal.isPresent()) {
			
			AnimalBuilder animalBuilder =  new AnimalBuilder();
			
			Animal editedAnimal = animalBuilder.editAnimal(dto, foundAnimal.get());
			
			Animal updatedAnimal = animalRepository.save(editedAnimal);
			
			return animalBuilder.toAnimalDto(updatedAnimal);
		
		}else {
			
			throw new NotFoundException("Nenhum animal encontrado com o id: "+ dto.id());
		}
		
	}
	
	public Page<AnimalShort> findAllShort(Pageable pagination){
		
		Page<Animal> animals = animalRepository.findAll(pagination);
	
		return animals.map(AnimalBuilder::toAnimalShort);
	}
	
	public Page<AnimalShort> findAnimalByDescription(Pageable pagination, String description) {

		Page<Animal> animals = animalDAL.findAnimalByDescription(pagination, description);
		return animals.map(AnimalBuilder::toAnimalShort);
	}
	
	public AnimalInfo findInfoByImage(MultipartFile file) {

		if (MediaType.IMAGE_JPEG_VALUE.equals(file.getContentType())
				|| MediaType.IMAGE_PNG_VALUE.equals(file.getContentType())) {

			String label = cnnApi.classify(file).getLabel();
			return findInfoByLabel(label);
		}
		throw new RuleException("O tipo "+file.getContentType()+" é inválido");

	}
	
	public AnimalInfo findInfoByLabel(String label) {

		LOG.info("Find by label: {}", label);

		Animal animal = animalRepository.findByLabel(label)
				.orElseThrow(() -> new NotFoundException("No animals were found"));

		LOG.info("{} found!!!", label);

		return new AnimalBuilder().toAnimalInfo(animal);
	}
	
	public List<String> findLabelsAvailableToRegister(){
		
		LOG.info("Searching labels available for registration");
		
		List<String> labels =  cnnApi.getAllLabels();
		
		List<String> registrationLabels = animalRepository.findByLabelIn(labels).stream().map(Animal::getLabel).toList();
		
		return labels.stream().filter(e -> !registrationLabels.contains(e)).toList();
	}

}
