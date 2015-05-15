package org.openxava.session;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;

import org.openxava.actions.OnChangeChartLabelColumnAction;
import org.openxava.actions.OnChangeChartNameAction;
import org.openxava.actions.OnChangeChartTypeAction;
import org.openxava.annotations.DisplaySize;
import org.openxava.annotations.Hidden;
import org.openxava.annotations.LabelFormat;
import org.openxava.annotations.LabelFormatType;
import org.openxava.annotations.ListProperties;
import org.openxava.annotations.OnChange;
import org.openxava.annotations.RemoveSelectedAction;
import org.openxava.annotations.Required;
import org.openxava.annotations.View;
import org.openxava.annotations.Views;
import org.openxava.model.meta.MetaModel;

@Views({
	@View(members = "name; chartData;"
			+ "chartType, yColumn;"
			+ "columns")
})
	
public class Chart implements Serializable {
	private static final long serialVersionUID = 1L;

	@DisplaySize(40)
	@Required
	@OnChange(value = OnChangeChartNameAction.class)
	@Column(length = 80)
	private String name;
	
	@Hidden
	private MetaModel metaModel;
	
	@LabelFormat(LabelFormatType.NO_LABEL)
	private String chartData;

	public enum ChartType {
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
		private ChartType(String jsType, boolean grouped) {
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
	@OnChange(value = OnChangeChartTypeAction.class)
	private ChartType chartType;
	
	@OnChange(value = OnChangeChartLabelColumnAction.class)
	private String yColumn;
	
	@RemoveSelectedAction("Chart.removeColumn")
	@ListProperties("displayed, name")
	@ElementCollection
	private List<ChartColumn> columns;

	private Boolean shared;
	
	private boolean changed;
	
	private boolean nameEditable;
	
	private boolean rendered;
	
	public Chart() {
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

	public String getChartData() {
		return chartData;
	}

	public void setChartData(String data) {
		this.chartData = data;
	}

	public ChartType getChartType() {
		return chartType;
	}

	public void setChartType(ChartType chartType) {
		this.chartType = chartType;
	}

	public String getyColumn() {
		return yColumn;
	}


	public void setyColumn(String xAxis) {
		this.yColumn = xAxis;
	}


	public List<ChartColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<ChartColumn> columns) {
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

	public boolean isRendered() {
		return rendered;
	}

	public void setRendered(boolean rendered) {
		this.rendered = rendered;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Chart [name=");
		builder.append(name);
		builder.append(", chartType=");
		builder.append(chartType);
		builder.append(", shared=");
		builder.append(shared);
		builder.append("]");
		return builder.toString();
	}
}
