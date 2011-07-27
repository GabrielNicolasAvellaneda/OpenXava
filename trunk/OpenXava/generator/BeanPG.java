
/* Generated by TL2Java Version 1.2, April 9, 2001 */
import org.w3c.dom.*;
import java.io.*;
import java.util.*;
import org.openxava.util.*;
import org.openxava.model.meta.*;
import org.openxava.component.MetaComponent;

/**
 * Program Generator created by TL2Java
 * @version Wed Jul 27 17:47:35 CEST 2011
 */
public class BeanPG {
    Properties properties = new Properties();

    /**
     * This method generates the output given a context and output stream
     */
    public boolean generate(XPathContext context, ProgramWriter out) {
        try {    
    String packageName = properties.getProperty("arg3");
    String componentName = properties.getProperty("arg4");
    String aggregateName = properties.getProperty("arg5");
    
    MetaAggregate metaAggregate = MetaComponent.get(componentName).getMetaAggregate(aggregateName);
    String interfaces = metaAggregate.getInterfacesNames().isEmpty()?"":", " + metaAggregate.getImplements();
    
    out.print("\n\n// File generated by OpenXava: ");
    out.print(new Date());
    out.print("\n// Archivo generado por OpenXava: ");
    out.print(new Date());
    out.print("\n\n// WARNING: NO EDIT\n// OJO: NO EDITAR\n\npackage ");
    out.print(packageName);
    out.print(";\n\nimport org.openxava.component.MetaComponent;\nimport org.openxava.model.meta.MetaModel;\nimport org.openxava.util.XavaException;\n\npublic class ");
    out.print(aggregateName);
    out.print(" implements java.io.Serializable");
    out.print(interfaces);
    out.print(" {\n\n\t// Attributes/Atributos");
    
    Iterator itProperties = metaAggregate.getMetaProperties().iterator();	
    while (itProperties.hasNext()) {	
    	MetaProperty property = (MetaProperty) itProperties.next();	
    	String attributeName = property.getName();
    	String type = property.getTypeName();
    
    out.print(" \n\tprivate ");
    out.print(type);
    out.print(" ");
    out.print(attributeName);
    out.print(";");
    } //while 
    
    Iterator itReferences = metaAggregate.getMetaReferences().iterator();	
    while (itReferences.hasNext()) {	
    	MetaReference ref = (MetaReference) itReferences.next();	
    	String attributeName = ref.getName();
    	String type = ref.getMetaModelReferenced().getInterfaceName();
    
    out.print(" \n\tprivate ");
    out.print(type);
    out.print(" ");
    out.print(attributeName);
    out.print(";");
    } //while 
    out.print("\n\n\n\n\t// Properties/Propiedades");
    
    itProperties = metaAggregate.getMetaProperties().iterator();	
    while (itProperties.hasNext()) {	
    	MetaProperty property = (MetaProperty) itProperties.next();	
    	String attributeName = property.getName();
    	String propertyName = Strings.firstUpper(attributeName);
    	String type = property.getTypeName();
    
    out.print(" \n\tpublic ");
    out.print(type);
    out.print(" get");
    out.print(propertyName);
    out.print("() {\n\t\treturn ");
    out.print(attributeName);
    out.print(";\n\t}\n\tpublic void set");
    out.print(propertyName);
    out.print("(");
    out.print(type);
    out.print(" new");
    out.print(propertyName);
    out.print(") {\n\t\tthis.");
    out.print(attributeName);
    out.print(" = new");
    out.print(propertyName);
    out.print(";\n\t}");
    } //while 
    out.print("\n\n\t// References");
    
    itReferences = metaAggregate.getMetaReferences().iterator();	
    while (itReferences.hasNext()) {	
    	MetaReference ref = (MetaReference) itReferences.next();	
    	String attributeName = ref.getName();
    	String propertyName = Strings.firstUpper(attributeName);
    	String type = ref.getMetaModelReferenced().getInterfaceName();
    
    out.print(" \n\tpublic ");
    out.print(type);
    out.print(" get");
    out.print(propertyName);
    out.print("() {\n\t\treturn ");
    out.print(attributeName);
    out.print(";\n\t}\n\tpublic void set");
    out.print(propertyName);
    out.print("(");
    out.print(type);
    out.print(" new");
    out.print(propertyName);
    out.print(") {\n\t\tthis.");
    out.print(attributeName);
    out.print(" = new");
    out.print(propertyName);
    out.print(";\n\t}");
    } //while 
    out.print("\n\t\n\tprivate MetaModel metaModel;\n\tpublic MetaModel getMetaModel() throws XavaException {\n\t\tif (metaModel == null) {\n\t\t\tmetaModel = MetaComponent.get(\"");
    out.print(componentName);
    out.print("\").getMetaAggregate(\"");
    out.print(aggregateName);
    out.print("\");\n\t\t}\n\t\treturn metaModel;\n\t}\n\t\n}");
    
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
            BeanPG pg = new BeanPG();
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
        { "Wed Jul 27 17:47:36 CEST 2011", // date this file was generated
             "../OpenXava/generator/bean.xml", // input file
             "../OpenXava/generator/BeanPG.java" }, // output file
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