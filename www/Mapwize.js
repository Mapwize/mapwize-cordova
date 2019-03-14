//var exec = require('cordova/exec');
var exec = cordova.exec;

var PLUGIN_NAME = "Mapwize"

function Mapwize() {
	console.log("Mapwize.js: is created");
}

Mapwize.prototype.callbacks;

Mapwize.prototype.createMapwizeView = function(options, success, failure) {
	console.log("Mapwize.js: createMapwizeView");
	exec(function(result) { 
				console.log("Mapwize.js: selectPlace: SUCCESS");
				success(result);
			}, function(err) {
				console.log("Mapwize.js: selectPlace: FAILED");
				failure(err);
			}, PLUGIN_NAME, "createMapwizeView", [JSON.stringify(options)]);
}

Mapwize.prototype.setCallback = function(callbacks) {
	console.log("Mapwize.js: createMapwizeView");
	this.callbacks = callbacks;

	exec(function(result) { 
		console.log("Mapwize.js: setCallback: SUCCESS");
		switch(result.event) {
			case "DidLoad":
			case "DidTapOnFollowWithoutLocation":
			case "DidTapOnMenu":
			case "shouldShowInformationButtonFor":
			case "TapOnPlaceInformationButton":
			case "TapOnPlacesInformationButton":
				console.log("Handling event " + result.event + " result: " + result.arg);
				if (!result.arg) {
					callbacks[result.event]();
				} else {
					callbacks[result.event](JSON.parse(result.arg));
				}
				
				break;
			default:
				console.log("Event not recognized...");
		}
	}, function(result) {
		/* alert("Error" + reply); */
		console.log("Mapwize.js: setCallback: FAILED");}, PLUGIN_NAME, "setCallback", []);
}

Mapwize.prototype.selectPlace = function(id, success, failure) {
	console.log("Mapwize.js: selectPlace");
	exec(function(result) { 
				console.log("Mapwize.js: selectPlace: SUCCESS");
				success(result);
			}, function(err) {
				console.log("Mapwize.js: selectPlace: FAILED");
				failure(err);
			}, PLUGIN_NAME, "selectPlace", [id]);
}

Mapwize.prototype.selectPlaceList = function(id, success, failure) {
	console.log("Mapwize.js: selectPlaceList");
	exec(function(result) { 
				console.log("Mapwize.js: selectPlaceList: SUCCESS");
				success(result);
			}, function(err) {
				console.log("Mapwize.js: selectPlaceList: FAILED");
				failure(err);
			}, PLUGIN_NAME, "selectPlaceList", [id]);
}

Mapwize.prototype.grantAccess = function(accessKey, success, failure) {
	console.log("Mapwize.js: grantAccess");
	exec(function(result) { 
				console.log("Mapwize.js: grantAccess: SUCCESS");
				success(result);
			}, function(err) {
				console.log("Mapwize.js: grantAccess: FAILED");
				failure(err);
			}, PLUGIN_NAME, "grantAccess", [accessKey]);
}

Mapwize.prototype.unselectContent = function(closeInfo, success, failure) {
	console.log("Mapwize.js: unselectContent");
	exec(function(result) { 
				console.log("Mapwize.js: unselectContent: SUCCESS");
				success(result);
			}, function(err) {
				console.log("Mapwize.js: unselectContent: FAILED");
				failure(err);
			}, PLUGIN_NAME, "unselectContent", [closeInfo]);
}

var mapwize = new Mapwize();
module.exports = mapwize;
