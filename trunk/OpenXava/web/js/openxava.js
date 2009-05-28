if (openxava == null) var openxava = {};

openxava.init = function(application, module) {
	dwr.util.useLoadingMessage(openxava.loadingMessage);
	document.onkeydown = openxava.processKey;  
	openxava.ajaxRequest(application, module);
}

openxava.ajaxRequest = function(application, module) {	
	document.throwPropertyChange = false; 
	document.getElementById(openxava.decorateId(application, module, "loading")).value=true;    
	document.body.style.cursor='wait';
	Module.request(
			application, module,
			dwr.util.getValues(openxava.getForm(application, module)),
			openxava.getMultipleValues(application, module), 
			openxava.getSelectedValues(application, module), 					 
			openxava.refreshPage); 			
}

openxava.refreshPage = function(result) {
	var changed = "";	
	if (result.error != null) {		
		openxava.systemError(result);
		changed = " ERROR";
		return;
	}
	if (result.reload) {
		window.location.reload();
		return;
	}		
	if (result.forwardURL != null) {
		if (result.forwardInNewWindow) { 		
			window.open(result.forwardURL);
			var form = openxava.getForm(result.application, result.module);
			if (form != null) { 
				form[openxava.decorateId(result.application, result.module, "xava_action")].value="";	
				form[openxava.decorateId(result.application, result.module, "xava_action_argv")].value="";
				form[openxava.decorateId(result.application, result.module, "xava_changed_property")].value="";
			}
			window.location.reload();	
			return; 			
		}
		else {
			location.href=result.forwardURL;			
		}
	}
	else if (result.nextModule != null) {		
		openxava.updateRootIds(result.application, result.module, result.nextModule);
		document.getElementById("xava_last_module_change").value=result.module + "::" + result.nextModule;		
		openxava.ajaxRequest(result.application, result.nextModule); 
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
		if (openxava.initTheme != null) openxava.initTheme();		
		if (result.focusPropertyId != null) { 
			document.getElementById(openxava.decorateId(result.application, result.module, "xava_focus_property_id")).value = result.focusPropertyId; 
			openxava.setFocus(result.application, result.module);		
		}
	}	
	document.getElementById('xava_processing_layer').style.display='none'; 
	var form = openxava.getForm(result.application, result.module); 
	if (form != null) {  
		form[openxava.decorateId(result.application, result.module, "xava_action")].value=""; 
		form[openxava.decorateId(result.application, result.module, "xava_action_argv")].value="";
		form[openxava.decorateId(result.application, result.module, "xava_changed_property")].value="";
	}		
	document.getElementById(openxava.decorateId(result.application, result.module, "loaded_parts")).value=changed;
	document.getElementById(openxava.decorateId(result.application, result.module, "loading")).value=false;
	openxava.lastApplication=result.application;
	openxava.lastModule=result.module;	
	document.body.style.cursor='auto';	
}

openxava.updateRootIds = function(application, moduleFrom, moduleTo) { 
	document.getElementById(openxava.decorateId(
		application, moduleFrom, "loading")).id =	
			openxava.decorateId(application, moduleTo, "loading");
	document.getElementById(openxava.decorateId(
		application, moduleFrom, "core")).id =	
			openxava.decorateId(application, moduleTo, "core");
	document.getElementById(openxava.decorateId(
		application, moduleFrom, "loaded_parts")).id =	
			openxava.decorateId(application, moduleTo, "loaded_parts");
}

openxava.getForm = function(application, module) { 		
	return document.getElementById(openxava.getFormName(application, module));	
}

openxava.getFormName = function(application, module) { 
	return openxava.decorateId(application, module, "form");		
}

// The logic is the same of org.openxava.web.Ids.decorateId 
openxava.decorateId = function(application, module, simpleName) { 
	return "ox_" + application + "_" + module + "__" + simpleName;
}

openxava.systemError = function(result) { 
	document.body.style.cursor='auto';	
	document.getElementById(openxava.decorateId(result.application, result.module, "core")).innerHTML="<big><big style='padding: 5px;font-weight: bold; color: rgb(255, 0, 0);'>ERROR: " + result.error + "</big></big>";
}

openxava.processKey = function(event) {	
	if (!event) event = window.event;
	
	if ( !(event.keyCode >= 112 && event.keyCode <= 123 ||
			event.ctrlKey || event.altKey || event.shiftKey) ) return;
	
	if (event.keyCode >= 49 && event.keyCode <= 57 && event.ctrlKey && !event.altKey) {				
		event.returnValue = false;
		event.preventDefault();
		openxava.executeAction(openxava.lastApplication, openxava.lastModule,
			"", false, "Sections.change", "activeSection=" + (event.keyCode - 49)); 
		return;
	}	
	
	var key = event.keyCode + "," + event.ctrlKey + "," + event.altKey + "," + event.shiftKey;	
	var action = openxava.strokeActions[key];
	if (action != null) {				
		event.returnValue = false;
		event.preventDefault();
		openxava.executeAction(openxava.lastApplication, openxava.lastModule,
			'', action.takesLong, action.name); 
		return;
	}
		
}

openxava.getSelectedValues = function(application, module) {  	  		
	var result = new Array();	  		
	var selected = document.getElementsByName(openxava.decorateId(application, module, "xava_selected"));  
	var j=0;
	for (var i=0; i<selected.length; i++) {
  		if (selected[i].checked) {
	  		result[j++] = selected[i].value;
  		}
	}	  		
	return result;
}

openxava.getMultipleValues = function(application, module) { 
	var result = new Object();
	var multiple = document.getElementsByName(openxava.decorateId(application, module, "xava_multiple"));  
	for (var i=0; i<multiple.length; i++) {
  		var propertyName = multiple[i].value; 
  		var elements = document.getElementsByName(propertyName);
  		if (elements.length == 1) {
  			result[propertyName] = dwr.util.toDescriptiveString(dwr.util.getValue(propertyName), 2);
  		}
  		else {  	  			
  			for (var j=0; j<elements.length; j++) {
  				var indexedName = elements[j].name + "::" + j;
  				var originalName = elements[j].name;
  				var element = elements[j]; 
  				element.name = indexedName;
  				result[indexedName] = dwr.util.getValue(indexedName);
  				element.name = originalName;  				  				
  			}
  		}
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
openxava.manageFilterRow = function(application, module, id, tabObject) { 
    var img = document.getElementById(
    	openxava.decorateId(application, module, "filter_image_" + id));
    var elem = document.getElementById(
    	openxava.decorateId(application, module, "tr_list_filter_" + id));
    var link = document.getElementById(
    	openxava.decorateId(application, module, "filter_link_" + id));
    var prefix = openxava.getForm(application, module)["xava_image_filter_prefix"].value;
	if (elem.style.display == ''){
		elem.style.display = 'none';
		img.src=prefix + 'show-filter.gif';
		link.title=openxava.showFiltersMessage;		
		Tab.setFilterVisible(application, module, false, tabObject);
	}
	else {
		elem.style.display = '';
		img.src=prefix + 'hide-filter.gif';
		link.title=this.hideFiltersMessage;
		Tab.setFilterVisible(application, module, true, tabObject);
	}    
}

openxava.executeAction = function(application, module, confirmMessage, takesLong, action) { 
	openxava.executeAction(application, module, confirmMessage, takesLong, action, null);
}

openxava.executeAction = function(application, module, confirmMessage, takesLong, action, argv) {  		
	if (confirmMessage != "" && !confirm(confirmMessage)) return;
	if (takesLong) { 
		document.getElementById('xava_processing_layer').style.display='block';
		setTimeout('document.images["xava_processingImage"].src = "images/processing.gif"', 1);
	}
	var form = openxava.getForm(application, module); 
	form[openxava.decorateId(application, module, "xava_focus_forward")].value = "false";
	form[openxava.decorateId(application, module, "xava_action")].value=action;	
	form[openxava.decorateId(application, module, "xava_action_argv")].value=argv;
	
	if (openxava.isSubmitNeeded(form)) { 
		form.submit();
	} 
	else {					
		openxava.ajaxRequest(application, module);
	}				
}

openxava.isSubmitNeeded = function(form) {		
	return form.enctype=="multipart/form-data";
}

openxava.throwPropertyChanged = function(application, module, property) {
	document.throwPropertyChange = true;
	var form = openxava.getForm(application, module); 
	form[openxava.decorateId(application, module, "xava_focus_forward")].value = "true";
	form[openxava.decorateId(application, module, "xava_focus_property")].value=property;	
	form[openxava.decorateId(application, module, "xava_changed_property")].value=property;
	setTimeout ('openxava.requestOnChange("' + application + '", "' + module + '")', 100); 
}

openxava.requestOnChange = function(application, module) {   
	if (document.throwPropertyChange)  { 
		openxava.ajaxRequest(application, module); 
	}			
}

openxava.setFocus = function(application, module) { 
	var form = openxava.getForm(application, module);
	var elementName = form.elements[openxava.decorateId(application, module, "xava_focus_property_id")].value;	
	var elementDecoratedName =  openxava.decorateId(application, module, elementName); 
	var element = form.elements[elementDecoratedName];	
	if (element != null && typeof element.disabled != "undefined" && !element.disabled) {
		if (element.type != "hidden") element.focus();
		if (typeof element.select != "undefined") { 
			element.select();
		}
	}	
}

openxava.clearConditionValues = function(application, module, prefix) { 
	var form = openxava.getForm(application, module);
	var elementName = openxava.decorateId(application, module, prefix + "conditionValue.");
	var element = form.elements[elementName + "0"];
	var i=0;
	while (typeof element != "undefined") {
		element.value = '';
		element = form.elements[elementName + (++i)];
	}
}

openxava.onSelectElement = function(application, module, action, argv, checkValue, idRow, hasOnSelectAction, cssSelectedRow, cssRow, selectedRowStyle, rowStyle, confirmMessage, takesLong) {
	if (checkValue) {
		var cssClass = cssSelectedRow + " " + cssRow;
		document.getElementById(idRow).className=cssClass;
		document.getElementById(idRow).onmouseout = function() {
            this.className = cssClass;
        }
		document.getElementById(idRow).style.cssText = rowStyle + selectedRowStyle;
	}
	else {
		document.getElementById(idRow).className=cssRow;
		document.getElementById(idRow).onmouseout = function() {
            this.className = cssRow;
        }
		document.getElementById(idRow).style.cssText = rowStyle;
	}
	
	if (hasOnSelectAction){
		argv = argv + ",selected=" + checkValue;
		openxava.executeAction(application, module, confirmMessage, takesLong, action, argv);	
	}
}