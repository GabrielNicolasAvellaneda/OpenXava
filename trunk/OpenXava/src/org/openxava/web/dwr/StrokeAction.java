package org.openxava.web.dwr;

/**
 * 
 * @author Javier Paniza
 */

public class StrokeAction {

	private String name;
	private boolean takesLong;
	
	public StrokeAction(String name, boolean takesLong) {
		this.name = name;
		this.takesLong = takesLong;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isTakesLong() {
		return takesLong;
	}
	public void setTakesLong(boolean takesLong) {
		this.takesLong = takesLong;
	}
		
}
