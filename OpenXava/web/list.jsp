<%@ include file="imports.jsp"%>

<%@ page import="org.openxava.tab.impl.IXTableModel" %>
<%@ page import="org.openxava.util.Strings" %>
<%@ page import="org.openxava.util.XavaPreferences" %>
<%@ page import="org.openxava.model.meta.MetaProperty" %>
<%@ page import="org.openxava.web.WebEditors" %>

<jsp:useBean id="errors" class="org.openxava.util.Messages" scope="request"/>
<jsp:useBean id="context" class="org.openxava.controller.ModuleContext" scope="session"/>
<jsp:useBean id="style" class="org.openxava.web.Style" scope="request"/>

<%
org.openxava.controller.ModuleManager manager = (org.openxava.controller.ModuleManager) context.get(request, "manager", "org.openxava.controller.ModuleManager");
String tabObject = request.getParameter("tabObject");
tabObject = (tabObject == null || tabObject.equals(""))?"xava_tab":tabObject;
org.openxava.tab.Tab tab = (org.openxava.tab.Tab) context.get(request, tabObject);
String action=request.getParameter("rowAction");
action=action==null?manager.getEnvironment().getValue("XAVA_LIST_ACTION"):action;
String sfilter = request.getParameter("filter");
boolean filter = !"false".equals(sfilter);
String lastRow = request.getParameter("lastRow");
%>

<% if (tab.isTitleVisible()) { %>
<table width="100%" id="list-title">
<tr><td class=<%=style.getListTitle()%>>
<%=tab.getTitle()%>
</td></tr>
</table>
<% } %>

<table id="list" class=<%=style.getList()%> width="100%" <%=style.getListCellSpacing()%>>
<tr>
<th class=<%=style.getListHeader()%> width="60"><xava:image action="List.customize"/></th>
<th class=<%=style.getListHeader()%> width="5">
<% if (tab.isCustomize()) { %><xava:image action="List.addColumns"/><% } %>
</th>
<%
tab.reset();
java.util.Collection properties = tab.getMetaProperties();
java.util.Iterator it = properties.iterator();
int columnIndex = 0;
while (it.hasNext()) {
	MetaProperty property = (MetaProperty) it.next();
%>
<th class=<%=style.getListHeader()%>>
<% if (tab.isCustomize()) { %><xava:image action="List.moveColumnToLeft" argv='<%="columnIndex="+columnIndex%>'/><% } %>
<%
	if (property.isCalculated()) {		
%>
<%=property.getLabel(request)%>
<%
	} else {
%>
<xava:link action='List.orderBy' argv='<%="property="+property.getQualifiedName()%>'><%=property.getLabel(request)%></xava:link>
<%
		if (tab.isOrderAscending(property.getQualifiedName())) {
%>
<img src="<%=request.getContextPath()%>/xava/images/<%=style.getAscendingImage()%>" alt="Ordenado ascendentemente" border="0" align="middle"/>
<%
		}
%>
<%
		if (tab.isOrderDescending(property.getQualifiedName())) {
%>
<img src="<%=request.getContextPath()%>/xava/images/<%=style.getDescendingImage()%>" alt="Ordenado descendente" border="0" align="middle"/>
<%
		}
%>
	
<% 
   }
   
   if (tab.isCustomize()) { %>
	<xava:image action="List.moveColumnToRight" argv='<%="columnIndex="+columnIndex%>'/>
	<xava:image action="List.removeColumn" argv='<%="columnIndex="+columnIndex%>'/>
<% }
 
%>
</th>
<%
	columnIndex++;
}
%>
</tr>
<% if (filter) { %>
<tr class=<%=style.getListSubheader()%>>
<th class=<%=style.getListSubheader()%> width="60">
<xava:button action="List.filter"/>
</th>
<th class=<%=style.getListSubheader()%> width="5"></th>
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
<th class=<%=style.getListSubheader()%> align="left">
<jsp:include page="comparatorsValidValuesCombo.jsp">
	<jsp:param name="validValues" value="<%=property.getValidValuesLabels(request)%>" />
	<jsp:param name="value" value="<%=value%>" />
</jsp:include>		
	<%	
		}
		else if (isBoolean) { 
	%>
<th class=<%=style.getListSubheader()%> align="left">
<jsp:include page="comparatorsBooleanCombo.jsp">
	<jsp:param name="comparator" value="<%=comparator%>" />
</jsp:include>
	<% } else { // Not boolean %>
<th class=<%=style.getListSubheader()%> align="left">
<% 
String urlComparatorsCombo = "comparatorsCombo.jsp" // in this way because websphere 6 has problems with jsp:param
	+ "?comparator=" + comparator
	+ "&isString=" + isString
	+ "&isDate=" + isDate;
%>
<jsp:include page="<%=urlComparatorsCombo%>" />
<input name="conditionValues" class=<%=style.getEditor()%> type="text" maxlength="<%=maxLength%>" size="<%=length%>" value="<%=value%>"/>
	<% } %>
</th>
<% 
	}
	else {
%>
<th class=<%=style.getListSubheader()%>></th>
<%
	} 
} // while	
%>
</tr>
<% } /* if (filter) */ %>
<%
int totalSize = 0;
if (tab.isRowsHidden()) {
%>
	<tr><td align="center">
	<xava:link action="List.showRows"/>
	</td></tr>
<%
}
else {
	
IXTableModel model = tab.getTableModel(); 
totalSize = tab.getTotalSize();
if (totalSize > 0) {
for (int f=tab.getInitialIndex(); f<model.getRowCount() && f < tab.getFinalIndex(); f++) {
	String checked=tab.isSelected(f)?"checked='true'":"";
	String cssClass=f%2==0?style.getListPair():style.getListOdd();	
	String cssStyle = tab.getStyle(request.getLocale(), f);
%>
<tr class=<%=cssClass%>>
	<td class=<%=cssClass%> style='vertical-align: middle;text-align: center'>
<xava:link action='<%=action%>' argv='<%="row="+f%>'/>
	</td>
	<td class=<%=cssClass%>>
	<INPUT type="CHECKBOX" name="selected" value="<%=f%>" <%=checked%>/>
	</td>	
<%
	for (int c=0; c<model.getColumnCount(); c++) {
		MetaProperty p = tab.getMetaProperty(c);
		String align =p.isNumber() && !p.hasValidValues()?"style='vertical-align: middle;text-align: right'":"style='vertical-align: middle;'";
		String fvalue = null;
		if (p.hasValidValues()) {
			fvalue = p.getValidValueLabel(request, model.getValueAt(f, c));
		}
		else {
			fvalue = WebEditors.format(request, p, model.getValueAt(f, c), errors);
		}
%>
	<td class=<%=cssClass%> <%=align%>>
	<% if (cssStyle != null) { %> <div id="cellStyle" class=<%=cssStyle%>> <% } %>
		<%=fvalue%>
	<% if (cssStyle != null) { %> </div> <% } %>
	</td>
<%
	}
%>
</tr>
<%
}
}
else {
%>
<tr><td class=<%=style.getMessages()%>>
<b><fmt:message key="no_objects"/></b>
</td></tr>
<%
}
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
<% if (!tab.isRowsHidden()) { %>
<table width="100%" class=<%=style.getListInfo()%>>
<tr class=<%=style.getListInfo()%>>
<td class=<%=style.getListInfo()%>>
<%
int last=tab.getLastPage();
int current=tab.getPage();
if (current > 1) {
%>
<xava:image action='List.goPreviousPage'/>
<% } 
for (int i=1; i<=last; i++) {
if (i == current) {
%>	 
 <b><%=i%></b>
<% } else { %>
 <xava:link action='List.goPage' argv='<%="page="+i%>'><%=i%></xava:link>
<% }} 
if (!tab.isLastPage()) {
%>
 <xava:image action='List.goNextPage'/> 
<% } %>	 
</td>
<td  align='right' class=<%=style.getListInfo()%>>
<% if (XavaPreferences.getInstance().isShowCountInList()) { %>
<fmt:message key="list_count">
	<fmt:param><%=totalSize%></fmt:param> 
</fmt:message>
<% } %>
(<xava:link action="List.hideRows"/>)
</td>
</tr>
</table>
<% } %>
