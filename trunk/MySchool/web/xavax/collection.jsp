<%@ include file="imports.jsp"%>

<jsp:useBean id="context" class="org.xavax.controller.ModuleContext" scope="session"/>
<jsp:useBean id="errors" class="org.xavax.util.Messages" scope="request"/>

<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Map" %>
<%@ page import="org.xavax.util.Is" %>
<%@ page import="org.xavax.view.View" %>
<%@ page import="org.xavax.model.meta.MetaProperty" %>
<%@ page import="org.xavax.model.meta.MetaReference" %>
<%@ page import="org.xavax.controller.meta.MetaAction" %>
<%@ page import="org.xavax.web.WebEditors" %>

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
	// removing xavax.NombreModelo.
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
		lineAction = "Collection.view";
	}
}
String propertyPrefix = Is.emptyString(propertyPrefixAccumulated)?"xavax." + view.getModelName() + "." + collectionName + ".":propertyPrefixAccumulated + collectionName + ".";
%>

<table id=<%=idCollection%> class=list>
<tr>
	<% if (lineAction != null) { %>	
	<th class=list>
	<% } %>
	<% if (hasListActions) { %>	
	<th class=list>
	<% } %>

<%
// Heading
Iterator it = subview.getMetaPropertiesList().iterator();
while (it.hasNext()) {
	MetaProperty p = (MetaProperty) it.next();
%>
	<th class=list><%=p.getLabel(request)%></th>
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
	String cssClass=f%2==0?"pair":"odd";
	if (f == subview.getCollectionEditingRow()) { 
		cssClass = "selected";
	}
	
%>
<tr class=<%=cssClass%>>
<% if (lineAction != null) { %>
<td class=<%=cssClass%>>
<xavax:link action="<%=lineAction%>" argv='<%="row="+f + ",viewObject="+viewName%>'/>
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
		String align = p.isNumber() && !p.hasValidValues()?"align='right'":"";
		String fvalue = null;
		Object value = null;
		String propertyName = p.getName();
		int idx = propertyName.indexOf('.');
		if (idx < 0) {
			value = row.get(propertyName);
		}
		else {
			String referenceName = propertyName.substring(0, idx);			
			Map refValues = (Map) row.get(referenceName);
			if (refValues != null) {
				String propertyReferenceName = propertyName.substring(idx+1);
				value = refValues.get(propertyReferenceName);
			}
		}
		if (p.hasValidValues()) {
			Number validValue = (Number) value;			
			fvalue = validValue == null?"":p.getValidValueLabel(request, validValue.intValue());
		}
		else {
			fvalue = WebEditors.format(request, p, value, errors);	
		}
%>
	<td class=<%=cssClass%> <%=align%>><%=fvalue%></td>
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
<table class=frame width='100%'>
<tr class=frame><th align='left'><%=ref.getLabel(request)%></th></tr>
<tr><td class=frame>
<jsp:include page="detail.jsp"> 
	<jsp:param name="viewObject" value="<%=viewName%>" />
	<jsp:param name="propertyPrefix" value="<%=propertyPrefix%>" />
</jsp:include>		
</td></tr>
<tr><td>
<% if (collectionEditable || collectionMembersEditables) { %>
<xavax:link action="Collection.save" argv="<%="viewObject="+viewName%>"/>
<% } %>
&nbsp;<xavax:link action="Collection.hiddenDetail" argv="<%="viewObject="+viewName%>"/>
<% if (collectionEditable) { %>
&nbsp;<xavax:link action="Collection.remove" argv="<%="viewObject="+viewName%>"/>
<% } %>
<% 
Iterator itDetailActions = subview.getActionsNamesDetail().iterator();
while (itDetailActions.hasNext()) {
%>
&nbsp;<xavax:link action="<%=itDetailActions.next().toString()%>" argv="<%="viewObject="+viewName%>"/>
<%	
} // while detail actions
%>
</td></tr>
</table>
<%
	}
	else {// no mostrar
%>
<tr><td colspan="<%=subview.getMetaPropertiesList().size()+1%>">
<% if (collectionEditable) { %>
<xavax:link action="Collection.new" argv="<%="viewObject="+viewName%>"/>

		<% 
		Iterator itListActions = subview.getActionsNamesList().iterator();
		while (itListActions.hasNext()) {
		%>
&nbsp;<xavax:link action="<%=itListActions.next().toString()%>" argv="<%="viewObject="+viewName%>"/>
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
	org.xavax.controller.ModuleManager manager = (org.xavax.controller.ModuleManager) context.get(request, "manager", "org.xavax.controller.ModuleManager");
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
				script = "onchange='throwPropertyChanged(\"" + propertyKey + "\")'";
			}
		}
		else {
			script = "onblur='executeXavaxAction(" + formName + ", \"Collection.save\", \"" + argv + "\")'";
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
