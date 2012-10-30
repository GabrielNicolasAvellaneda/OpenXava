package org.openxava.actions;

import javax.inject.*;

import org.openxava.model.inner.*;
import org.openxava.tab.*;
import org.openxava.util.*;

/**
 * tmp
 * 
 * @author Javier Paniza
 */

public class GenerateCustomReportAction extends GenerateReportAction {
	
	@Inject
	private CustomReport customReport; 
	
	public void execute() throws Exception {		
		super.execute();
		Tab tab = new Tab();
		tab.setModelName(getTab().getModelName());
		tab.setTabName(getTab().getTabName());
		tab.setTitle(getView().getValueString("reportName"));		
		tab.clearProperties();
		StringBuffer condition = new StringBuffer(getTab().getBaseCondition()==null?"":getTab().getBaseCondition()); 
		for (CustomReportColumn column: customReport.getColumns()) {
			tab.addProperty(column.getColumnName());
			if (column.getComparator() != null) {
				if (condition.length() > 0) {
					condition.append(" AND ");
				}
				condition.append("${");
				condition.append(column.getColumnName());
				condition.append("} ");
				condition.append(column.getComparatorSign());
				condition.append(' ');
				condition.append(column.getDecoratedValue());
			}
		}
		tab.setBaseCondition(condition.toString());
		
		
		getRequest().getSession().setAttribute("xava_reportTab", tab);
		// tmp ¿Qué hacemos con esto? getRequest().getSession().setAttribute("xava_selectedRowsReportTab", getTab().getSelected());		
		closeDialog();
	}
	
}
