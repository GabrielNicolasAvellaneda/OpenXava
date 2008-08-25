<table id=<%=idCollection%> class="<%=style.getList()%>" width="100%" <%=style.getListCellSpacing()%> <%=style.getListStyle()%>>
<tr id="xava_tr_list" class="<%=style.getListHeader()%>">
	<% if (lineAction != null) { %>	
	<th class=<%=style.getListHeaderCell()%>></th>
	<% } %>	
	<th class=<%=style.getListHeaderCell()%> width="5"></th>
<%
// Heading
Iterator it = subview.getMetaPropertiesList().iterator();
while (it.hasNext()) {
	MetaProperty p = (MetaProperty) it.next();
%>
	<th class=<%=style.getListHeaderCell()%>><%=p.getLabel(request)%>&nbsp;</th>
<%
}
%>
</tr>

<%
// Values
Collection aggregates = subview.getCollectionValues();
if (aggregates == null) aggregates = java.util.Collections.EMPTY_LIST;
Iterator itAggregates = aggregates.iterator();
int f=0;
while (itAggregates.hasNext()) {
	Map row = (Map) itAggregates.next();
	String cssClass=f%2==0?style.getListPair():style.getListOdd();
	String cssCellClass=f%2==0?style.getListPairCell():style.getListOddCell();
	String selectedClass = "";
	if (f == subview.getCollectionEditingRow()) { 
		selectedClass = f%2==0?style.getListPairSelected():style.getListOddSelected();
		cssClass = cssClass + " " + selectedClass;		
		if (style.isApplySelectedStyleToCellInList()) cssCellClass = cssCellClass + " " + selectedClass; 
	}		
	String events=f%2==0?style.getListPairEvents(selectedClass):style.getListOddEvents(selectedClass);
%>
<tr id="xava-tr-list" class="<%=cssClass%>" <%=events%>>
<% if (lineAction != null) { %>
<td class="<%=cssCellClass%>" style='vertical-align: middle;text-align: center;padding-right: 2px'>
<xava:action action="<%=lineAction%>" argv='<%="row="+f + ",viewObject="+viewName%>'/>
</td>
<% } %>
<td class="<%=cssCellClass%>" width="5">
<input type="CHECKBOX" name="xava_selected" value="<%=propertyPrefix%>__SELECTED__:<%=f%>"/>
</td>
<%
	f++;
	it = subview.getMetaPropertiesList().iterator();
	while (it.hasNext()) {
		MetaProperty p = (MetaProperty) it.next();
		String align = p.isNumber() && !p.hasValidValues()?"style='vertical-align: middle;text-align: right'":"style='vertical-align: middle;'";
		String fvalue = null;
		Object value = null;
		String propertyName = p.getName();
		value = Maps.getValueFromQualifiedName(row, propertyName);
		if (p.hasValidValues()) {
			if (value instanceof Number) {
				fvalue = p.getValidValueLabel(request, ((Number) value).intValue());
			}
			else {
				// In this case value is a enum type
				fvalue = p.getValidValueLabel(request, value);
			}
		}
		else {
			fvalue = WebEditors.format(request, p, value, errors, view.getViewName());	
		}
%>
	<td class="<%=cssCellClass%>" <%=align%>><%=fvalue%>&nbsp;</td>
<%
	}
}
%>
</tr>
</table>