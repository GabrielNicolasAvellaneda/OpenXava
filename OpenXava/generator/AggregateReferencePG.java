
/* Generated by TL2Java Version 1.2, April 9, 2001 */
import org.w3c.dom.*;
import java.io.*;
import java.util.*;
import org.openxava.util.Strings;
import org.openxava.util.XavaException;
import org.openxava.model.meta.*;
import org.openxava.generators.*;
import org.openxava.calculators.*;
import org.openxava.util.meta.MetaSet;
import org.openxava.mapping.*;

/**
 * Program Generator created by TL2Java
 * @version Tue Feb 21 12:28:36 CET 2006
 */
public class AggregateReferencePG {
    Properties properties = new Properties();


private MetaReference reference=null;
public void setReference(MetaReference ref) {
	this.reference = ref;
}

private boolean ejb;
public void setEjb(boolean ejb) {
	this.ejb = ejb;
}

public static void generateEJB(XPathContext context, ProgramWriter out, MetaReference ref) throws XavaException {
	AggregateReferencePG pg = new AggregateReferencePG();
	pg.setReference(ref);
	pg.setEjb(true); 
	pg.generate(context, out);
}

public static void generate(XPathContext context, ProgramWriter out, MetaReference ref) throws XavaException {
	AggregateReferencePG pg = new AggregateReferencePG();
	pg.setReference(ref);
	pg.setEjb(false); 
	pg.generate(context, out);
}



    /**
     * This method generates the output given a context and output stream
     */
    public boolean generate(XPathContext context, ProgramWriter out) {
        try {     
    String referenceName = Strings.firstUpper(reference.getName());
    MetaAggregateBean referencedModel = (MetaAggregateBean) reference.getMetaModelReferenced();
    String referencedModelClass = referencedModel.getBeanClass();
    
    out.print("  \t\n\t// ");
    out.print(referenceName);
    out.print(" : Aggregate/Agregado");
    if (ejb) { 
    out.print(" \n\t/**\n\t * @ejb.value-object match=\"persistentCalculatedAndAggregate\"\n\t * @ejb:interface-method\n\t */");
    } 
    out.print(" \n\tpublic ");
    out.print(referencedModelClass);
    out.print(" get");
    out.print(referenceName);
    out.print("() {\n\t\t");
    out.print(referencedModelClass);
    out.print(" r = new ");
    out.print(referencedModelClass);
    out.print("();");
    
    for (Iterator itAggregateProperties = referencedModel.getMetaProperties().iterator(); itAggregateProperties.hasNext();) {	
    	MetaProperty property = (MetaProperty) itAggregateProperties.next();
    	String propertyName = Strings.firstUpper(property.getName());
    	String get = "boolean".equals(property.getTypeName())?"is":"get";
    		
    out.print("\t\t\n\t\tr.set");
    out.print(propertyName);
    out.print("(");
    out.print(get);
    out.print(referenceName);
    out.print("_");
    out.print(property.getName());
    out.print("());");
    
    }		
    for (Iterator itReferences = referencedModel.getMetaReferences().iterator(); itReferences.hasNext();) {	
    	MetaReference ref = (MetaReference) itReferences.next();
    	String propertyName = Strings.firstUpper(ref.getName());
    	
    out.print(" \n\t\tr.set");
    out.print(propertyName);
    out.print("(get");
    out.print(referenceName);
    out.print("_");
    out.print(ref.getName());
    out.print("());");
    } 
    out.print("\t\t\n\t\treturn r;\n\t}");
    if (ejb) { 
    out.print(" \t\n\t/**\n\t * @ejb.value-object match=\"persistentCalculatedAndAggregate\"\n\t * @ejb:interface-method\n\t */");
    } 
    out.print(" \t \n\tpublic void set");
    out.print(referenceName);
    out.print("(");
    out.print(referencedModelClass);
    out.print(" new");
    out.print(referenceName);
    out.print(") {");
    if (ejb) { 
    out.print(" \n\t\tthis.modified = true;");
    } 
    out.print(" \t\n\t\tif (new");
    out.print(referenceName);
    out.print(" == null) new");
    out.print(referenceName);
    out.print(" = new ");
    out.print(referencedModelClass);
    out.print("();");
    	
    for (Iterator itAggregateProperties = referencedModel.getMetaProperties().iterator(); itAggregateProperties.hasNext();) {	
    	MetaProperty property = (MetaProperty) itAggregateProperties.next();
    	String propertyName = Strings.firstUpper(property.getName());
    
    out.print("\t\t\n\t\tset");
    out.print(referenceName);
    out.print("_");
    out.print(property.getName());
    out.print("(new");
    out.print(referenceName);
    out.print(".get");
    out.print(propertyName);
    out.print("());");
    
    } 			
    for (Iterator itReferences = referencedModel.getMetaReferences().iterator(); itReferences.hasNext();) {	
    	MetaReference ref = (MetaReference) itReferences.next();
    	String refName = Strings.firstUpper(ref.getName());
    
    out.print("\t\t\n\t\tset");
    out.print(referenceName);
    out.print("_");
    out.print(ref.getName());
    out.print("(new");
    out.print(referenceName);
    out.print(".get");
    out.print(refName);
    out.print("());");
    } 
    out.print("\t\t\t\n\t}");
    	
    for (Iterator itAggregateProperties = referencedModel.getMetaProperties().iterator(); itAggregateProperties.hasNext();) {	
    	MetaProperty originalProperty = (MetaProperty) itAggregateProperties.next();
    	originalProperty.setReadOnly(false);
    	MetaProperty property = originalProperty.cloneMetaProperty();
    	property.setName(reference.getName() + "_" + property.getName());
    	PropertyPG.generatePrivate(context, out, property, ejb); 
    } 
    
    for (Iterator itReferences = referencedModel.getMetaReferences().iterator(); itReferences.hasNext();) {	
    	MetaReference originalReference = (MetaReference) itReferences.next();
    	MetaReference ref = originalReference.cloneMetaReference();	
    	boolean isAggregate = ref.getMetaModelReferenced() instanceof MetaAggregateBean;
    	ref.setName(reference.getName() + "_" + ref.getName());	
    	if (isAggregate) {	
    		AggregateReferencePG.generate(context, out, ref);
    	} 
    	else { // reference to entity or aggregate implemented as EJB
    	    if (ejb) {
    			EntityReferenceEJBPG.generate(context, out, ref);
    		}	
    		else {
    			EntityReferencePG.generate(context, out, ref);
    		}
    	}		
    } 
    
    
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
            AggregateReferencePG pg = new AggregateReferencePG();
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
        { "Tue Feb 21 12:28:36 CET 2006", // date this file was generated
             "/home/javi/workspace/OpenXava/generator/aggregateReference.xml", // input file
             "/home/javi/workspace/OpenXava/generator/AggregateReferencePG.java" }, // output file
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