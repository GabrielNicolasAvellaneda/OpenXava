package org.openxava.actions;

import org.openxava.calculators.*;
import org.openxava.session.*;
import org.openxava.util.*;

/**
 * 
 * @author Javier Paniza
 */

public class EditGalleryAction extends ViewBaseAction implements INavigationAction {
	
	private String galleryProperty;
	private Gallery gallery;	

	public void execute() throws Exception {
		String oid = getView().getValueString(galleryProperty);
		if (Is.emptyString(oid)) {
			UUIDCalculator cal = new UUIDCalculator();  
			oid = (String) cal.calculate();
			getView().setValue(galleryProperty, oid);
		}
		gallery.setOid(oid);
		gallery.loadAllImages();
		gallery.setTitle(XavaResources.getString("gallery_title", 
				Labels.get(galleryProperty, Locales.getCurrent()), 
				Labels.get(getModelName(), Locales.getCurrent())));				
		if (gallery.isEmpty()) {
			addMessage("no_images");
		}
		gallery.setReadOnly(!isEditable());
	}

	private boolean isEditable() throws XavaException {
		return getView().isEditable(galleryProperty);		
	}

	public String getGalleryProperty() {
		return galleryProperty;
	}

	public void setGalleryProperty(String galleryOID) {
		this.galleryProperty = galleryOID;
	}

	public String[] getNextControllers() throws Exception {
		return isEditable()?new String [] { "Gallery" }:new String [] { "Return" };
	}

	public String getCustomView() throws Exception {
		return "xava/editors/gallery";
	}

	public Gallery getGallery() {
		return gallery;
	}

	public void setGallery(Gallery gallery) {
		this.gallery = gallery;
	}

}
