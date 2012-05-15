package org.openxava.tab.impl;

import java.rmi.*;
import java.util.*;
import javax.ejb.*;
import org.apache.commons.logging.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;

/**
 * 
 * @author Javier Paniza 
 */
abstract public class TabProviderBase implements ITabProvider, java.io.Serializable {
	
	private static Log log = LogFactory.getLog(TabProviderBase.class);
	private static final int DEFAULT_CHUNK_SIZE = 50;	

	private String select; // Select ... from ...
	private String selectSize;
	private Object[] key;
	private int chunkSize = DEFAULT_CHUNK_SIZE;
	private int current;  
	private boolean eof = true;
	private MetaModel metaModel;
	
	
	abstract protected String translateCondition(String condition);
	abstract protected Number executeNumberSelect(String select, String errorId);
	
	public void setMetaModel(MetaModel metaModel) {
		this.metaModel = metaModel;
	}
	
	protected MetaModel getMetaModel() {
		return metaModel;
	}
		
	public void search(String condition, Object key) throws FinderException, RemoteException {		
		current = 0;
		eof = false;
		this.key = toArray(key);					
		condition = condition == null ? "" : condition.trim(); 
		select = translateCondition(condition);
		selectSize = createSizeSelect(select);		
	}
						
	/** Size of chunk returned by {@link #nextChunk}. */
	public int getChunkSize() {
		return chunkSize;
	}
	
				
	/** Size of chunk returned by {@link #nextChunk}. */
	public void setChunkSize(int chunkSize) {
		this.chunkSize = chunkSize;
	}
	

	protected boolean keyHasNulls() {
		if (key == null) return true;
		for (int i=0; i < key.length; i++) {
			if (key[i] == null) return true;
		}
		return false;
	}
	
	protected Object[] getKey() {
		return key;
	}
	
	/**
	 * Return an array from the sent object.
	 * Si obj == null return Object[0]
	 */
	private Object[] toArray(Object obj) { 
		if (obj == null)
			return new Object[0];
		if (obj instanceof Object[]) {
			return (Object[]) obj;
		}
		else {
			Object[] rs = { obj };
			return rs;
		}
	}
	public int getCurrent() {
		return current;
	}

	public void setCurrent(int i) {
		current = i;
	}
	public int getResultSize() throws RemoteException { 
		return executeNumberSelect(this.selectSize, "tab_result_size_error").intValue();
	}
	
	public Number getSum(String column) { 
		return executeNumberSelect(createSumSelect(column), "column_sum_error"); 		
	}		
	
	private String createSizeSelect(String select) {
		if (select == null) return null;		
		String selectUpperCase = Strings.changeSeparatorsBySpaces(select.toUpperCase());
		int iniFrom = selectUpperCase.indexOf(" FROM ");
		int end = selectUpperCase.indexOf("ORDER BY ");
		StringBuffer sb = new StringBuffer("SELECT COUNT(*) ");
		if (end < 0) sb.append(select.substring(iniFrom));
		else sb.append(select.substring(iniFrom, end - 1));
		return sb.toString();
	}
	
	private String createSumSelect(String column) { 
		if (select == null) return null;		
		String selectUpperCase = Strings.changeSeparatorsBySpaces(select.toUpperCase());
		int iniFrom = selectUpperCase.indexOf(" FROM ");
		int end = selectUpperCase.indexOf("ORDER BY ");
		StringBuffer sb = new StringBuffer("SELECT SUM(");
		sb.append(column); 
		sb.append(") ");
		if (end < 0) sb.append(select.substring(iniFrom));
		else sb.append(select.substring(iniFrom, end - 1));
		return sb.toString();
	}
	
	public void reset() throws RemoteException {
		current = 0;
		eof = false;
	}
	
	protected String getSelect() {
		return select;
	}
	
	protected boolean isEOF() {
		return eof;
	}
	
	protected void setEOF(boolean eof) {
		this.eof = eof;
	}

}
