

// File generated by OpenXava: Tue Aug 23 12:25:46 CEST 2005
// Archivo generado por OpenXava: Tue Aug 23 12:25:46 CEST 2005

// WARNING: NO EDIT
// OJO: NO EDITAR

package org.openxava.test.ejb;

public class SubfamilyInfo implements java.io.Serializable {

	// Attributes/Atributos 
	private org.openxava.test.ejb.Family family; 
	private org.openxava.test.ejb.Subfamily subfamily;



	// Properties/Propiedades

	// References 
	public org.openxava.test.ejb.Family getFamily() {
		return family;
	}
	public void setFamily(org.openxava.test.ejb.Family newFamily) {
		this.family = newFamily;
	} 
	public org.openxava.test.ejb.Subfamily getSubfamily() {
		return subfamily;
	}
	public void setSubfamily(org.openxava.test.ejb.Subfamily newSubfamily) {
		this.subfamily = newSubfamily;
	}
	

}