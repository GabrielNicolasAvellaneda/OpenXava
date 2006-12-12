<%@ page import="java.util.Iterator" %>
<%@ page import="org.openxava.view.View" %>
<%@ page import="org.openxava.view.meta.MetaGroup" %>
<%@ page import="org.openxava.view.meta.PropertiesSeparator" %>
<%@ page import="org.openxava.model.meta.MetaProperty" %>
<%@ page import="org.openxava.model.meta.MetaReference" %>
<%@ page import="org.openxava.model.meta.MetaCollection" %>
<%@ page import="org.openxava.web.WebEditors" %>

<jsp:useBean id="context" class="org.openxava.controller.ModuleContext" scope="session"/>
<jsp:useBean id="style" class="org.openxava.web.style.Style" scope="request"/>
<%
String viewObject = request.getParameter("viewObject");
viewObject = (viewObject == null || viewObject.equals(""))?"xava_view":viewObject;
org.openxava.view.View view = (org.openxava.view.View) context.get(request, viewObject);
String propertyPrefix = request.getParameter("propertyPrefix");
propertyPrefix = (propertyPrefix == null || propertyPrefix.equals(""))?"xava." + view.getModelName() + ".":propertyPrefix;
%>

<% if (view.isFrame()) { %>
<table>
<tr>
<% } %>


<%
Iterator it = view.getMetaMembers().iterator();
boolean first = true;
boolean lastWasEditor = false;
while (it.hasNext()) {
	Object m = it.next();
	if (m instanceof MetaProperty) {		
		MetaProperty p = (MetaProperty) m;		
		if (!PropertiesSeparator.INSTANCE.equals(m)) {	
			boolean hasFrame = WebEditors.hasFrame(p);		
			lastWasEditor = !hasFrame;
			String propertyKey= propertyPrefix + p.getName();
			request.setAttribute(propertyKey, p);
			String urlEditor = "editor.jsp" // in this way because websphere 6 has problems with jsp:param
				+ "?propertyKey=" + propertyKey
				+ "&first=" + first
				+ "&hasFrame=" + hasFrame;
%>
	<jsp:include page="<%=urlEditor%>" />
<%
			first = false;
		}
		else { 
			first = true;						
			if (lastWasEditor && !view.isAlignedByColumns()) { 	
			%>
			</tr></table>			
			<% 
			} 
			lastWasEditor = false;
			%>
			</td></tr>
			<tr>
	<%	}
	}
	else {
		lastWasEditor = false;
	  	if (m instanceof MetaReference) {
			MetaReference ref = (MetaReference) m;
			if (view.displayAsDescriptionsList(ref)) {
				lastWasEditor = true;
				String referenceKey = propertyPrefix +  ref.getName();
				request.setAttribute(referenceKey, ref);			
				String urlDescriptionsList = "descriptionsList.jsp" // in this way because websphere 6 has problems with jsp:param
					+ "?referenceKey=" + referenceKey
					+ "&first=" + first;
	%>
		<jsp:include page="<%=urlDescriptionsList%>"/>
	<%
				first = false;		
			}
			else {
				String viewName = viewObject + "_" + ref.getName();
				View subview = view.getSubview(ref.getName());
				context.put(request, viewName, subview);
				String propertyInReferencePrefix = propertyPrefix + ref.getName() + ".";
				boolean withFrame = subview.isFrame() && 
					(!view.isSection() || view.getMetaMembers().size() > 1);
				if (withFrame || (view.isSection() && view.getMembersNames().size() ==1)) {
	%>		
		<tr><td colspan="4">
	<%	
				}
				if (withFrame) { 
					String labelKey = propertyPrefix + ref.getName() + "_LABEL_";
					String label = view.getLabelFor(ref);
	%>				
		<table class=<%=style.getFrame()%> width='100%' <%=style.getFrameSpacing()%>>
		<tr class=<%=style.getFrameTitle()%>><th align='left' class=<%=style.getFrameTitleLabel()%>>
			<%=style.getFrameTitleStartDecoration()%>
			<span id="<%=labelKey%>"><%=label%></span>
			<%=style.getFrameTitleEndDecoration()%>
		</th></tr>
		<tr><td class=<%=style.getFrameContent()%>>
	<%				} // withFrame
		%>	
		<jsp:include page="detail.jsp"> 
			<jsp:param name="viewObject" value="<%=viewName%>" />
			<jsp:param name="propertyPrefix" value="<%=propertyInReferencePrefix%>" />
		</jsp:include>	
	<%				if (withFrame) {
		%>				
		</td></tr>
		</table>		
		<%
				} // withFrame
			}
		} else if (m instanceof MetaCollection) {
			MetaCollection collection = (MetaCollection) m;
			String urlCollection = "collection.jsp";
			boolean withFrame = !view.isSection() || view.getMetaMembers().size() > 1;
		%>
		<tr><td colspan="4">		
	<%			if (withFrame) {
		%>	
		<table class=<%=style.getFrame()%> width='100%' <%=style.getFrameSpacing()%>>
		<tr class=<%=style.getFrameTitle()%>><th align='left' class=<%=style.getFrameTitleLabel()%>>
			<%=style.getFrameTitleStartDecoration()%>
			<%=collection.getLabel(request)%>
			<%=style.getFrameTitleEndDecoration()%>
		</th></tr>
		<tr><td class=<%=style.getFrameContent()%>>		
	<%			} // withFrame
		%>	
		<jsp:include page="<%=urlCollection%>"> 
			<jsp:param name="collectionName" value="<%=collection.getName()%>"/>
			<jsp:param name="viewObject" value="<%=viewObject%>"/>			
		</jsp:include>
	<%			if (withFrame) {
		%>			
		</td></tr>
		</table>		
	<%			} // withFrame
		} else if (m instanceof MetaGroup) {
			MetaGroup group = (MetaGroup) m;
			String viewName = viewObject + "_" + group.getName();
			View subview = view.getGroupView(group.getName());
			context.put(request, viewName, subview);
		%>
		<%
		if (first) { 
			first = false;
		%>
		<tr><td colspan="4">
		<% }  %>
		<table class="<%=style.getFrame()%>" style="float:left; margin-right:4px" <%=style.getFrameSpacing()%>>
		<tr class=<%=style.getFrameTitle()%>>
		<th align='left' class="<%=style.getFrameTitleLabel()%>">
		<%=style.getFrameTitleStartDecoration()%>
		<%=group.getLabel(request)%>
		<%=style.getFrameTitleEndDecoration()%>
		</th>
		</tr>
				
		<tr><td class=<%=style.getFrameContent()%>>
		<jsp:include page="detail.jsp">
			<jsp:param name="viewObject" value="<%=viewName%>" />
		</jsp:include>
		</td></tr>
		</table>		
	<%
		}
	} // if is not MetaProperty
}
%>
<% if (lastWasEditor) { %>
			</tr></table>
			</td>
<% } %>

<% if (view.isFrame()) { %>
</tr>
</table>
<% } %>

<%
if (view.hasSections()) { 
%>
	<jsp:include page="sections.jsp"/>	
<%
}
%>
