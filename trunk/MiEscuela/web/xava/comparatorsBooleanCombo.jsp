<%@ include file="imports.jsp"%>

<%
String comparator = request.getParameter("comparator");
String equal = "eq".equals(comparator)?"selected='selected'":"";
String different = "<>".equals(comparator)?"selected='selected'":"";
%>

<input type="hidden" name="conditionValues" value="true">
<select name="conditionComparators" class=editor>
	<option value=""></option>
	<option value="=" <%=equal%>><fmt:message key="yes"/></option>
	<option value="<>" <%=different%>><fmt:message key="no"/></option>
</select>	
	