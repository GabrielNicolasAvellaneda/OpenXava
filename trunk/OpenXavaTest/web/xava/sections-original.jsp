<%@ include file="imports.jsp"%>

<%@ page import="org.openxava.view.meta.MetaView" %>

<jsp:useBean id="context" class="org.openxava.controller.ModuleContext" scope="session"/>
<jsp:useBean id="style" class="org.openxava.web.style.Style" scope="request"/>

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


<table width='100%'>
	<tr><td>
<%-- tmp 
<table width='100%' class=<%=style.getSection()%>>
	<tr class=<%=style.getSection()%>><td class=<%=style.getSection()%>>
--%>	

	<div  class=<%=style.getSectionDiv()%> >

		<table align="left" cellspacing="0" border="0" cellpadding="0"  >
			<tr width="100%">
			
			<td class="LTabLeftLow"  nowrap="true">&nbsp;</td>
          <td class="LTabLow" align="center" valign="middle" nowrap="true" title="Carriers"><a href="http://localhost:8080/openxava/portal/OpenXavaTest/Carriers.psml">Carriers</a></td>
          <td class="LTabRightLow"  nowrap="true">&nbsp;</td>
                  
                        <td class="LTabLeftLow"  nowrap="true">&nbsp;</td>

          <td class="LTabLow" align="center" valign="middle" nowrap="true" title="Customers"><a href="http://localhost:8080/openxava/portal/OpenXavaTest/Customers.psml">Customers</a></td>
          <td class="LTabRightLow"  nowrap="true">&nbsp;</td>			
	
	<% 
	java.util.Iterator itSections = sections.iterator();
	int i=0;
	boolean tmp = false; // tmp
	while (tmp) {
	// tmp while (itSections.hasNext()) {
		MetaView section = (MetaView) itSections.next();
		if (activeSection == i) {
	%>
		<td class=<%=style.getSectionTabLeft()%>></td>
		<td valign="middle" class=<%=style.getSectionTabMiddle()%>>&nbsp;
			<%=section.getLabel(request) %>
		&nbsp;</td>
		<td class=<%=style.getSectionTabRight()%>></td>
	<%	
		}
		else {
	%>
		<td class=<%=style.getSectionTabLeftLow()%>></td>
		<td valign="middle" class=<%=style.getSectionTabMiddleLow()%>>&nbsp;
			<xava:link action='Sections.change' argv='<%="activeSection="+i%>'>
			<%=section.getLabel(request)%>
			</xava:link>        
		&nbsp;</td>
		<td class=<%=style.getSectionTabRightLow()%>></td>
	<%
		}
		i++;
	}
	%>	
	</tr></table>
	</div>
	</td></tr>
	<tr>
		<td class=<%=style.getSectionActive()%>>
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