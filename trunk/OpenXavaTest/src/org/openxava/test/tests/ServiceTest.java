package org.openxava.test.tests;

import org.openxava.tests.*;


/**
 * @author Javier Paniza
 */

public class ServiceTest extends ModuleTestBase {
			
	public ServiceTest(String nombreTest) {
		super(nombreTest, "Service");		
	}
		
	public void testFocusOnDescriptionsListInsideAggregate() throws Exception {
		execute("CRUD.new");
		setValue("family", "1");
		assertFocusOn("detail.subfamily");
		setValue("detail.subfamily", "1");
		assertFocusOn("detail.type");		
	}
	
	public void testRemoveAggregateFromCollectionWithReferenceToParentAsKey() throws Exception {
		execute("CRUD.new");
		execute("Collection.new", "viewObject=xava_view_section0_additionalDetails");
		setValue("number", "66");
		setValue("description", "JUNIT SERVICE");
		setValue("family", "1");
		setValue("detail.subfamily", "1");
		setValue("detail.type", "2"); // Assuming that 2 exists
		setValue("additionalDetails.subfamily", "1");
		setValue("additionalDetails.type.number", "2");
		execute("Collection.save", "viewObject=xava_view_section0_additionalDetails");		
		setValue("additionalDetails.subfamily", "1");
		setValue("additionalDetails.type.number", "2");
		execute("Collection.save", "viewObject=xava_view_section0_additionalDetails");		
		assertNoErrors();
		
		assertCollectionRowCount("additionalDetails", 2);
		execute("Collection.edit", "row=0,viewObject=xava_view_section0_additionalDetails");
		execute("Collection.remove", "viewObject=xava_view_section0_additionalDetails");		
		assertNoErrors();
		assertCollectionRowCount("additionalDetails", 1);
		
		// Delete it
		execute("CRUD.delete");														
		assertMessage("Service deleted successfully");		
	}
	
	public void testStereotypeDependenOn2StereotypesAndInsideAggregate() throws Exception {
		String [][] empty = {
			{ "", "" }
		};
		
		execute("CRUD.new");
		String [][] familyValues = {
			{ "", "" },
			{ "1" , "SOFTWARE" },
			{ "2" , "HARDWARE" },
			{ "3" , "SERVICIOS" }
		};
		assertValidValues("family", familyValues);
		assertValidValues("detail.subfamily", empty);
		assertValidValues("detail.type", empty);
		
		setValue("family", "1");
		String [][] subfamilyValues = {
			{ "", "" },
			{ "1" , "01 DESARROLLO" },
			{ "2" , "02 GESTION" },
			{ "3" , "03 SISTEMA" }
		};
		assertValidValues("family", familyValues);
		assertValidValues("detail.subfamily", subfamilyValues);
		assertValidValues("detail.type", empty);

		setValue("detail.subfamily", "1");
		String [][] typeValues = {
			{ "", "" },
			{ "2" , "CORREGIR BUG" }
		};
		assertValidValues("family", familyValues);
		assertValidValues("detail.subfamily", subfamilyValues);
		assertValidValues("detail.type", typeValues);
	}
	
	public void testReferenceInAggregateOfCollectionDependentFromPropertyInAggregateAndPropertyAndEntity() throws Exception {
		String [][] empty = {
			{ "", "" }
		};
		
		execute("CRUD.new");
		execute("Collection.new", "viewObject=xava_view_section0_additionalDetails");
		
		String [][] familyValues = {
			{ "", "" },
			{ "1" , "SOFTWARE" },
			{ "2" , "HARDWARE" },
			{ "3" , "SERVICIOS" }
		};
		assertValidValues("family", familyValues);
		assertValidValues("additionalDetails.subfamily", empty);
		assertValidValues("additionalDetails.type.number", empty);
		
		setValue("family", "1");
		String [][] subfamilyValues = {
			{ "", "" },
			{ "1" , "01 DESARROLLO" },
			{ "2" , "02 GESTION" },
			{ "3" , "03 SISTEMA" }
		};
		assertValidValues("family", familyValues);
		assertValidValues("additionalDetails.subfamily", subfamilyValues);
		assertValidValues("additionalDetails.type.number", empty);

		setValue("additionalDetails.subfamily", "1");
		String [][] typeValues = {
			{ "", "" },
			{ "2" , "CORREGIR BUG" }
		};
		assertValidValues("family", familyValues);
		assertValidValues("additionalDetails.subfamily", subfamilyValues);
		assertValidValues("additionalDetails.type.number", typeValues);
	}

	public void testSearchKey() throws Exception { 		
		execute("CRUD.new");
		execute("Sections.change", "activeSection=1");
		
		assertEditable("invoice.year");
		assertEditable("invoice.number");
		assertNoEditable("invoice.amount");
		assertNoEditable("invoice.description");
		assertValue("invoice.year", "");
		assertValue("invoice.number", "");
		assertValue("invoice.amount", "");
		assertValue("invoice.description", "");
		
		setValue("invoice.year", "2007");
		assertValue("invoice.amount", "");
		assertValue("invoice.description", "");
		
		setValue("invoice.number", "2");
		assertValue("invoice.amount", "1,730.00");
		assertValue("invoice.description", "Second service");	
		
		execute("Reference.search", "keyProperty=xava.Service.invoice.number");
		assertValueInList(0, 0, "2007");
		assertValueInList(0, 1, "1");
		assertValueInList(0, 2, "Primer servicio");
		execute("ReferenceSearch.choose", "row=0");
		assertValue("invoice.year", "2007");
		assertValue("invoice.number", "1");	
		assertValue("invoice.amount", "790.00");
		assertValue("invoice.description", "Primer servicio");	
		
	}

					
}
