<%@ include file="../imports.jsp"%>

<%@ page import="java.util.Iterator" %>
<%@ page import="org.openxava.util.Strings" %>
<%@ page import="org.openxava.session.GalleryImage" %>

<jsp:useBean id="context" class="org.openxava.controller.ModuleContext" scope="session"/>
<jsp:useBean id="style" class="org.openxava.web.style.Style" scope="request"/>

<%
final int IMAGES_BY_ROW = 6;
final int MINIMIZED_SIZE = 100;
org.openxava.session.Gallery gallery = (org.openxava.session.Gallery) context.get(request, "xava_gallery");
String applicationName = request.getParameter("application");
String module = request.getParameter("module");
long dif=System.currentTimeMillis(); // to avoid browser caching
%>

<table class=<%=style.getFrame()%> width='100%' style="float:left; margin-right:4px" <%=style.getFrameSpacing()%>>
<tr class=<%=style.getFrameTitle()%>>
<th align='left' class=<%=style.getFrameTitleLabel()%>>
<%=style.getFrameTitleStartDecoration()%>
<%=gallery.getTitle()%>
<% if (gallery.isMaximized()) { %>
<xava:link action='Gallery.minimizeImage'><img src='<%=style.getRestoreImage()%>' border='0' align="absmiddle"/></xava:link>
<% } %>
<%=style.getFrameTitleEndDecoration()%>
</th>
</tr>
<tr>

<%
if (gallery.isMaximized()) {
%>
<td class=<%=style.getFrameContent()%> colspan="2">
<xava:link action='Gallery.minimizeImage'>
<img src='<%=request.getContextPath()%>/xava/gallery?application=<%=applicationName%>&module=<%=module%>&oid=<%=gallery.getMaximizedOid()%>&dif=<%=dif%>' border="0"/>	
</xava:link>
<%
}
else {
%>
<td class=<%=style.getFrameContent()%>>
<table>
<tr>
<%
	int c = 0;
	for (Iterator it = gallery.getImages().iterator(); it.hasNext(); ) {
		GalleryImage image = (GalleryImage) it.next();
		if (c++ % IMAGES_BY_ROW == 0) {
%>
</tr><tr>
<%
		}
%>
<td>

<table class=<%=style.getFrame()%> width='100%' <%=style.getFrameSpacing()%>>
<tr class=<%=style.getFrameTitle()%>><th align='right' class=<%=style.getFrameTitleLabel()%>>
	<%=style.getFrameTitleStartDecoration(org.openxava.util.Align.RIGHT)%>
	<xava:link action='Gallery.maximizeImage' argv='<%="oid=" + image.getOid()%>'><img src='<%=style.getMaximizeImage()%>' border='0' align="absmiddle"/></xava:link>	
	<% if (!gallery.isReadOnly()) { %>
	<xava:link action='Gallery.removeImage' argv='<%="oid=" + image.getOid()%>'><img src='<%=style.getRemoveImage()%>' border='0' align="absmiddle"/></xava:link>
	<% } %>	
	<%=style.getFrameTitleEndDecoration()%>
</th></tr>
<tr><td class=<%=style.getFrameContent()%> colspan="<%=gallery.isReadOnly()?2:3%>">

	<input type="hidden" name="xava.GALLERY.images" value="<%=image.getOid()%>">
	<xava:link action='Gallery.maximizeImage' argv='<%="oid=" + image.getOid()%>'>
	<img src='<%=request.getContextPath()%>/xava/gallery?application=<%=applicationName%>&module=<%=module%>&oid=<%=image.getOid()%>&dif=<%=dif%>'
		width="<%=MINIMIZED_SIZE%>" height="<%=MINIMIZED_SIZE%>" border="0"/>
	</xava:link>		
</td></tr>
</table>		

</td>
<%
	} // for
%>
</tr>
<%-- The next row is for giving enough width for showing the heading in one line --%>
<tr><td><%=Strings.repeat(gallery.getTitle().length()*2+10, "&nbsp;")%></td></tr>
</table>
<%
} // if maximized
%>
<%-- The next row is for giving enough width for showing the heading in one line (needed when maximized is on) --%>
</td></tr>
<tr><td><%=Strings.repeat(gallery.getTitle().length()*2+10, "&nbsp;")%></td></tr>
</table>		
