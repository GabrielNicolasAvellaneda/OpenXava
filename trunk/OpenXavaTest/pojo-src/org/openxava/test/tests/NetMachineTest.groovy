package org.openxava.test.tests

import org.openxava.tests.*;

/**
 * 
 * @author Javier Paniza
 */
class NetMachineTest extends ModuleTestBase {

	NetMachineTest(String testName) {
		super(testName, "NetMachine")
	}
	
	void testMAC() {
		assertListRowCount 0
		execute "CRUD.new"
		setValue "name", "JUNIT"
		setValue "mac", "1"
		execute "CRUD.save"
		assertError "Invalid MAC"
		setValue "mac", "00:0D:61:2A:CB:B0"
		execute "CRUD.save"
		assertNoErrors()
		execute "Mode.list"
		assertListRowCount 1
		execute "CRUD.deleteRow", "row=0"
		assertListRowCount 0
	}
	
}
