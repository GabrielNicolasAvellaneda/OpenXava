package org.openxava.actions;

import java.util.*;

import javax.inject.*;

import org.apache.commons.logging.*;
import org.openxava.util.*;
import org.openxava.view.*;

/**
 * tmp ini
 * ¿Añadir showDialog()/hideDialog() y quitar show-dialog/hideDialog
 * - Código más expresivo. showView -> muestra en vista, showDialog -> muestra en diálogo
 * - Menos trabajo, solo escribimos acción.
 * - Lo de declarativo solo no funciona: Tienes que retocar código de acciones normalmente.
 * - Importante problema práctico: 
 * 		Si sobreescribes una acción que saca diágo has de editar su <action /> 
 * 		para que también lo saque. Así que cuando mejoras algo estándar sacando un diálogo, 
 *      has de documentar un cambio que han de aplicar todos.
 * - Sacar o no un diálogo puede ser algo intrinseco a la acción.
 * - Sacar o no un diálogo puede depender de cierta condición.
 * - Si se quiere sacar o no de forma declarativa, podemos tener un propiedad en la acción
 * - Mayor complejidad: Hay que usar showDialog() + editar un .xml
 * - Podemos hacer declarativo bajo demanda.     
 * 
 * tmp fin 
 * @author Javier Paniza
 */

abstract public class ViewBaseAction extends BaseAction {
	
	private static Log log = LogFactory.getLog(ViewBaseAction.class);
		
	@Inject // tmp Mensaje de advertencia en los setters, y migración
	private View view;
	@Inject
	private Stack previousViews;
	
	/**
	 * Creates a new view and shows it. <p>
	 * 
	 * After it if you call to getView() it will return this new view.<br>
	 * 
	 * @since 4m1
	 */
	protected void showNewView() { 
		showView(new View());			
	}
	
	/**
	 * Shows the specified view. <p>
	 * 
	 * After it if you call to getView() it will be the specified view.<br>
	 * 
	 * @since 4m2
	 */	
	protected void showView(View newView) { // tmp 
		assertPreviousViews("showView()");
		getView().putObject("xava.mode", getManager().getModeName());	
		newView.setRequest(getRequest());
		newView.setErrors(getErrors()); 
		newView.setMessages(getMessages());
		getPreviousViews().push(getView());
		setView(newView);		
		setNextMode(DETAIL);
	}
	
	protected void showDialog(View viewToShowInDialog) throws Exception { // tmp doc
		showView(viewToShowInDialog);
		if (getNextControllers() == null) {			
			clearActions();
		}
		getManager().showDialog();
	}
	
	protected void showDialog() throws Exception { // tmp doc
		showDialog(new View());
	}
	
	protected void closeDialog() { // tmp doc
		System.out.println("[ViewBaseAction.closeDialog] "); //  tmp
		returnToPreviousView();
		returnToPreviousControllers();
		getManager().closeDialog();		
	}	
	
	/**
	 * @since 4m1
	 */	
	protected void returnToPreviousView() {		
		if (getPreviousViews() != null && !getPreviousViews().empty()) {				
			View previousView = (View) getPreviousViews().pop();
			previousView.setRequest(getRequest());
			setView(previousView);
			setNextMode((String) getView().getObject("xava.mode"));
		}
		else {
			log.warn(XavaResources.getString(getRequest(), 
				"use_object_previousViews_required", "returnToPreviousView()", getClass().getName())); 
		}
	}
	
	/**
	 * @since 4m1
	 */	
	protected View getPreviousView() {
		assertPreviousViews("getPreviousView()");
		return (View) getPreviousViews().peek();					
	}

		
	private void assertPreviousViews(String method) { // tmp quitar 
		if (previousViews == null) {
			throw new XavaException("use_object_previousViews_required", method, getClass().getName());
		}		
	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}
			
	protected String getModelName() {
		// tmp return view.getModelName();
		return getView().getModelName(); // tmp
	}
	
	/**
	 * Reset the cache of all descriptions-list and 
	 * others uses of descriptionsEditors.	 
	 */
	protected void resetDescriptionsCache() {
		super.resetDescriptionsCache();
		getView().refreshDescriptionsLists();
	}
			
	public Stack getPreviousViews() {
		return previousViews;
	}


	public void setPreviousViews(Stack previousViews) {
		this.previousViews = previousViews;
	}
	
}
