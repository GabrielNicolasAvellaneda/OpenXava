package org.openxava.test.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.openxava.annotations.OnChange;
import org.openxava.annotations.Required;
import org.openxava.annotations.Stereotype;
import org.openxava.annotations.View;
import org.openxava.annotations.Views;
import org.openxava.test.actions.OnChangeGroupInColor;

/**
 * Number (the key) can be 0
 *  
 * @author Javier Paniza
 */

@Entity
@Views({
	@View( name="Ordinary", members="number; name; sample"),
	@View( name="View1", members="property1"), 
	@View( name="View2", members="property2"), 
	@View( name="View2Sub1", members="property2Sub1"), 
	@View( name="View2Sub2", members="property2Sub2"),
	@View( name="Groups", members="group; group1[property1], group2[property2]")
})
public class Color {

	@Id @Column(length=5)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer number;
	
	@Column(length=20) @Required
	private String name;
	
	@Version 
	private int version;
	
	@Stereotype("IMAGE_LABEL")
	public String getSample() {
		if ("red".equalsIgnoreCase(name) ||
			"rojo".equalsIgnoreCase(name)) return "RED";
		if ("black".equalsIgnoreCase(name) ||
			"negro".equalsIgnoreCase(name)) return "BLACK";
		return "nocolor";		
	}
	
	@Transient
	private String property1;
	
	@Transient
	private String property2;
	
	@Transient
	private String property2Sub1;
	
	@Transient
	private String property2Sub2;
	
	@Transient
	@OnChange(OnChangeGroupInColor.class)
	private Group group;
	public enum Group { GROUP1, GROUP2 }
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getProperty1() {
		return property1;
	}

	public void setProperty1(String property1) {
		this.property1 = property1;
	}

	public String getProperty2() {
		return property2;
	}

	public void setProperty2(String property2) {
		this.property2 = property2;
	}

	public String getProperty2Sub1() {
		return property2Sub1;
	}

	public void setProperty2Sub1(String property2Sub1) {
		this.property2Sub1 = property2Sub1;
	}

	public String getProperty2Sub2() {
		return property2Sub2;
	}

	public void setProperty2Sub2(String property2Sub2) {
		this.property2Sub2 = property2Sub2;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

}
