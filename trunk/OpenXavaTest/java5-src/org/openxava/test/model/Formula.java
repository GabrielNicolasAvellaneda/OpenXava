package org.openxava.test.model;

import java.util.*;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.*;

/**
 * 
 * @author Javier Paniza
 */

@Entity
@Views({
	@View(members=
		"name;" +
		"selectedIngredientSize;" +
		"ingredients { ingredients }" +		
		"recipe { recipe }"
	),
	@View(name="Simple", members="name")
})
public class Formula {
	
	@Id @Hidden
	@GeneratedValue(generator="system-uuid") 
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	@Column(name="ID")	
	private String oid;
	
	@Column(length=40) @Required
	private String name;
	
	@Transient @ReadOnly 
	private Integer selectedIngredientSize; 
	
	@OnSelectElementAction("Formula.onSelectIngredient") 
	@OneToMany(mappedBy="formula", cascade=CascadeType.REMOVE)
	private Collection<FormulaIngredient> ingredients;	
	
	@Stereotype("HTML_TEXT")
	private String recipe;

	public static Formula findByName(java.lang.String name) throws NoResultException {
		javax.persistence.Query query = org.openxava.jpa.XPersistence.getManager().createQuery("from Formula as o where o.name = :name"); 
		query.setParameter("name", name); 			
		return (Formula) query.getSingleResult();		  		
	}
	
	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Collection<FormulaIngredient> getIngredients() {
		return ingredients;
	}

	public void setIngredients(Collection<FormulaIngredient> ingredients) {
		this.ingredients = ingredients;
	}	

	public String getRecipe() {
		return recipe;
	}

	public void setRecipe(String recipe) {
		this.recipe = recipe;
	}
	
	public Integer getSelectedIngredientSize() {
		return selectedIngredientSize;
	}

	public void setSelectedIngredientSize(Integer selectedIngredientSize) {
		this.selectedIngredientSize = selectedIngredientSize;
	}	

}
