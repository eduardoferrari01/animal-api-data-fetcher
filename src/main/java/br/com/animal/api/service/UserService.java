package br.com.animal.api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.animal.api.domain.User;
import br.com.animal.api.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public Optional<User> findByLogin(String login) {

		return userRepository.findByLogin(login);

	}

	public User findById(String id) {

		Optional<User> user = userRepository.findById(id);

		return user.get();

	}
}
