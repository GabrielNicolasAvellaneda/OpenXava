package org.openxava.test.tests

import org.openxava.tests.ModuleTestBase 

/**
 * Create on 09/06/2011 (16:04:34)
 * @author Ana Andres
 */
class AuthorTest extends ModuleTestBase {
	
	AuthorTest(String testName) {
		super(testName, "Author")		
	}
	
	void testRemoveColumnAfterFiltering() {
		assertListRowCount 2
		assertListColumnCount 2
		setConditionValues (["J"])
		execute "List.filter"
		assertListRowCount 1
		execute "List.customize"
		execute "List.removeColumn", "columnIndex=1"
		assertListRowCount 1
		assertListColumnCount 1
		execute "List.addColumns"
		execute "AddColumns.restoreDefault"
		assertListColumnCount 2
		assertListRowCount 2 // It would be better 1, but it's difficult for implementing
							// and to lose the filter after clicking a "Restore.." button
							// is not so counter-intuitive  		
	}
	
	void testAddRemoveActionsForProperty() { 
		execute "Mode.detailAndFirst"
		assertNoAction "Author.addSuffix"
		execute "Author.showAddSuffix"
		assertAction "Author.addSuffix"
		assertValue "author", "JAVIER PANIZA"
		execute "Author.addSuffix", "xava.keyProperty=author"
		assertValue "author", "JAVIER PANIZA LUCAS"
		assertAction "Author.addSuffix"
		execute "Author.hideAddSuffix"
		assertNoAction "Author.addSuffix"
	}
	
	void testOverwritingDefaultSearch() {
		execute "Mode.detailAndFirst"
		assertMessage "Showing author JAVIER PANIZA"
		assertValue "author", "JAVIER PANIZA"
		execute "Navigation.next"
		assertMessage "Showing author MIGUEL DE CERVANTES"
		assertValue "author", "MIGUEL DE CERVANTES"
		execute "CRUD.search"
		setValue "author", "JAVIER PANIZA"
		execute "Search.search"
		assertMessage "Showing author JAVIER PANIZA"
		assertValue "author", "JAVIER PANIZA"
		execute "Mode.list"
		execute "List.viewDetail", "row=1"
		assertMessage "Showing author MIGUEL DE CERVANTES"
		assertValue "author", "MIGUEL DE CERVANTES"
	}
	

	void testCollectionViewWithGroup() {
		assertLabelInList(0, "Author")
		assertValueInList(1, 0, "MIGUEL DE CERVANTES")
		execute("List.viewDetail", "row=1")
		assertCollectionRowCount("humans", 1)
		execute("Collection.view", "row=0,viewObject=xava_view_humans")
		assertNoErrors()
		assertDialog()
	}
	
	void testCustomMessageWithBeanValidationJSR303() {
		execute "CRUD.new"
		setValue "author", "PEPE"
		execute "CRUD.save"
		assertError "Sorry, but PEPE is not a good name for an author"  
	}

}
