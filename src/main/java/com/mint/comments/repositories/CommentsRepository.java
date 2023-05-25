package com.mint.comments.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.mint.comments.models.Comment;

/**
 * Comments reactive CRUD repository.
 * 
 * @author bronagh
 *
 */
@Repository
public interface CommentsRepository extends ReactiveCrudRepository<Comment, Long>{

}
