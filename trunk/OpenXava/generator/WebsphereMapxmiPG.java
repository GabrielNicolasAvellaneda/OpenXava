
/* Generated by TL2Java Version 1.2, April 9, 2001 */
import org.w3c.dom.*;
import java.io.*;
import java.util.*;
import java.util.*;
import org.openxava.util.Strings;
import org.openxava.component.MetaComponent;
import org.openxava.model.meta.*;
import org.openxava.mapping.*;

/**
 * Program Generator created by TL2Java
 * @version Wed Mar 09 19:55:27 CET 2005
 */
public class WebsphereMapxmiPG {
    Properties properties = new Properties();

private static long id = System.currentTimeMillis();


    /**
     * This method generates the output given a context and output stream
     */
    public boolean generate(XPathContext context, ProgramWriter out) {
        try {    out.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    out.print(" \n\n<!-- Generated by OpenXava: ");
    out.print(new Date());
    out.print(" -->\n\n<ejbrdbmapping:EjbRdbDocumentRoot xmi:version=\"2.0\" xmlns:xmi=\"http://www.omg.org/XMI\" xmlns:RDBSchema=\"RDBSchema.xmi\" xmlns:ejb=\"ejb.xmi\" xmlns:ejbrdbmapping=\"ejbrdbmapping.xmi\" xmlns:Mapping=\"Mapping.xmi\" xmi:id=\"EjbRdbDocumentRoot_");
    out.print(id++);
    out.print("\" topToBottom=\"true\" commandStack=\"\">\n  <helper xmi:type=\"ejbrdbmapping:RdbSchemaProperies\" xmi:id=\"RdbSchemaProperies_");
    out.print(id++);
    out.print("\" primitivesDocument=\"SQL92\"/>");
    
    for (Iterator itModels=MetaModel.getAllPersistent().iterator(); itModels.hasNext();) {
    	MetaModel model = (MetaModel) itModels.next();
    	ModelMapping mapping = model.getMapping();
    	String tableId = Strings.change(mapping.getTable(), ".", "_"); 
    
    out.print("   \n  <nested xmi:type=\"ejbrdbmapping:RDBEjbMapper\" xmi:id=\"RDBEjbMapper_");
    out.print(id++);
    out.print("\">\n    <helper xmi:type=\"ejbrdbmapping:PrimaryTableStrategy\" xmi:id=\"PrimaryTableStrategy_");
    out.print(id++);
    out.print("\">\n      <table href=\"META-INF/backends/OPENXAVA/");
    out.print(tableId);
    out.print(".tblxmi#");
    out.print(tableId);
    out.print("\"/>\n    </helper>");
    
    	for (Iterator itProperties=mapping.getPropertyMappings().iterator(); itProperties.hasNext();) {
    		PropertyMapping pMapping = (PropertyMapping) itProperties.next();	
    		Collection fields = new ArrayList();
    		if (pMapping.hasMultipleConverter()) {
    			fields.addAll(pMapping.getCmpFields());
    		}
    		else {
    			fields.add(pMapping.toCmpField());
    		}
    		for (Iterator itFields=fields.iterator(); itFields.hasNext();) {
    			CmpField field = (CmpField) itFields.next();
    			String column = field.getColumn();
    			String sufixAttribute = null;
    			if (pMapping.hasMultipleConverter()) {
    				sufixAttribute = "_" + pMapping.getProperty() + "_" + field.getConverterPropertyName();
    			}
    			else if (pMapping.hasConverter()) {
    				sufixAttribute = "__" + Strings.firstUpper(pMapping.getProperty());
    			}			 
    			else {
    				sufixAttribute = "_" + pMapping.getProperty();
    			}
    			String cmpAttributeId = "CmpAttribute_" + model.getName() + sufixAttribute;
    
    out.print(" \n    <nested xmi:type=\"ejbrdbmapping:RDBEjbFieldMapper\" xmi:id=\"RDBEjbFieldMapper_");
    out.print(id++);
    out.print("\">\n      <inputs xmi:type=\"ejb:CMPAttribute\" href=\"META-INF/ejb-jar.xml#");
    out.print(cmpAttributeId);
    out.print("\"/>\n      <outputs xmi:type=\"RDBSchema:RDBColumn\" href=\"META-INF/backends/OPENXAVA/");
    out.print(tableId);
    out.print(".tblxmi#");
    out.print(column);
    out.print("\"/>\n    </nested>");
    
    		}
    	}
    
    out.print(" \n    <inputs xmi:type=\"ejb:ContainerManagedEntity\" href=\"META-INF/ejb-jar.xml#ContainerManagedEntity_");
    out.print(model.getName());
    out.print("\"/>\n    <outputs xmi:type=\"RDBSchema:RDBTable\" href=\"META-INF/backends/OPENXAVA/");
    out.print(tableId);
    out.print(".tblxmi#");
    out.print(tableId);
    out.print("\"/>\n  </nested>");
    
    }
    
    out.print("   \n  <inputs xmi:type=\"ejb:EJBJar\" href=\"META-INF/ejb-jar.xml#ejb-jar_1\"/>\n  <outputs xmi:type=\"RDBSchema:RDBDatabase\" href=\"META-INF/backends/OPENXAVA/DB.dbxmi#DB\"/>\n  <typeMapping xmi:type=\"Mapping:MappingRoot\" href=\"JavatoSQL92TypeMaps.xmi#Java_to_SQL92_TypeMaps\"/>\n</ejbrdbmapping:EjbRdbDocumentRoot>");
    
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
            WebsphereMapxmiPG pg = new WebsphereMapxmiPG();
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
        { "Wed Mar 09 19:55:28 CET 2005", // date this file was generated
             "/home/javi/workspace/OpenXava/generator/websphereMapxmi.xml", // input file
             "/home/javi/workspace/OpenXava/generator/WebsphereMapxmiPG.java" }, // output file
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