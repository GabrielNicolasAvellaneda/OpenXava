package org.openxava.web.style;

import java.util.*;

import javax.servlet.http.*;

import org.apache.commons.logging.*;
import org.openxava.util.*;

/**
 * This class and its subclasses is used from JSP code to give
 * style to the web applications. <p>
 * 
 * The nomenclature is:
 * <ul>
 * <li>By default: CSS class name.
 * <li>..Style: A CSS inline style.
 * <li>..Image: URI of image
 * <li>..Events: code for javascript events
 * <li>..StartDecoration/EndDecoration: HTML code to put before and after.
 * <li>..Spacing: Table spacing
 * </ul>
 * 
 * @author Javier Paniza
 */ 

public class Style {
		 	
	private static Log log = LogFactory.getLog(Style.class);
	private static Style instance = null;
	private static Collection styleClasses; 
	private static Map<String, Style> stylesByBrowser = new HashMap<String, Style>(); 
	private Collection<String> additionalCssFiles; 
	private String cssFile; 
	private boolean insidePortal; 
	private String browser; 
	
	public Style() { 		
	}
	
	/**
	 * @since 4.2
	 */
	public static Style getInstanceForBrowser(HttpServletRequest request) { 
		String browser = request.getHeader("user-agent"); 
		Style instance = stylesByBrowser.get(browser);
		if (instance == null) {
			try {
				for (Object styleClass: getStyleClasses()) {
					try {
						Style style = (Style) Class.forName((String) styleClass).newInstance();
						if (style.isForBrowse(browser)) {
							instance = style;							
							break;
						}
					}
					catch (Exception ex) {
						log.warn(XavaResources.getString("style_for_browser_warning", browser), ex);
					}
				}
				if (instance == null) instance = getInstance();
				instance.setBrowser(browser);
				stylesByBrowser.put(browser, instance);				
			}
			catch (Exception ex) {
				log.warn(XavaResources.getString("style_for_browser_warning", browser), ex); 					
				instance = getInstance();
				instance.setBrowser(browser);
			}			
		}		
		return instance; 
	}
	
	public static Style getInstance() { 
		if (instance == null) {
			try {
				instance = (Style) Class.forName(XavaPreferences.getInstance().getStyleClass()).newInstance();
				instance.cssFile = XavaPreferences.getInstance().getStyleCSS();
			}
			catch (Exception ex) {
				log.warn(XavaResources.getString("default_style_warning"), ex); 					
				instance = new Style();
				instance.cssFile = "default.css";
			}			
		}		
		return instance;
	}

	
	private static Collection getStyleClasses() throws Exception {
		if (styleClasses == null) {
			PropertiesReader reader = new PropertiesReader(Style.class, "styles.properties");
			styleClasses = reader.get().keySet();
		}
		return styleClasses;
	}
	
	
	public boolean isForBrowse(String browser) {
		return false;
	}
	
	public String [] getNoPortalModuleJsFiles() { 
		return null;
	}
	
	protected String getJQueryCss() { 
		return "/xava/style/ui-lightness/jquery-ui.css";
	}

	protected Collection<String> createAdditionalCssFiles() {
		return Arrays.asList( 
			"/xava/editors/calendar/skins/aqua/theme.css", 
			"/xava/style/openxava.css",
			"/xava/style/yahoo-treeview/fonts/fonts-min.css",
			"/xava/style/yahoo-treeview/treeview.css",
			getJQueryCss()
		);
	}
	
	/**
	 * These css files will be always added, inside and outside portal. <p>
	 * 
	 * To refine it overwrite the {@link #createAdditionalCssFiles} method.<br>
	 */
	final public Collection<String> getAdditionalCssFiles() {
		if (additionalCssFiles == null) {
			additionalCssFiles = createAdditionalCssFiles();
		}		
		return additionalCssFiles;
	}
	
	/** 
	 * 
	 * @since 4.2
	 */
	public boolean allowsResizeColumns() { 
		return true;
	}
	
	public String getInitThemeScript() { 
		return null;
	}
	
	public String getNoPortalModuleStartDecoration(String title) {
		return "";
	}
	
	public String getNoPortalModuleEndDecoration() {
		return "";
	}
	
	public String getCssFile() {
		return cssFile;
	}
	
	/** 
	 * 
	 * @since 4.2
	 */
	public String getMetaTags() { 
		return "";
	}

	public String getEditorWrapper() { 
		return "";
	}
	
	/** 
	 * The folder with images used for actions. <p>
	 *  
	 * If it starts with / is absolute, otherwise starts from the application context path. 
	 * 
	 * @since 4.2
	 */
	public String getImagesFolder() { 
		return "xava/images";
	}
	
	/** 
	 * 
	 * @since 4.2
	 */
	public String getPreviousPageDisableImage() { 
		return "previous_page_disable.gif";
	}
	
	/** 
	 * 
	 * @since 4.2
	 */
	public String getNextPageDisableImage() { 
		return "next_page_disable.gif";
	}
	
	/** 
	 * 
	 * @since 4.2
	 */
	public String getPageNavigationSelectedImage() { 
		return "page_navigation_selected.gif";
	}
	
	/** 
	 * 
	 * @since 4.2
	 */
	public String getPageNavigationImage() { 
		return "page_navigation.gif";
	}
	
	public String getModule() {
		return "portlet-font";		
	}
	
	/** 
	 * 
	 * @since 4.2
	 */
	public String getModuleDescription() { 
		return "";
	}
	
	public String getModuleSpacing() {
		return "style='padding: 4px;'";		
	}
	
	public String getActionLink() {
		return "ox-action-link";
	}
	
	public String getActionImage() {
		return "ox-image-link";
	}
		
	public String getButtonBar() {
		return "ox-button-bar"; 
	}
	
	public String getButtonBarButton() { 
		return "ox-button-bar-button";
	}	
	
	public boolean isSeveralActionsPerRow() {
		return true;
	}
	
	/** 
	 * 
	 * @since 4.2
	 */
	public boolean isUseLinkForNoButtonBarAction() {  
		return false;
	}
	
	/**
	 * If it has value, an image is shown using this value as class,
	 * otherwise the image would be shown as the background of a span 
	 * with the getButtonBarButton() class.
	 * 
	 * @since 4.2
	 */
	public String getButtonBarImage() { 
		return "";
	}

	
	public String getButtonBarModeButton() {		
		return "ox-button-bar-mode-button"; 
	}
		
	/**
	 * 
	 * @since 4.2
	 */		
	public String getActive() { 
		return "ox-active";
	}

	/**
	 * 
	 * @since 4.2
	 */		
	public String getFirst() { 
		return "ox-first";
	}
	
	/**
	 * 
	 * @since 4.2
	 */		
	public String getLast() { 
		return "ox-last";
	}
								
	public String getDetail() {
		return "";
	}
			
	public String getList() {  
		return "list";
	}
	
	/**
	 * 
	 * @since 4.2
	 */		
	public String getView() { 
		return "";
	}
		
	public String getListCellSpacing() {
		return ""; 
	}
	
	public String getListStyle() { 
		return ""; 
	}	
	
	public String getListCellStyle() {
		return ""; 
	}
	
	
	public String getListHeader() { 
		return "list";
	}
	
	public String getListHeaderCell() { 
		return getListHeader();
	}
	
	public String getListSubheader() {
		return "list-subheader";
	}	
	
	public String getListSubheaderCell() { 
		return getListSubheader();
	}
	
	public String getListOrderBy() {
		return "";
	}
	
	public String getListPair() { 
		return "list-pair";
	}
	
	/** 
	 * @param  Since v4m5 it has no parameters
	 */
	public String getListPairEvents() {  
		return "";
	}	
	
	public String getListPairCell() { 
		return getListPair();
	}
	
	public String getListOdd() { 
		return "list-odd";
	}
	
	/** 
	 * @param  Since v4m5 it has no parameters
	 */
	public String getListOddEvents() {   
		return "";
	}		
		
	public String getListOddCell() { 
		return getListOdd();
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
		return getListInfo() + " list-info-detail" + ((isIE7() || isIE6())?" ie7":""); 
		
	}
		
	public String getListTitle() {
		return "list-title";
	}
	
	/**
	 * 	
	 * @since 4.2
	 */
	public String getHeaderListCount() { 
		return "";
	}
	
	public String getListTitleWrapper() {
		return "";
	}
	
	public String getFrameHeaderStartDecoration() {
		return getFrameHeaderStartDecoration(0); 
	}

	public String getFrameHeaderStartDecoration(int width) {  
		StringBuffer r = new StringBuffer();
		String widthAttribute = width == 0?"":"width=" + width+ "% ";
		r.append("<table ");
		r.append(widthAttribute);
		r.append(" class='");
		r.append(getFrame());
		r.append("' style='float:left; margin-right:4px'"); 
		r.append(getFrameSpacing());
		r.append(">");
		r.append("<tr class='");
		r.append(getFrameTitle());
		r.append("'>");		
		r.append("<th>\n");						
		return r.toString();
	}	
	public String getFrameHeaderEndDecoration() { 		
		return "</th></tr>";			
	}
	
	public String getFrameTitleStartDecoration() { 		
		StringBuffer r = new StringBuffer();
		r.append("<span style='float: left' ");
		r.append("class='");
		r.append(getFrameTitleLabel());
		r.append("'>\n");
		return r.toString();
	}	
	
	public String getFrameTitleEndDecoration() { 
		return "</span>";
	}
	
	/**
	 * 
	 * @since 4.2
	 */
	public String getFrameActions() {
		return "ox-frame-actions";
	}
	
	public String getFrameActionsStartDecoration() { 
		return "<span style='float: right'>";
	}	
	public String getFrameActionsEndDecoration() { 
		return "</span>";
	}		
	
	public String getFrameContentStartDecoration() { 
		return getFrameContentStartDecoration(UUID.randomUUID().toString(), false);
	}
		
	public String getFrameContentStartDecoration(String id, boolean closed) { 
		StringBuffer r = new StringBuffer();		
		r.append("<tr id='");
		r.append(id);
		r.append("' ");
		if (closed) r.append("style='display: none;'");
		r.append("><td class='");
		r.append(getFrameContent());		
		r.append("'>\n");
		return r.toString();
	}
	
	
	public String getFrameContentEndDecoration() { 
		return "\n</td></tr></table>";
	}
	
	protected String getFrame() { 
		return "frame";
	}
	
	protected String getFrameTitle() {  
		return getFrame();
	}
	
	protected String getFrameTitleLabel() { 
		return getFrameTitle();
	}
				
	protected String getFrameContent() { 
		return getFrame();
	}
	
	protected String getFrameSpacing() { 
		return "";
	}
		
	public String getEditor() { 
		return "editor";
	}
	
	public String getLabel() { 
		return "portlet-form-field-label ox-label"; 
	}
	
	public String getSmallLabel() {
		return "small-label";
	}
		
	public String getErrors() { 
		return "errors";
	}
	
	public String getErrorStartDecoration () { 
		return "";
	}
	
	public String getErrorEndDecoration () { 
		return "";
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
	
	public String getMessageStartDecoration () { 
		return "";
	}
	
	public String getMessageEndDecoration () { 
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
	
	public String getAscending2Image() { 
		return "ascending2.gif";
	}
	
	public String getDescending2Image() { 
		return "descending2.gif";
	}	
	
	public String getSection() {
		return "Jetspeed";
	}

	/**
	 * 
	 * @since 4.2
	 */
	public String getSectionTab() {
		return "ox-section-tab";
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
	
	public String getSectionLinkStyle() {
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
		return getButtonBar(); 
	}
	
	/** 
	 * If it starts with '/' the URI is absolute, otherwise the context path is inserted before.
	 */
	public String getRestoreImage() {
		return getImagesFolder() +  "/restore.gif"; 
	}

	/** 
	 * If it starts with '/' the URI is absolute, otherwise the context path is inserted before.
	 */	
	public String getMaximizeImage() {
		return getImagesFolder() +  "/maximize.gif";
	}
	
	/** 
	 * If it starts with '/' the URI is absolute, otherwise the context path is inserted before.
	 */
	public String getMinimizeImage() {  
		return getImagesFolder() +  "/minimize.gif";
	}	

	/** 
	 * If it starts with '/' the URI is absolute, otherwise the context path is inserted before.
	 */	
	public String getRemoveImage() {
		return getImagesFolder() +  "remove.gif";
	}
	
	public String getLoadingModuleImage() {
		return "images/loading-module.gif";
	}	
	
	/**
	 * If <code>true</code< the header in list is aligned as data displayed in its column. <p>
	 * 
	 * By default is <code>false</code> and it's used the portal default alignament for headers.
	 */
	public boolean isAlignHeaderAsData() {
		return false;
	}
	
	/**
	 * If <code>true</code> the style for selected row (or special style) in a list
	 * is applied to the row (tr) and to <b>also the cell</b> (td). <p>
	 * 
	 * If <code>false</code> the style is applied only to the row (tr).<p> 
	 *
     * By default is <code>true</code>.
	 */
	public boolean isApplySelectedStyleToCellInList() {
		return true;
	}
	
	public boolean isShowImageInButtonBarButton() {
		return true;
	}
	
	/**
	 * 
	 * @since 4.2
	 */
	public boolean isShowModuleDescription() {  
		return false;
	}
	
	/**
	 * 
	 * @since 4.2
	 */
	public boolean isShowPageNumber() { 
		return true;
	}	
		
	/**
	 * 
	 * @since 4.2
	 */	
	public boolean isRowLinkable() { 
		return true;
	}
	
		
	public String getBottomButtonsStyle() {
		return "";
	}
	
	public String getBottomButtons() {
		return "";
	}
	
	public boolean isNeededToIncludeCalendar() {
		return true;
	}

	/**
	 * 
	 * @since 4.2
	 */
	public boolean isChangingPageRowCountAllowed() { 
		return true;
	}

	/**
	 * 
	 * @since 4.2
	 */	
	public boolean isHideRowsAllowed() { 
		return true;
	}
	
	/**
	 * 
	 * @since 4.2
	 */	
	public boolean isShowRowCountOnTop() { 
		return false;
	}
	
	public boolean isInsidePortal() {
		return insidePortal;
	}

	public void setInsidePortal(boolean insidePortal) {
		this.insidePortal = insidePortal;
	}

	public String getSelectedRow(){
		return "selected-row";
	}
	
	public String getSelectedRowStyle(){
		return "";
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public String getBrowser() {
		return browser;
	}
	
	protected boolean isFirefox() { 		
		return browser == null?false:browser.contains("Firefox");
	}
	
	/** @since 4m5 */
	protected boolean isIE6() { 		
		return browser == null?false:browser.contains("MSIE 6");
	}
	
	/** @since 4m5 */
	protected boolean isIE7() { 		
		return browser == null?false:browser.contains("MSIE 7");
	}
	
		
	/**
	 * @since 4m5
	 */
	public String getCurrentRow() {
		return "current-row"; 
	}
	
	/**
	 * @since 4m5
	 */
	public String getCurrentRowCell() {
		return ""; 
	}
	
	/**
	 * @since 4m5
	 */
	public String getPageNavigationArrowDisable() { 
		return "page-navigation page-navigation-arrow page-navigation-arrow-disable"; 
	}
	
	/**
	 * @since 4m5
	 */
	public String getPageNavigationSelected() { 		
		return "page-navigation-selected";
	}
	
	/**
	 * @since 4m5
	 */
	public String getPageNavigation() { 		
		return "page-navigation";
	}
	
	/**
	 * 
	 * @since 4.2
	 */		
	public String getPageNavigationPages() { 
		return "";
	}
	
	/**
	 * @since 4m5
	 */
	public String getPageNavigationArrow() { 		
		return "page-navigation page-navigation-arrow";
	}
	
	/**
	 * @since 4m5
	 */
	public String getRowsPerPage() { 		
		return "rows-per-page";
	}
	
	/**
	 * 
	 * @since 4.2
	 */		
	public boolean isHelpAvailable()  {
		return true;
	}

	/**
	 * @since 4m6
	 */
	public String getHelpImage() {
		return "images/help.png";
	}
	
	/** @since 4m6 */
	public String getActiveSectionTabStartDecoration(boolean first, boolean last) {
		if (first) {
			return getActiveSectionFirstTabStartDecoration();
		}
		else if (last){
			return getActiveSectionLastTabStartDecoration();
		}
		return getActiveSectionTabStartDecoration();
	}
	
	/** @since 4m6 */
	public String getSectionTabStartDecoration(boolean first, boolean last) {
		if (first) {
			return getSectionFirstTabStartDecoration();
		}
		else if (last){
			return getSectionLastTabStartDecoration();
		}
		return getSectionTabStartDecoration();
	}
	
	/** @since 4m6 */
	public String getActiveSectionFirstTabStartDecoration() { 
		return getActiveSectionTabStartDecoration(); 
	} 
	
	/** @since 4m6 */
	public String getActiveSectionLastTabStartDecoration() { 
		return getActiveSectionTabStartDecoration(); 
	} 
	
	/** @since 4m6 */
	public String getSectionFirstTabStartDecoration() { 
		return getSectionTabStartDecoration(); 
	} 
	
	/** @since 4m6 */
	public String getSectionLastTabStartDecoration() { 
		return getSectionTabStartDecoration(); 
	}
		
	/**
	 * CSS class for the help icon, link or button. <p>
	 * 
	 * @since 4m6
	 */
	public String getHelp() {
		return "ox-help";
	}
	
	public String getTotalRow() { 
		return "";
	}
	
	public String getTotalCell() { 
		return "";
	}
	
	public String getTotalCapableCell() { 
		return "";
	}
	
	public String getTotalCellStyle() {
		return getTotalCellAlignStyle();
	}
	
	protected String getTotalCellAlignStyle() { 
		return "vertical-align: middle; text-align: right;	padding-right: 0px;";
	}
	
	public String getTotalEmptyCellStyle() { 
		return ""; 
	}
	
	public String getTotalCapableCellStyle() { 
		return getTotalEmptyCellStyle() + "vertical-align: top; text-align: right;	padding: 0px;";
	}
	
	/**
	 * 
	 * @since 4.2
	 */	
	public String getFilterCell() { 
		return "ox-filter-cell";
	}
	
	
}