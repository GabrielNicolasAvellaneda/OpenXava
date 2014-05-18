This selenium tests are designed for selenium-ide.

Download the firefox plugin and run the Renderer.suite.

For improved testing reliability configure Selenium core extensions.
The core extension file is user-extensions.js and is located in 
the selenium-tests folder.

Openxava Extensions (Experimental)

You can also add ox-extensions.js (comma separated) that enables a 
set of commands compatible with ox.

Commands implemented:
oxOpen application module. Opens the module in the browser.
oxClick Controller.action. Perform a click on the button specified in Controller.action.
oxType propertyName value. Types the value in the property name.



