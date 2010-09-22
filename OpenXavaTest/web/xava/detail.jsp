<%@ include file="imports.jsp"%>

<%@ page import="java.util.Iterator" %>
<%@ page import="org.openxava.view.View" %>
<%@ page import="org.openxava.view.meta.MetaGroup" %>
<%@ page import="org.openxava.view.meta.PropertiesSeparator" %>
<%@ page import="org.openxava.model.meta.MetaProperty" %>
<%@ page import="org.openxava.model.meta.MetaReference" %>
<%@ page import="org.openxava.model.meta.MetaCollection" %>
<%@ page import="org.openxava.web.WebEditors" %>
<%@ page import="org.openxava.web.taglib.IdTag"%>
<%@ page import="org.openxava.web.Ids"%>
<%@ page import="org.openxava.model.meta.MetaMember"%>

<jsp:useBean id="errors" class="org.openxava.util.Messages" scope="request"/>
<jsp:useBean id="context" class="org.openxava.controller.ModuleContext" scope="session"/>
<jsp:useBean id="style" class="org.openxava.web.style.Style" scope="request"/>

<%!
private final static String LAST_TABLE_NOT_CLOSED = "xava.layout.detail.lastTableNotClose";
private boolean hasFrame(MetaMember m, View view) { 
	if (m instanceof MetaProperty) {
		return WebEditors.hasFrame((MetaProperty) m, view.getViewName());
	}
  	if (m instanceof MetaReference) {
  		return !view.displayReferenceWithNoFrameEditor((MetaReference) m);  		
  	}
  	return true;
}
%>

<%
String viewObject = request.getParameter("viewObject");
viewObject = (viewObject == null || viewObject.equals(""))?"xava_view":viewObject;
org.openxava.view.View view = (org.openxava.view.View) context.get(request, viewObject);
view.setViewObject(viewObject); 
String propertyPrefix = request.getParameter("propertyPrefix");
propertyPrefix = (propertyPrefix == null)?"":propertyPrefix; 
view.setPropertyPrefix(propertyPrefix);
boolean onlySections = view.hasSections() && view.getMetaMembers().isEmpty(); 
%>

<% if (!onlySections) { %>

<% if (view.isFrame()) { %>
<table>
<tr>
<% } %>


<%
Iterator it = view.getMetaMembers().iterator();
String sfirst = request.getParameter("first");
boolean first = !"false".equals(sfirst);
String slast = request.getParameter("last");
boolean last = !"false".equals(slast);
boolean lastWasEditor = false;
boolean lastWasProperty = false;
boolean firstNoFrameMember = true; 
boolean firstFrameMember = false; 
while (it.hasNext()) {
	MetaMember m = (MetaMember) it.next();
	lastWasProperty = false;	
	int frameWidth = view.isVariousMembersInSameLine(m)?0:100;	
	if (!PropertiesSeparator.INSTANCE.equals(m)) { 
		if (firstNoFrameMember && !hasFrame(m, view)) {			
				firstNoFrameMember = false;
				firstFrameMember = true;	
				if (request.getAttribute(LAST_TABLE_NOT_CLOSED) == null) { 						
	%>
		</tr><tr><td><table><tr>
	<%
				}
				else {
					request.removeAttribute(LAST_TABLE_NOT_CLOSED);
				}
		}
		else if (firstFrameMember && hasFrame(m, view)){
			firstFrameMember = false;
			firstNoFrameMember = true;
	%>
		</table>
	<%	
		}
	}
	if (m instanceof MetaProperty) {		
		MetaProperty p = (MetaProperty) m;		
		if (!PropertiesSeparator.INSTANCE.equals(m)) {			
			boolean hasFrame = WebEditors.hasFrame(p, view.getViewName());
			lastWasEditor = !hasFrame;
			lastWasProperty = true;
			String propertyKey= Ids.decorate(
					request.getParameter("application"),
					request.getParameter("module"),
					propertyPrefix + p.getName());
			request.setAttribute(propertyKey, p);
			String urlEditor = "editor.jsp" // in this way because websphere 6 has problems with jsp:param
				+ "?propertyKey=" + propertyKey
				+ "&first=" + first
				+ "&hasFrame=" + hasFrame;		
			boolean withFrame = hasFrame && 
				(!view.isSection() || view.getMetaMembers().size() > 1);
			if (withFrame || (view.isSection() && view.getMembersNames().size() ==1)) {
				if (first) { 						
	%>		
		<tr><td colspan="4">
	<%	
				} 
			}
			if (withFrame) { 					 					
				String labelKey = Ids.decorate(
					request.getParameter("application"),
					request.getParameter("module"),
					"label_" + propertyPrefix + p.getName()); 
				String label = view.getLabelFor(p);
	%>					 
		<%=style.getFrameHeaderStartDecoration(frameWidth) %>
		<%=style.getFrameTitleStartDecoration() %>
		<span id="<%=labelKey%>"><%=label%></span>		
		<%@ include file="editorIcons.jsp"%>
		<%=style.getFrameTitleEndDecoration() %>	
		<%=style.getFrameActionsStartDecoration()%>
		<% 
		String frameId = Ids.decorate(request, "frame_" + view.getPropertyPrefix() + p.getName());
		String frameActionsURL = "frameActions.jsp?frameId=" + frameId +
			"&closed=" + view.isFrameClosed(frameId); 
		%>
		<jsp:include page='<%=frameActionsURL%>'/>
		<%=style.getFrameActionsEndDecoration()%> 					 					
		<%=style.getFrameHeaderEndDecoration() %>
		<%=style.getFrameContentStartDecoration(frameId + "content", view.isFrameClosed(frameId))%>
		<%	} // withFrame %> 
	<jsp:include page="<%=urlEditor%>" />		
	<%
			if (withFrame) {
		%>
		<%=style.getFrameContentEndDecoration() %>		
		<%
			} // withFrame		
			first = false;
		}
		else {
			if (!it.hasNext()) break; 					
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
			String referenceKey = Ids.decorate(
					request.getParameter("application"),
					request.getParameter("module"),
					propertyPrefix +  ref.getName()); 
			request.setAttribute(referenceKey, ref);
			if (view.displayReferenceWithNoFrameEditor(ref)) {	
				lastWasEditor = true;			
				String urlReferenceEditor = "reference.jsp" // in this way because websphere 6 has problems with jsp:param
					+ "?referenceKey=" + referenceKey		
					+ "&first=" + first
					+ "&frame=false&composite=false&onlyEditor=false"; 				
	%>
		<jsp:include page="<%=urlReferenceEditor%>"/>
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
				lastWasEditor = !withFrame;
				boolean firstForSubdetail = first || withFrame;
				if (withFrame || (view.isSection() && view.getMembersNames().size() ==1)) {
				
					if (first) { 						
	%>		
		<tr><td colspan="4">
	<%	
					} 
				}
				if (withFrame) { 					 					
					String labelKey = Ids.decorate(
						request.getParameter("application"),
						request.getParameter("module"),
						"label_" + propertyPrefix + ref.getName()); 
					String label = view.getLabelFor(ref);
	%>				
		<%=style.getFrameHeaderStartDecoration(frameWidth) %>
		<%=style.getFrameTitleStartDecoration() %>
		<span id="<%=labelKey%>"><%=label%></span>
		<%=style.getFrameTitleEndDecoration() %>
		<%=style.getFrameActionsStartDecoration()%>
		<% 
		String frameId = Ids.decorate(request, "frame_" + view.getPropertyPrefix() + ref.getName());
		String frameActionsURL = "frameActions.jsp?frameId=" + frameId +
			"&closed=" + view.isFrameClosed(frameId); 		
		%>
		<jsp:include page='<%=frameActionsURL%>'/>
		<%=style.getFrameActionsEndDecoration()%> 					 					
		<%=style.getFrameHeaderEndDecoration() %>
		<%=style.getFrameContentStartDecoration(frameId + "content", view.isFrameClosed(frameId)) %>						
		<%		} // withFrame
		
				String urlReferenceEditor = null;
				if (view.displayReferenceWithNotCompositeEditor(ref)) {
					urlReferenceEditor = "reference.jsp" // in this way because websphere 6 has problems with jsp:param					
						+ "?referenceKey=" + referenceKey
						+ "&onlyEditor=true&frame=true&composite=false"		
						+ "&first=" + first;				
				}
				else {
					urlReferenceEditor = "reference.jsp" // in this way because websphere 6 has problems with jsp:param
						+ "?referenceKey=" + referenceKey
						+ "&onlyEditor=true&frame=true&composite=true"  
						+ "&viewObject=" + viewName					
						+ "&propertyPrefix=" + propertyInReferencePrefix 
						+ "&first=" + firstForSubdetail  
						+ "&last=" + !it.hasNext();
				}			
		%>  
			<jsp:include page="<%=urlReferenceEditor%>"/>
		<%		if (withFrame) {
		%>			
		<%=style.getFrameContentEndDecoration() %>		
		<%
				} // withFrame
			}
			first = false; 
		} else if (m instanceof MetaCollection) {
			MetaCollection collection = (MetaCollection) m;			
			boolean withFrame = !view.isSection() || view.getMetaMembers().size() > 1;
			boolean variousCollectionInLine = view.isVariousCollectionsInSameLine((MetaMember) m);
			boolean firstCollectionInLine = view.isFirstInLine((MetaMember) m);
			String styleCollectionTogether = 
				!variousCollectionInLine ? "" : 
				(firstCollectionInLine ? "float: left; " : "float: right; ") + 
				"overflow: auto; display: block ; border: 1px solid black; width: 49%; ";
			if (!variousCollectionInLine || (variousCollectionInLine && firstCollectionInLine)){
		
		%>
		<tr><td colspan="4">		
		<%	} %>
		<div style="<%=styleCollectionTogether %>">
	<%			if (withFrame) {
		%>	
		<%=style.getFrameHeaderStartDecoration(frameWidth)%>
		<%=style.getFrameTitleStartDecoration()%>
		<%=collection.getLabel(request) %>
		<%=style.getFrameTitleEndDecoration()%>
		<%=style.getFrameActionsStartDecoration()%>
		<% 
		String frameId = Ids.decorate(request, "frame_" + view.getPropertyPrefix() + collection.getName());
		String frameActionsURL = "frameActions.jsp?frameId=" + frameId +
			"&closed=" + view.isFrameClosed(frameId);
		%>
		<jsp:include page='<%=frameActionsURL%>'/>
		<%=style.getFrameActionsEndDecoration()%> 					 					
		<%=style.getFrameHeaderEndDecoration()%>
		<%=style.getFrameContentStartDecoration(frameId + "content", view.isFrameClosed(frameId))%>
	<%			} // withFrame
		%>	
		<jsp:include page="collection.jsp"> 
			<jsp:param name="collectionName" value="<%=collection.getName()%>"/>
			<jsp:param name="viewObject" value="<%=viewObject%>"/>			
		</jsp:include>
	<%			if (withFrame) {
		%>
		<%=style.getFrameContentEndDecoration()%>			
	<%			} // withFrame%>
		</div>
	<%	} else if (m instanceof MetaGroup) {
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
		<%=style.getFrameHeaderStartDecoration(frameWidth)%>
		<%=style.getFrameTitleStartDecoration()%>
		<%=group.getLabel(request)%>
		<%=style.getFrameTitleEndDecoration()%>
		<%=style.getFrameActionsStartDecoration()%>
		<% 
		String frameId = Ids.decorate(request, "frame_group_" + view.getPropertyPrefix() + group.getName());
		String frameActionsURL = "frameActions.jsp?frameId=" + frameId + 
			"&closed=" + view.isFrameClosed(frameId); 
		%>
		<jsp:include page='<%=frameActionsURL%>'/>
		<%=style.getFrameActionsEndDecoration()%> 					 			
		<%=style.getFrameHeaderEndDecoration()%>
		<%=style.getFrameContentStartDecoration(frameId + "content", view.isFrameClosed(frameId)) %>
		<jsp:include page="detail.jsp">
			<jsp:param name="viewObject" value="<%=viewName%>" />
		</jsp:include>
		<%=style.getFrameContentEndDecoration() %>
		
	<%
		}
	} // if is not MetaProperty
}
%>
<% if (lastWasEditor) { %>
		<% if (!(view.isRepresentsEntityReference() || view.isRepresentsAggregate()) || view.isFrame()) {
		%> </tr></table> <% } %>
			</td>			
<% } %>


<% 
	if (firstFrameMember) {		
		if (!(view.isSubview() && !view.isFrame())) {	
%>
	</table>
<%  	}
		else {
			request.setAttribute(LAST_TABLE_NOT_CLOSED, new Boolean(true));
		} 
	}
%>

<% 	
	if (view.isFrame() && 
			!(last && view.getParent() != null && !view.getParent().isFrame()) && 			  		
			!(!lastWasProperty && view.isSection() && view.getMembersNamesWithoutSectionsAndCollections().size() == 1 
					&& view.getParent() != null && view.getParent().isFrame())) {		
%>
</tr>
</table>
<% } %>



<% } // if (!onlySections) %>

<%
if (view.hasSections()) { 
%>
	<% if (!onlySections && view.isSubview() && !view.isFrame()) { %> 
	          </tr>                
              </table>
              </td>
            </tr>
            <tr>
              <td colspan="4">
              <table>                
                  <tr>
                    <td>
	<% } %>
	<div id="<xava:id name='<%="sections_" + viewObject%>'/>"> 
	<jsp:include page="sections.jsp"/>
	</div>
	
	<% if (!onlySections && view.isSubview() && !view.isFrame()) { %>
		 			</td>
	<% } %>
		
<%
}
%>
