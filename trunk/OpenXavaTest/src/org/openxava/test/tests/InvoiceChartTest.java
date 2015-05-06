package org.openxava.test.tests;

import org.openxava.actions.OnChangeMyChartLabelColumnAction;
import org.openxava.session.MyChart;
import org.openxava.tests.ModuleTestBase;
import org.openxava.util.Users;
import org.openxava.util.XavaResources;

import com.gargoylesoftware.htmlunit.html.DomElement;

/**
 * Tests for charts. Most tests are on selenium since htmlunit has poor support for SVG
 * @author Federico Alcantara
 *
 */
public class InvoiceChartTest extends ModuleTestBase {

	public InvoiceChartTest(String testName) {
		super(testName, "Invoice");		
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		String nodeName = "myChart.tab.OpenXavaTest.Invoice.Invoice";
		// Clear the preferences
		if (Users.getCurrentPreferences().nodeExists(nodeName)) {
			Users.getCurrentPreferences().remove(nodeName);
		}
		if (Users.getSharedPreferences().nodeExists(nodeName)) {
			Users.getSharedPreferences().remove(nodeName);
		}
		Users.getCurrentPreferences().flush();
		Users.getSharedPreferences().flush();
	}
	
	public void testChartElements() throws Exception {
		execute("CRUD.new");
		execute("Mode.list");
		assertListNotEmpty();
		execute("ExtendedPrint.myCharts");
		assertEditable("name");
		assertValue("name", "INVOICE REPORT");
		assertValidValues("myChartType", new String[][]{
				{"", ""},
				{Integer.toString(MyChart.MyChartType.BAR.ordinal()), "Bar"},
				{Integer.toString(MyChart.MyChartType.LINE.ordinal()), "Line"},
				{Integer.toString(MyChart.MyChartType.PIE.ordinal()), "Pie"},
				{Integer.toString(MyChart.MyChartType.DONUT.ordinal()), "Donut"},
				{Integer.toString(MyChart.MyChartType.STACKED_BAR.ordinal()), "Stacked bar"},
				{Integer.toString(MyChart.MyChartType.AREA.ordinal()), "Area"},
				{Integer.toString(MyChart.MyChartType.STACKED_AREA.ordinal()), "Stacked area"},
				{Integer.toString(MyChart.MyChartType.XY.ordinal()), "XY"},
				{Integer.toString(MyChart.MyChartType.SPLINE.ordinal()), "Spline"},
				{Integer.toString(MyChart.MyChartType.STEP.ordinal()), "Step"},
				{Integer.toString(MyChart.MyChartType.STACKED_STEP.ordinal()), "Stacked step"}
		});
		assertValue("myChartLabelColumn", "year");
		assertValidValueExists("myChartLabelColumn", OnChangeMyChartLabelColumnAction.SHOW_MORE, "[SHOW MORE...]");
		assertChartDisplayed();
	}
	
	private void assertChartDisplayed() throws Exception {
		DomElement container = getHtmlPage().getElementById(decorateId("xava_chart__container"));
		if (!container.hasChildNodes() &&
				container.getChildNodes().size() < 10) {
			fail(XavaResources.getString("my_chart_not_displayed"));
		}
	}
	
}


