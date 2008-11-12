if (openxava == null) var openxava = {};
if (openxava.editors == null) openxava.editors = {};

// htmlEditor
if (openxava.editors.html == null) openxava.editors.html = {};
openxava.editors.html.showValue = function(v){
  	document.getElementById('xava_html_editor_show_value').innerHTML=v; 
  	return true;
}
openxava.editors.html.openWindow = function(url){
 	window.open(url,'window','status=no,scrollbars=yes,menubar=no,location=no,resizable=no,height=600,width=750');
}

