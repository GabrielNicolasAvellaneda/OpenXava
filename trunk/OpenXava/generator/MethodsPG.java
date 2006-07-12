
/* Generated by TL2Java Version 1.2, April 9, 2001 */
import org.w3c.dom.*;
import java.io.*;
import java.util.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;
import org.openxava.util.meta.MetaSet;
import org.openxava.calculators.*;
import org.openxava.generators.Generators;

/**
 * Program Generator created by TL2Java
 * @version Wed Jul 12 11:09:05 CEST 2006
 */
public class MethodsPG {
    Properties properties = new Properties();


private String getException() {
	return ejb?"EJBException":"RuntimeException";
}

private MetaModel metaModel=null;
public void setMetaModel(MetaModel metaModel) {
	this.metaModel = metaModel;
}

private boolean ejb=false;
public void setEjb(boolean ejb) {
	this.ejb = ejb;
}

public static void generate(XPathContext context, ProgramWriter out, MetaModel metaModel) throws XavaException {
	generate(context, out, metaModel, false);
}

public static void generateEJB(XPathContext context, ProgramWriter out, MetaModel metaModel) throws XavaException {
	generate(context, out, metaModel, true);
}

private static void generate(XPathContext context, ProgramWriter out, MetaModel metaModel, boolean isEjb) throws XavaException {
	MethodsPG pg = new MethodsPG();
	pg.setMetaModel(metaModel);
	pg.setEjb(isEjb);
	pg.generate(context, out);
}



    /**
     * This method generates the output given a context and output stream
     */
    public boolean generate(XPathContext context, ProgramWriter out) {
        try {    out.print(" \n\n\t// Methods/Metodos");
    
    Iterator itMethods = metaModel.getMetaMethods().iterator();	
    while (itMethods.hasNext()) {	
    	MetaMethod method = (MetaMethod) itMethods.next();
    	String throwSentence = method.hasExceptions()?"throws " + method.getExceptions():"";
    
    out.print(" \n\t/**\n\t * @ejb:interface-method\n\t */\n\tpublic ");
    out.print(method.getTypeName());
    out.print(" ");
    out.print(method.getName());
    out.print("(");
    out.print(method.getArguments());
    out.print(") ");
    out.print(throwSentence);
    out.print(" {");
    
    	if (ejb) {
    
    out.print(" \n\t\tboolean cmtActivated = false;\n\t\tif (!org.openxava.hibernate.XHibernate.isCmt()) {\n\t\t\torg.openxava.hibernate.XHibernate.setCmt(true);\n\t\t\tcmtActivated = true;\n\t\t}");
    	
    	} 
    	MetaCalculator calculator = method.getMetaCalculator();
    	String calculatorClass = calculator.getClassName();
    			
    out.print(" \t\t\n\t\ttry {\t\t\t\n\t\t\t");
    out.print(calculatorClass);
    out.print(" ");
    out.print(method.getName());
    out.print("Calculator = (");
    out.print(calculatorClass);
    out.print(")\n\t\t\t\tgetMetaModel().getMetaMethod(\"");
    out.print(method.getName());
    out.print("\").getMetaCalculator().createCalculator();");
    	
    	Iterator itSets = calculator.getMetaSetsWithoutValue().iterator();
    	while (itSets.hasNext()) {
    		MetaSet set = (MetaSet) itSets.next();
    		String propertyNameInCalculator = Strings.firstUpper(set.getPropertyName());
    		String propertyNameFrom = set.getPropertyNameFrom();
    		if (propertyNameFrom .indexOf('.') >= 0) {
    			MetaProperty p = metaModel.getMetaProperty(propertyNameFrom);
    			StringTokenizer st = new StringTokenizer(propertyNameFrom, ".");
    			boolean moreThan2Levels = st.countTokens() > 2;
    			StringBuffer propertyNameFromInJava = new StringBuffer();
    			boolean isEmbededKey = ejb && ((p.isKey() && !moreThan2Levels) || (p.getMetaModel() instanceof MetaAggregate));
    			while (st.hasMoreTokens()) {
    				String token = st.nextToken();
    				if (propertyNameFromInJava.length() > 0) {
    					if (isEmbededKey && !st.hasMoreTokens()) {
    						propertyNameFromInJava.append("_");
    					}
    					else {
    						if (isEmbededKey) {
    							propertyNameFromInJava.append("Remote");
    						}	
    						propertyNameFromInJava.append("().get");						
    					}						
    				}
    				if (isEmbededKey && !st.hasMoreTokens()) {
    					propertyNameFromInJava.append(token);
    				}
    				else {
    					propertyNameFromInJava.append(Strings.firstUpper(token));	
    				}
    			}			
    			propertyNameFrom = propertyNameFromInJava.toString();
    		}
    		else {
    			propertyNameFrom  = Strings.firstUpper(propertyNameFrom );
    		}
    		String value = set.getValue();
    		if (set.hasValue()) {
    			
    out.print(" \n\t\t\t");
    out.print(method.getName());
    out.print("Calculator.set");
    out.print(propertyNameInCalculator);
    out.print("(\"");
    out.print(value);
    out.print("\");");
    
    		} else {	
    			
    out.print("  \t\n\t\t\t");
    out.print(method.getName());
    out.print("Calculator.set");
    out.print(propertyNameInCalculator);
    out.print("(get");
    out.print(propertyNameFrom);
    out.print("());");
    	}} // else/poners 	 
    		if (IModelCalculator.class.isAssignableFrom(Class.forName(calculatorClass))) { 
    			
    out.print(" \n\t\t\t\t");
    out.print(method.getName());
    out.print("Calculator.setModel(this);");
    } 
    		if (IEntityCalculator.class.isAssignableFrom(Class.forName(calculatorClass))) { 
    			
    out.print(" \n\t\t\t\t");
    out.print(method.getName());
    out.print("Calculator.setEntity(this);");
    } 
    		if (IJDBCCalculator.class.isAssignableFrom(Class.forName(calculatorClass))) { 
    			String connectionProvider = ejb?"getPortableContext()":"DataSourceConnectionProvider.getByComponent(\"" + metaModel.getMetaComponent().getName() + "\")";
    			
    out.print(" \n\t\t\t\t");
    out.print(method.getName());
    out.print("Calculator.setConnectionProvider(");
    out.print(connectionProvider);
    out.print(");");
    			
    		}  
    		if (method.hasArguments()) {
    			StringTokenizer st = new StringTokenizer(method.getArguments(), ",");
    			while (st.hasMoreTokens()) {
    				StringTokenizer stArgument = new StringTokenizer(st.nextToken());
    				stArgument.nextToken();
    				String argumentName = stArgument.nextToken().trim();
    				String argumentProperty = Strings.firstUpper(argumentName);
    			
    out.print(" \t\t\n\t\t\t");
    out.print(method.getName());
    out.print("Calculator.set");
    out.print(argumentProperty);
    out.print("(");
    out.print(argumentName);
    out.print(");");
    
    			}
    		}
    		String calculateValueSentence = method.getName() + "Calculator.calculate()";
    		if ("void".equals(method.getTypeName())) {
    			
    out.print(" \n\t\t\t");
    out.print(calculateValueSentence);
    out.print(";");
    
    		} else {
    			
    out.print(" \n\t\t\treturn ");
    out.print(Generators.generateCast(method.getTypeName(), calculateValueSentence));
    out.print(";");
    
    		}
    		if (method.hasExceptions()) {
    			StringTokenizer st = new StringTokenizer(method.getExceptions(), ",");
    			while (st.hasMoreTokens()) {
    				String exception = st.nextToken().trim();
    			
    out.print(" \n\t\t}\n\t\tcatch (");
    out.print(exception);
    out.print(" ex) {\n\t\t\tthrow ex;");
    		
    				} // while exceptions						
    			} // if has exceptions
    			
    out.print(" \n\t\t}\n\t\tcatch (Exception ex) {\n\t\t\tex.printStackTrace();\n\t\t\tthrow new ");
    out.print(getException());
    out.print("(XavaResources.getString(\"method_execution_error\", \"");
    out.print(method.getName());
    out.print("\", \"");
    out.print(metaModel.getName());
    out.print("\"));\n\t\t}");
    
    	if (ejb) {
    
    out.print(" \n\t\tfinally {\n\t\t\tif (cmtActivated) {\n\t\t\t\torg.openxava.hibernate.XHibernate.setCmt(false);\n\t\t\t}\n\t\t}");
    	
    	} 
    
    out.print(" \n\t\t\n\t}");
    		
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
            MethodsPG pg = new MethodsPG();
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
        { "Wed Jul 12 11:09:05 CEST 2006", // date this file was generated
             "/home/javi/workspace/OpenXava/generator/methods.xml", // input file
             "/home/javi/workspace/OpenXava/generator/MethodsPG.java" }, // output file
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