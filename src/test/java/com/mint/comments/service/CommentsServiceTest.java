package com.mint.comments.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;


import com.mint.comments.models.Comment;

import com.mint.comments.repositories.CommentsRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommentsServiceTest {
	
	@InjectMocks
	CommentsService service = new CommentsServiceImpl();
	
	@Mock
	CommentsRepository repository;

	@Test
	void testCreate() {
		when(repository.save(any(Comment.class))).thenReturn(Mono.just(new Comment()));
		
		final Mono<Comment> comment = service.createComment(new Comment());
		
		assertNotNull(comment.block());
	}
	
	@Test
	void testEdit() {
		when(repository.save(any(Comment.class))).thenReturn(Mono.just(new Comment()));
		
		final Mono<Comment> comment = service.editComment(new Comment());
		
		assertNotNull(comment.block());
	}

	@Test
	void testDelete() {
		when(repository.deleteById(any(Long.class))).thenReturn(Mono.empty());
		
		final Mono<Void> result = service.deleteComment(1L);
		
		assertNull(result.block());
	}
	
	@Test
	void testRead() {
		when(repository.findAll()).thenReturn(Flux.fromIterable(new ArrayList<Comment>()));
		
		final Flux<Comment> comment = service.readComments();
		
		assertNotNull(comment.collectList().block());
	}
}
