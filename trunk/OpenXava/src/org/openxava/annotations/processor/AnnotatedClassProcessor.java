package org.openxava.annotations.processor;

import java.io.*;
import java.text.MessageFormat;
import java.util.*;

import javassist.*;
import javassist.bytecode.*;
import javassist.bytecode.annotation.*;

import org.apache.commons.io.*;
import org.apache.commons.logging.*;
import org.openxava.annotations.*;
import org.openxava.util.*;

/**
 * Annotations processor for manipulating bytecode. <p>
 * 
 * @author Jeromy Altuna
 * @since  5.3
 */
public class AnnotatedClassProcessor {
	
	private static Log log = LogFactory.getLog(AnnotatedClassProcessor.class);
	
	private static ClassPool pool = ClassPool.getDefault();
	private static class I18n {
		private static ResourceBundle rb = ResourceBundle.getBundle("Messages");
		
		public static String getString(String key, Object... args) {
			MessageFormat formatter = new MessageFormat("");
			formatter.setLocale(rb.getLocale());
			formatter.applyPattern(rb.getString(key));
			return formatter.format(args);
		}
	}
	
	private String classpath;
	
	public AnnotatedClassProcessor(String classpath) {
		this.classpath = classpath;
		try {
			pool.appendClassPath(this.classpath);
		} catch (NotFoundException ex) {
			log.error(ex.getMessage(), ex);
			throw new RuntimeException(
				  I18n.getString("annotated_class_processor_creation_error")
			);
		} finally {
			log.info("Classpath: " + this.classpath);
		}
	}
	
	public void process() throws Exception {
		
		for (String className : getClassNames()) {
			CtClass clazz = pool.get(className);
			
			if (clazz.hasAnnotation(Concurrent.class)) {
				processingConcurrent(clazz);
			}
		}
	}
	
	private Collection<String> getClassNames() {
		Collection<String> classNames = new ArrayList<String>();
		File directory = new File(classpath);
		String[] extensions = {"class"}; 		
		int beginIndex = classpath.length() + 1;
		
		for (File file : FileUtils.listFiles(directory, extensions, true)) {
			classNames.add(FilenameUtils.removeExtension(file.getPath())
					                    .substring(beginIndex)
					                    .replaceAll(File.separator, "."));
		}
		return classNames;
	}	
	
	private void processingConcurrent(CtClass clazz) throws ClassNotFoundException, 
	                                                         CannotCompileException, IOException 
	{
		CtField field = null;
		CtMethod method = null;		
		Concurrent cc = (Concurrent) clazz.getAnnotation(Concurrent.class);
		Property prop = cc.value();
	    String getter = "get" + Strings.firstUpper(prop.name());
		String setter = "set" + Strings.firstUpper(prop.name());
		String declar = String.format("private %s %s;", prop.type().getClassname(), prop.name());
		
		try {
			field = clazz.getField(prop.name());
			log.warn(I18n.getString("existing_member_in_class", declar, clazz.getName()));	
			
		} catch (NotFoundException ex) {
			field = CtField.make(declar, clazz);
			clazz.addField(field);
			addAnnotation(field, clazz, "javax.persistence.Version");
		}
		try {
			clazz.getDeclaredMethod(getter);
			log.warn(I18n.getString("existing_member_in_class", getter, clazz.getName()));
			
		} catch (NotFoundException ex) {
			method = CtNewMethod.getter(getter, field);
			clazz.addMethod(method);			
		}
		try {
			clazz.getDeclaredMethod(setter);
			log.warn(I18n.getString("existing_member_in_class", setter, clazz.getName()));
			
		} catch (NotFoundException ex) {
			method = CtNewMethod.setter(setter, field);
			clazz.addMethod(method);
		}
		clazz.writeFile(classpath);
	}
	
	private void addAnnotation(CtField field, CtClass clazz, String annotation) {
		ClassFile cf = clazz.getClassFile();
		ConstPool cp = cf.getConstPool();
		AnnotationsAttribute attribute = new AnnotationsAttribute(cp, 
				                             AnnotationsAttribute.visibleTag);
		Annotation annt = new Annotation(annotation, cp); 
		attribute.addAnnotation(annt);
		field.getFieldInfo().addAttribute(attribute);
	}
}
