
/* Generated by TL2Java Version 1.2, April 9, 2001 */
import org.w3c.dom.*;
import java.io.*;
import java.util.*;
import java.util.*;
import org.openxava.application.meta.*;
import org.openxava.generators.*;

/**
 * Program Generator created by TL2Java
 * @version Wed Nov 02 18:35:53 CET 2005
 */
public class Jetspeed2PsmlPG {
    Properties properties = new Properties();

    /**
     * This method generates the output given a context and output stream
     */
    public boolean generate(XPathContext context, ProgramWriter out) {
        try {    out.print("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
    out.print("\n\n<!-- Generated by OpenXava: ");
    out.print(new Date());
    out.print(" -->");
    
    String applicationName = properties.getProperty("arg3");
    String moduleName = properties.getProperty("arg4");
    MetaModule module = MetaApplications.getMetaApplication(applicationName).getMetaModule(moduleName);
    
    out.print(" \n\n<page>\n\t<defaults\n\t\tskin=\"orange\"\n\t\tlayout-decorator=\"tigris\"\n\t\tportlet-decorator=\"tigris\"\n\t/>\n\t<title>");
    out.print(module.getName());
    out.print("</title>");
    	
    Collection locales = Generators.getAvailableLocales("../" + applicationName + "/i18n");
    for (Iterator it=locales.iterator(); it.hasNext();) {
    	Locale locale = (Locale) it.next();
    
    out.print(" \n\t<metadata name=\"title\" xml:lang=\"");
    out.print(locale);
    out.print("\">");
    out.print(module.getLabel(locale));
    out.print("</metadata>");
    
    }
    
    out.print(" \n\n\t<fragment id=\"");
    out.print(applicationName);
    out.print("::");
    out.print(moduleName);
    out.print("-layout\" type=\"layout\" name=\"jetspeed-layouts::VelocityOneColumn\">\n\n\t\t<fragment id=\"");
    out.print(applicationName);
    out.print("::");
    out.print(moduleName);
    out.print("\" type=\"portlet\" name=\"");
    out.print(applicationName);
    out.print("::");
    out.print(moduleName);
    out.print("\">\n\t\t\t<property layout=\"OneColumn\" name=\"row\" value=\"0\" />\n\t\t\t<property layout=\"OneColumn\" name=\"column\" value=\"0\" />\n\t\t</fragment>\n\n\t</fragment>\n\n\t<security-constraints>\n\t\t<security-constraint>\n\t\t\t<groups>");
    out.print(applicationName);
    out.print("</groups>\n\t\t\t<permissions>view, edit</permissions>\n\t\t</security-constraint>\n\t</security-constraints>\n\n</page>");
    
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
            Jetspeed2PsmlPG pg = new Jetspeed2PsmlPG();
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
        { "Wed Nov 02 18:35:53 CET 2005", // date this file was generated
             "/home/javi/workspace/OpenXava/generator/jetspeed2psml.xml", // input file
             "/home/javi/workspace/OpenXava/generator/Jetspeed2PsmlPG.java" }, // output file
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