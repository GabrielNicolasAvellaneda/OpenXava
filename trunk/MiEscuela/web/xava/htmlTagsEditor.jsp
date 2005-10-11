<%
String shasFrame = request.getParameter("hasFrame"); 
boolean hasFrame="true".equals(shasFrame)?true:false;
String sfirst = request.getParameter("first"); 
boolean first="true".equals(sfirst)?true:false;

String preLabel=null;
String postLabel=null;
String preIcons=null;
String postIcons=null;
String preEditor=null;
String postEditor=null;


if (hasFrame) {
	preLabel="<tr><td colspan=4><table class=" + style.getFrame() + " width='100%' " + style.getFrameSpacing() + "><tr class=" + style.getFrameTitle() + "><th align='left' class=" + style.getFrameTitleLabel() + " id=" + labelKey + " >";
	postLabel="&nbsp;";
	preIcons="";
	postIcons="";
	preEditor="</th></tr><tr><td class=" + style.getFrameContent() + ">";
	postEditor="</td></tr></table>";
}
else if (first) {
	preLabel="<th style='vertical-align: middle;text-align: left' class=" + style.getLabel() + " id=" + labelKey + " >";
	postLabel="</th>";
	preIcons="<td style='vertical-align: middle'>";
	postIcons="</td>";
	preEditor="<td style='vertical-align: middle'><table border='0' cellpadding='" + org.openxava.util.XavaPreferences.getInstance().getFormLineSpacing() + "' cellspacing='0'><tr><td style='vertical-align: middle'>";
	postEditor="</td>";
}
else {
	preLabel="<th style='vertical-align: middle;text-align: left' class=" + style.getLabel() + " id=" + labelKey + " >&nbsp;&nbsp;";
	postLabel="</th>";
	preIcons="<td style='vertical-align: middle'>";
	postIcons="</td>";
	preEditor="<td style='vertical-align: middle'>";
	postEditor="</td>";
}
%>