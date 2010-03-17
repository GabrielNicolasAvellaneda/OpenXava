openxava.addEditorInitFunction(function() {
		$('input.xava_numeric').focus(function() {
			$('input.xava_numeric').autoNumeric();
		});
});
