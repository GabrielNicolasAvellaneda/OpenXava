<%@ include file="imports.jsp"%>

<%@ page import="java.util.Iterator" %>

<jsp:useBean id="context" class="org.openxava.controller.ModuleContext" scope="session"/>
<%
String tabObject = request.getParameter("tabObject");
tabObject = (tabObject == null || tabObject.equals(""))?"xava_tab":tabObject;
org.openxava.tab.Tab tab = (org.openxava.tab.Tab) context.get(request, tabObject);
%>
<div class="prompt">
<fmt:message key="choose_property_add_list_prompt"/>
</div>

<table id="xavaPropertiesList" class="list" width="100%">
<tr>
	<th class=list width="5"></th>
	<th class=list><fmt:message key="property"/></th>
</tr>
<%
int f=0;
for (Iterator it=tab.getRemainingPropertiesNames().iterator(); it.hasNext();) {	
	String property = (String) it.next();
	String cssClass=f%2==0?"pair":"odd";
	f++;
%>
<tr class=<%=cssClass%>>
	<td class=<%=cssClass%> width="5">
		<INPUT type="CHECKBOX" name="selectedProperties" value="<%=property%>"/>	
	</td>
	<td class=<%=cssClass%>>
		<%=property%>	
	</td>
</tr>
<%
}
%>
<tr></tr>
</table>