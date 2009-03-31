package org.openxava.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created on 04/02/2009 (16:23:01)
 * @autor Ana Andres
 */
public class DeleteImageAction extends ViewBaseAction{
	private static Log log = LogFactory.getLog(DeleteImageAction.class);
	
	private String newImageProperty;
	
	public void execute() throws Exception {
		log.info("[DeleteImageAction.execute] propiedad:" + getNewImageProperty().replace("xava." + getModelName() + ".", "")); // tmp
		log.info("[DeleteImageAction.execute] despues del cambio"); // tmp
//		getView().setValue(getNewImageProperty().replace("xava." + getModelName() + ".", ""), null);
		getView().setValue(getNewImageProperty(), null);
	}

	public String getNewImageProperty() {
		return newImageProperty;
	}

	public void setNewImageProperty(String newImageProperty) {
		this.newImageProperty = newImageProperty;
	}

}
