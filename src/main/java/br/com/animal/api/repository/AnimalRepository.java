package br.com.animal.api.repository;

import java.util.Optional;

import br.com.animal.api.domain.Animal;

public interface AnimalRepository {

	Optional<Animal> findByLabel(String label);
}
