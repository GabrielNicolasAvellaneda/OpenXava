package org.openxava.mapping;

import java.util.*;

import org.openxava.converters.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;
import org.openxava.util.meta.*;

public class PropertyMapping extends MetaSetsContainer {
	
	private ArrayList cmpFields;
	private String property;
	private String column;
	private String converterClassName;
	private String multipleConverterClassName;
	private IConverter converter;
	private IMultipleConverter multipleConverter;
	private boolean converterCreated = false;
	private boolean multpleConverterCreated = false;
	private String cmpTypeName;
	private ModelMapping modelMapping;
	
	public PropertyMapping(ModelMapping parent) {
		this.modelMapping = parent;
	}
	
	public String getColumn() {
		return column;
	}
	public void setColumn(String tableColumn) {
		this.column = tableColumn;
	}

	public String getProperty() {
		return property;
	}
	public void setProperty(String modelProperty) {
		this.property = modelProperty;
	}
	
	public void setConverterClassName(String converterClassName) {
		this.converterClassName = converterClassName;		
	}

	/** 
	 * @return Null if mapping does not have converter
	 */	
	public  IConverter getConverter() throws XavaException {
		if (!converterCreated) {
			converter = createConverter();
			converterCreated = true;
		}
		return converter;
	}
	
	/** 
	 * @return Null if mapping does not have multiple converter
	 */	
	public  IMultipleConverter getMultipleConverter() throws XavaException {
		if (!multpleConverterCreated) {
			multipleConverter = createMultipleConverter();
			multpleConverterCreated = true;
		}
		return multipleConverter;
	}
	
	
	public boolean hasConverter() {
		return !Is.emptyString(converterClassName);
	}
	
	public boolean hasMultipleConverter() {
		return !Is.emptyString(multipleConverterClassName); 
	}
	
	public Collection getCmpFields() throws XavaException {
		if (cmpFields == null) return Collections.singletonList(toCmpField());
		return cmpFields;		
	}
	
	CmpField toCmpField() throws XavaException {
		CmpField f = new CmpField();
		if (Is.emptyString(getCmpTypeName())) {
			f.setCmpTypeName(getMetaProperty().getType().getName());
		}
		else {
			f.setCmpTypeName(getCmpTypeName());
		}
		f.setColumn(getColumn());
		if (hasConverter()) {
			f.setCmpPropertyName("_" + Strings.firstUpper(getProperty()));
		}
		else {
			f.setCmpPropertyName(getProperty());
		}
		return f;
	}
		
	private IConverter createConverter() throws XavaException {
		try {
			if (!hasConverter()) return null;
			IConverter conversor = (IConverter) Class.forName(converterClassName).newInstance();
			if (containsMetaSets()) {
				assignPropertiesValues(conversor);
			}						
			return conversor;
		}
		catch (ClassCastException ex) {
			ex.printStackTrace();
			throw new XavaException("create_converter_classcast_error", getProperty(), converterClassName, "IConverter");
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new XavaException("create_converter_error", getProperty(), ex.getLocalizedMessage());
		}
	}
	
	private IMultipleConverter createMultipleConverter() throws XavaException {
		try {
			if (!hasMultipleConverter()) return null;
			IMultipleConverter conversor = (IMultipleConverter) Class.forName(multipleConverterClassName).newInstance();
			if (containsMetaSets()) {
				assignPropertiesValues(conversor);
			}						
			return conversor;
		}
		catch (ClassCastException ex) {
			ex.printStackTrace();
			throw new XavaException("create_converter_classcast_error", getProperty(), converterClassName, "IMultipleConverter");
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new XavaException("create_converter_error", getProperty(), ex.getLocalizedMessage());
		}
	}
		
	public String getCmpTypeName() {
		if ("String".equals(cmpTypeName)) return "java.lang.String";
		if ("Integer".equals(cmpTypeName)) return "java.lang.Integer";
		if ("Long".equals(cmpTypeName)) return "java.lang.Long";
		return cmpTypeName;
	}
	
	public Class getCmpType() throws ClassNotFoundException {
		return Class.forName(getCmpTypeName());
	}

	public void setCmpTypeName(String cmpTypeName) {
		this.cmpTypeName = cmpTypeName;
	}

	public String getConverterClassName() {
		return converterClassName;
	}
	
	public void addCmpField(CmpField cmp) {
		if (cmpFields == null) cmpFields = new ArrayList();
		cmp.setCmpPropertyName(getProperty() + "_" + cmp.getConverterPropertyName());
		cmpFields.add(cmp);		
	}

	public String getMultipleConverterClassName() {
		return multipleConverterClassName;
	}

	public void setMultipleConverterClassName(String string) {
		this.multipleConverterClassName = string;
	}
	
	private MetaProperty getMetaProperty() throws XavaException {
		String property = Strings.change(getProperty(), "_", ".");
		return modelMapping.getMetaModel().getMetaProperty(property);
	}

}
