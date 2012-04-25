
/* Generated by TL2Java Version 1.2, April 9, 2001 */
import org.w3c.dom.*;
import java.io.*;
import java.util.*;
import java.util.*;
import org.openxava.application.meta.*;
import org.openxava.generators.Generators;
import org.openxava.util.*;

/**
 * Program Generator created by TL2Java
 * @version Tue Apr 24 10:19:09 CEST 2012
 */
public class PortletXmlPG {
    Properties properties = new Properties();

    /**
     * This method generates the output given a context and output stream
     */
    public boolean generate(XPathContext context, ProgramWriter out) {
        try {    out.print("<?xml version=\"1.0\" encoding=\"");
    out.print(properties.getProperty("arg4"));
    out.print("\"?>");
    out.print("\n\n<!-- Generated by OpenXava: ");
    out.print(new Date());
    out.print(" -->");
    
    String applicationName = properties.getProperty("arg3");
    MetaApplication application = MetaApplications.getMetaApplication(applicationName);
    String encoding = properties.getProperty("arg4");
    
    Collection locales = Generators.getAvailableLocales("../" + applicationName + "/i18n");
    
    out.print(" \n\n<portlet-app id=\"");
    out.print(applicationName);
    out.print("\"\n\txmlns=\"http://java.sun.com/xml/ns/portlet/portlet-app_1_0.xsd\" \n\tversion=\"1.0\" \n\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n\txsi:schemaLocation=\"http://java.sun.com/xml/ns/portlet/portlet-app_1_0.xsd http://java.sun.com/xml/ns/portlet/portlet-app_1_0.xsd\"\n>");
    
    for (Iterator it=application.getMetaModules().iterator(); it.hasNext(); ) {
    	MetaModule module = (MetaModule) it.next();
    
    out.print(" \n\t<portlet id=\"");
    out.print(Strings.toCharSet(module.getName(), encoding));
    out.print("\">\n\t\t<description>");
    out.print(Strings.toCharSet(application.getLabel(), encoding));
    out.print(" - ");
    out.print(Strings.toCharSet(module.getDescription(), encoding));
    out.print("</description>");
    for (Iterator itLocales=locales.iterator(); itLocales.hasNext(); ) { 
        	Locale locale = (Locale) itLocales.next();
        
    // Avoid to generate "en" locale description because the generated above (default) is English as portlet-app_1_0.xsd specifies. 
    // JetSpeed 2.2.1 throws a deployment error if this description is generated twice with the same locale 
    if (!locale.getLanguage().equals("en") ) { 
    out.print("\n\t\t<description xml:lang=\"");
    out.print(locale);
    out.print("\">");
    out.print(Strings.toCharSet(application.getLabel(locale), encoding));
    out.print(" - ");
    out.print(Strings.toCharSet(module.getDescription(locale), encoding));
    out.print("</description>");
    } 
    } 
    out.print("       \n\t\t<portlet-name>");
    out.print(Strings.toCharSet(module.getName(), encoding));
    out.print("</portlet-name>\n\t\t<display-name>");
    out.print(Strings.toCharSet(application.getLabel(), encoding));
    out.print(" - ");
    out.print(Strings.toCharSet(module.getLabel(), encoding));
    out.print("</display-name>");
    for (Iterator itLocales=locales.iterator(); itLocales.hasNext(); ) { 
        	Locale locale = (Locale) itLocales.next();
        
    // Avoid to generate "en" locale description because the generated above (default) is English as portlet-app_1_0.xsd specifies. 
    // JetSpeed 2.2.1 throws a deployment error if this display-name is generated twice with the same locale 
    if (!locale.getLanguage().equals("en") ) { 
    out.print(" \n\t\t<display-name xml:lang=\"");
    out.print(locale);
    out.print("\">");
    out.print(Strings.toCharSet(application.getLabel(locale), encoding));
    out.print(" - ");
    out.print(Strings.toCharSet(module.getLabel(locale), encoding));
    out.print("</display-name>");
    } 
    } 
    if (module.isDoc()) { 
    out.print(" \n\t\t<portlet-class>org.openxava.web.portlets.HtmlPortlet</portlet-class>\n\t\t<init-param>\n\t\t\t<name>Page</name>\n\t\t\t<value>");
    out.print(module.getDocURL());
    out.print("</value>\n\t\t</init-param>\n\t\t<init-param>\n\t\t\t<name>Languages</name>\n\t\t\t<value>");
    out.print(module.getDocLanguages());
    out.print("</value>\n\t\t</init-param>");
    } else { 
    out.print(" \n\t\t<portlet-class>org.openxava.web.portlets.XavaPortlet</portlet-class>\n\t\t<init-param>\n\t\t\t<name>Application</name>\n\t\t\t<value>");
    out.print(Strings.toCharSet(applicationName, encoding));
    out.print("</value>\n\t\t</init-param>\n\t\t<init-param>\n\t\t\t<name>Module</name>\n\t\t\t<value>");
    out.print(Strings.toCharSet(module.getName(), encoding));
    out.print("</value>\n\t\t</init-param>");
    } 
    out.print(" \n\t\t<expiration-cache>0</expiration-cache>\n\t\t<supports>\n\t\t\t<mime-type>text/html</mime-type>\n\t\t\t<portlet-mode>VIEW</portlet-mode>\n\t\t</supports>");
    for (Iterator itLocales=locales.iterator(); itLocales.hasNext(); ) { 
    out.print("\n\t\t<supported-locale>");
    out.print(itLocales.next());
    out.print("</supported-locale>");
    } 
    out.print(" \n    \t<resource-bundle>portlets/");
    out.print(Strings.toCharSet(module.getName(), encoding));
    out.print("</resource-bundle>\n\t</portlet>");
    
    }
    
    out.print("   \n\n\t<user-attribute>\n\t\t<description>User eMail</description>\n\t\t<name>user.home-info.online.email</name>\n\t</user-attribute>\n\t<user-attribute>\n\t\t<description>User Given Name</description>\n\t\t<name>user.name.given</name>\n\t</user-attribute>\n\t<user-attribute>\n\t\t<description>User Family Name</description>\n\t\t<name>user.name.family</name>\n\t</user-attribute>\n\t\t\n</portlet-app>");
    
        } catch (Exception e) {
            System.out.println("Exception: "+e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    /**
     * A program generator is typically (but not always) invoked
     * with a command line with arguments for the XML input file
     * and output file.
     */    
    public static void main(String[] args) {
        try {
            ProgramWriter out = args.length>=2
                ?new ProgramWriter(new FileOutputStream(args[1]))
                :new ProgramWriter(System.out);
            PortletXmlPG pg = new PortletXmlPG();
            for (int j=1; j<=args.length; ++j) {
                pg.properties.put("arg"+j, args[j-1]);
            }
            pg.generate(new XPathContext(args[0]), out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * This array provides program generator development history
     */
    public String[][] history = {
        { "Tue Apr 24 10:19:10 CEST 2012", // date this file was generated
             "../OpenXava/generator/portletxml.xml", // input file
             "../OpenXava/generator/PortletXmlPG.java" }, // output file
        {"Mon Apr 09 16:45:30 EDT 2001", "TL2Java.xml", "TL2Java.java", }, 
        {"Mon Apr 09 16:39:37 EDT 2001", "TL2Java.xml", "TL2Java.java", }, 
        {"Mon Apr 09 16:37:21 EDT 2001", "TL2Java.xml", "TL2Java.java", }, 
        {"Fri Feb 09 14:49:11 EST 2001", "TL2Java.xml", "TL2Java.java", }, 
        {"Fri Feb 09 14:30:24 EST 2001", "TL2Java.xml", "TL2Java.java", }, 
        {"Fri Feb 09 11:13:01 EST 2001", "TL2Java.xml", "TL2Java.java", }, 
        {"Fri Feb 09 10:57:04 EST 2001", "TL2Java.xml", "TL2Java.java", }, 
        {"Wed Apr 26 11:15:41 EDT 2000", "..\\input\\TL2Java.xml", "TL2Java1.java", }, 
        {"April 2000", "hand coded", "TL2Java1.java", }, 

    };
}