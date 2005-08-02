<%@ page import="org.openxava.util.Is" %>
<%@ page import="org.openxava.util.XavaResources" %>

<%@ include file="script.jsp" %>

<jsp:useBean id="errors" class="org.openxava.util.Messages" scope="request"/>
<jsp:useBean id="messages" class="org.openxava.util.Messages" scope="request"/>
<jsp:useBean id="context" class="org.openxava.controller.ModuleContext" scope="session"/>

<%
boolean messagesOnTop = !"false".equalsIgnoreCase(request.getParameter("messagesOnTop"));
org.openxava.controller.ModuleManager manager = (org.openxava.controller.ModuleManager) context.get(request, "manager", "org.openxava.controller.ModuleManager");
manager.setSession(session);

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
<jsp:setProperty name="tab" property="conditionComparators"/>
<jsp:setProperty name="tab" property="conditionValues"/>
<% } %>


<%
manager.setApplicationName(request.getParameter("application"));
boolean isNew = manager.setModuleName(request.getParameter("module"));
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
if (manager.isXavaView()) { // here and after action execution
	if (manager.actionOfThisModule(request)) {	
		view.assignValuesToWebView();
	}
}

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
String forwardURI = (String) session.getAttribute("xava_forward");
String forwardInNewWindow = (String) session.getAttribute("xava_forward_inNewWindow");
if (!Is.emptyString(forwardURI)) {
	session.removeAttribute("xava_forward");
	session.removeAttribute("xava_forward_inNewWindow");
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
boolean returnToPreviousModule = org.openxava.actions.IChangeModuleAction.PREVIOUS_MODULE.equals(manager.getNextModule());
if (returnToPreviousModule) { 
	org.openxava.controller.ModuleManager parentManager = (org.openxava.controller.ModuleManager) context.get(request.getParameter("application"), request.getParameter("parent"), "manager", "org.openxava.controller.ModuleManager");			
	parentManager.setNextModule(null);
	manager.setNextModule(null);
%>
<jsp:include page="module.jsp">
	<jsp:param name='application' value='<%=request.getParameter("application")%>'/>
	<jsp:param name='module' value='<%=request.getParameter("parent")%>'/>
</jsp:include>
<%	
}
else if (!org.openxava.util.Is.emptyString(manager.getNextModule())) {
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
function executeXavaAction(isConfirm, takesLong, formu, action) {
	if (isConfirm && !confirm('<%=XavaResources.getString(request, "are_you_sure")%>')) return;
	if (takesLong) {
		document.getElementById('processingLayer').style.display='block';
		setTimeout('document.images["processingImage"].src = "images/processing.gif"', 1);		
	}
	formu.focus_forward.value = "false";
	formu.xava_action.value=action;	
	formu.submit();	
}
function executeXavaAction(isConfirm, takesLong, formu, action, argv) {	
	if (isConfirm && !confirm('<%=XavaResources.getString(request, "are_you_sure")%>')) return;
	if (takesLong) {
		document.getElementById('processingLayer').style.display='block';
		setTimeout('document.images["processingImage"].src = "images/processing.gif"', 1);
	}
	formu.focus_forward.value = "false";
	formu.xava_action.value=action;	
	formu.xava_action_argv.value=argv;	
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
<title>OpenXava - <%=manager.getModuleDescription() %></title>
<link href="<%=request.getContextPath()%>/xava/style/default.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath()%>/xava/style/jetspeed.css" rel="stylesheet" type="text/css">
</head>

<body bgcolor="#ffffff" onload="setFocus()">

<%-- Layer for progress bar --%>
<div id='processingLayer' style='position:absolute;top:100px;left:150px;display:none'>
<table cellspacing='0'>
   <tr class='odd'>
       <td align='center' valign='middle' style='line-height:1.4;padding:25px 80px;border:2px solid #000'>
           <%=XavaResources.getString(request, "processing")%><br/>
           <img src='images/processing.gif' name='processingImage'/>
       </td>
   </tr>
</table>
</div>

<div class="module">
<form name="<%=manager.getForm()%>" method="POST" <%=manager.getEnctype()%>>
<INPUT type="hidden" name="xava_action" value=""/>
<INPUT type="hidden" name="xava_action_argv" value=""/>
<INPUT type="hidden" name="xava_action_application" value="<%=request.getParameter("application")%>"/>
<INPUT type="hidden" name="xava_action_module" value="<%=request.getParameter("module")%>"/>
<INPUT type="hidden" name="changed_property"/>
<INPUT type="hidden" name="focus_property"/>
<INPUT type="hidden" name="focus_forward"/>
<INPUT type="hidden" name="focus_property_id" value="<%=focusPropertyId%>"/>

<jsp:include page="languages.jsp"/>
<table cellpadding="2" cellspacing="2" border="0" width="100%">
  <tbody>
  	<% if (org.openxava.util.XavaPreferences.getInstance().isButtonBarOnTop()) { %>
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
  	<% if (org.openxava.util.XavaPreferences.getInstance().isButtonBarOnBottom()) { %>    
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
