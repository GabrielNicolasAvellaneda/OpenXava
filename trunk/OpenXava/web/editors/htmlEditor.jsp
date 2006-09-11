<%@ page import="org.openxava.model.meta.MetaProperty" %>
<%@ page import="org.openxava.util.Labels" %>
<jsp:useBean id="style" class="org.openxava.web.style.Style" scope="request"/>
<jsp:useBean id="xava_language" class="org.openxava.session.Language" scope="session"/>
<SCRIPT LANGUAGE="JavaScript">
<!--
 function showValue(v){
  	document.getElementById('showValue').innerHTML=v;
  	return true;
 }
 
 function openWindow(url){
 	window.open(url,'ventana','status=no,scrollbars=yes,menubar=no,location=no,resizable=no,height=600,width=750');
 }

// End -->
</script>
<%
String propertyKey = request.getParameter("propertyKey");
MetaProperty p = (MetaProperty) request.getAttribute(propertyKey);
String fvalue = (String) request.getAttribute(propertyKey + ".fvalue");
if ("0".equals(fvalue)) fvalue = "";
boolean editable="true".equals(request.getParameter("editable"));
%>
<% if (editable) { %>
<a href="javascript:openWindow('<%=request.getContextPath()%>/xava/editors/FCKEditor.jsp?nproperty=<%=propertyKey%>')"
title="<%=Labels.get("Collection.edit",xava_language.getLocale())%>">
<img border="0" src="<%=request.getContextPath()%>/xava/images/edit-text.gif" 
title="<%=Labels.get("Collection.edit",xava_language.getLocale())%>" align="right"></a><br>
<%}%>
<input type="hidden" name="<%=propertyKey%>" value='<%=fvalue%>'>	
<div id="showValue"><%=fvalue%></div>
