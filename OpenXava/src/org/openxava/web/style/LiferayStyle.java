package org.openxava.web.style;

/**
 * 
 * @author José Luis Santiago
 */ 

public class LiferayStyle extends Style {
	
	private static LiferayStyle instance = null;

	protected LiferayStyle() {		
	}
	
	public static Style getInstance() {
		if (instance == null) {
			instance = new LiferayStyle();
		}
		return instance;
	}
		
	public String getModule() {
		//return "portlet-font"; No, because usually link style is ugly, and not coherent with portal
		return ""; // The portal default style is used
	}
	
	public String getModuleSpacing() {
		return "border=0 cellpadding=0 cellspacing=2 width='100%'";
	}
		
	public String getButtonBar() {
		return "portlet-header-bar"; 
	}
	
	public String getButtonBarSpacing() {
		return "cellpadding=0 cellspacing=0";
	}	
	
	public String getButtonBarStart(boolean onBottom) {
		return "portlet-header-left";
	}
	
	public String getButtonBarMiddle(boolean onBottom) {
		return "";
	}
		
	public String getButtonBarEnd(boolean onBottom) {
		return "portlet-header-right";
	}
	
	
	public String getMode(boolean onButton) { 
		return "list-info";
	}
	
	public String getDetail() {
		return "";
	}
			
	public String getList() { 
		return "";
	}
	
	public String getListCellSpacing() {
		return "border=0 cellspacing=0 cellpadding=0";
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
		return "list-info";
	}
	
		
	public String getListTitle() {
		return "list-title";
	}
	
		
	public String getFrame() {
		return "frame";
	}
	
	public String getFrameTitle() { 
		return getFrame();
	}
	
	public String getFrameTitleLabel() {
		return getFrameTitle();
	}
	
	public String getFrameContent() {
		return getFrame();
	}
	
	public String getFrameSpacing() {
		return "";
	}
		
	public String getEditor() { 
		return "portlet-dlg-icon-label";
	}
	
	public String getLabel() { 
		return "portlet-dlg-icon-label";
	}
	
	public String getSmallLabel() {
		return "portlet-dlg-icon-label";
	}
	
	public String getErrors() { 
		return "portlet-msg-error";
	}
		

	public String getMessages() { 
		return "portlet-msg-success";
	}

	/**
	 * For messages and errors
	 */
	public String getMessagesWrapper() { 
		return "";
	}
	
	
	public String getProcessing() { 
		return "processing";
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
		return "";
	}
	
	public String getSectionActive() {
		return "";
	}	
	
	public String getSectionLink() {
		return null;
	}
	
	public String getSectionBarLeftDecoration() {
		return "";
	}
	
	/**
	 * If Middle and Right decoration is used
	 */
	public boolean sectionTabHasDecoration() {
		return false;
	}
	
	public String getSectionTabLeft() {
		return "layout-tab-selected";
	}
	
	public String getSectionTabMiddle() {
		return "layout-tab-selected";
	}

	public String getSectionTabRight() {
		return "layout-tab-selected";
	}
	
	public String getSectionTabLeftLow() {
		return "layout-tab";
	}

	public String getSectionTabMiddleLow() {
		return "layout-tab";
	}
	
	public String getSectionTabRightLow() {
		return "layout-tab";
	}
	
	public String getCollectionListActions() {
		return "";
	}
		
}
