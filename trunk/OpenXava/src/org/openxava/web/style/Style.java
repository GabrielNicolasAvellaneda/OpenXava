package org.openxava.web.style;

/**
 * tmp: Rename, and explain?
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
	
	public String getButtonBarStyle() {
		return "";
	}
	
	public String getButtonBarStartStyle() {
		return "";
	}
	
	public String getButtonBarMiddleStyle() {
		return "";
	}
	
	public String getButtonBarEndStyle() {
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
	
	public String getListPairCell() { 
		return getListPair();
	}
	
	public String getListOdd() { 
		return "list-odd";
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
		return getListInfo();
	}
		
	public String getListTitle() {
		return "list-title";
	}
	
	public String getListTitleWrapper() {
		return "";
	}

	public String getFrameHeaderStartDecoration() { // done
		StringBuffer r = new StringBuffer();
		r.append("<table class='");
		r.append(getFrame());
		r.append("' style='float:left; margin-right:4px'"); 
		r.append(getFrameSpacing());
		r.append(">");
		r.append("<tr class='");
		r.append(getFrameTitle());
		r.append("'>");
		r.append("<th class='");
		r.append(getFrameTitleLabel());
		r.append("'>\n");		
		return r.toString();
	}	
	public String getFrameHeaderEndDecoration() { // done		
		return "</th></tr>";			
	}
	
	public String getFrameTitleStartDecoration() { // done		
		return "<span style='float: left'>"; 		
	}	
	public String getFrameTitleEndDecoration() { // done
		return "</span>";
	}
	public String getFrameActionsStartDecoration() { // done
		return "<span style='float: right'>";
	}	
	public String getFrameActionsEndDecoration() { // done
		return "</span>";
	}		
	
	public String getFrameContentStartDecoration() { // done
		StringBuffer r = new StringBuffer();
		r.append("<tr><td class='");
		r.append(getFrameContent());		
		r.append("'>\n");
		return r.toString();
	}
	public String getFrameContentEndDecoration() { // done
		return "\n</td></tr></table>";
	}
	
	protected String getFrame() { // done
		return "frame";
	}
	
	protected String getFrameTitle() { // done 
		return getFrame();
	}
	
	protected String getFrameTitleLabel() { // done
		return getFrameTitle();
	}
				
	protected String getFrameContent() { // done
		return getFrame();
	}
	
	protected String getFrameSpacing() { // done
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
	
	public String getBottomButtonsStyle() {
		return "";
	}
	
}
