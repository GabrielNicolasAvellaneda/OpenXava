<%@ include file="../imports.jsp"%>

<%@page import="org.openxava.web.Actions"%>
<%@page import="org.openxava.web.Ids"%>
<%@page import="org.openxava.model.meta.MetaProperty"%>

<jsp:useBean id="style" class="org.openxava.web.style.Style" scope="request"/>

<%
String comparator = request.getParameter("comparator");
String prefix = request.getParameter("prefix");
if (prefix == null) prefix = "";
boolean isString = "true".equals(request.getParameter("isString"));
boolean isDate = "true".equals(request.getParameter("isDate"));
boolean isEmpty = "true".equals(request.getParameter("isEmpty"));
String eq = "eq".equals(comparator)?"selected='selected'":"";
String ne = "ne".equals(comparator)?"selected='selected'":"";
String ge = "ge".equals(comparator)?"selected='selected'":"";
String le = "le".equals(comparator)?"selected='selected'":"";
String gt = "gt".equals(comparator)?"selected='selected'":"";
String lt = "lt".equals(comparator)?"selected='selected'":"";
String startsWith = "starts_comparator".equals(comparator)?"selected='selected'":"";
String contains = "contains_comparator".equals(comparator)?"selected='selected'":"";
String notContains = "not_contains_comparator".equals(comparator)?"selected='selected'":"";
String year = "year_comparator".equals(comparator)?"selected='selected'":"";
String month = "month_comparator".equals(comparator)?"selected='selected'":"";
String yearMonth = "year_month_comparator".equals(comparator)?"selected='selected'":"";
String range = "range_comparator".equals(comparator)?"selected='selected'":"";
// tmp int index = Integer.parseInt(request.getParameter("index"));
String idConditionValue = request.getParameter("idConditionValue");
String idConditionValueTo = request.getParameter("idConditionValueTo");
// tmp String name = Ids.decorate(request, prefix + "conditionComparator." + index);
// tmp String actionOnChangeComparator = Actions.getActionOnChangeComparator(name,idConditionValue,idConditionValueTo);
// tmp ini
String propertyKey = request.getParameter("comparatorPropertyKey"); 
String name = null;
String script = null;
if (propertyKey == null) {
	int index = Integer.parseInt(request.getParameter("index"));
	name = Ids.decorate(request, prefix + "conditionComparator." + index);
	script = Actions.getActionOnChangeComparator(name,idConditionValue,idConditionValueTo);
}
else {
	name = propertyKey;
	script = request.getParameter("script");
}
// tmp fin
%>
<%-- tmp 
<select id="<%=name%>" name="<%=name%>" class=<%=style.getEditor()%> <%=actionOnChangeComparator%>>
--%>
<%-- tmp ini--%>
<select id="<%=name%>" name="<%=name%>" class=<%=style.getEditor()%> <%=script%>>
	<% 
	if (propertyKey != null) {
	%>
	<option value=""></option>	
	<% 
	}
	%>
<%-- tmp fin --%>
	<% 
	if (!isEmpty) { 
	%>
	<%
	if (isString) {
	%>						
	<option value="starts_comparator" <%=startsWith%>><xava:message key="starts_comparator"/></option>
	<option value="contains_comparator" <%=contains%>><xava:message key="contains_comparator"/></option>
	<option value="not_contains_comparator" <%=notContains%>><xava:message key="not_contains_comparator"/></option>
	<%
	}
	%>
	<option value="eq" <%=eq%>>=</option>
	<option value="ne" <%=ne%>><></option>
	<option value="ge" <%=ge%>>>=</option>
	<option value="le" <%=le%>><=</option>	
	<option value="gt" <%=gt%>>></option>
	<option value="lt" <%=lt%>><</option>
	<%
	if (isDate) {
	%>
	<option value="year_comparator" <%=year%>><xava:message key="year_comparator"/></option>
	<option value="month_comparator" <%=month%>><xava:message key="month_comparator"/></option>
	<option value="year_month_comparator" <%=yearMonth%>><xava:message key="year_month_comparator"/></option>
	<%
	}	
	%>
	<% 
	if (propertyKey == null) { // tmp
	%>	
	<option value="range_comparator" <%=range%>><xava:message key="range_comparator"/></option>
	<%
	}
	%>
	<%
	} // tmp isEmpty
	%>
</select>	
	