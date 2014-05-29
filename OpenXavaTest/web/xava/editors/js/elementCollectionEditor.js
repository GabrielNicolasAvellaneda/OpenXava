if (elementCollectionEditor == null) var elementCollectionEditor = {};

elementCollectionEditor.onChangeRow = function(element, rowIndex) {  	
	var currentRow = $(element).parent().parent(); 
	var nextRow = currentRow.next();
	if (nextRow.is(':visible')) return;			
	var newRow = nextRow.clone();
	var token1 = new RegExp("__" + (rowIndex + 1), "g");
	var token2 = "__" + (rowIndex + 2);
	newRow.attr("id", newRow.attr("id").replace(token1, token2));
	var newRowHtml = newRow.html().replace(token1, token2);
	token1 = new RegExp(", " + (rowIndex + 1) + "\\)", "g");
	token2 = ", " + (rowIndex + 2) + ")";
	newRowHtml = newRowHtml.replace(token1, token2);
	newRow.html(newRowHtml);
	nextRow.show();
	var table = currentRow.parent().parent(); 
	$(table).append(newRow);
	currentRow.children().first().find("a").css('visibility', 'visible');
}

elementCollectionEditor.removeRow = function(element, rowIndex) { 
	var currentRow = $(element).parent().parent().parent().parent();
	currentRow.find("input").val("");
	currentRow.hide();
}
