<%@include file="../xava/imports.jsp"%>

<%-- To put your own text add entries in the i18n messages files of your project --%>

<%
String language = "es".equals(request.getLocale().getLanguage()) || "ca".equals(request.getLocale().getLanguage())?"es":"en";
%>

<div id="first_steps">

<h2><xava:message key="first_steps_section1_title"/></h2>
<p><xava:message key="first_steps_section1_content1"/></p>
<p class="screenshot"><img src="../naviox/images/naviox-modules_<%=language%>.png"/></p>
<p><xava:message key="first_steps_section1_content2"/></p>

<h2><xava:message key="first_steps_section2_title"/></h2>
<p><xava:message key="first_steps_section2_content1"/></p>
<p class="screenshot"><img src="../naviox/images/naviox-roles_<%=language%>.png"/></p>
<p><xava:message key="first_steps_section2_content2"/></p>
<p><xava:message key="first_steps_section2_content3"/></p>

<h2><xava:message key="first_steps_section3_title"/></h2>
<p><xava:message key="first_steps_section3_content1"/></p>
<p class="screenshot"><img src="../naviox/images/naviox-folders_<%=language%>.png"/></p>
<p><xava:message key="first_steps_section3_content2"/></p>
<p><xava:message key="first_steps_section3_content3"/></p>

<h2><xava:message key="first_steps_section4_title"/></h2>
<p><xava:message key="first_steps_section4_content1"/></p>
<p><xava:message key="first_steps_section4_content2"/></p>
<p><xava:message key="first_steps_section4_content3"/></p>
<p class="screenshot"><img src="../naviox/images/naviox-module-labels_<%=language%>.png"/></p>

<h2><xava:message key="first_steps_section5_title"/></h2>
<p><xava:message key="first_steps_section5_content1"/></p>
<p><xava:message key="first_steps_section5_content2"/></p>


</div>