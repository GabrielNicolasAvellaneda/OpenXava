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
<form id="xava_form" name='<%=manager.getForm()%>' 
	method='POST' <%=manager.getEnctype()%> 
	<%=manager.getFormAction(request)%> style="display: inline;">

<INPUT type="hidden" name="xava_page_id" value="<%=manager.getPageId()%>"/>
<INPUT type="hidden" name="xava_action" value=""/>
<INPUT type="hidden" name="xava_action_argv" value=""/>
<INPUT type="hidden" name="xava_action_application" value="<%=request.getParameter("application")%>"/>
<INPUT type="hidden" name="xava_action_module" value="<%=request.getParameter("module")%>"/>
<INPUT type="hidden" name="xava_changed_property"/> 
<INPUT type="hidden" name="xava_focus_property"/> 
<INPUT type="hidden" name="xava_focus_forward"/>
<span id="xava_input_focus_property_id">  
<INPUT type="hidden" name="xava_focus_property_id" value="<%=view.getFocusPropertyId()%>"/>
</span>

<div <%=style.getModuleSpacing()%>>
    <% if (manager.isButtonBarVisible()) { %>
    <div id='xava_button_bar' class='<%=style.getButtonBar()%>'>		
		<jsp:include page="buttonBar.jsp"/>
	</div>
    <% } %>
	
    <% if (messagesOnTop) {  %>    
    <div id='xava_errors' style="display: inline;">
		<jsp:include page="errors.jsp"/>
    </div>
        
	<div id='xava_messages' style="display: inline;">
		<jsp:include page="messages.jsp"/>
	</div>            
    <% } %>
          		 
	<div id='xava_view' <%=manager.isListMode()?"":("class='" + style.getDetail() + "'")%> style='padding-top: 2px;'>
		<jsp:include page='<%=manager.getViewURL()%>'/>		
	</div>    	
    <div style="clear: both; padding-top: 2px;"></div>
	<div id='xava_bottom_buttons' <%=style.getBottomButtonsStyle()%>>	
		<jsp:include page="bottomButtons.jsp"/>
	</div>
    
    <% if (!messagesOnTop) { %>
	<div id='xava_errors'>
		<jsp:include page="errors.jsp"/>
	</div>
        
	<div id='xava_messages'>
		<jsp:include page="messages.jsp"/>
	</div>               
    <% } %>
</div>
</form>
</div>