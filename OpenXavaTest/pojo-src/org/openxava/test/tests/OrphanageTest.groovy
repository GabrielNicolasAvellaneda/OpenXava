package org.openxava.test.tests;

import org.openxava.tests.*;
import org.openxava.test.model.*;

/**
 * @author Javier Paniza
 */

class OrphanageTest extends ModuleTestBase {
	
	OrphanageTest(String testName) {
		super(testName, "Orphanage")		
	}
	
	void testOrphanRemovalAsEmbedded() {
		assertEquals 2, Orphan.count()
		execute "Mode.detailAndFirst"
		assertCollectionRowCount "orphans", 2
		execute "Collection.new", "viewObject=xava_view_orphans"
		setValue "name", "PEDRO"
		execute "Collection.save"
		assertEquals 3, Orphan.count()
		assertCollectionRowCount "orphans", 3
		execute "Collection.removeSelected", "row=2,viewObject=xava_view_orphans"
		assertCollectionRowCount "orphans", 2
		assertEquals 2, Orphan.count()
		assertMessage "Orphan deleted from database"
	}
	
	void testXavaEditorInCustomView() {
		execute "Mode.detailAndFirst"
		execute "Orphanage.proposeName"
		assertValue "name", "EL INTERNADO"
		setValue "name", "THE ORPHANAGE VII"
		execute "ProposeName.propose"
		assertMessage "I think that THE ORPHANAGE VII is already a good name"
	}
	
}
