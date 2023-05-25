package com.mint.comments.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// enabling csrf for aplplication security
		// exposing comment endpoints
		// exposing actuator endpoints
		http.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
				.authorizeHttpRequests((requests) -> requests.requestMatchers("/", "/add-comment").permitAll()
						.requestMatchers("/", "/read-comments").permitAll().requestMatchers("/", "/edit-comment/**")
						.permitAll().requestMatchers("/", "/delete-comment/**").permitAll()
						.requestMatchers("/", "/actuator/**").permitAll().anyRequest().authenticated());

		return http.build();
	}
}
