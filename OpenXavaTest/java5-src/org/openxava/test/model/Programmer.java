package org.openxava.test.model;

import javax.persistence.*;

/**
 * 
 * @author Javier Paniza
 */

@Entity
@DiscriminatorValue("PRO")
public class Programmer extends Human {

	@Column(length=20)
	private String mainLanguage;

	public String getMainLanguage() {
		return mainLanguage;
	}

	public void setMainLanguage(String mainLanguage) {
		this.mainLanguage = mainLanguage;
	}
	
}
