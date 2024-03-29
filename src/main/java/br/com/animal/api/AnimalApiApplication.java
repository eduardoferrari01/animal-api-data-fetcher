package br.com.animal.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableCaching
@EnableFeignClients
@EnableAutoConfiguration
@EnableSpringDataWebSupport
public class AnimalApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnimalApiApplication.class, args);
	}

}
