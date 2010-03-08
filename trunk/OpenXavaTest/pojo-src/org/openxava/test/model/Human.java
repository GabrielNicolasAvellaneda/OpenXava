package org.openxava.test.model;

import javax.persistence.*;

/**
 * 
 * @author Javier Paniza
 */

@Entity
@DiscriminatorColumn(name="TYPE")
@DiscriminatorValue("HUM")
@Table(name="PERSON")
@AttributeOverrides(
	@AttributeOverride(name="name", column=@Column(name="PNAME"))
)
public class Human extends Nameable {

	@Enumerated(EnumType.STRING)
	private Sex sex;
	public enum Sex { MALE, FEMALE };
	
	public Sex getSex() {
		return sex;
	}
	public void setSex(Sex sex) {
		this.sex = sex;
	}
	
}
