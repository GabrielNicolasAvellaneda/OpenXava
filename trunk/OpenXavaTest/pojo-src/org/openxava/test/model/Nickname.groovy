package org.openxava.test.model

import javax.persistence.*;

import org.openxava.annotations.*;
import org.openxava.model.*

/**
 * To test @UniqueConstraint in @Table. These unique 
 * constraints that are to be placed on the table.
 * The name element of UniqueConstrainst is only indicative, 
 * because Hibernate does not create the constraint in the 
 * table, with the indicated name.We have mapped the 
 * constraint name manually in Table.
 * 
 * @author Jeromy Altuna
 */

@Entity
@Table(
	uniqueConstraints=[
			@UniqueConstraint(name="not_repeat_nickname", 
				columnNames=["nickname"])
])
class Nickname extends Identifiable {
	
	@Required
	String nickname
	
	@ManyToOne
	ApplicationUser user	
}
