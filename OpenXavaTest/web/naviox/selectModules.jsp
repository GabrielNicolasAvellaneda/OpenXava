<%@page import="java.util.Iterator"%>
<%@page import="java.util.Collection"%>
<%@page import="org.openxava.application.meta.MetaModule"%>

<jsp:useBean id="modules" class="com.openxava.naviox.Modules" scope="session"/>

<%
String searchWord = request.getParameter("searchWord");
if (searchWord != null) searchWord.toLowerCase();
Collection modulesList = null;
boolean bookmarkModules = false;
%>
<%@ include file="getModulesList.jsp" %> 
<%
String smodulesLimit = request.getParameter("modulesLimit");
int modulesLimit = smodulesLimit == null?Integer.MAX_VALUE:Integer.parseInt(smodulesLimit);
int counter = 0; 
for (Iterator it= modulesList.iterator(); it.hasNext();) {
	if (counter == modulesLimit) break;
	counter++;
	MetaModule module = (MetaModule) it.next();
	String selected = module.getName().equals(modules.getCurrent())?"selected":""; 
	String label = module.getLabel(request.getLocale()); 
	String description = module.getDescription(request.getLocale()); 
	if (searchWord != null && !label.toLowerCase().contains(searchWord) && !description.toLowerCase().toLowerCase().contains(searchWord)) continue;
%>	
	<a  href="/<%=module.getMetaApplication().getName()%>/m/<%=module.getName()%>">
	<div id="<%=module.getName()%>_module" class="module-row <%=selected%>" onclick="$('#<%=module.getName()%>_loading').show()">	
		<div class="module-name">
			<%=label%>
			<% if (bookmarkModules) { %>
			<img src="<%=request.getContextPath()%>/naviox/images/bookmark-on.png" class="bookmark-decoration"/>
			<% } %>
			<img id="<%=module.getName()%>_loading" src="<%=request.getContextPath()%>/naviox/images/loading.gif" style="float: right; display:none;"/>
		</div>
		<div class="module-description"><%=description%></div>
	</div>	
	</a>
	
<%	
}
%>
