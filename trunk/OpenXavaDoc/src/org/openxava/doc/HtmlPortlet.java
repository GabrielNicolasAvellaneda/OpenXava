package org.openxava.doc;

import java.io.*;

import javax.portlet.*;

/**
 * Return a HTML page. <p>
 * 
 * You only need to define the url of the page.<br> 
 * In this way:
 *
 * <pre>
 *  <!-- Portlet Preferences -->
 *  <portlet-preferences>
 *    <preference>
 *      <description>My page</description>
 *      <name>Page</name>
 *      <value>mypage</value>
 *      <non-modifiable/>
 *    </preference>
 *    <preference>
 *      <description>Supported languages</description>
 *      <name>Languages</name>
 *      <value>en,es,de</value>
 *      <non-modifiable/>
 *    </preference>    
 *  </portlet-preferences>
 * </pre>
 * 
 * The <tt>Languages</tt> param is optional, by default "en,es" is assumed as value.<br> 
 *
 * @author  Javier Paniza
 */

public class HtmlPortlet extends GenericPortlet {

	/**
	 * Name of HTML page to serve. 
	 */
	public static final String PARAM_PAGE = "Page";
	public static final String PARAM_LANGUAGES = "Languages";
		
	private String page;
	private String languages;
	
	public void init(PortletConfig config) throws PortletException {
		super.init(config);		
		this.page = "/" + config.getInitParameter(PARAM_PAGE);		
		this.languages = config.getInitParameter(PARAM_LANGUAGES);
		if (this.languages == null) this.languages = "en,es";
	}

	/**
	 * Return the page specified in init parameter PARAM_PAGE.
	 * 
	 * @throws PortletException
	 * @throws IOException
	 */
	public void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
		PortletContext context = getPortletContext();
		String language = request.getLocale().getLanguage();		
		if (languages.indexOf(language) < 0) {			
			language = language.equals("ca")?"es":"en";
		}				
		String url = page + "_" + language + ".html";
		PortletRequestDispatcher rd = context.getRequestDispatcher(url);						
		rd.include(request, response);		
	}
	
}
