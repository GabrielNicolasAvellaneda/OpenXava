<%@ page import="org.openxava.util.Is" %>
<%@ page import="org.openxava.util.XavaResources" %>

<!-- Loading Theme file(s) -->
	<link rel="stylesheet" type="text/css" media="all" href="./calendar/skins/aqua/theme.css" title="Aqua" />
<!-- import the calendar script -->
<script type="text/javascript" src="./calendar/calendar.js"></script>

<!-- import the language module -->
<script type="text/javascript" src="./calendar/lang/calendar-sp.js"></script>

<!-- other languages might be available in the lang directory; please check
your distribution archive. -->

<!-- helper script that uses the calendar -->
<script type="text/javascript">

var oldLink = null;
// code to change the active stylesheet
function setActiveStyleSheet(link, title) {
  var i, a, main;
  for(i=0; (a = document.getElementsByTagName("link")[i]); i++) {
    if(a.getAttribute("rel").indexOf("style") != -1 && a.getAttribute("title")) {
      a.disabled = true;
      if(a.getAttribute("title") == title) a.disabled = false;
    }
  }
  if (oldLink) oldLink.style.fontWeight = 'normal';
  oldLink = link;
  link.style.fontWeight = 'bold';
  return false;
}

// This function gets called when the end-user clicks on some date.
function selected(cal, date) {
  cal.sel.value = date; // just update the date in the input field.
  if (cal.dateClicked && (cal.sel.id == "sel1" || cal.sel.id == "sel3"))
    // if we add this call we close the calendar on single-click.
    // just to exemplify both cases, we are using this only for the 1st
    // and the 3rd field, while 2nd and 4th will still require double-click.
    cal.callCloseHandler();
}

// And this gets called when the end-user clicks on the _selected_ date,
// or clicks on the "Close" button.  It just hides the calendar without
// destroying it.
function closeHandler(cal) {
  cal.hide();                        // hide the calendar
//  cal.destroy();
  _dynarch_popupCalendar = null;
}

// This function shows the calendar under the element having the given id.
// It takes care of catching "mousedown" signals on document and hiding the
// calendar if the click was outside.
function showCalendar(id, format, showsTime, showsOtherMonths) {
  var el = document.getElementById(id);
  if (_dynarch_popupCalendar != null) {
    // we already have some calendar created
    _dynarch_popupCalendar.hide();                 // so we hide it first.
  } else {
    // first-time call, create the calendar.
    var cal = new Calendar(1, null, selected, closeHandler);
    // uncomment the following line to hide the week numbers
    // cal.weekNumbers = false;
    if (typeof showsTime == "string") {
      cal.showsTime = true;
      cal.time24 = (showsTime == "24");
    }
    if (showsOtherMonths) {
      cal.showsOtherMonths = true;
    }
    _dynarch_popupCalendar = cal;                  // remember it in the global var
    cal.setRange(1900, 2070);        // min/max year allowed.
    cal.create();
  }
  _dynarch_popupCalendar.setDateFormat(format);    // set the specified date format
  _dynarch_popupCalendar.parseDate(el.value);      // try to parse the text in field
  _dynarch_popupCalendar.sel = el;                 // inform it what input field we use

  // the reference element that we pass to showAtElement is the button that
  // triggers the calendar.  In this example we align the calendar bottom-right
  // to the button.
  _dynarch_popupCalendar.showAtElement(el.nextSibling, "Br");        // show the calendar

  return false;
}

var MINUTE = 60 * 1000;
var HOUR = 60 * MINUTE;
var DAY = 24 * HOUR;
var WEEK = 7 * DAY;

// If this handler returns true then the "date" given as
// parameter will be disabled.  In this example we enable
// only days within a range of 10 days from the current
// date.
// You can use the functions date.getFullYear() -- returns the year
// as 4 digit number, date.getMonth() -- returns the month as 0..11,
// and date.getDate() -- returns the date of the month as 1..31, to
// make heavy calculations here.  However, beware that this function
// should be very fast, as it is called for each day in a month when
// the calendar is (re)constructed.
function isDisabled(date) {
  var today = new Date();
  return (Math.abs(date.getTime() - today.getTime()) / DAY) > 10;
}

function flatSelected(cal, date) {
  var el = document.getElementById("preview");
  el.innerHTML = date;
}

function showFlatCalendar() {
  var parent = document.getElementById("display");

  // construct a calendar giving only the "selected" handler.
  var cal = new Calendar(0, null, flatSelected);

  // hide week numbers
  cal.weekNumbers = false;

  // We want some dates to be disabled; see function isDisabled above
  cal.setDisabledHandler(isDisabled);
  cal.setDateFormat("%A, %B %e");

  // this call must be the last as it might use data initialized above; if
  // we specify a parent, as opposite to the "showCalendar" function above,
  // then we create a flat calendar -- not popup.  Hidden, though, but...
  cal.create(parent);

  // ... we can show it here.
  cal.show();
}
</script>


<jsp:useBean id="errors" class="org.openxava.util.Messages" scope="request"/>
<jsp:useBean id="messages" class="org.openxava.util.Messages" scope="request"/>
<jsp:useBean id="context" class="org.openxava.controller.ModuleContext" scope="session"/>



<%
boolean messagesOnTop = !"false".equalsIgnoreCase(request.getParameter("messagesOnTop"));
org.openxava.controller.ModuleManager manager = (org.openxava.controller.ModuleManager) context.get(request, "manager", "org.openxava.controller.ModuleManager");
manager.setSession(session);

org.openxava.tab.Tab t = (org.openxava.tab.Tab) context.get(request, "xava_tab");
request.setAttribute("tab", t);
%>
<jsp:useBean id="tab" class="org.openxava.tab.Tab" scope="request"/>
<%
if (manager.isListMode()) {
	tab.deselectVisualizedRows();
}
%>

<% if (!"false".equals(request.getAttribute("xava.sendParametersToTab"))) { %>
<jsp:setProperty name="tab" property="selected"/>
<jsp:setProperty name="tab" property="conditionComparators"/>
<jsp:setProperty name="tab" property="conditionValues"/>
<% } %>


<%
manager.setApplicationName(request.getParameter("application"));
boolean isNew = manager.setModuleName(request.getParameter("module"));
org.openxava.view.View view = (org.openxava.view.View) context.get(request, "xava_view");
if (isNew) { 
	view.setModelName(manager.getModelName());	
	view.setViewName(manager.getXavaViewName());
}
view.setRequest(request);
view.setErrors(errors);
view.setMessages(messages);

tab.setRequest(request);
if (manager.isListMode()) {
	tab.setModelName(manager.getModelName());
	if (tab.getTabName() == null) { 
		tab.setTabName(manager.getTabName());
	}
}
if (manager.isXavaView()) { // here and after action execution
	if (manager.actionOfThisModule(request)) {	
		view.assignValuesToWebView();
	}
}

manager.initModule(request, errors, messages);
if (manager.actionOfThisModule(request)) {
	manager.execute(request, errors, messages);	
	if (manager.isListMode()) { // here and before execute the action
		tab.setModelName(manager.getModelName());	
		if (tab.getTabName() == null) { 
			tab.setTabName(manager.getTabName());
		}
	}
}
String forwardURI = (String) session.getAttribute("xava_forward");
String forwardInNewWindow = (String) session.getAttribute("xava_forward_inNewWindow");
if (!Is.emptyString(forwardURI)) {
	session.removeAttribute("xava_forward");
	session.removeAttribute("xava_forward_inNewWindow");
	if ("true".equals(forwardInNewWindow)) {
%>
<script>
window.open("http://<%=request.getServerName()%>:<%=request.getServerPort()%><%=request.getContextPath()%><%=forwardURI%>");
</script>
<%	
	}
	else {
%>
<script>
location.href="http://<%=request.getServerName()%>:<%=request.getServerPort()%><%=request.getContextPath()%><%=forwardURI%>";
</script>
<%
	}
}
boolean returnToPreviousModule = org.openxava.actions.IChangeModuleAction.PREVIOUS_MODULE.equals(manager.getNextModule());
if (returnToPreviousModule) { 
	org.openxava.controller.ModuleManager parentManager = (org.openxava.controller.ModuleManager) context.get(request.getParameter("application"), request.getParameter("parent"), "manager", "org.openxava.controller.ModuleManager");			
	parentManager.setNextModule(null);
	manager.setNextModule(null);
%>
<jsp:include page="module.jsp">
	<jsp:param name='application' value='<%=request.getParameter("application")%>'/>
	<jsp:param name='module' value='<%=request.getParameter("parent")%>'/>
</jsp:include>
<%	
}
else if (!org.openxava.util.Is.emptyString(manager.getNextModule())) {
%>
<jsp:include page="module.jsp">
	<jsp:param name='application' value='<%=request.getParameter("application")%>'/>
	<jsp:param name='module' value='<%=manager.getNextModule()%>'/>
	<jsp:param name='parent' value='<%=request.getParameter("module")%>'/>
</jsp:include>
<%
}
else { // All else
%>


<script>
function executeXavaAction(takesLong, formu, action) {
	if (takesLong) {
		document.getElementById('processingLayer').style.display='block';
		setTimeout('document.images["processingImage"].src = "images/processing.gif"', 1);		
	}
	formu.focus_forward.value = "false";
	formu.xava_action.value=action;	
	formu.submit();	
}
function executeXavaAction(takesLong, formu, action, argv) {	
	if (takesLong) {
		document.getElementById('processingLayer').style.display='block';
		setTimeout('document.images["processingImage"].src = "images/processing.gif"', 1);
	}
	formu.focus_forward.value = "false";
	formu.xava_action.value=action;	
	formu.xava_action_argv.value=argv;	
	formu.submit();
}
function throwPropertyChanged(property) {	
	document.forms[0].focus_forward.value = "true";
	document.forms[0].focus_property.value=property;	
	document.forms[0].changed_property.value=property;	
	document.forms[0].submit();
}
<% String focusPropertyId = view.getFocusPropertyId(); %>
function setFocus() {
	element = document.<%=manager.getForm()%>.elements['<%=focusPropertyId%>'];
	if (element != null && typeof element.disabled != "undefined" && !element.disabled) {
		element.focus()
	}
}
</script>





<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<html>
<head>
<title>OpenXava - <%=manager.getModuleDescription() %></title>
<link href="<%=request.getContextPath()%>/xava/style/default.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath()%>/xava/style/jetspeed.css" rel="stylesheet" type="text/css">
</head>

<body bgcolor="#ffffff" onload="setFocus()">

<%-- Layer for progress bar --%>
<div id='processingLayer' style='position:absolute;top:100px;left:150px;display:none'>
<table cellspacing='0'>
   <tr class='odd'>
       <td align='center' valign='middle' style='line-height:1.4;padding:25px 80px;border:2px solid #000'>
           <%=XavaResources.getString(request, "processing")%><br/>
           <img src='images/processing.gif' name='processingImage'/>
       </td>
   </tr>
</table>
</div>

<div class="module">
<form name="<%=manager.getForm()%>" method="POST" <%=manager.getEnctype()%>>
<INPUT type="hidden" name="xava_action" value=""/>
<INPUT type="hidden" name="xava_action_argv" value=""/>
<INPUT type="hidden" name="xava_action_application" value="<%=request.getParameter("application")%>"/>
<INPUT type="hidden" name="xava_action_module" value="<%=request.getParameter("module")%>"/>
<INPUT type="hidden" name="changed_property"/>
<INPUT type="hidden" name="focus_property"/>
<INPUT type="hidden" name="focus_forward"/>
<INPUT type="hidden" name="focus_property_id" value="<%=focusPropertyId%>"/>

<jsp:include page="languages.jsp"/>
<table cellpadding="2" cellspacing="2" border="0" width="100%">
  <tbody>
  	<% if (org.openxava.util.XavaPreferences.getInstance().isButtonBarOnTop()) { %>
    <tr>
      <td class=buttonBar>
      	<jsp:include page="buttonBar.jsp"/>
      </td>
    </tr>
    <% } %>
    <% if (messagesOnTop && (errors.contains() || messages.contains())) {  %>
    <tr>
      <td>            
		<jsp:include page="errors.jsp"/>
      </td>
    </tr>    
    <tr>
      <td>
		<jsp:include page="messages.jsp"/>
      </td>
    </tr>            
    <% } %>
    <tr>      		
		<td class=body>
		<jsp:include page="<%=manager.getViewURL()%>"/>
		</td>
    </tr>
  	<% if (org.openxava.util.XavaPreferences.getInstance().isButtonBarOnBottom()) { %>    
    <tr>
      <td class=buttonBar>
		<jsp:include page="buttonBar.jsp"/>
      </td>
    </tr>
  	<% } %>    
    <% if (!messagesOnTop) { %>
    <tr>
      <td>
		<jsp:include page="errors.jsp"/>
      </td>
    </tr>    
    <tr>
      <td>
		<jsp:include page="messages.jsp"/>
      </td>
    </tr>            
    <% } %>
  </tbody>
</table>
</form>
</div>

</body></html>

<%
}
%>
