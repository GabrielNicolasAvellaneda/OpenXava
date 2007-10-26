
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
 * @version Thu Oct 25 17:13:01 CEST 2007
 */
public class PropertyPG {
    Properties properties = new Properties();


private String getException() {
	return ejb?"EJBException":"RuntimeException";
}

private MetaProperty property=null;
public void setProperty(MetaProperty property) {
	this.property = property;
}

private boolean ejb=false;
public void setEjb(boolean ejb) {
	this.ejb = ejb;
}

private MetaModel metaModel=null;
private ModelMapping modelMapping=null;
public void setMetaModel(MetaModel metaModel) throws XavaException {
	this.metaModel = metaModel;
	if (metaModel instanceof MetaAggregateForReference) {
		this.modelMapping = metaModel.getMetaComponent().getEntityMapping();
	}
	else {
		this.modelMapping = metaModel.getMapping();
	}	
}

private boolean attributeIsPublic = false;
private String attributeAccessLevel = "private";
public void setAttributePublic(boolean attributeIsPublic) {
	this.attributeIsPublic = attributeIsPublic;
	this.attributeAccessLevel = attributeIsPublic?"public":"private";
}

private boolean isPublic = true;
private String propertyAccessLevel = "public";
public void setPublic(boolean isPublic) {
	this.isPublic = isPublic;
	this.propertyAccessLevel = isPublic?"public":"private";
}
public static void generate(XPathContext context, ProgramWriter out, MetaProperty property) throws XavaException {
	generate(context, out, property, true, false);
}

public static void generateEJB(XPathContext context, ProgramWriter out, MetaProperty property) throws XavaException {
	generate(context, out, property, true, true);
}

public static void generatePrivate(XPathContext context, ProgramWriter out, MetaProperty property, boolean ejb) throws XavaException {
	generate(context, out, property, false, ejb);
}

private static void generate(XPathContext context, ProgramWriter out, MetaProperty property, boolean isPublic, boolean isEjb) throws XavaException {
	PropertyPG pg = new PropertyPG();
	pg.setProperty(property);
	pg.setMetaModel(property.getMetaModel());
	pg.setPublic(isPublic);
	pg.setEjb(isEjb);
	pg.generate(context, out);
}




    /**
     * This method generates the output given a context and output stream
     */
    public boolean generate(XPathContext context, ProgramWriter out) {
        try {    
    	if (GeneratorFactory.has(property, ejb)) {
    		IPropertyCodeGenerator generator = GeneratorFactory.create(property, ejb);
    		String propertyCode = generator.generate();
    
    out.print(" \n\t");
    out.print(propertyCode);
    		
    		return true;
    	}
    	String propertyName = Strings.firstUpper(property.getName());	
    	String type = property.getTypeName();	
    	String get = type.equals("boolean")?"is":"get";	
    	String tagValueObject = ejb && isPublic?"@ejb.value-object match=\"persistentCalculatedAndAggregate\"":"";
    	String tagInterfaceMethod = ejb && isPublic?"@ejb:interface-method":"";
    	if (property.isCalculated()) {
    		// Calculated ones
    	
    out.print(" \t\n\t/**\n\t * ");
    out.print(tagValueObject);
    out.print("\n\t * ");
    out.print(tagInterfaceMethod);
    out.print("\n\t */\n\tpublic ");
    out.print(type);
    out.print(" ");
    out.print(get);
    out.print(propertyName);
    out.print("() {");
    
    		if (ejb) {
    
    out.print(" \n\t\tboolean cmtActivated = false;\n\t\tif (!org.openxava.hibernate.XHibernate.isCmt()) {\n\t\t\torg.openxava.hibernate.XHibernate.setCmt(true);\n\t\t\tcmtActivated = true;\n\t\t}");
    	
    		} 
    			MetaCalculator calculator = property.getMetaCalculator();
    			String calculatorClass = calculator.getClassName();
    			String qualifiedPropertyName = Strings.change(property.getName(), "_", "."); // for aggregate member case
    			
    out.print(" \t\t\n\t\ttry {\t\t\t\n\t\t\t");
    out.print(calculatorClass);
    out.print(" ");
    out.print(property.getName());
    out.print("Calculator= (");
    out.print(calculatorClass);
    out.print(")\n\t\t\t\tgetMetaModel().getMetaProperty(\"");
    out.print(qualifiedPropertyName);
    out.print("\").getMetaCalculator().createCalculator();");
    	
    			Iterator itSets = calculator.getMetaSetsWithoutValue().iterator();
    			while (itSets.hasNext()) {
    				MetaSet set = (MetaSet) itSets.next();
    				String propertyNameInCalculator = Strings.firstUpper(set.getPropertyName());
    				String propertyNameFrom = set.getPropertyNameFrom();
    				MetaProperty p = null;
    				try {
    					p = metaModel.getMetaProperty(propertyNameFrom);
    				}
    				catch (org.openxava.util.ElementNotFoundException ex) {
    					// Trying if it's referencing to its parent
    					String parentPrefix = Strings.firstLower(metaModel.getContainerModelName()) + ".";
    					if (propertyNameFrom.startsWith(parentPrefix)) {
    						String propertyInParent = propertyNameFrom.substring(parentPrefix.length());
    						p = metaModel.getMetaModelContainer().getMetaProperty(propertyInParent);
    					}
    					else throw ex;
    				}
    				if (propertyNameFrom.indexOf('.') >= 0) {
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
    					propertyNameFrom = Strings.firstUpper(propertyNameFrom);
    				}
    				int idx = propertyName.indexOf('_');
    				if (idx >= 0) {
    					String aggregateName = propertyName.substring(0, idx + 1);
    					propertyNameFrom = aggregateName + Strings.firstLower(propertyNameFrom);
    				}
    				String getPropertyFrom = "boolean".equals(p.getTypeName())?"is":"get";
    				String value = set.getValue();
    				if (set.hasValue()) {
    			
    out.print(" \n\t\t\t");
    out.print(property.getName());
    out.print("Calculator.set");
    out.print(propertyNameInCalculator);
    out.print("(\"");
    out.print(value);
    out.print("\");");
    
    				} else {	
    			
    out.print("  \t\n\t\t\t\n\t\t\t");
    out.print(property.getName());
    out.print("Calculator.set");
    out.print(propertyNameInCalculator);
    out.print("(");
    out.print(getPropertyFrom);
    out.print(propertyNameFrom);
    out.print("());");
    	}} // else/sets 	 
    			if (IModelCalculator.class.isAssignableFrom(Class.forName(calculatorClass))) { 
    			
    out.print(" \n\t\t\t\t");
    out.print(property.getName());
    out.print("Calculator.setModel(this);");
    } 
    			if (IEntityCalculator.class.isAssignableFrom(Class.forName(calculatorClass))) { 
    			
    out.print(" \n\t\t\t\t");
    out.print(property.getName());
    out.print("Calculator.setEntity(this);");
    } 
    			if (IJDBCCalculator.class.isAssignableFrom(Class.forName(calculatorClass))) {
    				if (ejb) { 
    			
    out.print(" \n\t\t\t\t");
    out.print(property.getName());
    out.print("Calculator.setConnectionProvider(getPortableContext());");
    			
    				}
    				else { // not ejb
    			
    out.print(" \n\t\t\t\t");
    out.print(property.getName());
    out.print("Calculator.setConnectionProvider(DataSourceConnectionProvider.getByComponent(\"");
    out.print(metaModel.getMetaComponent().getName());
    out.print("\"));");
    			
    				}
    			}  
    			String calculateValueSentence = property.getName() + "Calculator.calculate()";		
    			
    out.print(" \n\t\t\treturn ");
    out.print(Generators.generateCast(property.getTypeName(), calculateValueSentence));
    out.print(";\n\t\t}\n\t\tcatch (NullPointerException ex) {\n\t\t\t// Usually for multilevel property access with null references");
    if (boolean.class.equals(property.getType())) { 
    out.print("\n\t\t\treturn false;");
    } else if (property.getType().isPrimitive()) { 
    out.print(" \n\t\t\treturn 0;");
    } else { 
    out.print(" \n\t\t\treturn null;");
    } 
    out.print(" \t\t\t\n\t\t}\n\t\tcatch (Exception ex) {\n\t\t\tex.printStackTrace();\n\t\t\tthrow new ");
    out.print(getException());
    out.print("(XavaResources.getString(\"generator.calculate_value_error\", \"");
    out.print(propertyName);
    out.print("\", \"");
    out.print(metaModel.getName());
    out.print("\", ex.getLocalizedMessage()));\n\t\t}");
    
    		if (ejb) {
    
    out.print(" \n\t\tfinally {\n\t\t\tif (cmtActivated) {\n\t\t\t\torg.openxava.hibernate.XHibernate.setCmt(false);\n\t\t\t}\n\t\t}");
    	
    		} 
    
    out.print(" \t\t\n\t}\n\tpublic void set");
    out.print(propertyName);
    out.print("(");
    out.print(type);
    out.print(" new");
    out.print(propertyName);
    out.print(") {\n\t\t// for it is in value object\n\t\t// para que aparezca en los value objects\n\t}");
    
    	}
    	else {
    		// Not calculateds
    		String column = modelMapping.getColumn(property.getName());			
    		PropertyMapping propertyMapping = modelMapping.getPropertyMapping(property.getName());
    		
    		String ejbPkField = "";
    		String ejbSetInterfaceMethod = ejb && isPublic?"@ejb:interface-method":"";
    		if (property.isKey()) {
    			ejbPkField = "@ejb:pk-field";
    			ejbSetInterfaceMethod = "";			
    		}
    		if (propertyMapping.hasConverter() || propertyMapping.hasMultipleConverter()) { // Common for multiple and normal converter
    			String converterClass = propertyMapping.hasConverter()?"org.openxava.converters.IConverter":propertyMapping.getMultipleConverterClassName();		
    			String multiple = propertyMapping.hasMultipleConverter()?"Multiple":"";
    			
    out.print(" \n\tprivate static ");
    out.print(converterClass);
    out.print(" ");
    out.print(property.getName());
    out.print("Converter;\n\tprivate ");
    out.print(converterClass);
    out.print(" get");
    out.print(propertyName);
    out.print("Converter() {\n\t\tif (");
    out.print(property.getName());
    out.print("Converter == null) {\n\t\t\ttry {\n\t\t\t\t");
    out.print(property.getName());
    out.print("Converter = (");
    out.print(converterClass);
    out.print(") \n\t\t\t\t\tgetMetaModel().getMapping().get");
    out.print(multiple);
    out.print("Converter(\"");
    out.print(property.getName());
    out.print("\");\n\t\t\t}\n\t\t\tcatch (Exception ex) {\n\t\t\t\tex.printStackTrace();\n\t\t\t\tthrow new ");
    out.print(getException());
    out.print("(XavaResources.getString(\"generator.create_converter_error\", \"");
    out.print(property.getName());
    out.print("\"));\n\t\t\t}\n\t\t\t\n\t\t}\t\n\t\treturn ");
    out.print(property.getName());
    out.print("Converter;\n\t}");
    			
    		}
    		if (propertyMapping.hasMultipleConverter()) { // With converter for multiple fields
    			Iterator itCmpFields = propertyMapping.getCmpFields().iterator();
    			while (itCmpFields.hasNext()) {
    				CmpField cmpField = (CmpField) itCmpFields.next();
    				if (ejb) {
    		
    out.print(" \n\t/**\t \n\t * @ejb:persistent-field\n\t * @jboss:column-name \"");
    out.print(cmpField.getColumn());
    out.print("\"\n\t */\t\t\n\tpublic abstract ");
    out.print(cmpField.getCmpTypeName());
    out.print(" get");
    out.print(propertyName);
    out.print("_");
    out.print(cmpField.getConverterPropertyName());
    out.print("();\n\tpublic abstract void set");
    out.print(propertyName);
    out.print("_");
    out.print(cmpField.getConverterPropertyName());
    out.print("(");
    out.print(cmpField.getCmpTypeName());
    out.print(" newValue);");
    
    				}
    				else { // not ejb
    		
    out.print(" \n\tprivate ");
    out.print(cmpField.getCmpTypeName());
    out.print(" ");
    out.print(property.getName());
    out.print("_");
    out.print(cmpField.getConverterPropertyName());
    out.print(";\n\tprivate ");
    out.print(cmpField.getCmpTypeName());
    out.print(" get");
    out.print(propertyName);
    out.print("_");
    out.print(cmpField.getConverterPropertyName());
    out.print("() {\n\t\treturn ");
    out.print(property.getName());
    out.print("_");
    out.print(cmpField.getConverterPropertyName());
    out.print(";\n\t}\n\tprivate void set");
    out.print(propertyName);
    out.print("_");
    out.print(cmpField.getConverterPropertyName());
    out.print("(");
    out.print(cmpField.getCmpTypeName());
    out.print(" newValue) {\n\t\tthis.");
    out.print(property.getName());
    out.print("_");
    out.print(cmpField.getConverterPropertyName());
    out.print(" = newValue;\n\t}");
    	
    				}
    			}
    		
    out.print(" \n\t/**\n\t * ");
    out.print(tagValueObject);
    out.print("\n\t * ");
    out.print(tagInterfaceMethod);
    out.print("\n\t */\n\tpublic ");
    out.print(type);
    out.print(" ");
    out.print(get);
    out.print(propertyName);
    out.print("() {\n\t\ttry {");
    
    			itCmpFields = propertyMapping.getCmpFields().iterator();
    			while (itCmpFields.hasNext()) {
    				CmpField cmpField = (CmpField) itCmpFields.next();				
    		
    out.print(" \n\t\t\tget");
    out.print(propertyName);
    out.print("Converter().set");
    out.print(Strings.firstUpper(cmpField.getConverterPropertyName()));
    out.print("(get");
    out.print(propertyName);
    out.print("_");
    out.print(cmpField.getConverterPropertyName());
    out.print("());");
    		
    			}
    			String toJavaSentence = "get" + propertyName + "Converter().toJava()";
    		
    out.print(" \n\t\t\treturn ");
    out.print(Generators.generateCast(type, toJavaSentence));
    out.print(";\n\t\t}\n\t\tcatch (org.openxava.converters.ConversionException ex) {\n\t\t\tex.printStackTrace();\n\t\t\tthrow new ");
    out.print(getException());
    out.print("(XavaResources.getString(\"generator.conversion_error\", \"");
    out.print(propertyName);
    out.print("\", \"");
    out.print(metaModel.getName());
    out.print("\", \"");
    out.print(type);
    out.print("\"));\n\t\t}\n\t}\n\n\t/**\n\t * ");
    out.print(ejbSetInterfaceMethod);
    out.print("\n\t */\n\tpublic void set");
    out.print(propertyName);
    out.print("(");
    out.print(type);
    out.print(" new");
    out.print(propertyName);
    out.print(") {\n\t\ttry {");
    if (ejb) { 
    out.print("\n\t\t\tthis.modified = true;");
    } 
    
    			String argv = Generators.generatePrimitiveWrapper(type, "new" + propertyName);
    			
    out.print(" \n\t\t\tget");
    out.print(propertyName);
    out.print("Converter().toDB(");
    out.print(argv);
    out.print(");");
    
    			itCmpFields = propertyMapping.getCmpFields().iterator();
    			while (itCmpFields.hasNext()) {
    				CmpField cmpField = (CmpField) itCmpFields.next();				
    		
    out.print(" \n\t\t\tset");
    out.print(propertyName);
    out.print("_");
    out.print(cmpField.getConverterPropertyName());
    out.print("(get");
    out.print(propertyName);
    out.print("Converter().get");
    out.print(Strings.firstUpper(cmpField.getConverterPropertyName()));
    out.print("());");
    		
    			}
    		
    out.print(" \t\t\t\n\t\t}\n\t\tcatch (org.openxava.converters.ConversionException ex) {\n\t\t\tex.printStackTrace();\n\t\t\tthrow new ");
    out.print(getException());
    out.print("(XavaResources.getString(\"generator.conversion_db_error\", \"");
    out.print(propertyName);
    out.print("\", \"");
    out.print(metaModel.getName());
    out.print("\"));\n\t\t}\t\t\n\t}");
    
    		}
    		else if (propertyMapping.hasConverter()) { // Wint single field converter
    			String cmpType = propertyMapping.getCmpTypeName();
    			String getSentence = "get" + propertyName + "Converter().toJava(get_"+propertyName+"())";			
    			String setSentence = "get" + propertyName + "Converter().toDB(" + Generators.generatePrimitiveWrapper(type, "new" + propertyName) + ")";
    			if (!ejb) {
    
    out.print(" \n\t");
    out.print(attributeAccessLevel);
    out.print(" ");
    out.print(cmpType);
    out.print(" ");
    out.print(property.getName());
    out.print(";\n\tprivate ");
    out.print(cmpType);
    out.print(" get_");
    out.print(propertyName);
    out.print("() {\n\t\treturn ");
    out.print(property.getName());
    out.print(";\n\t}\n\tprivate void set_");
    out.print(propertyName);
    out.print("(");
    out.print(cmpType);
    out.print(" new");
    out.print(propertyName);
    out.print(") {\n\t\tthis.");
    out.print(property.getName());
    out.print(" = new");
    out.print(propertyName);
    out.print(";\n\t}");
    			
    			}
    			else {
    
    out.print(" \n\t/**\t \n\t * @ejb:persistent-field\n\t * ");
    out.print(ejbPkField);
    out.print("\n\t * @jboss:column-name \"");
    out.print(column);
    out.print("\"\n\t */\n\tpublic abstract ");
    out.print(cmpType);
    out.print(" get_");
    out.print(propertyName);
    out.print("();\n\tpublic abstract void set_");
    out.print(propertyName);
    out.print("(");
    out.print(cmpType);
    out.print(" new");
    out.print(propertyName);
    out.print(");");
    
    			}
    
    out.print(" \t\n\t\n\t/**\n\t * ");
    out.print(tagValueObject);
    out.print("\n\t * ");
    out.print(tagInterfaceMethod);
    out.print("\n\t */\n\t");
    out.print(propertyAccessLevel);
    out.print(" ");
    out.print(type);
    out.print(" ");
    out.print(get);
    out.print(propertyName);
    out.print("() {\n\t\ttry {\n\t\t\treturn ");
    out.print(Generators.generateCast(type, getSentence));
    out.print(";\n\t\t}\n\t\tcatch (org.openxava.converters.ConversionException ex) {\n\t\t\tex.printStackTrace();\n\t\t\tthrow new ");
    out.print(getException());
    out.print("(XavaResources.getString(\"generator.conversion_error\", \"");
    out.print(propertyName);
    out.print("\", \"");
    out.print(metaModel.getName());
    out.print("\", \"");
    out.print(type);
    out.print("\"));\n\t\t}\n\t}\n\t\n\t/**\n\t * ");
    out.print(ejbSetInterfaceMethod);
    out.print("\n\t */\n\t");
    out.print(propertyAccessLevel);
    out.print(" void set");
    out.print(propertyName);
    out.print("(");
    out.print(type);
    out.print(" new");
    out.print(propertyName);
    out.print(") {\n\t\ttry {");
    if (ejb) { 
    out.print(" \n\t\t\tthis.modified = true;");
    } 
    out.print(" \n\t\t\tset_");
    out.print(propertyName);
    out.print("(");
    out.print(Generators.generateCast(cmpType, setSentence));
    out.print(");\n\t\t}\n\t\tcatch (org.openxava.converters.ConversionException ex) {\n\t\t\tex.printStackTrace();\n\t\t\tthrow new ");
    out.print(getException());
    out.print("(XavaResources.getString(\"generator.conversion_error\", \"");
    out.print(propertyName);
    out.print("\", \"");
    out.print(metaModel.getName());
    out.print("\", \"");
    out.print(type);
    out.print("\"));\n\t\t}\t\t\n\t}");
    		
    		} else { // normal
    			if (!ejb) {
    			
    out.print(" \n\t");
    out.print(attributeAccessLevel);
    out.print(" ");
    out.print(type);
    out.print(" ");
    out.print(property.getName());
    out.print(";\n\t");
    out.print(propertyAccessLevel);
    out.print(" ");
    out.print(type);
    out.print(" ");
    out.print(get);
    out.print(propertyName);
    out.print("() {\n\t\treturn ");
    out.print(property.getName());
    out.print(";\n\t}\n\t");
    out.print(propertyAccessLevel);
    out.print(" void set");
    out.print(propertyName);
    out.print("(");
    out.print(type);
    out.print(" new");
    out.print(propertyName);
    out.print(") {\n\t\tthis.");
    out.print(property.getName());
    out.print(" = new");
    out.print(propertyName);
    out.print(";\n\t}");
    			
    			}
    			else {
    
    out.print(" \n\t/**\n\t * ");
    out.print(tagInterfaceMethod);
    out.print("\n\t * @ejb:persistent-field\n\t * ");
    out.print(ejbPkField);
    out.print("\n\t * ");
    out.print(tagValueObject);
    out.print("\n\t *\n\t * @jboss:column-name \"");
    out.print(column);
    out.print("\"\n\t */\n\tpublic abstract ");
    out.print(type);
    out.print(" ");
    out.print(get);
    out.print(propertyName);
    out.print("();\n\t/**\n\t  * ");
    out.print(ejbSetInterfaceMethod);
    out.print("\n\t  */\n\tpublic abstract void set");
    out.print(propertyName);
    out.print("(");
    out.print(type);
    out.print(" new");
    out.print(propertyName);
    out.print(");");
    }}} //else/else/else 
    
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
            PropertyPG pg = new PropertyPG();
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
        { "Thu Oct 25 17:13:02 CEST 2007", // date this file was generated
             "../OpenXava/generator/property.xml", // input file
             "../OpenXava/generator/PropertyPG.java" }, // output file
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