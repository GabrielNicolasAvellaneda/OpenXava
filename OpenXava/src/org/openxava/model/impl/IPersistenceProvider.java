package org.openxava.model.impl;

import java.util.*;

import javax.ejb.*;

import net.sf.hibernate.*;

import org.openxava.model.meta.*;
import org.openxava.util.*;

/**
 * Provides the implementation of the persistence services
 * used in {@link MapFacadeBean}. <p>
 * 
 * @author Mª Carmen Gimeno Alabau
 */
public interface IPersistenceProvider {
	
	void setSession(Session session); 
	Object find(IMetaEjb metaModel, Map keyValues) throws FinderException, XavaException;
	
	
}
