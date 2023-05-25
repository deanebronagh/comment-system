package com.mint.comments.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mint.comments.models.Comment;
import com.mint.comments.models.ExistingComment;
import com.mint.comments.service.CommentsService;

import jakarta.validation.constraints.NotNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class CommentsController {
	
	// TODO: bulkhead?
	
	@Autowired
	private CommentsService commentsService;
	
	/**
	 * Retrieves all comments.
	 * 
	 * @return list of {@link Comment}s.
	 */
	@GetMapping(value = "/read-comments", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Flux<Comment> readComments() {
		return commentsService.readComments();
	}
	
	/**
	 * Creates a comment.
	 * 
	 * @param comment - the comment to be saved to the database, only text field is required
	 * @return the saved comment with all fields present
	 */
	@PostMapping(value = "/add-comment", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<Comment> createComment(@Validated @RequestBody final Comment comment) {
		return commentsService.createComment(comment);
	}
	
	/**
	 * Edits a comment.
	 * 
	 * @param id - id of the comment to be edited, must be specified
	 * @param comment - the comment with updated text field, validation is performed on all fields
	 * @return the updated comment
	 */
	@PutMapping(value = "/edit-comment/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Mono<Comment> editComment(@PathVariable("id") @Validated @NotNull final Long id, @Validated(ExistingComment.class) @RequestBody final Comment comment) {
		return commentsService.editComment(comment);
	}
	
	/**
	 * Deletes a comment.
	 * 
	 * @param id - the id of the comment to be deleted.
	 */
	@DeleteMapping("/delete-comment/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Mono<Void> deleteComment(@PathVariable("id") @Validated @NotNull final Long id) {
		return commentsService.deleteComment(id);
	}

}
