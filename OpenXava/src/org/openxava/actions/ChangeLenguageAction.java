package org.openxava.actions;

import org.openxava.session.*;

/**
 * @author Javier Paniza
 */

public class ChangeLenguageAction extends BaseAction {
	
	private String languageId;
	private Language language;

	public void execute() throws Exception {
		language.setId(languageId);
	}

	public String getLanguageId() {
		return languageId;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguageId(String string) {		
		languageId = string;		
	}

	public void setLanguage(Language idioma) {
		this.language = idioma;
	}

}
