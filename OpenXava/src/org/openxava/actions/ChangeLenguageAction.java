package org.openxava.actions;

import org.openxava.session.*;


/**
 * @author Javier Paniza
 */

public class ChangeLenguageAction extends BaseAction {
	
	private String idLanguage;
	private Language language;

	public void execute() throws Exception {
		language.setId(idLanguage);
	}

	public String getIdLanguage() {
		return idLanguage;
	}

	public Language getLanguage() {
		return language;
	}

	public void setIdLanguage(String string) {		
		idLanguage = string;		
	}

	public void setLanguage(Language idioma) {
		this.language = idioma;
	}

}
