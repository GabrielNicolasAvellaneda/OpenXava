package org.openxava.web.style;

/**
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
	
	public String getModuleSpacing() {
		return "cellpadding=2 cellspacing=2 border=0 width='100%'";
	}
		
	public String getButtonBar() {
		return "button-bar";
	}
	
	public String getButtonBar2() {
		return "";
	}
		
	public String getButtonBarSpacing() {
		return "";
	}	
	
	public String getButtonBarStart(boolean onBottom) {
		return "";
	}
	
	public String getButtonBarMiddle(boolean onBottom) {
		return "";
	}
		
	public String getButtonBarEnd(boolean onBottom) {
		return "";
	}
	
	
	public String getMode(boolean onButton) { 
		return "mode";
	}
	
	public String getDetail() {
		return "";
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
	
	public String getListInfoDetail() {
		return getListInfo();
	}
		
	public String getListTitle() {
		return "list-title";
	}
	
	public String getListTitleWrapper() {
		return "";
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
	
	public String getFrameTitleStartDecoration() {
		return "";
	}
	
	public String getFrameTitleEndDecoration() {
		return "";
	}
		
	public String getFrameContent() {
		return getFrame();
	}
	
	public String getFrameSpacing() {
		return "";
	}
		
	public String getEditor() { 
		return "editor";
	}
	
	public String getLabel() { 
		return "portlet-form-field-label";
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
		return "Jetspeed";
	}
	
	public String getSectionTableAttributes() {
		return "border='0' cellpadding='0' cellspacing='0'";
	}
	
	public String getSectionActive() {
		return "activeSection";
	}	
	
	public String getSectionLink() {
		return null;
	}
	
	public String getSectionBarStartDecoration() {
		return "";
	}
	
	public String getSectionBarEndDecoration() {
		return "";
	}
			
	public String getActiveSectionTabStartDecoration() {
		return "<td class='TabLeft' nowrap='true'>&nbsp;</td><td class='TabMiddle' style='vertical-align: middle; text-align: center;' nowrap='true'>";
	}
	
	public String getActiveSectionTabEndDecoration() {
		return "</td><td class='TabRight' nowrap='true'>&nbsp;</td>";		
	}
	
	public String getSectionTabStartDecoration() {
		return "<td class='TabLeftLow' nowrap='true'>&nbsp;</td><td class='TabMiddleLow' style='vertical-align: middle; text-align: center;' nowrap='true'>";
	}
	
	public String getSectionTabEndDecoration() {
		return "</td><td class='TabRightLow' nowrap='true'>&nbsp;</td>";		
	}		
		
	public String getCollectionListActions() {
		return "";
	}
	
	public String getRestoreImage() {
		return "images/restore.gif";
	}
	
	public String getMaximizeImage() {
		return "images/maximize.gif";
	}
	
	public String getRemoveImage() {
		return "images/remove.gif";
	}
	
	public String getActionsInFrame() {
		return "";
	}
	
	/**
	 * If <code>true</code< the header in list is aligned as data displayed in its column. <p>
	 * 
	 * By default is <code>false</code> and it's used the portal default alignament for headers.
	 */
	public boolean isAlignHeaderAsData() {
		return false;
	}
	
	public String getBottomButtonsStyle() {
		return "";
	}
	
}
