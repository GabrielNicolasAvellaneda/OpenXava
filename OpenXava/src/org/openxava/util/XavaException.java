package org.openxava.util;


/**
 * @author Javier Paniza
 */
public class XavaException extends Exception {
	
	private Object argv0;
	private Object argv1;
	private Object argv2;
	private Object argv3;

	public XavaException() {
		super();
	}
	
	/**
	 * @param idOrMessage Id for look messsage in XavaResources, or text of the
	 * 		message
	 */
	public XavaException(String idOrMessage) {
		super(idOrMessage);		
	}
	
	public XavaException(String idOrMessage, Object argv0) {
		super(idOrMessage);
		this.argv0 = argv0;
	}
		
	public XavaException(String idOrMessage, Object argv0, Object argv1) {
		super(idOrMessage);
		this.argv0 = argv0;
		this.argv1 = argv1;
	}
	
	public XavaException(String idOrMessage, Object argv0, Object argv1, Object argv2) {
		super(idOrMessage);
		this.argv0 = argv0;
		this.argv1 = argv1;
		this.argv2 = argv2;
	}
	
	public XavaException(String idOrMessage, Object argv0, Object argv1, Object argv2, Object argv3) {
		super(idOrMessage);
		this.argv0 = argv0;
		this.argv1 = argv1;
		this.argv2 = argv2;
		this.argv3 = argv3;
	}
	
	
	
	
	public String getMessage() {
		return getLocalizedMessage();
	}
	
	public String getLocalizedMessage() {
		if (argv3 != null) {
			return XavaResources.getString(super.getMessage(), argv0, argv1, argv2, argv3);
		}				
		if (argv2 != null) {
			return XavaResources.getString(super.getMessage(), argv0, argv1, argv2);
		}		
		if (argv1 != null) {
			return XavaResources.getString(super.getMessage(), argv0, argv1);
		}
		if (argv0 != null) return XavaResources.getString(super.getMessage(), argv0);
		return XavaResources.getString(super.getMessage());
	}
	
}
