package org.openxava.tests;

import java.io.*;
import java.net.*;
import java.util.*;

import org.hibernate.*;
import org.openxava.application.meta.*;
import org.openxava.component.*;
import org.openxava.controller.meta.*;
import org.openxava.model.impl.*;
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
	
	private final static String EDITABLE_SUFIX = "_EDITABLE_";
	private final static String ACTION_PREFIX = "xava.action";
	
	private static Properties xavaJunitProperties;
	private static boolean isLocaleSet = false;
	private static String locale;
	private static String jetspeed2URL;
	private static String jetspeed2UserName;
	private static String jetspeed2Password;
	private static int loginFormIndex = -1;
	private static String host;
	private static String port;
	private static SessionFactory sessionFactory; 

	private Session session; 	
	private Transaction transaction; 
	private List parameters;
	private MetaModule metaModule;
	private MetaModel metaModel;
	private MetaView metaView;
	private MetaTab metaTab;
	private String propertyPrefix;
	private String application;
	private String module;
	private WebConversation conversation; 
	private WebResponse response; 
	private WebForm form;	
	
	public ModuleTestBase(String nameTest, String application, String modul) {
		super(nameTest);
		MetaControllers.setContext(MetaControllers.WEB);
		this.application = application;
		this.module = modul;
	}
	
	protected void setUp() throws Exception {
		resetModule();	
	}
	
	protected void tearDown() throws Exception {
		if (isJetspeed2Enabled() && isJetspeed2UserPresent()) {
			// logout
			response = conversation.getResponse("http://" + getHost() + ":" + getPort() + "/" + getJetspeed2URL() + "/login/logout");
		}
		closeSession();
	}
	
	/**
	 * Like close navigator, open again, and reexecute the module.
	 */
	protected void resetModule() throws Exception {
		conversation = new WebConversation();
		if (getLocale() != null) {
			conversation.setHeaderField("Accept-Language", getLocale());
			Locale.setDefault(new Locale(getLocale(), ""));
		}
		if (isJetspeed2Enabled() && isJetspeed2UserPresent()) {
			response = conversation.getResponse("http://" + getHost() + ":" + getPort() + "/" + getJetspeed2URL() + "/portal/");
			resetLoginForm();			
			getForm().setParameter("org.apache.jetspeed.login.username", getJetspeed2UserName());
			getForm().setParameter("org.apache.jetspeed.login.password", getJetspeed2Password());
			getForm().submit();
		}
		response = conversation.getResponse(getModuleURL());
		resetForm();
		propertyPrefix = null;		
	}
	
	protected void changeModule(String module) throws Exception {
		changeModule(this.application, module);
	}
	
	protected void changeModule(String application, String module) throws Exception {
		this.application = application;
		this.module = module;
		propertyPrefix = null;
		metaModule = null;
		metaModel = null;
		metaView = null;
		metaTab = null;				
		response = conversation.getResponse(getModuleURL());
		resetForm();		
	}
	
	
	private String getModuleURL() throws XavaException {
		if (isJetspeed2Enabled()) {
			String folder = Is.emptyString(getMetaModule().getFolder())?"":Strings.change(getMetaModule().getFolder(), ".", "/") + "/";
			return "http://" + getHost() + ":" + getPort() + "/" + getJetspeed2URL() + "/portal/" + application + "/" + folder + module + ".psml";
		}
		else {
			return"http://" + getHost() + ":" + getPort() + "/" + application + "/xava/module.jsp?application="+application+"&module=" + module;
		}		
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
	 * Simulate the a link o button click.
	 * <p>
	 * For some this is necessary, because execute does not
	 * throw all possible events. But it does not work in
	 * all cases (sometimes it seems like if user click 2 times)
	 * hence execute is still the favorite. 
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
			resetForm();
		}						
	}
	
	
	/**
	 * Execute the action using javascript, is the preferred way.
	 */
	protected void execute(String action) throws Exception {
		assertAction(action);		
		response.getScriptableObject().setLocation("javascript:executeXavaAction(false, false, document.forms[" + getFormIndex() + "], '" + action + "')");
		response = conversation.getCurrentPage();
		if ("text/html".equals(response.getContentType())) {
			resetForm();
		}						
	}
										 	
	protected void assertFocusOn(String name) throws Exception {
		String focusProperty = getForm().getParameterValue("focus_property_id");
		assertEquals(XavaResources.getString("focus_in_unexpected_place"), getPropertyPrefix() + name, focusProperty);		
	}
	
	protected void execute(String action, String arguments) throws Exception {
		assertAction(action); 
		response.getScriptableObject().setLocation("javascript:executeXavaAction(false, false, document.forms[" + getFormIndex() + "], '" + action + "', '" + arguments+ "')");
		response = conversation.getCurrentPage();
		resetForm();
	}
	
	protected void executeDefaultAction() throws Exception {
		SubmitButton button = getForm().getSubmitButtons()[getFormIndex()];
		button.click();
		response = conversation.getCurrentPage();
		resetForm();
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
	
	/**
	 * For properties with multiple values
	 */
	protected String [] getValues(String name) throws Exception {		
		return getForm().getParameterValues(getPropertyPrefix() + name);
	}	
	
	protected String getLabel(String name) throws Exception {
		HTMLElement element = response.getElementWithID(getPropertyPrefix() + name + "_LABEL_");
		if (element == null) {
			fail(XavaResources.getString("label_not_found_in_ui", name));
		}
		return element.getText().trim();
	}
		
	/**
	 * In case we does not work with main view.
	 */
	protected String getValue(String model, String name) throws Exception {		
		return getForm().getParameterValue("xava." + model +"." + name);
	}
		
	/**
	 * Only for debug.
	 */
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
	 * Current HTML code.
	 * <p>
	 * It is not very advisable because this will cause dependency
	 * to HTML and it will be difficult migrate to another presentation tecnology.
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
		filterConditionComparators(values);
		getForm().setParameter("conditionComparators", values);
	}
	
	private void filterConditionComparators(String[] values) {
		for (int i = 0; i < values.length; i++) {
			if ("=".equals(values[i])) values[i] = "eq";
			if ("<>".equals(values[i])) values[i] = "ne";
			if (">=".equals(values[i])) values[i] = "ge";
			if ("<=".equals(values[i])) values[i] = "le";
			if (">".equals(values[i])) values[i] = "gt";
			if ("<".equals(values[i])) values[i] = "lt";
		}		
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
			resetForm();
		}
		
	}
	
	/**
	 * For multiple values properties
	 */
	protected void setValues(String name, String [] values) throws Exception {
		getForm().setParameter(getPropertyPrefix() + name, values);			
		
		// If onchange then reload the page, because onchange do submit
		HTMLElement el = response.getElementsWithName(getPropertyPrefix() +  name)[0];
		String onchange = el.getAttribute("onchange");	
		if (!Is.emptyString(onchange)) {
			response = conversation.getCurrentPage();
			resetForm();
		}
		
	}
	
	
	protected void setFileValue(String name, String filePath) throws Exception {
		File file = new File(filePath);		
		UploadFileSpec [] up =  { new UploadFileSpec(file) };
		getForm().setParameter(name, up);		
	}
	
	/**
	 * In case we do not work with main view. <p>
	 */
	protected void setValue(String model, String name, String value) throws Exception {		
		getForm().setParameter("xava." + model + "." + name, value);
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

	/**
	 * For multiple values property.
	 */
	protected void assertValues(String name, String [] values) throws Exception {		
		assertEquals(XavaResources.getString("unexpected_value", name), Arrays.asList(values), Arrays.asList(getValues(name)));		
	}
	
			
	protected void assertValueIgnoringCase(String name, String value) throws Exception {		
		assertTrue(XavaResources.getString("unexpected_value", name), value.equalsIgnoreCase(getValue(name)));		
	}
		
	protected void assertValue(String model, String name, String value) throws Exception {
		assertEquals(XavaResources.getString("unexpected_value_in_model", name, model), value, getValue(model, name));		
	}
	
	protected void assertDescriptionValue(String name, String value) throws Exception {
		assertEquals(XavaResources.getString("unexpected_description", name), value, getDescriptionValue(name));		
	}

	protected void assertAction(String action) {		
		assertTrue(XavaResources.getString("action_not_found_in_ui", action), getActions().contains(action));
	}
	
	protected void assertNoAction(String action) {
		assertTrue(XavaResources.getString("action_found_in_ui", action), !getActions().contains(action));
	}
	
	private List getActions() {
		String [] p = getForm().getParameterNames();
		List actions = new ArrayList();		
		for (int i = 0; i < p.length; i++) {			
			if (!p[i].startsWith(ACTION_PREFIX)) continue;
			actions.add(removeActionPrefix(p[i]));			
		}				
		return actions;		
	}		
			
	protected void assertActions(String [] expectedActions) throws Exception {
		List actionsInForm = getActions();			
		Collection left = new ArrayList();		
		for (int i = 0; i < expectedActions.length; i++) {
			String expectedAction = expectedActions[i];				
			if (actionsInForm.contains(expectedAction)) {
				actionsInForm.remove(expectedAction);
			}
			else {
				left.add(expectedAction);
			}				
		}			

		if (!left.isEmpty()) {
			fail(XavaResources.getString("actions_expected", left));
		}
		if (!actionsInForm.isEmpty()) {
			fail(XavaResources.getString("actions_not_expected", actionsInForm));
		}
	} 
		
	private String removeActionPrefix(String action) {		
		return action.substring(ACTION_PREFIX.length() + 1);
	}

	private String getPropertyPrefix() throws Exception {
		if (propertyPrefix == null) {
			String modelName = MetaApplications.getMetaApplication(application).getMetaModule(module).getModelName();
			propertyPrefix = "xava." + modelName + "."; 
		}
		return propertyPrefix;
	}
	
	protected String getValueInList(int row, String name) throws Exception {
		int column = getMetaTab().getPropertiesNames().indexOf(name);		
		return getValueInList(row, column);
	}
	
	protected String getValueInList(int row, int column) throws Exception {
		return getTableCellInList(row, column).getText();
	}
	
	private TableCell getTableCellInList(int row, int column) throws Exception { 
		WebTable table = response.getTableWithID("list");
		if (table == null) {
			fail(XavaResources.getString("list_not_displayed"));			
		}
		return table.getTableCell(row+2, column+2);
	}
	
	
	protected String getValueInCollection(String collection, int row, String name) throws Exception {		
		MetaCollectionView metaCollectionView = getMetaView().getMetaCollectionView(collection);
		List propertiesList = metaCollectionView.getPropertiesListNames();
		if (propertiesList.isEmpty()) propertiesList = getMetaModel().getPropertiesNames();
		int column = propertiesList.indexOf(name);
		return getValueInCollection(collection, row, column);
	}
	
	
	protected String getValueInCollection(String collection, int row, int column) throws Exception {
		return getTableCellInCollection(collection, row, column).getText();
	}
	
	private TableCell getTableCellInCollection(String collection, int row, int column) throws Exception {
		WebTable table = response.getTableWithID(collection);
		if (table == null) {
			fail(XavaResources.getString("collection_not_displayed", collection));			
		}
		int increment = table.getTableCell(row+1, 0).getLinks().length > 0?1:0;
		String [] elements = table.getTableCell(row+1, increment).getElementNames();
		if (elements != null && elements.length > 0 && elements[0].endsWith("__SELECTED__")) increment++;
		return table.getTableCell(row+1, column+increment);		
	}
	
	protected void assertRowStyleInList(int row, String expectedStyle) throws Exception {
		TableCell cell = getTableCellInList(row, 0);		
		HTMLElement div = cell.getElementWithID("cellStyle");
		String style = div.getAttribute("class");		
		assertEquals(XavaResources.getString("row_style_not_excepted"), expectedStyle, style);		
	}
	
	protected void assertNoRowStyleInList(int row) throws Exception {
		TableCell cell = getTableCellInList(row, 0);
		HTMLElement div = cell.getElementWithID("cellStyle");
		if (div == null) return;
		fail(XavaResources.getString("row_style_not_excepted"));
	}
	
	/**
	 * Rows count displayed with data. <p>
	 * 
	 * Exclude heading and footing, and the not displayed data (maybe in cache).
	 */
	protected int getListRowCount() throws Exception {
		WebTable table = response.getTableWithID("list");
		if (table == null) {
			fail(XavaResources.getString("list_not_displayed"));			
		}		
		return table.getRowCount() - 2;		
	}
	
	protected int getListColumnCount() throws Exception {
		WebTable table = response.getTableWithID("list");
		if (table == null) {
			fail(XavaResources.getString("list_not_displayed"));			
		}
		return table.getColumnCount() - 2;		
	}
	
	/**
	 * Row count displayed with data. <p>
	 * Excludes heading and footing, and not displayed data (but cached). 
	 */
	protected int getCollectionRowCount(String collection) throws Exception {
		WebTable table = response.getTableWithID(collection);
		if (table == null) {
			fail(XavaResources.getString("collection_not_displayed", collection));			
		}
		return table.getRowCount() - 2;		
	}
	
	/**
	 * Row count displayed with data. <p>
	 * Excludes heading and footing, and not displayed data (but cached). 
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
		WebTable table = response.getTableWithID("list");
		if (table == null) {
			fail(XavaResources.getString("list_not_displayed"));	
		}
		assertEquals(XavaResources.getString("label_not_match", new Integer(column)), label, table.getCellAsText(0, column+2).trim());
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
		Collection selectedNewOnes = new ArrayList();
		String sfila = String.valueOf(row);
		for (int i = 0; i < sel.length; i++) {
			if (!sfila.equals(sel[i])) selectedNewOnes.add(sel[i]);
		}
		String [] nsel = new String[selectedNewOnes.size()];
		selectedNewOnes.toArray(nsel);
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
		
		Integer [] expected = new Integer[rows.length];
		for (int i = 0; i < rows.length; i++) {
			expected[i] = new Integer(rows[i]);
		}
		
		Arrays.sort(isel);
		Arrays.sort(expected);
		assertEquals(XavaResources.getString("selected_rows_not_match"),
			Arrays.asList(expected), Arrays.asList(isel));
	}
	
	protected void assertRowUnchecked(int row) {
		String [] sel = getForm().getParameterValues("selected");
		Arrays.sort(sel);
		String srow = String.valueOf(row);		
		assertTrue(XavaResources.getString("selected_row_unexpected", new Integer(row)), Arrays.binarySearch(sel, srow) < 0);
	}
	
	
	protected void assertError(String message) throws Exception {		
		WebTable table = response.getTableWithID("errors");
		if (table == null) {
			fail(XavaResources.getString("error_not_found", message));
			return;
		}
		int rc = table.getRowCount();
		StringBuffer errors = new StringBuffer();
		for (int i = 0; i < rc; i++) {
			String error = table.getCellAsText(i, 0).trim();
			errors.append(error);
			errors.append('\n');			
			if (error.equals(message)) return;
		}
		System.out.println(XavaResources.getString("errors_produced", errors));
		fail(XavaResources.getString("error_not_found", message));
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
			
	protected void assertNoError(String message) throws Exception {		
		WebTable table = response.getTableWithID("errors");
		if (table == null) {			
			return;
		}
		int rc = table.getRowCount();				
		for (int i = 0; i < rc; i++) {
			String error = table.getCellAsText(i, 0).trim();
			if (error.equals(message)) fail(XavaResources.getString("error_found", message));
		}
	}
	
	/**
	 * The first message
	 */
	protected String getMessage() throws Exception {
		WebTable table = response.getTableWithID("messages");
		if (table == null || table.getRowCount() == 0) {			
			return "";
		}
		return table.getCellAsText(0, 0).trim();
	}	
	
	protected void assertMessage(String message) throws Exception {		
		WebTable table = response.getTableWithID("messages");
		if (table == null) {
			fail(XavaResources.getString("message_not_found", message));
			return;
		}
		int rc = table.getRowCount();
		StringBuffer messages = new StringBuffer();
		for (int i = 0; i < rc; i++) {
			String m = table.getCellAsText(i, 0).trim();
			if (m.equals(message)) return;
			messages.append(m);
			messages.append('\n');												
		}
		System.out.println(XavaResources.getString("messages_produced", messages));
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
			metaTab = MetaComponent.get(getMetaModule().getModelName()).getMetaTab(getMetaModule().getTabName());			
		}
		return metaTab;
	}
	
	private MetaView getMetaView() throws XavaException {
		if (metaView == null) {						 			
			metaView = getMetaModel().getMetaView(getMetaModule().getViewName());			
		}
		return metaView;
	}
	
	private MetaModel getMetaModel() throws XavaException {
		if (metaModel == null) {			
			metaModel = MetaComponent.get(getMetaModule().getModelName()).getMetaEntity(); 									
		}
		return metaModel;
	}	
	
	private MetaModule getMetaModule() throws XavaException {
		if (metaModule == null) {
			metaModule = MetaApplications.getMetaApplication(this.application).getMetaModule(this.module);
		}
		return metaModule;
	}

	
	protected void assertValidValues(String name, String [][] values) throws Exception {
		String [] descriptions = getForm().getOptions(getPropertyPrefix() + name);		
		String [] keys = getForm().getOptionValues(getPropertyPrefix() + name);
		assertEquals(XavaResources.getString("unexpected_valid_values", name), values.length, descriptions.length);
		for (int i = 0; i < values.length; i++) {
			assertEquals(XavaResources.getString("unexpected_key", name), values[i][0], keys[i]); 
			assertEquals(XavaResources.getString("unexpected_description", name), values[i][1], descriptions[i]);
		}
	}
	
	protected void assertValidValuesCount(String name, int count) throws Exception {
		String [] descripcions = getForm().getOptions(getPropertyPrefix() + name);
		assertEquals(XavaResources.getString("unexpected_valid_values", name), count, descripcions.length);
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
		
	private void assertEditable(String name, String value, String  message) throws Exception {
		String v = getValue(name + EDITABLE_SUFIX);		
		assertTrue(name + " " + message, value.equals(v)); 		
	}
	
	protected void assertListTitle(String expectedTitle) throws Exception {
		WebTable table = response.getTableWithID("list-title");
		if (table == null) {
			fail(XavaResources.getString("title_not_displayed"));			
		}
		assertEquals(XavaResources.getString("incorrect_title"), expectedTitle, table.getCellAsText(0, 0));
	}
	
	protected void assertNoListTitle() throws Exception {
		WebTable table = response.getTableWithID("list-title");
		if (table != null) {
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
	
	private static String getHost() {
		if (host == null) {
			host = getXavaJunitProperties().getProperty("host", "localhost");
		}
		return host;		
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
	
	public static boolean isJetspeed2Enabled() {
		return !Is.emptyString(getJetspeed2URL());
	}
	
	private static boolean isJetspeed2UserPresent() {
		return !Is.emptyString(getJetspeed2UserName());
	}
	
	
	private static String getJetspeed2URL() {
		if (jetspeed2URL == null) {
			jetspeed2URL = getXavaJunitProperties().getProperty("jetspeed2.url");
		}
		return jetspeed2URL;				
	}
	
	private static String getJetspeed2UserName() {
		if (jetspeed2UserName == null) {
			jetspeed2UserName = getXavaJunitProperties().getProperty("jetspeed2.username");
		}
		return jetspeed2UserName;				
	}
	
	private static String getJetspeed2Password() {
		if (jetspeed2Password == null) {
			jetspeed2Password = getXavaJunitProperties().getProperty("jetspeed2.password");
		}
		return jetspeed2Password;				
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
				URL resource = ModuleTestBase.class.getClassLoader().getResource("xava-junit.properties");
				if (resource != null) {
					xavaJunitProperties.load(resource.openStream());
				}
			}
			catch (IOException ex) {					
				System.err.println(XavaResources.getString("xavajunit_properties_file_warning"));
			}							
		}
		return xavaJunitProperties;
	}
	
	private void resetForm() throws Exception {		
		if (getFormIndex() >= response.getForms().length) return; 
		setForm(response.getForms()[getFormIndex()]);
	}
	
	private void resetLoginForm() throws Exception {
		setForm(response.getForms()[getLoginFormIndex()]);
	}
		
	private void setForm(WebForm form) {
		this.form = form;
		parameters = Arrays.asList(form.getParameterNames());
	}
	
	private WebForm getForm() {
		return form;	
	}	
	
	private int getFormIndex() throws Exception {
		return isJetspeed2Enabled()?1:0;
	}
	
	private int getLoginFormIndex() throws Exception {
		if (loginFormIndex == -1) {
			WebForm [] forms = response.getForms(); 
			for (int i = 0; i < forms.length; i++) {
				if (forms[i].hasParameterNamed("org.apache.jetspeed.login.username")) {					
					loginFormIndex = i;
					return loginFormIndex;
				}
			}
			loginFormIndex = 0;
		}
		return loginFormIndex;
	}	
	
	/**
	 * This allows you testing using HTTPUnit APIs directly. <p>
	 * 
	 * The use of this method is discoraged because binds your test
	 * to a HTML implemenation.
	 * Before to use this method look for another more abstract method
	 * in this class.
	 */
	protected WebConversation getConversation() {
		return conversation; 
	}
	
	/**
	 * Return a hibernate session. <p>
	 * 
	 * The first time a hibernate session is created.
	 * If you have closed the hibernate session, a new one is created. 
	 */
	protected Session getSession() throws Exception {
		if (session == null) {
			session = getSessionFactory().openSession();
			transaction = session.beginTransaction();
		} 
		return session;
	}
	
	private static SessionFactory getSessionFactory() throws Exception {
		if (sessionFactory == null) {
			sessionFactory = HibernatePersistenceProvider.createSessionFactory("/hibernate-junit.cfg.xml");
		}
		return sessionFactory;
	}
		
	/**
	 * Close the hibernate session. <p>
	 * 
	 * This flush any data to database and commit transaction.<br>
	 * If after calling this method you call to getSession() a new hibernate session is reopened.<br>
	 */
	protected void closeSession() throws Exception { 
		if (session != null) {
		  transaction.commit();
			session.close();
			transaction = null;
			session = null;
		}
	}	
 			
}
