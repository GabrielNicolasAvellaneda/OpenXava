package org.openxava.tests;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.*;

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
import org.openxava.web.*;
import org.openxava.web.style.*;
import org.w3c.dom.*;
import org.xml.sax.*;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.html.*;

import junit.framework.*;


/**
 * Base class for creating a junit test that runs against an OpenXava module. <p>
 * 
 * Look an
 * <a href="http://openxava.wikispaces.com/my-first-ox-project_en#toc6">
 * 	introduction to OpenXava module testing
 * </a> at
 * <a href="http://openxava.wikispaces.com/">
 * wiki
 * </a>.
 * 
 * @author Javier Paniza
 */

public class ModuleTestBase extends TestCase {
	
	private final static String EDITABLE_SUFIX = "_EDITABLE_";
	private final static String ACTION_PREFIX = "action"; 
	
	private static Log log = LogFactory.getLog(ModuleTestBase.class);
	
	private static Properties xavaJunitProperties;
	private static boolean isDefaultLocaleSet = false;
	private static String defaultLocale;
	private static String jetspeed2URL;
	private static String jetspeed2UserName;
	private static String jetspeed2Password;
	private static String liferayURL;	
	private static String host;
	private static String port;
	private static int loginFormIndex = -1;
	private String locale;
	private MetaModule metaModule;
	private MetaModel metaModel;
	private MetaView metaView;
	private MetaTab metaTab;	
	private String application;
	private String module;
	private WebClient client; 
	private HtmlPage page; 
	private HtmlForm form;
	private String lastNotNotifiedPropertyName; 
	private String lastNotNotifiedPropertyValue;
	private BrowserVersion browserVersion;	
	
	static {		
		XSystem._setLogLevelFromJavaLoggingLevelOfXavaPreferences();
		Logger.getLogger("com.gargoylesoftware").setLevel(Level.SEVERE);
		XHibernate.setConfigurationFile("/hibernate-junit.cfg.xml");
		XPersistence.setPersistenceUnit("junit");
		DataSourceConnectionProvider.setUseHibernateConnection(true);
	}
	
	/**
	 * To test the specified module of the specified application. <p>
	 * 
	 * You can send <code>null</code> for <code>null</code> in such a case you
	 * must use {@link #changeModule} at the very first of your test methods.<br> 
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
	 * property in <code>xava-junit.properties</code> file.<br>
	 * You can send <code>null</code> for <code>null</code> in such a case you
	 * must use {@link #changeModule} at the very first of your test methods.<br> 
	 */	
	public ModuleTestBase(String nameTest, String module) {
		this(nameTest, getXavaJunitProperties().getProperty("application"), module);
	}
	
	
	protected void setUp() throws Exception {
		locale = null;
		resetModule();	
	}
	
	protected void tearDown() throws Exception {
		if (isJetspeed2Enabled() && isJetspeed2UserPresent()) {
			logout();			
		}
		XHibernate.commit();
		XPersistence.commit();
		client = null;  
		page = null; 
		form = null; 
	}

	protected void login(String user, String password) throws Exception {
		if (isLiferayEnabled()) {
			// Liferay
			page = (HtmlPage) client.getPage("http://" + getHost() + ":" + getPort() + "/c/portal/login");
			resetLoginForm();			
			setFormValue("login", user, true, false);					
			setFormValue(getPasswordFieldInLiferay(), password, true, false);
			HtmlSubmitInput button = (HtmlSubmitInput) getForm().getHtmlElementsByAttribute("input", "type", "submit").get(0);
			page = (HtmlPage) button.click();
			
			try {
				page.getFormByName("fm"); // If not liferay 4.1 then throws ElementNotFoundException
				refreshPage();
			}
			catch (com.gargoylesoftware.htmlunit.ElementNotFoundException ex) {				
			}
						
			// The next line is because Liferay 5.0/5.1 does not go to private page on login,
			// and returns to the main guest page on logout; so going explicitly to the
			// module page after login is a secure way to go
			page = (HtmlPage) client.getPage(getModuleURL()); 
			resetForm();
		}
		else {
			// JetSpeed 2
			page = (HtmlPage) client.getPage("http://" + getHost() + ":" + getPort() + "/" + getJetspeed2URL() + "/portal/");
			resetLoginForm();			
			setFormValue("org.apache.jetspeed.login.username", user);
			setFormValue("org.apache.jetspeed.login.password", password);
			HtmlSubmitInput button = (HtmlSubmitInput) getForm().getHtmlElementsByAttribute("input", "type", "submit").get(0); 
			page = (HtmlPage) button.click();			
			page = (HtmlPage) client.getPage(getModuleURL()); 
			resetForm();
		}
	}
	
	private void setFormValueNoRefresh(String name, String value) throws Exception {
		setFormValue(name, value, false);
	}	
	
	private void setFormValue(String name, String value) throws Exception {
		setFormValue(name, value, true);
	}
	
	private void setFormValue(String name, String value, boolean refreshIfNeeded) throws Exception {
		setFormValue(name, value, refreshIfNeeded, true);
	}

	private void setFormValue(String name, String value, boolean refreshIfNeeded, boolean decorateName) throws Exception {
		boolean refreshNeeded = false;		
		String id = decorateName?decorateId(name):name; 
		try {
			HtmlInput input = getForm().getInputByName(id);	
			focus(input);
			assertNotDisable(name, input);
			if (input instanceof HtmlCheckBoxInput) {
				if ("true".equalsIgnoreCase(value) && !input.isChecked() ||
					"false".equalsIgnoreCase(value) && input.isChecked()) 
				{
					input.click();
					if (!Is.emptyString(input.getOnClickAttribute())) {
						refreshNeeded = true;				
					}
				}				
			}
			else if (input instanceof HtmlRadioButtonInput) {
				setRadioButtonsValue(id, value);
			}
			else {
				input.setValueAttribute(value);
			}
			if (!Is.emptyString(input.getOnChangeAttribute())) refreshNeeded = true;
		}
		catch (com.gargoylesoftware.htmlunit.ElementNotFoundException ex) {
			try {							
				HtmlSelect select = getForm().getSelectByName(id);
				focus(select);
				assertNotDisable(name, select);
				select.setSelectedAttribute(value, true);
				refreshNeeded = !Is.emptyString(select.getOnChangeAttribute());
			}
			catch (com.gargoylesoftware.htmlunit.ElementNotFoundException ex2) {
				HtmlTextArea textArea = getForm().getTextAreaByName(id);
				focus(textArea);
				assertNotDisable(name, textArea);
				textArea.setText(value);
				refreshNeeded = !Is.emptyString(textArea.getOnChangeAttribute());
			}
		}		
		if (refreshIfNeeded && refreshNeeded) refreshPage();
	}
	
	private void assertNotDisable(String name, HtmlElement element) { 		
		assertTrue(XavaResources.getString("element_cannot_be_disabled", name), !is("disabled", element)); 
		assertTrue(XavaResources.getString("element_cannot_be_readonly", name), !is("readonly", element)); 
	}

	private boolean is(String attribute, HtmlElement element) {
		String value = element.getAttribute(attribute);		
		if (HtmlElement.ATTRIBUTE_NOT_DEFINED.equals(value)) return false;
		if (HtmlElement.ATTRIBUTE_VALUE_EMPTY.equals(value)) return true;
		return !"false".equalsIgnoreCase(value);		
	}

	private void focus(HtmlElement element) throws Exception {
		element.focus();
		Thread.sleep(20);		
	}

	private void setRadioButtonsValue(String name, String value) {
		for (Iterator it=getForm().getInputsByName(name).iterator(); it.hasNext(); ) {
			HtmlRadioButtonInput radioButton = (HtmlRadioButtonInput) it.next();
			if (radioButton.getValueAttribute().equals(value)) {
				radioButton.setChecked(true);
				break;
			}
		}
	}
	
	private String getRadioButtonsValue(String name) {
		for (Iterator it=getForm().getInputsByName(name).iterator(); it.hasNext(); ) {
			HtmlRadioButtonInput radioButton = (HtmlRadioButtonInput) it.next();
			if (radioButton.isChecked()) {
				return radioButton.getValueAttribute();
			}
		}
		return "";
	}
	
	private void refreshPage() throws Exception {
		Thread.sleep(120);								
		resetForm();		
	}

	private String getFormValue(String name) {
		String id = decorateId(name); 
		try {			
			HtmlInput input = getForm().getInputByName(id);
			if (input instanceof HtmlRadioButtonInput) {
				return getRadioButtonsValue(id);
			}
			else if (input instanceof HtmlCheckBoxInput) {
				return Boolean.toString(input.isChecked());
			}
			else {
				return input.getValueAttribute();
			}
		}
		catch (com.gargoylesoftware.htmlunit.ElementNotFoundException ex) {
			try {
				HtmlSelect select = getForm().getSelectByName(id);				
				return ((HtmlOption )select.getSelectedOptions().get(0)).getValueAttribute();
			}
			catch (com.gargoylesoftware.htmlunit.ElementNotFoundException ex2) {
				return getForm().getTextAreaByName(id).getText();
			}
		}
	}
	
	private String [] getFormValues(String name) {		
		List elements = getForm().getInputsByName(name);
		if (elements.isEmpty()) {
			elements = getForm().getSelectByName(name).getSelectedOptions();			
		}
		String [] values = new String[elements.size()];
		int i=0;
		for (Iterator it = elements.iterator(); it.hasNext(); i++) {
			values[i] = ((HtmlElement) it.next()).getAttributeValue("value");
		}
		return values;
	}
	
	private boolean isOneMultipleSelect(List elements) { 
		if (elements.size() != 1) return false;
		Object element = elements.get(0);
		if (!(element instanceof HtmlSelect)) return false;
		return ((HtmlSelect) element).isMultipleSelectEnabled();
	}

	private void setFormValues(String name, String [] values) throws Exception {		
		List elements = new ArrayList(page.getHtmlElementsByName(name));		
		boolean refreshPage = false;
		if (isOneMultipleSelect(elements)) { 
			HtmlSelect select = (HtmlSelect) elements.get(0);
			unselectOptions(select);
			for (int i = 0; i < values.length; i++) {
				select.setSelectedAttribute(values[i], true);
			}			
			if (!Is.emptyString(select.getOnChangeAttribute())) {
				refreshPage = true;
			}			
		}
		else {
			int i=0;
			for (Iterator it = elements.iterator(); it.hasNext() && i < values.length; i++) {
				Object element = it.next();						
				if (element instanceof HtmlInput) {
					HtmlInput input = (HtmlInput) element;
					input.setValueAttribute(values[i]);
					if (!Is.emptyString(input.getOnChangeAttribute())) {
						refreshPage = true;
					}
				}
				else if (element instanceof HtmlSelect) {				
					HtmlSelect select = (HtmlSelect) element;		
					select.setSelectedAttribute(values[i], true);			
					if (!Is.emptyString(select.getOnChangeAttribute())) {
						refreshPage = true;
					}
				}
			}
		}
		if (refreshPage) refreshPage();
	}
			

	private void unselectOptions(HtmlSelect select) { 
		for (Iterator it = select.getOptions().iterator(); it.hasNext(); ) {
			HtmlOption option = (HtmlOption) it.next();
			option.setSelected(false);
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
		Collection inputs = getForm().getHtmlElementsByTagName("input");		
		String passwordTextField = "password";
		for (Iterator it = inputs.iterator(); it.hasNext(); ) {
			HtmlInput input = (HtmlInput) it.next();
			String elementName = input.getNameAttribute();
			if (elementName.endsWith("_password")) {
				passwordTextField = elementName; 
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
			page = (HtmlPage) client.getPage("http://" + getHost() + ":" + getPort() + "/c/portal/logout?referer=/c");			
		}
		else { 
			// Jetspeed 2
			page = (HtmlPage) client.getPage("http://" + getHost() + ":" + getPort() + "/" + getJetspeed2URL() + "/login/logout");
		}
	}
	
	/**
	 * Like close navigator, open again, and reexecute the module.
	 */
	protected void resetModule() throws Exception {		
		client = new WebClient(getBrowserVersion()); 		
		client.setThrowExceptionOnFailingStatusCode(false); // Because some .js are missing in Liferay 4.1
		client.setThrowExceptionOnScriptError(false); // Because some erroneous JavaScript in Liferay 4.3		
		if (getLocale() != null) {
			client.addRequestHeader("Accept-Language", getLocale());			
			Locale.setDefault(new Locale(getLocale(), ""));
		}
		if (isJetspeed2Enabled() && isJetspeed2UserPresent()) {		
			login(getJetspeed2UserName(), getJetspeed2Password());
		}		
		else {	
			if (this.module != null) {				
				page = (HtmlPage) client.getPage(getModuleURL());
				if (!getMetaModule().isDoc()) {
					resetForm();
				}
			}			
		}				
	}
	
	private BrowserVersion getBrowserVersion() {
		if (browserVersion == null) {
			String browser = getProperty("browser", "firefox2");
			if ("firefox2".equalsIgnoreCase(browser)) browserVersion = BrowserVersion.FIREFOX_2;
			else if ("iexplorer7".equalsIgnoreCase(browser)) browserVersion = BrowserVersion.INTERNET_EXPLORER_7_0;
			else if ("iexplorer6".equalsIgnoreCase(browser)) browserVersion = BrowserVersion.INTERNET_EXPLORER_6_0;
			else {
				log.warn(XavaResources.getString("unknown_browser_using_default", "Firefox 2"));
				browserVersion = BrowserVersion.FIREFOX_2;
			}
		}
		return browserVersion;
	}
	
	protected void selectModuleInPage(String module) throws Exception { 
		changeModule(application, module, false);
	}

	protected void changeModule(String module) throws Exception {
		changeModule(this.application, module);
	}
	
	protected void changeModule(String application, String module) throws Exception {
		changeModule(application, module, true);
	}
	
	private void changeModule(String application, String module, boolean reloadPage) throws Exception { 
		this.application = application;
		this.module = module;
		metaModule = null;
		metaModel = null;
		metaView = null;
		metaTab = null;				
		if (reloadPage) page = (HtmlPage) client.getPage(getModuleURL()); 
		resetForm();		
	}
	
		
	protected String getModuleURL() throws XavaException { 
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
	 * 
	 * @deprecated Now the model is deduced automatically 
	 */
	protected void setModel(String defaultModel) { 
	}
	
	/**
	 * Put the model of the module as the default model of the test. <p>
	 * 
	 * This is the default setting, hence this method is called
	 * to restore the original setting.	 
	 * 
	 * @deprecated Now this is done automatically
	 */
	protected void setModelToModuleSetting() { 		
	}
	

	
	/**
	 * Execute the action clicking in the link or button.
	 */
	protected void execute(String action) throws Exception {
		// Before click in the buttom, we blur from the current element
		HtmlElement focusedElement = page.getFocusedElement();
		if (focusedElement != null) {
			focusedElement.blur();
		}
		
		throwChangeOfLastNotNotifiedProperty(); 
		if (page.getHtmlElementsByName(Ids.decorate(application, module, ACTION_PREFIX + "." + action)).size() > 1) { // Action of list/collection
			execute(action, null);
			return;
		}		
				
		Element element  = getElementById(action);  
		if (element instanceof ClickableElement) {
			Page newPage = ((ClickableElement) element).click();
			if (newPage instanceof HtmlPage) {
				page = (HtmlPage) newPage;								
			}
			resetForm(); 
		}
		else {
			fail(XavaResources.getString("clickable_not_found", decorateId(action)));   
		}
	}

	private void throwChangeOfLastNotNotifiedProperty() throws Exception { 		
		if (lastNotNotifiedPropertyName != null) {
			setFormValueNoRefresh(lastNotNotifiedPropertyName, lastNotNotifiedPropertyValue);
			lastNotNotifiedPropertyName = null;
			lastNotNotifiedPropertyValue = null;
		}	
	}
										 	
	private void waitUntilPageIsLoaded() { 
		HtmlInput loading = (HtmlInput) getElementById("loading");
		for (int i=0; !"true".equals(loading.getValueAttribute()) && i<20; i++) {
			try { Thread.sleep(100); } catch (Exception ex) { }			
		}				
		while ("true".equals(loading.getValueAttribute())) {
			try { Thread.sleep(20); } catch (Exception ex) { }			
		}	
		if (getLoadedParts().endsWith("ERROR")) {
			fail(XavaResources.getString("ajax_loading_parts_error"));
		}
	}

	private HtmlElement getElementById(String id) { 
		return page.getHtmlElementById(decorateId(id));
	}
	
	/**
	 * Decorate the name to produced an unique identifier as the used by
	 * OX for HTML elements.
	 */
	protected String decorateId(String name) {  
		return Ids.decorate(application, module, name);
	}

	protected void assertFocusOn(String name) throws Exception {		
		String expectedFocusProperty = decorateId(name); 
		HtmlElement element = page.getFocusedElement(); 
		String focusProperty = element==null?null:element.getAttributeValue("name");
		assertEquals(XavaResources.getString("focus_in_unexpected_place"), expectedFocusProperty, focusProperty);		
	}
	
	protected void execute(String action, String arguments) throws Exception {
		throwChangeOfLastNotNotifiedProperty();
		ClickableElement element = null;
		String moduleMarkForAnchor = "executeAction('" + application + "', '" + module + "'";		
		for (Iterator it = page.getAnchors().iterator(); it.hasNext(); ) {			
			HtmlAnchor anchor = (HtmlAnchor) it.next();			
			if (arguments != null) { // 'List.viewDetail', 'row=0'				
				if (
					(
						anchor.getHrefAttribute().endsWith("'" + action + "', '" + arguments + "')") ||
						anchor.getHrefAttribute().endsWith("'" + action + "', '," + arguments + "')")
					)
					&& anchor.getHrefAttribute().indexOf(moduleMarkForAnchor) >= 0)  			
				{				
					element = anchor;				
				}
			}
			else { // 'ReferenceSearch.choose'				
				if (anchor.getHrefAttribute().endsWith("'" + action + "')")) {				
					element = anchor;				
				}				
			}
		}
		if (arguments == null && element == null) { // We try if it is a button
			String moduleMarkForButton = "executeAction(\"" + application + "\", \"" + module + "\"";
			for (Iterator it = getForm().getHtmlElementsByAttribute("input", "id", decorateId(action)).iterator(); it.hasNext(); ) { 
				Object inputElement = it.next();
				if (!(inputElement instanceof HtmlInput)) continue;
				HtmlInput input = (HtmlInput) inputElement;
				if ("button".equals(input.getTypeAttribute()) &&
					input.getOnClickAttribute().endsWith("\"" + action + "\")") &&
					input.getOnClickAttribute().indexOf(moduleMarkForButton) >= 0)
				{				
					element = input;				
				}				
			}
		}
		if (element != null) {
			Page newPage = element.click();
			if (newPage instanceof HtmlPage) {
				page = (HtmlPage) newPage;				
				resetForm();				
			}
			else {
				page = null; 
			}
		}
		else {
			if (isReferenceActionWithObsoleteStyle(action, arguments)) {		
				log.warn(XavaResources.getString("keyProperty_obsolete_style"));
				execute(action, refineArgumentsForReferenceActionWithObsoleteStyle(arguments));
				return;
			}
			fail(XavaResources.getString("clickable_not_found", action));  
		}		
		
	}
	
	private String refineArgumentsForReferenceActionWithObsoleteStyle(String arguments) {
		int idx1 = arguments.indexOf("keyProperty=xava.");
		int idx2 = arguments.indexOf(".", idx1 + 17);		
		return arguments.substring(0, idx1 + 12) + arguments.substring(idx2 + 1);
	}

	private boolean isReferenceActionWithObsoleteStyle(String action, String arguments) { 
		return action.startsWith("Reference.") && arguments.indexOf("keyProperty=xava.") >= 0;
	}

	protected void executeDefaultAction() throws Exception {
		HtmlButton button = getForm().getButtonByName("xava.DEFAULT_ACTION");
		page = (HtmlPage) button.click();
		resetForm();
	}
	
	protected void assertExists(String name) throws Exception {		 
		if (!hasElementByName(name) && !existsCollection(name)) {
			fail(XavaResources.getString("must_exist", name)); 
		}
	}

	protected void assertNotExists(String name) throws Exception { 		 		
		if (hasElementByName(name) || existsCollection(name)) {
			fail(XavaResources.getString("must_not_exist", name)); 
		} 
	}
	
	private boolean existsCollection(String collection) {
		return hasElementById("collection_" + collection + ".");
	}
	
	/**
	 * In the case of combo (descriptionsEditor.jsp) (or his read only version)
	 * the value that is visualized to user.
	 * @return
	 */
	protected String getDescriptionValue(String name) throws Exception {		
		return getFormValue(decorateId(name) + "__DESCRIPTION__"); 
	}
		
	protected String getValue(String name) throws Exception {		
		return getFormValue(decorateId(name)); 
	}
	
	/**
	 * For properties with multiple values
	 */
	protected String [] getValues(String name) throws Exception {		
		return getFormValues(decorateId(name));
	}	
	
	protected String getLabel(String name) throws Exception {
		HtmlElement element = page.getHtmlElementById(decorateId("label_" + name)); 
		if (element == null) {
			fail(XavaResources.getString("label_not_found_in_ui", name));
		}
		return element.asText().trim();
	}
		
	/**
	 * In case we does not work with main view.
	 * 
	 * @deprecated  The model is automatically deduced, so you can use just getValue(String name)
	 */
	protected String getValue(String model, String name) throws Exception {		
		return getFormValue(name);  
	}
		
	/**
	 * Only for debug.
	 */
	protected void printHtml() throws Exception {		
		log.debug(getHtml());		
	}
	
	/**
	 * Util for web applications, but using it make the test web dependent. 
	 * 
	 * @param type text/html, application/pdf, etc.
	 */
	protected void assertContentType(String type) {
		assertEquals(XavaResources.getString("content_type_not_match"), type, page.getWebResponse().getContentType());
	}
	
	/**
	 * Util for web applications, but using it make the test web dependent. 
	 * 
	 * @param type text/html, application/pdf, etc.
	 */
	protected void assertContentTypeForPopup(String type) {
		for (int i=0; !type.equals(getPopupResponse().getContentType()) && i<20; i++) {
			try { Thread.sleep(500); } catch (Exception ex) { }
		}
		assertEquals(XavaResources.getString("content_type_not_match"), type, getPopupResponse().getContentType());
	}	
	
	/**
	 * Response for a second window
	 * @return
	 */
	private WebResponse getPopupResponse() {
		return getPopupPage().getWebResponse();		
	}
	
	/**
	 * Page for a second window
	 * @return
	 */
	private Page getPopupPage() {
		List windows = client.getWebWindows();		
		if (windows.size() < 2) {
			fail(XavaResources.getString("popup_window_not_found"));
		}		
		for (int i=windows.size() - 1; i > 0; i--) {
			Page page = ((WebWindow) windows.get(i)).getEnclosedPage();
			if (page != null) return page;
		}
		fail(XavaResources.getString("popup_window_not_found"));
		return null;
	}	
	
	
	
	protected void assertNoPopup() throws Exception {
		List windows = client.getWebWindows();
		assertTrue(XavaResources.getString("unexpected_popup"), windows.size() < 2);
	}
	
	/**
	 * Current HTML code.
	 * <p>
	 * It is not very advisable because this will cause dependency
	 * to HTML and it will be difficult migrate to another presentation technology.
	 */
	protected String getHtml() throws IOException {
		return page.asXml()	
			.replaceAll("&apos;", "'") 
			.replaceAll("&lt;", "<")
			.replaceAll("&gt;", ">")	
			.replaceAll("&quot;", "\"");
	}
	
	/**
	 * The text of the response
	 */
	protected String getText() throws IOException {
		return page.asText();
	}

	/**
	 * The text of the response for popup window
	 */
	protected String getPopupText() throws IOException {
		return getPopupPage().getWebResponse().getContentAsString();
	}

	protected void setConditionValues(String [] values) throws Exception {
		setCollectionCondition("conditionValue", values);
	}

	private void setCollectionCondition(String id, String[] values) throws Exception { 
		for (int i=0; i<values.length; i++) {
			try {				
				setFormValue(id + "." + i, values[i]);	
			}
			catch (com.gargoylesoftware.htmlunit.ElementNotFoundException ex) {				
				break;
			}
		}
	}
	
	protected void setConditionComparators(String [] values) throws Exception {
		filterConditionComparators(values);
		setCollectionCondition("conditionComparator", values); 
	}
	
	protected void setConditionValues(String collection, String [] values) throws Exception {
		String collectionId = Tab.COLLECTION_PREFIX + Strings.change(collection, ".", "_") + "_conditionValue";
		setCollectionCondition(collectionId, values);
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

	protected void setConditionComparators(String collection, String [] values) throws Exception { 
		filterConditionComparators(values);
		setCollectionCondition(Tab.COLLECTION_PREFIX + collection + "conditionComparators", values);
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
		String qualifiedName = decorateId(name); 
		HtmlInput input = getForm().getInputByName(qualifiedName);
		input.setAttributeValue("value", value); // In this way onchange is not thrown 
		lastNotNotifiedPropertyName = qualifiedName; 
		lastNotNotifiedPropertyValue = value; 
	}
	
	protected void setValue(String name, String value) throws Exception {		
		setFormValue(decorateId(name), value); 
	}	
	
	/**
	 * For multiple values properties
	 */
	protected void setValues(String name, String [] values) throws Exception {					
		setFormValues(decorateId(name), values); 
	}
	
	
	protected void setFileValue(String name, String filePath) throws Exception {
		setFormValue(name, filePath, true, false); 			
	}
	
	/**
	 * In case we do not work with main view. <p>
	 * 
	 * @deprecated  Now model is deduced automatically, so you can use setValue(String model, String value)
	 */
	protected void setValue(String model, String name, String value) throws Exception {  
		setValue(name, value); 
	}
	
	protected void assertLabel(String name, String expectedLabel) throws Exception {		
		assertEquals(XavaResources.getString("unexpected_label", name), expectedLabel, getLabel(name));		
	}
	
	protected void assertNoLabel(String name) throws Exception{
		try{
			getLabel(name);
			fail(XavaResources.getString("label_found_in_ui", name));
		}
		catch(ElementNotFoundException ex){
		}
	}
	
	protected void assertValue(String name, String value) throws Exception {		
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
	
	private Collection getActions() {
		Collection hiddens = getForm().getHtmlElementsByAttribute("input", "type", "hidden");
		Set actions = new HashSet();		
		for (Iterator it = hiddens.iterator(); it.hasNext(); ) {
			HtmlInput input = (HtmlInput) it.next();
			if (!input.getNameAttribute().startsWith(Ids.decorate(application, module, ACTION_PREFIX))) continue;
			actions.add(removeActionPrefix(input.getNameAttribute()));			
		}								
		return actions;		
	}		
			
	protected void assertActions(String [] expectedActions) throws Exception {
		Collection actionsInForm = getActions();			
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
		String bareAction = Ids.undecorate(action);
		return bareAction.substring(ACTION_PREFIX.length() + 1);
	}

	protected String getValueInList(int row, String name) throws Exception {		
		int column = getMetaTab().getPropertiesNames().indexOf(name);		
		return getValueInList(row, column);
	}
	
	protected String getValueInList(int row, int column) throws Exception {
		return getTableCellInList(row, column).asText().trim();
	}
	
	private HtmlTable getTable(String id, String errorId) { 
		try {
			return (HtmlTable) getElementById(id);
		}
		catch (com.gargoylesoftware.htmlunit.ElementNotFoundException ex) {
			fail(XavaResources.getString(errorId, id));
			return null;
		}		
	}
		
	private HtmlTableCell getTableCellInList(int row, int column) throws Exception {
		return getTable("list", "list_not_displayed").getCellAt(row+2, column+2);
	}
	
	private HtmlTableRow getTableRow(String tableId, int row) throws Exception {
		return getTable(tableId, "collection_not_displayed").getRow(row+2);
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
		return getTableCellInCollection(collection, row, column).asText().trim();
	}
	
	private HtmlTableCell getTableCellInCollection(String collection, int row, int column) throws Exception {		
		HtmlTable table = getTable(collection, "collection_not_displayed");
		row = collectionHasFilterHeader(table)?row + 2:row + 1;
		int increment = hasLinks(table.getCellAt(row, 0)) || hasLinks(table.getCellAt(0, 0))?2:1;
		return table.getCellAt(row, column + increment);		
	}
	
	private boolean hasLinks(HtmlElement element) {
		return !element.getHtmlElementsByTagName("a").isEmpty();
	}

	protected void assertRowStyleInList(int row, String expectedStyle) throws Exception {
		assertRowStyle("list", row, expectedStyle);
	}
	
	protected void assertRowStyleInCollection(String collection, int row, String expectedStyle) throws Exception {
		assertRowStyle(collection, row, expectedStyle);
	}	
	
	private void assertRowStyle(String tableId, int row, String expectedStyle) throws Exception {
		// When testing again a portal styleClass in xava.properties must match with
		// the tested portal in order that this method works fine
		HtmlTableRow tableRow = getTableRow(tableId, row);
		String style = tableRow.getAttribute("class");
		int countTokens = new StringTokenizer(style).countTokens();
		int defaultStyleCountTokens = new StringTokenizer(getDefaultRowStyle(row)).countTokens(); 
		style = countTokens <= defaultStyleCountTokens?"":Strings.lastToken(style); 
		assertEquals(XavaResources.getString("row_style_not_excepted"), expectedStyle, style);		
	}
	
	protected void assertNoRowStyleInList(int row) throws Exception {
		assertNoRowStyle("list", row);
	}
	
	protected void assertNoRowStyleInCollection(String collection, int row) throws Exception {
		assertNoRowStyle(collection, row);
	}	
	
	private void assertNoRowStyle(String tableId, int row) throws Exception {
		HtmlTableRow tableRow = getTableRow(tableId, row);
		String style = tableRow.getAttribute("class");
		assertEquals(XavaResources.getString("row_style_not_excepted"), 
			new StringTokenizer(getDefaultRowStyle(row)).countTokens(),
			new StringTokenizer(style).countTokens());
	}

	private String getDefaultRowStyle(int row) {
		return (row % 2 == 0)?Style.getInstance().getListPair():Style.getInstance().getListOdd();
	}
	
	private int getListRowCount(String tableId, String message) throws Exception {
		HtmlTable table = getTable(tableId, message);
		if (table.getRowCount() > 2 && "nodata".equals(table.getRow(2).getId())) { 
			return 0;
		}						
		int increment = collectionHasFilterHeader(table)?2:1;
		return table.getRowCount() - increment;
	}
	
	private boolean collectionHasFilterHeader(HtmlTable table) { 				
		return table.getRowCount() > 1 && 
			!table.getCellAt(1, 0).
				getHtmlElementsByAttribute("input", "name", 
					Ids.decorate(application, module,
						ACTION_PREFIX + ".List.filter")).isEmpty();
	}
	
	private boolean collectionHasFilterHeader(String collection) throws Exception { 
		return collectionHasFilterHeader(getTable(collection, "collection_not_displayed"));
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
		return getTable(id, message).getRow(0).getCells().size() - 2;		
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
		assertEquals(XavaResources.getString("label_not_match", new Integer(column)), label, 
				getTable(tableId, message).getCellAt(0, column+2).asText().trim());
	}		
	
	protected void checkRow(int row) throws Exception {
		checkRow("selected", row);
	}
	
	protected void uncheckRow(int row) throws Exception {
		uncheckRow("selected", row);
	}
		
	protected void checkRowCollection(String collection, int row) throws Exception {		
		if (collectionHasFilterHeader(collection)) {
			checkRow(Tab.COLLECTION_PREFIX + collection.replace('.', '_') + "_selected", row);
		}
		else {			
			checkRow(collection + ".__SELECTED__", row);
		}
	}
	
	private HtmlInput getCheckable(String id, int row) {
		return (HtmlInput) getForm().getInputByValue(id + ":" + row); 
	}
		
	private void checkRow(String id, int row) throws Exception {
		getCheckable(id, row).setChecked(true);
	}
	
	protected void checkRow(String id, String value) throws Exception {
		try {
			HtmlInput input = getForm().getInputByValue(id + ":" + value); 
			input.click();
		}
		catch (com.gargoylesoftware.htmlunit.ElementNotFoundException ex) {
			fail(XavaResources.getString("must_exist", id));
		}
	}
		
	private void uncheckRow(String id, int row) throws Exception {		
		getCheckable(id, row).setChecked(false);
	}
			
	protected void assertRowChecked(int row) { 
		assertRowChecked("selected", row);
	}
	
	protected void assertRowCollectionChecked(String collection, int row) throws Exception { 
		if (collectionHasFilterHeader(collection)) {
			assertRowChecked(Tab.COLLECTION_PREFIX + collection.replace('.', '_') + "_selected", row);
		}
		else {			
			assertRowChecked(decorateId(collection + "." + "__SELECTED__"), row);
		}
	}	
	
	private void assertRowChecked(String id, int row) { 
		assertTrue(XavaResources.getString("selected_rows_not_match"), 
				getCheckable(id, row).isChecked()); 		
	}	
	
	protected void assertRowsChecked(int f1, int f2) {
		assertRowsChecked(new int [] {f1, f2});
	}
		
	protected void assertRowsChecked(int [] rows) {
		for (int i = 0; i < rows.length; i++) {
			assertRowChecked(rows[i]);
		}
	}
	
	protected void assertRowUnchecked(int row) { 
		assertRowUnchecked("selected", row);
	}

	protected void assertRowCollectionUnchecked(String collection, int row) throws Exception { 
		if (collectionHasFilterHeader(collection)) {
			assertRowUnchecked(Tab.COLLECTION_PREFIX + collection.replace('.', '_') + "_selected", row);
		}
		else {			
			assertRowUnchecked(decorateId(collection + "." + "__SELECTED__"), row);
		}
	}	
	
	private void assertRowUnchecked(String id, int row) { 
		assertTrue(XavaResources.getString("selected_row_unexpected", new Integer(row)), 
				!getCheckable(id, row).isChecked());
	}
	
	protected void assertError(String message) throws Exception {
		HtmlTable table = null;
		try {
			table = (HtmlTable) getElementById("errors_table"); 
		}
		catch (com.gargoylesoftware.htmlunit.ElementNotFoundException ex) {
			fail(XavaResources.getString("error_not_found", message));
			return;
		}		
		int rc = table.getRowCount();
		StringBuffer errors = new StringBuffer();
		for (int i = 0; i < rc; i++) {
			String error = table.getCellAt(i, 0).asText().trim();
			errors.append(error);
			errors.append('\n');			
			if (error.equals(message)) return;
		}
		log.error(XavaResources.getString("errors_produced", errors));
		fail(XavaResources.getString("error_not_found", message));
	}
	
	protected void assertErrorsCount(int expectedCount) throws Exception {
		HtmlTable table = null;
		try {
			table = (HtmlTable) getElementById("errors_table"); 
		}
		catch (com.gargoylesoftware.htmlunit.ElementNotFoundException ex) {
			if (expectedCount > 0) {
				fail(XavaResources.getString("no_error_and_expected", new Integer(expectedCount)));
			}
			return;
		}		
		assertEquals(XavaResources.getString("errors_count_unexpected"), expectedCount, table.getRowCount());
	}
	
	protected void assertMessagesCount(int expectedCount) throws Exception {
		HtmlTable table = null;
		try {
			table = (HtmlTable) getElementById("messages_table");
		}
		catch (com.gargoylesoftware.htmlunit.ElementNotFoundException ex) {
			if (expectedCount > 0) {
				fail(XavaResources.getString("no_message_and_expected", new Integer(expectedCount)));
			}
			return;
		}				
		assertEquals(XavaResources.getString("messages_count_unexpected"), expectedCount, table.getRowCount());
	}
			
	protected void assertNoError(String message) throws Exception {
		assertNoMessage(message, "errors_table", "error_found");
	}
	
	protected void assertNoMessage(String message) throws Exception {
		assertNoMessage(message, "messages_table", "message_found"); 
	}
		
	private void assertNoMessage(String message, String id, String notFoundErrorId) throws Exception {
		HtmlTable table = null;
		try {
			table = (HtmlTable) getElementById(id); 
		}
		catch (com.gargoylesoftware.htmlunit.ElementNotFoundException ex) {
			return;
		}								
		int rc = table.getRowCount();				
		for (int i = 0; i < rc; i++) {
			String error = table.getCellAt(i, 0).asText().trim();
			if (error.equals(message)) fail(XavaResources.getString(notFoundErrorId, message));
		}
	}
		
	/**
	 * The first message
	 */
	protected String getMessage() throws Exception {
		HtmlTable table = null;
		try {
			table = (HtmlTable) getElementById("messages_table"); 
		}
		catch (com.gargoylesoftware.htmlunit.ElementNotFoundException ex) {
			return "";
		}
		if (table.getRowCount() == 0) return "";
		return table.getCellAt(0, 0).asText().trim();
	}	
	
	protected void assertMessage(String message) throws Exception {
		HtmlTable table = null;
		try {
			table = (HtmlTable) getElementById("messages_table"); 
		}
		catch (com.gargoylesoftware.htmlunit.ElementNotFoundException ex) {
			fail(XavaResources.getString("message_not_found", message));
			return;
		}
		int rc = table.getRowCount();
		StringBuffer messages = new StringBuffer();
		for (int i = 0; i < rc; i++) {
			String m = table.getCellAt(i, 0).asText().trim();
			if (m.equals(message)) return;
			messages.append(m);
			messages.append('\n');												
		}
		log.error(XavaResources.getString("messages_produced", messages));
		fail(XavaResources.getString("message_not_found", message));
	}
	
	
	protected void assertNoErrors() throws Exception {
		assertNoMessages("errors_table", "Error");		
	}
	protected void assertNoMessages() throws Exception {
		assertNoMessages("messages_table", "Mensaje");		
	}
	
	private void assertNoMessages(String id, String label) throws Exception {
		HtmlTable table = null;
		try {
			table = (HtmlTable) getElementById(id); 
		}
		catch (com.gargoylesoftware.htmlunit.ElementNotFoundException ex) {
			return;
		}				
		int rc = table.getRowCount();
		if (rc > 0) {
			for (int i = 0; i < rc; i++) {
				String message = table.getCellAt(i, 0).asText().trim();
				log.error(XavaResources.getString("unexpected_message", label, message));							
			}			
			fail(XavaResources.getString("unexpected_messages", label.toLowerCase() + "s"));
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
		Collection options = getForm().getSelectByName(decorateId(name)).getOptions(); 
		assertEquals(XavaResources.getString("unexpected_valid_values", name), values.length, options.size());
		int i=0;
		for (Iterator it = options.iterator(); it.hasNext(); i++) {
			HtmlOption option = (HtmlOption) it.next();
			assertEquals(XavaResources.getString("unexpected_key", name), values[i][0], option.getValueAttribute()); 
			assertEquals(XavaResources.getString("unexpected_description", name), values[i][1], option.asText());			
		}
	}
	
	protected void assertValidValuesCount(String name, int count) throws Exception {
		HtmlSelect select = getForm().getSelectByName(decorateId(name)); 
		assertEquals(XavaResources.getString("unexpected_valid_values", name), count, select.getOptionSize());
	}
	
	
	protected String [] getKeysValidValues(String name) throws Exception {
		Collection options = getForm().getSelectByName(decorateId(name)).getOptions(); 
		String [] result = new String[options.size()];
		int i=0;
		for (Iterator it = options.iterator(); it.hasNext(); i++) {
			result[i] = ((HtmlOption) it.next()).getValueAttribute();
		}	
		return result;
	}
	
	protected void assertEditable(String name) throws Exception {
		assertEditable(name, "true", XavaResources.getString("must_be_editable"));
	}
	
	protected void assertNoEditable(String name) throws Exception {
		assertEditable(name, "false", XavaResources.getString("must_not_be_editable"));
	}
		
	private void assertEditable(String name, String value, String  message) throws Exception {
		String v = getValue(name + EDITABLE_SUFIX);		
		assertTrue(name + " " + message, value.equals(v)); 		
	}
	
	protected void assertListTitle(String expectedTitle) throws Exception {
		HtmlTable table = null;
		try {
			table = (HtmlTable) page.getHtmlElementById("list-title");
		}
		catch (com.gargoylesoftware.htmlunit.ElementNotFoundException ex) {
			fail(XavaResources.getString("title_not_displayed"));
			return;
		}				
		assertEquals(XavaResources.getString("incorrect_title"), expectedTitle, table.getCellAt(0, 0).asText());
	}
	
	protected void assertNoListTitle() throws Exception {		
		try {
			page.getHtmlElementById("list-title");
			fail(XavaResources.getString("title_displayed"));
		}
		catch (com.gargoylesoftware.htmlunit.ElementNotFoundException ex) {
		}				
	}
	
	protected void assertListNotEmpty() throws Exception {
		assertTrue(XavaResources.getString("minimum_1_elements_in_list"), getListRowCount() > 0);
	}
		
	protected static String getPort() { 
		if (port == null) {
			port = getXavaJunitProperties().getProperty("port", "8080");
		}
		return port;		
	}
	
	protected static String getHost() { 
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
	
	protected static String getLiferayURL() { 
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
		waitUntilPageIsLoaded();
		setNewModuleIfChanged(); 
		form = null; 		
	}
	
	private void setNewModuleIfChanged() throws Exception {		
		String lastModuleChange = ((HtmlInput) page.getElementById("xava_last_module_change")).getValueAttribute();
		if (Is.emptyString(lastModuleChange)) return;
		String [] modules = lastModuleChange.split("::");
		if (!module.equals(modules[0])) return;
		module = modules[1];
	}

	private void resetLoginForm() throws Exception { 
		form = ((HtmlForm)page.getForms().get(getLoginFormIndex()));		
	}
			
	/**
	 * Current HtmlForm (of HtmlUnit). <p>
	 * 
	 * This allow you to access directly to html form elements, but
	 * <b>it is not very advisable</i> because this will cause dependency
	 * to HTML and HtmlUnit so it will be difficult migrate to another 
	 * presentation technology.
	 */	
	protected HtmlForm getForm() { 
		if (form == null) {
			form = page.getFormByName(decorateId("form"));
		}
		return form;	
	}	
	
	private int getLoginFormIndex() throws Exception {
		if (loginFormIndex == -1) {
			if (isLiferayEnabled()) {
				// Liferay
				loginFormIndex = getFormIndexForInputElement("login");
			}
			else {
				// JetSpeed 2
				loginFormIndex = getFormIndexForInputElement("org.apache.jetspeed.login.username");
			}
		}
		return loginFormIndex;
	}
	
	
	private int getFormIndexForInputElement(String inputElementName) throws SAXException {
		Collection forms = page.getForms(); 
		int i = 0;
		for (Iterator it = forms.iterator(); it.hasNext(); i++) {
			HtmlForm form = (HtmlForm) it.next();			
			if (hasInput(form, inputElementName)) {					
				return i;				
			}
		}
		return 0;
	}	
	
	private boolean hasElementByName(String elementName) {
		return !page.getHtmlElementsByName(decorateId(elementName)).isEmpty();
	}
	
	private boolean hasElementById(String elementId) { 
		try {
			getElementById(elementId);
			return true;
		}
		catch (com.gargoylesoftware.htmlunit.ElementNotFoundException ex) {			
			return false;
		}		
	}		
		
	private boolean hasInput(HtmlForm form, String inputName) {
		try {
			form.getInputByName(inputName);
			return true;
		}
		catch (com.gargoylesoftware.htmlunit.ElementNotFoundException ex) {
			return false;
		}
	}

	/**
	 * This allows you testing using HtmlUnit APIs directly. <p>
	 * 
	 * The use of <b>this method is discoraged</b> because binds your test
	 * to a HTML implemenation.
	 * Before to use this method look for another more abstract method
	 * in this class.
	 */
	protected WebClient getWebClient() {
		return client;
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
	
	private String getLoadedParts() { 
		Page page = getWebClient().getCurrentWindow().getEnclosedPage();
		if (!(page instanceof HtmlPage)) return "";
		try {
			HtmlInput input = (HtmlInput) ((HtmlPage) page).getHtmlElementById(Ids.decorate(application, module, "loaded_parts"));
			return input.getValueAttribute();
		}
		catch (com.gargoylesoftware.htmlunit.ElementNotFoundException ex) {
			return "";
		}
	}
	
}
