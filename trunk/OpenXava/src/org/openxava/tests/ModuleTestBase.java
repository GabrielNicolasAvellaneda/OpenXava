package org.openxava.tests;

import java.io.*;
import java.net.*;
import java.util.*;

import org.openxava.application.meta.*;
import org.openxava.component.*;
import org.openxava.controller.meta.*;
import org.openxava.model.meta.*;
import org.openxava.tab.meta.*;
import org.openxava.util.*;
import org.openxava.view.meta.*;

import junit.framework.*;

import com.meterware.httpunit.*;

/**
 * @author Javier Paniza
 */

public class ModuleTestBase extends TestCase {
	
	private List parameters;
	private static String port;	
	private MetaModel metaModel;
	private MetaView metaView;
	private String moduleLink;

	private final static String EDITABLE_SUFIX = "_EDITABLE_";

	private MetaTab metaTab;

	private final static String ACTION_PREFIX = "xava.action";
	
	private String propertyPrefix;
	private String application;
	private String module;
	private WebConversation conversation;
	private WebResponse response;
	private WebForm form;
	private static Properties xavaJunitProperties;
	private static boolean isLocaleSet = false;
	private static String locale;
	
	public ModuleTestBase(String nameTest, String aplicacion, String modulo) {
		super(nameTest);
		MetaControllers.setContext(MetaControllers.WEB);
		this.application = aplicacion;
		this.module = modulo;
	}
	
	protected void setUp() throws Exception {
		resetModule();
	}
	
	/**
	 * Like close navigator, open again, and reexecute the module.
	 */
	protected void resetModule() throws Exception {
		conversation = new WebConversation();
		if (getLocale() != null) {
			conversation.setHeaderField("Accept-Language", getLocale());
			Locale.setDefault(new Locale(getLocale()));
		}
		response = conversation.getResponse( "http://localhost:" + getPort() + "/" + application + "/xava/module.jsp?application="+application+"&module=" + module);
		setForm(response.getForms()[0]);	
		propertyPrefix = null;		
	}
	
	protected void changeModule(String module) throws Exception {
		this.module = module;
		response = conversation.getResponse( "http://localhost:" + getPort() + "/" + application + "/xava/module.jsp?application="+application+"&module=" + module);
		setForm(response.getForms()[0]);
		propertyPrefix = null;
	}
	
	/**
	 * The model used as prefix to method like getValue, assertValue, etc. <p>
	 * 
	 * By default is the model of module.
	 * The effect of the this setting is only for the life of one test.
	 */
	protected void setModel(String defaultModel) {
		propertyPrefix = "xava." + defaultModel + ".";
	}
	
	/**
	 * Put the model of the module as the default model of the test. <p>
	 * 
	 * This is the default setting, hence this method is called
	 * to restore the original setting.	 
	 */
	protected void setModelToModuleSetting() {
		propertyPrefix = null;
	}

	/**
	 * Simula la pulsación de un botón o vínculo.
	 * 
	 * Para algunas cosas es necesario, porque ejecutar no lanza
	 * todos los eventos posibles. Pero no funciona bien en todos
	 * los casos (a veces parece como si se pulsara 2 veces) así
	 * que ejecutar sigue siendo favorito. 
	 */	
	protected void click(String action) throws Exception { 
		Button b = getForm().getButtonWithID(action);
		if (b != null) {
			b.click();
			response = conversation.getCurrentPage();
		}
		else {
			WebLink l = response.getLinkWithID(action);
			if (l != null) {
				response = l.click();
			}
			else {
				fail(XavaResources.getString("clickable_not_found", action)); 
			}
		}
								
		if ("text/html".equals(response.getContentType())) {
			setForm(response.getForms()[0]);						
		}						
	}
	
	
	/**
	 * Ejecuta mediante javascript la acción, es el que se suele usar.
	 */
	protected void execute(String accion) throws Exception {
		assertAction(accion);
		response.getScriptableObject().setLocation("javascript:executeXavaAction(document.forms[0], '" + accion + "')");
		response = conversation.getCurrentPage();
		if ("text/html".equals(response.getContentType())) {
			setForm(response.getForms()[0]);						
		}						
	}
										 	
	protected void assertFocusOn(String name) throws Exception {
		String propiedadFoco = getForm().getParameterValue("focus_property_id");
		assertEquals(XavaResources.getString("focus_in_unexpected_place"), getPropertyPrefix() + name, propiedadFoco);		
	}
	
	protected void execute(String accion, String argumentos) throws Exception {
		assertAction(accion);
		response.getScriptableObject().setLocation("javascript:executeXavaAction(document.forms[0], '" + accion + "', '" + argumentos+ "')");
		response = conversation.getCurrentPage();
		setForm(response.getForms()[0]);								
	}
	
	protected void executeDefaultAction() throws Exception {
		SubmitButton boton = getForm().getSubmitButtons()[0];
		boton.click();
		response = conversation.getCurrentPage();
		setForm(response.getForms()[0]);								
	}
	
	protected void assertExists(String name) throws Exception {		
		String id = getPropertyPrefix() + name; 
		assertTrue(XavaResources.getString("must_to_exists", name), parameters.contains(id));
	}

	protected void assertNotExists(String name) throws Exception {		
		String id = getPropertyPrefix() + name; 		
		assertTrue(XavaResources.getString("must_not_to_exists", name), !parameters.contains(id));
	}
	
	/**
	 * In the case of combo (descriptionsEditor.jsp) (or his read only version)
	 * the value that is visualized to user.
	 * @return
	 */
	protected String getDescriptionValue(String name) throws Exception {		
		return getForm().getParameterValue(getPropertyPrefix() + name + "__DESCRIPTION__");
	}
		
	protected String getValue(String name) throws Exception {		
		return getForm().getParameterValue(getPropertyPrefix() + name);
	}
	
	protected String getLabel(String name) throws Exception {		
		return response.getElementWithID(getPropertyPrefix() + name + "_LABEL_").getText().trim();
	}
	
	
	/**
	 * Por si no trabajamos con la vista principal
	 */
	protected String getValue(String modelo, String name) throws Exception {		
		return getForm().getParameterValue("xava." + modelo +"." + name);
	}
	
	
	// Solo para depurar
	protected void printHtml() throws Exception {
		System.out.println(response.getText());
	}
	
	/**
	 * Util for web applications, but using it make the test web dependent. 
	 * 
	 * @param type text/html, application/pdf, etc.
	 */
	protected void assertContentType(String type) {
		assertEquals(XavaResources.getString("content_type_not_match"), type, response.getContentType());
	}
	
	/**
	 * Util for web applications, but using it make the test web dependent. 
	 * 
	 * @param type text/html, application/pdf, etc.
	 */
	protected void assertContentTypeForPopup(String type) {
		assertEquals(XavaResources.getString("content_type_not_match"), type, getPopupResponse().getContentType());
	}	
	
	/**
	 * Response for a second window
	 * @return
	 */
	private WebResponse getPopupResponse() {
		WebWindow [] windows = conversation.getOpenWindows();
		if (windows.length < 2) {
			fail(XavaResources.getString("popup_window_not_found"));
		}
		if (windows.length > 2) {
			fail(XavaResources.getString("more_than_one_window"));
		}
		return windows[1].getCurrentPage();		
	}
	
	protected void assertNoPopup() throws Exception {
		WebWindow [] windows = conversation.getOpenWindows();
		assertTrue(XavaResources.getString("unexpected_popup"), windows.length < 2);
	}
	
	/**
	 * Código html actual. <p>
	 * No es aconsejable usarlo mucho pues el test tendría dependencia
	 * con html y sería más difícil de portal.	 
	 */
	protected String getHtml() throws IOException {
		return response.getText();
	}
	
	/**
	 * The text of the response
	 */
	protected String getText() throws IOException {
		return response.getText();
	}

	/**
	 * The text of the response for popup window
	 */
	protected String getPopupText() throws IOException {
		return getPopupResponse().getText();
	}

	protected void setConditionValues(String [] values) throws Exception {
		getForm().setParameter("conditionValues", values);
	}
	
	protected void setConditionComparators(String [] values) throws Exception {
		getForm().setParameter("conditionComparators", values);
	}
	
	protected void setValueNotNotify(String name, String value) throws Exception {
		getForm().setParameter(getPropertyPrefix() + name, value);
	}
	
	protected void setValue(String name, String value) throws Exception {
		boolean checkbox = false;
		if ("true".equals(value) || "false".equals(value)) { // maybe a checkbox
			try {
				getForm().setCheckbox(getPropertyPrefix() + name, "true".equals(value));
				checkbox = true;
			}
			catch (IllegalArgumentException ex) {
				// Not a checkbox
				getForm().setParameter(getPropertyPrefix() + name, value);
			}
		}
		else {
			getForm().setParameter(getPropertyPrefix() + name, value);			
		}
		
		// If onchange then reload the page, because onchange do submit
		HTMLElement el = response.getElementsWithName(getPropertyPrefix() +  name)[0];
		String onchange = el.getAttribute("onchange");		
		if (!Is.emptyString(onchange)) {
			if (checkbox) { // in checkbox case is needed throw onchange event
				response.getScriptableObject().setLocation("javascript:" + onchange);
			}
			response = conversation.getCurrentPage();
			setForm(response.getForms()[0]);	
		}
		
	}
	
	protected void setFileValue(String name, String filePath) throws Exception {
		File file = new File(filePath);		
		UploadFileSpec [] up =  { new UploadFileSpec(file) };
		getForm().setParameter(name, up);		
	}
	
	/**
	 * Por si no trabajamos con la vista principal.
	 */
	protected void setValue(String modelo, String name, String value) throws Exception {		
		getForm().setParameter("xava." + modelo + "." + name, value);
	}
	
	protected void assertLabel(String name, String expectedLabel) throws Exception {		
		assertEquals(XavaResources.getString("unexpected_label", name), expectedLabel, getLabel(name));		
	}
	
	
	protected void assertValue(String name, String value) throws Exception {		
		if ("false".equals(value) && !getForm().isTextParameter(getPropertyPrefix() + name)) { // possibly a checkbox
			value = null; 
		}
		assertEquals(XavaResources.getString("unexpected_value", name), value, getValue(name));		
	}
		
	
	protected void assertValueIgnoringCase(String name, String value) throws Exception {		
		assertTrue(XavaResources.getString("unexpected_value", name), value.equalsIgnoreCase(getValue(name)));		
	}
	
	
	protected void assertValue(String modelo, String name, String value) throws Exception {
		assertEquals(XavaResources.getString("unexpected_value_in_model", name, modelo), value, getValue(modelo, name));		
	}
	
	protected void assertDescriptionValue(String name, String value) throws Exception {
		assertEquals(XavaResources.getString("unexpected_description", name), value, getDescriptionValue(name));		
	}

	protected void assertAction(String accion) {
		assertTrue(XavaResources.getString("action_not_found_in_ui", accion), getAcciones().contains(accion));
	}
	
	protected void assertNoAction(String accion) {
		assertTrue(XavaResources.getString("action_found_in_ui", accion), !getAcciones().contains(accion));
	}
	
	private List getAcciones() {
		String [] p = getForm().getParameterNames();
		List acciones = new ArrayList();		
		for (int i = 0; i < p.length; i++) {			
			if (!p[i].startsWith(ACTION_PREFIX)) continue;
			acciones.add(quitarPrefijoAccion(p[i]));			
		}				
		return acciones;		
	}		
			
	protected void assertActions(String [] expectedActions) throws Exception {
		List accionesEnFormulario = getAcciones();			
		Collection faltan = new ArrayList();		
		for (int i = 0; i < expectedActions.length; i++) {
			String accionEsperada = expectedActions[i];				
			if (accionesEnFormulario.contains(accionEsperada)) {
				accionesEnFormulario.remove(accionEsperada);
			}
			else {
				faltan.add(accionEsperada);
			}				
		}			

		if (!faltan.isEmpty()) {
			fail(XavaResources.getString("actions_expected", faltan));
		}
		if (!accionesEnFormulario.isEmpty()) {
			fail(XavaResources.getString("actions_not_expected", accionesEnFormulario));
		}
	} 
		
	private String quitarPrefijoAccion(String accion) {		
		return accion.substring(ACTION_PREFIX.length() + 1);
	}

	private String getPropertyPrefix() throws Exception {
		if (propertyPrefix == null) {
			String modelName = MetaApplications.getMetaApplication(application).getMetaModule(module).getModelName();
			propertyPrefix = "xava." + modelName + "."; 
		}
		return propertyPrefix;
	}
	
	protected String getValueInList(int fila, String name) throws Exception {
		int columna = getMetaTab().getPropertiesNames().indexOf(name);
		return getValueInList(fila, columna);
	}
	
	protected String getValueInList(int fila, int columna) throws Exception {
		return getTableCellInList(fila, columna).getText();
	}
	
	private TableCell getTableCellInList(int fila, int columna) throws Exception { 
		WebTable tabla = response.getTableWithID("list");
		if (tabla == null) {
			fail(XavaResources.getString("list_not_displayed"));			
		}
		return tabla.getTableCell(fila+2, columna+2);
	}
	
	
	protected String getValueInCollection(String coleccion, int fila, String name) throws Exception {		
		MetaCollectionView metaVistaColeccion = getMetaView().getMetaCollectionView(coleccion);
		List propiedadesLista = metaVistaColeccion.getPropertiesListNames();
		if (propiedadesLista.isEmpty()) propiedadesLista = getMetaModel().getPropertiesNames();
		int columna = propiedadesLista.indexOf(name);
		return getValueInCollection(coleccion, fila, columna);
	}
	
	
	protected String getValueInCollection(String collection, int row, int column) throws Exception {
		return getTableCellInCollection(collection, row, column).getText();
	}
	
	private TableCell getTableCellInCollection(String coleccion, int fila, int columna) throws Exception {
		WebTable tabla = response.getTableWithID(coleccion);
		if (tabla == null) {
			fail(XavaResources.getString("collection_not_displayed", coleccion));			
		}
		int incremento = tabla.getTableCell(fila+1, 0).getLinks().length > 0?1:0;
		String [] elementos = tabla.getTableCell(fila+1, incremento).getElementNames();
		if (elementos != null && elementos.length > 0 && elementos[0].endsWith("__SELECTED__")) incremento++;
		return tabla.getTableCell(fila+1, columna+incremento);		
	}
	
	protected void assertRowStyleInList(int row, String expectedStyle) throws Exception {
		TableCell cell = getTableCellInList(row, 0);
		String style = cell.getAttribute("class");
		if (style.startsWith("pair-")) style = style.substring("pair-".length());
		else if (style.startsWith("odd-")) style = style.substring("odd-".length());		
		assertEquals(XavaResources.getString("row_style_not_excepted"), expectedStyle, style);
	}
	
	protected void assertNoRowStyleInList(int row) throws Exception {
		TableCell cell = getTableCellInList(row, 0);
		String style = cell.getAttribute("class");
		if (Is.emptyString(style) || style.equals("pair") || style.equals("odd")) return;
		fail(XavaResources.getString("row_style_not_excepted"));
	}
	
	
	
	/**
	 * Rows count displayed with data. <p>
	 * 
	 * Exclude heading and footing, and the not displayed data (maybe in cache).
	 */
	protected int getListRowCount() throws Exception {
		WebTable tabla = response.getTableWithID("list");
		if (tabla == null) {
			fail(XavaResources.getString("list_not_displayed"));			
		}		
		return tabla.getRowCount() - 2;		
	}
	
	protected int getListColumnCount() throws Exception {
		WebTable tabla = response.getTableWithID("list");
		if (tabla == null) {
			fail(XavaResources.getString("list_not_displayed"));			
		}
		return tabla.getColumnCount() - 2;		
	}
	
	
	
	
	/**
	 * Cantidad de filas visualizadas con datos. <p>
	 * Excluye las cabeceras y pies, y los datos no visualizados (aunque cacheados).
	 */
	protected int getCollectionRowCount(String collection) throws Exception {
		WebTable tabla = response.getTableWithID(collection);
		if (tabla == null) {
			fail(XavaResources.getString("collection_not_displayed", collection));			
		}
		return tabla.getRowCount() - 2;		
	}
	
	/**
	 * Cantidad de filas visualizadas con datos. <p>
	 * Excluye las cabeceras y pies, y los datos no visualizados (aunque cacheados).
	 */
	protected void assertCollectionRowCount(String collection, int cantidadEsperada) throws Exception {
		assertEquals(XavaResources.getString("collection_row_count", collection), cantidadEsperada, getCollectionRowCount(collection));
	}
	
	/**
	 * Rows count displayed with data. <p>
	 * 
	 * Exclude headers and footing, and the not displayed data (maybe cached).
	 */
	protected void assertListRowCount(int expected) throws Exception {
		assertEquals(XavaResources.getString("list_row_count"), expected, getListRowCount());
	}
	
	protected void assertListColumnCount(int expected) throws Exception {
		assertEquals(XavaResources.getString("list_column_count"), expected, getListColumnCount());
	}
	
		
	protected void assertValueInList(int row, String name, String value) throws Exception {
		assertEquals(XavaResources.getString("unexpected_value_in_list", name, new Integer(row)), value, getValueInList(row, name));
	}
	
	protected void assertValueInList(int row, int column, String value) throws Exception {
		assertEquals(XavaResources.getString("unexpected_value_in_list", new Integer(column), new Integer(row)), value, getValueInList(row, column));
	}
	
	protected void assertValueInCollection(String collection, int row, String name, String value) throws Exception {
		assertEquals(XavaResources.getString("unexpected_value_in_collection", name, new Integer(row), collection), value, getValueInCollection(collection, row, name));
	}
	
	protected void assertValueInCollection(String collection, int row, int column, String value) throws Exception {
		assertEquals(XavaResources.getString("unexpected_value_in_collection", new Integer(column), new Integer(row), collection), value, getValueInCollection(collection, row, column));
	}
	
	protected void assertValueInCollectionIgnoringCase(String collection, int row, int column, String value) throws Exception {
		String valueInCollection = getValueInCollection(collection, row, column);
		assertTrue(XavaResources.getString("unexpected_value_in_collection", new Integer(column), new Integer(row), collection), value.equalsIgnoreCase(valueInCollection));
	}
	
	
	protected void assertLabelInList(int column, String label) throws Exception {
		WebTable tabla = response.getTableWithID("list");
		if (tabla == null) {
			fail(XavaResources.getString("list_not_displayed"));	
		}
		assertEquals(XavaResources.getString("label_not_match", new Integer(column)), label, tabla.getCellAsText(0, column+2).trim());
	}
	
	
	
	
	protected void checkRow(int row) throws Exception {
		checkRow("selected", row);
	}
	
	protected void uncheckRow(int row) throws Exception {
		uncheckRow("selected", row);
	}
		
	protected void checkRowCollection(String collection, int row) throws Exception {
		checkRow(getPropertyPrefix() + collection + "." + "__SELECTED__", row);
	}
		
	private void checkRow(String id, int row) throws Exception {		
		String [] sel = getForm().getParameterValues(id);
		String [] nsel = new String[sel.length + 1];
		for (int i = 0; i < sel.length; i++) {
			nsel[i] = sel[i];
		}
		nsel[sel.length] = Integer.toString(row);
		getForm().setParameter(id, nsel);	
	}
	
	protected void checkRow(String id, String value) throws Exception {		
		String [] sel = getForm().getParameterValues(id);
		String [] nsel = new String[sel.length + 1];
		for (int i = 0; i < sel.length; i++) {
			nsel[i] = sel[i];
		}
		nsel[sel.length] = value;
		getForm().setParameter(id, nsel);	
	}
	
	
	private void uncheckRow(String id, int row) throws Exception {		
		String [] sel = getForm().getParameterValues(id);
		Collection nuevosSeleccionados = new ArrayList();
		String sfila = String.valueOf(row);
		for (int i = 0; i < sel.length; i++) {
			if (!sfila.equals(sel[i])) nuevosSeleccionados.add(sel[i]);
		}
		String [] nsel = new String[nuevosSeleccionados.size()];
		nuevosSeleccionados.toArray(nsel);
		getForm().setParameter(id, nsel);
	}
	
		
	protected void assertRowChecked(int row) {
		assertRowsChecked(new int [] {row});
	}
	
	protected void assertRowsChecked(int f1, int f2) {
		assertRowsChecked(new int [] {f1, f2});
	}
		
	protected void assertRowsChecked(int [] rows) {
		String [] sel = getForm().getParameterValues("selected");
		Integer [] isel = new Integer[sel.length];
		for (int i = 0; i < isel.length; i++) {
			isel[i] = new Integer(sel[i]);
		}
		
		Integer [] esperadas = new Integer[rows.length];
		for (int i = 0; i < rows.length; i++) {
			esperadas[i] = new Integer(rows[i]);
		}
		
		Arrays.sort(isel);
		Arrays.sort(esperadas);
		assertEquals(XavaResources.getString("selected_rows_not_match"),
			Arrays.asList(esperadas), Arrays.asList(isel));
	}
	
	protected void assertRowUnchecked(int row) {
		String [] sel = getForm().getParameterValues("selected");
		Arrays.sort(sel);
		String sfila = String.valueOf(row);		
		assertTrue(XavaResources.getString("selected_row_unexpected", new Integer(row)), Arrays.binarySearch(sel, sfila) < 0);
	}
	
	
	protected void assertError(String mensaje) throws Exception {		
		WebTable table = response.getTableWithID("errors");
		if (table == null) {
			fail(XavaResources.getString("error_not_found", mensaje));
			return;
		}
		int rc = table.getRowCount();
		StringBuffer errores = new StringBuffer();
		for (int i = 0; i < rc; i++) {
			String error = table.getCellAsText(i, 0).trim();
			errores.append(error);
			errores.append('\n');			
			if (error.equals(mensaje)) return;
		}
		System.out.println(XavaResources.getString("errors_produced", errores));
		fail(XavaResources.getString("error_not_found", mensaje));
	}
	
	protected void assertErrorsCount(int expectedCount) throws Exception {		
		WebTable table = response.getTableWithID("errors");
		if (table == null && expectedCount > 0) {
			fail(XavaResources.getString("no_error_and_expected", new Integer(expectedCount)));
			return;
		}
		assertEquals(XavaResources.getString("errors_count_unexpected"), expectedCount, table.getRowCount());
	}
	
	protected void assertMessagesCount(int expectedCount) throws Exception {		
		WebTable table = response.getTableWithID("messages");
		if (table == null && expectedCount > 0) {
			fail(XavaResources.getString("no_message_and_expected", new Integer(expectedCount)));
			return;
		}
		assertEquals(XavaResources.getString("messages_count_unexpected"), expectedCount, table.getRowCount());
	}
			
	protected void assertNoError(String mensaje) throws Exception {		
		WebTable table = response.getTableWithID("errors");
		if (table == null) {			
			return;
		}
		int rc = table.getRowCount();				
		for (int i = 0; i < rc; i++) {
			String error = table.getCellAsText(i, 0).trim();
			if (error.equals(mensaje)) fail(XavaResources.getString("error_found", mensaje));
		}
	}
	
	
	protected void assertMessage(String message) throws Exception {		
		WebTable table = response.getTableWithID("messages");
		if (table == null) {
			fail(XavaResources.getString("message_not_found", message));
			return;
		}
		int rc = table.getRowCount();
		StringBuffer mensajes = new StringBuffer();
		for (int i = 0; i < rc; i++) {
			String m = table.getCellAsText(i, 0).trim();
			if (m.equals(message)) return;
			mensajes.append(m);
			mensajes.append('\n');												
		}
		System.out.println(XavaResources.getString("messages_produced", mensajes));
		fail(XavaResources.getString("message_not_found", message));
	}
	
	
	protected void assertNoErrors() throws Exception {
		assertNoMensajes("errors", "Error");		
	}
	protected void assertNoMessages() throws Exception {
		assertNoMensajes("messages", "Mensaje");		
	}
	
	private void assertNoMensajes(String id, String label) throws Exception {		
		WebTable table = response.getTableWithID(id);
		if (table == null) {			
			return;
		}
		int rc = table.getRowCount();
		if (rc > 0) {
			for (int i = 0; i < rc; i++) {
				String message = table.getCellAsText(i, 0).trim();
				System.err.println(XavaResources.getString("unexpected_message", label, message));							
			}			
			fail(XavaResources.getString("unexpected_messages", id));
		}
	}
	
	
	
	private MetaTab getMetaTab() throws XavaException {
		if (metaTab == null) {
			MetaModule metaModulo = MetaApplications.getMetaApplication(this.application).getMetaModule(this.module);
			metaTab = MetaComponent.get(metaModulo.getModelName()).getMetaTab(metaModulo.getTabName());			
		}
		return metaTab;
	}
	
	private MetaView getMetaView() throws XavaException {
		if (metaView == null) {
			MetaModule metaModulo = MetaApplications.getMetaApplication(this.application).getMetaModule(this.module);			 			
			metaView = getMetaModel().getMetaView(metaModulo.getViewName());			
		}
		return metaView;
	}
	
	private MetaModel getMetaModel() throws XavaException {
		if (metaModel == null) {
			MetaModule metaModulo = MetaApplications.getMetaApplication(this.application).getMetaModule(this.module);
			metaModel = MetaComponent.get(metaModulo.getModelName()).getMetaEntity(); 									
		}
		return metaModel;
	}	

	
	protected void assertValidValues(String name, String [][] values) throws Exception {
		String [] descripciones = getForm().getOptions(getPropertyPrefix() + name);		
		String [] claves = getForm().getOptionValues(getPropertyPrefix() + name);
		assertEquals(XavaResources.getString("unexpected_valid_values", name), values.length, descripciones.length);
		for (int i = 0; i < values.length; i++) {
			assertEquals(XavaResources.getString("unexpected_key", name), values[i][0], claves[i]); 
			assertEquals(XavaResources.getString("unexpected_description", name), values[i][1], descripciones[i]);
		}
	}
	
	protected void assertValidValuesCount(String name, int count) throws Exception {
		String [] descripciones = getForm().getOptions(getPropertyPrefix() + name);
		assertEquals(XavaResources.getString("unexpected_valid_values", name), count, descripciones.length);
	}
	
	
	protected String [] getKeysValidValues(String name) throws Exception {
		return getForm().getOptionValues(getPropertyPrefix() + name);
	}
	
	protected void assertEditable(String name) throws Exception {
		assertEditable(name, "true", XavaResources.getString("must_to_be_editable"));
	}
	
	protected void assertNoEditable(String name) throws Exception {
		assertEditable(name, "false", XavaResources.getString("must_not_to_be_editable"));
	}
		
	private void assertEditable(String name, String value, String  mensaje) throws Exception {
		String v = getValue(name + EDITABLE_SUFIX);		
		assertTrue(name + " " + mensaje, value.equals(v)); 		
	}
	
	protected void assertListTitle(String tituloEsperado) throws Exception {
		WebTable tabla = response.getTableWithID("list-title");
		if (tabla == null) {
			fail(XavaResources.getString("title_not_displayed"));			
		}
		assertEquals(XavaResources.getString("incorrect_title"), tituloEsperado, tabla.getCellAsText(0, 0));
	}
	
	protected void assertNoListTitle() throws Exception {
		WebTable tabla = response.getTableWithID("list-title");
		if (tabla != null) {
			fail(XavaResources.getString("title_displayed"));			
		}		
	}
	
	protected void assertListNotEmpty() throws Exception {
		assertTrue(XavaResources.getString("minimum_2_elements_in_list"), getListRowCount() > 1);
	}
		
	private static String getPort() {
		if (port == null) {
			port = getXavaJunitProperties().getProperty("port", "8080");
		}
		return port;		
	}
	
	private static String getLocale() {
		if (!isLocaleSet) {
			locale = getXavaJunitProperties().getProperty("locale");
			if (Is.emptyString(locale)) {
				locale = null;
			}
			isLocaleSet = true;
		}
		return locale;
	}
		
	/**
	 * From file xava-junit.properties 
	 */
	protected String getProperty(String id) {
		return getXavaJunitProperties().getProperty(id);
	}
	
	private static Properties getXavaJunitProperties() {
		if (xavaJunitProperties == null) {
			try {
				xavaJunitProperties = new Properties();
				URL recurso = ModuleTestBase.class.getClassLoader().getResource("xava-junit.properties");
				if (recurso != null) {
					xavaJunitProperties.load(recurso.openStream());
				}
			}
			catch (IOException ex) {					
				System.err.println(XavaResources.getString("xavajunit_properties_file_warning"));
			}							
		}
		return xavaJunitProperties;
	}
	
	private void setForm(WebForm form) {
		this.form = form;
		parameters = Arrays.asList(form.getParameterNames());
	}
	
	private WebForm getForm() {
		return form;	
	}	
			
}
