package org.openxava.test.model;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.*;

/**
 * 
 * @author Javier Paniza
 */

@Entity
@View(name="Simple", members="code, model, make")
public class Vehicle {
	
	@Id @GeneratedValue(generator="system-uuid") @Hidden 
	@GenericGenerator(name="system-uuid", strategy="uuid")
	private String oid;
	
	@Column(length=5)
	private String code;
	
	@Column(length=40)
	private String model;
	
	@Column(length=20)
	private String make;

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}
	
}
