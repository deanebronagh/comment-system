package com.mint.comments;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@EnableR2dbcRepositories
@SpringBootApplication
@EnableR2dbcAuditing
public class CommentsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommentsApplication.class, args);
	}

}
