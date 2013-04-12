package org.openxava.test.model

import javax.persistence.*;

import org.jboss.security.SecurityAssociationActions.GetSubjectAction;
import org.openxava.annotations.*;
import org.openxava.model.*;

@Entity
class Folder extends Nameable {

    @ManyToOne @DescriptionsList
    Folder parent // parent, not folder, to test a case

    @OneToMany(cascade=CascadeType.ALL, mappedBy="parent")
    Collection<Folder> subfolders

    @OneToMany(mappedBy="folder")
    Collection<File> files
	
	@PreRemove
	private void detachFiles() {
		files.each() { it.folder = null }
	}    

}