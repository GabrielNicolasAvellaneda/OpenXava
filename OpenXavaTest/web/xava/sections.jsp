<%@ include file="imports.jsp"%>

<%@ page import="org.openxava.view.meta.MetaView" %>

<jsp:useBean id="context" class="org.openxava.controller.ModuleContext" scope="session"/>

<%
String viewObject = request.getParameter("viewObject");
viewObject = (viewObject == null || viewObject.equals(""))?"xava_view":viewObject;
org.openxava.view.View view = (org.openxava.view.View) context.get(request, viewObject);
java.util.Collection sections = view.getSections();

Object oactiveSection = context.get(request, "xava_activeSection");
int activeSection = oactiveSection==null?0:((Integer) oactiveSection).intValue();
if (activeSection >= sections.size()) {
	activeSection = 0;
	context.put(request, "xava_activeSection", new Integer(0));
}
view.setActiveSection(activeSection);
%>

<table width='100%' class=section>
	<tr class=section><td class=section>

	<div  class="Jetspeed" >
		<table align="left" cellspacing="0" border="0" cellpadding="0"  >
			<tr width="100%">
	
	<% 
	java.util.Iterator itSections = sections.iterator();
	int i=0;
	while (itSections.hasNext()) {
		MetaView section = (MetaView) itSections.next();
		if (activeSection == i) {
	%>
		<td class="TabLeft"></td>
		<td valign="middle"   class="TabMiddle" style="background-color: #FFCC00;">&nbsp;
			<%=section.getLabel(request) %>
		&nbsp;</td>
		<td   class="TabRight"  ></td>
	<%	
		}
		else {
	%>
		<td class="TabLeftLow"></td>
		<td valign="middle" class="TabMiddleLow" style="background-color: #FFCC00;" >&nbsp;
			<xava:link action="Sections.change" argv="<%="activeSection="+i%>">
			<%=section.getLabel(request)%>
			</xava:link>        
		&nbsp;</td>
		<td class="TabRightLow"></td>
	<%
		}
		i++;
	}
	%>	
	</tr></table></div>
	</td></tr>
	<tr>
		<td class=activeSection>
		<%
			String viewName = viewObject + "_section" + activeSection;
			context.put(request, viewName, view.getSectionView(activeSection));			
		%>	
		<jsp:include page="detail.jsp"> 
			<jsp:param name="viewObject" value="<%=viewName%>" />
		</jsp:include>		
		</td>
	</tr>
</table>
<br>