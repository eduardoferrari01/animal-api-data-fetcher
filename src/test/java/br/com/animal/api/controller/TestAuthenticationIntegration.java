package br.com.animal.api.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.animal.api.configuration.security.TokenService;
import br.com.animal.api.dto.LoginFormDTO;
import br.com.animal.api.dto.TokenDTO;
import br.com.animal.api.repository.UserRepository;
import br.com.animal.api.util.AuthenticateUserUtil;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestAuthenticationIntegration {

	@Autowired
	private MockMvc mockMvc;
	private static ObjectMapper objectMapper;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private UserRepository userRepository;

	@BeforeAll
	void setup() throws IOException {
		
		objectMapper = new ObjectMapper();
		userRepository.deleteAll();
		userRepository.save(AuthenticateUserUtil.userCreate());
	}
	
	@Test
	void mustAuthenticateUser() throws Exception {
		
		String json = objectMapper.writeValueAsString(AuthenticateUserUtil.LoginFormCreate().loginForm().build());

		MvcResult mvcResult =  mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/")
				.content(json).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn();
		
		String jsonResponse =  mvcResult.getResponse().getContentAsString();
	
		Assertions.assertNotNull(jsonResponse);
		Assertions.assertFalse(jsonResponse.isBlank());
		
		TokenDTO tokenDTO = objectMapper.readValue(jsonResponse, TokenDTO.class);
		
		Assertions.assertNotNull(tokenDTO);
		Assertions.assertTrue(tokenService.tokenIsValid(tokenDTO.toke()));
		Assertions.assertEquals(AuthenticateUserUtil.getBearer(), tokenDTO.type());
	}
	
	@Test
	void mustNotAuthenticate() throws Exception {
		
		LoginFormDTO loginDto =  AuthenticateUserUtil.LoginFormCreate().loginFormWrongPassword().build();
		
		String json = objectMapper.writeValueAsString(loginDto);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/").content(json)
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.date").isNotEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Sua conta ou senha está incorreta"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.detail").value("uri=/api/auth/"));	
	}
	
}