package org.openxava.escuela.pruebas;

import org.openxava.tests.*;



/**
 * @author Javier Paniza
 */

public class PruebaProfesor extends ModuleTestBase {
	
	
	
	public PruebaProfesor(String testName) {
		super(testName, "MiEscuela", "Profesor");		
	}

	public void testCrearLeerModificarBorrar() throws Exception {
		// Creamos
		execute("CRUD.new");
		setValue("codigo", "JU");
		setValue("nombre", "Profesor JUNIT");
		execute("CRUD.save");
		assertNoErrors();
		assertValue("codigo", "");
		assertValue("nombre", "");
		
		// Leeemos
		setValue("codigo", "JU");
		execute("CRUD.search");
		assertValue("codigo", "JU");
		assertValue("nombre", "Profesor JUNIT");
		
		// Modificamos
		setValue("nombre", "Profesor JUNIT MODIFICADO");
		execute("CRUD.save");
		assertNoErrors();
		assertValue("codigo", "");
		assertValue("nombre", "");
		
		// Comprobamos modificado
		setValue("codigo", "JU");
		execute("CRUD.search");
		assertValue("codigo", "JU");
		assertValue("nombre", "Profesor JUNIT MODIFICADO");
		
		// Borramos
		execute("CRUD.delete");		
		assertMessage("Profesor borrado satisfactoriamente");				
	}
	
}
