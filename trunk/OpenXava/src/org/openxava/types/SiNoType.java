package org.openxava.types;

import org.hibernate.type.*;

/**
 * Stores a boolean value as 'S' or 'N' in database. <p>
 * 
 * @author Javier Paniza
 */

public class SiNoType extends CharBooleanType {

	@Override
	protected String getTrueString() {
		return "S";
	}

	@Override
	protected String getFalseString() {
		return "N";
	}

}
