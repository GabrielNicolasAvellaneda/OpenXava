<% 
int additionalTotalsCount = subview.getCollectionTotalsCount();
for (int i=0; i<additionalTotalsCount; i++) {
%>
<tr class="<%=style.getTotalRow()%>">
<td style="<%=style.getTotalEmptyCellStyle()%>"/>
<% if (!subview.getMetaCollection().isElementCollection()) { %>
<td style="<%=style.getTotalEmptyCellStyle()%>"/>
<% } %>
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
