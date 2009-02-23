<%@ include file="imports.jsp"%>

<jsp:useBean id="errors" class="org.openxava.util.Messages" scope="request"/>
<jsp:useBean id="messages" class="org.openxava.util.Messages" scope="request"/>
<jsp:useBean id="style" class="org.openxava.web.style.Style" scope="request"/>
<jsp:useBean id="context" class="org.openxava.controller.ModuleContext" scope="session"/>

<%
org.openxava.controller.ModuleManager manager = (org.openxava.controller.ModuleManager) context.get(request, "manager");
org.openxava.view.View view = (org.openxava.view.View) context.get(request, "xava_view");
boolean messagesOnTop = !"false".equalsIgnoreCase(request.getParameter("messagesOnTop"));
%>
<div class="<%=style.getModule()%>">
<form id="<xava:id name='form'/>" name="<xava:id name='form'/>"
	method='POST' <%=manager.getEnctype()%> 
	<%=manager.getFormAction(request)%> style="display: inline;">

<div <%=style.getModuleSpacing()%> >
    <% if (manager.isButtonBarVisible()) { %>		
    <div id='<xava:id name="button_bar"/>' class='<%=style.getButtonBar()%>'>		
		<jsp:include page="buttonBar.jsp"/>
	</div>
    <% } %>
	
    <% if (messagesOnTop) { %>    
    <div id='<xava:id name="errors"/>' style="display: inline;">
    	<jsp:include page="errors.jsp"/>
	</div>
    
	<div id='<xava:id name="messages"/>' style="display: inline;">
		<jsp:include page="messages.jsp"/>
	</div>            
    <% } %>
          		 
	<div id='<xava:id name="view"/>' <%=manager.isListMode()?"":("class='" + style.getDetail() + "'")%> style='padding-top: 2px;'>
		<jsp:include page='<%=manager.getViewURL()%>'/>		
	</div>    	
    <div style="clear: both; padding-top: 2px;"></div>
	<div id='<xava:id name="bottom_buttons"/>' style="<%=style.getBottomButtonsStyle()%>">	
		<jsp:include page="bottomButtons.jsp"/>
	</div>
    
    <% if (!messagesOnTop) { %>
	<div id='<xava:id name="errors"/>'>
		<jsp:include page="errors.jsp"/>
	</div>
        
	<div id='<xava:id name="messages"/>'>
		<jsp:include page="messages.jsp"/>
	</div>               
    <% } %>
</div>
 
<INPUT type="hidden" name="<xava:id name='xava_action'/>" value=""/>
<INPUT type="hidden" name="<xava:id name='xava_action_argv'/>" value=""/>
<INPUT type="hidden" name="<xava:id name='xava_action_application'/>" value="<%=request.getParameter("application")%>"/>
<INPUT type="hidden" name="<xava:id name='xava_action_module'/>" value="<%=request.getParameter("module")%>"/>
<INPUT type="hidden" name="<xava:id name='xava_changed_property'/>"/> 
<INPUT type="hidden" name="<xava:id name='xava_focus_property'/>"/> 
<INPUT type="hidden" name="<xava:id name='xava_focus_forward'/>"/> 
<INPUT type="hidden" id="<xava:id name='xava_focus_property_id'/>" 
	name="<xava:id name='xava_focus_property_id'/>"/>

</form>
</div>