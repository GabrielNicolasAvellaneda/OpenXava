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
	private boolean conversorCreado = false;
	private boolean conversorMultipleCreado = false;
	private String cmpTypeName;
	private ModelMapping modelMapping;
	
	public PropertyMapping(ModelMapping parent) {
		this.modelMapping = parent;
	}
	
	public String getColumn() {
		return column;
	}
	public void setColumn(String columnaTabla) {
		this.column = columnaTabla;
	}

	public String getProperty() {
		return property;
	}
	public void setProperty(String propiedadModelo) {
		this.property = propiedadModelo;
	}
	
	public void setConverterClassName(String nombreClaseConversor) {
		this.converterClassName = nombreClaseConversor;
	}

	/** 
	 * @return nulo si el mapeo no tiene conversor asignado.
	 */	
	public  IConverter getConverter() throws XavaException {
		if (!conversorCreado) {
			converter = createConverter();
			conversorCreado = true;
		}
		return converter;
	}
	
	/** 
	 * @return nulo si el mapeo no tiene conversor asignado.
	 */	
	public  IMultipleConverter getMultipleConverter() throws XavaException {
		if (!conversorMultipleCreado) {
			multipleConverter = createMultipleConverter();
			conversorMultipleCreado = true;
		}
		return multipleConverter;
	}
	
	
	public boolean hasConverter() {
		return !Is.emptyString(converterClassName);
	}
	
	public boolean hasMultipleConverter() {
		return !Is.emptyString(multipleConverterClassName); 
	}
	
	public Collection getCmpFields() {
		if (cmpFields == null) return Collections.EMPTY_LIST;
		return cmpFields;		
	}
	
	public CmpField toCmpField() throws XavaException {
		CmpField f = new CmpField();
		if (Is.emptyString(getCmpTypeName())) {
			f.setCmpTypeName(getMetaProperty().getTypeName());
		}
		else {
			f.setCmpTypeName(getCmpTypeName());
		}
		f.setColumn(getColumn());		
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
	
	
	/**
	 * Returns the nombreTipoCmp.
	 * @return String
	 */
	public String getCmpTypeName() {
		if (cmpTypeName==null) return "java.lang.Object";
		if ("String".equals(cmpTypeName)) return "java.lang.String";
		if ("Integer".equals(cmpTypeName)) return "java.lang.Integer";
		if ("Long".equals(cmpTypeName)) return "java.lang.Long";
		return cmpTypeName;
	}

	/**
	 * Sets the nombreTipoCmp.
	 * @param nombreTipoCmp The nombreTipoCmp to set
	 */
	public void setCmpTypeName(String nombreTipoCmp) {
		this.cmpTypeName = nombreTipoCmp;
	}

	/**
	 * Returns the .
	 * @return String
	 */
	public String getConverterClassName() {
		return converterClassName;
	}
	
	public void addCmpField(CmpField cmp) {
		if (cmpFields == null) cmpFields = new ArrayList();
		cmpFields.add(cmp);		
	}

	public String getMultipleConverterClassName() {
		return multipleConverterClassName;
	}

	public void setMultipleConverterClassName(String string) {
		multipleConverterClassName = string;
	}
	
	private MetaProperty getMetaProperty() throws XavaException {
		String property = Strings.change(getProperty(), "_", ".");
		return modelMapping.getMetaModel().getMetaProperty(property);
	}

}
