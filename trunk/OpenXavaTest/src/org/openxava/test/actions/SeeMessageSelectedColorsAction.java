package org.openxava.test.actions;

import javax.inject.*;

import org.apache.commons.logging.*;
import org.openxava.actions.*;
import org.openxava.tab.*;

/**
 * Create on 28/02/2013 (12:46:06)
 * @author Ana Andres
 */
public class SeeMessageSelectedColorsAction extends BaseAction{
	private static Log log = LogFactory.getLog(SeeMessageSelectedColorsAction.class);
	
	@Inject
	public Tab tab;
	
	public void execute() throws Exception {
		int[] selected = getTab().getSelected();	// test the old method
		String m = "";
		for (int i = 0; i < selected.length; i++) m+="[" + selected[i] + "]";
		addMessage("color_selected", m);
	}

	public Tab getTab() {
		return tab;
	}

	public void setTab(Tab tab) {
		this.tab = tab;
	}
}
