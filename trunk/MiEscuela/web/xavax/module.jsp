<%@ page import="org.xavax.util.Is" %>

<jsp:useBean id="errors" class="org.xavax.util.Messages" scope="request"/>
<jsp:useBean id="messages" class="org.xavax.util.Messages" scope="request"/>
<jsp:useBean id="context" class="org.xavax.controller.ModuleContext" scope="session"/>

<%
boolean messagesOnTop = !"false".equalsIgnoreCase(request.getParameter("messagesOnTop"));
org.xavax.controller.ModuleManager manager = (org.xavax.controller.ModuleManager) context.get(request, "manager", "org.xavax.controller.ModuleManager");
manager.setSession(session);

org.xavax.tab.Tab t = (org.xavax.tab.Tab) context.get(request, "xavax_tab");
request.setAttribute("tab", t);
%>
<jsp:useBean id="tab" class="org.xavax.tab.Tab" scope="request"/>
<%
if (manager.isListMode()) {
	tab.deselectVisualizedRows();
}
%>
<jsp:setProperty name="tab" property="selected"/>
<jsp:setProperty name="tab" property="conditionComparators"/>
<jsp:setProperty name="tab" property="conditionValues"/>


<%
manager.setApplicationName(request.getParameter("application"));
boolean isNew = manager.setModuleName(request.getParameter("module"));
org.xavax.view.View view = (org.xavax.view.View) context.get(request, "xavax_view");
if (isNew) { 
	view.setModelName(manager.getModelName());	
	view.setViewName(manager.getXavaxViewName());
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
if (manager.isXavaxView()) { // here and after action execution
	if (manager.actionOfThisModule(request)) {	
		view.assignValuesToWebView();
	}
}

%>

<jsp:include page="../objects.jsp"/>

<%
manager.initModule(request, errors, messages);
if (manager.actionOfThisModule(request)) {
	manager.execute(request, errors, messages);	
	if (manager.isListMode()) { // here and before execute the action
		tab.setModelName(manager.getModelName());	
		if (tab.getTabName() == null) { 
			tab.setTabName(manager.getTabName());
		}
	}
}
String forwardURI = (String) session.getAttribute("xavax_forward");
String forwardInNewWindow = (String) session.getAttribute("xavax_forward_inNewWindow");
if (!Is.emptyString(forwardURI)) {
	session.removeAttribute("xavax_forward");
	session.removeAttribute("xavax_forward_inNewWindow");
	if ("true".equals(forwardInNewWindow)) {
%>
<script>
window.open("http://<%=request.getServerName()%>:<%=request.getServerPort()%><%=request.getContextPath()%><%=forwardURI%>");
</script>
<%	
	}
	else {
%>
<script>
location.href="http://<%=request.getServerName()%>:<%=request.getServerPort()%><%=request.getContextPath()%><%=forwardURI%>";
</script>
<%
	}
}
boolean returnToPreviousModule = org.xavax.actions.IChangeModuleAction.PREVIOUS_MODULE.equals(manager.getNextModule());
if (returnToPreviousModule) { 
	org.xavax.controller.ModuleManager parentManager = (org.xavax.controller.ModuleManager) context.get(request.getParameter("application"), request.getParameter("parent"), "manager", "org.xavax.controller.ModuleManager");			
	parentManager.setNextModule(null);
	manager.setNextModule(null);
%>
<jsp:include page="module.jsp">
	<jsp:param name='application' value='<%=request.getParameter("application")%>'/>
	<jsp:param name='module' value='<%=request.getParameter("parent")%>'/>
</jsp:include>
<%	
}
else if (!org.xavax.util.Is.emptyString(manager.getNextModule())) {
%>
<jsp:include page="module.jsp">
	<jsp:param name='application' value='<%=request.getParameter("application")%>'/>
	<jsp:param name='module' value='<%=manager.getNextModule()%>'/>
	<jsp:param name='parent' value='<%=request.getParameter("module")%>'/>
</jsp:include>
<%
}
else { // All else
%>


<script>
function executeXavaxAction(formu, action) {
	formu.focus_forward.value = "false";
	formu.xavax_action.value=action;	
	formu.submit();	
}
function executeXavaxAction(formu, action, argv) {	
	formu.focus_forward.value = "false";
	formu.xavax_action.value=action;	
	formu.xavax_action_argv.value=argv;	
	formu.submit();
}
function throwPropertyChanged(property) {	
	document.forms[0].focus_forward.value = "true";
	document.forms[0].focus_property.value=property;	
	document.forms[0].changed_property.value=property;	
	document.forms[0].submit();
}
<% String focusPropertyId = view.getFocusPropertyId(); %>
function setFocus() {
	element = document.<%=manager.getForm()%>.elements['<%=focusPropertyId%>'];
	if (element != null && typeof element.disabled != "undefined" && !element.disabled) {
		element.focus()
	}
}
</script>





<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<html>
<head>
<title>xavax - <%=manager.getModuleDescription() %></title>
<link href="style/default.css" rel="stylesheet" type="text/css">
<link href="style/jetspeed.css" rel="stylesheet" type="text/css">
</head>

<body bgcolor="#ffffff" onload="setFocus()">

<div class="module">
<form name="<%=manager.getForm()%>" method="POST" <%=manager.getEnctype()%>>
<INPUT type="hidden" name="xavax_action" value=""/>
<INPUT type="hidden" name="xavax_action_argv" value=""/>
<INPUT type="hidden" name="xavax_action_application" value="<%=request.getParameter("application")%>"/>
<INPUT type="hidden" name="xavax_action_module" value="<%=request.getParameter("module")%>"/>
<INPUT type="hidden" name="changed_property"/>
<INPUT type="hidden" name="focus_property"/>
<INPUT type="hidden" name="focus_forward"/>
<INPUT type="hidden" name="focus_property_id" value="<%=focusPropertyId%>"/>

<jsp:include page="languages.jsp"/>
<table cellpadding="2" cellspacing="2" border="0" width="100%">
  <tbody>
  	<% if (org.xavax.util.XavaxPreferences.getInstance().isButtonBarOnTop()) { %>
    <tr>
      <td class=buttonBar>
      	<jsp:include page="buttonBar.jsp"/>
      </td>
    </tr>
    <% } %>
    <% if (messagesOnTop && (errors.contains() || messages.contains())) {  %>
    <tr>
      <td>            
		<jsp:include page="errors.jsp"/>
      </td>
    </tr>    
    <tr>
      <td>
		<jsp:include page="messages.jsp"/>
      </td>
    </tr>            
    <% } %>
    <tr>      		
		<td class=body>
		<jsp:include page="<%=manager.getViewURL()%>"/>
		</td>
    </tr>
  	<% if (org.xavax.util.XavaxPreferences.getInstance().isButtonBarOnBottom()) { %>    
    <tr>
      <td class=buttonBar>
		<jsp:include page="buttonBar.jsp"/>
      </td>
    </tr>
  	<% } %>    
    <% if (!messagesOnTop) { %>
    <tr>
      <td>
		<jsp:include page="errors.jsp"/>
      </td>
    </tr>    
    <tr>
      <td>
		<jsp:include page="messages.jsp"/>
      </td>
    </tr>            
    <% } %>
  </tbody>
</table>
</form>
</div>

</body></html>

<%
}
%>