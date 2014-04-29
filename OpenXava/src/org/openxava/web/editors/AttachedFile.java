package org.openxava.web.editors;

import javax.persistence.*;

import org.openxava.model.*;

/**
 * Class that allows to implement stereotypes "FILE" and "FILES_LIBRARY" <p>
 * 
 * It's a JPA entity <p>
 * 	
 * @author Jeromy Altuna
 */
@Entity
@Table(name="OXFILES")
public class AttachedFile extends Identifiable {
	
	private String name;	
	private byte[] data;
	
	@Column(length=32)
	private String libraryId;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	public String getLibraryId() {
		return libraryId;
	}
	public void setLibraryId(String libraryId) {
		this.libraryId = libraryId;
	}	
}
