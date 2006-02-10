<%@ include file="imports.jsp"%>

<%@ page import="org.openxava.view.meta.MetaView" %>

<jsp:useBean id="context" class="org.openxava.controller.ModuleContext" scope="session"/>
<jsp:useBean id="style" class="org.openxava.web.style.Style" scope="request"/>

<%
String viewObject = request.getParameter("viewObject");
viewObject = (viewObject == null || viewObject.equals(""))?"xava_view":viewObject;
org.openxava.view.View view = (org.openxava.view.View) context.get(request, viewObject);
java.util.Collection sections = view.getSections();
int activeSection = view.getActiveSection(); 
%>

<table width='100%' cellspacing="0" border="0" cellpadding="0">
	<tr><td>

<div class=<%=style.getSection()%>>
	<table border="0" cellpadding="0" cellspacing="0">
    	<tr>
    		<%=style.getSectionBarLeftDecoration()%>    		
	<% 
	java.util.Iterator itSections = sections.iterator();
	int i=0;
	while (itSections.hasNext()) {
		MetaView section = (MetaView) itSections.next();
		if (activeSection == i) {
	%>        
			<% if (style.sectionTabHasDecoration()) { %>
			<td class=<%=style.getSectionTabLeft()%> nowrap="true">&nbsp;</td>
			<% } %>
			<td class=<%=style.getSectionTabMiddle()%> style="vertical-align: middle; text-align: center;" nowrap="true">
			<%=section.getLabel(request)%>
			</td>
			<% if (style.sectionTabHasDecoration()) { %>
			<td class=<%=style.getSectionTabRight()%> nowrap="true">&nbsp;</td>    	
			<% } %>
    <%
		}
		else {
    %>
    		<% if (style.sectionTabHasDecoration()) { %>
			<td class=<%=style.getSectionTabLeftLow()%> nowrap="true">&nbsp;</td>
			<% } %>
			<td class=<%=style.getSectionTabMiddleLow()%> style="vertical-align: middle; text-align: center;" nowrap="true">
				<%
				String viewObjectArgv = "xava_view".equals(viewObject)?"":",viewObject=" + viewObject;
				%>
				<xava:link action='Sections.change' argv='<%="activeSection=" + i + viewObjectArgv%>' cssClass='<%=style.getSectionLink()%>'>
				<%=section.getLabel(request)%>
				</xava:link>                  	
			</td>
			<% if (style.sectionTabHasDecoration()) { %>
			<td class=<%=style.getSectionTabRightLow()%> nowrap="true">&nbsp;</td>          
			<% } %>
  	<%   	
		}
		i++;
  	} 
  	%>                
	</tr>
  </table>
</div>                  
	
	</td></tr>
	<tr><td class=<%=style.getSectionActive()%>>
		<%
			String viewName = viewObject + "_section" + activeSection;
			context.put(request, viewName, view.getSectionView(activeSection));			
		%>	
		<table width='100%' cellspacing="0" border="0" cellpadding="2"><tr><td>
		<jsp:include page="detail.jsp"> 
			<jsp:param name="viewObject" value="<%=viewName%>" />
		</jsp:include>		
		</td></tr></table>
	</td></tr>	
</table>
<br>