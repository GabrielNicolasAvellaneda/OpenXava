package org.openxava.actions;

import java.util.*;

import org.openxava.mapping.*;
import org.openxava.model.meta.*;
import org.openxava.tab.*;
import org.openxava.util.*;
import org.openxava.view.*;



/**
 * @author Javier Paniza
 */

public class ReferenceSearchAction extends ViewBaseAction implements INavigationAction {
	
	private class InfoVista {
		
		View vista;		
		String nombreMiembro;
		View padre;
		
		InfoVista(View vista, String nombreMiembro, View padre) {
			this.vista = vista;
			this.nombreMiembro = nombreMiembro;
			this.padre = padre;
		}
		
	}
	
	private String keyProperty;
	private View referenceSubview;		
	private Tab tab;
	private String currentReferenceLabel; 
	
	public void execute() throws Exception {				
		InfoVista infoVista = getSubvista(getView(), crearNombreMiembro());
		View subvista = infoVista.vista;
		MetaModel metaModeloRaiz = infoVista.padre.getMetaModel();		
		getTab().setModelName(subvista.getModelName());
		MetaReference ref = getMetaReferencia(metaModeloRaiz, infoVista.nombreMiembro);
		
		ModelMapping mapeoRaiz = metaModeloRaiz.getMapping();
		if (mapeoRaiz.isReferenceOverlappingWithSomeProperty(ref.getName())) {			 
			StringBuffer condicion = new StringBuffer();			
			Iterator itPropiedadesSolapadas = mapeoRaiz.getOverlappingPropertiesOfReference(ref.getName()).iterator();			
			while (itPropiedadesSolapadas.hasNext()) {
				String propiedadReferencia = (String) itPropiedadesSolapadas.next();
				String solapada = mapeoRaiz.getOverlappingPropertyForReference(ref.getName(), propiedadReferencia);
				condicion.append("${");
				condicion.append(propiedadReferencia);
				condicion.append("} = ");				
				condicion.append(getView().getValue(solapada));				
				if (itPropiedadesSolapadas.hasNext()) {
					condicion.append(" AND "); 
				}
			}					
			getTab().setBaseCondition(condicion.toString());
		}
		else {
			getTab().setBaseCondition(null);
		}
		
		setReferenceSubview(subvista);			
		setCurrentReferenceLabel(ref.getLabel());	 
	}

	private MetaReference getMetaReferencia(MetaModel metaModeloRaiz, String nombreReferencia) throws XavaException {
		try {
			return metaModeloRaiz.getMetaReference(nombreReferencia);
		}
		catch (ElementNotFoundException ex) {
			return metaModeloRaiz.getMetaCollection(nombreReferencia).getMetaReference();
		}		
	}
	
	private InfoVista getSubvista(View vista, String nombreMiembro) throws XavaException {
		if (nombreMiembro.indexOf('.') < 0) {
			return new InfoVista(vista.getSubview(nombreMiembro), nombreMiembro, vista); 
		}
		StringTokenizer st = new StringTokenizer(nombreMiembro, ".");
		String nombreSubvista = st.nextToken();
		String siguienteMiembro = st.nextToken(); 
		return getSubvista(vista.getSubview(nombreSubvista), siguienteMiembro);
	}
	
	private String crearNombreMiembro() {		
		String prefijo = "xava." + getModelName() + ".";		
		String nombrePropiedad = keyProperty.substring(prefijo.length());				
		int idx = nombrePropiedad.lastIndexOf(".");		
		if (idx >= 0) return nombrePropiedad.substring(0, idx); 	
		return nombrePropiedad;
	}

	public String getKeyProperty() {
		return keyProperty;
	}

	public void setKeyProperty(String string) {
		keyProperty = string;		
	}

	public String[] getNextControllers() {		
		return new String[]{ "ReferenceSearch" };
	}

	public String getCustomView() {		
		return "xava/referenceSearch";
	}

	public Tab getTab() {
		return tab;
	}

	public void setTab(Tab tab) {
		this.tab = tab;
	}

	public View getReferenceSubview() {
		return referenceSubview;
	}

	public void setReferenceSubview(View vista) {
		referenceSubview = vista;
	}

	public String getCurrentReferenceLabel() {
		return currentReferenceLabel;
	}

	public void setCurrentReferenceLabel(String string) {
		currentReferenceLabel = string;
	}

}
