package org.openxava.session;

import java.util.*;

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
	
	/**
	 * Identificador del idioma, p. ej: es, ca, en, etc.
	 * @return
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
				System.err.println("¡ADVERTENCIA! Imposible crear acción para cambiar a castellano, devolvemos acción para cambiar a valenciano");
				return  getChangeToValencianoAction();
			}
			return  getChangeToValencianoAction();
		}
		catch (XavaException ex) {
			System.err.println("¡ADVERTENCIA! Imposible crear acciones para cambiar de idioma, se devuelve acción vacía");
			return new MetaAction();
		}
	}

	private static MetaAction getChangeToValencianoAction() throws XavaException {
		if (changeToValencianoAction == null) {
			changeToValencianoAction = MetaControllers.getMetaAction("Idiomas.cambiarAValenciano");
		}
		return changeToValencianoAction;
	}
	
	private static MetaAction getChangeToSpainAction() throws XavaException {
		if (changeToSpainAction == null) {
			changeToSpainAction = MetaControllers.getMetaAction("Idiomas.cambiarACastellano");
		}
		return changeToSpainAction;
	}
	
	public Locale getDefaultLocale() {
		return defaultLocale;
	}

	public void setDefaultLocale(Locale nuevo) {
		if (Is.equal(defaultLocale, nuevo)) return;
		defaultLocale = nuevo;
		setLocale(defaultLocale);
	}

}
