
// File generated by OpenXava: Tue May 10 17:43:39 CEST 2005
// Archivo generado por OpenXava: Tue May 10 17:43:39 CEST 2005

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: Customer		Entity/Entidad

package org.openxava.test.model;

import java.util.*;
import org.openxava.component.MetaComponent;
import org.openxava.model.meta.MetaModel;
import org.openxava.util.*;

/**
 * 
 * @author MCarmen Gimeno
 */
public class Customer implements java.io.Serializable {	
	
	// Properties/Propiedades 
	private org.openxava.converters.IntegerNumberConverter typeConverter;
	private org.openxava.converters.IntegerNumberConverter getTypeConverter() {
		if (typeConverter == null) {
			try {
				typeConverter = (org.openxava.converters.IntegerNumberConverter) 
					getMetaModel().getMapping().getConverter("type");
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new RuntimeException(XavaResources.getString("generator.create_converter_error", "type"));
			}
			
		}	
		return typeConverter;
	} 
	private java.lang.Integer _type;
	private java.lang.Integer get_Type() {
		return _type;
	}
	private void set_Type(java.lang.Integer newType) {
		this._type = newType;
	} 	
	
	/**
	 * 
	 * 
	 */
	public int getType() {
		try {
			return ((Integer) getTypeConverter().toJava(get_Type())).intValue();
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new RuntimeException(XavaResources.getString("generator.conversion_error", "Type", "Customer", "int"));
		}
	}
	
	/**
	 * 
	 */
	public void setType(int newType) {
		try { 
			set_Type((java.lang.Integer) getTypeConverter().toDB(new Integer(newType)));
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new RuntimeException(XavaResources.getString("generator.conversion_error", "Type", "Customer", "int"));
		}		
	} 
	private org.openxava.converters.TrimStringConverter remarksConverter;
	private org.openxava.converters.TrimStringConverter getRemarksConverter() {
		if (remarksConverter == null) {
			try {
				remarksConverter = (org.openxava.converters.TrimStringConverter) 
					getMetaModel().getMapping().getConverter("remarks");
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new RuntimeException(XavaResources.getString("generator.create_converter_error", "remarks"));
			}
			
		}	
		return remarksConverter;
	} 
	private java.lang.String _remarks;
	private java.lang.String get_Remarks() {
		return _remarks;
	}
	private void set_Remarks(java.lang.String newRemarks) {
		this._remarks = newRemarks;
	} 	
	
	/**
	 * 
	 * 
	 */
	public java.lang.String getRemarks() {
		try {
			return (java.lang.String) getRemarksConverter().toJava(get_Remarks());
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new RuntimeException(XavaResources.getString("generator.conversion_error", "Remarks", "Customer", "java.lang.String"));
		}
	}
	
	/**
	 * 
	 */
	public void setRemarks(java.lang.String newRemarks) {
		try { 
			set_Remarks((java.lang.String) getRemarksConverter().toDB(newRemarks));
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new RuntimeException(XavaResources.getString("generator.conversion_error", "Remarks", "Customer", "java.lang.String"));
		}		
	} 
	private org.openxava.converters.TrimStringConverter relationWithSellerConverter;
	private org.openxava.converters.TrimStringConverter getRelationWithSellerConverter() {
		if (relationWithSellerConverter == null) {
			try {
				relationWithSellerConverter = (org.openxava.converters.TrimStringConverter) 
					getMetaModel().getMapping().getConverter("relationWithSeller");
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new RuntimeException(XavaResources.getString("generator.create_converter_error", "relationWithSeller"));
			}
			
		}	
		return relationWithSellerConverter;
	} 
	private java.lang.String _relationWithSeller;
	private java.lang.String get_RelationWithSeller() {
		return _relationWithSeller;
	}
	private void set_RelationWithSeller(java.lang.String newRelationWithSeller) {
		this._relationWithSeller = newRelationWithSeller;
	} 	
	
	/**
	 * 
	 * 
	 */
	public String getRelationWithSeller() {
		try {
			return (String) getRelationWithSellerConverter().toJava(get_RelationWithSeller());
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new RuntimeException(XavaResources.getString("generator.conversion_error", "RelationWithSeller", "Customer", "String"));
		}
	}
	
	/**
	 * 
	 */
	public void setRelationWithSeller(String newRelationWithSeller) {
		try { 
			set_RelationWithSeller((java.lang.String) getRelationWithSellerConverter().toDB(newRelationWithSeller));
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new RuntimeException(XavaResources.getString("generator.conversion_error", "RelationWithSeller", "Customer", "String"));
		}		
	} 
	private org.openxava.converters.NoConversionConverter photoConverter;
	private org.openxava.converters.NoConversionConverter getPhotoConverter() {
		if (photoConverter == null) {
			try {
				photoConverter = (org.openxava.converters.NoConversionConverter) 
					getMetaModel().getMapping().getConverter("photo");
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new RuntimeException(XavaResources.getString("generator.create_converter_error", "photo"));
			}
			
		}	
		return photoConverter;
	} 
	private byte [] _photo;
	private byte [] get_Photo() {
		return _photo;
	}
	private void set_Photo(byte [] newPhoto) {
		this._photo = newPhoto;
	} 	
	
	/**
	 * 
	 * 
	 */
	public byte[] getPhoto() {
		try {
			return (byte[]) getPhotoConverter().toJava(get_Photo());
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new RuntimeException(XavaResources.getString("generator.conversion_error", "Photo", "Customer", "byte[]"));
		}
	}
	
	/**
	 * 
	 */
	public void setPhoto(byte[] newPhoto) {
		try { 
			set_Photo((byte []) getPhotoConverter().toDB(newPhoto));
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new RuntimeException(XavaResources.getString("generator.conversion_error", "Photo", "Customer", "byte[]"));
		}		
	} 
	private org.openxava.converters.TrimStringConverter nameConverter;
	private org.openxava.converters.TrimStringConverter getNameConverter() {
		if (nameConverter == null) {
			try {
				nameConverter = (org.openxava.converters.TrimStringConverter) 
					getMetaModel().getMapping().getConverter("name");
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new RuntimeException(XavaResources.getString("generator.create_converter_error", "name"));
			}
			
		}	
		return nameConverter;
	} 
	private java.lang.String _name;
	private java.lang.String get_Name() {
		return _name;
	}
	private void set_Name(java.lang.String newName) {
		this._name = newName;
	} 	
	
	/**
	 * 
	 * 
	 */
	public String getName() {
		try {
			return (String) getNameConverter().toJava(get_Name());
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new RuntimeException(XavaResources.getString("generator.conversion_error", "Name", "Customer", "String"));
		}
	}
	
	/**
	 * 
	 */
	public void setName(String newName) {
		try { 
			set_Name((java.lang.String) getNameConverter().toDB(newName));
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new RuntimeException(XavaResources.getString("generator.conversion_error", "Name", "Customer", "String"));
		}		
	} 
	private int number;
	public int getNumber() {
		return number;
	}
	public void setNumber(int newNumber) {
		this.number = newNumber;
	} 

	// References/Referencias 
	private Seller seller;
	public Seller getSeller() {
		return seller;
	}
	public void setSeller(Seller newSeller) {
		this.seller = newSeller;
	} 
	private Seller alternateSeller;
	public Seller getAlternateSeller() {
		return alternateSeller;
	}
	public void setAlternateSeller(Seller newSeller) {
		this.alternateSeller = newSeller;
	} 

	// Colecciones/Collections 
	private java.util.Collection deliveryPlaces;
	public java.util.Collection getDeliveryPlaces() {
		return deliveryPlaces;
	} 
	
	private MetaModel metaModel;
	private MetaModel getMetaModel() throws XavaException {
		if (metaModel == null) {
			metaModel = MetaComponent.get("Customer").getMetaEntity(); 	
		}
		return metaModel;
	}
}