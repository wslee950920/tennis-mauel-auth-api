package com.tennismauel.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tennismauel.auth.config.security.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

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

	@Bean
	RestTemplate restTemplate(){
		HttpComponentsClientHttpRequestFactory factory=new HttpComponentsClientHttpRequestFactory();
		factory.setConnectTimeout(2000);
		factory.setConnectionRequestTimeout(2000);

		return new RestTemplate(factory);
	}
}
