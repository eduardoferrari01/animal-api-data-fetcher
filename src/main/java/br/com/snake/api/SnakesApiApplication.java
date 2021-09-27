package br.com.snake.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Snake API", version = "1.0", description = "Information"))
@EnableAutoConfiguration
public class SnakesApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SnakesApiApplication.class, args);
	}

}
