package org.openxava.actions;

/**
 * Permite navegar a una vista con posibilidad de carga de fichero. <p>
 * 
 * Esto tiene sentido en aplicaciones web, en donde los formularios
 * para carga de ficheros son diferentes (ENCTYPE="multipart/form-data")
 * a los normales.<br>
 *  
 * Al no hacer referencia directa a http se puede usar en contextos no
 * web; simplemente hay que ignorar esta interfaz.<br>  
 * 
 * @author Javier Paniza
 */

public interface ILoadFileAction extends INavigationAction {
	
	boolean isLoadFile();

}
