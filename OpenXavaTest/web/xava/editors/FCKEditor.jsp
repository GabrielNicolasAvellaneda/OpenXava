<%@ page language="java" import="com.fredck.FCKeditor.*" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
	<head>
		<title>Edición</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta name="robots" content="noindex, nofollow">
<% 
String nproperty=request.getParameter("nproperty");
String form=request.getParameter("form");
String body="onLoad='updateThis();'";
String flag=request.getParameter("flag");
String vproperty="";
if (flag!=null){
	body="onLoad='updateParent();'";
	vproperty=request.getParameter("Editor");
}
%>	
	<SCRIPT type="text/javascript">

		function FCKeditor_OnComplete( editorInstance )
		{
			window.status = editorInstance.Description ;
		}

	</SCRIPT>
	</head>
	<body <%=body%>>
		<form name="fEditor" method="post" >
<%
FCKeditor oFCKeditor ;
oFCKeditor = new FCKeditor( request, "Editor" ) ;
oFCKeditor.setBasePath( "FCKeditor/" ) ;
oFCKeditor.setValue(vproperty);
oFCKeditor.setHeight("500");
out.println( oFCKeditor.create() ) ;
%>
			<br>
			<INPUT TYPE="submit" VALUE="Enviar">
			<INPUT name="flag" TYPE="hidden" VALUE="1">
		</form>
	<SCRIPT type="text/javascript">	
		<!--
		
		function updateThis(){
			document.fEditor.Editor.value=opener.document.<%=form%>.elements["<%=nproperty%>"].value;
		}
		
		function updateParent() {
		    opener.document.<%=form%>.elements["<%=nproperty%>"].value = document.fEditor.Editor.value;
		    opener.showValue(document.fEditor.Editor.value);
		    self.close();
		    return false;
		}
		//-->
	</SCRIPT>
	</body>
</html>
