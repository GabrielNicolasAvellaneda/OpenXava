package org.openxava.test.tests

import org.openxava.tests.*

/**
 *
 * @author Jeromy Altuna
 */
class ExamTest extends ModuleTestBase {
	
	
	ExamTest(String testName){
		super(testName, "Exam")
	}
	
	void testRemoveElementsFromQuestioningAndLeaveAtLeastOneQuestion(){
		assertListRowCount 0
		execute "CRUD.new"
		setValue "name", "ADMISSION"
		execute "Collection.new", "viewObject=xava_view_questioning"
		(1..3).each {
			setValue "name", "$it"
			execute "Collection.saveAndStay"
			assertMessage "Question created successfully" 
		}
		execute "Collection.hideDetail"
		assertValue "name", "ADMISSION"
		assertCollectionRowCount "questioning", 3
		
		checkAllCollection "questioning"
		execute "Collection.removeSelected", "viewObject=xava_view_questioning"
		assertError "It's required at least 1 Element in Questioning" 
		uncheckRowCollection "questioning", 0
		uncheckRowCollection "questioning", 1
		execute "Collection.removeSelected", "viewObject=xava_view_questioning"
		assertMessage "Question deleted from database"
		assertCollectionRowCount "questioning", 2
		execute "Collection.edit", "row=1,viewObject=xava_view_questioning"
		execute "Collection.remove"
		assertMessage "Question deleted from database"
		assertCollectionRowCount "questioning", 1
		execute "Collection.removeSelected", "row=0,viewObject=xava_view_questioning"
		assertError "It's required at least 1 Element in Questioning"
		execute "Collection.edit", "row=0,viewObject=xava_view_questioning"
		execute "Collection.remove"
		assertError "It's required at least 1 Element in Questioning"
		execute "Collection.hideDetail"
		assertCollectionRowCount "questioning", 1
		
		execute "CRUD.delete"
		assertNoErrors()
	}
	
}
