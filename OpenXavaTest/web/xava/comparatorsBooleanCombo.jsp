<%@ include file="imports.jsp"%>

<jsp:useBean id="style" class="org.openxava.web.Style" scope="request"/>

<%
String comparator = request.getParameter("comparator");
String equal = "eq".equals(comparator)?"selected='selected'":"";
String different = "ne".equals(comparator)?"selected='selected'":"";
%>

<input type="hidden" name="conditionValues" value="true">
<select name="conditionComparators" class=<%=style.getEditor()%>>
	<option value=""></option>
	<option value="eq" <%=equal%>><fmt:message key="yes"/></option>
	<option value="ne" <%=different%>><fmt:message key="no"/></option>
</select>	
	