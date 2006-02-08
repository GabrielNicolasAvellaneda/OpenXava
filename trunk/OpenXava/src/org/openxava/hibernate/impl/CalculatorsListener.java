package org.openxava.hibernate.impl;

import java.util.*;

import org.hibernate.*;
import org.hibernate.event.*;
import org.openxava.calculators.*;
import org.openxava.model.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;
import org.openxava.util.meta.*;

public class CalculatorsListener implements PreInsertEventListener, PreUpdateEventListener {
	
	private static CalculatorsListener instance = new CalculatorsListener();
	
	public static CalculatorsListener getInstance() {
		return instance;
	}
	
	private CalculatorsListener() {		
	}

	public boolean onPreInsert(PreInsertEvent ev) {
		executeCalculators(ev.getEntity(), true);
		return false;
	}

	public boolean onPreUpdate(PreUpdateEvent ev) {
		executeCalculators(ev.getEntity(), false);
		return false;
	}
	
	private void executeCalculators(Object entity, boolean create) {
		String modelName = "unknow";		
		try {
			IModel model = (IModel) entity;
			MetaModel metaModel = model.getMetaModel();
			Collection calculators = create?metaModel.getMetaCalculatorsPostCreate():metaModel.getMetaCalculatorsPostModify();
			if (calculators.isEmpty()) return;
			modelName = metaModel.getName();
			PropertiesManager pm = new PropertiesManager(model);			 
			for (Iterator it = calculators.iterator(); it.hasNext();) {
				MetaCalculator metaCalculator = (MetaCalculator) it.next();
				ICalculator calculator = metaCalculator.createCalculator();
				PropertiesManager pmCalculator = new PropertiesManager(calculator);
				for (Iterator itSets=metaCalculator.getMetaSetsWithoutValue().iterator(); itSets.hasNext();) {
					MetaSet set = (MetaSet) itSets.next();
					pmCalculator.executeSet(set.getPropertyName(), pm.executeGet(set.getPropertyNameFrom()));
				}
				if (calculator instanceof IJDBCCalculator) {
					((IJDBCCalculator) calculator).setConnectionProvider(DataSourceConnectionProvider.getByComponent(metaModel.getMetaComponent().getName()));
				}
				if (calculator instanceof IEntityCalculator) {
					((IEntityCalculator) calculator).setEntity(model);
				}				 
				calculator.calculate();				 								
			}			
		}
		catch (Exception ex) {
			ex.printStackTrace();
			String message = create?"entity_create_error":"entity_modify_error";
			throw new HibernateException(XavaResources.getString(message, modelName, ex.getLocalizedMessage()));
		}				
	}


}

