package org.openxava.session;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;

import org.openxava.actions.OnChangeMyChartLabelColumnAction;
import org.openxava.actions.OnChangeMyChartNameAction;
import org.openxava.actions.OnChangeMyChartTypeAction;
import org.openxava.annotations.AsEmbedded;
import org.openxava.annotations.DisplaySize;
import org.openxava.annotations.EditAction;
import org.openxava.annotations.Hidden;
import org.openxava.annotations.LabelFormat;
import org.openxava.annotations.LabelFormatType;
import org.openxava.annotations.ListProperties;
import org.openxava.annotations.NewAction;
import org.openxava.annotations.OnChange;
import org.openxava.annotations.ReadOnly;
import org.openxava.annotations.RemoveSelectedAction;
import org.openxava.annotations.Required;
import org.openxava.annotations.SaveAction;
import org.openxava.annotations.View;
import org.openxava.annotations.Views;
import org.openxava.model.meta.MetaModel;

@Views({
	@View(members = "name; myChartData;"
			+ "settings"
			+ "[myChartType, myChartLabelColumn;"
			+ " columns]"),
	@View(name = "SelectDatasets", members="myChartDatasetColumns")
})
	
public class MyChart implements Serializable {
	private static final long serialVersionUID = 1L;

	@DisplaySize(40)
	@Required
	@ReadOnly(forViews = "SelectDatasets")
	@OnChange(value = OnChangeMyChartNameAction.class)
	@Column(length = 80)
	private String name;
	
	@Hidden
	private MetaModel metaModel;
	
	@LabelFormat(LabelFormatType.NO_LABEL)
	private String myChartData;

	public enum MyChartType {
			BAR("bar", false),
			LINE("line", false),
			PIE("pie", false),
			DONUT("donut", false),
			STACKED_BAR("bar", true),
			AREA("area", false),
			STACKED_AREA("area", true),
			XY("scatter", false),
			SPLINE("spline", false),
			STEP("step", false),
			STACKED_STEP("area-step", true);
		String jsType;
		boolean grouped;
		private MyChartType(String jsType, boolean grouped) {
			this.jsType = jsType;
			this.grouped = grouped;
		}
		public String jsType() {
			return jsType;
		}
		public boolean grouped() {
			return grouped;
		}
	};
	@OnChange(value = OnChangeMyChartTypeAction.class)
	private MyChartType myChartType;
	
	@OnChange(value = OnChangeMyChartLabelColumnAction.class)
	private String myChartLabelColumn;

	@RemoveSelectedAction("MyChart.removeColumn")
	@AsEmbedded 
	@SaveAction("MyChart.saveColumn")
	@NewAction("MyChart.newColumn") 
	@EditAction("MyChart.editColumn")
	@ListProperties("displayed, label, comparator, value, order") 
	private List<MyChartColumn> columns;

	private Boolean shared;
	
	private boolean changed;
	
	private boolean nameEditable;
	
	public MyChart() {
		this.shared = false;
	}
	
	public String getName() {
		return name;
	}

	
	public void setName(String name) {
		this.name = name;
	}

	public MetaModel getMetaModel() {
		return metaModel;
	}

	public void setMetaModel(MetaModel metaModel) {
		this.metaModel = metaModel;
	}

	public String getMyChartData() {
		return myChartData;
	}

	public void setMyChartData(String data) {
		this.myChartData = data;
	}

	public MyChartType getMyChartType() {
		return myChartType;
	}

	public void setMyChartType(MyChartType chartType) {
		this.myChartType = chartType;
	}

	public String getMyChartLabelColumn() {
		return myChartLabelColumn;
	}


	public void setMyChartLabelColumn(String xAxis) {
		this.myChartLabelColumn = xAxis;
	}


	public List<MyChartColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<MyChartColumn> columns) {
		this.columns = columns;
	}

	public Boolean getShared() {
		return shared;
	}


	public void setShared(Boolean shared) {
		this.shared = shared;
	}
	
	public boolean isChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	public boolean isNameEditable() {
		return nameEditable;
	}

	public void setNameEditable(boolean editable) {
		this.nameEditable = editable;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MyChart [name=");
		builder.append(name);
		builder.append(", myChartType=");
		builder.append(myChartType);
		builder.append(", shared=");
		builder.append(shared);
		builder.append("]");
		return builder.toString();
	}
}
