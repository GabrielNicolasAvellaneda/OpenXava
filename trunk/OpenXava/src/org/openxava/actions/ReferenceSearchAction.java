package org.openxava.actions;

import java.util.*;



import org.openxava.mapping.*;
import org.openxava.model.meta.*;
import org.openxava.tab.*;
import org.openxava.util.*;
import org.openxava.view.*;

/**
 * @author Javier Paniza
 */

public class ReferenceSearchAction extends ReferenceBaseAction implements INavigationAction {
	
			
	private Tab tab;	
	private String tabName = "";
	private String currentReferenceLabel;
	private String nextController = "ReferenceSearch"; // If you change the default value change setter and getter doc too
	
	
	public void execute() throws Exception {		
		super.execute();
		Tab tab = new Tab();
		tab.setRequest(getTab().getRequest());
		setTab(tab);
				
		View subview = getViewInfo().getView();
		MetaModel metaRootModel = getViewInfo().getParent().getMetaModel();		
		getTab().setModelName(subview.getModelName());
		MetaReference ref = getMetaReference(metaRootModel, getViewInfo().getMemberName());
		tab.setTabName(tabName);
		
		ModelMapping rootMapping = null;
		try {
			rootMapping = metaRootModel.getMapping();
		}
		catch (XavaException ex) {			
			// In case of an aggregate without mapping
		}
		if (rootMapping != null && metaRootModel.containsMetaReference(ref.getName()) && // because maybe a collection of references 
			rootMapping.isReferenceOverlappingWithSomeProperty(ref.getName())) {
			StringBuffer condition = new StringBuffer();			
			Iterator itOverlappingProperties = rootMapping.getOverlappingPropertiesOfReference(ref.getName()).iterator();			
			while (itOverlappingProperties.hasNext()) {
				String referenceProperty = (String) itOverlappingProperties.next();
				String overlaped = rootMapping.getOverlappingPropertyForReference(ref.getName(), referenceProperty);
				condition.append("${");
				condition.append(referenceProperty);
				condition.append("} = ");
				boolean numeric = ref.getMetaModelReferenced().getMetaProperty(referenceProperty).isNumber(); 
				if (!numeric) condition.append("'"); 
				condition.append(getView().getValue(overlaped));
				if (!numeric) condition.append("'"); 
				if (itOverlappingProperties.hasNext()) {
					condition.append(" AND "); 
				}
			}					
			getTab().setBaseCondition(condition.toString());
		}
		else {
			getTab().setBaseCondition(null);
		}
					
		setCurrentReferenceLabel(ref.getLabel());	 
	}

	private MetaReference getMetaReference(MetaModel metaRootModel, String referenceName) throws XavaException {
		try {
			return metaRootModel.getMetaReference(referenceName);
		}
		catch (ElementNotFoundException ex) {
			return metaRootModel.getMetaCollection(referenceName).getMetaReference();
		}		
	}
	
	public String[] getNextControllers() {		
		return new String[]{ getNextController() }; 
	}

	public String getCustomView() {		
		return "xava/referenceSearch.jsp?rowAction="+ getNextController() + ".choose";
	}

	public Tab getTab() {
		return tab;
	}

	public void setTab(Tab tab) {
		this.tab = tab;
	}

	public String getCurrentReferenceLabel() {
		return currentReferenceLabel;
	}

	public void setCurrentReferenceLabel(String string) {
		currentReferenceLabel = string;
	}

	/**
	 * By default "ReferenceSearch".
	 */
	public String getNextController() {
		return nextController;
	}
	
	/**
	 * By default "ReferenceSearch".
	 */
	public void setNextController(String nextController) {
		this.nextController = nextController;
	}

	public String getTabName() {
		return tabName;
	}

	public void setTabName(String tabName) {
		this.tabName = tabName;
	}


}
