package org.openxava.test.model;

import java.util.*;

import javax.persistence.*;

/**
 * 
 * @author Javier Paniza
 */

@Entity
public class Community extends Nameable {
	
	@ManyToMany
	private Collection<Human> members;

	public void setMembers(Collection<Human> members) {
		this.members = members;
	}

	public Collection<Human> getMembers() {
		return members;
	}
	
}
