package org.openxava.test.model;

import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;
import org.openxava.model.*;

/**
 * 
 * @author Javier Paniza
 */

@Entity
public class Reallocation extends Identifiable {
	
	@Column(length=40) @Required
	private String description;
	
	@ElementCollection
	@ListProperties("place, product.number, product.description, product.unitPrice")
	private Collection<ReallocationDetail> details;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Collection<ReallocationDetail> getDetails() {
		return details;
	}

	public void setDetails(Collection<ReallocationDetail> details) {
		this.details = details;
	}

}
