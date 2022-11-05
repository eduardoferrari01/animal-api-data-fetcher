package br.com.animal.api.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import br.com.animal.api.domain.User;

@Component
public class MockDB {

	public Optional<User> getUsuario(String login) {

		Map<String, User> map = new HashMap<>();
		map.put("admin", createUser());

		return Optional.ofNullable(map.get(login));

	}

	public User getUsuarioById(Long userId) {
	
		Map<Long, User> map = new HashMap<>();
		map.put(1L, createUser());

		return map.get(userId);
	}
	
	public User createUser() {
		
		User user = new User();
		user.setId(1L);
		user.setLogin("admin");
		user.setPassword("$2a$12$8R8UkiWKdHlKH7kFt5UqAOVgkwRivb7QWeSFomOJDSiYzan15wnRu");
		
		return user;
	}
}
