package com.mint.comments;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mint.comments.exceptions.CommentException;

import org.springframework.web.bind.MethodArgumentNotValidException;

@RestControllerAdvice
public class SecurityControllerAdvice {

	@ExceptionHandler(value = { MethodArgumentNotValidException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		// This provides a detailed explanation to the user about the exception without exposing application details 
		return ex.getBindingResult().getAllErrors().get(0).toString();
	}
	
	@ExceptionHandler(value = { CommentException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String handleCommentException(CommentException ex) {
		// This provides a detailed explanation to the user about the exception without exposing application details 
		return ex.getMessage();
	}
}