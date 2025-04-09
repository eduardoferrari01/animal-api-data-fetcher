package br.com.animal.api.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import br.com.animal.api.domain.Animal;
import br.com.animal.api.exception.NotFoundException;
import br.com.animal.api.integration.classify.ClassificationResponse;
import br.com.animal.api.integration.classify.ClassifyApiProd;
import br.com.animal.api.repository.AnimalRepository;
import br.com.animal.api.util.AnimalUtil;

@ExtendWith(MockitoExtension.class)
public class AnimalServiceTest {

	@InjectMocks
	private AnimalService animalService;
	@Mock
	private AnimalRepository animalRepository;
	@Mock
	private ClassifyApiProd cnnApi;
	private static Animal animal;
	private static String label;

	@BeforeAll
	public static void setup() {

		label = AnimalUtil.getLabel();
		animal = AnimalUtil.createAnimalDomain();
	}

	@Test
	void mustReturnAnimalInfo() {

		when(animalRepository.findByLabel(label)).thenReturn(Optional.of(AnimalUtil.createAnimalDomain()));

		Animal animalFind = animalService.findInfoByLabel(label);
		verify(animalRepository).findByLabel(label);

		animalAssert(animalFind);

	}

	@Test
	void whenPassingImageMustReturnAnimal() throws IOException {

		ClassificationResponse response = new ClassificationResponse(animal.getLabel(), 99.00);

		when(cnnApi.classify(Mockito.any())).thenReturn(List.of(response));
		when(animalRepository.findByLabel(label)).thenReturn(Optional.of(AnimalUtil.createAnimalDomain()));

		MultipartFile multipartFile = new MockMultipartFile("file", "filename.png", "image/jpeg", "img".getBytes());

		Animal animalFind = animalService.findInfoByImage(multipartFile);
		verify(cnnApi).classify(Mockito.any());
		verify(animalRepository).findByLabel(label);

		animalAssert(animalFind);

	}
	
	@Test
	void shouldThrowNotFoundException() {

		NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> {
			animalService.findInfoByLabel(label);
		});

		Assertions.assertEquals("Nenhum animal foi encontrado", notFoundException.getMessage());
	}

	private void animalAssert(Animal animalFind) {
	
		Animal animalExpected = AnimalUtil.createAnimalDomain();
		
		Assertions.assertNotNull(animalFind);
		Assertions.assertEquals(animalExpected.getId(), animalFind.getId());
		Assertions.assertEquals(animalExpected.getAccidentSymptom().getDescription(), animalFind.getAccidentSymptom().getDescription());
		Assertions.assertEquals(animalExpected.getAccidentSymptom().getTypeAccident(), animalFind.getAccidentSymptom().getTypeAccident());
		Assertions.assertEquals(animalExpected.getAntivenom(), animalFind.getAntivenom());
		Assertions.assertEquals(animalExpected.getCharacteristics(), animalFind.getCharacteristics());
		Assertions.assertEquals(animalExpected.getConservationState().getLabel(), animalFind.getConservationState().getLabel());
		Assertions.assertEquals(animalExpected.getEtymology(), animalFind.getEtymology());
		Assertions.assertEquals(animalExpected.getGenre(), animalFind.getGenre());
		Assertions.assertEquals(animalExpected.getSpecies(), animalFind.getSpecies());
		Assertions.assertEquals(animalExpected.getUrlImage(), animalFind.getUrlImage());
		Assertions.assertEquals(animalExpected.getVenomous(), animalFind.getVenomous());
		Assertions.assertEquals(animalExpected.getTypeOfAnimal(), animalFind.getTypeOfAnimal());
		Assertions.assertLinesMatch(animalExpected.getPopularNames(), animalFind.getPopularNames());
	}

}
