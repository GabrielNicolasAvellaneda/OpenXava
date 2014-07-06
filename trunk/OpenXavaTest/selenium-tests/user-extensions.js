Selenium.prototype.runningVars = {};
Selenium.prototype.runningVars.nextCommands = new Array();
Selenium.prototype.runningVars.lastCommands = new Array();
Selenium.prototype.doOriginalClick = Selenium.prototype.doClick;
Selenium.prototype.doOriginalType = Selenium.prototype.doType;
Selenium.prototype.doOriginalCheck = Selenium.prototype.doCheck;
Selenium.prototype.doOriginalUncheck = Selenium.prototype.doUncheck;

Selenium.prototype.initializeUserExtensions = function() {
    selenium.originalNextCommand = currentTest.nextCommand;
    
    currentTest.nextCommand = function() {
	var command = null;
	try {
	    command = selenium.runningVars.nextCommands.pop();
	} catch (e) {}
	if (command == null) {
	    command = selenium.originalNextCommand();
	}
	selenium.runningVars.lastCommands.push(command);
	if (selenium.runningVars.lastCommands.length > 2) {
	    selenium.runningVars.lastCommands.shift();
	}
	return command;
    }
}
/*
 * Adds pre commands
 */
Selenium.prototype.addCommands = function(locator) {
        selenium.runningVars.nextCommands = new Array();
    var lastCommand = selenium.runningVars.lastCommands.shift();
    if (lastCommand == null ||
	lastCommand.command != "pause") {
	selenium.runningVars.nextCommands.unshift(new SeleniumCommand("waitForElementPresent", locator, ""));
	selenium.runningVars.nextCommands.unshift(new SeleniumCommand("pause", "300", ""));
    }
}

/*
 * Performs the click sequence in openxava buttons.
 */
Selenium.prototype.doClick = function(locator) {
    selenium.addCommands(locator);
    selenium.runningVars.nextCommands.unshift(new SeleniumCommand("originalClick", locator, ""));
    selenium.runningVars.nextCommands.unshift(new SeleniumCommand("waitForTextNotPresent", "loading...", ""));
}

/*
 * Performs the type sequence in openxava buttons.
 */
Selenium.prototype.doType = function( locator, value) {
    selenium.addCommands(locator);
    selenium.runningVars.nextCommands.unshift(new SeleniumCommand("originalType", locator, value));
    selenium.runningVars.nextCommands.unshift(new SeleniumCommand("waitForTextNotPresent", "loading...", ""));
}

/**
 * Overrides check function to allow the triggering of internal scripts
 */
Selenium.prototype.doCheck = function(locator) {
    selenium.addCommands(locator);
    selenium.runningVars.nextCommands.unshift(new SeleniumCommand("checkAndClick", locator, ""));
}

Selenium.prototype.doCheckAndClick = function(locator) {
    var element = this.findToggleButton(locator);
    if (!element.checked) {
	this.doClick(locator);
    }
}

/**
 * Overrides uncheck function to allow the triggering of internal scripts
 */
Selenium.prototype.doUncheck = function(locator) {
    selenium.addCommands(locator);
    selenium.runningVars.nextCommands.unshift(new SeleniumCommand("uncheckAndClick", locator, ""));
}

Selenium.prototype.doUncheckAndClick = function(locator) {
    var element = this.findToggleButton(locator);
    if (element.checked) {
	this.doClick(locator);
    }
}

/*
 * Liferay support
 */
Selenium.prototype.lrConvertToLink = function(pageName) {
    var returnValue = "";
    var separator="ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    var i = 0;
    for (i = 0; i < pageName.length; i++) {
	charValue = pageName.charAt(i);
	if (i > 0) {
	    if ((charValue >= "A" && charValue <= "Z") ||
		(charValue>="0" && charValue <="9")) {
		returnValue = returnValue + "_";
	    }
	}
	returnValue = returnValue + charValue.toLowerCase();
    }
    return returnValue;
}

/* Login */
Selenium.prototype.doLrLogin = function(locator, value) {
    selenium.runningVars.nextCommands.unshift(new SeleniumCommand("originalType", "css=input[id$='login']", locator));
    selenium.runningVars.nextCommands.unshift(new SeleniumCommand("originalType", "css=input[id$='password']", value));
    selenium.runningVars.nextCommands.unshift(new SeleniumCommand("originalClick", "css=input.aui-button-input.aui-button-input-submit", ""));
    selenium.runningVars.nextCommands.unshift(new SeleniumCommand("waitForPageToLoad", "", ""));
    selenium.runningVars.nextCommands.unshift(new SeleniumCommand("waitForTextPresent", "You are signed in as", ""));
}

/* Logout*/
Selenium.prototype.doLrLogout = function() {
    selenium.runningVars.nextCommands.unshift(new SeleniumCommand("originalClick", "css=a:contains('Sign Out')", ""));
}

/* goto page*/
Selenium.prototype.doLrToPage = function(locator, value) {
	var newLocator = selenium.lrConvertToLink(locator);
	if (locator.length == 0 || locator.charAt(0) != '/') {
		newLocator = "/en/group/" + newLocator;
	}
    selenium.runningVars.nextCommands.unshift(new SeleniumCommand("open", newLocator + selenium.lrConvertToLink(value), ""));
    selenium.runningVars.nextCommands.unshift(new SeleniumCommand("waitForPageToLoad", "", ""));
}

/*
 * Add sideflow.js content HERE
 */

var gotoLabels= {};
var whileLabels = {};

// overload the original Selenium reset function
Selenium.prototype.reset = function() {
    // reset the labels
    this.initialiseLabels();
    // proceed with original reset code
    this.defaultTimeout = Selenium.DEFAULT_TIMEOUT;
    this.browserbot.selectWindow("null");
    this.browserbot.resetPopups();
    
    // ADDED by Federico Alcantara
    this.initializeUserExtensions();
}


/*
 * ---   Initialize Conditional Elements  --- *
 *  Run through the script collecting line numbers of all conditional elements
 *  There are three a results arrays: goto labels, while pairs and forEach pairs
 *  
 */
Selenium.prototype.initialiseLabels = function()
{
    gotoLabels = {};
    whileLabels = { ends: {}, whiles: {} };
    var command_rows = [];
    var numCommands = testCase.commands.length;
    for (var i = 0; i < numCommands; ++i) {
        var x = testCase.commands[i];
        command_rows.push(x);
    }
    var cycles = [];
    var forEachCmds = [];
    for( var i = 0; i < command_rows.length; i++ ) {
        if (command_rows[i].type == 'command')
        switch( command_rows[i].command.toLowerCase() ) {
            case "label":
                gotoLabels[ command_rows[i].target ] = i;
                break;
            case "while":
            case "endwhile":
                cycles.push( [command_rows[i].command.toLowerCase(), i] )
                break;
            case "foreach":
            case "endforeach":
                forEachCmds.push( [command_rows[i].command.toLowerCase(), i] )
                break;
        }
    }  
    var i = 0;
    while( cycles.length ) {
        if( i >= cycles.length ) {
            throw new Error( "non-matching while/endWhile found" );
        }
        switch( cycles[i][0] ) {
            case "while":
                if( ( i+1 < cycles.length ) && ( "endwhile" == cycles[i+1][0] ) ) {
                    // pair found
                    whileLabels.ends[ cycles[i+1][1] ] = cycles[i][1];
                    whileLabels.whiles[ cycles[i][1] ] = cycles[i+1][1];
                    cycles.splice( i, 2 );
                    i = 0;
                } else ++i;
                break;
            case "endwhile":
                ++i;
                break;
        }
    }

}

Selenium.prototype.continueFromRow = function( row_num )
{
    if(row_num == undefined || row_num == null || row_num < 0) {
        throw new Error( "Invalid row_num specified." );
    }
    testCase.debugContext.debugIndex = row_num;
}

// do nothing. simple label
Selenium.prototype.doLabel = function(){};

Selenium.prototype.doGotoLabel = function( label )
{
    if( undefined == gotoLabels[label] ) {
        throw new Error( "Specified label '" + label + "' is not found." );
    }
    this.continueFromRow( gotoLabels[ label ] );
};

Selenium.prototype.doGoto = Selenium.prototype.doGotoLabel;

Selenium.prototype.doGotoIf = function( condition, label )
{
    if( eval(condition) ) this.doGotoLabel( label );
}

Selenium.prototype.doWhile = function( condition )
{
    if( !eval(condition) ) {
        var last_row = testCase.debugContext.debugIndex;
        var end_while_row = whileLabels.whiles[ last_row ];
        if( undefined == end_while_row ) throw new Error( "Corresponding 'endWhile' is not found." );
        this.continueFromRow( end_while_row );
    }
}

Selenium.prototype.doEndWhile = function()
{
    var last_row = testCase.debugContext.debugIndex;
    var while_row = whileLabels.ends[ last_row ] - 1;
    if( undefined == while_row ) throw new Error( "Corresponding 'While' is not found." );
    this.continueFromRow( while_row );
}

Selenium.prototype.doPush= function(value, varName)
{
    if(!storedVars[varName]) {
        storedVars[varName] = new Array();
    } 
    if(typeof storedVars[varName] !== 'object') {
        throw new Error("Cannot push value onto non-array " + varName);
    } else {
        storedVars[varName].push(value);
    }
}
