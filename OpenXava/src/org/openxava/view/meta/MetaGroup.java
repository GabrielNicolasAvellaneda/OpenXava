package org.openxava.view.meta;

import org.openxava.model.meta.*;
import org.openxava.util.*;

/**
 * @author Javier Paniza
 */
public class MetaGroup extends MetaMember {
	
	private String membersNames;

	private MetaView metaView;
	private MetaView metaViewParent;
	
	public MetaGroup(MetaView padre) {
		this.metaViewParent = padre;
	}
	
	public MetaView getMetaView() throws XavaException {
		if (metaView == null) {
			try {
				metaView = (MetaView) metaViewParent.clone();
				metaView.setMembersNames(membersNames);
			} 
			catch (CloneNotSupportedException e) {
				throw new XavaException("group_view_error_no_clone");			
			}			
		}
		return metaView;				
	}

	public void setMembersNames(String miembros) {
		this.membersNames = miembros;		
	}
		
}
