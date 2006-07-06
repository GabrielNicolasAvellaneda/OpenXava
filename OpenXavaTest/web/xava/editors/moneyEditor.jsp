<%@ include file="textEditor.jsp"%>

<%@page import="java.util.Currency"%>
<%@page import="java.util.Locale"%>

<b> x=<%=Currency.getInstance(Locale.getDefault()).getSymbol()%></b>