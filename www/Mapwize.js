var exec = cordova.exec;
var PLUGIN_NAME = "Mapwize"

function Mapwize() {
	console.log("Mapwize: is created");
}

Mapwize.prototype.createMapwizeView = function (options, uisettings, success, failure) {
	console.log("Mapwize: createMapwizeView1...");
	console.log("Mapwize: createMapwizeView...options: " + JSON.stringify(options));
	console.log("Mapwize: createMapwizeView...uisettings: " + JSON.stringify(uisettings));
	
	exec(function(result) { 
				console.log("Mapwize: createMapwizeView: SUCCESS ");
				success(result);
			}, function(err) {
				console.log("Mapwize: createMapwizeView: FAILED");
				failure(err);
			}, PLUGIN_NAME, "createMapwizeView", [JSON.stringify(options), JSON.stringify(uisettings)]);
	return new MapwizeView();
}

Mapwize.prototype.createOfflineManager = function (styleUrl, success, failure) {
	console.log("Mapwize: createOfflineManager...");
	return new OfflineManager(styleUrl);
}

Mapwize.prototype.createApiManager = function (success, failure) {
	console.log("Mapwize: createApiManager...");
	return new ApiManager();
}

var mapwize = new Mapwize();
module.exports = mapwize;