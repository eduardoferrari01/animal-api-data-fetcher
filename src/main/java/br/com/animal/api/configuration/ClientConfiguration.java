package br.com.animal.api.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.animal.api.exception.CustomErrorDecoder;
import feign.codec.ErrorDecoder;

@Configuration
public class ClientConfiguration {

	@Bean
	public ErrorDecoder errorDecoder() {
		return new CustomErrorDecoder();
	}
}