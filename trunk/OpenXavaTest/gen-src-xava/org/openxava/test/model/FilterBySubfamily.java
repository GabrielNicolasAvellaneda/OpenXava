
// File generated by OpenXava: Sat Mar 12 14:13:33 CET 2005
// Archivo generado por OpenXava: Sat Mar 12 14:13:33 CET 2005

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: FilterBySubfamily		Entity/Entidad

package org.openxava.test.model;

import java.util.*;
import org.openxava.component.MetaComponent;
import org.openxava.model.meta.MetaModel;
import org.openxava.util.*;

/**
 * 
 * @author MCarmen Gimeno
 */
public class FilterBySubfamily implements java.io.Serializable {	
	
	// Properties/Propiedades 
	private String oid;
	public String getOid() {
		return oid;
	}
	public void setOid(String newOid) {
		this.oid = newOid;
	} 

	// References/Referencias 
	private Subfamily2 subfamily;
	public Subfamily2 getSubfamily() {
		return subfamily;
	}
	public void setSubfamily(Subfamily2 newSubfamily2) {
		this.subfamily = newSubfamily2;
	} 

	// Colecciones/Collections 
	
	private MetaModel metaModel;
	private MetaModel getMetaModel() throws XavaException {
		if (metaModel == null) {
			metaModel = MetaComponent.get("FilterBySubfamily").getMetaEntity(); 	
		}
		return metaModel;
	}
}