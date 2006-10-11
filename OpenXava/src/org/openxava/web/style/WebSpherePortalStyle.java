package org.openxava.web.style;

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
	
	public String getModuleSpacing() {
		return "cellpadding=0 cellspacing=0 border=0 width='100%'";
	}	
	
	public String getDetail() {
		return "wpsTable";
	}
		
	public String getList() { 
		return "wpsTable";
	}
	
	public String getListCellSpacing() {
		return "border=0 cellspacing=0 cellpadding=0";
	}
	
	public String getListHeader() {  
		return "wpsTableHead";
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
		return "wpsTable";
	}
	
	public String getListInfoDetail() {
		return "";
	}
	
	public String getListTitle() {
		return "websphere-list-title"; 
	}	
	
	public String getListTitleWrapper() {
		return "wpsTable";
	}	
	
	public String getFrame() {
		return "";
	}
	
	public String getFrameTitle() {
		return "wpsPortletTitle";
	}
	

	public String getFrameTitleLabel() {
		return "websphere-frame-title-label";
	}
	
	public String getFrameContent() {
		return "wpsPortletBorder";
	}
	
	public String getFrameSpacing() {
		return "border=0 cellspacing=0 cellpadding=2";
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
	
	public String getMessagesWrapper() { 
		return "wpsTable";
	}	
				
	public String getMode(boolean onBottom) {	
		return onBottom?"wpsPagingTableFooterMiddle":"wpsPagingTableHeaderMiddle";
	}
	
	public String getButtonBar() {
		return "";
	}
	
	public String getButtonBarSpacing() {
		return "border=0 cellspacing=0 cellpadding=0";
	}		
	
	public String getButtonBarStart(boolean onBottom) {
		return onBottom?"wpsPagingTableFooterStart":"wpsPagingTableHeaderStart";
	}
	
	public String getButtonBarMiddle(boolean onBottom) {
		return onBottom?"wpsPagingTableFooterMiddle":"wpsPagingTableHeaderMiddle";
	}	
	
	public String getButtonBarEnd(boolean onBottom) {
		return onBottom?"wpsPagingTableFooterEnd":"wpsPagingTableHeaderEnd";
	}
	
	
	public String getAscendingImage() {
		return "ascending-white.gif";
	}
	
	public String getDescendingImage() {
		return "descending-white.gif";
	}
	
	public String getSection() {
		return "wpsPageBar";
	}
	
	public String getSectionActive() {
		return "";
	}	
	
	public String getSectionLink() {
		return "wpsUnSelectedPageLink";
	}
	
	public String getSectionBarLeftDecoration() {
		return "<td class='wpsPageBar3dShadow'><img width='10' height='16' border='0' src='/wps/images/dot.gif' alt=''></td>";
	}
	
	
	public boolean sectionTabHasDecoration() {
		return false;
	}
	
	public String getSectionTabLeft() {
		return "wpsSelectedPage";
	}
	
	public String getSectionTabMiddle() {
		return "wpsSelectedPage"; 
	}

	public String getSectionTabRight() {
		return "wpsSelectedPage";
	}
	
	public String getSectionTabLeftLow() {
		return "wpsUnSelectedPage";
	}

	public String getSectionTabMiddleLow() {
		return "wpsUnSelectedPage";
	}
	
	public String getSectionTabRightLow() {
		return "wpsUnSelectedPage";
	}	
	
	public String getCollectionListActions() {
		return "wpsTableNrmRow";
	}
	
	public String getRestoreImage() {
		return "/wps/themes/html/title_alt_restore.gif";
	}
		
	public String getMaximizeImage() {
		return "/wps/themes/html/title_alt_maximize.gif";
	}
	
	public String getRemoveImage() {
		return "/wps/images/icons/Delete.gif";
	}
	
	public String getActionsInFrame() {
		return "wpsPortletTitleIconBackground";
	}
	
	public boolean isAlignHeaderAsData() {
		return true;
	}
	
	public String getBottomButtonsStyle() {
		return "style='padding-top: 4px'";
	}

}
