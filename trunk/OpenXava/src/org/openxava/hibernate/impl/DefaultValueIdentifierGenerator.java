package org.openxava.hibernate.impl;

import java.io.*;
import java.util.*;

import org.hibernate.*;
import org.hibernate.dialect.*;
import org.hibernate.engine.*;
import org.hibernate.id.*;
import org.hibernate.type.*;
import org.openxava.calculators.*;
import org.openxava.model.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;
import org.openxava.util.meta.*;

/**
 * Executes the default-value-calculator for key properties. <p> 
 * 
 * @author Javier Paniza
 */

public class DefaultValueIdentifierGenerator implements IdentifierGenerator, Configurable {
	
	private String property;

	public Serializable generate(SessionImplementor implementor, Object object)	throws HibernateException {
		String modelName = "unknow";
		try {			
			IModel model = (IModel) object;
			IMetaModel metaModel = model.getMetaModel();
			modelName = metaModel.getName();
			MetaProperty pr = (MetaProperty) metaModel.getMetaProperty(getProperty()); 
			PropertiesManager pm = new PropertiesManager(model);
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
			return (Serializable) calculator.calculate();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new HibernateException(XavaResources.getString("entity_create_error", modelName, ex.getLocalizedMessage()));
		}
	}

	public String getProperty() {
		return property;
	}

	public void configure(Type type, Properties params, Dialect dialect) throws MappingException {
		property = params.getProperty("property");	
	}

}
