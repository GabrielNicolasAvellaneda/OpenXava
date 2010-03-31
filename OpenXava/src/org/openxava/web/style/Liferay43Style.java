package org.openxava.web.style;

import java.util.*;

/**
 * For Liferay 4.3, 4.4 and 5.0. <p>
 * 
 * @author Javier Paniza
 */ 

public class Liferay43Style extends Style {
	
	private static Liferay43Style instance = null;

	
	
	protected Liferay43Style() {
	}
	
	public static Style getInstance() {
		if (instance == null) {
			instance = new Liferay43Style();
		}
		return instance;
	}
	
	public String [] getNoPortalModuleJsFiles() {  
		// The JS for Liferay 5.1 works fine for Liferay 4.3
		return Liferay51Style.getInstance().getNoPortalModuleJsFiles(); 
	}
	
	protected String getJQueryCss() { 
		return "/xava/style/cupertino/jquery-ui.css";
	}
			
	public String getInitThemeScript() { 
		return "jQuery( function() { Liferay.Util.addInputType(); Liferay.Util.addInputFocus(); } );";
	}
	
	public String getNoPortalModuleStartDecoration(String title) {   
		return "<div class='portlet' style='margin: 4px'><div class='portlet-topper'><span class='portlet-title'>"
			+ title + "</span></div><div class='portlet-content'>"; 
	}
	
	public String getNoPortalModuleEndDecoration() { 
		return "</div></div>";
	}					
	
	public String getModule() {
		return ""; 		
	}
	
	public String getModuleSpacing() {
		return "style='padding: 2px;'";		
	}
	
	public String getLabel() { 	
		return super.getLabel() + " liferay-xava-label";
	} 
	
	public String getEditorWrapper() { 
		return "liferay-xava-editor-wrapper";
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
		return "position: static; margin-bottom: 4px;"; 
	}
	
	public String getButtonBarStartStyle() {
		return "position: static;"; 
	}
	
	public String getButtonBarMiddleStyle() {
		return "position: static;";
	}
	
	public String getButtonBarEndStyle() {
		return "position: static;"; 
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
		return "border-collapse: collapse; border-bottom: 2px solid #CCCCCC;"; 
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
	
	public String getListPairEvents(String additionalClass, String selectedClass) { 		
		return "onmouseover=\"this.className = 'portlet-section-body-hover results-row hover " + additionalClass + "';\" onmouseout=\"this.className = 'portlet-section-body results-row " + additionalClass + " " + selectedClass + "';\"";
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

	public String getListOddEvents(String additionalClass, String selectedClass) { 
		return "onmouseover=\"this.className = 'portlet-section-alternate-hover results-row alt hover " + additionalClass + "';\" onmouseout=\"this.className = 'portlet-section-alternate results-row alt " + additionalClass + " " + selectedClass + "';\"";		
	}
	
	public String getListOddCell() { 
		return getListCell();		
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

	public String getFrameContentStartDecoration() { 		
		return "<div class='portlet-content'><div class='portlet-content-container'>\n";
	}
	public String getFrameContentEndDecoration() { 		
		return "\n</div></div></div></td></tr></table>";		
	}
	
	public String getListPairSelected() { 
		return "liferay-list-selected"; 
	}
	
	public String getListOddSelected() { 
		return "liferay-list-selected"; 
	}	

	public String getFrameHeaderStartDecoration(int width) {
		String widthAttribute = width == 0?"":"width=" + width+ "% ";				
		return "<table " + widthAttribute + "style='float:left; clear:none'><tr><td>\n" +
			"<div class='portlet'><div class='portlet-topper' style='position: static; padding-right: 8px;'><table width='100%'><tr>"; // position: static needed for ie7 + liferay 4.3
	}	
	public String getFrameHeaderEndDecoration() { 
		return "</tr></table></div>"; 
	}
	public String getFrameTitleStartDecoration() { 
		return "<td><span class='portlet-title'>";
	}	
	public String getFrameTitleEndDecoration() { 
		return "</span></td>";
	}
	public String getFrameActionsStartDecoration() { 
		return "<td align='right'>";
	}	
	public String getFrameActionsEndDecoration() { 
		return "</td>";
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
		return "<li class='current' style='position: static;'><a href='javascript:void(0)'>"; // position: static needed for ie7 + liferay 4.3 
	}
	
	public String getActiveSectionTabEndDecoration() {
		return "</a></li>";	  	
	}
	
	public String getSectionTabStartDecoration() {
		return "<li style='position: static;'>"; // position: static needed for ie7 + liferay 4.3
	}
	
	public String getSectionTabEndDecoration() {
		return "</li>";		
	}	
		
	public String getCollectionListActions() {
		return "";
	}
	
	public String getRestoreImage() {
		return getImagesFolder() + "portlet/minimize.png"; 
	}
	
	public String getMaximizeImage() {
		return getImagesFolder() + "portlet/maximize.png"; 
	}
	
	public String getRemoveImage() {
		return getImagesFolder() + "portlet/close.png";
	}
	
	public String getLoadingModuleImage() { 
		return getImagesFolder() + "progress_bar/loading_animation.gif";
	}
	
	public boolean isApplySelectedStyleToCellInList() {
		return false;
	}
	
	public String getSectionLinkStyle() { 
		return "position: static;";
	}
	
	protected String getImagesFolder() { 
		return isInsidePortal()?"/html/themes/classic/images/":"style/liferay43/images/"; 
	}
	
	public String getSelectedRow(){
		return "liferay4-selected-row";
	}
	
	public String getSelectedRowStyle(){
		return "";
	}	
}
