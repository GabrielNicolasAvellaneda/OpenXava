<%@ include file="imports.jsp"%>

<%@ page import="org.xavax.controller.meta.MetaAction" %>

<jsp:useBean id="context" class="org.xavax.controller.ModuleContext" scope="session"/>
<%

org.xavax.controller.ModuleManager manager = (org.xavax.controller.ModuleManager) context.get(request, "manager", "org.xavax.controller.ModuleManager");
manager.setSession(session);
%>

<table width="100%">
<tr>
<td>

<button name="xava.ACCION_DEFECTO" 
	onclick="executeXavaxAction(<%=manager.getForm()%>, '<%=manager.getDefaultActionQualifiedName()%>')"
	style="padding: 0; border: none; background-color:transparent">
</button>


<%
java.util.Iterator it = manager.getMetaActions().iterator();
while (it.hasNext()) {
	MetaAction action = (MetaAction) it.next();
	if (action.isHidden()) continue;
	if (action.hasImage()) {
	%>
	<xavax:image action="<%=action.getQualifiedName()%>"/>
	<%
	} else {	
	%>
	<xavax:button action="<%=action.getQualifiedName()%>"/>
	<%
	}
}
%>
</td>
<td align="right">

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
	// 'sectionNameAction' only run well if the only sections
	// are list and detail, but at momment that is the case
	String sectionNameAction = action.getName().equals("list")?"list":"detail"; 
	if (sectionNameAction.equals(manager.getSectionName())) {
	%>
	<b><%=action.getLabel(request)%></b>
	<%
	}
	else {	
	%>
	<xavax:link action="<%=action.getQualifiedName()%>"/>
	<%
	}
}
	%>
</td>
</tr>
</table>