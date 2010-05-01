package org.openxava.view.meta;

import java.lang.reflect.Field;

import org.openxava.annotations.MoveNodeDownAction;
import org.openxava.annotations.MoveNodeLeftAction;
import org.openxava.annotations.MoveNodeRightAction;
import org.openxava.annotations.MoveNodeUpAction;
import org.openxava.model.MapFacade;
import org.openxava.tab.impl.IXTableModel;
import org.openxava.util.Is;
import org.openxava.view.View;

public class MetaTreeViewActions {
	private static final String UP_ACTION = "TreeView.up";
	private static final String DOWN_ACTION = "TreeView.down";
	private static final String LEFT_ACTION = "TreeView.left";
	private static final String RIGHT_ACTION = "TreeView.right";
	
	private String upAction = UP_ACTION;
	private String downAction = DOWN_ACTION;
	private String leftAction = LEFT_ACTION;
	private String rightAction = RIGHT_ACTION;
	private MetaTreeView metaTreeView;
	
	public MetaTreeViewActions() {
		
	}
	
	public MetaTreeViewActions(View view, MetaTreeView metaTreeView) throws Exception {
		this.metaTreeView = metaTreeView;
		parseActions(view);
	}
	
	public void parseActions(View view) throws Exception {
		IXTableModel model = view.getCollectionTab().getTableModel();
		// No actions for Up, Down, Left, Right if you don't have more than one node
		if (model.getTotalSize() > 1) {
			//Object treeNode = MapFacade.findEntity(view.getModelName(),(Map)model.getObjectAt(0));
			Object parent = MapFacade.findEntity(view.getRoot().getModelName(), 
					view.getRoot().getKeyValues());
			if (parent != null) {
				Field field = parent.getClass().getDeclaredField(view.getMemberName());
				if (metaTreeView.isOrderDefined()) {
					upAction = UP_ACTION;
					downAction = DOWN_ACTION;
				} else {
					upAction = "";
					downAction = "";
				}
				if (field != null) {
					// Up and Down default actions are suppressed if nodeOrder is not defined in @TreeView
					MoveNodeUpAction moveUpAction = field.getAnnotation(MoveNodeUpAction.class);
					MoveNodeDownAction moveDownAction = field.getAnnotation(MoveNodeDownAction.class);
					if (moveUpAction != null) {
						upAction = validateAction(upAction,
								moveUpAction.value(), view.getViewName(),
								moveUpAction.forViews(), moveUpAction.notForViews());
					}
					if (moveDownAction != null) {
						downAction = validateAction(downAction,
								moveUpAction.value(), view.getViewName(),
								moveUpAction.forViews(), moveUpAction.notForViews());
					}

					MoveNodeLeftAction moveLeftAction = field.getAnnotation(MoveNodeLeftAction.class);
					MoveNodeRightAction moveRightAction = field.getAnnotation(MoveNodeRightAction.class);
					if (moveLeftAction != null) {
						leftAction = validateAction(LEFT_ACTION, moveUpAction.value(), view.getViewName(),
								moveUpAction.forViews(), moveUpAction.notForViews());
					}
					if (moveRightAction != null) {
						rightAction = validateAction(RIGHT_ACTION, moveUpAction.value(), view.getViewName(),
								moveUpAction.forViews(), moveUpAction.notForViews());
					}
	
				}
			}
		} else {
			upAction = "";
			downAction = "";
			leftAction = "";
			rightAction = "";
		}
	}

	public String validateAction(String defaultAction, String newAction, String viewName, 
			String forViews, String notForViews) {
		String returnValue = defaultAction;
		if (Is.empty(forViews) && Is.empty(notForViews)) {
			returnValue = newAction;
		} else {
			if (isInList(forViews, viewName)) {
				returnValue = newAction;
			}
			if (isInList(notForViews, viewName)) {
				returnValue = defaultAction;
			}
		}
		return returnValue;
	}
	
	private boolean isInList(String commaList, String name) {
		boolean returnValue = false;
		if (!Is.empty(commaList)) {
			if (Is.empty(name)) {
				name = "DEFAULT";
			}
			String[] list = commaList.split(",");
			for (String listItem : list) {
				if (name.equals(listItem.trim())) {
					returnValue = true;
					break;
				}
			}
		}
		return returnValue;
	}
	/**
	 * @return the upAction
	 */
	public String getUpAction() {
		return upAction;
	}

	/**
	 * @param upAction the upAction to set
	 */
	public void setUpAction(String upAction) {
		this.upAction = upAction;
	}

	/**
	 * @return the downAction
	 */
	public String getDownAction() {
		return downAction;
	}

	/**
	 * @param downAction the downAction to set
	 */
	public void setDownAction(String downAction) {
		this.downAction = downAction;
	}

	/**
	 * @return the leftAction
	 */
	public String getLeftAction() {
		return leftAction;
	}

	/**
	 * @param leftAction the leftAction to set
	 */
	public void setLeftAction(String leftAction) {
		this.leftAction = leftAction;
	}

	/**
	 * @return the rightAction
	 */
	public String getRightAction() {
		return rightAction;
	}

	/**
	 * @param rightAction the rightAction to set
	 */
	public void setRightAction(String rightAction) {
		this.rightAction = rightAction;
	}
	
}
