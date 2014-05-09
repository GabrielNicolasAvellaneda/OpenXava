<%@ include file="../imports.jsp"%>

<%@page import="java.util.Iterator"%>
<%@page import="java.util.Collection"%>
<%@page import="java.util.Map"%>
<%@page import="org.openxava.controller.meta.MetaAction"%>
<%@page import="org.openxava.web.Ids"%>
<%@page import="org.openxava.controller.meta.MetaControllers"%>
<%@page import="org.openxava.util.Is"%>
<%@page import="org.openxava.util.Maps"%>
<%@page import="org.openxava.web.Actions"%>
<%@page import="org.openxava.util.XavaPreferences"%>
<%@page import="org.openxava.view.View"%>
<%@page import="org.openxava.model.meta.MetaProperty"%>
<%@page import="org.openxava.web.WebEditors"%>

<jsp:useBean id="context" class="org.openxava.controller.ModuleContext" scope="session"/>
<jsp:useBean id="errors" class="org.openxava.util.Messages" scope="request"/>
<jsp:useBean id="style" class="org.openxava.web.style.Style" scope="request"/>

<%
String collectionName = request.getParameter("collectionName");
String viewObject = request.getParameter("viewObject");
String viewName = viewObject + "_" + collectionName;
View view = (View) context.get(request, viewObject);
View subview = view.getSubview(collectionName);
String idCollection = org.openxava.web.Collections.id(request, collectionName);
String propertyPrefixAccumulated = request.getParameter("propertyPrefix");
String propertyPrefix = propertyPrefixAccumulated == null?collectionName + ".":propertyPrefixAccumulated + collectionName + ".";


String rowStyle = "border-bottom: 1px solid;";
boolean resizeColumns = style.allowsResizeColumns() && XavaPreferences.getInstance().isResizeColumns();
String browser = request.getHeader("user-agent");
boolean scrollSupported = !(browser != null && (browser.indexOf("MSIE 6") >= 0 || browser.indexOf("MSIE 7") >= 0));
String styleOverflow = org.openxava.web.Lists.getOverflow(browser, subview.getMetaPropertiesList());
%>
<div class="<%=style.getElementCollection()%>">  
<% if (resizeColumns && scrollSupported) { %> 
<div class="<xava:id name='collection_scroll'/>" style="<%=styleOverflow%>">
<% } %>
<table id="<xava:id name='<%=idCollection%>'/>" class="<%=style.getList()%>" <%=style.getListCellSpacing()%> style="<%=style.getListStyle()%>">
<tr class="<%=style.getListHeader()%>">
	<th class=<%=style.getListHeaderCell()%> width="5">
<%
	// Heading
Iterator it = subview.getMetaPropertiesList().iterator();
for (int columnIndex=0; it.hasNext(); columnIndex++) {
	MetaProperty p = (MetaProperty) it.next();
	String label = p.getQualifiedLabel(request);
	int columnWidth = subview.getCollectionColumnWidth(columnIndex);
	String width = columnWidth<0 || !resizeColumns?"":"width: " + columnWidth + "px";
%>
	<th class=<%=style.getListHeaderCell()%> style="padding-right: 0px">
		<div id="<xava:id name='<%=idCollection%>'/>_col<%=columnIndex%>" class="<%=((resizeColumns)?("xava_resizable"):(""))%>" style="overflow: hidden; <%=width%>" >
		<%if (resizeColumns) {%><nobr><%}%>
		<%=label%>&nbsp;
		<%if (resizeColumns) {%></nobr><%}%>
		</div>
	</th>
<%
	}
%>
</tr>

<%
	// Values
Collection values = subview.getCollectionValues();
if (values == null) values = java.util.Collections.EMPTY_LIST;
int rowCount = values.size() + 2;
for (int f=0; f < rowCount; f++) {
	String cssClass=f%2==0?style.getListPair():style.getListOdd();
	String cssCellClass=f%2==0?style.getListPairCell():style.getListOddCell();
	String idRow = Ids.decorate(request, propertyPrefix) + f;	
	String events=f%2==0?style.getListPairEvents():style.getListOddEvents();
	String newRowStyle = f== rowCount - 1?"display: none;":"";
	String lastRowEvent = f >= rowCount - 2?"onchange='elementCollectionEditor.onChangeRow(this, "+  f + ")'":"";
%>
<tr id="<%=idRow%>" class="<%=cssClass%>" <%=events%> style="border-bottom: 1px solid; <%=newRowStyle%>">
<td class="<%=cssCellClass%>" style="vertical-align: middle;text-align: center;padding-right: 2px; <%=style.getListCellStyle()%>">
<nobr>
<xava:action action="CRUD.delete" argv='<%="row="+f + ",viewObject="+viewName%>'/>
</nobr>
</td>
<%
	it = subview.getMetaPropertiesList().iterator();	
	for (int columnIndex = 0; it.hasNext(); columnIndex++) { 
		MetaProperty p = (MetaProperty) it.next();
		String cellStyle = style.getListCellStyle();
		int columnWidth = subview.getCollectionColumnWidth(columnIndex);
		String width = columnWidth<0 || !resizeColumns?"":"width: " + columnWidth + "px"; 
		String propertyName = collectionName + "." + f + "." + p.getName();
		boolean throwPropertyChanged = subview.throwsPropertyChanged(p.getName()); 
%>
	<td class="<%=cssCellClass%>" style="<%=cellStyle%>; padding-right: 0px">
		<%if (resizeColumns) {%><nobr><%}%>
		<span <%=lastRowEvent%>> 
		<xava:editor property="<%=propertyName%>" throwPropertyChanged="<%=throwPropertyChanged%>"/>
		</span>
	 	<%if (resizeColumns) {%></nobr><%}%>	 
	</td>		
<%
	}
}
%>
</tr>
<% 
int additionalTotalsCount = subview.getCollectionTotalsCount();
for (int i=0; i<additionalTotalsCount; i++) {
%>
<tr class="<%=style.getTotalRow()%>">
<td style="<%=style.getTotalEmptyCellStyle()%>"/>
<td style="<%=style.getTotalEmptyCellStyle()%>"/>
<%
it = subview.getMetaPropertiesList().iterator();
for (int c = 0; it.hasNext(); c++) {	
	MetaProperty p = (MetaProperty) it.next();
	String align =p.isNumber() && !p.hasValidValues()?"text-align: right; ":"";
	String cellStyle = align + style.getTotalCellStyle(); 
	
	if (subview.hasCollectionTotal(i, c)) {
		String ftotal = WebEditors.format(request, p, subview.getCollectionTotal(i, c), errors, view.getViewName(), true);		
	%> 	
	<td class="<%=style.getTotalCell()%>" style="<%=cellStyle%>">
	<div class=" <xava:id name='<%=idCollection%>'/>_col<%=c%>" style="overflow: hidden;">	
	<%=ftotal%>&nbsp;
	</nobr>	
	</div>	
	</td>
	<%	
	}
	else if (subview.hasCollectionTotal(i, c + 1)) { 
	%>
	<td style="<%=style.getTotalLabelCellStyle()%>">	
		<%=subview.getCollectionTotalLabel(i, c + 1)%>&nbsp;
	</td>
	<%	
	}
	else {
	%>	 
	<td style="<%=style.getTotalEmptyCellStyle()%>"/>
	<%		
	}	
}
%>
</tr>
<%
} // for additionalTotalsCount 
%>
</table>
<% if (resizeColumns && scrollSupported) { %>
</div>
<% } %>
</div> 