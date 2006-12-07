package org.openxava.session;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.controller.meta.*;
import org.openxava.util.*;


/**
 * @author Javier Paniza
 */

public class Language {

	private static MetaAction changeToSpainAction;
	private static MetaAction changeToValencianoAction;
	private String id;
	private Locale locale; 
	private Locale defaultLocale;
	
	private static Log log = LogFactory.getLog(Language.class);
	
	/**
	 * Language identifier, for example: es, ca, en, etc.
	 */
	public String getId() {		
		return id;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setId(String string) {
		id = string;		
		this.locale = new Locale(id, "");
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
		this.id = locale.getLanguage();		
	}
	
	public String toString() {		
		return id;
	}
	
	public MetaAction getChangeAction() {
		try {
			try {
				if ("ca".equals(this.id)) return getChangeToSpainAction();
			}
			catch (XavaException ex) {
				log.warn(XavaResources.getString("to_spanish_warning"),ex);
				return  getChangeToValencianoAction();
			}
			return  getChangeToValencianoAction();
		}
		catch (XavaException ex) {
			log.warn(XavaResources.getString("change_language_create_action_warning"),ex);
			return new MetaAction();
		}
	}

	private static MetaAction getChangeToValencianoAction() throws XavaException {
		if (changeToValencianoAction == null) {
			changeToValencianoAction = MetaControllers.getMetaAction("Languages.changeToValenciano");
		}
		return changeToValencianoAction;
	}
	
	private static MetaAction getChangeToSpainAction() throws XavaException {
		if (changeToSpainAction == null) {
			changeToSpainAction = MetaControllers.getMetaAction("Languages.changeToSpanish");
		}
		return changeToSpainAction;
	}
	
	public Locale getDefaultLocale() {
		return defaultLocale;
	}

	public void setDefaultLocale(Locale newLocale) {
		if (Is.equal(defaultLocale, newLocale)) return;
		defaultLocale = newLocale;
		setLocale(defaultLocale);
	}

}
