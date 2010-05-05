package org.openxava.test.model;

import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;
import org.openxava.jpa.*;
import org.openxava.test.actions.OnChangeGroupInColorAction;

/**
 * Number (the key) can be 0
 *  
 * @author Javier Paniza
 */

@Entity
@Views({
	@View( name="Ordinary", members="number; name; sample; hexValue; usedTo, characteristicThing"),
	@View( name="Ordinary2", members="number; name; sample; hexValue; usedTo, characteristicThing"),	
	@View( name="View1", members="property1"), 
	@View( name="View2", members="property2"), 
	@View( name="View2Sub1", members="property2Sub1"), 
	@View( name="View2Sub2", members="property2Sub2"),
	@View( name="Groups", members="group; group1[property1], group2[property2]")
})
@Tabs({
	@Tab( properties = "number, name, hexValue, sample, usedTo.name, characteristicThing.description"),
	@Tab( name="Color2", properties = "number, name, hexValue, sample, usedTo.name, characteristicThing.description")	
})
public class Color {

	@Id @Column(length=5)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer number;
	
	@Column(length=20) @Required
	private String name;
	
	@Version 
	private Integer version;
	
	@Column(length=6)
	private String hexValue; 
	
	@Stereotype("IMAGE_LABEL")
	public String getSample() {
		if ("red".equalsIgnoreCase(name) ||
			"rojo".equalsIgnoreCase(name)) return "RED";
		if ("black".equalsIgnoreCase(name) ||
			"negro".equalsIgnoreCase(name)) return "BLACK";
		return "nocolor";		
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@DescriptionsList
	@JoinColumn(name="IDTHING")
	private Thing usedTo;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@DescriptionsLists({
		@DescriptionsList(
			condition="${thing.number} = ?", 
			depends="this.usedTo"),
		@DescriptionsList(
			forViews="Ordinary2", 
			condition="${number} < 2",
			forTabs="Color2")
	})
	@LabelFormat(LabelFormatType.NO_LABEL)
	private CharacteristicThing characteristicThing;
	
	@Transient
	private String property1;
	
	@Transient
	private String property2;
	
	@Transient
	private String property2Sub1;
	
	@Transient
	private String property2Sub2;
	
	@Transient
	@OnChange(forViews="Groups", value=OnChangeGroupInColorAction.class) 
	private Group group;
	public enum Group { GROUP1, GROUP2 }
	
	
	public static Collection<Color> findAll() {
		Query query = XPersistence.getManager().createQuery("from Color");
		return query.getResultList();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getProperty1() {
		return property1;
	}

	public void setProperty1(String property1) {
		this.property1 = property1;
	}

	public String getProperty2() {
		return property2;
	}

	public void setProperty2(String property2) {
		this.property2 = property2;
	}

	public String getProperty2Sub1() {
		return property2Sub1;
	}

	public void setProperty2Sub1(String property2Sub1) {
		this.property2Sub1 = property2Sub1;
	}

	public String getProperty2Sub2() {
		return property2Sub2;
	}

	public void setProperty2Sub2(String property2Sub2) {
		this.property2Sub2 = property2Sub2;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public String getHexValue() {
		return hexValue;
	}

	public void setHexValue(String hexValue) {
		this.hexValue = hexValue;
	}

	public Thing getUsedTo() {
		return usedTo;
	}

	public void setUsedTo(Thing usedTo) {
		this.usedTo = usedTo;
	}

	public CharacteristicThing getCharacteristicThing() {
		return characteristicThing;
	}

	public void setCharacteristicThing(CharacteristicThing characteristicThing) {
		this.characteristicThing = characteristicThing;
	}

}
