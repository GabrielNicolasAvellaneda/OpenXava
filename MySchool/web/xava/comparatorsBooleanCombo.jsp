<%@ include file="imports.jsp"%>

<%
String comparator = request.getParameter("comparator");
String equal = "eq".equals(comparator)?"selected='selected'":"";
String different = "ne".equals(comparator)?"selected='selected'":"";
%>

<input type="hidden" name="conditionValues" value="true">
<select name="conditionComparators" class=editor>
	<option value=""></option>
	<option value="eq" <%=equal%>><fmt:message key="yes"/></option>
	<option value="ne" <%=different%>><fmt:message key="no"/></option>
</select>	
	