<%@ page import="org.openxava.model.meta.MetaProperty" %>
<%@ page import="org.openxava.actions.OnChangeCustomReportColumnNameAction" %>

<jsp:useBean id="context" class="org.openxava.controller.ModuleContext" scope="session"/>
<jsp:useBean id="style" class="org.openxava.web.style.Style" scope="request"/>

<% 
boolean editable="true".equals(request.getParameter("editable"));
if (!editable) {
%>

<jsp:include page="textEditor.jsp"/>

<% } else {
	String propertyKey = request.getParameter("propertyKey");
	String fvalue = (String) request.getAttribute(propertyKey + ".fvalue");
	boolean isString = OnChangeCustomReportColumnNameAction.STRING_COMPARATOR.equals(fvalue); 	
	boolean isDate = OnChangeCustomReportColumnNameAction.DATE_COMPARATOR.equals(fvalue);
	boolean isEmpty = OnChangeCustomReportColumnNameAction.EMPTY_COMPARATOR.equals(fvalue);
%>

<jsp:include page="comparatorsCombo.jsp">
	<jsp:param name="comparatorPropertyKey" value="<%=propertyKey%>"/>
	<jsp:param name="isString" value="<%=isString%>"/>
	<jsp:param name="isDate" value="<%=isDate%>"/>
	<jsp:param name="isEmpty" value="<%=isEmpty%>"/>
</jsp:include>

<% } %>