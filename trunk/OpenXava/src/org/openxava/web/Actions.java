package org.openxava.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.controller.meta.MetaAction;
import org.openxava.util.Is;

/**
 * Utility class to help in action code generato for JSPs.
 * 
 * Create on 30/10/2009 (16:32:16)
 * @author Ana Andr�s
 */
public class Actions {
	private static Log log = LogFactory.getLog(Actions.class);
	
	public static String getActionOnClick(String application, String module, 
			String onSelectCollectionElementAction, int row, String viewObject, String idRow,
			String cssSelectedRow, String cssClass, String selectedRowStyle, String rowStyle,
			MetaAction onSelectCollectionElementMetaAction){
		return "onClick=\"openxava.onSelectElement(" +
			"'" + application + "'," + 
			"'" + module + "'," + 
			"'" + onSelectCollectionElementAction + "'," + 
			"'row=" + row + ",viewObject=" + viewObject + "'," +
			"this.checked," + 
			"'" + idRow + "'," +  
			!Is.empty(onSelectCollectionElementAction) + "," +
			"'" + cssSelectedRow + "'," + 
			"'" + cssClass + "'," +
			"'" + selectedRowStyle + "'," +
			"'" + rowStyle + "'," +
			"'" + (Is.empty(onSelectCollectionElementMetaAction)?"":onSelectCollectionElementMetaAction.getConfirmMessage()) + "'," + 
			(Is.empty(onSelectCollectionElementMetaAction)?false:onSelectCollectionElementMetaAction.isTakesLong()) + 
			")\"";
	}
	
	public static String getActionOnClickAll(String application, String module, 
			String onSelectCollectionElementAction, String viewObject, String prefix,
			String cssSelectedRow, String selectedRowStyle, String rowStyle){
		return  "onClick=\"openxava.onSelectAll(" +
			"'" + application + "'," + 
			"'" + module + "'," + 
			"'" + onSelectCollectionElementAction + "'," + 
			"'viewObject=" + viewObject + "'," +
			"this.checked," + 
			!Is.empty(onSelectCollectionElementAction) + "," +
			"'" + prefix + "'," +
			"'" + cssSelectedRow + "'," + 
			"'" + selectedRowStyle + "'," +
			"'" + rowStyle + "'" + 
			")\"";
	}
	
}