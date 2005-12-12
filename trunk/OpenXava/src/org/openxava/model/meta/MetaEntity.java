package org.openxava.model.meta;


import java.util.*;

import org.openxava.util.*;

/**
 * 
 * 
 * @author Javier Paniza
 */
abstract public class MetaEntity extends MetaModel {
	
	/**
	 * @return The names of key fields. Of <tt>String</tt>.
	 */
	abstract public Collection getKeyFields() throws XavaException;
		
	public boolean isKey(String propertyName) throws XavaException {	
		if (isGenerate() && super.isKey(propertyName)) return true; 	
		return getKeyFields().contains(propertyName);		
	}
	
	/**
	 * If has key fields that aren't properties hence does not math with key properties. <p>
	 */
	public boolean hasHiddenKeys() throws XavaException {		
		return !getKeyPropertiesNames().containsAll(getKeyFields());
	}
	
	public String getId() {
		return getName();
	}

}