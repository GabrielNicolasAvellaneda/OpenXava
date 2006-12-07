package org.openxava.util;

import java.io.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Some utilities for work with files and directories. <p>
 * 
 * @author Javier Paniza
 */

public class Files {
	
	private static Log log = LogFactory.getLog(Files.class);
	
	public static boolean deleteDir(String dirURL) {
        return deleteDir(new File(dirURL));
    }	
		
	public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean deleted = deleteDir(new File(dir, children[i]));
                if (!deleted) {
                    return false;
                }
            }
        }    
        return dir.delete();
    }	

}
