package org.openxava.tab.impl;

import java.io.*;
import java.rmi.*;
import java.util.*;

import javax.ejb.*;

import org.openxava.calculators.*;
import org.openxava.converters.*;
import org.openxava.mapping.*;
import org.openxava.model.*;
import org.openxava.util.*;
import org.openxava.util.meta.*;


/**
 * 
 * @author Javier Paniza
 */
public class EntityTabDataProvider implements IEntityTabDataProvider, Serializable {
	
	private String componentName;
	private IConnectionProvider connectionProvider;
		
	public DataChunk nextChunk(ITabProvider tabProvider, String nombreModelo, List nombresPropiedades, Collection calculadoresTab, Map indicesClave, Collection conversoresTab) throws RemoteException {		
		if (tabProvider instanceof JDBCTabProvider) {
			((JDBCTabProvider) tabProvider).setConnectionProvider(getConnectionProvider());
		}
		DataChunk tv = null;
		try {
			tv = tabProvider.nextChunk();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new RemoteException(XavaResources.getString("tab_next_chunk_error"));
		}
		
		List data = tv.getData();
		int l = data.size();
		
		// Conversion
		try {
			if (conversoresTab != null) {
				for (int i = 0; i < l; i++) {
					data.set(i, hacerConversiones((Object[]) data.get(i), conversoresTab));
				}
			}
		}
		catch (XavaException ex) {
			ex.printStackTrace();
			throw new RemoteException(XavaResources.getString("tab_conversion_error"));
		}
				
		// calculos	
		try {
			if (calculadoresTab != null) {
				for (int i = 0; i < l; i++) {
					data.set(i, hacerCalculos(nombreModelo, (Object[]) data.get(i), calculadoresTab, indicesClave, nombresPropiedades));
				}
			}
		}
		catch (XavaException ex) {
			ex.printStackTrace();
			throw new RemoteException(XavaResources.getString("tab_calculate_properties_error"));
		}
							
		return tv;
	}


	public IConnectionProvider getConnectionProvider() throws RemoteException {
		if (connectionProvider == null) {			 		
			try {
				connectionProvider = DataSourceConnectionProvider.createByComponent(getComponentName());
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new RemoteException(XavaResources.getString("error_obtaining_connection_provider"));
			}
		}
		return connectionProvider;		
	}	
	public void setConnectionProvider(IConnectionProvider provider) {
		this.connectionProvider = provider;
	}

	private Object[] hacerCalculos(String nombreModelo, Object[] fila, Collection calculadoresTab, Map indicesClave, List nombresPropiedades) throws XavaException {
		Object entidad = null;
		Iterator itCalculadores = calculadoresTab.iterator();
		while (itCalculadores.hasNext()) {
			TabCalculator calculadorTab = (TabCalculator) itCalculadores.next();
			try {
				PropertiesManager mpCalculador =
					calculadorTab.getPropertiesManager();
				MetaSetsContainer metaCalculador =
					calculadorTab.getMetaCalculator();	
				if (metaCalculador.containsMetaSets()) {
					Iterator itMetaPoners =
						metaCalculador.getMetaSetsWithoutValue().iterator();					
					int idx = calculadorTab.getPropertyName().indexOf('.');
					String ref = "";
					if (idx >= 0) {
						ref = calculadorTab.getPropertyName().substring(0, idx + 1);
					}
					while (itMetaPoners.hasNext()) {
						MetaSet metaPoner = (MetaSet) itMetaPoners.next();
						Object valor =
							getValor(ref + metaPoner.getPropertyNameFrom(), fila, nombresPropiedades);
						try {	
							mpCalculador.executeSet(
								metaPoner.getPropertyName(),
								valor);
						}
						catch (PropertiesManagerException ex) {
							throw new XavaException("calculator_property_not_found", metaPoner.getPropertyName(), valor.getClass().getName());
						}
					}
				}
				ICalculator calculador = calculadorTab.getCalculator();
				if (calculador instanceof IEntityCalculator) {
					if (entidad == null) entidad = getEntidad(nombreModelo, fila, indicesClave); 
					((IEntityCalculator) calculador).setEntity(entidad);
				}
				if (calculador instanceof IJDBCCalculator) {
					((IJDBCCalculator) calculador).setConnectionProvider(getConnectionProvider());
				}
				fila[calculadorTab.getIndex()] = calculador.calculate();
			}
			catch (Exception ex) {
				ex.printStackTrace();
				System.err.println(
					"ADVERTENCIA: Imposible calcular el valor de la propiedad "
						+ calculadorTab.getPropertyName()
						+ " desde un tab");
				fila[calculadorTab.getIndex()] = "ERROR";
			}
		}
		return fila;		
	}
	
	/**
	 * Devuelve la entidad correspondiente a la fila enviada. <p>
	 *
	 * @param fila  Fila íntegra con los datos tabulares.
	 * @param indicesClave Mapa con nombre e indice de las claves.
	 * @exception  FinderException  Si no se consigue localizar la fila.
	 * @exception  NullPointerException  Si <tt>fila == null</tt>.
	 */
	private Object getEntidad(String nombreModelo, Object[] fila, Map indicesClave)
		throws FinderException, XavaException, RemoteException {
		if (indicesClave == null) return null;
		Iterator it = indicesClave.entrySet().iterator();
		Map clave = new HashMap();		
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry) it.next();
			String nombrePropiedad = (String) e.getKey();
			int indice = ((Integer) e.getValue()).intValue();			
			clave.put(nombrePropiedad, fila[indice]);						
		}
		return MapFacade.findEntity(nombreModelo, clave);
	}
	
	
	private Object[] hacerConversiones(Object[] fila, Collection conversoresTab) throws XavaException {
		Iterator itConversores = conversoresTab.iterator();
		while (itConversores.hasNext()) {
			TabConverter conversorTab = (TabConverter) itConversores.next();
			try {				
				int idx = conversorTab.getIndex();
				if (conversorTab.tieneMultipleConverter()) { 
					IMultipleConverter conversor = conversorTab.getMultipleConverter();
					PropertiesManager mp = new PropertiesManager(conversor);					
					Iterator itCamposCmp = conversorTab.getCmpFields().iterator();
					while (itCamposCmp.hasNext()) {
						CmpField campo = (CmpField) itCamposCmp.next();
						Object valor = fila[conversorTab.getIndex(campo)]; 
						mp.executeSet(campo.getConverterPropertyName(), valor);					
					}										
					fila[idx] = conversor.toJava();										
				}
				else {
					IConverter conversor = conversorTab.getConverter();					
					fila[idx] = conversor.toJava(fila[idx]);					
				}
			}
			catch (Exception ex) {
				ex.printStackTrace();
				System.err.println(
					"ADVERTENCIA: Imposible convertir el valor de la propiedad "
						+ conversorTab.getPropertyName()
						+ " desde un tab");
				fila[conversorTab.getIndex()] = "ERROR";
			}
		}
		return fila;
	}

	

	private Object getValor(String nombrePropiedad, Object[] valores, List nombresPropiedades)
		throws XavaException {
		return valores[nombresPropiedades.indexOf(nombrePropiedad)];
	}
	
	public int getResultSize(ITabProvider tabProvider) {
		try {
			if (tabProvider instanceof JDBCTabProvider) {
				((JDBCTabProvider) tabProvider).setConnectionProvider(getConnectionProvider());
			}			
			return tabProvider.getResultSize();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("tab_result_size_error"));
		}
	}	
		
	public String getComponentName() {
		return componentName;
	}
	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}
	
}
