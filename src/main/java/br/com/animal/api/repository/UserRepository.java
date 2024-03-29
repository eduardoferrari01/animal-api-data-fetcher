package br.com.animal.api.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.animal.api.domain.User;

public interface UserRepository extends MongoRepository<User, String>{

	Optional<User> findByLogin(String login);
}