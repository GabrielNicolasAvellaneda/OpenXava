
/* Generated by TL2Java Version 1.2, April 9, 2001 */
import org.w3c.dom.*;
import java.io.*;
import java.util.*;
import org.openxava.model.meta.*;
import org.openxava.mapping.*;
import org.openxava.util.*;
import org.openxava.generators.*;

/**
 * Program Generator created by TL2Java
 * @version Thu Jun 08 19:16:50 CEST 2006
 */
public class EntityReferencePG {
    Properties properties = new Properties();


private MetaReference reference=null;
public void setReference(MetaReference ref) {
	this.reference = ref;
}

public static void generate(XPathContext context, ProgramWriter out, MetaReference ref) throws XavaException {
	EntityReferencePG pg = new EntityReferencePG();
	pg.setReference(ref);
	pg.generate(context, out);
}


    /**
     * This method generates the output given a context and output stream
     */
    public boolean generate(XPathContext context, ProgramWriter out) {
        try {    
    String refName = Strings.firstUpper(reference.getName());
    
    out.print(" \n\n\tprivate ");
    out.print(reference.getMetaModelReferenced().getInterfaceName());
    out.print(" ");
    out.print(reference.getName());
    out.print(";");
    	
    ModelMapping mapping = null;
    if (reference.getMetaModel() instanceof MetaAggregateForReference) {
    	mapping = reference.getMetaModel().getMetaModelContainer().getMapping();
    }
    else {
    	mapping = reference.getMetaModel().getMapping();
    }
    ReferenceMapping referenceMapping = mapping.getReferenceMapping(reference.getName());
    boolean overlapped = mapping.isReferenceOverlappingWithSomeProperty(reference.getName());
    String setException = overlapped?"throws RemoteException":"";
    MetaModel referencedModel = (MetaModel) reference.getMetaModelReferenced();
    if (overlapped) {
    	for (Iterator itDetails = referenceMapping.getDetails().iterator(); itDetails.hasNext(); ) {
    		ReferenceMappingDetail detail = (ReferenceMappingDetail) itDetails.next();
    		if (!mapping.isReferenceOverlappingWithSomeProperty(reference.getName(), detail.getReferencedModelProperty())) {
    			String cmpTypeName = detail.getCmpTypeName();
    			Class type = null;
    			if (Is.emptyString(cmpTypeName)) {
    				type = referencedModel.getMetaProperty(detail.getReferencedModelProperty()).getType();
    			}
    			else {
    				type = Class.forName(cmpTypeName);
    			}
    			String typeName = Primitives.toWrapperClass(type).getName();
    			String referencedModelProperty = Strings.change(detail.getReferencedModelProperty(), ".", "_");
    
    out.print(" \n\tprivate ");
    out.print(typeName);
    out.print(" ");
    out.print(reference.getName());
    out.print("_");
    out.print(referencedModelProperty);
    out.print(";");
    
    			if (detail.hasConverter()) {
    				String converterAttribute = reference.getName() + "_" + detail.getReferencedModelProperty() + "Converter";
    
    out.print(" \n\tprivate static org.openxava.converters.IConverter ");
    out.print(converterAttribute);
    out.print(";\n\tprivate org.openxava.converters.IConverter get");
    out.print(Strings.firstUpper(converterAttribute));
    out.print("() {\n\t\tif (");
    out.print(converterAttribute);
    out.print(" == null) {\n\t\t\ttry {\n\t\t\t\t");
    out.print(converterAttribute);
    out.print(" = (org.openxava.converters.IConverter) \n\t\t\t\t\tgetMetaModel().getMapping().getReferenceMapping(\"");
    out.print(reference.getName());
    out.print("\").getConverterForReferencedModelProperty(\"");
    out.print(detail.getReferencedModelProperty());
    out.print("\");\n\t\t\t}\n\t\t\tcatch (Exception ex) {\n\t\t\t\tex.printStackTrace();\n\t\t\t\tthrow new RuntimeException(XavaResources.getString(\"generator.create_converter_error\", \"");
    out.print(reference.getName());
    out.print(".");
    out.print(detail.getReferencedModelProperty());
    out.print("\"));\n\t\t\t}\n\t\t\t\n\t\t}\t\n\t\treturn ");
    out.print(converterAttribute);
    out.print(";\n\t}");
    
    			}
    		}
    	}
    }
    
    out.print(" \t\n\tpublic ");
    out.print(reference.getMetaModelReferenced().getInterfaceName());
    out.print(" get");
    out.print(refName);
    out.print("() {\n\t\tif (");
    out.print(reference.getName());
    out.print(" != null) {\n\t\t\t// Because not-found='ignore' annul lazy initialization, we simulate it\n\t\t\ttry {\n\t\t\t\t");
    out.print(reference.getName());
    out.print(".toString();\n\t\t\t}\n\t\t\tcatch (Exception ex) {\n\t\t\t\treturn null;\n\t\t\t}\n\t\t}\t\n\t\treturn ");
    out.print(reference.getName());
    out.print(";\n\t}\n\tpublic void set");
    out.print(refName);
    out.print("(");
    out.print(reference.getMetaModelReferenced().getInterfaceName());
    out.print(" new");
    out.print(reference.getReferencedModelName());
    out.print(") ");
    out.print(setException);
    out.print("{\n\t\tif (new");
    out.print(reference.getReferencedModelName());
    out.print(" != null && !(new");
    out.print(reference.getReferencedModelName());
    out.print(" instanceof ");
    out.print(reference.getMetaModelReferenced().getPOJOClassName());
    out.print(")) {\n\t\t\tthrow new IllegalArgumentException(XavaResources.getString(\"ejb_to_pojo_illegal\")); \n\t\t}\n\t\tthis.");
    out.print(reference.getName());
    out.print(" = new");
    out.print(reference.getReferencedModelName());
    out.print(";");
    
    if (overlapped) {
    	for (Iterator itDetails = referenceMapping.getDetails().iterator(); itDetails.hasNext(); ) {
    		ReferenceMappingDetail detail = (ReferenceMappingDetail) itDetails.next();
    		if (!mapping.isReferenceOverlappingWithSomeProperty(reference.getName(), detail.getReferencedModelProperty())) {
    			String sentence = "new" + reference.getReferencedModelName() + detail.getReferenceModelPropertyAsJavaMethodCall();						
    			String type = referencedModel.getMetaProperty(detail.getReferencedModelProperty()).getType().getName();
    			String cmpType = detail.getCmpTypeName() == null?type:detail.getCmpTypeName();
    			String referencedModelProperty = Strings.change(detail.getReferencedModelProperty(), ".", "_");
    			if (detail.hasConverter()) {
    				String converterGet = "get" + Strings.firstUpper(reference.getName()) + "_" + detail.getReferencedModelProperty() + "Converter()";
    
    out.print(" \n\t\ttry {\n\t\t\t");
    out.print(reference.getName());
    out.print("_");
    out.print(referencedModelProperty);
    out.print(" = (");
    out.print(cmpType);
    out.print(") ");
    out.print(converterGet);
    out.print(".toDB(new");
    out.print(reference.getReferencedModelName());
    out.print("==null?null:");
    out.print(Generators.generatePrimitiveWrapper(type, sentence));
    out.print(");\n\t\t}\n\t\tcatch (org.openxava.converters.ConversionException ex) {\n\t\t\tex.printStackTrace();\n\t\t\tthrow new RuntimeException(XavaResources.getString(\"generator.conversion_error\", \"");
    out.print(reference.getName());
    out.print(".");
    out.print(detail.getReferencedModelProperty());
    out.print("\", \"");
    out.print(reference.getMetaModel().getName());
    out.print("\", \"");
    out.print(type);
    out.print("\"));\n\t\t}");
    			
    			}
    			else {
    
    out.print(" \n\t\tthis.");
    out.print(reference.getName());
    out.print("_");
    out.print(referencedModelProperty);
    out.print(" = new");
    out.print(reference.getReferencedModelName());
    out.print(" == null?null:");
    out.print(Generators.generatePrimitiveWrapper(type, sentence));
    out.print(";");
    
    			}
    		}
    	}
    }
    
    out.print(" \n\t}");
    
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
            EntityReferencePG pg = new EntityReferencePG();
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
        { "Thu Jun 08 19:16:50 CEST 2006", // date this file was generated
             "/home/javi/workspace2/OpenXava/generator/entityReference.xml", // input file
             "/home/javi/workspace2/OpenXava/generator/EntityReferencePG.java" }, // output file
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