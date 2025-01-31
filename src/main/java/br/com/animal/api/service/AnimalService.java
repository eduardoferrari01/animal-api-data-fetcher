package br.com.animal.api.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.animal.api.builder.AnimalBuilder;
import br.com.animal.api.domain.Animal;
import br.com.animal.api.dto.AnimalInfo;
import br.com.animal.api.exception.NotFoundException;
import br.com.animal.api.exception.RuleException;
import br.com.animal.api.integration.CnnApi;
import br.com.animal.api.repository.AnimalDAL;
import br.com.animal.api.repository.AnimalRepository;
import br.com.animal.api.service.event.EventLogAfterReturning;

@Service
public class AnimalService {

	@Autowired
	private AnimalRepository animalRepository;
	@Autowired
	private CnnApi cnnApi;
	private static final Logger LOG = LoggerFactory.getLogger(AnimalService.class);
	@Autowired
	private AnimalDAL animalDAL;
	
	@EventLogAfterReturning(value = "Buscar animais por descrição")
	public Page<AnimalInfo> findAnimalByDescription(Pageable pagination, String description) {

		Page<Animal> animals = animalDAL.findAnimalByDescription(pagination, description);
		 
		AnimalBuilder builder =  new AnimalBuilder();
		
		return animals.map(builder::toAnimalInfo);
	}
	
	@EventLogAfterReturning(value = "Buscar informações por imagem")
	public AnimalInfo findInfoByImage(MultipartFile file) {

		if (MediaType.IMAGE_JPEG_VALUE.equals(file.getContentType())
				|| MediaType.IMAGE_PNG_VALUE.equals(file.getContentType())) {

			String label = cnnApi.classify(file).getLabel();
			return findInfoByLabel(label);
		}
		throw new RuleException("O tipo "+file.getContentType()+" é inválido");

	}
	
	@EventLogAfterReturning(value = "Buscar informação do animal por label")
	public AnimalInfo findInfoByLabel(String label) {

		LOG.info("Find by label: {}", label);
		
		Animal animal = animalRepository.findByLabel(label)
				.orElseThrow(() -> new NotFoundException("Nenhum animal foi encontrado"));

		LOG.info("{} found!!!", label);

		return new AnimalBuilder().toAnimalInfo(animal);
	}
	
	@EventLogAfterReturning(value = "Buscar labels disponíveis")
	public List<String> findLabelsAvailable(){
		
		LOG.info("Searching labels available");
		
		List<String> labels =  cnnApi.getAllLabels();
		
		List<String> registrationLabels = animalRepository.findByLabelIn(labels).stream().map(Animal::getLabel).toList();
		
		return labels.stream().filter(e -> !registrationLabels.contains(e)).toList();
	}

}
