<%@ include file="imports.jsp"%>

<%@ page import="org.openxava.controller.meta.MetaAction" %>

<jsp:useBean id="context" class="org.openxava.controller.ModuleContext" scope="session"/>
<jsp:useBean id="style" class="org.openxava.web.Style" scope="request"/>

<%
org.openxava.controller.ModuleManager manager = (org.openxava.controller.ModuleManager) context.get(request, "manager", "org.openxava.controller.ModuleManager");
manager.setSession(session);
%>

<table width="100%">
<tr>
<td style='vertical-align: middle'>

<button name="xava.DEFAULT_ACTION" 
	onclick="executeXavaAction(false, false, <%=manager.getForm()%>, '<%=manager.getDefaultActionQualifiedName()%>')"
	style="padding: 0; border: none; background-color:transparent">
</button>

<%
java.util.Iterator it = manager.getMetaActions().iterator();
while (it.hasNext()) {
	MetaAction action = (MetaAction) it.next();
	if (action.isHidden()) continue;
	if (action.hasImage()) {
	%>
	<xava:image action="<%=action.getQualifiedName()%>"/>
	<%
	} else {	
	%>
	<xava:button action="<%=action.getQualifiedName()%>"/>
	<%
	}
}
%>
</td>

<td align="right" style='vertical-align: middle' class=<%=style.getMode()%>>
<%
java.util.Iterator itSections = manager.getMetaActionsSections().iterator();
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
	// 'modeNameAction' only run well if the only modes
	// are list and detail, but at momment that is the case
	String modeNameAction = action.getName().equals("list")?"list":"detail"; 
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
	%>
</td>
</tr>
</table>