<%@ include file="imports.jsp"%>

<%@ page import="org.openxava.controller.meta.MetaAction" %>
<%@ page import="org.openxava.util.XavaPreferences"%>
<%@ page import="org.openxava.util.Is"%>

<jsp:useBean id="context" class="org.openxava.controller.ModuleContext" scope="session"/>
<jsp:useBean id="style" class="org.openxava.web.style.Style" scope="request"/>

<%
org.openxava.controller.ModuleManager manager = (org.openxava.controller.ModuleManager) context.get(request, "manager", "org.openxava.controller.ModuleManager");
manager.setSession(session);
boolean onBottom = false;
String mode = request.getParameter("xava_mode"); 
if (mode == null) mode = manager.isSplitMode()?"detail":manager.getModeName();
boolean headerButtonBar = !manager.isSplitMode() || mode.equals("list");  
String helpImage = style.getHelpImage().startsWith("xava/")?request.getContextPath() + "/" + style.getHelpImage():style.getHelpImage();

if (manager.isButtonBarVisible()) {
%>
	<div class="<%=style.getButtonBar()%>">
	<%
	java.util.Iterator it = manager.getMetaActions().iterator();
	boolean showLabels = XavaPreferences.getInstance().isShowLabelsForToolBarActions(); 
	while (it.hasNext()) {
		MetaAction action = (MetaAction) it.next();
		if (action.isHidden()) continue;
		if (action.appliesToMode(mode) && action.hasImage()) {
		%>
		<jsp:include page="barButton.jsp">
			<jsp:param name="action" value="<%=action.getQualifiedName()%>"/>
		</jsp:include>		
		<%
		} 
	}
	%>
	
	<span style="float: right"> 
	<%
	java.util.Stack previousViews = (java.util.Stack) context.get(request, "xava_previousViews"); 
	if (headerButtonBar && previousViews.isEmpty()) { 
		java.util.Iterator itSections = manager.getMetaActionsMode().iterator();
		while (itSections.hasNext()) {
			MetaAction action = (MetaAction) itSections.next();
			if (action.isHidden()) continue;
			String modeNameAction = action.getName().startsWith("detail")?"detail":action.getName();
			if (modeNameAction.equals(manager.getModeName())) {			
			%>
			<span class="<%=style.getButtonBarActiveModeButton()%>">
				<a href="">
					&nbsp;&nbsp;
					<%=action.getLabel(request)%>
					&nbsp;&nbsp;
				</a>
			</span>
			<%
			}
			else {	
			%>
			<span class="<%=style.getButtonBarModeButton()%>">			
				<xava:link action="<%=action.getQualifiedName()%>">
					&nbsp;&nbsp;		 			
					<%=action.getLabel(request)%>
					&nbsp;&nbsp;
				</xava:link>
			</span>
			<%
			}
		}
	}	

	String language = request.getLocale().getLanguage();
	String href = XavaPreferences.getInstance().getDefaultHelpPrefix() + language;
	String target = XavaPreferences.getInstance().isHelpInNewWindow() ? "_blank" : "";
	if (!Is.empty(XavaPreferences.getInstance().getHelpPrefix())) { 
		href = 
			"/" + manager.getApplicationName() + "/" + 
			XavaPreferences.getInstance().getHelpPrefix() +
			manager.getModuleName() +
			"_" + language + 
			XavaPreferences.getInstance().getHelpSuffix();
	} 
	%>
		<span class="<%=style.getHelp()%>"> 
			<a href="<%=href%>" target="<%=target%>"><img src="<%=helpImage%>"/></a> 				
		</span>
	</span>		

	</div>
	
<% } // end isButtonBarVisible %>