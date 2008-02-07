package org.openxava.tests;

import java.io.*;
import java.net.*;
import java.util.*;

import org.apache.commons.logging.*;
import org.openxava.application.meta.*;
import org.openxava.component.*;
import org.openxava.controller.meta.*;
import org.openxava.hibernate.XHibernate;
import org.openxava.jpa.*;
import org.openxava.model.meta.*;
import org.openxava.tab.*;
import org.openxava.tab.meta.*;
import org.openxava.util.*;
import org.openxava.view.meta.*;
import org.xml.sax.*;

import junit.framework.*;

import com.meterware.httpunit.*;

/**
 * @author Javier Paniza
 */

public class ModuleTestBase extends TestCase {
	
	private final static String EDITABLE_SUFIX = "_EDITABLE_";
	private final static String ACTION_PREFIX = "xava.action";
	
	private static Log log = LogFactory.getLog(ModuleTestBase.class);
	
	private static Properties xavaJunitProperties;
	private static boolean isDefaultLocaleSet = false;
	private static String defaultLocale;
	private static String jetspeed2URL;
	private static String jetspeed2UserName;
	private static String jetspeed2Password;
	private static String liferayURL;
	private static int loginFormIndex = -1;
	private static String host;
	private static String port;
	private String locale;
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
	private String allowDuplicateSubmit;
	private int formIndex = -1; 		
	
	static {		
		XSystem._setLogLevelFromJavaLoggingLevelOfXavaPreferences();
		XHibernate.setConfigurationFile("/hibernate-junit.cfg.xml");
		XPersistence.setPersistenceUnit("junit");
		DataSourceConnectionProvider.setUseHibernateConnection(true);
		HttpUnitOptions.setExceptionsThrownOnScriptError(false); // In order to run in Liferay
	}
	
	/**
	 * To test the specified module of the specified application 
	 */
	public ModuleTestBase(String nameTest, String application, String module) {
		super(nameTest);
		MetaControllers.setContext(MetaControllers.WEB);
		this.application = application;
		this.module = module;
	}

	/**
	 * To test the specified module of the default application. <p>
	 * 
	 * In this case the application is obtained from the <code>application</code>
	 * property in <code>xava-junit.properties</code> file.
	 */	
	public ModuleTestBase(String nameTest, String module) {
		this(nameTest, getXavaJunitProperties().getProperty("application"), module);
	}
	
	
	protected void setUp() throws Exception {
		allowDuplicateSubmit = "";
		locale = null;
		resetModule();	
	}
	
	protected void tearDown() throws Exception {
		if (isJetspeed2Enabled() && isJetspeed2UserPresent()) {
			logout();			
		}
		XHibernate.commit();
		XPersistence.commit();
	}

	protected void login(String user, String password) throws Exception {
		if (isLiferayEnabled()) {
			// Liferay
			response = conversation.getResponse("http://" + getHost() + ":" + getPort() + "/c/portal/login");
			resetLoginForm();			
			getForm().setParameter("login", user);		
			getForm().setParameter(getPasswordFieldInLiferay(), password);					
			response = getForm().submit();
			WebForm form = response.getFormWithName("fm"); // For liferay 4.1
			if (form != null) {
				response =  form.submit(); 
			}
			formIndex = -1; 
			resetForm();
		}
		else {
			// JetSpeed 2
			response = conversation.getResponse("http://" + getHost() + ":" + getPort() + "/" + getJetspeed2URL() + "/portal/");
			resetLoginForm();			
			getForm().setParameter("org.apache.jetspeed.login.username", user);
			getForm().setParameter("org.apache.jetspeed.login.password", password);
			getForm().submit();
			response = conversation.getResponse(getModuleURL());
			resetForm();
		}
	}
	
	/**
	 * Warning: Does not rely heavily in this method, it can change in the future.
	 */
	static public boolean isOX3() {
		try {
			Class.forName("org.openxava.annotations.parse.AnnotatedClassParser");
			return true;
		}
		catch (ClassNotFoundException ex) {
			return false;
		}
	}

	private String getPasswordFieldInLiferay() {
		String [] parameterNames = getForm().getParameterNames();
		String passwordTextField = "password";
		for (int i=0; i<parameterNames.length; i++) {
			if (parameterNames[i].endsWith("_password")) {
				passwordTextField = parameterNames[i]; 
				break;
			}
		}
		return passwordTextField;
	}
	
	
	/**
	 * User logout. <p>
	 * 
	 * At the moment only works against Liferay and JetSpeed2.
	 */
	protected void logout() throws Exception {
		if (isLiferayEnabled()) {
			// Liferay 
			response = conversation.getResponse("http://" + getHost() + ":" + getPort() + "/c/portal/logout?referer=/c");
			formIndex = -1; 
		}
		else { 
			// Jetspeed 2
			response = conversation.getResponse("http://" + getHost() + ":" + getPort() + "/" + getJetspeed2URL() + "/login/logout");
		}
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
			login(getJetspeed2UserName(), getJetspeed2Password());
		}		
		else {
			response = conversation.getResponse(getModuleURL() + allowDuplicateSubmit);			
			resetForm();
		}		
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
		if (isLiferayEnabled()) {
			return "http://" + getHost() + ":" + getPort() + "/" + getLiferayURL() + "/" + application + "/" + module;
		}
		else if (isJetspeed2Enabled()) {
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
	 * Disable duplicate submit support on OpenXava module. <p>
	 *
	 * You must call this method if you want to use <code>click()</code>
	 * which does not work with duplicate submit control activate (an httpunit issue). 
	 */
	protected void allowDuplicateSubmit() throws Exception {
		allowDuplicateSubmit = "&xava_allow_duplicate_submit=true";
		resetModule();
	}

	/**
	 * Simulate the a link o button click.
	 * <p>
	 * For some cases  it's necessary, because 'execute' does not
	 * throw all possible events. But it does not work in
	 * all cases (sometimes it seems like if user click 2 times)
	 * hence execute is still the favorite. 
	 * <p>
	 * <b>It's mandatory to call <code>allowDuplicateSubmit()</code> before
	 * call this method.<b>
	 */	
	protected void click(String action) throws Exception {		
		if (Is.emptyString(allowDuplicateSubmit)) {
			throw new IllegalStateException(XavaResources.getString("allowDuplicateSubmit_before_click"));
		}		
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
		response.getScriptableObject().setLocation("javascript:executeXavaAction('', false, document.forms[" + getFormIndex() + "], '" + action + "')");
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
		response.getScriptableObject().setLocation("javascript:executeXavaAction('', false, document.forms[" + getFormIndex() + "], '" + action + "', '" + arguments+ "')");
		response = conversation.getCurrentPage();
		resetForm();
	}
	
	protected void executeDefaultAction() throws Exception {
		SubmitButton button = getForm().getSubmitButtons()[0];
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
		log.debug(response.getText());
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
	 * to HTML and it will be difficult migrate to another presentation technology.
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
	
	protected void setConditionValues(String collection, String [] values) throws Exception {
		String collectionId = Tab.COLLECTION_PREFIX + Strings.change(collection, ".", "_") + "_conditionValues";
		getForm().setParameter(collectionId, values);		
		// If a valid-values (an Enum) is used, httpunit changes the order of parameter values
		// we restore the correct order
		// Maybe this adjust is also needed for list mode (but it's not done yet)
		String [] currentValues = getForm().getParameterValues(collectionId);
		if (!conditionValuesEquals(values, currentValues)) {
			values = adjustConditionValues(collection, values);			
			getForm().setParameter(collectionId, values);
		}			
	}
	
	private boolean conditionValuesEquals(String[] expectedValues, String[] currentValues) {
		int i=0;
		for (i=0; i<expectedValues.length; i++) {
			if (i >= currentValues.length) return emptyFrom(expectedValues, i);
			if (!Is.equal(expectedValues[i], currentValues[i])) return false;			
		}
		return emptyFrom(currentValues, i);
	}

	private boolean emptyFrom(String[] values, int initial) {
		for (int i=initial; i<values.length; i++) {
			if (!Is.emptyString(values[i])) return false;
		}
		return true;
	}

	private String [] adjustConditionValues(String collection, String[] values) throws Exception { 
		// Refactorizar las siguientes 3 línea
		List propertiesList = getPropertiesList(collection);		
		MetaModel collectionModel = getMetaModel().getMetaCollection(collection).getMetaReference().getMetaModelReferenced();
		
		List originalValues = new ArrayList(Arrays.asList(values));
		
		int i = 0;
		for (Iterator it=propertiesList.iterator(); it.hasNext(); i++) {			
			if (i >= originalValues.size()) originalValues.add("");
			MetaProperty p = collectionModel.getMetaProperty((String) it.next());
			if (p.hasValidValues()) {
				Object value = originalValues.get(i);
				originalValues.remove(i);
				originalValues.add(0, value);
			}
		}
		String [] result = new String[originalValues.size()];
		originalValues.toArray(result);		
		return result;
	}

	protected void setConditionComparators(String collection, String [] values) throws Exception { 
		filterConditionComparators(values);
		getForm().setParameter(Tab.COLLECTION_PREFIX + collection + "conditionComparators", values);
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
	
	private void setValueWithPrefix(String propertyPrefix, String name, String value) throws Exception {
		boolean checkbox = false;
		if ("true".equals(value) || "false".equals(value)) { // maybe a checkbox
			try {
				getForm().setCheckbox(propertyPrefix + name, "true".equals(value));
				checkbox = true;
			}
			catch (IllegalArgumentException ex) {
				// Not a checkbox
				getForm().setParameter(propertyPrefix + name, value);
			}
		}
		else {
			getForm().setParameter(propertyPrefix + name, value);			
		}
		
		// If onchange then reload the page, because onchange do submit
		HTMLElement el = response.getElementsWithName(propertyPrefix +  name)[0];
		String onchange = el.getAttribute("onchange");	
		if (!Is.emptyString(onchange)) {
			if (checkbox) { // in checkbox case is needed throw onchange event
				response.getScriptableObject().setLocation("javascript:" + onchange);
			}
			response = conversation.getCurrentPage();
			resetForm();
		}
		
	}
	
	protected void setValue(String name, String value) throws Exception {
		setValueWithPrefix(getPropertyPrefix(), name, value);
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
		setValueWithPrefix("xava." + model + ".", name, value); 
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

	protected boolean existsAction(String action) {		
		return getActions().contains(action);
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
	
	private TableRow getTableRow(String tableId, int row) throws Exception {  
		WebTable table = response.getTableWithID(tableId);
		if (table == null) {			
			fail(XavaResources.getString("collection_not_displayed", tableId));
		}
		return table.getRows()[row+2];
	}
		
	protected String getValueInCollection(String collection, int row, String name) throws Exception {		
		int column = getPropertiesList(collection).indexOf(name);		
		return getValueInCollection(collection, row, column);
	}

	private List getPropertiesList(String collection) throws Exception {
		MetaCollectionView metaCollectionView = getMetaView().getMetaCollectionView(collection);
		List propertiesList = metaCollectionView==null?null:metaCollectionView.getPropertiesListNames();
		if (propertiesList == null || propertiesList.isEmpty()) propertiesList = getMetaModel().getMetaCollection(collection).getMetaReference().getMetaModelReferenced().getPropertiesNamesWithoutHiddenNorTransient();
		return propertiesList;
	}	
	
	protected String getValueInCollection(String collection, int row, int column) throws Exception {
		return getTableCellInCollection(collection, row, column).getText();
	}
	
	private TableCell getTableCellInCollection(String collection, int row, int column) throws Exception {		
		WebTable table = response.getTableWithID(collection);
		if (table == null) {
			fail(XavaResources.getString("collection_not_displayed", collection));			
		}		
		row = collectionHasFilterHeader(table)?row + 2:row + 1;
		int increment = table.getTableCell(row, 0).getLinks().length > 0 || table.getTableCell(0, 0).getLinks().length > 0?2:1;
		return table.getTableCell(row, column + increment);		
	}
	
	protected void assertRowStyleInList(int row, String expectedStyle) throws Exception {
		assertRowStyle("list", row, expectedStyle);
	}
	
	protected void assertRowStyleInCollection(String collection, int row, String expectedStyle) throws Exception {
		assertRowStyle(collection, row, expectedStyle);
	}	
	
	private void assertRowStyle(String tableId, int row, String expectedStyle) throws Exception {
		TableRow tableRow = getTableRow(tableId, row);
		String style = tableRow.getAttribute("class");
		int countTokens = new StringTokenizer(style).countTokens();
		// countTokens <= 1 because the row has at least a style in addition to the special one
		style = countTokens <= 1?"":Strings.lastToken(style);
		assertEquals(XavaResources.getString("row_style_not_excepted"), expectedStyle, style);		
	}
	
	protected void assertNoRowStyleInList(int row) throws Exception {
		assertNoRowStyle("list", row);
	}
	
	protected void assertNoRowStyleInCollection(String collection, int row) throws Exception {
		assertNoRowStyle(collection, row);
	}	
	
	private void assertNoRowStyle(String tableId, int row) throws Exception {
		TableRow tableRow = getTableRow(tableId, row);
		String style = tableRow.getAttribute("class");
		int countTokens = new StringTokenizer(style).countTokens();
		// countTokens <= 1 because the row has at least a style in addition to the special one 
		assertTrue(XavaResources.getString("row_style_not_excepted"), countTokens <= 1);
	}
	
	private int getListRowCount(String tableId, String message) throws Exception {
		WebTable table = response.getTableWithID(tableId);
		if (table == null) {
			fail(message);
		}
		if (table.getRows().length > 2 && "nodata".equals(table.getRows()[2].getID())) { 
			return 0;
		}						
		int increment = collectionHasFilterHeader(table)?2:1;
		return table.getRowCount() - increment;
	}
	
	private boolean collectionHasFilterHeader(WebTable table) { 				
		return table.getRows().length > 1 && table.getTableCell(1, 0).getElementsWithName("xava.action.List.filter").length > 0;
	}
	
	private boolean collectionHasFilterHeader(String collection) throws Exception { 
		WebTable table = response.getTableWithID(collection);
		if (table == null) {
			fail(XavaResources.getString("collection_not_displayed", collection));
		}
		return collectionHasFilterHeader(table);
	}
	
	
	/**
	 * Rows count displayed with data. <p>
	 * 
	 * Exclude heading and footing, and the not displayed data (maybe in cache).
	 */
	protected int getListRowCount() throws Exception {
		return getListRowCount("list", XavaResources.getString("list_not_displayed"));
	}
	
	protected int getListColumnCount() throws Exception {
		return getListColumnCount("list", XavaResources.getString("list_not_displayed"));
	}
	
	protected int getCollectionColumnCount(String collection) throws Exception {
		return getListColumnCount(collection, XavaResources.getString("collection_not_displayed", collection)); 
	}
	
	private int getListColumnCount(String id, String message) throws Exception {
		WebTable table = response.getTableWithID(id);
		if (table == null) {
			fail(message);			
		}
		return table.getColumnCount() - 2;		
	}	
	
	/**
	 * Row count displayed with data. <p>
	 * Excludes heading and footing, and not displayed data (but cached). 
	 */
	protected int getCollectionRowCount(String collection) throws Exception {
		return getListRowCount(collection, XavaResources.getString("collection_not_displayed", collection)); 
	}
	
	/**
	 * Row count displayed with data. <p>
	 * Excludes heading and footing, and not displayed data (but cached). 
	 */
	protected void assertCollectionRowCount(String collection, int expectedCount) throws Exception {
		assertEquals(XavaResources.getString("collection_row_count", collection), expectedCount, getCollectionRowCount(collection));
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
	
	protected void assertCollectionColumnCount(String collection, int expected) throws Exception {
		assertEquals(XavaResources.getString("collection_column_count", collection), expected, getCollectionColumnCount(collection)); 
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
	
	protected void assertLabelInCollection(String collection, int column, String label) throws Exception {
		assertLabelInList(collection, XavaResources.getString("collection_not_displayed", collection), column, label);
	}
	
	protected void assertLabelInList(int column, String label) throws Exception {
		assertLabelInList("list", XavaResources.getString("list_not_displayed"), column, label);
	}


	private void assertLabelInList(String tableId, String message, int column, String label) throws Exception {
		WebTable table = response.getTableWithID(tableId);
		if (table == null) {
			fail(message);	
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
		//if (getMetaModel().getMetaCollection(collection).hasCalculator()) {
		if (collectionHasFilterHeader(collection)) {
			checkRow(Tab.COLLECTION_PREFIX + collection.replace('.', '_') + "_selected", row);
		}
		else {
			checkRow(getPropertyPrefix() + collection + "." + "__SELECTED__", row);			
		}
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
		log.error(XavaResources.getString("errors_produced", errors));
		fail(XavaResources.getString("error_not_found", message));
	}
	
	protected void assertErrorsCount(int expectedCount) throws Exception {		
		WebTable table = response.getTableWithID("errors");
		if (table == null) {
			if (expectedCount > 0) {
				fail(XavaResources.getString("no_error_and_expected", new Integer(expectedCount)));
			}
			return;
		}		
		assertEquals(XavaResources.getString("errors_count_unexpected"), expectedCount, table.getRowCount());
	}
	
	protected void assertMessagesCount(int expectedCount) throws Exception {		
		WebTable table = response.getTableWithID("messages");
		if (table == null) {
			if (expectedCount > 0) {
				fail(XavaResources.getString("no_message_and_expected", new Integer(expectedCount)));
			}
			return;
		}
		assertEquals(XavaResources.getString("messages_count_unexpected"), expectedCount, table.getRowCount());
	}
			
	protected void assertNoError(String message) throws Exception {
		assertNoMessage(message, "errors", "error_found");
	}
	
	protected void assertNoMessage(String message) throws Exception {
		assertNoMessage(message, "messages", "message_found"); 
	}
		
	private void assertNoMessage(String message, String id, String notFoundErrorId) throws Exception {		
		WebTable table = response.getTableWithID(id);
		if (table == null) {			
			return;
		}
		int rc = table.getRowCount();				
		for (int i = 0; i < rc; i++) {
			String error = table.getCellAsText(i, 0).trim();
			if (error.equals(message)) fail(XavaResources.getString(notFoundErrorId, message));
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
		log.error(XavaResources.getString("messages_produced", messages));
		fail(XavaResources.getString("message_not_found", message));
	}
	
	
	protected void assertNoErrors() throws Exception {
		assertNoMessages("errors", "Error");		
	}
	protected void assertNoMessages() throws Exception {
		assertNoMessages("messages", "Mensaje");		
	}
	
	private void assertNoMessages(String id, String label) throws Exception {		
		WebTable table = response.getTableWithID(id);
		if (table == null) {			
			return;
		}
		int rc = table.getRowCount();
		if (rc > 0) {
			for (int i = 0; i < rc; i++) {
				String message = table.getCellAsText(i, 0).trim();
				log.error(XavaResources.getString("unexpected_message", label, message));							
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
		assertTrue(XavaResources.getString("minimum_1_elements_in_list"), getListRowCount() > 0);
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
	
		
	private static String getDefaultLocale() {
		if (!isDefaultLocaleSet) {
			defaultLocale = getXavaJunitProperties().getProperty("locale");
			if (Is.emptyString(defaultLocale)) {
				defaultLocale = null;
			}
			isDefaultLocaleSet = true;
		}
		return defaultLocale;
	}
	
	
	
	protected static boolean isJetspeed2Enabled() {
		return !Is.emptyString(getJetspeed2URL());
	}
	
	protected static boolean isLiferayEnabled() {
		return !Is.emptyString(getLiferayURL());
	}
	
	/**
	 * Jetspeed2 or Liferay
	 */
	public static boolean isPortalEnabled() {
		return isLiferayEnabled() || isJetspeed2Enabled();
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
	
	private static String getLiferayURL() {
		if (liferayURL == null) {
			liferayURL = getXavaJunitProperties().getProperty("liferay.url");
		}
		return liferayURL;				
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
	static public String getProperty(String id) {
		return getXavaJunitProperties().getProperty(id);
	}
	
	/**
	 * From file xava-junit.properties 
	 */	
	static public String getProperty(String id, String defaultValue) {
		return getXavaJunitProperties().getProperty(id, defaultValue);
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
				log.warn(XavaResources.getString("xavajunit_properties_file_warning"),ex);
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
	
	
	/**
	 * Current WebForm (of httpunit). <p>
	 * 
	 * This allow you to access directly to html form elements, but
	 * it is not very advisable because this will cause dependency
	 * to HTML and it will be difficult migrate to another presentation technology.
	 */	
	protected WebForm getForm() {
		return form;	
	}	
	
	private int getFormIndex() throws Exception {
		if (formIndex == -1) {
			formIndex = getFormIndexForParameter("xava_page_id");			
		}
		return formIndex;
	}
	
	private int getLoginFormIndex() throws Exception {
		if (loginFormIndex == -1) {
			if (isLiferayEnabled()) {
				// Liferay
				loginFormIndex = getFormIndexForParameter("login");
			}
			else {
				// JetSpeed 2
				loginFormIndex = getFormIndexForParameter("org.apache.jetspeed.login.username");
			}
		}
		return loginFormIndex;
	}

	private int getFormIndexForParameter(String parameterName) throws SAXException {
		WebForm [] forms = response.getForms(); 
		for (int i = 0; i < forms.length; i++) {
			if (forms[i].hasParameterNamed(parameterName)) {					
				return i;				
			}
		}
		return 0;
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

	protected String getLocale() { 
		return locale == null?getDefaultLocale():locale;
	}

	protected void setLocale(String locale) throws Exception {
		this.locale = locale;
		resetModule();
	}		
	
	/**
	 * Returns a string representations of the key of a POJO
	 * from the POJO itself. <p>
	 * 
	 * Useful for obtaining the value to put into a combo (a descriptions list)
	 * from a POJO object.<br>
	 */
	protected String toKeyString(Object pojo) throws Exception { 
		return MetaModel.getForPOJO(pojo).toString(pojo);
	}
 			
}
