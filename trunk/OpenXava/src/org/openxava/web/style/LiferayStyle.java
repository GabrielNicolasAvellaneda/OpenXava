package org.openxava.web.style;

/*
 * 					Classic		Brochure	Negria		Standalone	Lf41	Lf42	WPS6	
 * Formulario		x			x			x			x
 * Secciones		x			x			x			x
 * Grupos			x			x			x			x											
 * Referencias		x			x			x			x			
 * Detall. colec.	x			x			x			x
 * Galería Imag.	x			x			x			x
 * Ed. enmarcables	x			x			x			x
 * Botones			x			x			x			x
 * Colecciones		x			x			x			x
 * Botonera			x			x			x			x
 * Vínculos			x			x			x			x
 * Lista			x			x			x			x
 * Calendario		x			x			x			x
 * 
 * Buscar tmps and dones 
 */


/**
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
		return ""; // done		
	}
	
	public String getModuleSpacing() {
		return "border=0 cellpadding=0 cellspacing=2 width='100%'";
	}
		
	public String getButtonBar() {
		return ""; // done
	}
	
	public String getButtonBar2() {
		return "portlet-section-header"; // done
	}
	
	public String getButtonBarSpacing() {
		return "cellpadding=0 cellspacing=0";		
	}	
	
	public String getButtonBarStart(boolean onBottom) {
		return ""; // done
	}
	
	public String getButtonBarMiddle(boolean onBottom) {
		return ""; 
	}
		
	public String getButtonBarEnd(boolean onBottom) {
		return ""; // done
	}
	
	public String getButtonBarStyle() {
		return "style='position: static; margin-bottom: 4px'"; // done		
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
		return "liferay-table"; // done		
	}
			
	public String getList() { 
		return "";
	}
	
	public String getListCellSpacing() {
		return "border=0 cellspacing=0 cellpadding=0"; // done
	}
	
	public String getListStyle() {
		return "style='border-collapse: collapse; border-bottom: 2px solid #CCCCCC;'"; // done
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
	
	public String getListPairEvents() { // tmp
		return "onmouseover=\"this.className = 'portlet-section-body-hover';\" onmouseout=\"this.className = 'portlet-section-body';\"";
	}
	
	public String getListPairCell() {
		return getListCell();
	}
	
	public String getListOdd() { 
		return "portlet-section-alternate";		
	}
	
	public String getListOddEvents() { // tmp
		return "onmouseover=\"this.className = 'portlet-section-alternate-hover';\" onmouseout=\"this.className = 'portlet-section-alternate';\"";
	}
		
	
	
	public String getListOddCell() { 
		return getListCell();		
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
	
	private String getListCell() { 
		return "liferay-xava-cell-wrapper";		
	}

	public String getFrameHeaderStartDecoration() { // done
		
		return "<table style='float:left; clear:none'><tr><td>\n" +
			"<div class='portlet'><div class='portlet-topper'>";		
	}	
	public String getFrameHeaderEndDecoration() { // done
		return "</div>"; 
	}
	public String getFrameTitleStartDecoration() { // done
		return "<span class='portlet-title'>";
	}	
	public String getFrameTitleEndDecoration() { // done
		return "</span>";
	}
	public String getFrameActionsStartDecoration() { // done
		return "<div class='portlet-icons'>";
	}	
	public String getFrameActionsEndDecoration() { // done
		return "</div>";
	}		
		
	public String getFrameContentStartDecoration() { // done		
		return "<div class='portlet-content'><div class='portlet-content-container'>\n";
	}
	public String getFrameContentEndDecoration() { // done		
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
		return ""; // done
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
