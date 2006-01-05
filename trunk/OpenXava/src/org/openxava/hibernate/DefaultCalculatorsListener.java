package org.openxava.hibernate;

import java.util.*;

import org.hibernate.*;
import org.hibernate.event.*;
import org.openxava.calculators.*;
import org.openxava.model.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;
import org.openxava.util.meta.*;

public class DefaultCalculatorsListener implements PreInsertEventListener {

	public boolean onPreInsert(PreInsertEvent ev) {		
		String modelName = "unknow"; 
		try {
			IModel model = (IModel) ev.getEntity();
			MetaModel metaModel = model.getMetaModel();
			modelName = metaModel.getName();
			PropertiesManager pm = new PropertiesManager(model);
			List propertyNames = Arrays.asList(ev.getPersister().getPropertyNames());
			Object [] state = ev.getState();
			for (Iterator itProperties=metaModel.getMetaPropertiesWithDefaultValueOnCreate().iterator(); itProperties.hasNext();) {
				MetaProperty pr = (MetaProperty) itProperties.next();
				MetaCalculator metaCalculator = pr.getMetaCalculatorDefaultValue();
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
				Object value = calculator.calculate();
				pm.executeSet(pr.getName(), value);
				int i = propertyNames.indexOf(pr.getName());
				state[i]=value;
			}				
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new HibernateException(XavaResources.getString("entity_create_error", modelName, ex.getLocalizedMessage()));
		}
		// tmp ini
		System.out.println("[DefaultCalculatorsListener.onPreInsert] state=" + Arrays.asList(ev.getState())); //  tmp
		System.out.println("[DefaultCalculatorsListener.onPreInsert] propertyNames=" + Arrays.asList(ev.getPersister().getPropertyNames())); //  tmp
		// tmp fin
		return false;
	}

}

