<%@page import="org.openxava.util.Is"%>
<%@page import="org.openxava.web.Ids"%>
<%@page import="org.openxava.util.Labels"%>
<%@page import="java.util.Collection"%>
<%@page import="org.openxava.controller.meta.MetaAction"%>
<%@page import="org.openxava.controller.meta.MetaController"%>
<%@page import="org.openxava.controller.meta.MetaControllers"%>
<%@page import="org.openxava.controller.meta.MetaSubcontroller"%>

<%@ include file="imports.jsp"%>

<jsp:useBean id="style" class="org.openxava.web.style.Style" scope="request"/>
<jsp:useBean id="context" class="org.openxava.controller.ModuleContext" scope="session"/>

<%
org.openxava.controller.ModuleManager manager = (org.openxava.controller.ModuleManager) context.get(request, "manager", "org.openxava.controller.ModuleManager");
manager.setSession(session);
String controllerName = request.getParameter("controller");
String image = request.getParameter("image");
String id = Ids.decorate(request, "sc-" + controllerName);
String idContainer = Ids.decorate(request, "sc-container-" + controllerName);
String idButton = Ids.decorate(request, "sc-button-" + controllerName);
String idImage = Ids.decorate(request, "sc-image-" + controllerName);
String idA = Ids.decorate(request, "sc-a-" + controllerName);
String idSpan = Ids.decorate(request, "sc-span-" + controllerName);
%>
<span id='<%=idContainer%>'>
	<span id='<%=idButton%>' class="<%=style.getButtonBarButton()%>">
		<a 
			id ='<%=idA%>'
			href="javascript:openxava.subcontroller('<%=id%>','<%=idContainer%>','<%=idButton%>','<%=idImage%>','<%=idA%>','<%=idSpan%>')" 
			>
			<span
				id='<%=idSpan%>' 
				style="padding:4px; background: url(<%=request.getContextPath()%>/<%=style.getImagesFolder()%>/<%=image%>) no-repeat 5px 50%;">				
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;		
			</span>
			<%= Labels.get(controllerName)%>
			<img  
				id='<%=idImage%>' 
				src='<%=request.getContextPath()%>/<%=style.getImagesFolder()%>/ascending3.gif'/>
			&nbsp;
		</a>
	</span>
	
	<div id="<%=id%>" class="<%=style.getSubcontroller()%>" style="display:none;">
		<table>
		<%
		MetaController controller = MetaControllers.getMetaController(controllerName);
		Collection actions = controller.getMetaActions();
		String mode = request.getParameter("xava_mode"); 
		if (mode == null) mode = manager.isSplitMode()?"detail":manager.getModeName();
		java.util.Iterator itActions = actions.iterator();
		while(itActions.hasNext()){
			MetaAction action = (MetaAction)itActions.next();
			if (action.appliesToMode(mode)) {
		%>	
			<tr><td>
				<jsp:include page="barButton.jsp">
					<jsp:param name="action" value="<%=action.getQualifiedName()%>"/>
					<jsp:param name="addSpaceWithoutImage" value="true"/>
				</jsp:include>
			</td></tr>
		<%
			}
		}
		%>
		</table>
	</div>
</span>	