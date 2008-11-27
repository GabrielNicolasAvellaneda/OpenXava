<%@ include file="imports.jsp"%>

<%@page import="org.openxava.web.dwr.Module"%>
<%@page import="org.openxava.web.servlets.Servlets"%>
<%@page import="org.openxava.util.XavaResources"%>
<%@page import="org.openxava.util.Locales"%>
<%@page import="org.openxava.util.XSystem"%>

<%
if (request.getAttribute("style") == null) {	
	request.setAttribute("style", org.openxava.web.style.Style.getInstance());
}
%>

<jsp:useBean id="context" class="org.openxava.controller.ModuleContext" scope="session"/>
<jsp:useBean id="style" class="org.openxava.web.style.Style" scope="request"/>
<%
/* tmp
request.setCharacterEncoding(XSystem.getEncoding()); 
response.setCharacterEncoding(XSystem.getEncoding());
*/
Servlets.setCharacterEncoding(request, response); // tmp
Locales.setCurrent(request);
request.getSession().setAttribute("xava.user", request.getRemoteUser()); 
String app = request.getParameter("application");
String module = request.getParameter("module");
org.openxava.controller.ModuleManager manager = (org.openxava.controller.ModuleManager)context.get(request, "manager", "org.openxava.controller.ModuleManager");
manager.setSession(session);
manager.setApplicationName(request.getParameter("application"));
manager.setModuleName(request.getParameter("module")); // In order to show the correct description in head
if (manager.isFormUpload()) {
	new Module().requestMultipart(request, response, app, module);
}
String form = manager.getForm();
String browser = request.getHeader("user-agent");
boolean isPortlet = (request.getAttribute("xava.portlet.renderURL") != null);
Module.setPortlet(isPortlet);
Module.setStyle(style);
%>
<% if (!isPortlet) { %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">


<%@page import="org.openxava.web.servlets.Servlets"%><html xmlns="http://www.w3.org/1999/xhtml" >


<head>
	<title><%=manager.getModuleDescription() %></title>
	<link href="<%=request.getContextPath()%>/xava/style/<%=style.getCssFile()%>" rel="stylesheet" type="text/css"> 
	<% 
	String [] jsFiles = style.getNoPortalModuleJsFiles(); 
	if (jsFiles != null) {
		for (int i=0; i<jsFiles.length; i++) {	
	%>
	<script src="<%=jsFiles[i]%>" type="text/javascript"></script>
	<% 	
		}
	}
	%>

<% } %>
	<link rel="stylesheet" type="text/css" media="all" href="<%=request.getContextPath()%>/xava/editors/calendar/skins/aqua/theme.css" title="Aqua" /> 
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/xava/style/openxava.css" />	
	<script type='text/javascript' src='<%=request.getContextPath()%>/dwr/engine.js'></script>
	<script type='text/javascript' src='<%=request.getContextPath()%>/dwr/util.js'></script>
	<script type='text/javascript' src='<%=request.getContextPath()%>/dwr/interface/Module.js'></script>
	<script type='text/javascript' src='<%=request.getContextPath()%>/dwr/interface/Tab.js'></script>
	<script type='text/javascript' src='<%=request.getContextPath()%>/xava/js/openxava.js'></script>
	<% if (style.isNeededToIncludeCalendar()) { %>
	<script type="text/javascript" src="<%=request.getContextPath()%>/xava/editors/calendar/calendar.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/xava/editors/calendar/lang/calendar-<%=Locales.getCurrent().getLanguage()%>.js"></script>	
	<% } %>	
	<script type='text/javascript' src='<%=request.getContextPath()%>/xava/js/calendar.js'></script>
	<script type='text/javascript' src='<%=request.getContextPath()%>/xava/js/editors.js'></script> 	
	<script type='text/javascript' src='<%=request.getContextPath()%>/xava/js/custom-editors.js'></script>
<% if (!isPortlet) { %>
</head>
<body bgcolor="#ffffff">
<%=style.getNoPortalModuleStartDecoration(manager.getModuleDescription())%>
<% } %>	
	<input id="xava_loading" type="hidden" value="true"/>
	<input id="xava_loaded_parts" type="hidden" value=""/>	
	<%-- Layer for progress bar --%>
	<div id='xava_processing_layer' style='position:absolute;top:100px;left:150px;display:none'>
	<table cellspacing='0'>
	   <tr class='<%=style.getProcessing()%>'>
	       <td align='center' valign='middle' style='line-height:1.4;padding:25px 80px;border:2px solid #000'>
	           <%=XavaResources.getString(request, "processing")%><br/>
	           <img src='<%=request.getContextPath()%>/xava/images/processing.gif' name='xava_processingImage'/>
	       </td>
	   </tr>
	</table>
	</div>	
	<div id="xava_core" style="display: inline;">
		<%
		// tmp ini
		String loadingImage=style.getLoadingModuleImage();
		if (!loadingImage.startsWith("/")) loadingImage = request.getContextPath() + "/xava/" + style.getLoadingModuleImage();
		// tmp ini
		%>
		<img src='<%=loadingImage%>' style="padding: 20px;"/>
	</div>

<% if (!isPortlet) { %>
<%=style.getNoPortalModuleEndDecoration()%>
</body>
</html>
<% } %>

<script>
openxavaOnLoad = function() { 
	if (openxava != null && openxava.application == null) {
		openxava.application = '<%=app%>';
		openxava.module = '<%=module%>';
		openxava.formName = '<%=form%>'; 			
		openxava.showFiltersMessage = '<xava:message key="show_filters"/>';
		openxava.hideFiltersMessage = '<xava:message key="hide_filters"/>';
		openxava.loadingMessage = '<xava:message key="loading"/>';
		openxava.calendarAlign = '<%=browser != null && browser.indexOf("MSIE 6") >= 0?"tr":"Br"%>';
		<%
		String initThemeScript = style.getInitThemeScript();
		if (initThemeScript != null) {
		%>
		openxava.initTheme = function () { <%=style.getInitThemeScript() %> }; 
		<%
		}
		%>
		openxava.init();		
	}	
}
window.onload = openxavaOnLoad;
setTimeout('openxavaOnLoad()', 1000);
</script>


