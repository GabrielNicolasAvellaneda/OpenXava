package org.openxava.test.actions;

import org.openxava.actions.*;

/**
 * @author Javier Paniza
 */
public class ChangeDefaultZoneAction extends ViewBaseAction {
	
	private int newDefaultZone;
	private int defaultZone;

	public void execute() throws Exception {
		setDefaultZone(getNewDefaultZone());
		resetDescriptionsCache();
	}

	public int getDefaultZone() {
		return defaultZone;
	}
	public void setDefaultZone(int defaultZone) {
		this.defaultZone = defaultZone;
	}
	public int getNewDefaultZone() {
		return newDefaultZone;
	}
	public void setNewDefaultZone(int newDefaultZone) {
		this.newDefaultZone = newDefaultZone;
	}
}
