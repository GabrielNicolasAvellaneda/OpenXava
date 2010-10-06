package org.openxava.invoicing.model;

import javax.persistence.*;
import org.openxava.annotations.*;

@MappedSuperclass
public class Deletable extends Identifiable {
	
	@Hidden
	private boolean deleted;
	
	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

}
