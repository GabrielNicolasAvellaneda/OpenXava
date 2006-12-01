package org.openxava.tab.impl;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.util.*;

/**
 * A chunk of data. <p>
 * 
 * Simply is a collection with a mark with is
 * the last.
 * Util for obtain a big amount of data little by little
 * from server.
 *
 * @author  Javier Paniza
 */

public class DataChunk implements java.io.Serializable {

	private List data;
	private boolean last;
	private int indexNext;
	
	private Log log = LogFactory.getLog(DataChunk.class);

	public DataChunk(List data, boolean last, int indexNext) {
		Assert.arg(data);
		this.data = data;
		this.last = last;
		this.indexNext = indexNext;
	}

	public List getData() {
		return data;
	}

	
	public boolean isLast() {
		return last;
	}

	public int getIndexNext() {
		return indexNext;
	}

}
