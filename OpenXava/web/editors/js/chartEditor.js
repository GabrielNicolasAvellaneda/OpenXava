openxava.addEditorInitFunction(function() { 
	if ($('#xava_application').length &&
			$('#xava_module').length) {
		var applicationName = $('#xava_application').val();
		var moduleName = $('#xava_module').val();
		var xavaChartPrefix = openxava.decorateId(applicationName, moduleName, "xava_chart__");
		var idPrefix = "#" + xavaChartPrefix;
		if ($(idPrefix + 'type').length) {
			var chartType = $(idPrefix + 'type').val();
			var grouped = $(idPrefix + 'grouped').val();
			var specification = openxava.renderChart(applicationName, moduleName, chartType, grouped, xavaChartPrefix);
			if (specification != 'empty') {
				c3.generate(specification);
			}
		}
	}
});
