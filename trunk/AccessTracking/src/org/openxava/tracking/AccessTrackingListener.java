package org.openxava.tracking;

import javax.persistence.*;

import org.apache.commons.logging.*;
import org.openxava.util.*;

/**
 * Adapter for AccessTrackingCalculator to a listener suitable for JPA. <p>
 * 
 * @author Javier Paniza
 */

public class AccessTrackingListener {
	
	private static Log log = LogFactory.getLog(AccessTrackingListener.class);
	
	@PostPersist
	public void trackCreateAccess(Object model) {
		trackAccess(model, "Create");
	}
	
	@PostLoad
	public void trackReadAccess(Object model) {
		trackAccess(model, "Read");
	}
	
	@PostUpdate
	public void trackUpdateAccess(Object model) {
		trackAccess(model, "Update");
	}
	
	@PreRemove
	public void trackDeleteAccess(Object model) {
		trackAccess(model, "Delete");
	}
		
	private void trackAccess(Object model, String accessType) {		
		try {			
			AccessTrackingCalculator calculator = new AccessTrackingCalculator();
			calculator.setModel(model);
			calculator.setAccessType(accessType);
			calculator.calculate();
		} 
		catch (Exception ex) {
			log.warn(XavaResources.getString("tracking_warning", ex.getLocalizedMessage())); // tracking is not so critical to abort the user work.
		}
	}


}
