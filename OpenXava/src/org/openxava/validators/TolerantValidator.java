package org.openxava.validators;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.util.*;

/**
 * Everything is valid. <p>
 * 
 * @author Javier Paniza
 */
public class TolerantValidator implements IPropertyValidator {

	private static Log log = LogFactory.getLog(TolerantValidator.class);
	
	public void validate(
		Messages errors,
		Object object,
		String objectName,
		String propertyName)
		throws java.rmi.RemoteException {
	}
	
}
