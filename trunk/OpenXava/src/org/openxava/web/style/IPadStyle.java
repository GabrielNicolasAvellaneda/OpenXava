package org.openxava.web.style;

import java.util.*;

/**
 * For iPads. <p>
 * 
 * @author Javier Paniza
 */ 

public class IPadStyle extends Style {
	
	private static IPadStyle instance = null;	
	
	protected IPadStyle() {
	}
	
	public static Style getInstance() { 
		if (instance == null) {
			instance = new IPadStyle();
		}
		return instance;
	}
	
	public boolean isForBrowse(String browser) {		 
		return browser != null && browser.contains("iPad");
	}
	
	public String getCssFile() {
		return "ipad/ipad.css";
	}
		
	protected Collection<String> createAdditionalCssFiles() {
		return Arrays.asList( 
			"/xava/editors/calendar/skins/aqua/theme.css", 
			"/xava/style/yahoo-treeview/fonts/fonts-min.css",
			"/xava/style/yahoo-treeview/treeview.css",
			getJQueryCss()
		);
	}
	
	/**
	 * 
	 * @since 4.2
	 */
	public String getDefaultModeController() {
		return "DetailList";
	}
	
	public boolean allowsResizeColumns() { 
		return false;
	}
	
	public boolean isRowLinkable() { 
		return false;
	}
	
	public boolean isShowPageNumber() { 
		return false;
	}
	
	public boolean isShowModuleDescription() { 
		return true;
	}
	
	public boolean isSeveralActionsPerRow() {
		return false;
	}
	
	public boolean isChangingPageRowCountAllowed() {
		return false;
	}
	
	public boolean isHideRowsAllowed() {
		return false;
	}
	
	public boolean isShowRowCountOnTop() {
		return true;
	}

	public boolean isUseLinkForNoButtonBarAction() { 
		return true;
	}
	
	public boolean isHelpAvailable()  { 
		return false;
	}
	
	public String getMetaTags() { 
		return "<meta name='apple-mobile-web-app-capable' content='yes'/>";
	}

	public String getFrameActionsStartDecoration() {  
		return "<span class='" + getFrameActions() + "' style='float: right'>";
	}	
	public String getFrameActionsEndDecoration() { 
		return "</span>";
	}		

	public String getModuleSpacing() {
		return "style='padding: 0px;'";		
	}
	
	public String getModule() {
		return "ox-module";		
	}	
	
	public String getView() { 
		return "ox-view";
	}	
	
	public String getDetail() {
		return "ox-detail";
	}
	
	public String getLabel() { 
		return "ox-label";
	}
	
	public String getModuleDescription() { 
		return "ox-module-title";
	}

	
	protected String getFrameContent() { 
		return "ox-frame-content";
	}
	
	protected String getFrameTitle() {   
		return "ox-frame-title";
	}
	
	protected String getFrameTitleLabel() { 
		return "ox-frame-title-label";
	}
	
	public String getList() {  
		return "ox-list";
	}
	
	public String getListCellSpacing() {
		return "cellspacing=0 cellpadding=0";
	}
	
	public String getListPair() { 
		return "ox-list-pair";
	}
			
	public String getListOdd() { 
		return "ox-list-odd";
	}
	
	public String getListHeader() { 
		return "ox-list-header";
	}
	
	public String getListSubheader() {
		return "ox-list-subheader";
	}	
	
	public String getTotalRow() { 
		return "ox-total-row";
	}
	
	public String getTotalCell() {
		return "ox-total-cell"; 
	}
	
	public String getTotalCapableCell() {
		return "ox-total-capable-cell"; 
	}
	
	public String getTotalCellStyle() {
		return "";
	}
	
	public String getTotalCapableCellStyle() { 
		return "";
	}
	
	public String getListTitle() {
		return "ox-list-title";
	}
	
	public String getHeaderListCount() { 
		return "ox-header-list-count";
	}


	
	public String getListInfo() {
		return "ox-list-info";
	}
	
	public String getListInfoDetail() {
		return "";
	}
	
	public String getPageNavigationPages() {
		return "ox-page-navigation-pages";
	}
		
	public String getPageNavigation() {
		return "ox-page-navigation"; 
	}
	
	public String getPageNavigationSelected() { 		
		return "ox-page-navigation-selected"; 
	}

	
	public String getPageNavigationArrow() { 		
		return "ox-page-navigation-arrow";
	}
	
	public String getPageNavigationArrowDisable() { 		
		return getPageNavigationArrow();
	}
	

	public String getButtonBarModeButton() {
		return "ox-button-bar-mode-button";
		
	}
	
	public String getButtonBarActiveModeButton() { 
		return "ox-button-bar-active-mode-button";		
	}
	
	public String getBottomButtons() {
		return "ox-bottom-buttons";
	}

	public String getCollectionListActions() { 
		return "ox-collection-list-actions"; 
	}
		
	public String getSection() {
		return "ox-section";
	}
	
	public String getSectionActive() {
		return "ox-active-section";
	}
	
	public String getSectionLink() {
		return "ox-section-link";
	}
	
	public String getErrors() { 
		return "ox-errors";
	}
		
	public String getMessages() { 
		return "ox-messages";
	}

	
	public String getActiveSectionTabStartDecoration(boolean first, boolean last) { 
		return getSectionTabStartDecoration(first, last, true);
	}
	
	public String getSectionTabStartDecoration(boolean first, boolean last) { 
		return getSectionTabStartDecoration(first, last, false);
	}	
	
	protected String getSectionTabStartDecoration(boolean first, boolean last, boolean active) {
		StringBuffer r = new StringBuffer();		
		r.append("<span class='");
		if (active) {
			r.append(getActive());
			r.append(' ');
		}
		if (first) {
			r.append(getFirst());
			r.append(' ');
		}
		if (last) r.append(getLast());
		r.append("'>");		
		r.append("<span class='"); 
		r.append(getSectionTab());
		r.append("'>");
		return r.toString();
	}
	
	public String getActiveSectionTabEndDecoration() {		
		return "</span></span>";
	}
	
	public String getSectionTabEndDecoration() {
		return "</span></span>";
	}
		
	public boolean isShowImageInButtonBarButton() {
		return false;
	}
	
	public String getImagesFolder() { 
		return "xava/style/ipad/images";
	}
	
	public String getButtonBarImage() { 
		return "ox-button-bar-image";
	}
	
}

