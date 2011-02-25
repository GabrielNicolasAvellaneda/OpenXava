package org.openxava.test.model

import javax.persistence.*;
import org.openxava.annotations.*;

@Entity 
@Tab(properties="id, name, assured.name") 
public class Deal { 
	
	@Id
	Long id
	
	@Column(length=40)
	String name
	
	@OneToOne 
	@PrimaryKeyJoinColumn
	DealAssured assured 
	
} 