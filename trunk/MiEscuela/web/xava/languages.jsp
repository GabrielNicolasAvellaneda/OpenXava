<%@ include file="imports.jsp"%>

<jsp:useBean id="xava_language" class="org.openxava.session.Language" scope="session"/>
<%
xava_language.setDefaultLocale(request.getLocale());
request.setAttribute("xava.locale", xava_language.getLocale());
%>


