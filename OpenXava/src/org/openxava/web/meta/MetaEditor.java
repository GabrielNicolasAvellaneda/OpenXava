package org.openxava.web.meta;

import java.util.*;

import org.openxava.formatters.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;
import org.openxava.util.meta.*;



/**
 * Editor asociado a un tipo.
 * 
 * @author Javier Paniza
 */
public class MetaEditor {
	
	private boolean formatterFromType;
	private IFormatter formatter;
	private String propertiesURL;
	private java.lang.String url;	
	private Map properties;
	private Collection stereotypesIDepend;
	private Collection propertiesIDepend;
	private String formatterClassName;
	private Collection formatterMetaSets;
	private boolean format = true;
	private boolean frame = false;
	
	public void _addFormatterMetaSet(MetaSet metaSet) {
		if (formatterMetaSets == null) formatterMetaSets = new ArrayList();
		formatterMetaSets.add(metaSet);
	}

	public java.lang.String getUrl() {
		return url + getPropertiesURL();
	}


	public void setUrl(java.lang.String string) {
		url = string;
	}

	public void addProperty(String nombre, String valor) {
		if (properties == null) properties = new HashMap();
		properties.put(nombre, valor);
		propertiesURL = null;		
	}
	
	private String getPropertiesURL() {
		if (propertiesURL == null) {
			if (properties == null) propertiesURL="";
			else {
				StringBuffer sb = new StringBuffer("?");
				Iterator it = properties.entrySet().iterator();
				while (it.hasNext()) {					
					Map.Entry e = (Map.Entry) it.next();
					sb.append(e.getKey());
					sb.append("=");
					sb.append(e.getValue());
					if (it.hasNext()) sb.append("&");
				}
				propertiesURL = sb.toString();
			}
		}		
		return propertiesURL; 
	}
	
	public void setDependsStereotypes(String estereotipos) {
		if (estereotipos == null) return;
		StringTokenizer st = new StringTokenizer(estereotipos, ",");
		stereotypesIDepend = new ArrayList();
		while (st.hasMoreTokens()) {
			stereotypesIDepend.add(st.nextToken().trim());			
		}
	}
	
	public void setDependsProperties(String propiedades) {				
		if (propiedades == null) return;
		StringTokenizer st = new StringTokenizer(propiedades, ",");
		propertiesIDepend = new ArrayList();
		while (st.hasMoreTokens()) {
			propertiesIDepend.add(st.nextToken().trim());			
		}
	}
	
	public boolean depends(MetaProperty p) {		
		if (dependsStereotype(p)) return true;		
		return dependsPropertyName(p);
	}		
		
	public boolean dependsStereotype(MetaProperty p) {
		if (stereotypesIDepend == null) return false; 
		if (!p.hasStereotype()) return false;
		return stereotypesIDepend.contains(p.getStereotype()); 
	}
	
	private boolean dependsPropertyName(MetaProperty p) {
		if (propertiesIDepend == null) return false; 		
		return propertiesIDepend.contains(p.getName()); 
	}

	public boolean hasFormatter() {		
		return !Is.emptyString(formatterClassName);
	}
	
	/**
	 * 
	 * @return Nunca nulo
	 * @throws XavaException Por ej, si claseFormat es cadena vacía
	 */
	public IFormatter getFormatter() throws XavaException {
		if (formatter == null) {
			if (Is.emptyString(formatterClassName)) {
				throw new XavaException("no_formatter_class_error");
			}
			try {
				formatter = (IFormatter) Class.forName(formatterClassName).newInstance();
				if (formatterMetaSets != null) {
					PropertiesManager pm = new PropertiesManager(formatter);
					for (Iterator it = formatterMetaSets.iterator(); it.hasNext(); ) {
						MetaSet metaSet = (MetaSet) it.next();
						pm.executeSetFromString(metaSet.getPropertyName(), metaSet.getValue());
					}
				}				
			}
			catch (ClassCastException ex) {
				throw new XavaException("implements_required", formatterClassName, IFormatter.class.getName());
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new XavaException("create_formatter_error", formatterClassName);
			}
		}
		return formatter;
	}
	
	public String getFormatterClassName() {
		return formatterClassName;
	}

	public void setFormatterClassName(String string) {
		formatterClassName = string;
	}

	public boolean isFormat() {
		return format;
	}

	public void setFormat(boolean b) {
		format = b;
	}

	public boolean isFrame() {
		return frame;
	}

	public void setFrame(boolean b) {
		frame = b;
	}

	public boolean isFormatterFromType() {
		return formatterFromType;
	}
	public void setFormatterFromType(boolean formatterFromType) {
		this.formatterFromType = formatterFromType;
	}
}