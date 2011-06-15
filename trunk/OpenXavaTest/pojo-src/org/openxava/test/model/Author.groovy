package org.openxava.test.model

import javax.persistence.*;

import org.openxava.annotations.*;

@Entity
class Author {
	
	@Id @Column(length=30)
	String author // The same name of the reference used from Book: for testing a bug
	
	@Stereotype("MEMO")
	String biography;

	@OneToMany(mappedBy="favoriteAuthor")
	@CollectionView(value="WithGroup")
	Collection<Human> humans
	
}
