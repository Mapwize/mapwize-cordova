//var exec = require('cordova/exec');
var exec = cordova.exec;
var PLUGIN_NAME = "Mapwize"

function ApiManager() {
	console.log("ApiManager: is created");
}

ApiManager.prototype.getVenueWithId = function(id, success, failure) {
	console.log("ApiManager: getVenueWithId...");
	exec(function(result) {
		console.log("ApiManager: getVenueWithId: SUCCESS");
		success(result);
	}, function(err) {
		console.log("ApiManager: getVenueWithId: FAILED");
		failure(err);
	}, PLUGIN_NAME, "getVenueWithId", [id]);
}
ApiManager.prototype.getVenuesWithFilter = function(filter, success, failure) {
	console.log("ApiManager: getVenuesWithFilter...");
	exec(function(result) {
		console.log("ApiManager: getVenuesWithFilter: SUCCESS");
		success(result);
	}, function(err) {
		console.log("ApiManager: getVenuesWithFilter: FAILED");
		failure(err);
	}, PLUGIN_NAME, "getVenuesWithFilter", [filter]);
}
ApiManager.prototype.getVenueWithName = function(name, success, failure) {
	console.log("ApiManager: getVenueWithName...");
	exec(function(result) {
		console.log("ApiManager: getVenueWithName: SUCCESS");
		success(result);
	}, function(err) {
		console.log("ApiManager: getVenueWithName: FAILED");
		failure(err);
	}, PLUGIN_NAME, "getVenueWithName", [name]);
}
ApiManager.prototype.getVenueWithAlias = function(alias, success, failure) {
	console.log("ApiManager: getVenueWithAlias...");
	exec(function(result) {
		console.log("ApiManager: getVenueWithAlias: SUCCESS");
		success(result);
	}, function(err) {
		console.log("ApiManager: getVenueWithAlias: FAILED");
		failure(err);
	}, PLUGIN_NAME, "getVenueWithAlias", [alias]);
}
ApiManager.prototype.getPlaceWithId = function(id, success, failure) {
	console.log("ApiManager: getPlaceWithId...");
	exec(function(result) {
		console.log("ApiManager: getPlaceWithId: SUCCESS");
		success(result);
	}, function(err) {
		console.log("ApiManager: getPlaceWithId: FAILED");
		failure(err);
	}, PLUGIN_NAME, "getPlaceWithId", [id]);
}
ApiManager.prototype.getPlacesWithName = function(name, venueId, success, failure) {
	console.log("ApiManager: getPlacesWithName...");
	exec(function(result) {
		console.log("ApiManager: getPlacesWithName: SUCCESS");
		success(result);
	}, function(err) {
		console.log("ApiManager: getPlacesWithName: FAILED");
		failure(err);
	}, PLUGIN_NAME, "getPlacesWithName", [name, venueId]);
}
ApiManager.prototype.getPlaceWithAlias = function(alias, venueId, success, failure) {
	console.log("ApiManager: getPlacesWithAlias...");
	exec(function(result) {
		console.log("ApiManager: getPlacesWithAlias: SUCCESS");
		success(result);
	}, function(err) {
		console.log("ApiManager: getPlacesWithAlias: FAILED");
		failure(err);
	}, PLUGIN_NAME, "getPlaceWithAlias", [alias, venueId]);
}
ApiManager.prototype.getPlacesWithFilter = function(filter, success, failure) {
	console.log("ApiManager: getPlacesWithFilter...");
	exec(function(result) {
		console.log("ApiManager: getPlacesWithFilter: SUCCESS");
		success(result);
	}, function(err) {
		console.log("ApiManager: getPlacesWithFilter: FAILED");
		failure(err);
	}, PLUGIN_NAME, "getPlacesWithFilter", [filter]);
}
ApiManager.prototype.getPlaceListWithId = function(id, success, failure) {
	console.log("ApiManager: getPlaceListWithId...");
	exec(function(result) {
		console.log("ApiManager: getPlaceListWithId: SUCCESS");
		success(result);
	}, function(err) {
		console.log("ApiManager: getPlaceListWithId: FAILED");
		failure(err);
	}, PLUGIN_NAME, "getPlaceListWithId", [id]);
}
ApiManager.prototype.getPlaceListsWithName = function(name, venueId, success, failure) {
	console.log("ApiManager: getPlaceListsWithName...");
	exec(function(result) {
		console.log("ApiManager: getPlaceListsWithName: SUCCESS");
		success(result);
	}, function(err) {
		console.log("ApiManager: getPlaceListsWithName: FAILED");
		failure(err);
	}, PLUGIN_NAME, "getPlaceListsWithName", [name, venueId]);
}
ApiManager.prototype.getPlaceListsWithAlias = function(alias, venueId, success, failure) {
	console.log("ApiManager: getPlaceListsWithAlias...");
	exec(function(result) {
		console.log("ApiManager: getPlaceListsWithAlias: SUCCESS");
		success(result);
	}, function(err) {
		console.log("ApiManager: getPlaceListsWithAlias: FAILED");
		failure(err);
	}, PLUGIN_NAME, "getPlaceListsWithAlias", [alias, venueId]);
}
ApiManager.prototype.getPlaceListsWithFilter = function(filter, success, failure) {
	console.log("ApiManager: getPlaceListsWithFilter...");
	exec(function(result) {
		console.log("ApiManager: getPlaceListsWithFilter: SUCCESS");
		success(result);
	}, function(err) {
		console.log("ApiManager: getPlaceListsWithFilter: FAILED");
		failure(err);
	}, PLUGIN_NAME, "getPlaceListsWithFilter", [filter]);
}
ApiManager.prototype.getUniverseWithId = function(id, success, failure) {
	console.log("ApiManager: getUniverseWithId...");
	exec(function(result) {
		console.log("ApiManager: getUniverseWithId: SUCCESS");
		success(result);
	}, function(err) {
		console.log("ApiManager: getUniverseWithId: FAILED");
		failure(err);
	}, PLUGIN_NAME, "getUniverseWithId", [id]);
}
ApiManager.prototype.getUniversesWithFilter = function(filter, success, failure) {
	console.log("ApiManager: getUniversesWithFilter...");
	exec(function(result) {
		console.log("ApiManager: getUniversesWithFilter: SUCCESS");
		success(result);
	}, function(err) {
		console.log("ApiManager: getUniversesWithFilter: FAILED");
		failure(err);
	}, PLUGIN_NAME, "getUniversesWithFilter", [filter]);
}
ApiManager.prototype.getAccessibleUniversesWithVenue = function(venueId, success, failure) {
	console.log("ApiManager: getAccessibleUniversesWithVenue...");
	exec(function(result) {
		console.log("ApiManager: getAccessibleUniversesWithVenue: SUCCESS");
		success(result);
	}, function(err) {
		console.log("ApiManager: getAccessibleUniversesWithVenue: FAILED");
		failure(err);
	}, PLUGIN_NAME, "getAccessibleUniversesWithVenue", [venueId]);
}
ApiManager.prototype.searchWithParams = function(searchParams, success, failure) {
	console.log("ApiManager: searchWithParams...");
	exec(function(result) {
		console.log("ApiManager: searchWithParams: SUCCESS");
		success(result);
	}, function(err) {
		console.log("ApiManager: searchWithParams: FAILED");
		failure(err);
	}, PLUGIN_NAME, "searchWithParams", [searchParams]);
}

//+ (void)getDirectionWithFrom:(id<MWZDirectionPoint>) from to:(id<MWZDirectionPoint>) to isAccessible:(BOOL) isAccessible callbackId:(NSString*) callbackId;
//+ (void)getDirectionWithFrom:(id<MWZDirectionPoint>) from tos:(NSArray<id<MWZDirectionPoint>>*) tos isAccessible:(BOOL) isAccessible callbackId:(NSString*) callbackId;
//+ (void)getDirectionWithFrom:(id<MWZDirectionPoint>) from to:(id<MWZDirectionPoint>) to waypoints:(NSArray<id<MWZDirectionPoint>>*) waypoints isAccessible:(BOOL) isAccessible callbackId:(NSString*) callbackId;
//+ (void)getDirectionWithFrom:(id<MWZDirectionPoint>) from tos:(NSArray<id<MWZDirectionPoint>>*) tos waypoints:(NSArray<id<MWZDirectionPoint>>*) waypoints isAccessible:(BOOL) isAccessible callbackId:(NSString*) callbackId;
//+ (void)getDistanceWithFrom:(id<MWZDirectionPoint>) from tos:(NSArray<id<MWZDirectionPoint>>*) tos isAccessible:(BOOL) isAccessible sortByTravelTime:(BOOL) sort callbackId:(NSString*) callbackId;



module.exports = ApiManager;


