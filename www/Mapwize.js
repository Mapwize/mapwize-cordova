//var exec = require('cordova/exec');
var exec = cordova.exec;

var PLUGIN_NAME = "Mapwize"

function Mapwize() {
	console.log("Mapwize: is created");
}

Mapwize.prototype.createMapwizeView = function(options, success, failure) {
	console.log("Mapwize: createMapwizeView...");
	exec(function(result) { 
				console.log("Mapwize: selectPlace: SUCCESS");
				success(result);
			}, function(err) {
				console.log("Mapwize: selectPlace: FAILED");
				failure(err);
			}, PLUGIN_NAME, "createMapwizeView", [JSON.stringify(options)]);
	return new MapwizeView();
}






var mapwize = new Mapwize();
module.exports = mapwize;
