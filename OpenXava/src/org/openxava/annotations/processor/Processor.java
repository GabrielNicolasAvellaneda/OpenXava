package org.openxava.annotations.processor;

import org.apache.commons.logging.*;

/**
 * @author Jeromy Altuna
 * @since  5.3 
 */
public class Processor {
	
	private static Log log = LogFactory.getLog(Processor.class);
	
	public static void main(String[] args) {
		try {
			new AnnotatedClassProcessor(args[0]).process();
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);			
		}
	}
}
