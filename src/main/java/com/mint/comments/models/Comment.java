package com.mint.comments.models;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Comment entity.
 *
 * @author bronagh
 */
@Entity
@Data
@NoArgsConstructor
@Table(name = "COMMENT")
public class Comment {
	
	@Column("text")
	@NotBlank
	private String text;
	
    // using the groups constraint creates different validation specifications
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = ExistingComment.class)
    @Column("id")
	private Long id;
		
	@CreatedDate
	@NotNull(groups = ExistingComment.class)
    @Column("dateCreated")
	LocalDateTime dateCreated;
	
    
	@LastModifiedDate
	@NotNull(groups = ExistingComment.class)
	@Column("lastModified")
    LocalDateTime lastModified;

}
