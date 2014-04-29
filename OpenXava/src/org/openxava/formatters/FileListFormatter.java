package org.openxava.formatters;

import javax.servlet.http.*;

import org.openxava.util.*;
import org.openxava.web.editors.*;

/**
 * For stereotypes FILE and ARCHIVO in list mode <p>
 * 
 * @author Jeromy Altuna
 */
public class FileListFormatter implements IFormatter {

	public String format(HttpServletRequest request, Object object) throws Exception {
		if(Is.empty(object)) return "";
		AttachedFile file = FilePersistorFactory.getInstance().find((String) object);
		if(file != null) {
			return String.format(
				"<a href='%s/xava/xfile?application=%s&module=%s&fileId=%s&dif=%d' target='_blank'>%s</a>",				
				request.getContextPath(), request.getParameter("application"), request.getParameter("module"), 
				file.getId(), System.currentTimeMillis(), file.getName());
		}
		return "";
	}

	public Object parse(HttpServletRequest request, String string) throws Exception {		
		return null;
	}

}
