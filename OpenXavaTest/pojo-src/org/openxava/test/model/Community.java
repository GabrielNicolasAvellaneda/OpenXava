package org.openxava.test.model;

import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;

/**
 * 
 * @author Javier Paniza
 */

@Entity
@View(name="EditableMembers") // tmp
public class Community extends Nameable {
	
	@ManyToMany
	@ListAction("ManyToMany.new")
	@EditAction(forViews="EditableMembers", value="ManyToMany.edit") // tmp
	@OrderBy("id asc") // tmp
	private Collection<Human> members;

	public void setMembers(Collection<Human> members) {
		this.members = members;
	}

	public Collection<Human> getMembers() {
		return members;
	}
	
}
