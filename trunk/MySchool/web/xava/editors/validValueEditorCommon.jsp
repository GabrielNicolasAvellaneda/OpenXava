<%@ page import="org.openxava.model.meta.MetaProperty" %> 
 
<% 
String propertyKey = request.getParameter("propertyKey"); 
String script = request.getParameter("script"); 
MetaProperty p = (MetaProperty) request.getAttribute(propertyKey); 
Object ovalue = request.getAttribute(propertyKey + ".value"); 
int baseIndex = 1; 
int value = 0; 
if (p.isNumber()) value = ovalue==null?0:((Integer) ovalue).intValue();
else {
	// We assume that if it isn't Number then it's an Enum of Java 5, we use instropection
	// to allow this code run in a Java 1.4 servlet container.
	baseIndex = 0;
	if (ovalue == null) {
		value = -1;	
	}
	else if (ovalue instanceof Number) { // Directly the ordinal
		value = ((Number) ovalue).intValue();
	}
	else { // An object of enum type
		value = ((Integer) org.openxava.util.Objects.execute(ovalue, "ordinal")).intValue();
	}
}
 
boolean editable = "true".equals(request.getParameter("editable")); 
boolean label = org.openxava.util.XavaPreferences.getInstance().isReadOnlyAsLabel(); 
%>
