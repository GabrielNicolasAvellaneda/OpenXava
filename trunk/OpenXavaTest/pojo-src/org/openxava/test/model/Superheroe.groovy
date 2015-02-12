package org.openxava.test.model

import org.openxava.annotations.*;
import org.openxava.model.*;

import javax.persistence.*;

@Entity
@Table(name="SUPERS")
class Superheroe extends Identifiable {

	@Stereotype("NO_HTML_IN_LIST")
	String name
	
}
