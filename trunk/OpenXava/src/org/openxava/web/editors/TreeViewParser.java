package org.openxava.web.editors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.annotations.Tree;
import org.openxava.model.MapFacade;
import org.openxava.model.meta.MetaProperty;
import org.openxava.tab.Tab;
import org.openxava.tab.impl.IXTableModel;
import org.openxava.util.*;
import org.openxava.view.View;
import org.openxava.view.meta.MetaCollectionView;
import org.openxava.view.meta.MetaView;
import org.openxava.web.WebEditors;
import org.openxava.web.style.Style;

/**
 * Parse the tree view.
 * 
 * @author Federico Alcantara
 */
public class TreeViewParser {
	private static Log log = LogFactory.getLog(TreeViewParser.class);
	public static final String XAVA_TREE_VIEW_PARSER = "xava_treeViewParser";
	public static final String XAVA_TREE_VIEW_NODE_FULL_PATH = "xava_treeViewNodeFullPath";
	
	private Tab tab;
	private String viewObject;
	private Style style;
	private Messages errors;
	private TreeView metaTreeView;
	private class TreeNodeHolder{
		public TreeNodeHolder(Object treeNode, int index) {
			this.treeNode = treeNode;
			this.index = index;
			this.rendered = false;
		}
		private Object treeNode;
		private int index;
		private boolean rendered;
	}
	private Map<String, List<TreeNodeHolder>> groups;
	private Tree treePath;
	private Object parentObject;
	private String collectionName;
	private Map<String, TreeView> metaTreeViews;
	
	public TreeViewParser(){
	}
	
	
	@SuppressWarnings("unchecked")
	public void createMetaTreeView(Tab tab, String viewObject, String collectionName, Style style, Messages errors)
			throws Exception {
		this.tab = tab;
		this.viewObject = viewObject;
		this.style = style;
		this.collectionName = collectionName;
		Object treeNode;
		if (tab.getTableModel().getTotalSize() > 0) {
			// check if we have a previous metaTreeView
			metaTreeView = getMetaTreeView(tab.getModelName());
			if (metaTreeView == null) {
				// Initialize metaTreeView for further processing.
				treeNode = MapFacade.findEntity(tab.getModelName(), tab.getAllKeys()[0]);
				View collectionView = tab.getCollectionView();
				View parentView = collectionView.getParent();
				MetaView metaView = parentView.getMetaModel().getMetaView(parentView.getViewName());
				MetaCollectionView metaCollectionView = metaView.getMetaCollectionView(collectionName);
				// Find container
				Map keyValues = collectionView.getParent().getKeyValues();
				if (keyValues != null) {
					this.parentObject = MapFacade.findEntity(collectionView.getParent().getModelName(), keyValues);
					treePath = null;
					if (metaCollectionView != null) {
						treePath = metaCollectionView.getPath();
					}
			
					metaTreeView = new TreeView(
							treePath, treeNode, this.parentObject, collectionName);
					getMetaTreeViews().put(tab.getModelName(), metaTreeView);
					log.debug("Added metaTreeView for:" + tab.getModelName());
				}
			}
		}
	}

	public TreeView getMetaTreeView(String modelName) {
		return getMetaTreeViews().get(modelName);
	}
	
	public String parse(String modelName) throws Exception {
		String returnValue = "";
		metaTreeView = getMetaTreeView(modelName);
		if (metaTreeView != null) {
			parseGroups();
			for (String path : groups.keySet()) {
				if (!Is.empty(returnValue)) {
					returnValue = returnValue + ",";
				}
				returnValue = returnValue + parseTreeNode(path);
			}
			returnValue = ("new YAHOO.widget.TreeView('tree_" + collectionName +
					"',[" +
					returnValue + "]);");
		}
		return returnValue;
		
	}
		
	private void parseGroups() throws Exception {
		Object treeNode;
		String nodePath;
		groups = new TreeMap<String, List<TreeNodeHolder>>();
		List<TreeNodeHolder> nodesHolder;
		
		for (int index = 0; index < tab.getTableModel().getTotalSize(); index++) {
			treeNode = MapFacade.findEntity(tab.getModelName(), tab.getAllKeys()[index]);
			nodePath = metaTreeView.getNodePath(treeNode);
			nodesHolder = groups.get(nodePath);
			if (nodesHolder == null) {
				nodesHolder = new ArrayList<TreeNodeHolder>();
				groups.put(nodePath, nodesHolder);
			}
			nodesHolder.add(new TreeNodeHolder(treeNode, index));
		}

		
	}
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	private String parseTreeNode(String path) throws Exception {
		String returnValue = "";
		Object treeNode;
		boolean expandedState;
		String expanded="";
		String html="";
		String parsedChildren;
		String styleListCellSpacing = "border=\"1\" cellspacing=\"10\" cellpadding=\"10\"";
		List<TreeNodeHolder> nodesHolder;
		String tooltip = XavaResources.getString("double_click_to_edit_view");
		nodesHolder = groups.get(path);
		if (nodesHolder == null) {
			return "";
		}
		if (metaTreeView.isEntityObject()) {
			Collections.sort(nodesHolder, new Comparator(){
	
				public int compare(Object object1, Object object2) {
					Integer ord1 = metaTreeView.getNodeOrder(((TreeNodeHolder)object1).treeNode);
					Integer ord2 = metaTreeView.getNodeOrder(((TreeNodeHolder)object2).treeNode);
					return ord1.compareTo(ord2);
				}});
		}
		
		for (TreeNodeHolder nodeHolder : nodesHolder) {
			if (!nodeHolder.rendered) {
				nodeHolder.rendered = true;
				IXTableModel model = tab.getTableModel();
				int index = nodeHolder.index;
				HttpServletRequest request = tab.getRequest();
				treeNode = MapFacade.findEntity(tab.getModelName(), tab.getAllKeys()[index]);
				html = "";
				if (model.getColumnCount() > 1) {
					html = "<table class=\"" + style.getList() + "\" " +
						"width=\"100%\" " + styleListCellSpacing + " style=\"" + style.getListStyle() + "\" " +
								"title=\"" + tooltip + "\"> <tr>";
					for (int c=0; c<model.getColumnCount(); c++) {
						MetaProperty p = tab.getMetaProperty(c);
						String align =p.isNumber() && !p.hasValidValues()?"vertical-align: middle;text-align: right; ":"vertical-align: middle; ";
						String cellStyle = align + style.getListCellStyle();
						String fvalue = null;
						if (p.hasValidValues()) {
							fvalue = p.getValidValueLabel(request, model.getValueAt(index, c));
						}
						else {
							fvalue = WebEditors.format(request, p, model.getValueAt(index, c), errors, viewObject, true);
						}
						html = html + "<td class=\"" + (c%2==0?style.getListPairCell():style.getListOddCell()) + "\" " +
								"style=\"" + cellStyle +"\">" + fvalue +"</td"; 
					}
					html = html + "</tr></table>";
				} else {
					if (model.getColumnCount() == 1) {
						MetaProperty p = tab.getMetaProperty(0);
						String fvalue = null;
						if (p.hasValidValues()) {
							fvalue = p.getValidValueLabel(request, model.getValueAt(index, 0));
						}
						else {
							fvalue = WebEditors.format(request, p, model.getValueAt(index, 0), errors, viewObject, true);
						}
						html = "&nbsp;<span title=\"" + tooltip +
								"\">" + fvalue + "</span>";
					}
				}
				expandedState = metaTreeView.getNodeExpandedState(treeNode);
				expanded = expandedState?",expanded:true":"";
				parsedChildren = parseTreeNode(metaTreeView.getNodeFullPath(treeNode));
				if (!Is.empty(parsedChildren)) {
					parsedChildren = ",children:[" + parsedChildren + "]";
				}
				if (!Is.empty(returnValue)) {
					returnValue = returnValue + ",";
				}
				returnValue = returnValue + "{type:'html',editable:true, " +
						"data:\"" + index +"\", " +
				"html:'" + html + "'" + expanded +
				parsedChildren + "}";
			} else {
				break; // if the first one has been rendered, the rest had been too.
			}
		}
		return returnValue;
	}

	public Map<String, TreeView> getMetaTreeViews() {
		if (metaTreeViews == null) {
			metaTreeViews = new HashMap<String, TreeView>();
		}
		return metaTreeViews;
	}
}
