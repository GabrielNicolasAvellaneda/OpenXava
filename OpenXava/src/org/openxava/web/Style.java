package org.openxava.web;

/**
 * tmo: ¿Mover a paquete styles?
 * tmp: Doc
 * Falta probar:				JS2		WPS		Nada	JS1
 * - Editores/Etiquetas			X
 * - Mensajes					X
 * - Títulos grupos
 * - Botones
 * - highlight para filas
 * - Carpetas
 * - Listas 
 * - ¿Grupos?
 *  
 * Faltaría también:
 * - Borra jetspeed.css
 * 
 * @author Javier Paniza
 */ 

public class Style {
	
	private static Style instance = null;

	public Style() {		
	}
	
	public static Style getInstance() {
		if (instance == null) {
			instance = new Style();
		}
		return instance;
	}	
	
	public String getModule() {
		return "portlet-font";
	}
	
	public String getButtonBar() {
		return "button-bar";
	}
	
	public String getList() { // tmp ¿sobreescribir?
		return "list";
	}
	
	public String getPair() { // tmp ¿sobreescribir?
		return "pair";
	}
	
	public String getOdd() { // tmp ¿sobreescribir?
		return "odd";
	}
	
	public String getFrame() {
		return "frame";
	}
	
	public String getEditor() { // tmp ¿sobreescribir?
		return "editor";
	}
	
	public String getLabel() { // tmp ¿sobreescribir?
		return "label";
	}
	
	public String getSmallLabel() {
		return "small-label";
	}
	
	public String getErrors() { // tmp ¿sobreescribir?
		return "errors";
	}

	public String getMessages() { // tmp ¿sobreescribir?
		return "messages";
	}
	
	public String getProcessing() { 
		return "processing";
	}
			
	public String getSearch() { // tmp ¿sobreescribir?
		return "search";
	}
	
	public String getListInfo() {
		return "list-info";
	}
}
