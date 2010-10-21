package org.openxava.test.model

import org.openxava.annotations.*;

import javax.persistence.*;

/**
 * 
 * @author Javier Paniza 
 */

@Entity
class Orphanage extends Nameable {

	@OneToMany(orphanRemoval=true, mappedBy="orphanage")
	@AsEmbedded
	Collection<Orphan> orphans;
	
}
