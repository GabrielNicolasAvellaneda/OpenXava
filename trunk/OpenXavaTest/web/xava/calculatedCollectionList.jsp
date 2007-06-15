<table id=<%=idCollection%> class="<%=style.getList()%>" width="100%" <%=style.getListCellSpacing()%>>
<tr id="xava-tr-list" class="<%=style.getListHeader()%>">
	<% if (lineAction != null) { %>	
	<th class=<%=style.getListHeaderCell()%>></th>
	<% } %>
	<% if (hasListActions  && !collectionView.isCollectionDetailVisible() && (collectionEditable || !subview.getActionsNamesList().isEmpty())) {  %>	
	<th class=<%=style.getListHeaderCell()%> width="5"></th>
	<% } %>

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
	if (f == subview.getCollectionEditingRow()) { 
		String selectedClass = f%2==0?style.getListPairSelected():style.getListOddSelected();
		cssClass = cssClass + " " + selectedClass;
		if (style.isApplySelectedStyleToCellInList()) cssCellClass = cssCellClass + " " + selectedClass; // tmp
	}	
	
%>
<tr id="xava-tr-list" class="<%=cssClass%>">
<% if (lineAction != null) { %>
<td class="<%=cssCellClass%>" style='vertical-align: middle;text-align: center;padding-right: 2px'>
<xava:action action="<%=lineAction%>" argv='<%="row="+f + ",viewObject="+viewName%>'/>
</td>
<% } %>
<% if (hasListActions  && !collectionView.isCollectionDetailVisible() && (collectionEditable || !subview.getActionsNamesList().isEmpty())) { %>
<td class="<%=cssCellClass%>" width="5">
<input type="CHECKBOX" name="<%=propertyPrefix%>__SELECTED__" value="<%=f%>"/>
</td>
<% } %>
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
			fvalue = WebEditors.format(request, p, value, errors);	
		}
%>
	<td class="<%=cssCellClass%>" <%=align%>><%=fvalue%>&nbsp;</td>
<%
	}
}
%>
</tr>
</table>