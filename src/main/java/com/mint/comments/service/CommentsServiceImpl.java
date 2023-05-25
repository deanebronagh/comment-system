package com.mint.comments.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mint.comments.models.Comment;
import com.mint.comments.repositories.CommentsRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Comments service implementation - provides comment create, read, update and delete functionality.
 * 
 * @author bronagh
 *
 */
@Service
@Slf4j
public class CommentsServiceImpl implements CommentsService {
	
	@Autowired
	public CommentsRepository commentsRepository;

	@Override
	public Flux<Comment> readComments() {
		log.info("Retrieving all comments");
		return commentsRepository.findAll();
	}

	@Override
	public Mono<Comment> createComment(final Comment comment) {
		log.info("Saving new comment: " + comment.getText());
		final Mono<Comment> savedComment = commentsRepository.save(comment);
		return savedComment;
	}

	@Override
	public Mono<Comment> editComment(final Comment comment) {
		log.info("Updating existing comment with ID, " + comment.getId() + ", with new text, " + comment.getText());
		final Mono<Comment> savedComment = commentsRepository.save(comment);
		log.info("Updated existing with ID: " + comment.getId());
		return savedComment;
	}

	@Override
	public Mono<Void> deleteComment(final Long id) {
		log.info("Deleting comment with ID: " + id);
		final Mono<Void> result = commentsRepository.deleteById(id);
		log.info("Deleted comment with ID: " + id);		
		return result;
	}
}
