<%@page import="org.openxava.util.Users"%>

<jsp:useBean id="modules" class="com.openxava.naviox.Modules" scope="session"/>

<%
String module = Users.getCurrent() == null?"SignIn":modules.getCurrent();
%>

<script type="text/javascript">
window.location="m/<%=module%>";
</script>
