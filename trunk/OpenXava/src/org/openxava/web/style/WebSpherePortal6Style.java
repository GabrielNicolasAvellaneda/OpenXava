package org.openxava.web.style;

/**
 * 
 * @author Javier Paniza
 */

public class WebSpherePortal6Style extends WebSpherePortalStyle {
	
	private static WebSpherePortal6Style instance = null;
	
	protected WebSpherePortal6Style() {		
	}
	
	public static Style getInstance() {
		if (instance == null) {
			instance = new WebSpherePortal6Style();
		}
		return instance;
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
		return "<td><ul class='wpsPageBarFirstRow'>";
	}
	public String getSectionBarEndDecoration() {	
		return "</ul><div class='pageBarSeparator'><!--  --></div></td>";
	}
		
	public String getActiveSectionTabStartDecoration() {
		return "<li class='wpsSelectedPage'>";
	}
	
	public String getActiveSectionTabEndDecoration() {
		return "</li>";		
	}
	
	public String getSectionTabStartDecoration() {
		return "<li class='wpsUnSelectedPage'>";
	}
	
	public String getSectionTabEndDecoration() {
		return "</li>";		
	}

	public String getFrame() { // tmp hacer proctected
		return "wpsPortlet";
	}
	
	public String getFrameTitle() { // tmp hacer proctected
		return "wpsPortletTitleBar";
	}
		
	public String getFrameTitleLabel() { // tmp hacer proctected
		return "websphere-frame-title-label";
	}
	
	public String getFrameTitleStartDecoration() { // tmp hacer proctected
		return "<div style='width:100%;'>";
	}
	
	public String getFrameTitleEndDecoration() { // tmp hacer proctected
		return "<img alt='' style='border:0; text-align: right;' width='1' height='22' src='/wps/skins/html/IBM/title_minheight.gif'></div>";
	}
	
	public String getFrameContent() { // tmp hacer proctected
		return "wpsPortletBody";
	}
	
	public String getFrameSpacing() { // tmp hacer proctected
		return "border=0 cellspacing=0 cellpadding=2"; 		
	}
				
}
