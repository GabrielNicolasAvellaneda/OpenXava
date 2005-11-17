<%@ include file="imports.jsp"%>

<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Map" %>
<%@ page import="org.openxava.model.meta.MetaReference" %>
<%@ page import="org.openxava.model.meta.IMetaEjb" %>

<jsp:useBean id="errors" class="org.openxava.util.Messages" scope="request"/>
<jsp:useBean id="context" class="org.openxava.controller.ModuleContext" scope="session"/>
<jsp:useBean id="style" class="org.openxava.web.style.Style" scope="request"/>

<%
String viewObject = request.getParameter("viewObject");
viewObject = (viewObject == null || viewObject.equals(""))?"xava_view":viewObject;
org.openxava.view.View view = (org.openxava.view.View) context.get(request, viewObject);
String referenceKey = request.getParameter("referenceKey");
MetaReference ref = (MetaReference) request.getAttribute(referenceKey); 
String labelKey = referenceKey + "_LABEL_";
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
<img src="<%=request.getContextPath()%>/xava/images/key.gif"/>
<% } else if (ref.isRequired()) {  %>	
<img src="<%=request.getContextPath()%>/xava/images/required.gif"/>
<% } if ( errors.memberHas(ref)) {%>
<img src="<%=request.getContextPath()%>/xava/images/error.gif"/>
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

org.openxava.controller.ModuleManager manager = (org.openxava.controller.ModuleManager) context.get(request, "manager", "org.openxava.controller.ModuleManager");
String formName = manager.getForm();	
boolean throwChanged=view.throwsReferenceChanged(ref);
String script = throwChanged?
	"onchange='throwPropertyChanged(" + formName + ", \"" + propertyKey + "\")'":"";

String parameterValuesProperties=view.getParameterValuesPropertiesInDescriptionsList(ref);
String condition = view.getConditionInDescriptionsList(ref);
boolean orderByKey = view.isOrderByKeyInDescriptionsList(ref);
String order = view.getOrderInDescriptionsList(ref); 
org.openxava.tab.meta.MetaTab metaTab = ref.getMetaModelReferenced().getMetaComponent().getMetaTab();
String filterArg = "";
if (metaTab.hasFilter()) {
	filterArg = "&filter=" + metaTab.getMetaFilter().getClassName();
}
if (metaTab.hasBaseCondition()) {
	if (org.openxava.util.Is.emptyString(condition)) {
		condition = metaTab.getBaseCondition();
	}
	else {
		condition = metaTab.getBaseCondition() + " AND " + condition;
	}
}
String urlDescriptionEditor = "editors/descriptionsEditor.jsp" // in this way because websphere 6 has problems with jsp:param
	+ "?script=" + script
	+ "&propertyKey=" + propertyKey
	+ "&editable=" + editable
	+ "&model=" + ref.getReferencedModelName()
	+ "&keyProperty=" + keyProperty
	+ "&keyProperties=" + keyProperties
	+ "&descriptionProperty=" + descriptionProperty
	+ "&descriptionProperties=" + descriptionProperties
	+ "&parameterValuesProperties=" + parameterValuesProperties
	+ "&condition=" + condition
	+ "&orderByKey=" + orderByKey
	+ "&order=" + order
	+ filterArg;
%>
<jsp:include page="<%=urlDescriptionEditor%>" />

<%
if (editable && view.isCreateNewForReference(ref)) {
%>
<xava:action action='Reference.createNew' argv='<%="model="+ref.getReferencedModelName() + ",keyProperty=" + propertyKey%>'/>
<%
}
%>

<%=postEditor%>
