package org.openxava.escuela.pruebas;

import org.openxava.tests.*;



/**
 * @author Javier Paniza
 */

public class PruebaProfesores extends ModuleTestBase {
	
	
	
	public PruebaProfesores(String testName) {
		super(testName, "MiEscuela", "Profesores");		
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
		execute("ConfirmDelete.confirmDelete");
		assertMessage("Objeto borrado satisfactoriamente");				
	}
	
}
