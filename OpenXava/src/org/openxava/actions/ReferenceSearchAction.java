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

public class ReferenceSearchAction extends ViewBaseAction implements INavigationAction {
	
	private class ViewInfo {
		
		View view;		
		String memberName;
		View parent;
		
		ViewInfo(View view, String memberName, View parent) {
			this.view = view;
			this.memberName = memberName;
			this.parent = parent;
		}
		
	}
	
	private String keyProperty;
	private View referenceSubview;		
	private Tab tab;	
	private String currentReferenceLabel;
	private String nextController = "ReferenceSearch"; // If you change the default value change setter and getter doc too
	
	public void execute() throws Exception {
		Tab tab = new Tab();
		tab.setRequest(getTab().getRequest());
		setTab(tab);
		
		ViewInfo viewInfo = getSubview(getView(), createMemberName());
		View subview = viewInfo.view;
		MetaModel metaRootModel = viewInfo.parent.getMetaModel();		
		getTab().setModelName(subview.getModelName());
		MetaReference ref = getMetaReference(metaRootModel, viewInfo.memberName);
		
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
				condition.append(getView().getValue(overlaped));				
				if (itOverlappingProperties.hasNext()) {
					condition.append(" AND "); 
				}
			}					
			getTab().setBaseCondition(condition.toString());
		}
		else {
			getTab().setBaseCondition(null);
		}
		
		setReferenceSubview(subview);			
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
	
	private ViewInfo getSubview(View view, String memberName) throws XavaException {
		if (memberName.indexOf('.') < 0) {
			return new ViewInfo(view.getSubview(memberName), memberName, view); 
		}
		StringTokenizer st = new StringTokenizer(memberName, ".");
		String subviewName = st.nextToken();
		String nextMember = st.nextToken(); 
		return getSubview(view.getSubview(subviewName), nextMember);
	}
	
	private String createMemberName() {		
		String prefix = "xava." + getModelName() + ".";		
		String propertyName = keyProperty.substring(prefix.length());				
		int idx = propertyName.lastIndexOf(".");		
		if (idx >= 0) return propertyName.substring(0, idx); 	
		return propertyName;
	}

	public String getKeyProperty() {
		return keyProperty;
	}

	public void setKeyProperty(String string) {
		keyProperty = string;		
	}

	public String[] getNextControllers() {		
		return new String[]{ getNextController() }; 
	}

	public String getCustomView() {		
		return "xava/referenceSearch";
	}

	public Tab getTab() {
		return tab;
	}

	public void setTab(Tab tab) {
		this.tab = tab;
	}

	public View getReferenceSubview() {
		return referenceSubview;
	}

	public void setReferenceSubview(View view) {
		referenceSubview = view;
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

}
