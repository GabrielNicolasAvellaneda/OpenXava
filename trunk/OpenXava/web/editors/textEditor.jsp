<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.text.DecimalFormatSymbols" %>
<%@ page import="org.openxava.model.meta.MetaProperty" %>
<%@ page import="org.openxava.util.Strings" %>
<%@ page import="org.openxava.util.Align" %>
<%@ page import="org.openxava.util.Locales" %>
<%@ page import="org.openxava.util.XavaResources" %>
<%@ page import="org.apache.commons.logging.LogFactory" %>
<%@ page import="org.apache.commons.logging.Log" %>

<jsp:useBean id="style" class="org.openxava.web.style.Style" scope="request"/>

<%
String propertyKey = request.getParameter("propertyKey");
MetaProperty p = (MetaProperty) request.getAttribute(propertyKey);
String fvalue = (String) request.getAttribute(propertyKey + ".fvalue");
String align = p.isNumber()?"style='text-align:right'":"";
String browser = request.getHeader("user-agent").toLowerCase();
boolean editable="true".equals(request.getParameter("editable"));
String disabled=editable?"":"disabled";
String script = request.getParameter("script");
boolean label = org.openxava.util.XavaPreferences.getInstance().isReadOnlyAsLabel();
String smaxSize = request.getParameter("maxSize");
int maxSize = 0;
if (!org.openxava.util.Is.emptyString(smaxSize)) {
	maxSize = Integer.parseInt(smaxSize);
}
else {
	maxSize = org.openxava.util.XavaPreferences.getInstance().getMaxSizeForTextEditor();
}
int size = p.getSize() > maxSize?maxSize:p.getSize();
int maxLength = p.getSize();
String numericAlt = ""; 
String numericClass = ""; 
if (p.isNumber()) {
	if (p.getScale() > 0) {
		int sizeIncrement = (size - 1) / 3 + 2; // The points/commas for thousands + point/comma for decimal + minus sign
		size += sizeIncrement;
		maxLength += sizeIncrement;
	}
	String integer = p.getScale() == 0?"true":"false";	
	numericAlt = getNumericAlt(p.getSize(), p.getScale());  
	numericClass = "xava_numeric"; 
}	

boolean fillWithZeros = "true".equals(request.getParameter("fillWithZeros"));
if (fillWithZeros && fvalue.length() > 0) {	
	fvalue = Strings.fix(fvalue, size, Align.RIGHT, '0');
}

if (editable || !label) {
%>
<input id="<%=propertyKey%>"
    name="<%=propertyKey%>" class="<%=style.getEditor()%> <%=numericClass%> <%=numericAlt%>"
	type="text" 
	title="<%=p.getDescription(request)%>"
	<%=align%>
	maxlength="<%=maxLength%>" 
	size="<%=size%>"
	value="<%=Strings.change(fvalue, "\"", "&quot;")%>"	
	<%=disabled%>
	<%=script%>
	/>
<%
} else {
%>
<%=fvalue%>&nbsp;	
<%
}
%>
<% if (!editable) { %>
	<input type="hidden" name="<%=propertyKey%>" value="<%=fvalue%>">
<% } %>			


<%!
private static Log log = LogFactory.getLog("textEditor.jsp");

private String getNumericAlt(int size, int scale) {
	try {		
		DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(Locales.getCurrent());
		DecimalFormatSymbols symbols = df.getDecimalFormatSymbols();
		StringBuffer result = new StringBuffer("{");
		result.append("aNeg:'-' "); // Negatives always allowed
		result.append(",mNum:");
		result.append(Integer.toString(size));
		if (scale == 0 || !df.isGroupingUsed()) {
			result.append(",aSep:'' "); // no grouping separator
		} else {
			result.append(",aSep:'");
			result.append(symbols.getGroupingSeparator());
			result.append("'");
			result.append(",dGroup:");
			result.append(df.getGroupingSize());
		}
		result.append(",aDec:'");
		result.append(symbols.getDecimalSeparator());
		result.append("'");
		result.append(",mDec:");
		result.append(Integer.toString(scale)); // Scale
		result.append("}");
		return result.toString();
	}
	catch (Exception ex) {
		log.warn(XavaResources.getString("default_numeric_editor_configuration")); 
		return "{mDec:2}";
	}
}
%>