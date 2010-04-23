package org.openxava.test.model;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.openxava.annotations.Editor;
import org.openxava.annotations.Required;
import org.openxava.annotations.Tab;
import org.openxava.annotations.View;

/**
 * For testing the default schema behaviour. <p>
 *  
 * @author Javier Paniza
 */

@Entity
@Tab(defaultOrder="${description} asc")	// failed to change default schema in as400
@View(name="IssueWeb", members="id, description")
public class Issue {
	
	@Id @Column(length=5) @Required
	private String id;

	@Column(length=40) @Required
	private String description;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Worker worker;
	
	@OneToMany(mappedBy="issue", cascade=CascadeType.REMOVE)
	private Collection<Comment> comments;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Editor(value="UserAttribute", forViews="IssueWeb")
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

	public Worker getWorker() {
		return worker;
	}

	public void setWorker(Worker worker) {
		this.worker = worker;
	}

}
