package com.mint.comments.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.mint.comments.SecurityControllerAdvice;
import com.mint.comments.models.Comment;
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
	WebTestClient webTestClient;

	@MockBean
	CommentsServiceImpl mockService;

	Comment comment;
	
	final static String EDIT_ENDPOINT = "/edit-comment/";
	final static String CREATE_ENDPOINT = "/add-comment";
	final static String DELETE_ENDPOINT = "/delete-comment/";
	
	final static String COMMENT_TEXT = "abcd";

	@BeforeEach
	void setUp() throws Exception {
		comment = new Comment();
		comment.setText(COMMENT_TEXT);
	}

	@Test
	void testRead() throws Exception {
		final List<Comment> comments = new ArrayList<Comment>();
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
        .value(comment -> comment.get(0).getText().equals(COMMENT_TEXT));
	}

	@Test
	void testCreate() throws Exception {
		when(mockService.createComment(any(Comment.class))).thenReturn(Mono.just(comment));
		
		webTestClient
			.mutateWith(SecurityMockServerConfigurers.mockUser())
			.mutateWith(SecurityMockServerConfigurers.csrf()).post()
	        .uri(CREATE_ENDPOINT)
	        .accept(MediaType.APPLICATION_JSON)
	        .bodyValue(comment)
	        .exchange()
	        .expectStatus().isCreated()
	        .expectBody(Comment.class)
	        .value(comment -> comment.getText().equals(COMMENT_TEXT));
	}
	
	@Test
	void testCreateBadRequest() throws Exception {
		webTestClient
			.mutateWith(SecurityMockServerConfigurers.mockUser())
			.mutateWith(SecurityMockServerConfigurers.csrf()).post()
	        .uri(CREATE_ENDPOINT)
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
	        .uri(EDIT_ENDPOINT + "1")
	        .accept(MediaType.APPLICATION_JSON)
	        .bodyValue(comment)
	        .exchange()
	        .expectStatus().isOk()
	        .expectBody(Comment.class)
	        .value(comment -> comment.getText().equals(COMMENT_TEXT));
	}
	
	@Test
	void testEditBadRequest() throws Exception {
		webTestClient
			.mutateWith(SecurityMockServerConfigurers.mockUser())
			.mutateWith(SecurityMockServerConfigurers.csrf()).put()
	        .uri(EDIT_ENDPOINT+"1")
	        .accept(MediaType.APPLICATION_JSON)
	        .bodyValue(new Comment())
	        .exchange()
	        .expectStatus().isBadRequest();
	}
	
	@Test
	void testEditNullParameterID() throws Exception {
		comment.setDateCreated(LocalDateTime.now());
		comment.setLastModified(LocalDateTime.now());
		comment.setId(3L);
		
		webTestClient
			.mutateWith(SecurityMockServerConfigurers.mockUser())
			.mutateWith(SecurityMockServerConfigurers.csrf()).put()
	        .uri(EDIT_ENDPOINT+"null")
	        .accept(MediaType.APPLICATION_JSON)
	        .bodyValue(comment)
	        .exchange()
	        .expectStatus().isBadRequest();
	}
	
	@Test
	void testEditDifferentParameterIDAndBodyId() throws Exception {
		comment.setDateCreated(LocalDateTime.now());
		comment.setLastModified(LocalDateTime.now());
		comment.setId(1L);
		
		webTestClient
			.mutateWith(SecurityMockServerConfigurers.mockUser())
			.mutateWith(SecurityMockServerConfigurers.csrf()).put()
	        .uri(EDIT_ENDPOINT+"2")
	        .accept(MediaType.APPLICATION_JSON)
	        .bodyValue(comment)
	        .exchange()
	        .expectStatus().is4xxClientError()
	        .expectBody()
	        .equals("Resource specified in path does not match resouce specified in body");
	}
	
	@Test
	void testDelete() throws Exception {
		when(mockService.deleteComment(any(Long.class))).thenReturn(Mono.empty());
		
		webTestClient
			.mutateWith(SecurityMockServerConfigurers.mockUser())
			.mutateWith(SecurityMockServerConfigurers.csrf()).delete()
	        .uri(DELETE_ENDPOINT+"1")
	        .accept(MediaType.APPLICATION_JSON)
	        .exchange()
	        .expectStatus().isNoContent();
	}
	
	@Test
	void testDeleteNullParameter() throws Exception {
		
		webTestClient
			.mutateWith(SecurityMockServerConfigurers.mockUser())
			.mutateWith(SecurityMockServerConfigurers.csrf()).delete()
	        .uri(DELETE_ENDPOINT+"null")
	        .accept(MediaType.APPLICATION_JSON)
	        .exchange()
	        .expectStatus().isBadRequest();
	}

}