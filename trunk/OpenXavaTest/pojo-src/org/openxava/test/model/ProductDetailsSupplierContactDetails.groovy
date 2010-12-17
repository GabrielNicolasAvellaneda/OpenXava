package org.openxava.test.model

import javax.persistence.*;
import org.openxava.model.*;

/**
 * 
 * @author Javier Paniza
 */
@Entity
class ProductDetailsSupplierContactDetails extends Identifiable {
	
	@ManyToOne
	Product5 product
	
	@Column(length=40)
	String suplier

	@Column(length=100)	
	String details
	
}
