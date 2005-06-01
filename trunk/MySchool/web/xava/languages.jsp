<%@ include file="imports.jsp"%>

<jsp:useBean id="xava_language" class="org.openxava.session.Language" scope="session"/>
<%
xava_language.setDefaultLocale(request.getLocale());
request.setAttribute("xava.locale", xava_language.getLocale());
%>

<c:choose>
	<c:when test="${xava_language == 'en'}">
		<fmt:setLocale value="en" scope="request"/>
	</c:when>
	<c:when test="${xava_language == 'es'}">
		<fmt:setLocale value="es" scope="request"/>
	</c:when>
	<c:when test="${xava_language == 'ca'}">
		<fmt:setLocale value="ca" scope="request"/>
	</c:when>
	<c:when test="${xava_language == 'it'}">
		<fmt:setLocale value="it" scope="request"/>
	</c:when>
	<c:when test="${xava_language == 'de'}">
		<fmt:setLocale value="de" scope="request"/>
	</c:when>
	<c:when test="${xava_language == 'fr'}">
		<fmt:setLocale value="fr" scope="request"/>
	</c:when>
	<c:when test="${xava_language == 'pt'}">
		<fmt:setLocale value="pt" scope="request"/>
	</c:when>
</c:choose>

<fmt:setBundle basename="XavaResources" scope="request"/>

