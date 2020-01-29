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
		console.log("MapwizeView: setCallback: SUCCESS, result: " + JSON.stringify(result));
		switch(result.event) {
			case "DidLoad":
			case "DidTapOnFollowWithoutLocation":
			case "DidTapOnMenu":
			case "TapOnPlaceInformationButton":
			case "TapOnPlaceListInformationButton":
			case "TapOnCloseButton":
				console.log("Handling event " + JSON.stringify(result));
				if (callbacks[result.event]) {
					if(!!result && result.arg){
						console.log("Handling event has arg...");
						callbacks[result.event](JSON.parse(result.arg));
					} else{
						console.log("Handling event has NO arg...");
						callbacks[result.event](result);
					}
				} else {
					console.log("Callback function doesn't exist...");
				}
				
				break;
			default:
				console.log("Event not recognized... event: " + JSON.stringify(result));
		}
	}, function(result) {
		console.log("MapwizeView: setCallback: FAILED");}, PLUGIN_NAME, "setCallback", []);
}

MapwizeView.prototype.close = function(success, failure) {
	console.log("MapwizeView: close");
	exec(function(result) { 
				console.log("MapwizeView: close: SUCCESS");
				if(!!result && result.arg){
					success(result.arg);
				} else{
					success(result);
				}
			}, function(err) {
				console.log("MapwizeView: close: FAILED");
				failure(err);
			}, PLUGIN_NAME, "closeMapwizeView", []);
}

MapwizeView.prototype.setDirection = function(direction, from, to, isAccessible, success, failure) {
	console.log("MapwizeView: setDirection...");
	exec(function(result) { 
				console.log("MapwizeView: setDirection: SUCCESS");
				if(!!result && result.arg){
					success(result.arg);
				} else{
					console.log("setDirection, no result: " + JSON.stringify(result));
					success(result);
				}
			}, function(err) {
				console.log("MapwizeView: setDirection: FAILED");
				failure(err);
			}, PLUGIN_NAME, "setDirection", [JSON.stringify(direction), JSON.stringify(from), JSON.stringify(to), isAccessible]);
}

MapwizeView.prototype.selectPlace = function(id, centerOn, success, failure) {
	console.log("MapwizeView: selectPlace");
	exec(function(result) { 
				console.log("MapwizeView: selectPlace: SUCCESS");
				if(!!result && result.arg){
					success(result.arg);
				} else{
					success(result);
				}
			}, function(err) {
				console.log("MapwizeView: selectPlace: FAILED");
				failure(err);
			}, PLUGIN_NAME, "selectPlace", [id, centerOn]);
}

MapwizeView.prototype.selectPlaceList = function(id, success, failure) {
	console.log("MapwizeView: selectPlaceList");
	exec(function(result) { 
				console.log("MapwizeView: selectPlaceList: SUCCESS");
				if(!!result && result.arg){
					success(result.arg);
				} else{
					success(result);
				}
			}, function(err) {
				console.log("MapwizeView: selectPlaceList: FAILED");
				failure(err);
			}, PLUGIN_NAME, "selectPlaceList", [id]);
}

MapwizeView.prototype.setPlaceStyle = function(id, style, success, failure) {
	console.log("MapwizeView: setPlaceStyle");
	exec(function(result) { 
				console.log("MapwizeView: setPlaceStyle: SUCCESS");
				if(!!result && result.arg){
					success(result.arg);
				} else{
					success(result);
				}
			}, function(err) {
				console.log("MapwizeView: setPlaceStyle: FAILED");
				failure(err);
			}, PLUGIN_NAME, "setPlaceStyle", [id, JSON.stringify(style)]);
}

MapwizeView.prototype.grantAccess = function(accessKey, success, failure) {
	console.log("MapwizeView: grantAccess");
	exec(function(result) { 
				console.log("MapwizeView: grantAccess: SUCCESS");
				if(!!result && result.arg){
					success(result.arg);
				} else{
					success(result);
				}
			}, function(err) {
				console.log("MapwizeView: grantAccess: FAILED");
				failure(err);
			}, PLUGIN_NAME, "grantAccess", [accessKey]);
}

MapwizeView.prototype.unselectContent = function(closeInfo, success, failure) {
	console.log("MapwizeView: unselectContent");
	exec(function(result) { 
				console.log("MapwizeView: unselectContent: SUCCESS");
				if(!!result && result.arg){
					success(result.arg);
				} else{
					success(result);
				}
			}, function(err) {
				console.log("MapwizeView: unselectContent: FAILED");
				failure(err);
			}, PLUGIN_NAME, "unselectContent", [closeInfo]);
}

module.exports = MapwizeView;
