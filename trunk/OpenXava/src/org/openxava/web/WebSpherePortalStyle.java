package org.openxava.web;

public class WebSpherePortalStyle extends Style {
	
	private static WebSpherePortalStyle instance = null;

	protected WebSpherePortalStyle() {		
	}
	
	public static Style getInstance() {
		if (instance == null) {
			instance = new WebSpherePortalStyle();
		}
		return instance;
	}
	
	public String getButtonBar() {
		return "";
	}
	
	public String getList() { 
		return "wpsTable";
	}
	
	public String getListCellSpacing() {
		return "border=0 cellspacing=0 cellpadding=0";
	}
	
	public String getListHeader() { 
		return "wpsPagingTable"; // tmp wpsPagingTableHeader";
	}
	
	public String getListSubheader() {
		return "wpsTableHead";
	}
	
	public String getListPair() { 
		return "wpsTableNrmRow";
	}
	
	public String getListOdd() { 
		return "wpsTableNrmRow";
	}
	
	public String getListPairSelected() { 
		return "portlet-table-selected";
	}
	
	public String getListOddSelected() { 
		return "portlet-table-selected";
	}
				
	public String getListInfo() {	
		return "";
	}
	
	public String getListTitle() {
		return "websphere-list-title";
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
				
	public String getMode() {	
		return "";
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
