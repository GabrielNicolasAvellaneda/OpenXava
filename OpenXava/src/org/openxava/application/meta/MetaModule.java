package org.openxava.application.meta;


import java.util.*;

import org.openxava.controller.*;
import org.openxava.util.meta.*;





/**
 * @author Javier Paniza
 */
public class MetaModule extends MetaElement implements java.io.Serializable {
	
	private Map environmentVariables;
	private Environment environment;
	private String modelName;
	private String swingViewClass;
	private String webViewURL;
	private String viewName;
	private String tabName;
	private MetaApplication metaApplication;	
	private Collection controllersNames = new ArrayList();
	private String modeControllerName;	
	private MetaReport metaReport;



	/**
	 * Gets the modelo
	 * @return Returns a String
	 */
	public String getModelName() {
		return modelName;
	}
	/**
	 * Sets the modelo
	 * @param modelo The modelo to set
	 */
	public void setModelName(String modelo) {
		this.modelName = modelo;
	}


	/**
	 * Gets the aplicacion
	 * @return Returns a Aplicacion
	 */
	public MetaApplication getMetaApplication() {
		return metaApplication;
	}
	/**
	 * Sets the aplicacion
	 * @param aplicacion The aplicacion to set
	 */
	public void setMetaApplication(MetaApplication aplicacion) {
		this.metaApplication = aplicacion;
	}


	/**
	 * Gets the controladores
	 * @return De <tt>String</tt>. 
	 */
	public Collection getControllersNames() {
		return controllersNames;
	}
	
	
	public void addControllerName(String controlador) {
		controllersNames.add(controlador);
	}


	public String getSwingViewClass() {
		return swingViewClass;
	}

	public void setSwingViewClass(String swingViewClass) {
		this.swingViewClass = swingViewClass;
	}


	public MetaReport getMetaReport() {
		return metaReport;
	}

	public void setMetaReport(MetaReport metaListado) {
		this.metaReport = metaListado;
	}
	
	public String getViewName() {
		return viewName;
	}

	public void setViewName(String nombreVista) {
		this.viewName = nombreVista;
	}

	public String getTabName() {
		return tabName==null?"":tabName;
	}

	public void setTabName(String string) {
		tabName = string;
	}
	
	public String getId() {
		return getMetaApplication().getId() + "." + getName();
	}


	public String getModeControllerName() {
		return modeControllerName;
	}

	public void setModeControllerName(String string) {
		modeControllerName = string;
	}

	public String getWebViewURL() {
		return webViewURL;
	}

	public void setWebViewURL(String string) {
		webViewURL = string;
	}
	public Environment getEnvironment() {
		if (environment == null) {
			environment = new Environment(environmentVariables);
		}
		return environment;
	}

	public void addEnvironmentVariable(String nombre, String valor) {
		if (environmentVariables == null) environmentVariables = new HashMap();
		environmentVariables.put(nombre, valor);
	}	

}


