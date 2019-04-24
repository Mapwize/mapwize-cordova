//var exec = require('cordova/exec');
var exec = cordova.exec;
var PLUGIN_NAME = "Mapwize"

function OfflineManager() {
	console.log("OfflineManager: is created");
}

OfflineManager.prototype.removeDataForVenue = function(venueId, universeId, success, failure) {
	console.log("OfflineManager: close");
	exec(function(result) { 
				console.log("OfflineManager: close: SUCCESS");
				success(result);
			}, function(err) {
				console.log("OfflineManager: close: FAILED");
				failure(err);
			}, PLUGIN_NAME, "removeDataForVenue", [venueId, universeId]);
}

OfflineManager.prototype.downloadDataForVenue = function(venueId, universeId, success, failure, progress) {
	console.log("OfflineManager: downloadDataForVenue");
	exec(function(result) { 
				console.log("OfflineManager: downloadDataForVenue: SUCCESS");
				success(result);
			}, function(err) {
				console.log("OfflineManager: downloadDataForVenue: FAILED");
				failure(err);
			}, PLUGIN_NAME, "downloadDataForVenue", [venueId, universeId]);
}

OfflineManager.prototype.isOfflineForVenue = function(venueId, universeId, success, failure) {
	console.log("OfflineManager: isOfflineForVenue");
	exec(function(result) { 
				console.log("OfflineManager: isOfflineForVenue: SUCCESS");
				success(result);
			}, function(err) {
				console.log("OfflineManager: isOfflineForVenue: FAILED");
				failure(err);
			}, PLUGIN_NAME, "isOfflineForVenue", [venueId, universeId]);
}

OfflineManager.prototype.getOfflineVenues = function(success, failure) {
	console.log("OfflineManager: getOfflineVenues");
	exec(function(result) { 
				console.log("OfflineManager: getOfflineVenues: SUCCESS");
				success(result);
			}, function(err) {
				console.log("OfflineManager: getOfflineVenues: FAILED");
				failure(err);
			}, PLUGIN_NAME, "getOfflineVenues", []);
}

OfflineManager.prototype.getOfflineUniversesForVenue = function(venueId, success, failure) {
	console.log("OfflineManager: getOfflineUniversesForVenue");
	exec(function(result) { 
				console.log("OfflineManager: getOfflineUniversesForVenue: SUCCESS");
				success(result);
			}, function(err) {
				console.log("OfflineManager: getOfflineUniversesForVenue: FAILED");
				failure(err);
			}, PLUGIN_NAME, "getOfflineUniversesForVenue", [venueId]);
}

module.exports = OfflineManager;


