<%@page import="org.openxava.session.MyReport"%>
<%@page import="org.openxava.util.Labels"%>
<jsp:useBean id="context" class="org.openxava.controller.ModuleContext" scope="session"/>
<jsp:useBean id="style" class="org.openxava.web.style.Style" scope="request"/>

<%@ page import="org.openxava.util.KeyAndDescription" %>
<%@ page import="org.openxava.util.Is" %>
<%@ page import="org.openxava.util.XavaResources" %>
<%@ page import="org.openxava.model.meta.MetaProperty" %>
<%@ page import="org.openxava.calculators.DescriptionsCalculator" %>
<%@ page import="org.openxava.formatters.IFormatter" %>
<%@ page import="org.openxava.filters.IFilter" %>
<%@ page import="org.openxava.filters.IRequestFilter" %>
<%@ page import="org.openxava.mapping.PropertyMapping"%>
<%@ page import="org.openxava.converters.IConverter"%>

<%
String viewObject = request.getParameter("viewObject");
viewObject = (viewObject == null || viewObject.equals(""))?"xava_view":viewObject;
org.openxava.view.View view = (org.openxava.view.View) context.get(request, viewObject);
String propertyKey = request.getParameter("propertyKey");
String script = request.getParameter("script");
MetaProperty p = (MetaProperty) request.getAttribute(propertyKey);
String title = (p == null)?"":p.getDescription(request);
String fvalue = (String) request.getAttribute(propertyKey + ".fvalue");
boolean editable = "true".equals(request.getParameter("editable"));
boolean label = org.openxava.util.XavaPreferences.getInstance().isReadOnlyAsLabel() || "true".equalsIgnoreCase(request.getParameter("readOnlyAsLabel"));
org.openxava.session.MyReport report = (org.openxava.session.MyReport) view.getModel();

// 
java.lang.Boolean fromAdminReportsAction = (java.lang.Boolean) context.get(request, "xava_fromAdminReportsAction");
// if (fromAdminReportsAction == null) fromAdminReportsAction = Boolean.FALSE;	// tmp
//
String[] adminUserDescriptions = report.getAllNamesAdminUser();
String[] currentUserDescription = report.getAllNamesCurrentUser();
String sufix = !fromAdminReportsAction ? Labels.get("adminReportSufix") : "";
if (!editable) {
%>
<select id="<%=propertyKey%>" name="<%=propertyKey%>" tabindex="1" class=<%=style.getEditor()%> <%=script%> title="<%=title%>">	
<%	
	
	// current user
	if (!fromAdminReportsAction){
		for (int i=0; i<currentUserDescription.length; i++) {
			String selected = "";
			String description = currentUserDescription[i];		
			if (Is.equalAsStringIgnoreCase(fvalue, description)) {
				selected = "selected"; 
			} 		
	%>
		<option value="<%=description%>" <%=selected%>><%=description%></option>
	<%
		} // del for currentUserDescription
	} // if !fromAdminReportsAction
	
	// admin user
	for (int i=0; i<adminUserDescriptions.length; i++) {
		String selected = "";
		String description = adminUserDescriptions[i];
		String descriptionKey = description + MyReport.ADMIN_REPORT;
		if (!fvalue.endsWith(MyReport.ADMIN_REPORT)) fvalue = fvalue + MyReport.ADMIN_REPORT;
		if (Is.equalAsStringIgnoreCase(fvalue, descriptionKey)) {
			selected = "selected"; 
		} 		
	%>
	<option value="<%=descriptionKey%>" <%=selected%>><%=description%> <%=sufix%></option>
	<%
	} // del for adminUserDescription
%>
</select>
<input type="hidden" name="<%=propertyKey%>__DESCRIPTION__" value="<%=fvalue%>">
<% 
} else {
%>	
<jsp:include page="textEditor.jsp">
	<jsp:param name="script" value=""/>
</jsp:include> 
<% 
} 
%>			
