package com.tennismauel.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tennismauel.auth.config.security.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties(AppProperties.class)
@SpringBootApplication
public class AuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);
	}

	@Bean
	ObjectMapper objectMapper(){
		return new ObjectMapper();
	}
}
