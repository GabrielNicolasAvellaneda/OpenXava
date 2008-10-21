<%@ include file="imports.jsp"%>

<xava:editor 
	property="<%=request.getParameter("propertyName")%>" 
	editable="<%=Boolean.parseBoolean(request.getParameter("editable"))%>" 
	throwPropertyChanged="<%=Boolean.parseBoolean(request.getParameter("throwPropertyChanged"))%>"/>
	
