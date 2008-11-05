<%@ page import="java.awt.event.InputEvent" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="javax.swing.KeyStroke" %>
<%@ page import="org.openxava.controller.meta.MetaAction" %>
<%@ page import="org.openxava.util.Is" %>
<%@ page import="org.openxava.util.Users" %>
<%@ page import="org.openxava.util.Locales" %>
<%@ page import="org.openxava.util.XavaResources" %>

<jsp:useBean id="errors" class="org.openxava.util.Messages" scope="request"/>
<jsp:useBean id="messages" class="org.openxava.util.Messages" scope="request"/>
<jsp:useBean id="context" class="org.openxava.controller.ModuleContext" scope="session"/>

<%
String browser = (String) request.getAttribute("xava.portlet.user-agent");
if (browser == null) { 
	browser = request.getHeader("user-agent");
	request.setAttribute("xava.portlet.user-agent", browser);
}
Users.setCurrent(request);
Locales.setCurrent(request);

org.openxava.controller.ModuleManager manager = (org.openxava.controller.ModuleManager) context.get(request, "manager", "org.openxava.controller.ModuleManager");
manager.setSession(session);
manager.resetPersistence();

org.openxava.tab.Tab t = (org.openxava.tab.Tab) context.get(request, "xava_tab");
request.setAttribute("tab", t);
%>
<jsp:useBean id="tab" class="org.openxava.tab.Tab" scope="request"/>
<%
if (manager.isListMode()) {
	tab.deselectVisualizedRows();
}
%>

<% if (!"false".equals(request.getAttribute("xava.sendParametersToTab"))) { %>  
<jsp:setProperty name="tab" property="selected"/>
<% } %>

<%
manager.setApplicationName(request.getParameter("application"));
boolean isNew = manager.setModuleName(request.getParameter("module"));
manager.executeBeforeEachRequestActions(request, errors, messages); 
org.openxava.view.View view = (org.openxava.view.View) context.get(request, "xava_view");
if (isNew) {
	view.setModelName(manager.getModelName());	
	view.setViewName(manager.getXavaViewName());
}
view.setRequest(request);
view.setErrors(errors);
view.setMessages(messages);

tab.setRequest(request);
if (manager.isListMode()) {
	tab.setModelName(manager.getModelName());
	if (tab.getTabName() == null) { 
		tab.setTabName(manager.getTabName());
	}
}
boolean hasProcessRequest = manager.hasProcessRequest(request);
if (manager.isXavaView()) { // here and after action execution
	if (hasProcessRequest) {	
		view.assignValuesToWebView();
	}
}
manager.initModule(request, errors, messages);
manager.executeOnEachRequestActions(request, errors, messages); 
if (hasProcessRequest) {
	manager.execute(request, errors, messages);	
	if (manager.isListMode()) { // here and before execute the action
		tab.setModelName(manager.getModelName());	
		if (tab.getTabName() == null) { 
			tab.setTabName(manager.getTabName());
		}
	}
}
%>

<jsp:include page="languages.jsp"/>