package org.openxava.mapping;

import java.io.*;

/**
 * @author Javier Paniza
 */
public class CmpField implements Serializable {
	
	private String converterPropertyName;
	private String column;
	private String cmpTypeName; 

	public String getColumn() {
		return column;
	}

	public String getConverterPropertyName() {
		return converterPropertyName;
	}

	public String getCmpTypeName() {
		return cmpTypeName;
	}

	public void setColumn(String string) {		
		column = string;
	}

	public void setConverterPropertyName(String string) {
		converterPropertyName = string;
	}

	public void setCmpTypeName(String string) {		
		cmpTypeName = string;
	}

}
