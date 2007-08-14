package org.openxava.test.model;

import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;

/**
 * For testing the default schema behaviour. <p>
 *  
 * @author Javier Paniza
 */

@Entity
public class Issue {
	
	@Id @Column(length=5) @Required
	private String id;

	@Column(length=40) @Required
	private String description;
	
	@OneToMany(mappedBy="issue", cascade=CascadeType.REMOVE)
	private Collection<Comment> comments;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Collection<Comment> getComments() {
		return comments;
	}

	public void setComments(Collection<Comment> comments) {
		this.comments = comments;
	}

}
