
/* Generated by TL2Java Version 1.2, April 9, 2001 */
import org.w3c.dom.*;
import java.io.*;
import java.util.*;
import org.openxava.util.*;
import org.openxava.model.meta.*;
import org.openxava.generators.*;
import org.openxava.calculators.*;
import org.openxava.util.meta.MetaSet;
import org.openxava.mapping.*;

/**
 * Program Generator created by TL2Java
 * @version Thu Dec 14 18:26:59 CET 2006
 */
public class EntityReferenceEJBPG {
    Properties properties = new Properties();


private MetaReference reference=null;
public void setReference(MetaReference ref) {
	this.reference = ref;
}

private boolean ejb;
public void setEjb(boolean ejb) {
	this.ejb = ejb;
}

private String getExcepcion() {
	return ejb?"EJBException":"RuntimeException";
}


public static void generate(XPathContext context, ProgramWriter out, MetaReference ref) throws XavaException {
	EntityReferenceEJBPG pg = new EntityReferenceEJBPG();
	pg.setReference(ref);
	pg.setEjb(true); // Always true
	pg.generate(context, out);
}



    /**
     * This method generates the output given a context and output stream
     */
    public boolean generate(XPathContext context, ProgramWriter out) {
        try {    
    MetaModel metaModel = reference.getMetaModel();
    ModelMapping modelMapping = null;
    if (metaModel instanceof MetaAggregateForReference) {
    	modelMapping = metaModel.getMetaComponent().getEntityMapping();
    }
    else {
    	modelMapping = metaModel.getMapping();
    }	
    String referenceName = Strings.firstUpper(reference.getName());
    ReferenceMapping referenceMapping = modelMapping.getReferenceMapping(reference.getName());
    MetaModel referencedModel = reference.getMetaModelReferenced();
    ModelMapping referencedMapping = referencedModel.getMapping();
    String referencedModelClass = referencedModel.getInterfaceName();
    String referencedJNDI = referencedModel.getMetaEJB().getJndi();
    String referencedKeyClass = referencedModel.getMetaEJB().getPrimaryKey();
    String homeClass = referencedModel.getMetaEJB().getHome();		
    String homeAttribute = Strings.firstLower(referenceName) + "Home";
    String getHome = "get" + Strings.firstUpper(homeAttribute);		
    String interfaceMethodSet = reference.isKey()?"":"@ejb:interface-method";
    String pkField = reference.isKey()?"@ejb:pk-field":"";
    
    out.print(" \n\n\t// ");
    out.print(referenceName);
    out.print(" : Entity reference/Referencia a entidad\n\t\n\t/**\n\t * @ejb:interface-method\n\t */\n\tpublic ");
    out.print(referencedModelClass);
    out.print(" get");
    out.print(referenceName);
    out.print("() {\n\t\ttry {\t\t\n\t\t\treturn ");
    out.print(getHome);
    out.print("().findByPrimaryKey(get");
    out.print(referenceName);
    out.print("Key());\n\t\t}\n\t\tcatch (ObjectNotFoundException ex) {\n\t\t\treturn null;\n\t\t}\n\t\tcatch (Exception ex) {\n\t\t\tex.printStackTrace();\n\t\t\tthrow new EJBException(XavaResources.getString(\"get_reference_error\", \"");
    out.print(referenceName);
    out.print("\", \"");
    out.print(metaModel.getName());
    out.print("\"));\n\t\t}\t\t\n\t}\t\n\t\n\t\n\t/**\n\t * @ejb:interface-method\n\t */\n\tpublic ");
    out.print(referencedModel.getMetaEJB().getRemote());
    out.print(" get");
    out.print(referenceName);
    out.print("Remote() {\n\t\treturn (");
    out.print(referencedModel.getMetaEJB().getRemote());
    out.print(") get");
    out.print(referenceName);
    out.print("();\n\t}\n\t\n\t/**\n\t * ");
    out.print(interfaceMethodSet);
    out.print("\n\t */\n\tpublic void set");
    out.print(referenceName);
    out.print("(");
    out.print(referencedModelClass);
    out.print(" new");
    out.print(referenceName);
    out.print(") {");
    if (ejb) { 
    out.print(" \n\t\tthis.modified = true;");
    } 
    out.print(" \n\t\ttry {\t\n\t\t\tif (new");
    out.print(referenceName);
    out.print(" == null) set");
    out.print(referenceName);
    out.print("Key(null);\n\t\t\telse {\n\t\t\t\tif (new");
    out.print(referenceName);
    out.print(" instanceof ");
    out.print(referencedModel.getPOJOClass().getName());
    out.print(") {\n\t\t\t\t\tthrow new IllegalArgumentException(XavaResources.getString(\"pojo_to_ejb_illegal\"));\n\t\t\t\t}\n\t\t\t\t");
    out.print(referencedModel.getMetaEJB().getRemote());
    out.print(" remote = (");
    out.print(referencedModel.getMetaEJB().getRemote());
    out.print(") new");
    out.print(referenceName);
    out.print(";\n\t\t\t\tset");
    out.print(referenceName);
    out.print("Key((");
    out.print(referencedKeyClass);
    out.print(") remote.getPrimaryKey());\n\t\t\t}\t\n\t\t}\n\t\tcatch (IllegalArgumentException ex) {\n\t\t\tthrow ex;\n\t\t}\n\t\tcatch (Exception ex) {\n\t\t\tex.printStackTrace();\n\t\t\tthrow new EJBException(XavaResources.getString(\"set_reference_error\", \"");
    out.print(referenceName);
    out.print("\", \"");
    out.print(metaModel.getName());
    out.print("\"));\n\t\t}\n\t}\n\t\n\t/**\n\t * @ejb:interface-method\n\t */\n\tpublic ");
    out.print(referencedKeyClass);
    out.print(" get");
    out.print(referenceName);
    out.print("Key() {\t\t\t\t\n\t\t");
    out.print(referencedKeyClass);
    out.print(" key = new ");
    out.print(referencedKeyClass);
    out.print("();");
    
    		Iterator itKeys = referencedModel.getAllKeyPropertiesNames().iterator();
    		while (itKeys.hasNext()) {
    			String property = (String) itKeys.next();
    			String key = Strings.change(property, ".", "_"); 
    			String keyAttribute = null;
    			String underline = "";
    			if (referencedMapping.hasConverter(key) || property.indexOf('.') >= 0) { 
    				keyAttribute = "_" + Strings.firstUpper(key);
    				if (!modelMapping.isReferenceOverlappingWithSomeProperty(reference.getName(), property)) {
    					underline = "_";
    				}
    			}
    			else {
    				keyAttribute = key;
    			}
    		
    out.print(" \n\t\tkey.");
    out.print(keyAttribute);
    out.print(" = get");
    out.print(underline);
    out.print(referenceName);
    out.print("_");
    out.print(key);
    out.print("();");
    } 
    out.print("\t\t\n\t\treturn key;\n\t}\t\n\t\n\t/**\n\t * ");
    out.print(interfaceMethodSet);
    out.print("\n\t */\n\tpublic void set");
    out.print(referenceName);
    out.print("Key(");
    out.print(referencedKeyClass);
    out.print(" key) {");
    if (ejb) { 
    out.print(" \n\t\tthis.modified = true;");
    } 
    out.print(" \t\t\n\t\tif (key == null) {\n\t\t\tkey = new ");
    out.print(referencedKeyClass);
    out.print("();");
    
    		itKeys = referencedModel.getAllKeyPropertiesNames().iterator();		
    		while (itKeys.hasNext()) {
    		    String property = (String) itKeys.next(); 
    			String key = Strings.change(property, ".", "_");
    			String keyAttribute=null;
    			if (referencedMapping.hasConverter(key) || property.indexOf('.') >= 0) {
    				keyAttribute = "_" + Strings.firstUpper(key);
    			}
    			else {
    				keyAttribute = key;
    			}
    		
    out.print(" \n\t\t\tset");
    out.print(referenceName);
    out.print("_");
    out.print(key);
    out.print("(key.");
    out.print(keyAttribute);
    out.print(");");
    } 
    out.print("\t\t\t\t\t\n\t\t}\n\t\telse {");
    		
    		itKeys = referencedModel.getAllKeyPropertiesNames().iterator();		
    		while (itKeys.hasNext()) {
    		    String property = (String) itKeys.next(); 
    			String key = Strings.change(property, ".", "_");
    			String keyAttribute=null;
    			String underline = "";
    			if (referencedMapping.hasConverter(key) || property.indexOf('.') >= 0) {
    				keyAttribute = "_" + Strings.firstUpper(key);
    				if (!modelMapping.isReferenceOverlappingWithSomeProperty(reference.getName(), property)) {
    					underline = "_";
    				}
    			}
    			else {
    				keyAttribute = key;
    			}
    		
    out.print(" \n\t\t\tset");
    out.print(underline);
    out.print(referenceName);
    out.print("_");
    out.print(key);
    out.print("(key.");
    out.print(keyAttribute);
    out.print(");");
    } 
    out.print("\t\t\n\t\t}\n\t}");
    
    	Iterator itEntityReferenceKeyProperties = referencedModel.getAllMetaPropertiesKey().iterator();	
    	while (itEntityReferenceKeyProperties.hasNext()) {		
    		MetaProperty originalProperty = (MetaProperty) itEntityReferenceKeyProperties.next();
    		originalProperty.setReadOnly(false);
    		PropertyMapping propertyMapping = originalProperty.getMapping();
    		MetaProperty property = originalProperty.cloneMetaProperty();
    		property.setName(reference.getName() + "_" + property.getName());
    		String propertyName = Strings.change(Strings.firstUpper(property.getName()), ".", "_");
    		if (GeneratorFactory.has(property, ejb)) {
    			IPropertyCodeGenerator generator = GeneratorFactory.create(property, ejb);		
    			String propertyCode = generator.generate();
    	
    out.print("\n\t\t");
    out.print(propertyCode);
    		
    			continue;
    		}
    		else if (modelMapping.isReferenceOverlappingWithSomeProperty(reference.getName(), originalProperty.getName())) { 
    			String type = property.getTypeName();
    			String overlapPropertyName = Strings.firstUpper(modelMapping.getOverlappingPropertyForReference(reference.getName(), originalProperty.getName()));
    	
    out.print("\n\t/**\t\t\n\t * @ejb:interface-method\n\t *\n\t * @ejb.value-object match=\"persistentCalculatedAndAggregate\"\n\t */\n\tpublic ");
    out.print(type);
    out.print(" get");
    out.print(propertyName);
    out.print("() {\n\t\treturn get");
    out.print(overlapPropertyName);
    out.print("();\n\t}\n\tpublic void set");
    out.print(propertyName);
    out.print("(");
    out.print(type);
    out.print(" ");
    out.print(propertyName);
    out.print(") {\n\t}");
    					
    			continue;
    		}		
    		String type = null;
    		if (propertyMapping.hasConverter()) {
    			type = propertyMapping.getCmpTypeName();
    		}
    		else {
    			type = property.getTypeName();
    		}	
    		String column = referenceMapping.getColumnForReferencedModelProperty(originalProperty.getName());			
    		String cmpType = referenceMapping.getCmpTypeNameForReferencedModelProperty(originalProperty.getName()); 
    		if (Is.emptyString(cmpType)) cmpType = type;
    	
    out.print("\n\t/**\t\t\n\t * @ejb:persistent-field\n\t * ");
    out.print(pkField);
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
    out.print(");\n\n\t/**\t\t\n\t * @ejb:interface-method\n\t * @ejb.value-object match=\"persistentCalculatedAndAggregate\"\n\t */\n\tpublic ");
    out.print(type);
    out.print(" get");
    out.print(propertyName);
    out.print("() {");
    
    		if (referenceMapping.hasConverter(originalProperty.getName())) {			
    			String getSentence = property.getName() + "Converter.toJava(get_" + propertyName + "())";							
    		
    out.print(" \n\t\ttry {\n\t\t\treturn ");
    out.print(Generators.generateCast(type, getSentence));
    out.print(";\n\t\t}\n\t\tcatch (org.openxava.converters.ConversionException ex) {\n\t\t\tex.printStackTrace();\n\t\t\tthrow new ");
    out.print(getExcepcion());
    out.print("(XavaResources.getString(\"generator.conversion_error\", \"");
    out.print(reference.getName());
    out.print(".");
    out.print(originalProperty.getName());
    out.print("\", \"");
    out.print(metaModel.getName());
    out.print("\", \"");
    out.print(type);
    out.print("\"));\n\t\t}");
    } else { 
    out.print(" \n\t\treturn get_");
    out.print(propertyName);
    out.print("();");
    } 
    out.print(" \n\t}\n\tpublic void set");
    out.print(propertyName);
    out.print("(");
    out.print(type);
    out.print(" new");
    out.print(propertyName);
    out.print(") {");
    
    		if (referenceMapping.hasConverter(originalProperty.getName())) {			
    			String argv = Generators.generatePrimitiveWrapper(type, "new" + propertyName);			
    			String sentence = property.getName() + "Converter.toDB(" + argv + ")";
    		
    out.print(" \n\t\ttry {\n\t\t\tset_");
    out.print(propertyName);
    out.print("(");
    out.print(Generators.generateCast(cmpType, sentence));
    out.print(");\n\t\t}\n\t\tcatch (org.openxava.converters.ConversionException ex) {\n\t\t\tex.printStackTrace();\n\t\t\tthrow new ");
    out.print(getExcepcion());
    out.print("(XavaResources.getString(\"generator.conversion_error\", \"");
    out.print(reference.getName());
    out.print(".");
    out.print(originalProperty.getName());
    out.print("\", \"");
    out.print(metaModel.getName());
    out.print("\", \"");
    out.print(type);
    out.print("\"));\n\t\t}");
    } else { 
    out.print(" \n\t\tset_");
    out.print(propertyName);
    out.print("(new");
    out.print(propertyName);
    out.print(");");
    } 
    out.print(" \t\n\t}");
    } // while referenced entity key properties 
    out.print(" \n\n\tprivate ");
    out.print(homeClass);
    out.print(" ");
    out.print(homeAttribute);
    out.print(";\t\n\tprivate ");
    out.print(homeClass);
    out.print(" ");
    out.print(getHome);
    out.print("() throws Exception{\n\t\tif (");
    out.print(homeAttribute);
    out.print(" == null) {\n\t\t\t");
    out.print(homeAttribute);
    out.print(" = (");
    out.print(homeClass);
    out.print(") PortableRemoteObject.narrow(\n\t\t\t \t\tBeansContext.get().lookup(\"");
    out.print(referencedJNDI);
    out.print("\"),\n\t\t\t \t\t");
    out.print(homeClass);
    out.print(".class);\t\t\t \t\t\n\t\t}\n\t\treturn ");
    out.print(homeAttribute);
    out.print(";\n\t}");
    
    	for (Iterator it = referenceMapping.getDetails().iterator(); it.hasNext(); ) {	
    		ReferenceMappingDetail detail = (ReferenceMappingDetail) it.next();
    		if (detail.hasConverter()) {
    			String key = Strings.change(detail.getReferencedModelProperty(), ".", "_");
    	
    out.print(" \n\t\n\tprivate static final ");
    out.print(detail.getConverterClassName());
    out.print(" \n\t\t");
    out.print(reference.getName());
    out.print("_");
    out.print(key);
    out.print("Converter =\n\t\t\tnew ");
    out.print(detail.getConverterClassName());
    out.print("();");
    
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
            EntityReferenceEJBPG pg = new EntityReferenceEJBPG();
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
        { "Thu Dec 14 18:26:59 CET 2006", // date this file was generated
             "../OpenXava/generator/entityReferenceEJB.xml", // input file
             "../OpenXava/generator/EntityReferenceEJBPG.java" }, // output file
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