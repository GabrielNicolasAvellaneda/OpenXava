package org.openxava.web.style;

/**
 * For Liferay 4.3. <p>
 * 
 * @author Javier Paniza
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
		return ""; 		
	}
	
	public String getModuleSpacing() {
		return "border=0 cellpadding=0 cellspacing=2 width='100%'";
	}
		
	public String getButtonBar() {
		return ""; 
	}
	
	public String getButtonBar2() {
		return "portlet-section-header"; 
	}
	
	public String getButtonBarSpacing() {
		return "cellpadding=0 cellspacing=0";		
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
	
	public String getButtonBarStyle() {
		return "style='position: static; margin-bottom: 4px'"; 		
	}
	
	public String getButtonBarStartStyle() {
		return "style='position: static'";
	}
	
	public String getButtonBarMiddleStyle() {
		return "style='position: static'";
	}
	
	public String getButtonBarEndStyle() {
		return "style='position: static'";
	}

		
	public String getMode(boolean onButton) { 
		return "list-info";
	}
	
	public String getDetail() {
		return "liferay-table"; 		
	}
			
	public String getList() { 
		return "";
	}
	
	public String getListCellSpacing() {
		return "border=0 cellspacing=0 cellpadding=0"; 
	}
	
	public String getListStyle() {
		return "style='border-collapse: collapse; border-bottom: 2px solid #CCCCCC;'"; 
	}	
	
	
	public String getListHeader() { 
		return "portlet-section-header";		
	}
	
	
	
	public String getListHeaderCell() { 		
		return getListCell();
	}
	
	
	public String getListSubheader() {
		return "portlet-section-subheader";		
	}
	
	
	public String getListSubheaderCell() { 
		return getListCell();		
	}
		
	public String getListPair() { 
		return "portlet-section-body";		
	}
	
	public String getListPairEvents(String additionalClass) { 		
		return "onmouseover=\"this.className = 'portlet-section-body-hover " + additionalClass + "';\" onmouseout=\"this.className = 'portlet-section-body " + additionalClass + "';\"";
	}
	
	public String getListPairCell() {
		return getListCell();
	}
	
	public String getListOdd() { 
		return "portlet-section-alternate";		
	}
	
	public String getListOddEvents(String additionalClass) { 
		return "onmouseover=\"this.className = 'portlet-section-alternate-hover " + additionalClass + "';\" onmouseout=\"this.className = 'portlet-section-alternate " + additionalClass + "';\"";		
	}
				
	public String getListOddCell() { 
		return getListCell();		
	}
	
	public String getListPairSelected() { 
		return "portlet-section-selected liferay-list-selected"; 
	}
	
	public String getListOddSelected() { 
		return "portlet-section-selected liferay-list-selected"; 
	}
	
		
	public String getListInfo() {
		return "list-info";
	}
	
		
	public String getListTitle() {
		return "list-title";
	}
	
	private String getListCell() { 
		return "liferay-xava-cell-wrapper";		
	}

	public String getFrameHeaderStartDecoration() { 		
		return "<table style='float:left; clear:none'><tr><td>\n" +
			"<div class='portlet'><div class='portlet-topper'>";		
	}	
	public String getFrameHeaderEndDecoration() { 
		return "</div>"; 
	}
	public String getFrameTitleStartDecoration() { 
		return "<span class='portlet-title'>";
	}	
	public String getFrameTitleEndDecoration() { 
		return "</span>";
	}
	public String getFrameActionsStartDecoration() { 
		return "<div class='portlet-icons'>";
	}	
	public String getFrameActionsEndDecoration() { 
		return "</div>";
	}		
		
	public String getFrameContentStartDecoration() { 		
		return "<div class='portlet-content'><div class='portlet-content-container'>\n";
	}
	public String getFrameContentEndDecoration() { 		
		return "\n</div></div></div></td></tr></table>";
	}
			
	public String getEditor() { 
		return "form-text";
	}
	
	public String getSmallLabel() {
		return "''";
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
		return ""; 
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
	
	public String getSectionTableAttributes() {		
		return "border='0' cellpadding='0' cellspacing='0' width='100%'";
	}
	
	public String getSectionActive() {
		return "";
	}	
	
	public String getSectionLink() {
		return "";
	}
	
	public String getSectionBarStartDecoration() {
		return "<td style='padding-top: 4px;'><ul class='tabs'>"; 
	}
	public String getSectionBarEndDecoration() {	
		return "</ul></td>";
	}
		
	public String getActiveSectionTabStartDecoration() {
		return "<li class='current'><a href='javascript:void(0)'>"; 
	}
	
	public String getActiveSectionTabEndDecoration() {
		return "</a></li>";	  	
	}
	
	public String getSectionTabStartDecoration() {
		return "<li>";
	}
	
	public String getSectionTabEndDecoration() {
		return "</li>";		
	}	
		
	public String getCollectionListActions() {
		return "";
	}
	
	public String getRestoreImage() {
		return "/html/themes/classic/images/portlet/minimize.png";
	}
	
	public String getMaximizeImage() {
		return "/html/themes/classic/images/portlet/maximize.png";
	}
	
	public String getRemoveImage() {
		return "/html/themes/classic/images/portlet/close.png";
	}
	
	public boolean isApplySelectedStyleToCellInList() {
		return false;
	}
		
}
