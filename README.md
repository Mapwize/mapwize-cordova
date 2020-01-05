# Mapwize Cordova Plugin

Use this plugin to integrate native Mapwize indoor maps in your iOS and Android Cordova apps.

This plugin is open-source. Don't hesitate to contribute to it!

For documentation about Mapwize SDK objects like Venue, Place, MapOptions... Please refer to the Mapwize SDK documentation on [docs.mapwize.io](https://docs.mapwize.io).

## Compatibility

The plugin is compatible with Cordova 8 / cordova-ios 4.5+ / cordova-android 8.0+ / iOS 10+ / Android 5+.

## Demo app

A simple Ionic app using the plugin is available in this repository [mapwize-cordova-demo](https://github.com/Mapwize/mapwize-cordova-demo). It's a great way to see how it works.

## Preparation

### Installing plugin

```
cordova plugin add mapwize-cordova-plugin \
 --variable MWZAPIKEY="XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX" \
 --variable MWZSERVERURL="https://api.mapwize.io/" \
 --variable MWZSTYLEURL="https://outdoor.mapwize.io/styles/mapwize/style.json?key=XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX" \
 --variable MWZREFRESHINTERVAL=100

```

or from github

```
cordova plugin add https://github.com/Mapwize/mapwize-cordova.git \
 --variable MWZAPIKEY="XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX" \
 --variable MWZSERVERURL="https://api.mapwize.io/" \
 --variable MWZSTYLEURL="https://outdoor.mapwize.io/styles/mapwize/style.json?key=XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX" \
 --variable MWZREFRESHINTERVAL=100
```

### Setting up your ionic project

Configure Android launchMode in config.xml.

```
...
<preference name="androidLaunchMode" value="singleTask" />
...
```



To use the plugin, declare Mapwize symbol for your components.

E.g.

```
import ...;

declare var Mapwize : any;

@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage {
...
```



## MapwizeView

### Creating MapwizeView

```
this.mapwizeView = Mapwize.createMapwizeView(
      {
        floor: 0,
        language: "en",
        universeId: "",
        restrictContentToVenueId: "",
        restrictContentToOrganizationId: "",
        centerOnVenueId: "xxxxxxxxxxxxxxxxxxxxxxxx",
        centerOnPlaceId: "xxxxxxxxxxxxxxxxxxxxxxxx",
        showCloseButton: true,
        showInformationButtonForPlaces: true,
        showInformationButtonForPlaceLists: false
        
      },
      {
      	mainColor: #FF0000,
		menuButtonIsHidden: true, 
		followUserButtonIsHidden: true,
		floorControllerIsHidden: true,
		compassIsHidden: true
      },

      () => {
        // Handle  successfull creation
      }, (err) => {
        // Handle  failed creation
        console.log("Error: " + err.message + ", localized message: " + err.locMessage);
      });
```

#### Additional parameters

Options
```
showCloseButton: <<boolean>>, hide/show close button on MapwizeView (default: false)
showInformationButtonForPlaces: <<boolean>>, hide/show Information button on selecting a 		Place in case the Place doesn't include the "cordovaShowInformationButton" flag.			(default: true)
showInformationButtonForPlaceLists: <<boolean>>, hide/show Information button on selecting a 	 PlaceList in case the PlaceList doesn't include the "cordovaShowInformationButton" flag.
	(default: true)
```

UISettings fields
```
mainColor: The main color of Mapwize in hex form either #FF0000 or #99FF0000,
menuButtonIsHidden: Is menu button hidden true/false
followUserButtonIsHidden: Is follow user button hidden true/false
floorControllerIsHidden: Is floor controller button hidden true/false
compassIsHidden: Is compass hidden true/false
```

### Callback functions

To receive callbacks you need to set the supported callback functions.

```
...
this.mapwizeView.setCallback(
        {
          DidLoad: function() {
            console.log("DidLoad...");
            // Handle  DidLoad
          },
          DidTapOnFollowWithoutLocation: function(arg) {
            console.log("The cordova result(DidTapOnFollowWithoutLocation): " + JSON.stringify(arg));
            // Handle DidTapOnFollowWithoutLocation
          },
          DidTapOnMenu: function(arg) {
            console.log("The cordova result(DidTapOnMenu): " + JSON.stringify(arg));
            // Handle DidTapOnMenu
          },
          shouldShowInformationButtonFor: function(arg) {
            console.log("The cordova result(shouldShowInformationButtonFor): " + JSON.stringify(arg));
            // Handle shouldShowInformationButtonFor
          },
          TapOnPlaceInformationButton: function(arg) {
            console.log("The cordova result: " + JSON.stringify(arg));
            // Handle  TapOnPlaceInformationButton
          },
          TapOnPlaceListInformationButton: function(arg) {
            console.log("The cordova result(TapOnPlaceListInformationButton): " + JSON.stringify(arg));
            // Handle  TapOnPlaceListInformationButton
          }
        }
      );
 ...
```

#### DidLoad

Triggers when the View has loaded.

#### DidTapOnFollowWithoutLocation

---

#### DidTapOnMenu

Triggers when the Menu icon is tapped.

#### shouldShowInformationButtonFor~~

#### TapOnPlaceInformationButton (place)

Triggers when a place is selected and the "Information" button is tapped.

Returns a Place object like this:

```
{
	"_id": "5bc49413bf0ed600114db212",
	"venueId": "56b20714c3fa800b00d8f0b5",
	"name": "Mapwize",
	"alias": "mapwize",
	"cache": {
		"30": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB4AAAAeCAYAAAA7MK6iAAAACXBIWXMAAAsSAAALEgHS3X78AAABvUlEQVRIx8VXvUrEQBDOI6S3SWt3pWUq6xQ+QPAJ7hHO0sLCSkTwECxEi2B3XaxEsIiIhaUgYiMEbcRq3G/ZDWN+NrveJhn44LjszLczOz+7QWApRBQKJAILgaVArrBU/+FbGPgSYSwWyIjJz+c3vd2+SOB3TbA2XocwUh5Jeb58oNXuBZ1u7tPRxt4f4D98wxom0I1cSRG2EtqPJ3etZF3AWugogY3EljSFxtdrSVfbx9aEdUAXNpSkNp7Sx9O7k5cm72FLSWI60xK79EHKyZXnZeuZ60RaJ7ymsOuEaysZuj+48U6qAdtKYk6coR59hrgt5KrmM96RZA2aFFsaRUOwxmSD1XlYlQ8agEnJVkw2wFGVl+qzvaHyQczsLGQ2o9bGIlZ1nUtiNPuxiME1PfFUoZ4suWQ5Xe+cDU4MDl5OkZ67QzcQNqejakD0tczzrUO5YxOwxqJl5o05PNKQSBtjETsaaiw2vGXEM5+3j45byMx43xro6pNaXfagYEqWPkDXmrROjnP5T8JBh5Vf6nq3xpkXegOoQVPi4RvWMMKi80wdvC94o9DPF42aFM5eWjxp5rUHG3+4zV2eLL/SaxnLIxHfTwAAAABJRU5ErkJggg=="
	},
	"style": {
		"markerUrl": "https://mapwize.blob.core.windows.net/placetypes/30/room.png",
		"fillColor": "#8b72a2",
		"strokeColor": "#8b72a2",
		"markerDisplay": true,
		"strokeWidth": 1,
		"fillOpacity": 0.1,
		"strokeOpacity": 0.5
	},
	"universes": [{
		"_id": "57ec94f8098881c02bdc5eb8",
		"name": ""
	}],
	"translations": [{
		"_id": "5c9c054383ae8c00130aa3ac",
		"title": "Mapwize",
		"subTitle": "",
		"details": "",
		"language": "fr"
	}, {
		"_id": "5c9c054383ae8c00130aa3ab",
		"title": "Mapwize",
		"subTitle": "",
		"details": "",
		"language": "en"
	}, {
		"_id": "5c9c054383ae8c00130aa3aa",
		"title": "Mapwize",
		"subTitle": "",
		"details": "",
		"language": "nl"
	}],
	"isSearchable": true,
	"isVisible": true,
	"isClickable": true,
	"order": 0,
	"floor": 2,
	"marker": {
		"latitude": 50.6326106893481,
		"longitude": 3.02014356351719
	},
	"entrance": {
		"latitude": 50.6326106893481,
		"longitude": 3.02014356351719
	},
	"geometry": {
		"coordinates": [
			[
				[3.02006240934133, 50.6326447714475],
				[3.02012697356986, 50.6326715469577],
				[3.02022471769305, 50.6325766118717],
				[3.02015982801095, 50.6325498317385],
				[3.02006240934133, 50.6326447714475]
			]
		],
		"type": "Polygon"
	},
	"data": {
		"name": "LB204-LB206",
		"companyMapwizeName": "Mapwize",
		"companyMapwizeTitle": "Mapwize"
	}
}
```

#### TapOnPlaceListInformationButton (place)

Triggers when a place is selected and the "Information" button is tapped.

Returns a PlaceList object like this:

```
{
	"_id": "5784fc5f7f2a900b0055f603",
	"venueId": "56b20714c3fa800b00d8f0b5",
	"name": "Bathrooms",
	"alias": "bathrooms",
	"translations": [{
		"_id": "5784fc5f7f2a900b0055f604",
		"title": "Toilettes",
		"subTitle": "",
		"details": "",
		"language": "fr"
	}, {
		"_id": "5784fc817f2a900b0055f607",
		"title": "Bathroom",
		"subTitle": "",
		"details": "",
		"language": "en"
	}, {
		"_id": "5784fc817f2a900b0055f606",
		"title": "Toiletten",
		"subTitle": "",
		"details": "",
		"language": "nl"
	}, {
		"_id": "5784fc817f2a900b0055f605",
		"title": "厕所",
		"subTitle": "",
		"details": "",
		"language": "zh"
	}],
	"placeIds": ["57036c2eb247f50b00a0746a", "57036cd6b247f50b00a0746e", "57036ce8b247f50b00a07470", "57036f56b247f50b00a0748e", "57036dc3b247f50b00a0747c", "57036be6b247f50b00a07466", "57036c1bb247f50b00a07468", "57036dacb247f50b00a07478", "57036f42b247f50b00a0748c", "57036e7fb247f50b00a07482", "57027466ab184e0b009f4502", "57036efbb247f50b00a07486", "57036f28b247f50b00a0748a", "57036f11b247f50b00a07488", "57036f6eb247f50b00a07490", "570275a8ab184e0b009f450c", "57036debb247f50b00a07480", "57036bcbb247f50b00a07464", "57036f80b247f50b00a07492", "57036d40b247f50b00a07472", "57037073b247f50b00a074a0", "57036eeab247f50b00a07484", "57027445ab184e0b009f4500", "57037064b247f50b00a0749e", "57036d6bb247f50b00a07474", "56e6941bf195860b00fddc16", "5757e9b326e1df17005fe78b", "57036f9ab247f50b00a07496", "57036fadb247f50b00a07498", "57036fbfb247f50b00a0749a", "57036fd8b247f50b00a0749c", "5bc724831ca3a80012a4a607", "5bc724625608e40012436ec8", "5bc724471ca3a80012a4a5ef", "5bc723f2bae3b90012cb95f3"],
	"icon": "https:\/\/mapwize.blob.core.windows.net\/placetypes\/30\/toilet.png",
	"isSearchable": true
}
```

#### MapwizeView functions

These functions can be used after the view has been loaded.

#### selectPlace(id, centerOn, successFn, failureFn)

Selects the Place object of the given *id* in the view.

```
id: id of the Place object
centerOn: if the View has to position the Place to the center
successFn(place): returns the Place object.
failureFn(err): returns the error object. {messsage: <<error message>>, locMessage: <<localized error message>>}
```



```
this.mapwiseView.selectPlace(
        "5bc49413bf0ed600114db212", true, 
        (place) => {console.log("Select place successfully returned: " + JSON.stringify(place))},
        (err) => {console.log("Select place failed err: " + err.message)}
      );
```

#### selectPlaceList(id, successFn, failureFn)

Selects the PlaceList object of the given *id* in the view.

```
id: id of the PlaceList object
successFn(placeList): returns the Place object.
failureFn(err): returns the error object. {messsage: <<error message>>, locMessage: <<localized error message>>}
```



```
this.mapwiseView.selectPlaceList(
        "5bc49413bf0ed600114db212",
        (placeList) => {console.log("Select placeList successfully returned: " + JSON.stringify(placeList))},
        (err) => {console.log("Select placeList failed err: " + err.message)}
      );
```

#### grantAccess(token, successFn, failureFn)

Grants access.

```
token: The access token
successFn(): returns on success.
failureFn(err): returns on failure. {messsage: <<error message>>, locMessage: <<localized error message>>}
```



```
this.mapwiseView.grantAccess(
        "xxxxxxxxxxxxxxxxxx", 
        () => {console.log("grantAccess successfully returned..."},
        (err) => {console.log("grantAccess failed err: " + err.message)}
      );
```

#### unselectContent(closeInfo, successFn, failureFn)

TBD

```
closeInfo: *** tbd ***
successFn(): returns on success.
failureFn(err): returns on failure. {messsage: <<error message>>, locMessage: <<localized error message>>}
```



```
this.mapwiseView.unselectContent(
        true, 
        () => {console.log("unselectContent successfully returned..."},
        (err) => {console.log("unselectContent failed err: " + err.message)}
      );
```

#### setPlaceStyle(id, style, successFn, failureFn)

Sets the style of the given Place.

```
id: id of the Place object
style: the style of the Place of the following JSON format:
{
    "markerUrl": "https://res.cloudinary.com/contexeo/image/upload/v1548842542/Lego/meeting-red.png",
    "markerDisplay": true,
    "fillColor": "#d90a1b",
    "fillOpacity": 0.5,
    "strokeColor": "#d90a1b",
    "strokeOpacity": 1,
    "strokeWidth": 2
}

successFn(place): returns the Place object.
failureFn(err): returns the error object. {messsage: <<error message>>, locMessage: <<localized error message>>}
```



```
this.mapwizeView.setPlaceStyle(
            place._id,
            {
                "markerUrl": "https://res.cloudinary.com/contexeo/image/upload/v1548842542/Lego/meeting-red.png",
                "markerDisplay": true,
                "fillColor": "#d90a1b",
                "fillOpacity": 0.5,
                "strokeColor": "#d90a1b",
                "strokeOpacity": 1,
                "strokeWidth": 2
            },
            (res) => {console.log("MapwizeView setPlaceStyle successfully returned: " + JSON.stringify(res))},
            (err) => {console.log("MapwizeView setPlaceStyle failed err: " + JSON.stringify(err))}
          );
```

## Localization

For localized texts,

The localization files should be placed in directories named 'languages/ios' and 'languages/android' under the root directory of your ionic project

### Android localization

1. Create value-<<2-letter country code>> directories for each language
2. Create a translation content file named "strings2.xml" with the following content (the values to be translated):

```
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <!-- <string name="app_name">MapwizeUIComponents</string> -->
    <!-- Strings used by Mapwize Fragment. You can use them to translate the Fragment
    text or change them.
    Those values override the default strings value found in mapwizecomponent project-->
    <string name="time_placeholder">%1$d min</string>
    <string name="floor_placeholder">Floor %1$s</string>
    <string name="search_in_placeholder">Search in %1$s…</string>
    <string name="search_venue">Search a venue…</string>
    <string name="loading_venue_placeholder">Loading %1$s…</string>
    <string name="current_location">Current location</string>
    <string name="choose_language">Choose your language</string>
    <string name="choose_universe">Choose your universe</string>
    <string name="direction">Direction</string>
    <string name="information">Information</string>
    <string name="starting_point">Starting point</string>
    <string name="destination">Destination</string>
</resources>
```

### Ios localization

1. Create <<2-letter country code>>.lproj directories for each language
2. Create a translation content file named "Localizable.strings" with the following content (the values to be translated):

```
"Direction" = "Direction";
"Information" = "Information";
"Search a venue..." = "Search a venue...";
"Entering in %@..." = "Entering in %@...";
"Search in %@..." = "Search in %@...";
"Destination" = "Destination";
"Starting point" = "Starting point";
"Current location" = "Current location";
"No results" = "No results";
"Floor %@" = "Floor %@";
"Universes" = "Universes";
"Choose an universe to display it on the map" = "Choose a universe to display it on the map";
"Languages" = "Languages";
"Choose your preferred language" = "Choose your preferred language";
"Cancel" = "Cancel";

```

Configure and run the ionic project the usual way. The localization takes the default device language to select the proper localization file.



## API Manager

### Creating API Manager
To use API Manager functions you need to create an API Manager instance

```
this.apiManager = Mapwize.createApiManager();
```



#### #### getVenueWithId(id, successFn, failureFn)

Gets venue with the given *id*.

```
id: id of the Venue object
successFn(venue): returns the Venue object.
failureFn(err): returns the error object. {messsage: <<error message>>, locMessage: <<localized error message>>}
```


#### getVenuesWithFilter(filter, successFn, failureFn)

Gets venues according to the *filter*.

```
filter: the filter
successFn(venues): returns an array of Venues object.
failureFn(err): returns the error object. {messsage: <<error message>>, locMessage: <<localized error message>>}
```

##### The filter

The filter is an object with the following optional fields:
```
{
	venueId: <<venueId>>,
	venueIds: <<array of venueIds>>,
	universeId: <<universeId>>,
	isVisible: <<isVisible>>,
	organizationId: <<organizationId>>,
	alias: <<alias>>,
	name: <<name>>,
	floor: <<floor>>,
	latitudeMin: <<latitudeMin>>,
	latitudeMax: <<latitudeMax>>,
	longitudeMin: <<longitudeMin>>,
	longitudeMax: <<longitudeMax>>
}

```

#### getVenueWithName(name, successFn, failureFn)

Gets venue with the given *name*.

```
name: name of the Place object
successFn(venue): returns the Venue object.
failureFn(err): returns the error object. {messsage: <<error message>>, locMessage: <<localized error message>>}
```

#### getVenueWithAlias(alias, successFn, failureFn)

Gets venue with the given *alias*.

```
alias: alias of the Place object
successFn(place): returns the Place object.
failureFn(err): returns the error object. {messsage: <<error message>>, locMessage: <<localized error message>>}
```

#### getPlaceWithId(id, successFn, failureFn)

Gets the Place object of the given *id* in the view.

```
id: id of the Place object
successFn(place): returns the Place object.
failureFn(err): returns the error object. {messsage: <<error message>>, locMessage: <<localized error message>>}
```

#### getPlaceWithName(name, venueId, successFn, failureFn)

Gets the Place object of the given *name* and *venueId*.

```
name: name of the Place object
venueId: venueId of the Place object
successFn(place): returns the Place object.
failureFn(err): returns the error object. {messsage: <<error message>>, locMessage: <<localized error message>>}
```

#### getPlaceWithAlias(alias, venueId, successFn, failureFn)

Gets the Place object of the given *alias* and *venueId*.

```
alias: name of the Place object
venueId: venueId of the Place object
successFn(place): returns the Place object.
failureFn(err): returns the error object. {messsage: <<error message>>, locMessage: <<localized error message>>}
```

#### getPlacesWithFilter(filter, successFn, failureFn)

Gets venues according to the *filter*.

```
filter: the filter (for more information pls. check the getVenuesWithFilter function)
successFn(venues): returns an array of Places object.
failureFn(err): returns the error object. {messsage: <<error message>>, locMessage: <<localized error message>>}
```

#### getPlaceListWithId(id, successFn, failureFn)

Gets the PlaceList object of the given *id*.

```
id: id of the Placelist object
successFn(place): returns the PlaceList object.
failureFn(err): returns the error object. {messsage: <<error message>>, locMessage: <<localized error message>>}
```

#### getPlaceListWithName(name, venueId, successFn, failureFn)

Gets the PlaceList object of the given *name* and *venueId*.

```
name: name of the PlaceList object
venueId: venueId of the PlaceList object
successFn(place): returns the PlaceList object.
failureFn(err): returns the error object. {messsage: <<error message>>, locMessage: <<localized error message>>}
```

#### getPlaceListWithAlias(alias, venueId, successFn, failureFn)

Gets the PlaceList object of the given *alias* and *venueId*.

```
alias: alias of the PlaceList object
venueId: venueId of the PlaceList object
successFn(place): returns the PlaceList object.
failureFn(err): returns the error object. {messsage: <<error message>>, locMessage: <<localized error message>>}
```

#### getPlaceListsWithFilter(filter, successFn, failureFn)

Gets PlaceList object according to the *filter*.

```
filter: the filter (for more information pls. check the getVenuesWithFilter function)
successFn(venues): returns an array of Places object.
failureFn(err): returns the error object. {messsage: <<error message>>, locMessage: <<localized error message>>}
```

#### getUniverseWithId(id, successFn, failureFn) 

Gets Universe object of the *id*.

```
id: id of the Universe object
successFn(universe): returns the Universe object.
failureFn(err): returns the error object. {messsage: <<error message>>, locMessage: <<localized error message>>}
```

#### getUniversesWithFilter(filter, successFn, failureFn)

Gets Universe object according to the *filter*.

```
filter: the filter (for more information pls. check the getVenuesWithFilter function)
successFn(venues): returns an array of Places object.
failureFn(err): returns the error object. {messsage: <<error message>>, locMessage: <<localized error message>>}
```

#### getAccessibleUniversesWithVenue(venueId, successFn, failureFn)

Gets the accessible Universes of the Venue.

```
venueId: id of the Venue
successFn(universes): returns the array of Universes accessible for the Venue.
failureFn(err): returns the error object. {messsage: <<error message>>, locMessage: <<localized error message>>}
```

#### searchWithParams(searchParams, successFn, failureFn)

Search for Mapwize objects according to the search parameters.

```
searchParams: search parameters. For more info see below.
successFn(objects): returns an array of Mapwize objects.
failureFn(err): returns the error object. {messsage: <<error message>>, locMessage: <<localized error message>>}
```

##### Searh Parameters

The "search parameters" is an object with the following optional fields:

```
{
	query: <<query>>,
	venueId: <<venueId>>,
	venueIds: <<array of venueIds>>,
	organizationId: <<organizationId>>,
	universeId: <<universeId>>,
	objectClass: <<objectClass>>
}
```

#### getDirectionWithFrom(directionPointFrom, directionPointTo, isAccessible, successFn, failureFn)

Gets a direction of the given parameters.

```
directionPointFrom: direction point of the starting point. For more info on direction points please see below.
directionPointTo: direction point of the starting point. 
isAccessible: if the direction to get is accessible.
successFn(direction): returns the Direction object.
failureFn(err): returns the error object. {messsage: <<error message>>, locMessage: <<localized error message>>}
```

##### Direction points

Direction point is used as interface in Mapwize.
To get direction points use the following objects:
1. Place 
2. PlaceList
3. LatLngFloor

To get the above objects, use the respective getter functions documented above.

#### getDirectionWithDirectionPointsFrom(directionPointFrom, directionPointsListTo, isAccessible, successFn, failureFn)

Gets a direction according to the given parameters.

```
directionPointFrom: the starting direction point
directionPointsListTo: the list of end direction points
isAccessible: if the direction to get is accessible.
successFn(direction): returns the Direction object.
failureFn(err): returns the error object. {messsage: <<error message>>, locMessage: <<localized error message>>}
```

#### getDirectionWithWayPointsFrom(directionPointFrom, directionPointTo, waypointsList, isAccessible, successFn, failureFn)

Gets a direction according to the given parameters.

```
directionPointFrom: the starting direction point
directionPointTo: the end direction point
waypointsList: the array of direction points to traverse
isAccessible: if the direction to get is accessible.
successFn(direction): returns the Direction object.
failureFn(err): returns the error object. {messsage: <<error message>>, locMessage: <<localized error message>>}
```

#### getDirectionWithDirectionAndWayPointsFrom(directionPointFrom, directionpointsToList, waypointsList, isAccessible, successFn, failureFn)

Gets a direction according to the given parameters.

```
directionPointFrom: the starting direction point
directionpointsToList: the array of end direction points
waypointsList: the array of direction points to traverse
isAccessible: if the direction to get is accessible.
successFn(direction): returns the Direction object.
failureFn(err): returns the error object. {messsage: <<error message>>, locMessage: <<localized error message>>}
```

#### getDistancesWithFrom(directionPointFrom, directionpointsToList, isAccessible, sortByTravelTime, successFn, failureFn)

Gets the distance between the starting point and the end points according to the given parameters.

```
directionPointFrom: the starting direction point
directionpointsToList: the array of end direction points
isAccessible: if the direction to get is accessible.
sortByTravelTime: sorting by travel time. true: travel time, false: travel length
successFn(distance): returns the Distance object.
failureFn(err): returns the error object. {messsage: <<error message>>, locMessage: <<localized error message>>}
```

##### Distance object

```
{
	from: <<Direction object>>>,
	distances: <<array of DirectionAndDistance objects>>
}
```

###### DirectionAndDistance object

```
{
	distance: <<distance>>,
	traveltime: <<traveltime>>,
	to: {
		floor: <<floor>>,
		placeId: <<placeId of the end point>>,
		venueId: <<venueId of the end point>>,
		placeListId: <<placeListId of the end point>>
	}
	
}
```


## Contributions

This plugin has been mainly developed by [Laszlo Blum](https://github.com/laszloblum).

We want to thank [Guillaume Emery](https://github.com/EmeryGuillaume) for its contributions, advises and feedbacks along the development.

Contributions to this open-source project are welcome.
