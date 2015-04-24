package org.openxava.test.model;

import java.util.*;

import javax.persistence.*;

/**
 * 
 * @author Javier Paniza
 */

@Entity
public class Project extends Nameable {

	@OneToMany(mappedBy="project")
	@OrderColumn(name="IDX")
	private List<ProjectMember> members;
	
	@OneToMany(mappedBy="project", cascade=CascadeType.ALL)
	@OrderColumn
	private List<ProjectTask> tasks;

	public List<ProjectMember> getMembers() {
		return members;
	}

	public void setMembers(List<ProjectMember> members) {
		this.members = members;
	}

	public List<ProjectTask> getTasks() {
		return tasks;
	}

	public void setTasks(List<ProjectTask> tasks) {
		this.tasks = tasks;
	}
	
}
