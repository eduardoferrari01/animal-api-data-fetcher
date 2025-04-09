package br.com.animal.api.repository.adpater.mongo;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import br.com.animal.api.domain.Animal;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AnimalDocumentMapper {

	 Animal animalDocumentToAnimal(AnimalDocument animalDocument);
}
