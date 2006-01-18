
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
 * @version Wed Jan 18 10:17:26 CET 2006
 */
public class CalculatedCollectionPG {
    Properties properties = new Properties();


private String getException() {
	return ejb?"EJBException":"RuntimeException";
}

private IMetaModel metaModel=null;
public void setMetaModel(IMetaModel metaModel) throws XavaException {
	this.metaModel = metaModel;
}

private MetaCollection collection=null;
public void setCollection(MetaCollection col) {
	this.collection = col;
}

private boolean ejb;
public void setEjb(boolean ejb) {
	this.ejb = ejb;
}

public static void generateEJB(XPathContext context, ProgramWriter out, MetaCollection col) throws XavaException {
	CalculatedCollectionPG pg = new CalculatedCollectionPG();
	pg.setCollection(col);
	pg.setEjb(true);
	pg.setMetaModel(col.getMetaModel());
	pg.generate(context, out);
}

public static void generate(XPathContext context, ProgramWriter out, MetaCollection col) throws XavaException {
	CalculatedCollectionPG pg = new CalculatedCollectionPG();
	pg.setCollection(col);
	pg.setMetaModel(col.getMetaModel());
	pg.generate(context, out);
}



    /**
     * This method generates the output given a context and output stream
     */
    public boolean generate(XPathContext context, ProgramWriter out) {
        try {    
    String collectionName = Strings.firstUpper(collection.getName());
    
    if (ejb) { 
    out.print(" \n\t/**\n\t * @ejb:interface-method\n\t */");
    } 
    out.print(" \n\tpublic java.util.Collection get");
    out.print(collectionName);
    out.print("() {\t\t\n\t\ttry {");
    
    			MetaCalculator calculator = collection.getMetaCalculator();
    			String calculatorClass = calculator.getClassName();
    			
    out.print(" \t\t\n\t\t\t");
    out.print(calculatorClass);
    out.print(" ");
    out.print(collection.getName());
    out.print("Calculator= (");
    out.print(calculatorClass);
    out.print(")\n\t\t\t\tgetMetaModel().getMetaCollection(\"");
    out.print(collection.getName());
    out.print("\").getMetaCalculator().createCalculator();");
    	
    			Iterator itSets = calculator.getMetaSetsWithoutValue().iterator();
    			while (itSets.hasNext()) {
    				MetaSet set = (MetaSet) itSets.next();
    				String propertyNameInCalculator = Strings.firstUpper(set.getPropertyName());
    				String propertyNameFrom = set.getPropertyNameFrom();
    				MetaProperty p = metaModel.getMetaProperty(propertyNameFrom);				
    				if (propertyNameFrom.indexOf('.') >= 0) {
    					if (p.isKey() || p.getMetaModel() instanceof MetaAggregate) {
    						propertyNameFrom = Strings.firstUpper(Strings.change(propertyNameFrom, ".", "_"));
    					}
    					else {
    						StringTokenizer st = new StringTokenizer(propertyNameFrom, ".");
    						String ref = st.nextToken();
    						String pro = st.nextToken();
    						propertyNameFrom = Strings.firstUpper(ref) + "().get" + Strings.firstUpper(pro);
    					}
    				}
    				else {
    					propertyNameFrom = Strings.firstUpper(propertyNameFrom);
    				}
    				String getPropertyFrom = "boolean".equals(p.getTypeName())?"is":"get";
    				String value = set.getValue();
    				if (set.hasValue()) {
    			
    out.print(" \n\t\t\t");
    out.print(collection.getName());
    out.print("Calculator.set");
    out.print(propertyNameInCalculator);
    out.print("(\"");
    out.print(value);
    out.print("\");");
    
    				} else {	
    			
    out.print("  \t\n\t\t\t");
    out.print(collection.getName());
    out.print("Calculator.set");
    out.print(propertyNameInCalculator);
    out.print("(");
    out.print(getPropertyFrom);
    out.print(propertyNameFrom);
    out.print("());");
    	}} // else/sets 	 
    			if (IEntityCalculator.class.isAssignableFrom(Class.forName(calculatorClass))) { 
    			
    out.print(" \n\t\t\t\t");
    out.print(collection.getName());
    out.print("Calculator.setEntity(this);");
    } 
    			if (IJDBCCalculator.class.isAssignableFrom(Class.forName(calculatorClass))) { 
    			
    out.print(" \n\t\t\t\t");
    out.print(collection.getName());
    out.print("Calculator.setConnectionProvider(getPortableContext());");
    			
    			}  
    			String calculateValueSentence = collection.getName() + "Calculator.calculate()";		
    			
    out.print(" \n\t\t\treturn ");
    out.print(Generators.generateCast("java.util.Collection", calculateValueSentence));
    out.print(";\n\t\t}\n\t\tcatch (Exception ex) {\n\t\t\tex.printStackTrace();\n\t\t\tthrow new ");
    out.print(getException());
    out.print("(XavaResources.getString(\"generator.calculate_value_error\", \"");
    out.print(collection.getName());
    out.print("\", \"");
    out.print(metaModel.getName());
    out.print("\", ex.getLocalizedMessage()));\n\t\t}\n\t}");
    
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
            CalculatedCollectionPG pg = new CalculatedCollectionPG();
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
        { "Wed Jan 18 10:17:26 CET 2006", // date this file was generated
             "/home/javi/workspace/OpenXava/generator/calculatedCollection.xml", // input file
             "/home/javi/workspace/OpenXava/generator/CalculatedCollectionPG.java" }, // output file
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