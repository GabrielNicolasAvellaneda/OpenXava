<table id=<%=idCollection%> class=<%=style.getList()%> width="100%" <%=style.getListCellSpacing()%>>
<tr>
	<% if (lineAction != null) { %>	
	<th class=<%=style.getListHeader()%>></th>
	<% } %>
	<% if (hasListActions  && !collectionView.isCollectionDetailVisible() && (collectionEditable || !subview.getActionsNamesList().isEmpty())) {  %>	
	<th class=<%=style.getListHeader()%> width="5"></th>
	<% } %>

<%
// Heading
Iterator it = subview.getMetaPropertiesList().iterator();
while (it.hasNext()) {
	MetaProperty p = (MetaProperty) it.next();
%>
	<th class=<%=style.getListHeader()%>><%=p.getLabel(request)%>&nbsp;</th>
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
	if (f == subview.getCollectionEditingRow()) { 
		cssClass = cssClass=f%2==0?style.getListPairSelected():style.getListOddSelected();
	}
	
%>
<tr class=<%=cssClass%>>
<% if (lineAction != null) { %>
<td class=<%=cssClass%> style='vertical-align: middle;text-align: center;padding-right: 2px'>
<xava:action action="<%=lineAction%>" argv='<%="row="+f + ",viewObject="+viewName%>'/>
</td>
<% } %>
<% if (hasListActions  && !collectionView.isCollectionDetailVisible() && (collectionEditable || !subview.getActionsNamesList().isEmpty())) { %>
<td class=<%=cssClass%> width="5">
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
	<td class=<%=cssClass%> <%=align%>><%=fvalue%>&nbsp;</td>
<%
	}
}
%>
</tr>
</table>