package org.openxava.web.style;

public class WebSpherePortal61Style extends WebSpherePortal6Style {
	
	private static WebSpherePortal61Style instance = null;
		
	public String getLoadingModuleImage() {		
		return "/wps/themes/html/Portal/images/loading-dk.gif";
	}
	
	public String getSectionBarStartDecoration() {
		return "<td><div class='wptheme-pageHeader' style='height: 33px'><ul class='wpsPageBarFirstRow'>";
	}
	public String getSectionBarEndDecoration() {	
		return "</ul></div><div class='pageBarSeparator'><!--  --></div></td>";
	}	
	
	public String getFrameHeaderStartDecoration() {		
		return "<table style='float: left; margin-right: 4px;' class='wpsPortlet' cellpadding='0' cellspacing='0'><tr><td><table class='wpsPortletTitleBar' style='' cellpadding='0' cellspacing='0'><tr>";
	}
	
	public String getFrameTitleStartDecoration() {		
		return "<td align='left' class='" + getFrameTitleLabel() + "'>";
	}
		
	public String getFrameTitleEndDecoration() {		
		return "<img alt='' style='border: 0pt none ; text-align: right;' src='wps_files/title_minheight.gif' height='22' width='1'></td>";
	}
	
	public String getFrameActionsStartDecoration() { 
		return "<td align='right'>";
	}	
	public String getFrameActionsEndDecoration() { 
		return "</td>";
	}		
	
	
	public String getFrameHeaderEndDecoration() {		
		return "</tr></table>";
	}
		
	public String getFrameContentStartDecoration() {
		return "<div class='" + getFrameContent() + "'>\n";		
	}
	public String getFrameContentEndDecoration() { 
		return "\n</div></td></tr></table>";
	}
	
	protected String getImageMessageBaseURI() { 
		return "/wps/themes/html/Portal/"; 		
	}

	

	public static Style getInstance() {
		if (instance == null) {
			instance = new WebSpherePortal61Style();
		}
		return instance;
	}

}