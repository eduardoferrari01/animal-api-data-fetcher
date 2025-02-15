package br.com.animal.api.controller.dto;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import br.com.animal.api.domain.Animal;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AnimalDTOMapper {
	
	@Mapping(source = "accidentSymptom.description", target = "accidentSymptom")
	@Mapping(source = "popularNames", target = "popularNames")
	@Mapping(source = "venomous", target = "venomous")
	@Mapping(source = "canCauseSeriousAccident", target = "canCauseSeriousAccident")
	@Mapping(source = "conservationState.label", target = "conservationState")
	AnimalInfo animalToAnimalInfo(Animal animal);

	//venomous e canCauseSeriousAccident
	default String mapBooleanToYesOrNotStr(Boolean venomous) {
        return venomous ? "Sim" : "Não";
    }
	
	default String mapPopularNamesToString(List<String> popularNames) {
		return String.join(", ", popularNames);
	}
}
