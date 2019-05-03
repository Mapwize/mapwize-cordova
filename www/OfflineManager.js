//var exec = require('cordova/exec');
var exec = cordova.exec;
var PLUGIN_NAME = "Mapwize"


OfflineManager.prototype.styleURL;

function OfflineManager(styleURL) {
	console.log("OfflineManager: is created, styleURL: " + styleURL);
	this.styleURL = styleURL;
	exec(function(result) { 
				console.log("OfflineManager: init: SUCCESS");
				// success(JSON.parse(result.arg));
			}, function(err) {
				console.log("OfflineManager: init: FAILED");
				// failure(err);
			}, PLUGIN_NAME, "initOfflineManager", [styleURL]);
}

OfflineManager.prototype.removeDataForVenue = function(venueId, universeId, success, failure) {
	console.log("OfflineManager: close");
	exec(function(result) { 
				console.log("OfflineManager: close: SUCCESS");
				if(!!result && result.arg){
					success(JSON.parse(result.arg));
				} else{
					success(result);
				}
			}, function(err) {
				console.log("OfflineManager: close: FAILED");
				failure(err);
			}, PLUGIN_NAME, "removeDataForVenue", [venueId, universeId]);
}

OfflineManager.prototype.downloadDataForVenue = function(venueId, universeId, success, failure, progress) {
	console.log("OfflineManager: downloadDataForVenue");
	exec(function(result) { 
				console.log("OfflineManager: downloadDataForVenue: SUCCESS");
				if (result && !isNaN(result.arg)) {
					progress(result.arg);
				} else {
					success();
				}
			}, function(err) {
				console.log("OfflineManager: downloadDataForVenue: FAILED");
				failure(err);
			}, PLUGIN_NAME, "downloadDataForVenue", [venueId, universeId]);
}

OfflineManager.prototype.isOfflineForVenue = function(venueId, universeId, success, failure) {
	console.log("OfflineManager: isOfflineForVenue");
	exec(function(result) { 
				console.log("OfflineManager: isOfflineForVenue: SUCCESS");
				if(!!result && result.arg){
					success(JSON.parse(result.arg));
				} else{
					success(result);
				}
			}, function(err) {
				console.log("OfflineManager: isOfflineForVenue: FAILED");
				failure(err);
			}, PLUGIN_NAME, "isOfflineForVenue", [venueId, universeId]);
}

OfflineManager.prototype.getOfflineVenues = function(success, failure) {
	console.log("OfflineManager: getOfflineVenues");
	exec(function(result) { 
				console.log("OfflineManager: getOfflineVenues: SUCCESS");
				if(!!result && result.arg){
					success(JSON.parse(result.arg));
				} else{
					success(result);
				}
			}, function(err) {
				console.log("OfflineManager: getOfflineVenues: FAILED");
				failure(err);
			}, PLUGIN_NAME, "getOfflineVenues", []);
}

OfflineManager.prototype.getOfflineUniversesForVenue = function(venueId, success, failure) {
	console.log("OfflineManager: getOfflineUniversesForVenue");
	exec(function(result) { 
				console.log("OfflineManager: getOfflineUniversesForVenue: SUCCESS");
				if(!!result && result.arg){
					success(JSON.parse(result.arg));
				} else{
					success(result);
				}
			}, function(err) {
				console.log("OfflineManager: getOfflineUniversesForVenue: FAILED");
				failure(err);
			}, PLUGIN_NAME, "getOfflineUniversesForVenue", [venueId]);
}

module.exports = OfflineManager;


