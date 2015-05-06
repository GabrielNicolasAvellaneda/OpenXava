<%@ include file="../imports.jsp"%>

<%@ page import="java.lang.reflect.InvocationTargetException" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>

<%@ page import="org.apache.commons.beanutils.PropertyUtils" %>
<%@ page import="org.apache.commons.lang.ArrayUtils" %>

<%@ page import="org.openxava.model.MapFacade" %>
<%@ page import="org.openxava.model.meta.MetaProperty" %>
<%@ page import="org.openxava.session.MyChart" %>
<%@ page import="org.openxava.session.MyChartColumn" %>
<%@ page import="org.openxava.util.Is" %>
<%@ page import="org.openxava.util.MyCharts"%>
<%@ page import="org.openxava.util.XavaException" %>

<jsp:useBean id="context" class="org.openxava.controller.ModuleContext" scope="session"/>


<%!private static final org.apache.commons.logging.Log log 
		= org.apache.commons.logging.LogFactory.getLog("myChartDataEditor.jsp");%>

<%
String viewObject = request.getParameter("viewObject");

viewObject = (viewObject == null || viewObject.equals(""))?"xava_view":viewObject;
org.openxava.view.View view = (org.openxava.view.View) context.get(request, viewObject);

org.openxava.tab.Tab tab = (org.openxava.tab.Tab) session.getAttribute("xava_chartTab");

String myChartObject = request.getParameter("myChartObject");
myChartObject = (myChartObject == null || myChartObject.equals(""))?"xava_myChart":myChartObject;
MyChart myChart = (MyChart) context.get(request, myChartObject);

String propertyKey = request.getParameter("propertyKey");
MetaProperty p = (MetaProperty) request.getAttribute(propertyKey);
String fvalue = (String) request.getAttribute(propertyKey + ".fvalue");
String chartTypeName = fvalue.split(MyCharts.CHART_DATA_SEPARATOR)[0];
boolean isGrouped = "true".equalsIgnoreCase(fvalue.split(MyCharts.CHART_DATA_SEPARATOR)[1]);
MyChart.MyChartType chartType = MyChart.MyChartType.valueOf(fvalue.split(MyCharts.CHART_DATA_SEPARATOR)[2]);
boolean labelColumnIsNumber = "true".equalsIgnoreCase(fvalue.split(MyCharts.CHART_DATA_SEPARATOR)[3]);
String labelColumnLabel = fvalue.split(MyCharts.CHART_DATA_SEPARATOR)[4];
boolean addLabelColumnToData = labelColumnIsNumber && 
		(MyChart.MyChartType.PIE.equals(chartType) ||
		MyChart.MyChartType.DONUT.equals(chartType));


String applicationName = request.getParameter("application");
String module = request.getParameter("module");
String idPrefix = org.openxava.web.Ids.decorate(request, "xava_chart__");
%>
	<input type="hidden" id="xava_application" value="<%=applicationName%>" />
	<input type="hidden" id="xava_module" value="<%=module%>" />
	<input type="hidden" name='<xava:id name="data"/>' value="<%=fvalue%>" />
<%
	// Let's read the data
try {
	org.openxava.tab.impl.IXTableModel model = tab.getTableModel();
	Map[] selectedKeys = tab.getSelectedKeys();
	if (selectedKeys == null || selectedKeys.length == 0) {
		selectedKeys = tab.getAllKeys();
	}
	java.util.List<Integer> selected = new java.util.ArrayList<Integer>(); 
	int end = model.getRowCount();
	for (int i = 0; i < end; i++){
		Map key = (Map)model.getObjectAt(i);
		for (Map selectedKey : selectedKeys) {
	if (selectedKey.equals(key)) {
		selected.add(i);
		break;
	}
		}
	}
	int[] selectedRows = ArrayUtils.toPrimitive(selected.toArray(new Integer[selected.size()]));
	SimpleDateFormat sdf = new SimpleDateFormat("yyy/MM/dd");
	// select datasets and create titles
	int columnCount = 0;
	if (addLabelColumnToData) {
		String id = idPrefix + "dataset_" + columnCount++ + "_title";
%>
			<input type="hidden" id='<%=id%>' value="<%=labelColumnLabel%>" />
		<%
			}
			List<MyChartColumn> selectedColumns = new ArrayList<MyChartColumn>();
			for (int index = 0; index < myChart.getColumns().size(); index++) {
				MyChartColumn column = myChart.getColumns().get(index);
				if (!column.isDisplayed() ||
						!column.isNumber()) {
					continue;
				}
				selectedColumns.add(column);
				String id = idPrefix + "dataset_" + columnCount++ + "_title";
		%>
		<input type="hidden" id='<%=id%>' value="<%=column.getLabel()%>" />
		<%
			}
		%>
	<input type="hidden" id='<%=idPrefix + "columnCount"%>' value="<%=selectedColumns.size() + (addLabelColumnToData ? 1 : 0)%>" />
	<%
		if (!Is.emptyString(myChart.getMyChartLabelColumn())) {
	%>
		<input type="hidden" id='<%=idPrefix + "rowCount"%>' value="<%=selectedRows.length%>" />
		<%
			Class labelColumnType = null;
				
			for (int rowIndex = 0; rowIndex < selectedRows.length; rowIndex++) {
			String id = idPrefix + "title_" + rowIndex;
			int row = selectedRows[rowIndex];
			Map keyValues = (Map)model.getObjectAt(row);
			Object object = MapFacade.findEntity(tab.getModelName(), keyValues);
			Object labelColumnObject;
			try {
				labelColumnObject = PropertyUtils.getProperty(object, myChart.getMyChartLabelColumn());
				String labelColumnValue = "";
				if (labelColumnObject != null) {
					if (labelColumnObject instanceof java.util.Date) {
						labelColumnValue = sdf.format((java.util.Date) labelColumnObject);
					} else {
						labelColumnValue = labelColumnObject.toString();
					}
				}
		%>
				<input type="hidden" id="<%=id%>" value="<%=labelColumnValue%>" />
				<%
					int columnIndex = 0;
						// Include the label as part of the data (if numeric)
						if (addLabelColumnToData) {
							String datasetValueIdPrefix = idPrefix + "dataset_" + (columnIndex++) + "_value_";
							if (labelColumnValue == null) {
								labelColumnValue = "0";
							}
				%>
					<input type="hidden" id='<%=datasetValueIdPrefix + rowIndex%>' name="<%=datasetValueIdPrefix%>"
							value="<%=labelColumnValue.toString()%>" />
					<%
						}
							// Process the columns
							for (int index = 0; index < selectedColumns.size(); index++) {
								MyChartColumn column = selectedColumns.get(index);
								Object value = null;
								try {
									String datasetValueIdPrefix = idPrefix + "dataset_" + (columnIndex++) + "_value_";					
									value = PropertyUtils.getProperty(object, column.getName());
									if (value == null) {
										value = "";
									}
					%>
							<input type="hidden" id='<%=datasetValueIdPrefix + rowIndex %>' name="<%=datasetValueIdPrefix%>"
									value="<%=value.toString()%>" />
						<%						
					} catch (IllegalAccessException e) {
						log.error(e.getMessage());
					} catch (InvocationTargetException e) {
						log.error(e.getMessage());
					} catch (NoSuchMethodException e) {
						log.error(e.getMessage());
					} catch (Exception e) {
						log.error(e.getMessage());								
					}
				}
			} catch (IllegalAccessException e1) {
				log.error(e1.getMessage());
			} catch (InvocationTargetException e1) {
				log.error(e1.getMessage());
			} catch (NoSuchMethodException e1) {
				log.error(e1.getMessage());
			}
		}
	}	
} catch (javax.ejb.FinderException e) {
	throw new XavaException(e.getMessage());
}
%>
<input type="hidden" id='<%=idPrefix + "type" %>' value="<%=chartTypeName %>" />
<input type="hidden" id='<%=idPrefix + "grouped" %>' value="<%=isGrouped %>" />
<input type="hidden" id='<%=propertyKey%>' value="<%=fvalue%>" />

<div class="ct-chart ct-perfect-fourth" id='<%=idPrefix + "container" %>' style="width:640px; height:400px; overflow:hidden">
</div>
