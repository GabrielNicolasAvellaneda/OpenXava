<%@ include file="imports.jsp"%>

<jsp:useBean id="context" class="org.openxava.controller.ModuleContext" scope="session"/>
<jsp:useBean id="errors" class="org.openxava.util.Messages" scope="request"/>
<jsp:useBean id="style" class="org.openxava.web.style.Style" scope="request"/>

<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Map" %>
<%@ page import="org.openxava.util.Is" %>
<%@ page import="org.openxava.util.Maps" %>
<%@ page import="org.openxava.view.View" %>
<%@ page import="org.openxava.model.meta.MetaProperty" %>
<%@ page import="org.openxava.model.meta.MetaReference" %>
<%@ page import="org.openxava.web.WebEditors" %>

<%
String collectionName = request.getParameter("collectionName");
String viewObject = request.getParameter("viewObject");
View view = (View) context.get(request, viewObject);
View subview = view.getSubview(collectionName);
MetaReference ref = view.getMetaModel().getMetaCollection(collectionName).getMetaReference();
String viewName = viewObject + "_" + collectionName;
String propertyPrefixAccumulated = request.getParameter("propertyPrefix");
String idCollection = null;
if (Is.emptyString(propertyPrefixAccumulated)) {
	idCollection = collectionName;
}
else {
	// removing xava.ModelName.
	int idx = propertyPrefixAccumulated.indexOf('.');
	idx = propertyPrefixAccumulated.indexOf('.', idx+1) + 1;
	idCollection = propertyPrefixAccumulated.substring(idx) + collectionName;
}
boolean collectionEditable = subview.isCollectionEditable();
boolean collectionMembersEditables = subview.isCollectionMembersEditables();
boolean hasListActions = subview.hasListActions();
String lineAction = null;
if (collectionEditable || collectionMembersEditables) {
	lineAction = subview.getEditCollectionElementAction();
}
else {
	if (!subview.isDetailMemberInCollection()) {
		lineAction = subview.getViewCollectionElementAction();
	}
}
String propertyPrefix = Is.emptyString(propertyPrefixAccumulated)?"xava." + view.getModelName() + "." + collectionName + ".":propertyPrefixAccumulated + collectionName + ".";
%>

<table id=<%=idCollection%> class=<%=style.getList()%> <%=style.getListCellSpacing()%>>
<tr>
	<% if (lineAction != null) { %>	
	<th class=<%=style.getListHeader()%>>
	<% } %>
	<% if (hasListActions) { %>	
	<th class=<%=style.getListHeader()%>>
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
subview.resetCollectionValues();
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
<td class=<%=cssClass%> style='vertical-align: middle;text-align: center'>
<xava:link action="<%=lineAction%>" argv='<%="row="+f + ",viewObject="+viewName%>'/>
</td>
<% } %>
<% if (hasListActions) { %>
<td class=<%=cssClass%>>
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
			Number validValue = (Number) value;			
			fvalue = validValue == null?"":p.getValidValueLabel(request, validValue.intValue());
		}
		else {
			fvalue = WebEditors.format(request, p, value, errors);	
		}
%>
	<td class=<%=cssClass%> <%=align%>><%=fvalue%>&nbsp;</td>
<%
	}
%>
</tr>

<%
}
// New
if (view.displayDetailInCollection(collectionName)) {
	View collectionView = view.getSubview(collectionName);
	context.put(request, viewName, collectionView);
	if (collectionView.isCollectionDetailVisible()) {
%>	
<tr><td colspan="<%=subview.getMetaPropertiesList().size()+1%>">		
<table class=<%=style.getFrame()%> width='100%' <%=style.getFrameSpacing()%>>
<tr class=<%=style.getFrameTitle()%>><th class=<%=style.getFrameTitleLabel()%> align='left'><%=ref.getLabel(request)%></th></tr>
<tr><td class=<%=style.getFrameContent()%>>
<jsp:include page="detail.jsp"> 
	<jsp:param name="viewObject" value="<%=viewName%>" />
	<jsp:param name="propertyPrefix" value="<%=propertyPrefix%>" />
</jsp:include>		
</td></tr>
<tr><td>
<% if (collectionEditable || collectionMembersEditables) { %>
<xava:link action="Collection.save" argv='<%="viewObject="+viewName%>'/>
<% } %>
&nbsp;<xava:link action="Collection.hiddenDetail" argv='<%="viewObject="+viewName%>'/>
<% if (collectionEditable) { %>
&nbsp;<xava:link action="Collection.remove" argv='<%="viewObject="+viewName%>'/>
<% } %>
<% 
Iterator itDetailActions = subview.getActionsNamesDetail().iterator();
while (itDetailActions.hasNext()) {
%>
&nbsp;<xava:link action="<%=itDetailActions.next().toString()%>" argv='<%="viewObject="+viewName%>'/>
<%	
} // while detail actions
%>
</td></tr>
</table>
<%
	}
	else {// no mostrar
%>
<tr class=<%=style.getCollectionListActions()%>><td colspan="<%=subview.getMetaPropertiesList().size()+1%>" class=<%=style.getCollectionListActions()%>>
<% if (collectionEditable) { %>
<xava:link action="Collection.new" argv='<%="viewObject="+viewName%>'/>

		<% 
		Iterator itListActions = subview.getActionsNamesList().iterator();
		while (itListActions.hasNext()) {
		%>
&nbsp;<xava:link action="<%=itListActions.next().toString()%>" argv='<%="viewObject="+viewName%>'/>
		<%	
		} // while list actions
		%>

<% } %>
</td></tr>
<%	
	}
}
else {
%>
<td></td>
<%
	org.openxava.controller.ModuleManager manager = (org.openxava.controller.ModuleManager) context.get(request, "manager", "org.openxava.controller.ModuleManager");
	String formName = manager.getForm();	
	String argv = "collectionName=" + collectionName;
	it = subview.getMetaPropertiesList().iterator();
	while (it.hasNext()) {
		MetaProperty p = (MetaProperty) it.next(); 
		String propertyKey= propertyPrefix + p.getName();
		String valueKey = propertyKey + ".value";
		request.setAttribute(propertyKey, p);
		request.setAttribute(valueKey, subview.getValue(p.getName()));		
		String script = "";
		if (it.hasNext()) {
			if (subview.throwsPropertyChanged(p)) {
				script = "onchange='throwPropertyChanged(" + formName + ", \"" + propertyKey + "\")'";
			}
		}
		else {
			script = "onblur='executeXavaAction(" + formName + ", \"Collection.save\", \"" + argv + "\")'";
		}
		Object value = request.getAttribute(propertyKey + ".value");
		if (WebEditors.mustToFormat(p)) {
			String fvalue = WebEditors.format(request, p, value, errors);
			request.setAttribute(propertyKey + ".fvalue", fvalue);
		}		
	%>
	<td>
		<jsp:include page="<%=WebEditors.getUrl(p)%>">
			<jsp:param name="propertyKey" value="<%=propertyKey%>"/>
			<jsp:param name="script" value="<%=script%>"/>
			<jsp:param name="editable" value="true"/>
		</jsp:include>
	</td>
	<%
	}
}
%>

</tr>
</table>
