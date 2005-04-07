package org.openxava.model.meta;
import java.util.*;

import org.openxava.component.*;
import org.openxava.mapping.*;
import org.openxava.util.*;

/**
 * @author Javier Paniza
 */
public interface IMetaModel {
	String getName(); 
	Collection getMetaFinders();	
	MetaProperty getMetaProperty(String nombre) throws ElementNotFoundException, XavaException;
	MetaReference getMetaReference(String nombre) throws ElementNotFoundException, XavaException;
	MetaMethod getMetaMethod(String nombre) throws ElementNotFoundException, XavaException;
	Collection getKeyPropertiesNames() throws XavaException;
	Collection getAllKeyPropertiesNames() throws XavaException;
	Collection getMetaProperties();
	Collection getMetaReferences();
	Collection getMetaEntityReferences() throws XavaException;
	Collection getMetaAggregateReferences() throws XavaException;
	Collection getMetaCollections();
	Collection getMetaMethods();
	MetaComponent getMetaComponent();
	ModelMapping getMapping() throws XavaException;
	Collection getMetaPropertiesWithDefaultValueOnCreate();
	Collection getMetaPropertiesKey() throws XavaException;
	Collection getAllMetaPropertiesKey() throws XavaException;
	Collection getMetaPropertiesPersistents() throws XavaException;
	MetaModel getMetaModelContainer() throws XavaException;
	Collection getMetaReferencesWithMapping() throws XavaException;
	int getMetaCalculatorsPostCreateCount();
	MetaCalculator getMetaCalculatorPostCreate(int idx);
	int getMetaCalculatorsPostModifyCount();
	MetaCalculator getMetaCalculatorPostModify(int idx);
	Collection getMetaCollectionsWithConditionInOthersModels() throws XavaException;
	Map extractKeyValues(Map valores) throws XavaException;
	Class getPropertiesClass() throws XavaException;
	Collection getInterfacesNames();
	String getImplements();
	boolean isKey(String name) throws XavaException;
	Collection getRecursiveQualifiedPropertiesNames() throws XavaException;
}