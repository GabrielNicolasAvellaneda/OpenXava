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
	
	/**
	 * Constructor for Coleccion
	 */
	public MetaCollection() {
		super();
	}
	
	public void addMetaCalculatorPostRemove(MetaCalculator metaCalculador) {
		if (metaCalculatorsPostRemove == null) {
			metaCalculatorsPostRemove = new ArrayList();			
		}
		metaCalculatorsPostRemove.add(metaCalculador);		
	}
	
	public Collection getMetaCalculatorsPostRemove() {
		return metaCalculatorsPostRemove == null?Collections.EMPTY_LIST:metaCalculatorsPostRemove;
	}
	
	public boolean hasPostRemoveCalculators() {
		return !getMetaCalculatorsPostRemove().isEmpty();
	}

	/**
	 * Gets the minimo
	 * @return Returns a int
	 */
	public int getMinimum() {
		return minimum;
	}
	/**
	 * Sets the minimo
	 * @param minimo The minimo to set
	 */
	public void setMinimum(int minimo) {
		this.minimum = minimo;
	}


	/**
	 * Gets the referencia
	 * @return Returns a Referencia
	 */
	public MetaReference getMetaReference() {
		return metaReference;
	}
	/**
	 * Sets the referencia
	 * @param referencia The referencia to set
	 */
	public void setMetaReference(MetaReference referencia) {
		this.metaReference = referencia;
		if (getMetaModel() != null && this.metaReference != null) {
			this.metaReference.setMetaModel(getMetaModel());
		}
	}

	public boolean isAggregate() throws XavaException {
		return getMetaReference().isAggregate();
	}

	/**
	 * @see MetaMember#setMetaModel(MetaModelo)
	 */
	public void setMetaModel(MetaModel newContenedor) {
		super.setMetaModel(newContenedor);
		if (metaReference != null) {
			metaReference.setMetaModel(newContenedor);
		}
	}
	/**
	 * @param objeto Ha de ser de tipo Collection, si es nulo se toma como cadena vacía 
	 */
	public void validate(
		Messages errores,
		Object objeto,
		String nombreObjeto,
		String nombrePropiedad)
		throws RemoteException {			
		objeto = (objeto == null)?Collections.EMPTY_SET:objeto;	
		if (!(objeto instanceof Collection)) {
			throw new IllegalArgumentException(XavaResources.getString("only_validate_collection"));
		}	
		Collection c = (Collection) objeto;
		if (c.size() < getMinimum()) {
			String idElementos = getMinimum() == 1?"element":"elements";
			errores.add("minimum_elements", new Integer(getMinimum()), idElementos, getName());
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
		MetaModel metaModelo = getMetaReference().getMetaModelReferenced(); 
		StringBuffer sb = new StringBuffer("SELECT OBJECT(o) FROM ");
		sb.append(metaModelo.getName());
		sb.append(" o");
		if (!Is.emptyString(this.condition)) {
			sb.append(" WHERE ");			
			String condicion = changePropertiesThisByArguments(getCondition(), true); 			
			sb.append(metaModelo.getMapping().changePropertiesByCMPAttributes(condicion));
		}
		return sb.toString();
	}
	
	public String getJBossQLCondition() throws XavaException {
		if (Is.emptyString(this.order)) return getEJBQLCondition();
		StringBuffer sb = new StringBuffer(getEJBQLCondition());
		sb.append(" ORDER BY ");
		sb.append(getMetaReference().getMetaModelReferenced().getMapping().changePropertiesByCMPAttributes(this.order));
		return sb.toString();
	}
		
	private String changePropertiesThisByArguments(String origen, boolean ejbql) throws XavaException {			
		StringBuffer r = new StringBuffer(origen);
		Collection propiedades = new ArrayList();
		int i = r.toString().indexOf("${this.");
		int f = 0;			
		int c = 0;
		while (i >= 0) {
			f = r.toString().indexOf("}", i+2);
			if (f < 0) break;
			String propiedad = r.substring(i+2, f);
			String argumento = ejbql?"?" + (++c):"{" + (c++) + "}"; 
			r.replace(i, f+1, argumento);
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
	 * Util para generar código EJB.	 
	 */
	public String getFinderName() {
		return Strings.firstUpper(getName()) + "De" + getMetaModel().getName(); 
	}

	/**
	 * Util para generar código EJB.	 
	 */
	public String getFinderArguments() throws XavaException {
		StringBuffer argumentos = new StringBuffer();
		Iterator it = getMetaPropertiesFinderArguments().iterator();
		while (it.hasNext()) {
			MetaProperty pr = (MetaProperty) it.next();
			argumentos.append(pr.getType().getName());
			argumentos.append(' ');
			argumentos.append(pr.getName());
			if (it.hasNext()) {
				argumentos.append(", ");		 	
			}
		}
		return argumentos.toString();
	}

	/**
	 * Util para generar código EJB.	 
	 */	
	public Collection getMetaPropertiesFinderArguments() throws XavaException {
		if (metaPropertiesFinderArguments == null) {
			metaPropertiesFinderArguments = new ArrayList();
			String condicion = getCondition();
			int i = condicion.indexOf("${this.");
			int f = 0;			
			while (i >= 0) {
				f = condicion.indexOf("}", i+2);
				if (f < 0) break;
				String nombrePropiedad = condicion.substring(i+7, f);
				MetaProperty original = getMetaModel().getMetaProperty(nombrePropiedad);
				MetaProperty pr = new MetaProperty();
				boolean estaCalificada = nombrePropiedad.indexOf('.') >= 0;
				if (estaCalificada) {
					pr.setName(Strings.change(nombrePropiedad, ".", "_"));
				}
				else {
					pr.setName(nombrePropiedad);				
				}
				if (original.getMapping().hasConverter()) {
					if (!estaCalificada) {
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
				i = condicion.indexOf("${this.",f);
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

