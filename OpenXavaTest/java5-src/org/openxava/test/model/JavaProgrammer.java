package org.openxava.test.model;

import javax.persistence.*;

import org.openxava.annotations.*;

/**
 * 
 * @author Javier Paniza
 */

@Entity
@DiscriminatorValue("JPR")
@Tab(baseCondition="${mainLanguage} = 'JAVA'")
@View(members="name, sex; mainLanguage, favouriteFramework")
public class JavaProgrammer extends Programmer {

	@Column(length=20)
	private String favouriteFramework;

	public String getFavouriteFramework() {
		return favouriteFramework;
	}

	public void setFavouriteFramework(String favouriteFramework) {
		this.favouriteFramework = favouriteFramework;
	} 
	
}
