package org.openxava.web.style;


/**
 * For Liferay 5.1. <p>
 * 
 * @author Javier Paniza
 */ 

public class Liferay51Style extends Liferay43Style {
	
	private static Liferay51Style instance = null;
	private static String [] noPortalModuleJsFiles = { 
		"style/liferay51/js/theme_display.js",	
		"style/liferay51/js/barebone_unpacked.js",
		"style/liferay51/js/liferay_init.js"
	};

	protected Liferay51Style() {
	}
	
	public static Style getInstance() {
		if (instance == null) {
			instance = new Liferay51Style();
		}
		return instance;
	}
	
	public String [] getNoPortalModuleJsFiles() { 
		return noPortalModuleJsFiles;
	}	
		
	protected String getJQueryCss() { 
		return "/xava/style/liferay51/css/jquery-ui-patch.css";
	}	
			
	public String getModuleSpacing() { 
		return "";		
	}
	
	public String getDetail() {
		return ""; 		
	}
	
	public String getListHeader() { 
		return "portlet-section-header results-header";		
	}
	
	public String getListSubheader() {
		return "portlet-section-subheader results-header";		
	}
			
	public String getListPair() { 
		return "portlet-section-body results-row";		
	}
	
	public String getListPairEvents(String additionalClass) { 		
		return "onmouseover=\"this.className = 'portlet-section-body-hover results-row hover " + additionalClass + "';\" onmouseout=\"this.className = 'portlet-section-body results-row " + additionalClass + "';\"";
	}
	
	public String getListOdd() { 
		return "portlet-section-alternate results-row alt";		
	}
	
	public String getListOddEvents(String additionalClass) { 
		return "onmouseover=\"this.className = 'portlet-section-alternate-hover results-row alt hover " + additionalClass + "';\" onmouseout=\"this.className = 'portlet-section-alternate results-row alt " + additionalClass + "';\"";		
	}
				
	public String getAscendingImage() {
		return "ascending-white.gif";
	}
	
	public String getDescendingImage() {
		return "descending-white.gif";
	}	
	
	public String getButtonBar2() {
		return "portlet-topper";  
	}
	
	public String getSectionBarStartDecoration() {
		return "<td style='padding-top: 4px;'>\n" +
				"<ul class='tabs ui-tabs'>"; 
	}
	
	public String getActiveSectionTabStartDecoration() {
		return "<li class='current' style='position: static;'><a href='javascript:void(0)' style='position: static;'>"; // position: static needed for ie7 
	}
	
	public String getSectionTabStartDecoration() {
		return "<li style='position: static;'>"; // position: static needed for ie7 
	}
	
	protected String getImagesFolder() {
		return isInsidePortal()?"/html/themes/classic/images/":"style/liferay51/images/"; 
	}
	
	public String getSelectedRow(){
		return "selected-row";
	}
	
}