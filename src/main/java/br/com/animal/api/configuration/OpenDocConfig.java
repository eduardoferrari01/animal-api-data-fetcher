package br.com.animal.api.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenDocConfig {

	@Bean
	OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info().title("Animal API").version("1.0").description("Documentaçã do serviço de api"));
	}

}
