//var exec = require('cordova/exec');
var exec = cordova.exec;
var PLUGIN_NAME = "Mapwize"

function MapwizeView() {
	console.log("MapwizeView: is created");
}

MapwizeView.prototype.callbacks;


MapwizeView.prototype.setCallback = function(callbacks) {
	console.log("MapwizeView: setCallback");
	this.callbacks = callbacks;

	exec(function(result) { 
		console.log("MapwizeView: setCallback: SUCCESS");
		switch(result.event) {
			case "DidLoad":
			case "DidTapOnFollowWithoutLocation":
			case "DidTapOnMenu":
			case "TapOnPlaceInformationButton":
			case "TapOnPlaceListInformationButton":
			case "TapOnCloseButton":
				console.log("Handling event " + result.event + " result: " + result.arg);
				if (!result.arg) {
					callbacks[result.event]();
				} else {
					callbacks[result.event](JSON.parse(result.arg));
				}
				
				break;
			default:
				console.log("Event not recognized... event: " + JSON.stringify(result.event));
		}
	}, function(result) {
		console.log("MapwizeView: setCallback: FAILED");}, PLUGIN_NAME, "setCallback", []);
}

MapwizeView.prototype.close = function(success, failure) {
	console.log("MapwizeView: close");
	exec(function(result) { 
				console.log("MapwizeView: close: SUCCESS");
				success(JSON.parse(result.arg));
			}, function(err) {
				console.log("MapwizeView: close: FAILED");
				failure(err);
			}, PLUGIN_NAME, "closeMapwizeView", []);
}

MapwizeView.prototype.selectPlace = function(id, centerOn, success, failure) {
	console.log("MapwizeView: selectPlace");
	exec(function(result) { 
				console.log("MapwizeView: selectPlace: SUCCESS");
				success(JSON.parse(result.arg));
			}, function(err) {
				console.log("MapwizeView: selectPlace: FAILED");
				failure(err);
			}, PLUGIN_NAME, "selectPlace", [id, centerOn]);
}

MapwizeView.prototype.selectPlaceList = function(id, success, failure) {
	console.log("MapwizeView: selectPlaceList");
	exec(function(result) { 
				console.log("MapwizeView: selectPlaceList: SUCCESS");
				success(JSON.parse(result.arg));
			}, function(err) {
				console.log("MapwizeView: selectPlaceList: FAILED");
				failure(err);
			}, PLUGIN_NAME, "selectPlaceList", [id]);
}

MapwizeView.prototype.setPlaceStyle = function(id, style, success, failure) {
	console.log("MapwizeView: setPlaceStyle");
	exec(function(result) { 
				console.log("MapwizeView: setPlaceStyle: SUCCESS");
				success(JSON.parse(result.arg));
			}, function(err) {
				console.log("MapwizeView: setPlaceStyle: FAILED");
				failure(err);
			}, PLUGIN_NAME, "setPlaceStyle", [id, JSON.stringify(style)]);
}

MapwizeView.prototype.grantAccess = function(accessKey, success, failure) {
	console.log("MapwizeView: grantAccess");
	exec(function(result) { 
				console.log("MapwizeView: grantAccess: SUCCESS");
				success(JSON.parse(result.arg));
			}, function(err) {
				console.log("MapwizeView: grantAccess: FAILED");
				failure(err);
			}, PLUGIN_NAME, "grantAccess", [accessKey]);
}

MapwizeView.prototype.unselectContent = function(closeInfo, success, failure) {
	console.log("MapwizeView: unselectContent");
	exec(function(result) { 
				console.log("MapwizeView: unselectContent: SUCCESS");
				success(JSON.parse(result.arg));
			}, function(err) {
				console.log("MapwizeView: unselectContent: FAILED");
				failure(err);
			}, PLUGIN_NAME, "unselectContent", [closeInfo]);
}

module.exports = MapwizeView;