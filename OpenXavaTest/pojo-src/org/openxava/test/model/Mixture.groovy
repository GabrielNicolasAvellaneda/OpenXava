package org.openxava.test.model

import javax.persistence.*;

import org.openxava.util.*;

/**
 * Create on 20/12/2010 (16:17:35)
 * @author Ana Andres
 */

@IdClass(MixtureKey)
@Entity
class Mixture {
	
	@Id
	String colorName1
	
	@Id
	String colorName2
	
	String description
	
	String getFullDescription(){
		String one = colorName1.trim()
		one = Strings.repeat(10 - one.length(), "-") + colorName1.trim()
		String two = colorName2.trim()
		two = Strings.repeat(10 - two.length(), "-") + colorName2.trim()
		
		return one + "&" + two + ":" + description
	}
}
