<%@ include file="imports.jsp"%>

<%@ page import="java.util.StringTokenizer" %>

<%
String validValues = request.getParameter("validValues");
String value = request.getParameter("value");
int ivalue = 0;
try {
	ivalue = Integer.parseInt(value);
}
catch (Exception ex) {
}
%>

<input type="hidden" name="conditionComparators" value="=">

<select name="conditionValues" class=editor>
	<option value=""></option>
<%
	StringTokenizer st = new StringTokenizer(validValues, "|");
	int i = 0;
	while (st.hasMoreTokens()) {
		i++;
		String selected =  (i == ivalue)?"selected":"";
%>
	<option value="<%=i%>" <%=selected%>><%=st.nextToken()%></option>
<%
	} // while
%>
</select>