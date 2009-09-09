package org.openxava.web.dwr;

import java.util.*;

/**
 * 
 * @author Javier Paniza
 */

public class Result {

	private Map changedParts;
	private Map strokeActions;
	private String forwardURL;
	private boolean forwardInNewWindow;
	private String application;  
	private String module;
	private String nextModule; 
	
	private String focusPropertyId;
	private String error;
	private boolean reload; 
	
	public Result() {
	}
	
	public Result(Map changedParts) {
		this.changedParts = changedParts;
	}		
	
	public Map getStrokeActions() {
		return strokeActions == null?Collections.EMPTY_MAP:strokeActions;
	}

	public void setStrokeActions(Map strokeActions) {
		this.strokeActions = strokeActions;
	}

	public Map getChangedParts() {
		return changedParts;
	}

	public void setChangedParts(Map changedParts) {
		this.changedParts = changedParts;
	}
	
	public String getForwardURL() {
		return forwardURL;
	}

	public void setForwardURL(String forwardURL) {
		this.forwardURL = forwardURL;
	}
	
	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}	
	
	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}
	
	public String getNextModule() {
		return nextModule;
	}

	public void setNextModule(String nextModule) {
		this.nextModule = nextModule;
	}
		
	public boolean isForwardInNewWindow() {
		return forwardInNewWindow;
	}

	public void setForwardInNewWindow(boolean forwardInNewWindow) {
		this.forwardInNewWindow = forwardInNewWindow;
	}

	public String getFocusPropertyId() {
		return focusPropertyId;
	}

	public void setFocusPropertyId(String focusPropertyId) {
		this.focusPropertyId = focusPropertyId;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public boolean isReload() {
		return reload;
	}

	public void setReload(boolean reload) {
		this.reload = reload;
	}

}