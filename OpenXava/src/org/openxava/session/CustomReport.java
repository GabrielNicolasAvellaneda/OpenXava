package org.openxava.session;

import java.util.*;

import javax.persistence.*;
import org.openxava.annotations.*;
import org.openxava.model.meta.*;

/**
 * 
 * @author Javier Paniza 
 */

public class CustomReport implements java.io.Serializable {
	
	@Required @Column(length=80)
	private String name;
	
	@Hidden
	private MetaModel metaModel;
	
	@RowActions({
		@RowAction("CustomReport.columnUp"),
		@RowAction("CustomReport.columnDown")
	})
	@RemoveSelectedAction("CustomReport.removeColumn")
	@AsEmbedded 
	@SaveAction("CustomReport.saveColumn")
	@EditAction("CustomReport.editColumn")
	@ListProperties("name, comparator, value, order")
	private List<CustomReportColumn> columns;
	
	public static CustomReport create(org.openxava.tab.Tab tab) {  
		CustomReport report = new CustomReport();
		report.setName(tab.getTitle()); 
		report.setColumns(createColumns(report, tab));
		report.setMetaModel(tab.getMetaTab().getMetaModel());
		return report;
	}
	
	private static List<CustomReportColumn> createColumns(CustomReport report, org.openxava.tab.Tab tab) {
		List<CustomReportColumn> columns = new ArrayList<CustomReportColumn>();
		for (MetaProperty property: tab.getMetaProperties()) {		
			CustomReportColumn column = new CustomReportColumn();
			column.setReport(report);
			column.setName(property.getQualifiedName());
			column.setCalculated(property.isCalculated());
			columns.add(column);
		}		
		return columns;		
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<CustomReportColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<CustomReportColumn> columns) {
		this.columns = columns;
	}

	public MetaModel getMetaModel() {
		return metaModel;
	}

	public void setMetaModel(MetaModel metaModel) {
		this.metaModel = metaModel;
	}
		
}
