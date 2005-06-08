package org.openxava.model.meta;

import java.io.*;
import java.util.*;

import org.openxava.util.*;

/**
 * @author Javier Paniza
 */
public class MetaFinder implements Serializable {
	
	private static Map argumentsJBoss11ToEJBQL;
	 	
	private String name;
	private String arguments;	
	private boolean collection;
	private String condition;
	private String order;
	private MetaModel metaModel;
	
	public String getArguments() {
		arguments = Strings.change(arguments, "String", "java.lang.String");
		arguments = Strings.change(arguments, "java.lang.java.lang.String", "java.lang.String");
		return arguments;
	}

	public boolean isCollection() {
		return collection;
	}

	public String getCondition() {
		return condition;
	}

	public String getName() {
		return name;
	}

	public void setArguments(String arguments) {
		this.arguments = arguments;
	}

	public void setCollection(boolean collection) {
		this.collection = collection;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getSQLCondition() throws XavaException {		
		if (Is.emptyString(this.condition)) return "1=1";
		return getMetaModel().getMapping().changePropertiesByColumns(this.condition);
	}
	
	public String getEJBQLCondition() throws XavaException {
		StringBuffer sb = new StringBuffer("SELECT OBJECT(o) FROM ");
		sb.append(getMetaModel().getName());
		sb.append(" o");
		if (!Is.emptyString(this.condition)) {
			sb.append(" WHERE ");			
			String attributesCondition = getMetaModel().getMapping().changePropertiesByCMPAttributes(this.condition);			 
			sb.append(Strings.change(attributesCondition, getArgumentsJBoss11ToEJBQL()));
		}
		if (!Is.emptyString(this.order)) {			
			sb.append(" ORDER BY ");
			sb.append(getMetaModel().getMapping().changePropertiesByCMPAttributes(this.order));
		}
		return sb.toString();
	}
	
	public MetaModel getMetaModel() {
		return metaModel;
	}

	public void setMetaModel(MetaModel metaModel) {
		this.metaModel = metaModel;
	}

	public String getOrder() {
		return order;
	}
	
	public String getSQLOrder() throws XavaException {
		if (Is.emptyString(this.order)) return "";
		return getMetaModel().getMapping().changePropertiesByColumns(this.order);
	}

	public void setOrder(String order) {
		this.order = order;
	}
	
	private static Map getArgumentsJBoss11ToEJBQL() {
		if (argumentsJBoss11ToEJBQL == null) {
			argumentsJBoss11ToEJBQL = new HashMap();
			for (int i=0; i<30; i++) {
				argumentsJBoss11ToEJBQL.put("{" + i+ "}", "?" + (i+1));
			}			
		}
		return argumentsJBoss11ToEJBQL;
	}

}
