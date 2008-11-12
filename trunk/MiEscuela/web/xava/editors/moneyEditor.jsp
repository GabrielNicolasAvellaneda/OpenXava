<%@page import="java.util.Currency"%>
<%@page import="java.util.Locale"%>

<%@ include file="textEditor.jsp"%>

<b> <%=Currency.getInstance(Locale.getDefault()).getSymbol()%></b>