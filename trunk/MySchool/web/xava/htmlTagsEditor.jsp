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
	preLabel="<tr><td colspan=4><table class=frame width='100%'><tr class=frame><th align='left'>";
	postLabel="&nbsp;";
	preIcons="";
	postIcons="";
	preEditor="</th></tr><tr><td class=frame>";
	postEditor="</td></tr></table>";
}
else if (first) {
	preLabel="<th align='left' class=etiqueta>";
	postLabel="</th>";
	preIcons="<td>";
	postIcons="</td>";
	preEditor="<td><table border='0' cellpadding='" + org.openxava.util.XavaPreferences.getInstance().getFormLineSpacing() + "' cellspacing='0'><tr><td valign='middle'>";
	postEditor="</td>";
}
else {
	preLabel="<th align='left' class=etiqueta>&nbsp;&nbsp;";
	postLabel="</th>";
	preIcons="<td>";
	postIcons="</td>";
	preEditor="<td valign='middle'>";
	postEditor="</td>";
}
%>