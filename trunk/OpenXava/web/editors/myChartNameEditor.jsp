<jsp:useBean id="context" class="org.openxava.controller.ModuleContext" scope="session"/>
<jsp:useBean id="style" class="org.openxava.web.style.Style" scope="request"/>

<%@ page import="org.openxava.util.Is" %>
<%@ page import="org.openxava.model.meta.MetaProperty" %>

<%
String viewObject = request.getParameter("viewObject");
viewObject = (viewObject == null || viewObject.equals(""))?"xava_view":viewObject;
org.openxava.view.View view = (org.openxava.view.View) context.get(request, viewObject);

String tabObject = request.getParameter("tabObject");
tabObject = (tabObject == null || tabObject.equals(""))?"xava_tab":tabObject;
org.openxava.tab.Tab tab = (org.openxava.tab.Tab) context.get(request, tabObject);

String myChartObject = request.getParameter("myChartObject");
myChartObject = (myChartObject == null || myChartObject.equals(""))?"xava_myChart":myChartObject;
org.openxava.session.MyChart myChart = (org.openxava.session.MyChart) context.get(request, myChartObject);

String propertyKey = request.getParameter("propertyKey");
String script = request.getParameter("script");
MetaProperty p = (MetaProperty) request.getAttribute(propertyKey);
String title = (p == null)?"":p.getDescription(request);
String fvalue = (String) request.getAttribute(propertyKey + ".fvalue");
String value = (String) request.getAttribute(propertyKey + ".value");
boolean editable = "true".equalsIgnoreCase(request.getParameter("editable"));
boolean label = org.openxava.util.XavaPreferences.getInstance().isReadOnlyAsLabel() || "true".equalsIgnoreCase(request.getParameter("readOnlyAsLabel"));

if (!editable) {
%>
<select id="<%=propertyKey%>" name="<%=propertyKey%>" tabindex="1" class=<%=style.getEditor()%> <%=script%> title="<%=title%>">	
<%
		// current user
		java.util.List<String> nodeNames = org.openxava.util.MyCharts.INSTANCE.getAllChartNodeNames(tab);
		for (int i = 0; i < nodeNames.size(); i++) {
			String selected = "";
			String nodeName = nodeNames.get(i);
			String description = org.openxava.util.MyCharts.INSTANCE.getChartPreferenceName(tab, nodeName);
			if (nodeName.equals(fvalue)) {
				selected = "selected"; 
			}
	%>
		<option value="<%=nodeName%>" <%=selected%>><%=description%></option>
	<%
	}
%>
</select>
<input type="hidden" name="<%=propertyKey%>__DESCRIPTION__" value="<%=fvalue%>">
<% 
} else {
	String description = org.openxava.util.MyCharts.INSTANCE.getChartPreferenceName(tab, fvalue);
	if (!Is.emptyString(description)) {
		if (description.startsWith(org.openxava.util.MyCharts.SHARED_NAME_PREFIX)) {
			description = description.substring(org.openxava.util.MyCharts.SHARED_NAME_PREFIX.length());
		}
		request.setAttribute(propertyKey + ".fvalue", description);
	}
%>	
<jsp:include page="textEditor.jsp">
	<jsp:param name="script" value=""/>
</jsp:include> 
<% 
} 
%>			
