<%@ include file="imports.jsp"%>

<jsp:useBean id="xavax_language" class="org.xavax.session.Language" scope="session"/>
<%
xavax_language.setDefaultLocale(request.getLocale());
request.setAttribute("xavax.locale", xavax_language.getLocale());
%>

<c:if test="${xavax_language == 'en'}">
	<fmt:setLocale value="en" scope="request"/>
</c:if>
<c:if test="${xavax_language == 'es'}">
	<fmt:setLocale value="es" scope="request"/>
</c:if>
<c:if test="${xavax_language == 'ca'}">
	<fmt:setLocale value="ca" scope="request"/>
</c:if>
<fmt:setBundle basename="XavaxResources" scope="request"/>

