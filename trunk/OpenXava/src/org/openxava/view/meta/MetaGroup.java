package org.openxava.view.meta;

import org.apache.commons.lang3.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;

/**
 * @author Javier Paniza
 */
public class MetaGroup extends MetaMember {
	
	private String membersNames;
	private boolean alignedByColumns = false;
	private MetaView metaView;
	private MetaView metaViewParent;
			
	public MetaGroup(MetaView parent) {
		this.metaViewParent = parent;
	}
	
	public MetaView getMetaView() throws XavaException {
		if (metaView == null) {
			try {
				metaView = (MetaView) metaViewParent.clone();
				metaView.setAlignedByColumns(isAlignedByColumns()); 
				metaView.setMembersNames(membersNames);				
			} 
			catch (CloneNotSupportedException e) {
				throw new XavaException("group_view_error_no_clone");			
			}			
		}
		return metaView;				
	}

	public void setMembersNames(String members) {
		this.membersNames = members;		
	}

	public boolean isAlignedByColumns() {
		return alignedByColumns;
	}

	public void setAlignedByColumns(boolean alignedByColumns) {
		this.alignedByColumns = alignedByColumns;
	}
	
	/** 
	 * @throws  XavaException
	 * 			If {@code newName} is null, empty string or if {@code newName}
	 * 			contains blanks (\t, \r, \n) or whitespace
	 *  
	 * @since  5.1.1
	 */
	@Override
	public void setName(String newName) throws XavaException {
		if (Is.emptyString(newName) || StringUtils.containsAny(newName, "\t\r\n "))
		{
			throw new XavaException("group_name_not_allowed", newName);
		}
		super.setName(newName);
	}
}
