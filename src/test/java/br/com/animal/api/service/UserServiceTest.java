package br.com.animal.api.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.animal.api.domain.User;
import br.com.animal.api.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@InjectMocks
	private UserService userService;
	@Mock
	private UserRepository userRepository;
	private final String login = "admin";
	private final String password = "XXXX";
	private final String id = "XXX999";
	private User userMock() {
		
		User user = new User(login, password);
		user.setId(id);
		return user;
	}
	
	@Test
	void mustReturnUserWhenLoginIsPassed() {
	
		when(userRepository.findByLogin("admin")).thenReturn(Optional.of(userMock()));
		
		Optional<User> user =  userService.findByLogin(login);
		verify(userRepository).findByLogin(login);
		
		Assertions.assertTrue(user.isPresent());
		Assertions.assertNotNull(user.get().getId());
		Assertions.assertFalse(user.get().getId().isBlank());
		Assertions.assertEquals(login, user.get().getLogin());
		Assertions.assertEquals(password, user.get().getPassword());
	}
	
	@Test
	void mustReturnUserWhenIdIsPassed() {
		
		when(userRepository.findById(id)).thenReturn(Optional.of(userMock()));
		
		User user =  userService.findById(id);
		verify(userRepository).findById(id);
		
		Assertions.assertNotNull(user);
		Assertions.assertNotNull(user.getId());
		Assertions.assertFalse(user.getId().isBlank());
		Assertions.assertEquals(login, user.getLogin());
		Assertions.assertEquals(password, user.getPassword());
	}
}
