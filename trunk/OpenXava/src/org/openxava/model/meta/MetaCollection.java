package org.openxava.model.meta;

import java.rmi.*;
import java.util.*;

import org.openxava.util.*;
import org.openxava.validators.*;

/**
 * 
 * @author Javier Paniza
 */

public class MetaCollection extends MetaMember implements IPropertyValidator {
	
	private Collection metaPropertiesFinderArguments;
	private int minimum;
	private String condition;
	private String order;
	
	private MetaReference metaReference;
	private Collection metaCalculatorsPostRemove;
	private MetaCalculator metaCalculator;
	
	public void addMetaCalculatorPostRemove(MetaCalculator metaCalculator) {
		if (metaCalculatorsPostRemove == null) {
			metaCalculatorsPostRemove = new ArrayList();			
		}
		metaCalculatorsPostRemove.add(metaCalculator);		
	}
	
	public Collection getMetaCalculatorsPostRemove() {
		return metaCalculatorsPostRemove == null?Collections.EMPTY_LIST:metaCalculatorsPostRemove;
	}
	
	public boolean hasPostRemoveCalculators() {
		return !getMetaCalculatorsPostRemove().isEmpty();
	}

	public int getMinimum() {
		return minimum;
	}
	public void setMinimum(int minimum) {
		this.minimum = minimum;
	}


	public MetaReference getMetaReference() {
		return metaReference;
	}
	public void setMetaReference(MetaReference reference) {
		this.metaReference = reference;
		if (getMetaModel() != null && this.metaReference != null) {
			this.metaReference.setMetaModel(getMetaModel());
		}
	}

	public boolean isAggregate() throws XavaException {
		return getMetaReference().isAggregate();
	}

	public void setMetaModel(MetaModel newContainer) {
		super.setMetaModel(newContainer);
		if (metaReference != null) {
			metaReference.setMetaModel(newContainer);
		}
	}
	/**
	 * @param errors  Object that acumulate the list of validation errors.
	 * @param object Collection to validate. Has to be of type Collection. If null empty collection is assumed.
	 * @param objectName Object that contains the collection to validate.
	 * @param propertyName Name of the collection property within objectName. 
	 */
	public void validate(
		Messages errors,
		Object object,
		String objectName,
		String propertyName)
		throws RemoteException {			
		object = (object == null)?Collections.EMPTY_SET:object;	
		if (!(object instanceof Collection)) {
			throw new IllegalArgumentException(XavaResources.getString("only_validate_collection"));
		}	
		Collection c = (Collection) object;
		if (c.size() < getMinimum()) {
			String idElements = getMinimum() == 1?"element":"elements";
			errors.add("minimum_elements", new Integer(getMinimum()), idElements, getName());
		}						
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String string) {
		order = string;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String string) {
		condition = string;
	}
	
	public String getSQLCondition() throws XavaException {
		if (Is.emptyString(getCondition())) return "";
		String condicion = changePropertiesThisByArguments(getCondition(), false);
		return getMetaReference().getMetaModelReferenced().getMapping().changePropertiesByColumns(condicion);
	}
	
	public String getEJBQLCondition() throws XavaException {
		MetaModel metaModel = getMetaReference().getMetaModelReferenced(); 
		StringBuffer sb = new StringBuffer("SELECT OBJECT(o) FROM ");
		sb.append(metaModel.getName());
		sb.append(" o");
		if (!Is.emptyString(this.condition)) {
			sb.append(" WHERE ");			
			String condition = changePropertiesThisByArguments(getCondition(), true); 			
			sb.append(metaModel.getMapping().changePropertiesByCMPAttributes(condition));
		}
		if (!Is.emptyString(this.order)) { 		
			sb.append(" ORDER BY ");
			sb.append(getMetaReference().getMetaModelReferenced().getMapping().changePropertiesByCMPAttributes(this.order));
		}
		return sb.toString();
	}
	
	private String changePropertiesThisByArguments(String source, boolean ejbql) throws XavaException {			
		StringBuffer r = new StringBuffer(source);
		Collection properties = new ArrayList();
		int i = r.toString().indexOf("${this.");
		int f = 0;			
		int c = 0;
		while (i >= 0) {
			f = r.toString().indexOf("}", i+2);
			if (f < 0) break;
			String property = r.substring(i+2, f);
			String argument = ejbql?"?" + (++c):"{" + (c++) + "}"; 
			r.replace(i, f+1, argument);
			i = r.toString().indexOf("${this.");
		}			
		return r.toString();
	}
	
	
	public String getSQLOrder() throws XavaException {
		if (Is.emptyString(getOrder())) return "";
		return getMetaReference().getMetaModelReferenced().getMapping().changePropertiesByColumns(getOrder());
	}
	
	public boolean hasCondition() {
		return !Is.emptyString(getCondition());	
	}
	
	/**
	 * Util to generate EJB code. 	 
	 */
	public String getFinderName() {
		return Strings.firstUpper(getName()) + "Of" + getMetaModel().getName(); 
	}

	/**
	 * Util to generate EJB code.	 
	 */
	public String getFinderArguments() throws XavaException {
		StringBuffer arguments = new StringBuffer();
		Iterator it = getMetaPropertiesFinderArguments().iterator();
		while (it.hasNext()) {
			MetaProperty pr = (MetaProperty) it.next();
			arguments.append(pr.getType().getName());
			arguments.append(' ');
			arguments.append(pr.getName());
			if (it.hasNext()) {
				arguments.append(", ");		 	
			}
		}
		return arguments.toString();
	}

	/**
	 * Util to generate EJB code. 	 
	 */	
	public Collection getMetaPropertiesFinderArguments() throws XavaException {
		if (metaPropertiesFinderArguments == null) {
			metaPropertiesFinderArguments = new ArrayList();
			String condition = getCondition();
			int i = condition.indexOf("${this.");
			int f = 0;			
			while (i >= 0) {
				f = condition.indexOf("}", i+2);
				if (f < 0) break;
				String propertyName = condition.substring(i+7, f);
				MetaProperty original = getMetaModel().getMetaProperty(propertyName);
				MetaProperty pr = new MetaProperty();
				boolean isQualified = propertyName.indexOf('.') >= 0;
				if (isQualified) {
					pr.setName(Strings.change(propertyName, ".", "_"));
				}
				else {
					pr.setName(propertyName);				
				}
				if (original.getMapping().hasConverter()) {
					if (!isQualified) {
						pr.setName("_" + Strings.firstUpper(pr.getName()));
					}					
					String typeCmp = original.getMapping().getCmpTypeName();
					if (Is.emptyString(typeCmp)) {
						throw new XavaException("tipo_cmp_required", original.getName());
					}
					pr.setTypeName(typeCmp);
				}
				else {
					pr.setTypeName(original.getTypeName());
				}				
				metaPropertiesFinderArguments.add(pr);
				i = condition.indexOf("${this.",f);
			}						
		}
		return metaPropertiesFinderArguments;
	}
		

	public MetaCalculator getMetaCalculator() {
		return metaCalculator;
	}
	public void setMetaCalculator(MetaCalculator metaCalculator) {
		this.metaCalculator = metaCalculator;
	}
	
	public boolean hasCalculator() {
		return metaCalculator != null;
	}
	
}

