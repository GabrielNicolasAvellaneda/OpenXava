<%@ include file="imports.jsp"%>

<%@ page import="org.xavax.tab.impl.IXTableModel" %>
<%@ page import="org.xavax.util.Strings" %>
<%@ page import="org.xavax.model.meta.MetaProperty" %>
<%@ page import="org.xavax.web.WebEditors" %>

<jsp:useBean id="errors" class="org.xavax.util.Messages" scope="request"/>

<jsp:useBean id="context" class="org.xavax.controller.ModuleContext" scope="session"/>
<%
org.xavax.controller.ModuleManager manager = (org.xavax.controller.ModuleManager) context.get(request, "manager", "org.xavax.controller.ModuleManager");
String tabObject = request.getParameter("tabObject");
tabObject = (tabObject == null || tabObject.equals(""))?"xavax_tab":tabObject;
org.xavax.tab.Tab tab = (org.xavax.tab.Tab) context.get(request, tabObject);
String action=request.getParameter("rowAction");
action=action==null?manager.getEnvironment().getValue("XAVAX_LIST_ACTION"):action;
String sfilter = request.getParameter("filter");
boolean filter = !"false".equals(sfilter);
String lastRow = request.getParameter("lastRow");
%>

<% if (tab.isTitleVisible()) { %>
<table width="100%" id="list-title">
<tr><td align='center'>
<%=tab.getTitle()%>
</td></tr>
</table>
<% } %>

<table id="list" class="list" width="100%">
<tr>
<th class=list width="60"><xavax:image action="List.customize"/></th>
<th class=list width="5">
<% if (tab.isCustomize()) { %><xavax:image action="List.addColumns"/><% } %>
</th>
<%
tab.reset();
IXTableModel model = tab.getTableModel();
java.util.Collection properties = tab.getMetaProperties();
java.util.Iterator it = properties.iterator();
int columnIndex = 0;
while (it.hasNext()) {
	MetaProperty property = (MetaProperty) it.next();
%>
<th class=list>
<% if (tab.isCustomize()) { %><xavax:image action="List.moveColumnToLeft" argv='<%="columnIndex="+columnIndex%>'/><% } %>
<%
	if (property.isCalculated()) {		
%>
<%=property.getLabel(request)%>
<%
	} else {
%>
<xavax:link action='List.orderBy' argv='<%="property="+property.getQualifiedName()%>'><%=property.getLabel(request)%></xavax:link>
<%
		if (tab.isOrderAscending(property.getQualifiedName())) {
%>
<img src="images/ascending.gif" alt="Ordenado ascendentemente" border="0" align="middle"/>
<%
		}
%>
<%
		if (tab.isOrderDescending(property.getQualifiedName())) {
%>
<img src="images/descending.gif" alt="Ordenado descendente" border="0" align="middle"/>
<%
		}
%>
	
<% 
   }
   
   if (tab.isCustomize()) { %>
	<xavax:image action="List.moveColumnToRight" argv='<%="columnIndex="+columnIndex%>'/>
	<xavax:image action="List.removeColumn" argv='<%="columnIndex="+columnIndex%>'/>
<% }
 
%>
</th>
<%
	columnIndex++;
}
%>
</tr>
<% if (filter) { %>
<tr class=search>
<th class=search width="60">
<xavax:button action="List.filter"/>
</th>
<th class=search width="5"></th>
<%
it = properties.iterator();
String [] conditionValues = tab.getConditionValues();
String [] conditionComparators = tab.getConditionComparators();
int iConditionValues = 0;
while (it.hasNext()) {
	MetaProperty property = (MetaProperty) it.next();
	if (!property.isCalculated()) {
		boolean isValidValues = property.hasValidValues();
		boolean isString = "java.lang.String".equals(property.getType().getName());
		boolean isBoolean = "boolean".equals(property.getType().getName()) || "java.lang.Boolean".equals(property.getType().getName());
		boolean isDate = "java.util.Date".equals(property.getType().getName());
		int maxLength = property.getSize();
		int length = Math.min(isString?property.getSize()/2:property.getSize(), 20);
		String value= conditionValues==null?"":conditionValues[iConditionValues];
		String comparator = conditionComparators==null?"":Strings.change(conditionComparators[iConditionValues], "=", "eq");
		iConditionValues++;
		if (isValidValues) {
	%>	
<th class=search align="left">
<jsp:include page="comparatorsValidValuesCombo.jsp">
	<jsp:param name="validValues" value="<%=property.getValidValuesLabels(request)%>" />
	<jsp:param name="value" value="<%=value%>" />
</jsp:include>		
	<%	
		}
		else if (isBoolean) { 
	%>
<th class=search align="left">
<jsp:include page="comparatorsBooleanCombo.jsp">
	<jsp:param name="comparator" value="<%=comparator%>" />
</jsp:include>
	<% } else { // Not boolean %>
<th class=search align="left">
<jsp:include page="comparatorsCombo.jsp">
	<jsp:param name="comparator" value="<%=comparator%>" />
	<jsp:param name="isString" value="<%=isString%>" />
	<jsp:param name="isDate" value="<%=isDate%>" />
</jsp:include>
<input name="conditionValues" class=editor type="text" maxlength="<%=maxLength%>" size="<%=length%>" value="<%=value%>"/>
	<% } %>
</th>
<% 
	}
	else {
%>
<th class=search></th>
<%
	} 
} // while	
%>
</tr>
<% } /* if (filtrar) */ %>
<%
int totalSize = tab.getTotalSize();
if (totalSize > 0) {
for (int f=tab.getInitialIndex(); f<model.getRowCount() && f < tab.getFinalIndex(); f++) {
	String checked=tab.isSelected(f)?"checked='true'":"";
	String cssClass=f%2==0?"pair":"odd";
%>
<tr class=<%=cssClass%>>
	<td class=<%=cssClass%> align='center'>
<xavax:link action='<%=action%>' argv='<%="row="+f%>'/>
	</td>
	<td class=<%=cssClass%>>
	<INPUT type="CHECKBOX" name="selected" value="<%=f%>" <%=checked%>/>
	</td>	
<%
	for (int c=0; c<model.getColumnCount(); c++) {
		MetaProperty p = tab.getMetaProperty(c);
		String align = p.isNumber() && !p.hasValidValues()?"align='right'":"";
		String fvalue = null;
		if (p.hasValidValues()) {
			fvalue = p.getValidValueLabel(request, model.getValueAt(f, c));
		}
		else {
			fvalue = WebEditors.format(request, p, model.getValueAt(f, c), errors);
		}
%>
	<td class=<%=cssClass%> <%=align%>><%=fvalue%></td>
<%
	}
%>
</tr>
<%
}
}
else {
%>
<tr><td class=mensajes>
<b><fmt:message key="no_objects"/></b>
</td></tr>
<%
}
if (lastRow != null) {
%>
<tr>
	<jsp:include page="<%=lastRow%>"/>
</tr>
<%
}
%>
</table>

<table width="100%" class="list-info">
<tr>

<td>
<fmt:message key="list_count">
	<fmt:param><%=totalSize%></fmt:param> 
</fmt:message>
</td>
<td align='right'>
<%
int last=tab.getLastPage();
int current=tab.getPage();
if (current > 1) {
%>
<xavax:image action='List.goPreviousPage'/>
<% } 
for (int i=1; i<=last; i++) {
if (i == current) {
%>	 
<b> <%=i%> </b>
<% } else { %>
 <xavax:link action='List.goPage' argv='<%="page="+i%>'><%=i%></xavax:link>
<% }} 
if (!tab.isLastPage()) {
%>
 <xavax:image action='List.goNextPage'/> 
<% } %>	 
</td>
</tr>
</table>

