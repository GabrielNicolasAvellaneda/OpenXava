package org.openxava.test.model

import javax.persistence.*;

import org.openxava.annotations.*;

/**
 * 
 * @author Jeromy Altuna
 */
@Entity
class UserWithNickname extends Nameable {
	
	@OneToOne @ReferenceView("OnlyNickname")
	@AsEmbedded @NoFrame
	Nickname nickname
}
