package org.openxava.web;

/**
 * tmp: ¿Mover a paquete styles?
 * tmp: 
 * Falta probar:					JS2		WPS	
 * - Editores/Etiquetas				X		X
 * - Mensajes(takes-long)			X		X
 * - Vinculos modo					X		X
 * - Información lista				X		X
 * - Título lista					X		X
 * - Botones						X		X
 * - Botonera						X		X
 * - Marcos(grup, col, ref, edit)	X  
 * - Listas(highlight)				X 						 		
 * - Carpetas 						X
 *  
 * Faltaría también:
 * - Borrar jetspeed.css
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
	
	public String getList() {  
		return "list";
	}
	
	public String getListCellSpacing() {
		return ""; 
	}
	
	public String getListHeader() { 
		return "list";
	}
	
	public String getListSubheader() {
		return "list-subheader";
	}	
	
	public String getListPair() { 
		return "list-pair";
	}
	
	public String getListOdd() { 
		return "list-odd";
	}
	
	public String getListPairSelected() { 
		return "list-pair-selected";
	}
	
	public String getListOddSelected() { 
		return "list-odd-selected";
	}
		
	public String getListInfo() {
		return "list-info";
	}
	
	public String getListTitle() {
		return "list-title";
	}
	
	public String getFrame() {
		return "frame";
	}
	
	public String getFrameTitle() { 
		return "frame";
	}
	
	public String getFrameTitleLabel() {
		return "frame";
	}
	
	public String getFrameContent() {
		return "frame";
	}
	
	
	public String getEditor() { 
		return "editor";
	}
	
	public String getLabel() { 
		return "label";
	}
	
	public String getSmallLabel() {
		return "small-label";
	}
	
	public String getErrors() { 
		return "errors";
	}

	public String getMessages() { 
		return "messages";
	}
	
	public String getProcessing() { 
		return "processing";
	}
			
	public String getMode() { 
		return "mode";
	}
		
	public String getButton() {
		return "portlet-form-button";
	}
	
	public String getAscendingImage() {
		return "ascending.gif";
	}
	
	public String getDescendingImage() {
		return "descending.gif";
	}
	
	public String getSection() {
		return "Jetspeed";
	}
	
	public String getSectionActive() {
		return "activeSection";
	}	
	
	public String getSectionTabLeft() {
		return "TabLeft";
	}
	
	public String getSectionTabMiddle() {
		return "TabMiddle";
	}

	public String getSectionTabRight() {
		return "TabRight";
	}
	
	public String getSectionTabLeftLow() {
		return "TabLeftLow";
	}

	public String getSectionTabMiddleLow() {
		return "TabMiddleLow";
	}
	
	public String getSectionTabRightLow() {
		return "TabRightLow";
	}	
	
}
