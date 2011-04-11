<%@ include file="imports.jsp"%>

<%@page import="java.io.File"%>
<%@page import="java.util.Arrays"%>
<%@page import="org.openxava.util.XavaResources"%>
<%@page import="org.openxava.util.Locales"%>
<%@page import="org.openxava.util.Users"%>
<%@page import="org.openxava.util.XSystem"%>
<%@page import="org.openxava.util.Strings"%>
<%@page import="org.openxava.util.Is"%>
<%@page import="org.openxava.web.dwr.Module"%>
<%@page import="org.openxava.web.servlets.Servlets"%>
<%@page import="org.openxava.web.Ids"%>
<%@page import="org.openxava.web.servlets.Servlets"%>
<%@page import="org.openxava.web.servlets.Servlets"%>
<%@page import="org.apache.commons.logging.LogFactory" %>
<%@page import="org.apache.commons.logging.Log" %>

<%!private static Log log = LogFactory.getLog("module.jsp");

	private String getAdditionalParameters(HttpServletRequest request) {
		StringBuffer result = new StringBuffer();
		for (java.util.Enumeration en = request.getParameterNames(); en
				.hasMoreElements();) {
			String name = (String) en.nextElement();
			if ("application".equals(name) || "module".equals(name)
					|| "xava.portlet.application".equals(name)
					|| "xava.portlet.module".equals(name))
				continue;
			String value = request.getParameter(name);
			result.append('&');
			result.append(name);
			result.append('=');
			result.append(value);
		}
		return result.toString();
	}%>

<%
	if (request.getAttribute("style") == null) {
		request.setAttribute("style", org.openxava.web.style.Style
				.getInstance());
	}
%>

<jsp:useBean id="context" class="org.openxava.controller.ModuleContext" scope="session"/>
<jsp:useBean id="style" class="org.openxava.web.style.Style" scope="request"/>
<%
	Servlets.setCharacterEncoding(request, response);
	Locales.setCurrent(request);	
	request.getSession().setAttribute("xava.user",
			request.getRemoteUser());
	Users.setCurrent(request); 
	String app = request.getParameter("application");
	String module = context.getCurrentModule(request);

	org.openxava.controller.ModuleManager managerHome = (org.openxava.controller.ModuleManager) context
			.get(request, "manager",
					"org.openxava.controller.ModuleManager");
	org.openxava.controller.ModuleManager manager = (org.openxava.controller.ModuleManager) context
			.get(app, module, "manager",
					"org.openxava.controller.ModuleManager");

	manager.setSession(session);
	manager.setApplicationName(request.getParameter("application"));

	manager.setModuleName(module); // In order to show the correct description in head

	if (manager.isFormUpload()) {
		new Module().requestMultipart(request, response, app, module);
	}
	else {
		Module.restoreLastMessages(request, app, module); 
	}
	String browser = request.getHeader("user-agent");
	style.setBrowser(browser);
	boolean isPortlet = (session.getAttribute(Ids.decorate(app, request
			.getParameter("module"), "xava.portlet.uploadActionURL")) != null);

	Module.setPortlet(isPortlet);
	Module.setStyle(style);
	String version = org.openxava.controller.ModuleManager.getVersion();
	String realPath = request.getSession().getServletContext()
			.getRealPath("/");
	boolean coreViaAJAX = !manager.getPreviousModules().isEmpty() || manager.getDialogLevel() > 0;
%>
<jsp:include page="execute.jsp"/>
<%
	if (!isPortlet) {
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" >

<head>
	<title><%=managerHome.getModuleDescription()%></title>
	<link href="<%=request.getContextPath()%>/xava/style/<%=style.getCssFile()%>?ox=<%=version%>" rel="stylesheet" type="text/css"> 
	<%
 		String[] jsFiles = style.getNoPortalModuleJsFiles();
 			if (jsFiles != null) {
 				for (int i = 0; i < jsFiles.length; i++) {
 	%>
	<script src="<%=request.getContextPath()%>/xava/style/<%=jsFiles[i]%>?ox=<%=version%>" type="text/javascript"></script>
	<%
		}
			}
	%>

<%
	}
%>
	<%
		for (java.util.Iterator it = style.getAdditionalCssFiles()
				.iterator(); it.hasNext();) {
			String cssFile = (String) it.next();
	%> 
	<link rel="stylesheet" type="text/css" media="all" href="<%=request.getContextPath()%><%=cssFile%>?ox=<%=version%>"/>
	<%
		}
	%> 	
	<script type='text/javascript' src='<%=request.getContextPath()%>/xava/js/dwr-engine.js?ox=<%=version%>'></script>
	<script type='text/javascript' src='<%=request.getContextPath()%>/dwr/util.js?ox=<%=version%>'></script>
	<script type='text/javascript' src='<%=request.getContextPath()%>/dwr/interface/Module.js?ox=<%=version%>'></script>
	<script type='text/javascript' src='<%=request.getContextPath()%>/dwr/interface/Tab.js?ox=<%=version%>'></script>
	<script type='text/javascript' src='<%=request.getContextPath()%>/dwr/interface/View.js?ox=<%=version%>'></script>
	<script type='text/javascript' src='<%=request.getContextPath()%>/xava/js/openxava.js?ox=<%=version%>'></script>
	<%
		if (style.isNeededToIncludeCalendar()) {
	%>
	<script type="text/javascript" src="<%=request.getContextPath()%>/xava/editors/calendar/calendar.js?ox=<%=version%>"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/xava/editors/calendar/lang/calendar-<%=Locales.getCurrent().getLanguage()%>.js?ox=<%=version%>"></script>	
	<%
			}
		%>	
	<script type='text/javascript' src='<%=request.getContextPath()%>/xava/js/calendar.js?ox=<%=version%>'></script>
	<%
		if (new File(realPath + "/xava/js/custom-editors.js").exists()) {
	%>
	<script type='text/javascript' src='<%=request.getContextPath()%>/xava/js/custom-editors.js?ox=<%=version%>'></script>
	<%
		log.warn(XavaResources.getString("custom_editors_deprecated"));
		}
	%>	
	<script type="text/javascript">
		if (typeof jQuery != "undefined") {  
			portalJQuery = jQuery;
		}       
	</script>				
	<script type="text/javascript" src="<%=request.getContextPath()%>/xava/js/jquery.js?ox=<%=version%>"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/xava/js/jquery-ui.js?ox=<%=version%>"></script>	
	<script type="text/javascript" src="<%=request.getContextPath()%>/xava/js/jquery.bgiframe.min.js?ox=<%=version%>"></script>
	<%
		File jsEditorsFolder = new File(realPath + "/xava/editors/js");		
		String[] jsEditors = jsEditorsFolder.list();
		Arrays.sort(jsEditors);
		for (int i = 0; i < jsEditors.length; i++) {
	%>
	<script type="text/javascript" src="<%=request.getContextPath()%>/xava/editors/js/<%=jsEditors[i]%>?ox=<%=version%>"></script>
	<%
		}
	%>	
	<script type="text/javascript">
		$ = jQuery;
		if (typeof portalJQuery != "undefined") {  
			jQuery = portalJQuery;    
		}   
	</script>
<%
	if (!isPortlet) {
%>
</head>
<body bgcolor="#ffffff">
<%=style.getNoPortalModuleStartDecoration(managerHome
						.getModuleDescription())%>
<%
	}
%> 
<% 
if (manager.isResetFormPostNeeded()) {	
%>		
	<form id="xava_reset_form"></form>
<% } else  { %>

	<input id="xava_last_module_change" type="hidden" value=""/>
	<input id="<xava:id name='loading'/>" type="hidden" value="<%=coreViaAJAX%>"/>
	<input id="<xava:id name='loaded_parts'/>" type="hidden" value=""/>
	<input id="<xava:id name='view_member'/>" type="hidden" value=""/>
		
	<%-- Layer for progress bar --%>
	<div id='xava_processing_layer' style='position:absolute;top:100px;left:150px;display:none; z-index: 9999'>
	<table cellspacing='0'>
	   <tr class='<%=style.getProcessing()%>'>
	       <td align='center' valign='middle' style='line-height:1.4;padding:25px 80px;border:2px solid #000'>
	           <%=XavaResources.getString(request, "processing")%><br/>
	           <img src='<%=request.getContextPath()%>/xava/images/processing.gif' name='xava_processingImage'/>
	       </td>
	   </tr>
	</table>
	</div>	
	<div id="<xava:id name='core'/>" style="display: inline;">
		<%
			if (!coreViaAJAX) {
		%>
		<jsp:include page="core.jsp"/>
		<%
			}
		%>
		
	</div>
<% } %>			
	<div id="xava_console">
	</div>

<%
	if (!isPortlet) {
%>
<%=style.getNoPortalModuleEndDecoration()%>
</body>
</html>
<%
	}
%>

<% 
if (manager.isResetFormPostNeeded()) {  
	manager.setResetFormPostNeeded(false);		
%>		
	<script type="text/javascript">
	$("#xava_reset_form").submit();
	</script>		
<% } else  { %>

<script type="text/javascript">
<%String prefix = Strings.change(manager.getApplicationName(), "-",
					"_")
					+ "_" + Strings.change(manager.getModuleName(), "-", "_");
			String onLoadFunction = prefix + "_openxavaOnLoad";
			String initiated = prefix + "_initiated";%>
<%=onLoadFunction%> = function() { 
	if (openxava != null && openxava.<%=initiated%> == null) {
		openxava.showFiltersMessage = '<xava:message key="show_filters"/>';
		openxava.hideFiltersMessage = '<xava:message key="hide_filters"/>';
		openxava.loadingMessage = '<xava:message key="loading"/>';
		openxava.selectedRowClass = '<%=style.getSelectedRow()%>';
		openxava.currentRowClass = '<%=style.getCurrentRow()%>';
		openxava.currentRowCellClass = '<%=style.getCurrentRowCell()%>';
		openxava.closeDialogOnEscape = <%=browser != null && browser.indexOf("Firefox") >= 0 ? "false":"true"%>;		  
		openxava.calendarAlign = '<%=browser != null && browser.indexOf("MSIE 6") >= 0 ? "tr"
					: "Br"%>';
		<%String initThemeScript = style.getInitThemeScript();
			if (initThemeScript != null) {%>
		openxava.initTheme = function () { <%=style.getInitThemeScript()%> }; 
		<%}%>
		openxava.init("<%=manager.getApplicationName()%>", "<%=manager.getModuleName()%>");
		<%if (coreViaAJAX) {%>
		openxava.ajaxRequest("<%=manager.getApplicationName()%>", "<%=manager.getModuleName()%>", true);	
		<%}%>
		openxava.<%=initiated%> = true;
	}	
}
window.onload = <%=onLoadFunction%>;
setTimeout('<%=onLoadFunction%>()', 1000);
document.additionalParameters="<%=getAdditionalParameters(request)%>";
</script>

<% } %>