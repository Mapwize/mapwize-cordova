var exec = cordova.exec;

var PLUGIN_NAME = "Mapwize"

function Mapwize() {
	console.log("Mapwize: is created");
}

Mapwize.prototype.createMapwizeView = function(options, success, failure) {
	console.log("Mapwize: createMapwizeView...");
	exec(function(result) { 
				console.log("Mapwize: createMapwizeView: SUCCESS ");
				success(JSON.parse(result));
			}, function(err) {
				console.log("Mapwize: createMapwizeView: FAILED");
				failure(err);
			}, PLUGIN_NAME, "createMapwizeView", [JSON.stringify(options)]);
	return new MapwizeView();
}

Mapwize.prototype.createOfflineManager = function(styleUrl, success, failure) {
	console.log("Mapwize: createOfflineManager...");
	return new OfflineManager(styleUrl);
}

Mapwize.prototype.createApiManager = function(success, failure) {
	console.log("Mapwize: createApiManager...");
	return new ApiManager();
}

Mapwize.prototype.destroyMapwizeView = function(success, failure) {
	console.log("Mapwize: destroyMapwizeView");
	exec(function(result) { 
				console.log("Mapwize: destroyMapwizeView: SUCCESS");
				success(JSON.parse(result));
			}, function(err) {
				console.log("Mapwize: destroyMapwizeView: FAILED");
				failure(err);
			}, PLUGIN_NAME, "destroyMapwizeView", []);
}


var mapwize = new Mapwize();
module.exports = mapwize;