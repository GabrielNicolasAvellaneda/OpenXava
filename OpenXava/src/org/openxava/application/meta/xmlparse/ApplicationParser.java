package org.openxava.application.meta.xmlparse;


import org.openxava.application.meta.*;
import org.openxava.controller.meta.*;
import org.openxava.util.*;
import org.openxava.util.xmlparse.*;
import org.w3c.dom.*;

/**
 * @author: Javier Paniza
 */
public class ApplicationParser extends ParserBase {
	
			
	private ApplicationParser(String xmlFileURL, int language) {
		super(xmlFileURL, language);		
	}
	
	public static void configurarAplicaciones() throws XavaException {
		ApplicationParser enParser = new ApplicationParser("application.xml", ENGLISH);
		enParser.parse();		
		ApplicationParser esParser = new ApplicationParser("aplicacion.xml", ESPA�OL);
		esParser.parse();
	}


	private void addApplication() throws XavaException {
		MetaApplication application = new MetaApplication();				
		application.setName(getRoot().getAttribute(xname[lang]));
		application.setLabel(getRoot().getAttribute(xlabel[lang]));
		addModules(application);
		MetaApplications._addMetaApplication(application);
	}
	
	private void addModules(MetaApplication application) throws XavaException {
		NodeList l = getRoot().getElementsByTagName(xmodule[lang]);
		int c = l.getLength();
		for (int i = 0; i < c; i++) {
			MetaModule m = createModule(l.item(i));
			application.addMetaModule(m);
		}		
	}
		
	protected void createObjects() throws XavaException {
		addApplication();
	}
	
	private MetaModule createModule(Node n) throws XavaException {
		Element el = (Element) n;
		MetaModule m = new MetaModule();
		m.setName(el.getAttribute(xname[lang]));		
		m.setLabel(el.getAttribute(xlabel[lang]));
		m.setDescription(el.getAttribute(xdescription[lang]));
		m.setModelName(createModel(el));
		m.setViewName(createView(el));
		m.setTabName(createTab(el));
		m.setSwingViewClass(createSwingView(el));
		m.setWebViewURL(createWebView(el));
		m.setModeControllerName(createModeController(el));
		m.setMetaReport(createMetaReport(el));
		fillControllers(el, m);
		fillEnvironmentVar(el, m);		
		return m;
	}
	
	private String createModel(Element el) throws XavaException {
		NodeList l = el.getElementsByTagName(xmodel[lang]);
		if (l.getLength() > 0) {
			Element elModel = (Element) l.item(0);
			return elModel.getAttribute(xname[lang]);
		}
		return null;
	}
	
	private String createView(Element el) throws XavaException {
		NodeList l = el.getElementsByTagName(xview[lang]);
		if (l.getLength() > 0) {
			Element elModel = (Element) l.item(0);
			return elModel.getAttribute(xname[lang]);
		}
		return null;
	}
	
	private String createTab(Element el) throws XavaException {
		NodeList l = el.getElementsByTagName(xtab[lang]);
		if (l.getLength() > 0) {
			Element elModel = (Element) l.item(0);
			return elModel.getAttribute(xname[lang]);
		}
		return null;
	}
	
	
	
	private String createSwingView(Element el) throws XavaException {
		NodeList l = el.getElementsByTagName(xswing_view[lang]);
		if (l.getLength() > 0) {
			Element elSwingView = (Element) l.item(0);
			return elSwingView.getAttribute(xclass[lang]);
		}
		return null;
	}
	
	private String createWebView(Element el) throws XavaException {
		NodeList l = el.getElementsByTagName(xweb_view[lang]);
		if (l.getLength() > 0) {
			Element elWebView = (Element) l.item(0);
			return elWebView.getAttribute(xurl[lang]);
		}
		return null;
	}	
		
	private String createModeController(Element el) throws XavaException {
		NodeList l = el.getElementsByTagName(xmode_controller[lang]);
		if (l.getLength() > 0) {
			Element elModeControllerSecciones = (Element) l.item(0);
			return elModeControllerSecciones.getAttribute(xname[lang]);
		}
		return null;
	}	
	
	private MetaReport createMetaReport(Element el) throws XavaException {
		NodeList l = el.getElementsByTagName(xreport[lang]);
		if (l.getLength() > 0) {
			Element elReport = (Element) l.item(0);
			MetaReport metaReport = new MetaReport();
			metaReport.setModelName(elReport.getAttribute(xmodel[lang]));
			metaReport.setTabName(elReport.getAttribute(xtab[lang]));
			return metaReport;
		}
		return null;
	}
	
		
	private void fillControllers(Element el, MetaModule container) throws XavaException {
		NodeList l = el.getElementsByTagName(xcontroller[lang]);
		int c = l.getLength();
		if (MetaControllers.WEB.equals(MetaControllers.getContext()) && // en web 
			!Is.emptyString(container.getModelName()) && // con modelo
			Is.emptyString(container.getModeControllerName()) // y sin controlador de secciones
		) {
			container.addControllerName("Navigation");
			container.addControllerName("List");
		} 
		for (int i = 0; i < c; i++) {
			Element elController = (Element) l.item(i);
			String s = elController.getAttribute(xname[lang]);
			container.addControllerName(s);
		}
	}
	
	private void fillEnvironmentVar(Element el, MetaModule container) throws XavaException {
		NodeList l = el.getElementsByTagName(xenvironment_var[lang]);
		int c = l.getLength();
		for (int i = 0; i < c; i++) {
			Element elVar = (Element) l.item(i);
			String name = elVar.getAttribute(xname[lang]);
			String value = elVar.getAttribute(xvalue[lang]);
			container.addEnvironmentVariable(name, value);
		}
	}
	
	
}