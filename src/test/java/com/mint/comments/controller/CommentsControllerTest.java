package com.mint.comments.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mint.comments.SecurityControllerAdvice;
import com.mint.comments.models.Comment;
import com.mint.comments.repositories.CommentsRepository;
import com.mint.comments.service.CommentsServiceImpl;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@WebFluxTest
@ContextConfiguration(classes = {CommentsController.class, SecurityControllerAdvice.class})
class CommentsControllerTest {

	@Autowired
	ObjectMapper mapper;

	@Autowired
	WebTestClient webTestClient;

	@MockBean
	CommentsServiceImpl mockService;

	@MockBean
	CommentsRepository mockRepo;

	Comment comment;

	@BeforeEach
	void setUp() throws Exception {
		comment = new Comment();
		comment.setText("abcd");
	}

	@Test
	void testRead() throws Exception {
		List<Comment> comments = new ArrayList<Comment>();
		comments.add(comment);
		when(mockService.readComments()).thenReturn(Flux.fromIterable(comments));
		
		webTestClient
		.mutateWith(SecurityMockServerConfigurers.mockUser())
		.mutateWith(SecurityMockServerConfigurers.csrf())
		.get()
        .uri("/read-comments")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(Comment.class)
        .value(comment -> comment.get(0).getText().equals("abcd"));
	}

	@Test
	void testCreate() throws Exception {
		when(mockService.createComment(any(Comment.class))).thenReturn(Mono.just(comment));
		
		webTestClient
			.mutateWith(SecurityMockServerConfigurers.mockUser())
			.mutateWith(SecurityMockServerConfigurers.csrf()).post()
	        .uri("/add-comment")
	        .accept(MediaType.APPLICATION_JSON)
	        .bodyValue(comment)
	        .exchange()
	        .expectStatus().isCreated()
	        .expectBody(Comment.class)
	        .value(comment -> comment.getText().equals("abcd"));
	}
	
	@Test
	void testCreateBadRequest() throws Exception {
		webTestClient
			.mutateWith(SecurityMockServerConfigurers.mockUser())
			.mutateWith(SecurityMockServerConfigurers.csrf()).post()
	        .uri("/add-comment")
	        .accept(MediaType.APPLICATION_JSON)
	        .bodyValue(new Comment())
	        .exchange()
	        .expectStatus().isBadRequest();
	}
	
	@Test
	void testEdit() throws Exception {
		comment.setDateCreated(LocalDateTime.now());
		comment.setLastModified(LocalDateTime.now());
		comment.setId(1L);
		when(mockService.editComment(any(Comment.class))).thenReturn(Mono.just(comment));
		
		webTestClient
			.mutateWith(SecurityMockServerConfigurers.mockUser())
			.mutateWith(SecurityMockServerConfigurers.csrf()).put()
	        .uri("/edit-comment/1")
	        .accept(MediaType.APPLICATION_JSON)
	        .bodyValue(comment)
	        .exchange()
	        .expectStatus().isOk()
	        .expectBody(Comment.class)
	        .value(comment -> comment.getText().equals("abcd"));
	}
	
	@Test
	void testEditBadRequest() throws Exception {
		webTestClient
			.mutateWith(SecurityMockServerConfigurers.mockUser())
			.mutateWith(SecurityMockServerConfigurers.csrf()).put()
	        .uri("/edit-comment/1")
	        .accept(MediaType.APPLICATION_JSON)
	        .bodyValue(new Comment())
	        .exchange()
	        .expectStatus().isBadRequest();
	}
	
	@Test
	void testEditNullParameterID() throws Exception {
		comment.setDateCreated(LocalDateTime.now());
		comment.setLastModified(LocalDateTime.now());
		comment.setId(1L);
		when(mockService.editComment(any(Comment.class))).thenReturn(Mono.just(comment));
		
		webTestClient
			.mutateWith(SecurityMockServerConfigurers.mockUser())
			.mutateWith(SecurityMockServerConfigurers.csrf()).put()
	        .uri("/edit-comment/null")
	        .accept(MediaType.APPLICATION_JSON)
	        .bodyValue(comment)
	        .exchange()
	        .expectStatus().isBadRequest();
	}
	
	@Test
	void testDelete() throws Exception {
		when(mockService.deleteComment(any(Long.class))).thenReturn(Mono.empty());
		
		webTestClient
			.mutateWith(SecurityMockServerConfigurers.mockUser())
			.mutateWith(SecurityMockServerConfigurers.csrf()).delete()
	        .uri("/delete-comment/1")
	        .accept(MediaType.APPLICATION_JSON)
	        .exchange()
	        .expectStatus().isNoContent();
	}
	
	@Test
	void testDeleteNullParameter() throws Exception {
		
		webTestClient
			.mutateWith(SecurityMockServerConfigurers.mockUser())
			.mutateWith(SecurityMockServerConfigurers.csrf()).delete()
	        .uri("/delete-comment/null")
	        .accept(MediaType.APPLICATION_JSON)
	        .exchange()
	        .expectStatus().isBadRequest();
	}

}