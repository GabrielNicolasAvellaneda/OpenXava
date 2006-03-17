package org.openxava.test.tests;

import org.openxava.tests.*;



/**
 * @author Javier Paniza
 */

public class FormulasTest extends ModuleTestBase {
	
	public FormulasTest(String testName) {
		super(testName, "OpenXavaTest", "Formulas");		
	}
	
	public void testDependentReferencesAsDescriptionsListWithHiddenKeyInCollection_aggregateCanHasReferenceToModelOfContainerType() throws Exception {
		execute("CRUD.new");		
		execute("Collection.new", "viewObject=xava_view_ingredients");
		assertExists("ingredients.anotherFormula.oid"); // Reference to a model of 'Formula' type, the same of the container
		
		String [][] ingredients = {
			{ "", "" },
			{ "03C5C64CC0A80116000000009590B64C", "AZUCAR" },
			{ "03C59CF0C0A8011600000000618CC74B", "CAFE" },
			{ "03C6E1ADC0A8011600000000498BC537", "CAFE CON LECHE" },
			{ "03C6B61AC0A8011600000000AB4E7ACB", "LECHE" }, 
			{ "03C6C61DC0A801160000000076765581", "LECHE CONDENSADA"} 
		};
		
		String [][] empty = {
			{ "", "" }
		};
		
		String [][] cafeConLeche = {
				{ "", "" },
				{ "03C5C64CC0A80116000000009590B64C", "AZUCAR" },
				{ "03C59CF0C0A8011600000000618CC74B", "CAFE" },		
				{ "03C6B61AC0A8011600000000AB4E7ACB", "LECHE" }, 		 				
		};
		
		assertValidValues("ingredients.ingredient.oid", ingredients);
		assertValidValues("ingredients.accentuate.oid", empty);
		
		setValue("ingredients.ingredient.oid", "03C6E1ADC0A8011600000000498BC537");
		assertValidValues("ingredients.ingredient.oid", ingredients);
		assertValidValues("ingredients.accentuate.oid", cafeConLeche);
	}
		
}
