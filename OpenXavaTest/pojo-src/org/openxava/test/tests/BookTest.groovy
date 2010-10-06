package org.openxava.test.tests;

import org.openxava.tests.*;

/**
 * @author Javier Paniza
 */

class BookTest extends ModuleTestBase {
	
	BookTest(String testName) {
		super(testName, "Book")		
	}
	
	void testReferenceNameMatchesIdOfReferencedEntityName() throws Exception {
		execute "CRUD.new"
		execute "Reference.search", "keyProperty=author.author"		
		assertListNotEmpty()
		String author = getValueInList(0, 0)
		println "author=$author"
		execute "ReferenceSearch.choose", "row=0"
		assertNoErrors()
		assertValue "author.author", author
	}
	
}
