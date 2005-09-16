<%@ include file="imports.jsp"%>

<%@ page import="java.util.StringTokenizer" %>

<jsp:useBean id="style" class="org.openxava.web.Style" scope="request"/>

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

<select name="conditionValues" class=<%=style.getEditor()%>>
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