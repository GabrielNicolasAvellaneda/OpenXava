package org.openxava.model.inner;

import java.util.*;

import javax.persistence.*;
import org.openxava.annotations.*;
import org.openxava.model.meta.*;

/**
 * tmp ¿Mover a paquete session? Ahora es un objeto de sesión 
 * 
 * @author Javier Paniza 
 */

public class CustomReport implements java.io.Serializable {
	
	@Required @Column(length=80)
	private String reportName;
	
	@RowActions({
		@RowAction("CustomReport.columnUp"),
		@RowAction("CustomReport.columnDown")
	})
	@RemoveSelectedAction("CustomReport.removeColumn")
	@AsEmbedded 
	@SaveAction("CustomReport.saveColumn")
	@EditAction("CustomReport.editColumn")
	private List<CustomReportColumn> columns;
	
	public static CustomReport create(org.openxava.tab.Tab tab) { // tmp ¿Mover esto a la acción? tab es un objeto de sesión 
		CustomReport report = new CustomReport();
		report.setReportName(tab.getTitle()); // tmp
		report.setColumns(createColumns(tab));
		return report;
	}
	
	private static List<CustomReportColumn> createColumns(org.openxava.tab.Tab tab) {
		List<CustomReportColumn> columns = new ArrayList<CustomReportColumn>();
		for (MetaProperty property: tab.getMetaProperties()) {		
			CustomReportColumn column = new CustomReportColumn();					
			column.setColumnName(property.getQualifiedName());
			columns.add(column);
		}		
		return columns;		
	}

	
	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public List<CustomReportColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<CustomReportColumn> columns) {
		this.columns = columns;
	}
		
}
