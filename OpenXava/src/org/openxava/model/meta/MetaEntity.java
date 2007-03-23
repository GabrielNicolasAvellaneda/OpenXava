package org.openxava.model.meta;

import java.lang.reflect.*;
import java.util.*;



import org.openxava.mapping.*;
import org.openxava.util.*;

/**
 * 
 * 
 * @author Javier Paniza
 */
public class MetaEntity extends MetaModel {
	
	private Collection keyFields;
	private MetaEJB metaEJB;
	private boolean annotatedEJB3;
	
	
		
	/**
	 * @return The names of key fields. Of <tt>String</tt>.
	 */
	public Collection getKeyFields() throws XavaException {
		if (keyFields == null) {
			keyFields = new ArrayList();	
			if (isEjbGenerated() || isAnnotatedEJB3()) {
				keyFields.addAll(getAllKeyPropertiesNames());
			}
			else {		
				Field[] fields = getMetaEJB().getPrimaryKeyClass().getFields();				
				for (int i = 0; i < fields.length; i++) {
					keyFields.add(fields[i].getName());
				}
			}
		}		
		return keyFields;
	}
		
	public boolean isKey(String propertyName) throws XavaException {		 	
		if ((isAnnotatedEJB3() || isPojoGenerated() || isEjbGenerated()) &&  super.isKey(propertyName)) return true;
		return getKeyFields().contains(propertyName);		
	}
	
	public Class getPropertiesClass() throws XavaException {
		if (isAnnotatedEJB3()) return getPOJOClass();
		return super.getPropertiesClass();
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
	
	public ModelMapping getMapping() throws XavaException {
		return getMetaComponent().getEntityMapping();
	}
	
	/**
	 * EJB info about this entity.
	 * 
	 * MetaEntity always has metaEJB. Although ejb tag is not specified or
	 * we do not use EJB at all a default value is generated and returned.  
	 */
	public MetaEJB getMetaEJB() {
		if (metaEJB == null) {
			if (super.getMetaEJB() != null) metaEJB =  super.getMetaEJB();
			else {
				metaEJB = new MetaEJB();
				metaEJB.setMetaModel(this);
			}
		}
		return metaEJB;
	}

	public boolean isAnnotatedEJB3() {
		return annotatedEJB3;
	}

	public void setAnnotatedEJB3(boolean annotatedEJB3) {
		this.annotatedEJB3 = annotatedEJB3;
	}
		
}