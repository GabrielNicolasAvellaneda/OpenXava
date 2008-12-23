package org.openxava.test.model;

import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;

/**
 *  
 * @author Javier Paniza
 */

@Entity
@Views({
	@View(members="name; children"), 
	@View(name="Simple", members="name") 
})
public class Composite extends Nameable {

	@ManyToOne @JoinColumn(name="PARENT_OID") 
	private Composite composite;

	
	@OneToMany(mappedBy="composite", cascade=CascadeType.REMOVE)
	@CollectionView("Simple") 
	private Collection<Composite> children;
	
	
	public Composite getComposite() {
		return composite;
	}

	public void setComposite(Composite composite) {
		this.composite = composite;
	}		

	
	public Collection<Composite> getChildren() {
		return children;
	}

	public void setChildren(Collection<Composite> children) {
		this.children = children;
	}
	
	
}
