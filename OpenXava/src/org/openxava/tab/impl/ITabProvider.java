package org.openxava.tab.impl;

import org.openxava.model.meta.*;

/**
 * Provides data and specific behavior from the underlying query technology. <p>
 * 
 * @author  Javier Paniza
 */

public interface ITabProvider extends ISearch, IDataReader {
	
	void setMetaModel(MetaModel metaModel); 
	
	Number getSum(String column);  
  
	String translateSelect(String select); 
  
	void setChunkSize(int chunkSize); 
  
	String toQueryField(String propertyName); 
  
	void setCurrent(int index); 
  
}
