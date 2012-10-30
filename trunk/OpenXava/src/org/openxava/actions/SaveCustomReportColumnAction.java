package org.openxava.actions;

import javax.inject.*;

import org.openxava.model.inner.*;

/**
 * tmp ¿Podría se genérica para cualquier modelo transitorio?
 * 
 * @author Javier Paniza 
 */

public class SaveCustomReportColumnAction extends CollectionElementViewBaseAction {
	
	@Inject
	private CustomReport customReport; 

	public void execute() throws Exception {
		CustomReportColumn column = new CustomReportColumn();
		// tmp Molaría que column se rellenara automáticamente, echar un vistazo para ver si es muy complicado
		String columnName = getCollectionElementView().getValueString("columnName");
		column.setColumnName(columnName);
		String comparator = getCollectionElementView().getValueString("comparator");
		column.setComparator(comparator);
		String value = getCollectionElementView().getValueString("value");
		column.setValue(value);		
		if (getCollectionElementView().getCollectionEditingRow() < 0) {
			customReport.getColumns().add(column);
			addMessage("Columna añadida"); // tmp i18n
		}
		else {
			customReport.getColumns().set(getCollectionElementView().getCollectionEditingRow(), column);
			addMessage("Columna modificada"); // tmp i18n			
		}
		closeDialog();
	}

}
