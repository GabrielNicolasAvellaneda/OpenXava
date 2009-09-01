package org.openxava.invoicing.model;

import java.math.*;

import javax.persistence.*;

import org.openxava.annotations.*;
import org.openxava.invoicing.annotations.*;

@Entity 
@View(name="Simple", members="number, description")
public class Product {
	
	@Id @Column(length=9)
	private int number;
	
	@Column(length=50) @Required
	private String description;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@DescriptionsList
	private Author author;
		
	@ManyToOne(
		fetch=FetchType.LAZY,
		optional=false)
	@DescriptionsList
	private Category category;
	
	@Stereotype("MONEY")
	private BigDecimal price;
	
	@Column(length=13) @ISBN
	private String isbn; // tmp
	
	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}	
	
	
	@Stereotype("PHOTO") 
	private byte [] photo;
	
	
	@Stereotype("IMAGES_GALLERY") 
	@Column(length=32)
	private String morePhotos;
	
	
	@Stereotype("MEMO")
	private String remarks;
	
	// Getters and setters

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public String getMorePhotos() {
		return morePhotos;
	}

	public void setMorePhotos(String morePhotos) {
		this.morePhotos = morePhotos;
	}
	
	
	public void setAuthor(Author author) {
		this.author = author;
	}

	public Author getAuthor() {
		return author;
	}
	
			
}
