<%@ include file="imports.jsp"%>

<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Map" %>
<%@ page import="org.openxava.model.meta.MetaReference" %>
<%@ page import="org.openxava.model.meta.IMetaEjb" %>
<%@ page import="org.openxava.model.meta.MetaEjbImpl" %>

<jsp:useBean id="errors" class="org.openxava.util.Messages" scope="request"/>
<jsp:useBean id="context" class="org.openxava.controller.ModuleContext" scope="session"/>

<%
String viewObject = request.getParameter("viewObject");
viewObject = (viewObject == null || viewObject.equals(""))?"xava_view":viewObject;
org.openxava.view.View view = (org.openxava.view.View) context.get(request, viewObject);
String referenceKey = request.getParameter("referenceKey");
MetaReference ref = (MetaReference) request.getAttribute(referenceKey); 
%>

<%@ include file="htmlTagsEditor.jsp"%>

<%
String editableKey = referenceKey + "_EDITABLE_";
boolean editable = view.isEditable(ref);
%>
<input type="hidden" name="<%=editableKey%>" value="<%=editable%>">

<%=preLabel%>
<%=ref.getLabel(request) %>
<%=postLabel%>
<%=preIcons%>
<% if (ref.isKey()) { %>
<img src="images/key.gif"/>
<% } else if (ref.isRequired()) {  %>	
<img src="images/required.gif"/>
<% } if ( errors.memberHas(ref)) {%>
<img src="images/error.gif"/>
<% } %>
<%=postIcons%>
<%=preEditor%>

<%
Collection keys = ref.getMetaModelReferenced().getKeyPropertiesNames();
String keyProperty = "";
String keyProperties = "";
String propertyKey = null;
if (keys.size() == 1) {		
	keyProperty = keys.iterator().next().toString();
	propertyKey = referenceKey + "." + keyProperty;	
	Map values = (Map) view.getValue(ref.getName());	
	values = values == null?java.util.Collections.EMPTY_MAP:values;
	Object value = values.get(keyProperty);
	String valueKey = propertyKey + ".value";
	request.setAttribute(valueKey, value);	
	String fvalue = value==null?"":value.toString();
	request.setAttribute(propertyKey + ".fvalue", fvalue);
}
else {	
	propertyKey = referenceKey + ".KEY";
	Map values = (Map) view.getValue(ref.getName());
	values = values == null?java.util.Collections.EMPTY_MAP:values;
	java.util.Iterator it = keys.iterator();
	StringBuffer sb = new StringBuffer();
	while (it.hasNext()) {
		String property = (String) it.next();		
		Object value = values.get(property);
		String valueKey = referenceKey + "." + property + ".value";
		request.setAttribute(valueKey, value);
		sb.append(property);
		if (it.hasNext()) {
			sb.append(',');
		}
	}	
	Object key = ((IMetaEjb) ref.getMetaModelReferenced()).obtainPrimaryKeyFromKey(values);	
	String fvalue = key==null?"0":key.toString();
	request.setAttribute(propertyKey + ".fvalue", fvalue);
	keyProperties = sb.toString();
}

String descriptionProperty = view.getDescriptionPropertyInDescriptionsList(ref);
String descriptionProperties = view.getDescriptionPropertiesInDescriptionsList(ref);

boolean throwChanged=view.throwsReferenceChanged(ref);
String script = throwChanged?
	"onchange='throwPropertyChanged(\"" + propertyKey + "\")'":"";

String parameterValuesProperties=view.getParameterValuesPropertiesInDescriptionsList(ref);
String condition = view.getConditionInDescriptionsList(ref);
boolean orderByKey = view.isOrderByKeyInDescriptionsList(ref);
%>

<jsp:include page="editors/descriptionsEditor.jsp">
	<jsp:param name="script" value="<%=script%>"/>
	<jsp:param name="propertyKey" value="<%=propertyKey%>"/>
	<jsp:param name="editable" value="<%=editable%>"/>
	<jsp:param name="model" value="<%=ref.getReferencedModelName() %>"/>
	<jsp:param name="keyProperty" value="<%=keyProperty%>"/>
	<jsp:param name="keyProperties" value="<%=keyProperties%>"/>
	<jsp:param name="descriptionProperty" value="<%=descriptionProperty%>"/>	
	<jsp:param name="descriptionProperties" value="<%=descriptionProperties%>"/>	
	<jsp:param name="parameterValuesProperties" value="<%=parameterValuesProperties%>"/>
	<jsp:param name="condition" value="<%=condition%>"/>
	<jsp:param name="orderByKey" value="<%=orderByKey%>"/>	
</jsp:include>

<%
if (editable && view.isCreateNewForReference(ref)) {
%>
<xava:link action='Reference.createNew' argv='<%="model="+ref.getReferencedModelName() + ",keyProperty=" + propertyKey%>'/>
<%
}
%>

<%=postEditor%>
