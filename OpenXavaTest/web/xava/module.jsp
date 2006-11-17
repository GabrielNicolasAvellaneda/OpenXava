<%@ page import="java.awt.event.InputEvent" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="javax.swing.KeyStroke" %>
<%@ page import="org.apache.commons.collections.IteratorUtils" %>
<%@ page import="org.openxava.controller.meta.MetaAction" %>
<%@ page import="org.openxava.util.Is" %>
<%@ page import="org.openxava.util.Users" %>
<%@ page import="org.openxava.util.Locales" %>
<%@ page import="org.openxava.util.XavaResources" %>

<%@ include file="script.jsp" %>

<jsp:useBean id="errors" class="org.openxava.util.Messages" scope="request"/>
<jsp:useBean id="messages" class="org.openxava.util.Messages" scope="request"/>
<jsp:useBean id="context" class="org.openxava.controller.ModuleContext" scope="session"/>
<jsp:useBean id="style" class="org.openxava.web.style.Style" scope="request"/>

<%
Users.setCurrent(request);
Locales.setCurrent(request);
org.openxava.hibernate.XHibernate.setCmt(false); 
boolean isPortlet = (request.getAttribute("xava.portlet.renderURL") != null);
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
boolean hasProcessRequest = manager.hasProcessRequest(request); 
if (manager.isXavaView()) { // here and after action execution
	if (hasProcessRequest) {	
		view.assignValuesToWebView();
	}
}
manager.initModule(request, errors, messages);
if (hasProcessRequest) {
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
window.open("<%=request.getScheme()%>://<%=request.getServerName()%>:<%=request.getServerPort()%><%=request.getContextPath()%><%=forwardURI%>");
</script>
<%	
	}
	else {
%>
<script>
location.href="<%=request.getScheme()%>://<%=request.getServerName()%>:<%=request.getServerPort()%><%=request.getContextPath()%><%=forwardURI%>";
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
function executeXavaAction(confirmMessage, takesLong, formu, action) {
	executeXavaAction(confirmMessage, takesLong, formu, action, null);
}
function executeXavaAction(confirmMessage, takesLong, formu, action, argv) {	
	if (confirmMessage != "" && !confirm(confirmMessage)) return;
	if (takesLong) {
		document.getElementById('processingLayer').style.display='block';
		setTimeout('document.images["processingImage"].src = "<%=request.getContextPath()%>/xava/images/processing.gif"', 1);
	}
	formu.focus_forward.value = "false";
	formu.xava_action.value=action;	
	formu.xava_action_argv.value=argv;	
	formu.submit();
}
function throwPropertyChanged(formu, property) {	
	formu.focus_forward.value = "true";
	formu.focus_property.value=property;	
	formu.changed_property.value=property;	
	formu.submit();	
}
<% String focusPropertyId = view.getFocusPropertyId(); %>
function setFocus() {
	element = document.<%=manager.getForm()%>.elements['<%=focusPropertyId%>'];
	if (element != null && typeof element.disabled != "undefined" && !element.disabled) {
		element.focus();
		element.select();		
	}
}

function processKey(event) {
	if (!event) event = window.event;
<%
java.util.Iterator it = IteratorUtils.chainedIterator(
		new Iterator[] {
			manager.getMetaActions().iterator(), 
			manager.getMetaActionsMode().iterator()
		}
);
while (it.hasNext()) {
	MetaAction action = (MetaAction) it.next();
	if (!action.hasKeystroke()) continue;	

	KeyStroke key = KeyStroke.getKeyStroke(action.getKeystroke());
	if (key == null) {
		continue;
	}	
	int keyCode = key.getKeyCode();
	String ctrl = (key.getModifiers() & InputEvent.CTRL_DOWN_MASK) > 0?" && event.ctrlKey":""; 
	String alt = (key.getModifiers() & InputEvent.ALT_DOWN_MASK) > 0?" && event.altKey":""; 	
	String shift = (key.getModifiers() & InputEvent.SHIFT_DOWN_MASK) > 0?" && event.shiftKey":"";
%>
	if (event.keyCode == <%=keyCode%> <%=ctrl%> <%=alt%> <%=shift%>) {
		executeXavaAction('<%=action.getConfirmMessage(request)%>', <%=action.isTakesLong()%>, document.<%=manager.getForm()%>, '<%=action.getQualifiedName()%>');		
		event.returnValue = false;
		event.preventDefault();
		return;
	}
<%	
}
%>
	if (event.keyCode >= 49 && event.keyCode <= 57 && event.ctrlKey) {
		executeXavaAction("", false, document.<%=manager.getForm()%>, "Sections.change", "activeSection=" + (event.keyCode - 49));		
		event.returnValue = false;
		event.preventDefault();
		return;
	}	
}

document.onkeydown = processKey;
</script>



<% if (!isPortlet) { %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<html>
<head>
<title>OpenXava - <%=manager.getModuleDescription() %></title>
<link href="<%=request.getContextPath()%>/xava/style/default.css" rel="stylesheet" type="text/css">
</head>

<body bgcolor="#ffffff">
<% } %>

<link href="<%=request.getContextPath()%>/xava/style/openxava.css" rel="stylesheet" type="text/css">

<%-- Layer for progress bar --%>
<div id='processingLayer' style='position:absolute;top:100px;left:150px;display:none'>
<table cellspacing='0'>
   <tr class='<%=style.getProcessing()%>'>
       <td align='center' valign='middle' style='line-height:1.4;padding:25px 80px;border:2px solid #000'>
           <%=XavaResources.getString(request, "processing")%><br/>
           <img src='<%=request.getContextPath()%>/xava/images/processing.gif' name='processingImage'/>
       </td>
   </tr>
</table>
</div>

<div class="<%=style.getModule()%>">
<form name='<%=manager.getForm()%>' 
	method='POST' <%=manager.getEnctype()%> 
	<%=manager.getFormAction(request)%>>

<INPUT type="hidden" name="xava_page_id" value="<%=manager.getPageId()%>"/>
<INPUT type="hidden" name="xava_action" value=""/>
<INPUT type="hidden" name="xava_action_argv" value=""/>
<INPUT type="hidden" name="xava_action_application" value="<%=request.getParameter("application")%>"/>
<INPUT type="hidden" name="xava_action_module" value="<%=request.getParameter("module")%>"/>
<INPUT type="hidden" name="changed_property"/>
<INPUT type="hidden" name="focus_property"/>
<INPUT type="hidden" name="focus_forward"/>
<INPUT type="hidden" name="focus_property_id" value="<%=focusPropertyId%>"/>

<jsp:include page="languages.jsp"/>

<table <%=style.getModuleSpacing()%>>
  <tbody>
    <tr>
      <td class='<%=style.getButtonBar()%>'>
      	<jsp:include page="buttonBar.jsp"/>
      </td>
    </tr>
    <% if (messagesOnTop && (errors.contains() || messages.contains())) {  %>
    <tr>
      <td class=<%=style.getMessagesWrapper()%>>
		<jsp:include page="errors.jsp"/>
      </td>
    </tr>    
    <tr>
      <td class=<%=style.getMessagesWrapper()%>>
		<jsp:include page="messages.jsp"/>
      </td>
    </tr>            
    <% } %>
    <tr>      		
		<td <%=manager.isListMode()?"":("class=" + style.getDetail())%>>
		<jsp:include page='<%=manager.getViewURL()%>'/>
		</td>
    </tr>
    <tr>
      <td <%=style.getBottomButtonsStyle()%>>	
		<jsp:include page="bottomButtons.jsp"/>
      </td>
    </tr>
    <% if (!messagesOnTop) { %>
    <tr>
      <td class=<%=style.getMessagesWrapper()%>>
		<jsp:include page="errors.jsp"/>
      </td>
    </tr>    
    <tr>
      <td class=<%=style.getMessagesWrapper()%>>
		<jsp:include page="messages.jsp"/>
      </td>
    </tr>            
    <% } %>
  </tbody>
</table>
</form>
</div>

<% if (!isPortlet) { %>
</body></html>
<% } %>

<%
}
%>

<%
manager.commit(); // If hibernate, ejb3, etc is used to render some value here is commit
%>

<script>setFocus()</script>
