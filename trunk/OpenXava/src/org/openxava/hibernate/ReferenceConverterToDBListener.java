package org.openxava.hibernate;

import java.util.*;
import org.hibernate.*;
import org.hibernate.event.*;
import org.openxava.converters.*;
import org.openxava.mapping.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;

public class ReferenceConverterToDBListener implements PreInsertEventListener {

	public boolean onPreInsert(PreInsertEvent ev) {
		String currentReference = "";
		try {
			MetaModel metaModel = MetaModel.getByPOJOClass(ev.getEntity().getClass());  // tmp Rescribir usando IModel
			ModelMapping mapping = metaModel.getMapping();
			if (!mapping.hasReferenceConverters()) return false;
			List propertyNames = Arrays.asList(ev.getPersister().getPropertyNames());
			Collection referenceMappings =  mapping.getReferenceMappingsWithConverter();
			Iterator it = referenceMappings.iterator();
			while (it.hasNext()) {
				ReferenceMapping referenceMapping = (ReferenceMapping) it.next();
				PropertiesManager pm = new PropertiesManager(ev.getEntity());
				Object referencedObject = pm.executeGet(referenceMapping.getReference());
				MetaReference metaReference = metaModel.getMetaReference(referenceMapping.getReference());
				currentReference = metaReference.getName();
				if (referencedObject == null) {
					referencedObject = metaReference.getMetaModelReferenced().getPOJOClass().newInstance(); 
				}
			
				Collection detailMappings = referenceMapping.getDetails();
				Iterator itd = detailMappings.iterator();
				while (itd.hasNext()) {
					ReferenceMappingDetail referenceMappingDetail = (ReferenceMappingDetail) itd.next();
					if (referenceMappingDetail.hasConverter()) {
						IConverter conv = referenceMappingDetail.getConverter();
						PropertiesManager pm2 = new PropertiesManager(referencedObject);
						Object propertyValue = pm2.executeGet(referenceMappingDetail.getReferencedModelProperty());
						pm2.executeSet(referenceMappingDetail.getReferencedModelProperty(),conv.toDB(propertyValue));
					}
				}
				int i = propertyNames.indexOf(metaReference.getName());
				ev.getState()[i]=referencedObject;
			}
		} 
		catch(Exception e){ 
			e.printStackTrace();
			throw new HibernateException(XavaResources.getString("generator.conversion_error", currentReference, ev.getEntity().getClass(), ""));
		}
		 				
		return false; 
	}

}
