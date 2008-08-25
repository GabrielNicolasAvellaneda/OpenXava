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
<jsp:useBean id="style" class="org.openxava.web.style.Style" scope="request"/>

<%
String browser = (String) request.getAttribute("xava.portlet.user-agent");
if (browser == null) { 
	browser = request.getHeader("user-agent");
	request.setAttribute("xava.portlet.user-agent", browser);
}
Users.setCurrent(request);
Locales.setCurrent(request);

boolean messagesOnTop = !"false".equalsIgnoreCase(request.getParameter("messagesOnTop"));
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
boolean returnToPreviousModule = org.openxava.actions.IChangeModuleAction.PREVIOUS_MODULE.equals(manager.getNextModule());
if (returnToPreviousModule) { 
	org.openxava.controller.ModuleManager parentManager = (org.openxava.controller.ModuleManager) context.get(request.getParameter("application"), request.getParameter("parent"), "manager", "org.openxava.controller.ModuleManager");			
	parentManager.setNextModule(null);
	manager.setNextModule(null);
%>
<jsp:include page="core.jsp">
	<jsp:param name='application' value='<%=request.getParameter("application")%>'/>
	<jsp:param name='module' value='<%=request.getParameter("parent")%>'/>
</jsp:include>
<%	
}
else if (!org.openxava.util.Is.emptyString(manager.getNextModule())) {
%>
<jsp:include page="core.jsp">
	<jsp:param name='application' value='<%=request.getParameter("application")%>'/>
	<jsp:param name='module' value='<%=manager.getNextModule()%>'/>
	<jsp:param name='parent' value='<%=request.getParameter("module")%>'/>
</jsp:include>
<%
}
else { // All else
%>

<div class="<%=style.getModule()%>">
<form id="xava_form" name='<%=manager.getForm()%>' 
	method='POST' <%=manager.getEnctype()%> 
	<%=manager.getFormAction(request)%>>

<INPUT type="hidden" name="xava_page_id" value="<%=manager.getPageId()%>"/>
<INPUT type="hidden" name="xava_action" value=""/>
<INPUT type="hidden" name="xava_action_argv" value=""/>
<INPUT type="hidden" name="xava_action_application" value="<%=request.getParameter("application")%>"/>
<INPUT type="hidden" name="xava_action_module" value="<%=request.getParameter("module")%>"/>
<INPUT type="hidden" name="xava_changed_property"/> 
<INPUT type="hidden" name="xava_focus_property"/> 
<INPUT type="hidden" name="xava_focus_forward"/> 
<INPUT type="hidden" name="xava_focus_property_id" value="<%=view.getFocusPropertyId()%>"/>

<jsp:include page="languages.jsp"/>

<table <%=style.getModuleSpacing()%>>
  <tbody>
    <% if (manager.isButtonBarVisible()) { %>
    <tr>
      <td class='<%=style.getButtonBar()%>'>
      	<jsp:include page="buttonBar.jsp"/>
      </td>
    </tr>
    <% } %>
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
		<td <%=manager.isListMode()?"":("class='" + style.getDetail() + "'")%>>
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

<%
}
%>

<%
manager.commit(); // If hibernate, ejb3, etc is used to render some value here is commit
%>