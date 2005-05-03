
/* Generated by TL2Java Version 1.2, April 9, 2001 */
import org.w3c.dom.*;
import java.io.*;
import java.util.*;
import org.openxava.component.MetaComponent;
import org.openxava.model.meta.*;
import org.openxava.mapping.*;
import org.openxava.util.Strings;

/**
 * Program Generator created by TL2Java
 * @version Tue May 03 16:28:56 CEST 2005
 */
public class HibernatePG {
    Properties properties = new Properties();

    /**
     * This method generates the output given a context and output stream
     */
    public boolean generate(XPathContext context, ProgramWriter out) {
        try {    out.print("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
    out.print("\n\n<!-- Generated by OpenXava: ");
    out.print(new Date());
    out.print(" -->");
    
    String packageName = properties.getProperty("arg3");
    String componentName = properties.getProperty("arg4");
    String aggregateName = properties.getProperty("arg5");
    MetaComponent component = MetaComponent.get(componentName);
    
    String name=null;
    IMetaModel metaModel=null;
    if (aggregateName == null) {
    	name=componentName;	
    	metaModel = (IMetaModel) component.getMetaEntity();
    }
    else {
    	name=aggregateName;	
    	metaModel =  (IMetaEjb)component.getMetaAggregate(aggregateName);
    }
    ModelMapping mapping = metaModel.getMapping();
    
    out.print("\n\n<!DOCTYPE hibernate-mapping SYSTEM \"http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd\">\n\n<hibernate-mapping package=\"");
    out.print(packageName);
    out.print("\">\n\n  <class \n  \tname=\"");
    out.print(name);
    out.print("\"\n  \ttable=\"");
    out.print(mapping.getTable());
    out.print("\">");
    
    Collection keyProperties = metaModel.getMetaPropertiesKey(); 
    if (keyProperties.size() == 1) {
    	MetaProperty key = (MetaProperty) keyProperties.iterator().next();
    	PropertyMapping pMapping = key.getMapping();
    	String propertyName = pMapping.hasConverter()?"_"+Strings.firstUpper(key.getName()):key.getName();	
    
    out.print(" \t\n\t\t<id name=\"");
    out.print(propertyName);
    out.print("\" column=\"");
    out.print(pMapping.getColumn());
    out.print("\">\n\t\t\t<generator class=\"assigned\"/>\n\t\t</id>");
    
    }
    else {
    
    out.print(" \n\t\t<composite-id>");
    
    	for (Iterator it = keyProperties.iterator(); it.hasNext();) {
    		MetaProperty key = (MetaProperty) it.next();
    		PropertyMapping pMapping = key.getMapping();
    		String propertyName = pMapping.hasConverter()?"_"+Strings.firstUpper(key.getName()):key.getName();			
    
    out.print(" \t\n\t\t\t<key-property name=\"");
    out.print(propertyName);
    out.print("\" column=\"");
    out.print(pMapping.getColumn());
    out.print("\"/>");
    	
    	}
    
    out.print("  \t\n\t\t</composite-id>");
    		
    }
    
    	Collection properties = metaModel.getMetaPropertiesPersistents();
    	for (Iterator it = properties.iterator(); it.hasNext();) {
    		MetaProperty prop = (MetaProperty) it.next();
    		PropertyMapping pMapping = prop.getMapping();
    		String propertyName = pMapping.hasConverter()?"_"+Strings.firstUpper(prop.getName()):prop.getName();			
    		if (!prop.isKey()) {
    
    out.print(" \t\n\t\t<property name=\"");
    out.print(propertyName);
    out.print("\" column=\"");
    out.print(pMapping.getColumn());
    out.print("\"/>");
    	
    		} 
    	}
    
    out.print("   \n\t\t\n  </class>\n\n</hibernate-mapping>");
    
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
            HibernatePG pg = new HibernatePG();
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
        { "Tue May 03 16:28:56 CEST 2005", // date this file was generated
             "/home/javi/workspace/OpenXava/generator/hibernate.xml", // input file
             "/home/javi/workspace/OpenXava/generator/HibernatePG.java" }, // output file
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