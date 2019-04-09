# mapwize-cordova
Cordova plugin for Mapwize Indoor Maps

# 1. Installation

```
ionic cordova plugin add --variable MWZMapwizeApiKey="xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" mapwize-cordova-plugin
```

or from github

```
ionic cordova plugin add --variable MWZMapwizeApiKey="xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" https://github.com/Mapwize/mapwize-cordova.git
```



# 2. Setting up your ionic project

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



# 2. MapwizeView

## 2.1 Creating MapwizeView

```
this.mapwizeView = Mapwize.createMapwizeView(
      {
        floor: 0,
        language: "en",
        universeId: "",
        restrictContentToVenueId: "",
        restrictContentToOrganizationId: "",
        centerOnVenueId: "xxxxxxxxxxxxxxxxxxxxxxxx",
        centerOnPlaceId: "xxxxxxxxxxxxxxxxxxxxxxxx"
      }, () => {
        // Handle  successfull creation
      }, (err) => {
        // Handle  failed creation
        console.log("Error: " + err.message + ", localized message: " + err.locMessage);
      });
```

## 2.2. Callback functions

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

### 2.2.1 DidLoad

Triggers when the View has loaded.

### 2.2.2 DidTapOnFollowWithoutLocation

---

### 2.2.3 DidTapOnMenu

Triggers when the Menu icon is tapped.

### 2.2.4 shouldShowInformationButtonFor

### 2.2.5 TapOnPlaceInformationButton (place)

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

### 2.2.6 TapOnPlaceListInformationButton (place)

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

### 2.3 MapwizeView functions

These functions can be used after the view has been loaded.

### 2.3.1 selectPlace(id, centerOn, successFn, failureFn)

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

### 2.3.2 selectPlaceList(id, successFn, failureFn)

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

### 2.3.3 grantAccess(token, successFn, failureFn)

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

### 2.3.4 unselectContent(closeInfo, successFn, failureFn)

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

### 