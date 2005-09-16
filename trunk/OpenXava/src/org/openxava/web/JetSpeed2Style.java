package org.openxava.web;

public class JetSpeed2Style extends Style {
	
	private static JetSpeed2Style instance = null;

	protected JetSpeed2Style() {		
	}
	
	public static Style getInstance() {
		if (instance == null) {
			instance = new JetSpeed2Style();
		}
		return instance;
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
	
	public String getEditor() { 
		return "portlet-form-input-field";
	}
	
	public String getLabel() { 
		return "portlet-form-field-label";
	}
	
	public String getSmallLabel() {
		return "small-label";
	}
	
	public String getErrors() { 
		return "portlet-msg-error";
	}

	public String getMessages() { 
		return "portlet-msg-success"; 
	}
				
	public String getSearch() { // tmp ¿sobreescribir?
		return "search";
	}
	
}
