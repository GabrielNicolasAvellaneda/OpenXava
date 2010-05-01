package org.openxava.test.model;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.openxava.annotations.Editor;
import org.openxava.annotations.Hidden;
import org.openxava.annotations.ListProperties;
import org.openxava.annotations.Path;
import org.openxava.annotations.View;
import org.openxava.annotations.Views;

@Entity
@Views({
	@View(members="description; treeItems; treeItemTwos"),
	@View(name="Simple", members="description; treeItems"),
	@View(name="NoDefaultPath", members="description; treeItemTwos")
})
public class TreeContainer {
	@Id @Hidden
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length=30)
	private String description;

	@OneToMany(mappedBy="parentContainer", cascade = CascadeType.REMOVE)
	@Editor(value="TreeView")
	@ListProperties("description")
	@OrderBy("path, treeOrder")
	private Collection<TreeItem> treeItems;
	
	@OneToMany(mappedBy="parentContainer", cascade = CascadeType.REMOVE)
	@Editor(value="TreeView")
	@ListProperties("description")
	@OrderBy("folder, treeOrder")
	@Path(pathProperty="folder")
	private Collection<TreeItemTwo> treeItemTwos;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Collection<TreeItem> getTreeItems() {
		return treeItems;
	}

	public void setTreeItems(Collection<TreeItem> treeItems) {
		this.treeItems = treeItems;
	}

	public void setTreeItemTwos(Collection<TreeItemTwo> treeItemTwos) {
		this.treeItemTwos = treeItemTwos;
	}

	public Collection<TreeItemTwo> getTreeItemTwos() {
		return treeItemTwos;
	}
	
}