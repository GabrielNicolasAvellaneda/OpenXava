<%@ include file="imports.jsp"%>

<%@ page import="java.util.Iterator" %>

<jsp:useBean id="context" class="org.openxava.controller.ModuleContext" scope="session"/>
<jsp:useBean id="style" class="org.openxava.web.style.Style" scope="request"/>

<%
String tabObject = request.getParameter("tabObject");
tabObject = (tabObject == null || tabObject.equals(""))?"xava_tab":tabObject;
org.openxava.tab.Tab tab = (org.openxava.tab.Tab) context.get(request, tabObject);
%>
<div class="portlet-form-label">
<fmt:message key="choose_property_add_list_prompt"/>
</div>

<table id="xavaPropertiesList" class='<%=style.getList()%>' width="100%">
<tr>
	<th class=<%=style.getList()%> width="5"></th>
	<th class=<%=style.getList()%>><fmt:message key="property"/></th>
</tr>
<%
int f=0;
for (Iterator it=tab.getRemainingPropertiesNames().iterator(); it.hasNext();) {	
	String property = (String) it.next();
	String cssClass=f%2==0?style.getListPair():style.getListOdd();
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