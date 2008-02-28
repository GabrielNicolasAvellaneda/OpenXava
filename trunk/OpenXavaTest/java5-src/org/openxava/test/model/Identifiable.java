package org.openxava.test.model;

import javax.persistence.*;

import org.hibernate.annotations.*;
import org.openxava.annotations.*;

/**
 * Base class for defining entities with a UUID oid. <p>
 * 
 * @author Javier Paniza
 */

@MappedSuperclass
public class Identifiable {
	
	@Id @GeneratedValue(generator="system-uuid") @Hidden 
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	private String oid;

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}	

}
