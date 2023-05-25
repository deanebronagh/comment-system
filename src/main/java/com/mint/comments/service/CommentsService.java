package com.mint.comments.service;


import com.mint.comments.models.Comment;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Comments service interface - provides comment create, read, update and delete functionality.
 * See implementation {@link CommentsServiceImpl}
 * 
 * @author brona
 */
public interface CommentsService {
	
	public Flux<Comment> readComments();
	
	public Mono<Comment> createComment(Comment comment);
	
	public Mono<Comment> editComment(Comment comment);
	
	public Mono<Void> deleteComment(Long id);

}
