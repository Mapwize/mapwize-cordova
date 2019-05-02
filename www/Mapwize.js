var exec = cordova.exec;

var PLUGIN_NAME = "Mapwize"

function Mapwize() {
	console.log("Mapwize: is created");
}

Mapwize.prototype.createMapwizeView = function(options, callbacks, success, failure) {
	console.log("Mapwize: createMapwizeView...");
	exec(function(result) { 
				console.log("Mapwize: selectPlace: SUCCESS");
				success(JSON.parse(result.arg));
			}, function(err) {
				console.log("Mapwize: selectPlace: FAILED");
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

var mapwize = new Mapwize();
module.exports = mapwize;
