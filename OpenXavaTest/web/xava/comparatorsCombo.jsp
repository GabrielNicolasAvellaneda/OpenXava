<%@ include file="imports.jsp"%>

<%
String comparator = request.getParameter("comparator");
boolean isString = "true".equals(request.getParameter("isString"));
boolean isDate = "true".equals(request.getParameter("isDate"));
String eq = "eq".equals(comparator)?"selected='selected'":"";
String ne = "<>".equals(comparator)?"selected='selected'":"";
String ge = ">eq".equals(comparator)?"selected='selected'":"";
String le = "<eq".equals(comparator)?"selected='selected'":"";
String gt = ">".equals(comparator)?"selected='selected'":"";
String lt = "<".equals(comparator)?"selected='selected'":"";
String startsWith = "starts_comparator".equals(comparator)?"selected='selected'":"";
String contains = "contains_comparator".equals(comparator)?"selected='selected'":"";
String year = "year_comparator".equals(comparator)?"selected='selected'":"";
String month = "month_comparator".equals(comparator)?"selected='selected'":"";
%>

<select name="conditionComparators" class=editor>
	<%
	if (isString) {
	%>						
	<option value="starts_comparator" <%=startsWith%>><fmt:message key="starts_comparator"/></option>
	<option value="contains_comparator" <%=contains%>><fmt:message key="contains_comparator"/></option>
	<%
	}
	%>
	<option value="=" <%=eq%>>=</option>
	<option value="<>" <%=ne%>><></option>
	<option value=">=" <%=ge%>>>=</option>
	<option value="<=" <%=le%>><=</option>	
	<option value=">" <%=gt%>>></option>
	<option value="<" <%=lt%>><</option>
	<%
	if (isDate) {
	%>
	<option value="year_comparator" <%=year%>><fmt:message key="year_comparator"/></option>
	<option value="month_comparator" <%=month%>><fmt:message key="month_comparator"/></option>
	<%
	}	
	%>
</select>	
	