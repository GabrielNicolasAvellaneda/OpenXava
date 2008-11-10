if (openxava == null) var openxava = {};

openxava.init = function() {
	dwr.util.useLoadingMessage(this.loadingMessage);
	document.onkeydown = this.processKey; 
	this.ajaxRequest();
}

openxava.ajaxRequest = function() {
	document.throwPropertyChange = false;
	document.getElementById("xava_loading").value=true; 
	document.body.style.cursor='wait'; 
	Module.request(
			this.application, this.module, 			 
			dwr.util.getValues(this.formName), 
			this.getMultipleValues(), 
			this.getSelectedValues(), 					 
			this.refreshPage); 			
}

openxava.refreshPage = function(result) {
	var changed = "";
	if (result.error != null) {
		openxava.systemError(result.error);
		changed = " ERROR";
		return;
	}
	if (result.forwardURL != null) {
		if (result.forwardInNewWindow) { 		
			window.open(result.forwardURL);
		}
		else {
			location.href=result.forwardURL;			
		}
	}
	else if (result.module != null) { 		
		openxava.module = result.module;
		openxava.formName = result.form;
		openxava.ajaxRequest();
		return;
	}	
	else {	
		openxava.strokeActions = result.strokeActions;		
		var changedParts = result.changedParts; 		
		for (var id in changedParts) { 	
			changed = changed + id + ", ";  			
			try { 
				document.getElementById(id).innerHTML = changedParts[id];
			}
			catch (ex) {
				changed = changed + " ERROR";
				alert("Error refreshing part: " + id);
				errors = true;
				break;
			}			
		}						
		openxava.formName = "xava_form";
		openxava.form = document.getElementById(openxava.formName);		
		document.getElementById("xava_focus_property_id").value = result.focusPropertyId;
		openxava.setFocus();		
	}
	document.getElementById('xava_processing_layer').style.display='none'; 
	openxava.form.xava_action.value="";	
	openxava.form.xava_action_argv.value="";
	openxava.form.xava_changed_property.value=""; 
	
	document.getElementById("xava_loading").value=false;
	document.getElementById("xava_loaded_parts").value=changed;
	document.body.style.cursor='auto'; 
}

openxava.systemError = function(error) {
	document.body.style.cursor='auto';
	document.getElementById("xava_core").innerHTML="<big><big style='padding: 5px;font-weight: bold; color: rgb(255, 0, 0);'>ERROR: " + error + "</big></big>";
}

openxava.processKey = function(event) {	
	if (!event) event = window.event;
	
	if ( !(event.keyCode >= 112 && event.keyCode <= 123 ||
			event.ctrlKey || event.altKey || event.shiftKey) ) return;
	
	if (event.keyCode >= 49 && event.keyCode <= 57 && event.ctrlKey && !event.altKey) {				
		event.returnValue = false;
		event.preventDefault();
		openxava.executeAction("", false, "Sections.change", "activeSection=" + (event.keyCode - 49));
		return;
	}	
	
	var key = event.keyCode + "," + event.ctrlKey + "," + event.altKey + "," + event.shiftKey;	
	var action = openxava.strokeActions[key];
	if (action != null) {				
		event.returnValue = false;
		event.preventDefault();
		openxava.executeAction('', action.takesLong, action.name);
		return;
	}
		
}

openxava.getSelectedValues = function() { 	  		
	var result = new Array();	  		
	var selected = document.getElementsByName("xava_selected"); 
	var j=0;
	for (var i=0; i<selected.length; i++) {
  		if (selected[i].checked) {
	  		result[j++] = selected[i].value;
  		}
	}	  		
	return result;
}

openxava.getMultipleValues = function() {
	var result = new Object();	  		
	var multiple = document.getElementsByName("xava_multiple"); 
	var j=0;
	for (var i=0; i<multiple.length; i++) {
  		var propertyName = multiple[i].value; 
  		result[propertyName] = dwr.util.toDescriptiveString(dwr.util.getValue(propertyName), 2);		  		
	}	  		
	return result;
}

// JavaScript for text area maxsize
openxava.limitLength = function(ev, max) { 
	var target = window.event ? window.event.srcElement : ev.target;			
	if ( target.value.length > max ) {
		target.value = target.value.substring(0, max);		
	}	
}

// JavaScript for numeric editors
openxava.validateNumeric = function(ev, max, integer) {		
	if (ev.which == 0) return true;
	var charCode = (ev.which) ? ev.which : ev.keyCode;		
	if (charCode == 0 || charCode == 8) return true;		
	if (ev.ctrlKey) return true;		
	var target = window.event ? window.event.srcElement : ev.target;	
	var text = target.value + String.fromCharCode(charCode);	
	var numerics = 0;			
	var textLength = text.length;
	for (var i=0; i < textLength; i++) {		
		var theChar = text.charAt(i);		
		if (theChar >= '0' && theChar <= '9') {
			numerics++;
			if (numerics > max) {
				return false;
			}
		}	
		else if (!integer && !(theChar == ',' || theChar == '.' || theChar == '-')) {
			return false;
		}
		else if (integer && theChar != '-') {
			return false;
		}
	}	
	return true;
}

// JavaScript for collections and list
openxava.manageFilterRow = function(id, tabObject) {
    var img = document.getElementById("xava_filter_image_" + id);
    var elem = document.getElementById("xava_tr_list_filter_" + id)
    var link = document.getElementById("xava_filter_link_" + id)
	if (elem.style.display == ''){
		elem.style.display = 'none';
		img.src='images/show-filter.gif';
		link.title=this.showFiltersMessage;
		Tab.setFilterVisible(this.application, this.module, false, tabObject);
	}
	else {
		elem.style.display = '';
		img.src='images/hide-filter.gif';
		link.title=this.hideFiltersMessage;
		Tab.setFilterVisible(this.application, this.module, true, tabObject);
	}    
}

openxava.executeAction = function(confirmMessage, takesLong, action) {
	this.executeAction(confirmMessage, takesLong, action, null);
}

openxava.executeAction = function(confirmMessage, takesLong, action, argv) {		
	if (confirmMessage != "" && !confirm(confirmMessage)) return;
	if (takesLong) {
		document.getElementById('xava_processing_layer').style.display='block';
		setTimeout('document.images["xava_processingImage"].src = "images/processing.gif"', 1);
	}
	this.form.xava_focus_forward.value = "false";
	this.form.xava_action.value=action;	
	this.form.xava_action_argv.value=argv;
	if (this.isSubmitNeeded()) { 
		this.form.submit();
	} 
	else {					
		this.ajaxRequest();
	}				
}

openxava.isSubmitNeeded = function() {		
	return this.form.enctype=="multipart/form-data";
}

openxava.throwPropertyChanged = function(property) {	
	this.form.xava_focus_forward.value = "true";
	this.form.xava_focus_property.value=property;	
	this.form.xava_changed_property.value=property;
	document.throwPropertyChange = true;
	setTimeout ('openxava.requestOnChange()', 100);
}

openxava.requestOnChange = function() { 
	if (document.throwPropertyChange)  { 
		this.ajaxRequest(); 
	}			
}

openxava.setFocus = function() {
	var elementName = this.form.elements['xava_focus_property_id'].value;	
	var element = this.form.elements[elementName];	
	if (element != null && typeof element.disabled != "undefined" && !element.disabled) {
		if (element.type != "hidden") element.focus();
		if (typeof element.select != "undefined") { 
			element.select();
		}
	}	
}

openxava.clearConditionValues = function(prefix) {
	var elementName = prefix + "conditionValue.";
	var element = this.form.elements[elementName + "0"];
	var i=0;
	while (typeof element != "undefined") {
		element.value = '';
		element = this.form.elements[elementName + (++i)];
	}
}
