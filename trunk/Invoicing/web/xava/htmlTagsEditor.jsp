<%
String sfirst = request.getParameter("first"); 
boolean first="true".equals(sfirst)?true:false;

String preLabel=null;
String postLabel=null;
String preIcons=null;
String postIcons=null;
String preEditor=null;
String postEditor=null;

if (first && !view.isAlignedByColumns()) { 
	preLabel="<td style='vertical-align: middle;text-align: left' class='" + style.getLabel() + "'>";
	postLabel="</td>";
	preIcons="<td style='vertical-align: middle' class='" + style.getEditorWrapper()+ "'>";
	postIcons="</td>";	
	String browser = (String) request.getAttribute("xava.portlet.user-agent");
	boolean firefox = browser.indexOf("Firefox") >= 0;
	// width: 99%  is for label and data not very separated when only one row, 
	//				but it produces no good layout of frames in Firefox.
	String width = firefox?"":"width: 99%";
	preEditor="<td style='vertical-align: middle; "  + width + "'><table border='0' cellpadding='0' cellspacing='0'><tr><td style='vertical-align: middle' class='" + style.getEditorWrapper()+ "'>";
	postEditor="</td>";
} 
else {
	preLabel="<td style='vertical-align: middle;text-align: left' class='" + style.getLabel() + "'>&nbsp;&nbsp;";
	postLabel="</td>";
	preIcons="<td style='vertical-align: middle' class='" + style.getEditorWrapper()+ "'>";
	postIcons="</td>";
	preEditor="<td style='vertical-align: middle' class='" + style.getEditorWrapper()+ "'>";
	postEditor="</td>";
}
%>