<%@ include file="imports.jsp"%>

<jsp:useBean id="xava_language" class="org.openxava.session.Language" scope="session"/>
<%
xava_language.setDefaultLocale(request.getLocale());
request.setAttribute("xava.locale", xava_language.getLocale());
%>

<c:if test="${xava_language == 'en'}">
	<fmt:setLocale value="en" scope="request"/>
</c:if>
<c:if test="${xava_language == 'es'}">
	<fmt:setLocale value="es" scope="request"/>
</c:if>
<c:if test="${xava_language == 'ca'}">
	<fmt:setLocale value="ca" scope="request"/>
</c:if>
<fmt:setBundle basename="XavaResources" scope="request"/>

