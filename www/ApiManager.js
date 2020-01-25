var exec = cordova.exec;
var PLUGIN_NAME = "Mapwize"

function ApiManager() {
	console.log("ApiManager: is created");
}

ApiManager.prototype.getVenueWithId = function(id, success, failure) {
	console.log("ApiManager: getVenueWithId...");
	exec(function(result) {
		console.log("ApiManager: getVenueWithId: SUCCESS");
		success(JSON.parse(result.arg));
	}, function(err) {
		console.log("ApiManager: getVenueWithId: FAILED");
		failure(err);
	}, PLUGIN_NAME, "getVenueWithId", [id]);
}
ApiManager.prototype.getVenuesWithFilter = function(filter, success, failure) {
	console.log("ApiManager: getVenuesWithFilter...");
	exec(function(result) {
		console.log("ApiManager: getVenuesWithFilter: SUCCESS");
		success(JSON.parse(result.arg));
	}, function(err) {
		console.log("ApiManager: getVenuesWithFilter: FAILED");
		failure(err);
	}, PLUGIN_NAME, "getVenuesWithFilter", [JSON.stringify(filter)]);
}
ApiManager.prototype.getVenueWithName = function(name, success, failure) {
	console.log("ApiManager: getVenueWithName...");
	exec(function(result) {
		console.log("ApiManager: getVenueWithName: SUCCESS, res: " + result.arg);
		success(JSON.parse(result.arg));
	}, function(err) {
		console.log("ApiManager: getVenueWithName: FAILED");
		failure(err);
	}, PLUGIN_NAME, "getVenueWithName", [name]);
}
ApiManager.prototype.getVenueWithAlias = function(alias, success, failure) {
	console.log("ApiManager: getVenueWithAlias...");
	exec(function(result) {
		console.log("ApiManager: getVenueWithAlias: SUCCESS");
		success(JSON.parse(result.arg));
	}, function(err) {
		console.log("ApiManager: getVenueWithAlias: FAILED");
		failure(err);
	}, PLUGIN_NAME, "getVenueWithAlias", [alias]);
}
ApiManager.prototype.getPlaceWithId = function(id, success, failure) {
	console.log("ApiManager: getPlaceWithId...");
	exec(function(result) {
		console.log("ApiManager: getPlaceWithId: SUCCESS, res: ");
		if(!!result && result.arg){
			var arg = JSON.parse(result.arg);
			console.log("getPlaceWithId, result OK..." + arg);
			console.log("getPlaceWithId, arg: " + JSON.stringify(arg));
			success(arg);
		} else{
			console.log("getPlaceWithId, no result: " + JSON.stringify(result));
			success({});
		}
	}, function(err) {
		console.log("ApiManager: getPlaceWithId: FAILED");
		failure(err);
	}, PLUGIN_NAME, "getPlaceWithId", [id]);
}
ApiManager.prototype.getPlaceWithName = function(name, venueId, success, failure) {
	console.log("ApiManager: getPlacesWithName...");
	exec(function(result) {
		console.log("ApiManager: getPlacesWithName: SUCCESS");
		success(JSON.parse(result.arg));
	}, function(err) {
		console.log("ApiManager: getPlacesWithName: FAILED");
		failure(err);
	}, PLUGIN_NAME, "getPlaceWithName", [name, venueId]);
}
ApiManager.prototype.getPlaceWithAlias = function(alias, venueId, success, failure) {
	console.log("ApiManager: getPlaceWithAlias...");
	exec(function(result) {
		console.log("ApiManager: getPlaceWithAlias: SUCCESS");
		success(JSON.parse(result.arg));
	}, function(err) {
		console.log("ApiManager: getPlaceWithAlias: FAILED");
		failure(err);
	}, PLUGIN_NAME, "getPlaceWithAlias", [alias, venueId]);
}
ApiManager.prototype.getPlacesWithFilter = function(filter, success, failure) {
	console.log("ApiManager: getPlacesWithFilter...");
	exec(function(result) {
		console.log("ApiManager: getPlacesWithFilter: SUCCESS");
		success(JSON.parse(result.arg));
	}, function(err) {
		console.log("ApiManager: getPlacesWithFilter: FAILED");
		failure(err);
	}, PLUGIN_NAME, "getPlacesWithFilter", [JSON.stringify(filter)]);
}
ApiManager.prototype.getPlaceListWithId = function(id, success, failure) {
	console.log("ApiManager: getPlaceListWithId...");
	exec(function(result) {
		console.log("ApiManager: getPlaceListWithId: SUCCESS");
		success(JSON.parse(result.arg));
	}, function(err) {
		console.log("ApiManager: getPlaceListWithId: FAILED");
		failure(err);
	}, PLUGIN_NAME, "getPlaceListWithId", [id]);
}
ApiManager.prototype.getPlaceListWithName = function(name, venueId, success, failure) {
	console.log("ApiManager: getPlaceListWithName...");
	exec(function(result) {
		console.log("ApiManager: getPlaceListWithName: SUCCESS");
		success(JSON.parse(result.arg));
	}, function(err) {
		console.log("ApiManager: getPlaceListWithName: FAILED");
		failure(err);
	}, PLUGIN_NAME, "getPlaceListWithName", [name, venueId]);
}
ApiManager.prototype.getPlaceListWithAlias = function(alias, venueId, success, failure) {
	console.log("ApiManager: getPlaceListWithAlias...");
	exec(function(result) {
		console.log("ApiManager: getPlaceListWithAlias: SUCCESS");
		success(JSON.parse(result.arg));
	}, function(err) {
		console.log("ApiManager: getPlaceListWithAlias: FAILED");
		failure(err);
	}, PLUGIN_NAME, "getPlaceListWithAlias", [alias, venueId]);
}
ApiManager.prototype.getPlaceListsWithFilter = function(filter, success, failure) {
	console.log("ApiManager: getPlaceListsWithFilter...");
	exec(function(result) {
		console.log("ApiManager: getPlaceListsWithFilter: SUCCESS");
		success(JSON.parse(result.arg));
	}, function(err) {
		console.log("ApiManager: getPlaceListsWithFilter: FAILED");
		failure(err);
	}, PLUGIN_NAME, "getPlaceListsWithFilter", [JSON.stringify(filter)]);
}
ApiManager.prototype.getUniverseWithId = function(id, success, failure) {
	console.log("ApiManager: getUniverseWithId...");
	exec(function(result) {
		console.log("ApiManager: getUniverseWithId: SUCCESS");
		success(JSON.parse(result.arg));
	}, function(err) {
		console.log("ApiManager: getUniverseWithId: FAILED");
		failure(err);
	}, PLUGIN_NAME, "getUniverseWithId", [id]);
}
ApiManager.prototype.getUniversesWithFilter = function(filter, success, failure) {
	console.log("ApiManager: getUniversesWithFilter...");
	exec(function(result) {
		console.log("ApiManager: getUniversesWithFilter: SUCCESS");
		success(JSON.parse(result.arg));
	}, function(err) {
		console.log("ApiManager: getUniversesWithFilter: FAILED");
		failure(err);
	}, PLUGIN_NAME, "getUniversesWithFilter", [JSON.stringify(filter)]);
}
ApiManager.prototype.getAccessibleUniversesWithVenue = function(venueId, success, failure) {
	console.log("ApiManager: getAccessibleUniversesWithVenue...");
	exec(function(result) {
		console.log("ApiManager: getAccessibleUniversesWithVenue: SUCCESS");
		success(JSON.parse(result.arg));
	}, function(err) {
		console.log("ApiManager: getAccessibleUniversesWithVenue: FAILED");
		failure(err);
	}, PLUGIN_NAME, "getAccessibleUniversesWithVenue", [venueId]);
}
ApiManager.prototype.searchWithParams = function(searchParams, success, failure) {
	console.log("ApiManager: searchWithParams...");
	if (searchParams) {
		if (!searchParams.objectClass) {
			console.log("ApiManager: searchWithParams, adding objectClass...");
			searchParams.objectClass = ['place', 'placeList', 'venue'];
		};
	} else {
		searchParams = {objectClass: []};
	}
	
	exec(function(result) {
		console.log("ApiManager: searchWithParams: SUCCESS");
		success(JSON.parse(result.arg));
	}, function(err) {
		console.log("ApiManager: searchWithParams: FAILED");
		failure(err);
	}, PLUGIN_NAME, "searchWithParams", [JSON.stringify(searchParams)]);
}

ApiManager.prototype.getDirectionWithFrom = function(directionPointFrom, directionPointTo, isAccessible, success, failure) {
	console.log("ApiManager: getDirectionWithFrom...");
	exec(function(result) {
		console.log("ApiManager: getDirectionWithFrom: SUCCESS");
		success(JSON.parse(result.arg));
	}, function(err) {
		console.log("ApiManager: getDirectionWithFrom: FAILED");
		failure(err);
	}, PLUGIN_NAME, "getDirectionWithFrom", [JSON.stringify(directionPointFrom), JSON.stringify(directionPointTo), isAccessible]);
}

ApiManager.prototype.getDirectionWithDirectionPointsFrom = function(directionPointFrom, directionPointsListTo, isAccessible, success, failure) {
	console.log("ApiManager: getDirectionWithDirectionPointsFrom...");
	exec(function(result) {
		console.log("ApiManager: getDirectionWithDirectionPointsFrom: SUCCESS");
		success(JSON.parse(result.arg));
	}, function(err) {
		console.log("ApiManager: getDirectionWithDirectionPointsFrom: FAILED");
		failure(err);
	}, PLUGIN_NAME, "getDirectionWithDirectionPointsFrom", [JSON.stringify(directionPointFrom), JSON.stringify(directionPointsListTo), isAccessible]);
}

ApiManager.prototype.getDirectionWithWayPointsFrom = function(directionPointFrom, directionPointTo, waypointsList, isAccessible, success, failure) {
	console.log("ApiManager: getDirectionWithWayPointsFrom...");
	exec(function(result) {
		console.log("ApiManager: getDirectionWithWayPointsFrom: SUCCESS");
		success(JSON.parse(result.arg));
	}, function(err) {
		console.log("ApiManager: getDirectionWithWayPointsFrom: FAILED");
		failure(err);
	}, PLUGIN_NAME, "getDirectionWithWayPointsFrom", [JSON.stringify(directionPointFrom), JSON.stringify(directionPointTo), JSON.stringify(waypointsList), isAccessible]);
}

ApiManager.prototype.getDirectionWithDirectionAndWayPointsFrom = function(directionPointFrom, directionpointsToList, waypointsList, isAccessible, success, failure) {
	console.log("ApiManager: getDirectionWithDirectionAndWayPointsFrom...");
	exec(function(result) {
		console.log("ApiManager: getDirectionWithDirectionAndWayPointsFrom: SUCCESS");
		success(JSON.parse(result.arg));
	}, function(err) {
		console.log("ApiManager: getDirectionWithDirectionAndWayPointsFrom: FAILED");
		failure(err);
	}, PLUGIN_NAME, "getDirectionWithDirectionAndWayPointsFrom", [JSON.stringify(directionPointFrom), JSON.stringify(directionpointsToList), JSON.stringify(waypointsList), isAccessible]);
}

ApiManager.prototype.getDistancesWithFrom = function(directionPointFrom, directionpointsToList, isAccessible, sortByTraveltime, success, failure) {
	console.log("ApiManager: getDistancesWithFrom...");
	exec(function(result) {
		console.log("ApiManager: getDistancesWithFrom: SUCCESS");
		success(JSON.parse(result.arg));
	}, function(err) {
		console.log("ApiManager: getDistancesWithFrom: FAILED");
		failure(err);
	}, PLUGIN_NAME, "getDistancesWithFrom", [JSON.stringify(directionPointFrom), JSON.stringify(directionpointsToList), isAccessible, sortByTraveltime]);
}

module.exports = ApiManager;


