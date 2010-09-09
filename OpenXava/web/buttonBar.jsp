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
String buttonBarClass = headerButtonBar?style.getButtonBar2():"";  

if (manager.isButtonBarVisible()) {
%>
 
	<table width="100%" <%=style.getButtonBarSpacing()%> class="<%=buttonBarClass%>" style="<%=style.getButtonBarStyle()%>">
	<tr>
	<td class=<%=style.getButtonBarStart(onBottom)%> style="<%=style.getButtonBarStartStyle()%>" width=1>&nbsp;</td>
	<td style='vertical-align: middle' class="<%=style.getButtonBarMiddle(onBottom)%>" style="<%=style.getButtonBarMiddleStyle()%>">
	
	<%
	java.util.Iterator it = manager.getMetaActions().iterator();
	while (it.hasNext()) {
		MetaAction action = (MetaAction) it.next();
		if (action.isHidden()) continue;
		if (action.appliesToMode(mode) && action.hasImage()) {  
		%>
		<xava:image action="<%=action.getQualifiedName()%>"/>
		<%
		} 
	}
	%>
	</td>
	
	<td align="right" style='vertical-align: middle' class="<%=style.getMode(onBottom)%>">
	&nbsp;
	<%
	java.util.Stack previousViews = (java.util.Stack) context.get(request, "xava_previousViews"); 
	if (headerButtonBar && previousViews.isEmpty()) { 
		java.util.Iterator itSections = manager.getMetaActionsMode().iterator();
		boolean firstTime = true;
		while (itSections.hasNext()) {
			MetaAction action = (MetaAction) itSections.next();
			if (action.isHidden()) continue;
			if (firstTime) firstTime=false;
			else {
			%>
			-
			<%
			}
			String modeNameAction = action.getName().startsWith("detail")?"detail":action.getName(); 
			if (modeNameAction.equals(manager.getModeName())) {			
			%>
			<b><%=action.getLabel(request)%></b>
			<%
			}
			else {	
			%>
			<xava:link action="<%=action.getQualifiedName()%>"/>
			<%
			}
		}
	}	
		%>
	</td>
	<td align="right" style="width: 20px; height: 16px;">
		<%
		String language = request.getLocale().getLanguage();
		String href = "http://openxava.wikispaces.com/help_" + language;
		String target = XavaPreferences.getInstance().isHelpInNewWindow() ? "_blank" : "";
		if (!Is.empty(XavaPreferences.getInstance().getHelpPrefix())) { 
			href = 
				(XavaPreferences.getInstance().getHelpPrefix().startsWith("http:") ? "" :
					"/" + manager.getApplicationName() + "/") + 
				XavaPreferences.getInstance().getHelpPrefix() +
				manager.getModuleName() +
				"_" + language + 
				XavaPreferences.getInstance().getHelpSuffix();
		} 
		%>
		<a href="<%=href%>" target="<%=target%>"><img src="/<%=manager.getApplicationName()%>/xava/images/help.gif"/></a>
	</td>		
	<td class="<%=style.getButtonBarEnd(onBottom)%>" style="<%=style.getButtonBarEndStyle()%>" width=1>&nbsp;</td>
	</tr>
	</table>
<% } // end isButtonBarVisible %>