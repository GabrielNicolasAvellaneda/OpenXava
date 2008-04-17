package org.openxava.test.tests;

import org.openxava.tests.ModuleTestBase;
import org.openxava.util.Is;

/**
 * Create on 07/04/2008 (12:16:03)
 * @autor Ana Andrés
 */
public class CarrierWithSectionsTest extends ModuleTestBase {
	
	public CarrierWithSectionsTest(String testName) {
		super(testName, "CarrierWithSections");
	}
	
	public void testCarrierSelected() throws Exception{
		assertListNotEmpty();
		execute("List.viewDetail", "row=0");
		execute("CarrierWithSections.fellowCarriersSelected");
		assertTrue(Is.empty(getValue("fellowCarriersSelected")));
		execute("Sections.change", "activeSection=1");
		checkRowCollection("fellowCarriersCalculated", 0);
		checkRowCollection("fellowCarriersCalculated", 1);
		execute("Sections.change", "activeSection=0");
		execute("CarrierWithSections.fellowCarriersSelected");
		assertTrue(getValue("fellowCarriersSelected").equalsIgnoreCase("DOS TRES"));
		execute("Mode.list");
		execute("List.viewDetail", "row=0");
		execute("CarrierWithSections.fellowCarriersSelected");
		assertTrue(Is.empty(getValue("fellowCarriersSelected")));
	}
}
