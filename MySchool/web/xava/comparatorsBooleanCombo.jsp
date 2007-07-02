<%@ include file="imports.jsp"%>

<jsp:useBean id="style" class="org.openxava.web.style.Style" scope="request"/>

<%
String comparator = request.getParameter("comparator");
String equal = "eq".equals(comparator)?"selected='selected'":"";
String different = "ne".equals(comparator)?"selected='selected'":"";
String prefix = request.getParameter("prefix");
if (prefix == null) prefix = "";
%>

<input type="hidden" name="<%=prefix%>conditionValues" value="true">
<select name="<%=prefix%>conditionComparators" class=<%=style.getEditor()%>>
	<option value=""></option>
	<option value="eq" <%=equal%>><xava:message key="yes"/></option>
	<option value="ne" <%=different%>><xava:message key="no"/></option>
</select>	
	