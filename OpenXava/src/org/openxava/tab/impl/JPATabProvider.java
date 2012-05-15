package org.openxava.tab.impl;

import java.rmi.*;
import java.util.*;

import javax.persistence.*;

import org.apache.commons.logging.*;
import org.openxava.jpa.*;
import org.openxava.mapping.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;

/** 
 * An <code>ITabProvider</code> that obtain data via JPA. <p>
 *
 * @author  Javier Paniza
 */

public class JPATabProvider extends TabProviderBase {
	
	private static Log log = LogFactory.getLog(JPATabProvider.class);

	protected String translateCondition(String condition) {
		int i = 0;
		while (condition.contains("?")) {		
			condition = condition.replaceFirst("\\?", ":p" + (i++));
		}
		return condition; 
	}
	
	public String toQueryField(String propertyName) {		
		return "e." + propertyName;
	}

	public String translateSelect(String select) {
		select = changePropertiesByJPAProperties(select);
		select = Strings.noLastToken(select.trim());
		select = select + " " + getMetaModel().getName() + " e";
		return select;
	}
	
	private String changePropertiesByJPAProperties(String source) { // tmp ¿Unificar con changePropertiesByColumns?
		StringBuffer r = new StringBuffer(source);		
		int i = r.toString().indexOf("${");
		int f = 0;
		while (i >= 0) {
			f = r.toString().indexOf("}", i + 2);
			if (f < 0)
				break;
			String property = r.substring(i + 2, f);
			String jpaProperty = "0"; // thus it remained if it is calculated
			if (!getMetaModel().isCalculated(property)) {
				jpaProperty = "e." + property;				
			}			
			r.replace(i, f + 1, jpaProperty);
			i = r.toString().indexOf("${");
		}
		return r.toString();
	}

	public DataChunk nextChunk() throws RemoteException {		
		if (getSelect() == null || isEOF()) { // search not called yet
			return new DataChunk(Collections.EMPTY_LIST, true, getCurrent()); // Empty
		}		
		try { 
			List data = nextBlock();			
			setCurrent(getCurrent() + data.size());			
			setEOF(data.size() != getChunkSize());
			return new DataChunk(data, isEOF(), getCurrent());
		}
		catch (Exception ex) {
			log.error(XavaResources.getString("select_error", getSelect()), ex);
			throw new RemoteException(XavaResources.getString("select_error", getSelect()));
		}
	}
		
	private List<Object []> nextBlock() {		
		if (keyHasNulls()) return null; // Because some databases (like Informix) have problems setting nulls
				
		Query query = XPersistence.getManager().createQuery(getSelect()); 
		// Fill key values
		StringBuffer message =
			new StringBuffer("[JPATabProvider.nextBlock] ");
		message.append(XavaResources.getString("executing_select", getSelect()));		
		
		Object [] key = getKey(); 
		for (int i = 0; i < key.length; i++) {
			query.setParameter("p" + i, key[i]);
			message.append(key[i]);
			if (i < key.length - 1)
				message.append(", ");
		}
		log.debug(message);
		
		query.setMaxResults(getChunkSize()); 
		query.setFirstResult(getCurrent());
		return query.getResultList();						
	}

	protected Number executeNumberSelect(String select, String errorId) {
		if (select == null || keyHasNulls()) return 0;						
		try {			
			Query query = XPersistence.getManager().createQuery(select);
			Object [] key = getKey();
			for (int i = 0; i < key.length; i++) {
				query.setParameter("p" + i, key[i]);				
			}			
			
			return (Number) query.getSingleResult();
		}
		catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw new XavaException(errorId);
		}
	}

}
