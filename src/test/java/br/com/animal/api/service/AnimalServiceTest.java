package br.com.animal.api.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import br.com.animal.api.domain.Animal;
import br.com.animal.api.dto.AnimalInfo;
import br.com.animal.api.exception.NotFoundException;
import br.com.animal.api.integration.CnnApiProd;
import br.com.animal.api.integration.IntegrationResponse;
import br.com.animal.api.repository.AnimalDAL;
import br.com.animal.api.repository.AnimalRepository;
import br.com.animal.api.util.AnimalUtil;

@ExtendWith(MockitoExtension.class)
public class AnimalServiceTest {

	@InjectMocks
	private AnimalService animalService;
	@Mock
	private AnimalRepository animalRepository;
	@Mock
	private CnnApiProd cnnApi;
	private static Animal animal;
	private static String label;
	@Mock
	private AnimalDAL animalDAL;

	@BeforeAll
	public static void setup() {

		label = AnimalUtil.getLabel();
		animal = AnimalUtil.createAnimalDomain();
	}

	@Test
	void mustReturnAnimalInfo() {

		when(animalRepository.findByLabel(label)).thenReturn(Optional.of(animal));

		AnimalInfo animalInfo = animalService.findInfoByLabel(label);
		verify(animalRepository).findByLabel(label);

		Assertions.assertNotNull(animalInfo);
		Assertions.assertEquals(animal.getAccidentSymptom().getDescription(), animalInfo.accidentSymptom());
		Assertions.assertEquals(animal.getAntivenom(), animalInfo.antivenom());
		Assertions.assertEquals(animal.getCharacteristics(), animalInfo.characteristics());
		Assertions.assertEquals(animal.getConservationState().getLabel(), animalInfo.conservationState());
		Assertions.assertEquals(animal.getEtymology(), animalInfo.etymology());
		Assertions.assertEquals(animal.getGenre(), animalInfo.genre());
		Assertions.assertEquals(animal.getSpecies(), animalInfo.species());
		Assertions.assertEquals(animal.getUrlImage(), animalInfo.urlImage());
		Assertions.assertEquals(animal.getVenomous() ? "Sim" : "Não", animalInfo.venomous());
		String popularNames = String.join(", ", animal.getPopularNames());
		Assertions.assertEquals(popularNames, animalInfo.popularNames());
	}

	@Test
	void shouldThrowNotFoundException() {

		NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> {
			animalService.findInfoByLabel(label);
		});

		Assertions.assertEquals("Nenhum animal foi encontrado", notFoundException.getMessage());
	}

	@Test
	void mustReturnLabelsAvailableForRegistration() {

		when(cnnApi.getAllLabels()).thenReturn(Arrays.asList("label1", "label2", "label3", "label4"));

		Animal animal1 = new Animal();
		animal1.setLabel("label1");
		Animal animal2 = new Animal();
		animal2.setLabel("label2");

		when(animalRepository.findByLabelIn(Mockito.anyList())).thenReturn(Arrays.asList(animal1, animal2));

		List<String> labels = animalService.findLabelsAvailable();

		verify(cnnApi).getAllLabels();
		verify(animalRepository).findByLabelIn(Mockito.anyList());

		Assertions.assertNotNull(labels);
		Assertions.assertFalse(labels.isEmpty());
		Assertions.assertEquals(2, labels.size());
		Assertions.assertIterableEquals(Arrays.asList("label3", "label4"), labels);
	}

	@Test
	void whenPassingDescriptionItMustReturnAnimal() {

		PageImpl<Animal> pageImpl = new PageImpl<>(Arrays.asList(AnimalUtil.createAnimalDomainWithId()),
				PageRequest.of(0, 10, Direction.ASC, "id"), 0);

		when(animalDAL.findAnimalByDescription(Mockito.any(Pageable.class), Mockito.anyString())).thenReturn(pageImpl);

		String description = "dendroaspis";

		Page<AnimalInfo> page = animalService.findAnimalByDescription(PageRequest.of(0, 10, Direction.ASC, "id"),
				description);

		verify(animalDAL).findAnimalByDescription(Mockito.any(Pageable.class), Mockito.anyString());

		Assertions.assertNotNull(page);
		Assertions.assertFalse(page.isEmpty());
		Assertions.assertEquals(0, page.getNumber());
		Assertions.assertEquals(10, page.getSize());
		Assertions.assertEquals(1, page.getTotalElements());
		Assertions.assertEquals(1, page.getTotalPages());
		Assertions.assertTrue(page.getSort().isSorted());

		AnimalInfo animalInfo = page.getContent().get(0);
		
		Assertions.assertNotNull(animalInfo);
		Assertions.assertEquals(animal.getAccidentSymptom().getDescription(), animalInfo.accidentSymptom());
		Assertions.assertEquals(animal.getAntivenom(), animalInfo.antivenom());
		Assertions.assertEquals(animal.getCharacteristics(), animalInfo.characteristics());
		Assertions.assertEquals(animal.getConservationState().getLabel(), animalInfo.conservationState());
		Assertions.assertEquals(animal.getEtymology(), animalInfo.etymology());
		Assertions.assertEquals(animal.getGenre(), animalInfo.genre());
		Assertions.assertEquals(animal.getSpecies(), animalInfo.species());
		Assertions.assertEquals(animal.getUrlImage(), animalInfo.urlImage());
		Assertions.assertEquals(animal.getVenomous() ? "Sim" : "Não", animalInfo.venomous());
		String popularNames = String.join(", ", animal.getPopularNames());
		Assertions.assertEquals(popularNames, animalInfo.popularNames());
	}

	@Test
	void whenPassingImageMustReturnAnimal() throws IOException {

		IntegrationResponse response = new IntegrationResponse();
		response.setLabel(animal.getLabel());

		when(cnnApi.classify(Mockito.any())).thenReturn(response);
		when(animalRepository.findByLabel(label)).thenReturn(Optional.of(animal));

		MultipartFile multipartFile = new MockMultipartFile("file", "filename.png", "image/jpeg", "img".getBytes());

		AnimalInfo animalInfo = animalService.findInfoByImage(multipartFile);
		verify(cnnApi).classify(Mockito.any());
		verify(animalRepository).findByLabel(label);

		Assertions.assertNotNull(animalInfo);
		Assertions.assertEquals(animal.getAccidentSymptom().getDescription(), animalInfo.accidentSymptom());
		Assertions.assertEquals(animal.getAntivenom(), animalInfo.antivenom());
		Assertions.assertEquals(animal.getCharacteristics(), animalInfo.characteristics());
		Assertions.assertEquals(animal.getConservationState().getLabel(), animalInfo.conservationState());
		Assertions.assertEquals(animal.getEtymology(), animalInfo.etymology());
		Assertions.assertEquals(animal.getGenre(), animalInfo.genre());
		Assertions.assertEquals(animal.getSpecies(), animalInfo.species());
		Assertions.assertEquals(animal.getUrlImage(), animalInfo.urlImage());
		Assertions.assertEquals(animal.getVenomous() ? "Sim" : "Não", animalInfo.venomous());
		String popularNames = String.join(", ", animal.getPopularNames());
		Assertions.assertEquals(popularNames, animalInfo.popularNames());
	}

}
