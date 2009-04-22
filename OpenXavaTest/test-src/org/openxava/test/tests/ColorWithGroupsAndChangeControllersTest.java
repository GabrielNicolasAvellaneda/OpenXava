package org.openxava.test.tests;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.test.model.Color;
import org.openxava.tests.ModuleTestBase;

/**
 * Create on 05/03/2009 (10:17:49)
 * @autor Ana Andrés
 */
public class ColorWithGroupsAndChangeControllersTest extends ModuleTestBase {
	private static Log log = LogFactory.getLog(ColorWithGroupsAndChangeControllersTest.class);
	
	public ColorWithGroupsAndChangeControllersTest(String testName) {
		super(testName, "ColorWithGroupsAndChangeControllers");		
	}

	public void testViewGroupAndControllerOnChangeGroup() throws Exception {
		assertValue("group", isOX3()?"":"0");
		assertNotExists("property1");
		assertNotExists("property2");
		assertActions(new String[] {});
		setValue("group", String.valueOf(Color.Group.GROUP1.ordinal())); // For OX3
		//setValue("group", "1"); // For OX2
		assertExists("property1");
		assertNotExists("property2");
		assertActions(new String[] { "ReturnPreviousModule.return" });
		setValue("group", String.valueOf(Color.Group.GROUP2.ordinal())); // For OX3
		//setValue("group", "2"); // For OX2
		assertNotExists("property1");
		assertExists("property2");
		assertActions(new String[] { "ReturnPreviousModule.return", "ActionWithImage.new" });
		setValue("group", isOX3()?"":"0");
		assertNotExists("property1");
		assertNotExists("property2");
		assertActions(new String[] {});
	}
}
