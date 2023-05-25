package com.mint.comments.exceptions;

public class CommentException extends Exception{
	private static final long serialVersionUID = -79072390650431642L;

	public CommentException(final String errorMessage) {
        super(errorMessage);
    }
}
