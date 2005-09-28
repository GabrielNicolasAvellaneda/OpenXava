package org.openxava.web.style;

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
		
	
	public String getList() { 
		return "";
	}
	
	public String getListHeader() { 
		return "portlet-section-header";
	}
	
	public String getListSubheader() {
		return "portlet-section-subheader";
	}
	
	public String getListPair() { 
		return "portlet-section-body";
	}
	
	public String getListOdd() { 
		return "portlet-section-alternate";
	}
	
	public String getListPairSelected() { 
		return "portlet-section-selected";
	}
	
	public String getListOddSelected() { 
		return "portlet-section-selected";
	}
				
	public String getListInfo() {	
		return "jetspeed2-list-info";
	}
	
	public String getListTitle() {
		return "PTitleContent";
	}	
	
	public String getFrame() {
		return "frame";
	}
	
	public String getFrameTitle() {
		return "PTitle";
	}
	
	public String getFrameTitleLabel() {
		return "PTitleContent";
	}
	
	public String getFrameContent() {
		return "PContent";
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
				
	public String getMode(boolean onBottom) {	
		return "jetspeed2-mode";
	}
	
	public String getAscendingImage() {
		return "ascending-white.gif";
	}
	
	public String getDescendingImage() {
		return "descending-white.gif";
	}
	
	public String getSection() {
		return "tabs";
	}
	
	public String getSectionActive() {
		return "";
	}	
	
	public String getSectionTabLeft() {
		return "LTabLeft";
	}
	
	public String getSectionTabMiddle() {
		return "LTab"; 
	}

	public String getSectionTabRight() {
		return "LTabRight";
	}
	
	public String getSectionTabLeftLow() {
		return "LTabLeftLow";
	}

	public String getSectionTabMiddleLow() {
		return "LTabLow";
	}
	
	public String getSectionTabRightLow() {
		return "LTabRightLow";
	}	
		
}
