package br.com.animal.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.animal.api.domain.Animal;

public interface AnimalDAL {

	Page<Animal> findAnimalByDescription(Pageable pagination, String description);
}
