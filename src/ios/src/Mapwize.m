//
//  Mapwize.m
//  Mapwize Tester
//
//  Created by Laszlo Blum on 2019. 04. 23..
//

#import <Foundation/Foundation.h>
#import <AVFoundation/AVFoundation.h>
#import <Cordova/CDV.h>
#import "Mapwize.h"
#import "ViewController.h"
#import "OfflineManager.h"
#import "ApiManager.h"
#import <MapwizeUI/MapwizeUI.h>
#import <MapwizeUI/MWZUISettings.h>
#import "Constants.h"

@implementation Mapwize
{
    BOOL enabled;
    UINavigationController* navController;
    ViewController* viewCtrl;
    OfflineManager* offlineManager;
    BOOL showInformationButtonForPlaces;
    BOOL showInformationButtonForPlaceLists;
}
NSString* mCallbackId;

/**
 * Initialize the plugin.
 */
- (void) pluginInitialize
{
    NSLog(@"pluginInitialize...");
    
    
    NSLog(@"ApiManager initManager called...");
    [ApiManager initManager:self];
    viewCtrl = nil;
}

- (void)dealloc {
    NSLog(@"dealloc...");
    [UIApplication sharedApplication].idleTimerDisabled = NO;
    self->navController = nil;
    self->viewCtrl = nil;
    self->offlineManager = nil;
}

- (void)setCallback:(CDVInvokedUrlCommand*)command {
    NSLog(@"setCallback called...");
    mCallbackId = command.callbackId;
    [viewCtrl setPlugin:self callbackId:mCallbackId];
    NSLog(@"setCallback called...END");
}

- (void)createMapwizeView:(CDVInvokedUrlCommand*)command {
    NSLog(@"createMapwizeView called...");
    if (viewCtrl != nil) {
        viewCtrl = nil;
    }
    viewCtrl = [[ViewController alloc] init];
    BOOL showCloseButton = YES;
    
    BOOL showInformationButtonForPlaces = YES;
    BOOL showInformationButtonForPlaceLists = YES;
    
    NSLog(@"arguments...size: %lu", [command.arguments count]);
    
    NSString *optionsStr = [command.arguments objectAtIndex:0];
    NSLog(@"options...optionsStr...");
    
    MWZOptions* opts = [self jsonToOptions:optionsStr];
    
    NSLog(@"showCloseButton...");
    showCloseButton =  [self getShowCloseButton:optionsStr];
    
    if (showCloseButton == YES) {
        navController = [[UINavigationController alloc] initWithRootViewController:viewCtrl];
    }
    
    NSLog(@"showCloseButton...");
    [viewCtrl setOptions:opts showInformationButtonForPlaces:showInformationButtonForPlaces showInformationButtonForPlaceLists:showInformationButtonForPlaceLists];
    
    NSLog(@"uiSettings...");
    NSString *uiSettingsStr = [command.arguments objectAtIndex:1];
    NSLog(@"uiSettings...");
    NSLog(@"uiSettings...uiSettingsStr: %@", uiSettingsStr);
    MWZUISettings* uiSettings = [self jsonToUiSettings:uiSettingsStr];
    
    NSLog(@"setUiSettings...");
    [viewCtrl setUiSettings:uiSettings];
    
    NSLog(@"getting addChildViewController...");
    NSLog(@"getting presentViewController...");
    
    if (showCloseButton == YES) {
        NSLog(@"showCloseButton == YES...");
        [self.viewController presentViewController:navController
                                          animated:NO completion:nil];
    } else {
        NSLog(@"showCloseButton else...");
        self.viewController.modalPresentationStyle = UIModalPresentationFullScreen;
        [self.viewController presentViewController:viewCtrl
                                          animated:NO
                                        completion:nil];
    }

    NSLog(@"createMapwizeView END...");
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}


- (BOOL) getShowCloseButton:(NSString*)optionsStr {
    NSLog(@"getShowCloseButton optionsStr...%@", optionsStr);
    NSError *jsonError;
    NSData *data = [optionsStr dataUsingEncoding:NSUTF8StringEncoding];
    NSDictionary *json = [NSJSONSerialization JSONObjectWithData:data
                                 options:NSJSONReadingMutableContainers
                                   error:&jsonError];
    if(jsonError.code != 0) {
        NSLog(@"getShowCloseButton, jsonError...%@", jsonError.localizedDescription);
        return NO;
    }
    
    NSLog(@"getting showCloseButton...");
    if ([json objectForKey:@"showCloseButton"] != nil) {
        NSLog(@"setting showCloseButton...");
        return [[json valueForKey:@"showCloseButton"] boolValue];
    } else {
        NSLog(@"setting showCloseButton...no value, defaulting...");
        return NO;
    }
}

- (MWZUISettings*) jsonToUiSettings:(NSString*)settingsStr {
    NSLog(@"jsonToUiSettings optionsStr...%@", settingsStr);
    NSError *jsonError;
    NSData *data = [settingsStr dataUsingEncoding:NSUTF8StringEncoding];
    NSDictionary *json = [NSJSONSerialization
                          JSONObjectWithData:data
                                     options:NSJSONReadingMutableContainers
                                       error:&jsonError];
    NSLog(@"creating settings...");
    MWZUISettings *settings = [[MWZUISettings alloc] init];
    
    NSLog(@"getting UIOPT_MENUBUTTONISHIDDEN...");
    if ([json objectForKey:UIOPT_MENUBUTTONISHIDDEN] != nil) {
        NSLog(@"setting UIOPT_MENUBUTTONISHIDDEN...");
        settings.menuButtonIsHidden = [[json valueForKey:UIOPT_MENUBUTTONISHIDDEN] boolValue];
        NSLog(@"setting UIOPT_MENUBUTTONISHIDDEN...%d: ", settings.menuButtonIsHidden);
    }
    
    NSLog(@"getting UIOPT_FOLLOWUSERBUTTONISHIDDEN...");
    if ([json objectForKey:UIOPT_FOLLOWUSERBUTTONISHIDDEN] != nil) {
        NSLog(@"setting UIOPT_FOLLOWUSERBUTTONISHIDDEN...");
        settings.followUserButtonIsHidden = [[json valueForKey:UIOPT_FOLLOWUSERBUTTONISHIDDEN] boolValue];
    }
    
    NSLog(@"getting UIOPT_FLOORCONTROLLERISHIDDEN...");
    if ([json objectForKey:UIOPT_FLOORCONTROLLERISHIDDEN] != nil) {
        NSLog(@"setting UIOPT_FLOORCONTROLLERISHIDDEN...");
        settings.floorControllerIsHidden = [[json valueForKey:UIOPT_FLOORCONTROLLERISHIDDEN] boolValue];
    }
    
    NSLog(@"getting UIOPT_COMPASSISHIDDEN...");
    if ([json objectForKey:UIOPT_COMPASSISHIDDEN] != nil) {
        NSLog(@"setting UIOPT_COMPASSISHIDDEN...");
        settings.compassIsHidden = [[json valueForKey:UIOPT_COMPASSISHIDDEN] boolValue];
    }
    return settings;
}

- (MWZUIOptions*) jsonToOptions:(NSString*)optionsStr {
    NSLog(@"jsonToOptions optionsStr...%@", optionsStr);
    NSLog(@"jsonToOptions optionsStr type...%@", [optionsStr class]);
    NSError *jsonError;
    
    NSLog(@"jsonToOptions, data...");
    NSData *data = [optionsStr dataUsingEncoding:NSUTF8StringEncoding];
    
    NSLog(@"jsonToOptions, json...");
    NSDictionary *json = [NSJSONSerialization JSONObjectWithData:data
                                 options:NSJSONReadingMutableContainers
                                   error:&jsonError];
    NSLog(@"creating opts...");
    MWZUIOptions *opts = [[MWZUIOptions alloc] init];
    
    NSLog(@"getting showInformationButtonForPlaces...");
    if ([json objectForKey:OPT_SHOW_INFO_BUTTON_FOR_PLACES] != nil) {
        NSLog(@"setting showInformationButtonForPlaces...");
        showInformationButtonForPlaces = [[json valueForKey:OPT_SHOW_INFO_BUTTON_FOR_PLACES] boolValue];
    }
    
    NSLog(@"getting showInformationButtonForPlaces...");
    if ([json objectForKey:OPT_SHOW_INFO_BUTTON_FOR_PLACELISTS] != nil) {
        NSLog(@"setting showInformationButtonForPlaces...");
        showInformationButtonForPlaceLists = [[json valueForKey:OPT_SHOW_INFO_BUTTON_FOR_PLACELISTS] boolValue];
    }
    
    NSLog(@"getting floor...");
    NSNumber* floor = json[@"floor"];
    if (floor != nil) {
        NSLog(@"setting floor...");
        opts.floor = floor;
    }
    
    NSLog(@"getting language...");
    NSString* language = json[@"language"];
    if ([language length] != 0) {
        opts.language = language;
    }
    
    NSLog(@"getting universeId...");
    NSString* universeId = json[@"universeId"];
    if ([universeId length] != 0) {
        opts.universeId = universeId;
    }
    
    NSLog(@"getting restrictContentToVenueId...");
    NSString* restrictContentToVenueId = json[@"restrictContentToVenueId"];
    if ([restrictContentToVenueId length] != 0) {
        opts.restrictContentToVenueId = restrictContentToVenueId;
    }

    NSLog(@"getting restrictContentToOrganizationId...");
    NSString* restrictContentToOrganizationId = json[@"restrictContentToOrganizationId"];
    if ([restrictContentToOrganizationId length] != 0) {
        opts.restrictContentToOrganizationId = restrictContentToOrganizationId;
    }
    
    NSLog(@"getting centerOnVenueId...");
    NSString* centerOnVenueId = json[@"centerOnVenueId"];
    if ([centerOnVenueId length] != 0) {
        opts.centerOnVenueId = centerOnVenueId;
    }
    
    NSLog(@"getting centerOnPlaceId...");
    NSString* centerOnPlaceId = json[@"centerOnPlaceId"];
    if ([centerOnPlaceId length] != 0) {
        opts.centerOnPlaceId = centerOnPlaceId;
    }
    
    NSLog(@"getting centerOnLocation...");
    NSDictionary* centerOnLocation = json[@"centerOnLocation"];
    if (centerOnLocation != nil) {
        MWZLatLngFloor* latlng = [MWZApiResponseParser parseLatLng:centerOnLocation]; //TODO: need a MWZLatLngFloor parser
        opts.centerOnLocation = latlng;
    } else {
        opts.centerOnLocation = nil;
    }
    
    NSLog(@"getting UIOPT_MAINCOLOR...");
    NSString* colorStr = [json valueForKey:UIOPT_MAINCOLOR];
    if (colorStr != nil) {
        NSLog(@"setting showInformationButtonForPlaces...");
        NSLog(@"TODO, need to deserialize mainColor...");
        
        NSLog(@"TODO, need to deserialize mainColor...colorStr type: %@", [colorStr class]);
        NSLog(@"TODO, need to deserialize mainColor...colorStr: %@", colorStr);
        opts.mainColor = [Mapwize colorWithHexString:colorStr];
    }
    
    return opts;
}

- (MWZDirectionWrapper*) jsonToDirectionWrapper:(NSDictionary*)json {
    NSLog(@"json2DirectionWrapper json...%@", json);
    NSLog(@"json2DirectionWrapper json type...%@", [json class]);
//    NSData *data = [json dataUsingEncoding:NSUTF8StringEncoding];
//    NSError *jsonError;
    
//    NSLog(@"json2DirectionWrapper, json...");
//    NSDictionary *json = [NSJSONSerialization JSONObjectWithData:data
//                                 options:NSJSONReadingMutableContainers
//                                   error:&jsonError];
    MWZDirectionWrapper *dw = [[MWZDirectionWrapper alloc] init];
    
    NSNumber* latitude = json[@"latitude"];
    if (latitude != nil) {
        NSLog(@"latitude...");
        dw.latitude = latitude;
    }
    
    NSNumber* longitude = json[@"longitude"];
    if (longitude != nil) {
        NSLog(@"longitude...");
        dw.longitude = longitude;
    }
    
    NSNumber* floor = json[@"floor"];
    if (floor != nil) {
        NSLog(@"floor...");
        dw.floor = floor;
    }
    
    NSString* placeId = json[@"placeId"];
    if (placeId != nil) {
        NSLog(@"placeId...");
        dw.placeId = placeId;
    }
    
    NSString* venueId = json[@"venueId"];
    if (venueId != nil) {
        NSLog(@"venueId...");
        dw.venueId = venueId;
    }
    
    NSString* placeListId = json[@"placeListId"];
    if (placeListId != nil) {
        NSLog(@"placeListId...");
        dw.placeListId = placeListId;
    }
    
    return dw;
}

- (NSArray<MWZLatLng *> *) jsonToMWZLatLngArr:(NSArray*)latLngArr {
    NSMutableArray* arr = [[NSMutableArray alloc] init];
    
    int i = 0;
    for (i = 0; i < [latLngArr count]; i++) {
        NSDictionary* latLngDict = [latLngArr objectAtIndex:i];
        NSLog(@"jsonToMWZLatLngArr...%@", [latLngDict description]);
        MWZLatLng* latLng = [self jsonToLatLng:latLngDict];
        [arr addObject:latLng];
    }
    
    return arr;
}

- (MWZLatLng*) jsonToLatLng:(NSArray*)json {
    MWZLatLng* latLong = [[MWZLatLng alloc] initWithLatitude:[[json objectAtIndex:1] doubleValue] longitude:[[json objectAtIndex:0] doubleValue]];
    return latLong;
}


- (MWZRoute*) jsonToRouter:(NSDictionary*)json {
    NSLog(@"jsonToRouter json...%@", json);
    NSLog(@"jsonToRouter json type...%@", [json class]);
    
    NSNumber* floor = json[@"floor"];
    NSNumber* fromFloor = json[@"fromFloor"];
    NSNumber* toFloor = json[@"toFloor"];
    
    BOOL isStart = [json[@"isStart"] boolValue];
    BOOL isEnd = [json[@"isEnd"] boolValue];
    
    double traveltime = [json[@"traveltime"] doubleValue];
    double timeToEnd = [json[@"timeToEnd"] doubleValue];
    double distance = [json[@"distance"] doubleValue];

    NSString* connectorTypeTo = json[@"connectorTypeTo"];
    NSString* connectorTypeFrom = json[@"connectorTypeFrom"];
        
    NSString* uniqId = json[@"uniqId"];
    NSArray<MWZLatLng *> *latLngArr =[self jsonToMWZLatLngArr:json[@"path"]];
    
    NSString* boundsStr = json[@"bounds"];
    NSLog(@"jsonToRouter boundsStr...%@", boundsStr);
    NSLog(@"jsonToRouter boundsStr type...%@", [boundsStr class]);
    
    MGLCoordinateBounds bounds = [self jsonToCoordinateBounds:boundsStr];
    
    
    
    
    MWZRoute *route = [[MWZRoute alloc] initWithFloor:floor
            fromFloor:fromFloor
              toFloor:toFloor
              isStart:isStart
                isEnd:isEnd
           traveltime:traveltime
            timeToEnd:timeToEnd
               bounds:bounds
             distance:distance
      connectorTypeTo:connectorTypeTo
    connectorTypeFrom:connectorTypeFrom
                 path:latLngArr];
        
//    - (instancetype _Nonnull)initWithFloor:(NSNumber *_Nullable)floor
//            fromFloor:(NSNumber *_Nullable)fromFloor
//              toFloor:(NSNumber *_Nullable)toFloor
//              isStart:(BOOL)isStart
//                isEnd:(BOOL)isEnd
//           traveltime:(double)traveltime
//            timeToEnd:(double)timeToEnd
//               bounds:(struct MGLCoordinateBounds)bounds
//             distance:(double)distance
//      connectorTypeTo:(NSString *_Nullable)connectorTypeTo
//    connectorTypeFrom:(NSString *_Nullable)connectorTypeFrom
//                 path:(NSArray<MWZLatLng *> *_Nonnull)path;
    
    return route;
}





- (NSArray<MWZRoute *> *) jsonToRouteArray:(NSArray*)routeArray {
    NSMutableArray* raArray = [[NSMutableArray alloc] init];
    NSLog(@"jsonToRouteArray...%@", [routeArray description]);
    NSLog(@"jsonToRouteArray json type...%@", [routeArray class]);
//    NSLog(@"jsonToRouteArray, data...");
//    NSData *data = [routeArrayStr dataUsingEncoding:NSUTF8StringEncoding];
    
    NSLog(@"jsonToRouteArray, json...");
    
    int i = 0;
    for (i=0; i<[routeArray count]; i++) {
        NSDictionary *arrayResult = [routeArray objectAtIndex:i];
        NSLog(@"MWZRoute,  %@",[arrayResult description]);
        MWZRoute* route = [self jsonToRouter:arrayResult];
        [raArray addObject:route];
    }
    
    return raArray;
}

- (NSArray<MWZDirectionWrapper *> *) jsonToDirectionWrapperArray:(NSArray*)directionWrapperArray {
    NSArray* dwArray = [[NSArray alloc] init];
    NSError *jsonError;
    
    NSLog(@"jsonToDirectionWrapperArray, data...%@", [directionWrapperArray description]);
    NSLog(@"jsonToDirectionWrapperArray, data...%@", [directionWrapperArray class]);
//    NSData *data = [directionWrapperArrayStr dataUsingEncoding:NSUTF8StringEncoding];
//    id allKeys = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableContainers error:&jsonError];
    int i = 0;
    for (i=0; i<[directionWrapperArray count]; i++) {
        NSDictionary *arrayResult = [directionWrapperArray objectAtIndex:i];
        NSLog(@"MWZDirectionWrapper,  %@",[arrayResult description]);
        
//        NSLog(@"ID=%@",[arrayResult objectForKey:@"ID"]);
    }
    
    return dwArray;
}

- (NSArray<MWZDirection *> *) jsonToDirectionArray:(NSArray*)directionArray {
    NSArray* dArray = [[NSArray alloc] init];
    NSError *jsonError;
    
    NSLog(@"jsonToDirectionArray, data...%@", [directionArray description]);
    NSLog(@"jsonToDirectionArray, type...%@", [directionArray class]);
//    NSData *data = [directionArrayStr dataUsingEncoding:NSUTF8StringEncoding];
    
//    NSLog(@"jsonToDirectionArray, json...");
    
//    id allKeys = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableContainers error:&jsonError];
    int i = 0;
    for (i=0; i<[directionArray count]; i++) {
        NSDictionary *arrayResult = [directionArray objectAtIndex:i];
        NSLog(@"MWZDirectionWrapper,  %@",[arrayResult description]);
        
//        NSLog(@"ID=%@",[arrayResult objectForKey:@"ID"]);
    }
    
    return dArray;
}


//- (CLLocationCoordinate2D) jsonToLocationCoord:(NSString*)coordStr {
//
//}

- (MGLCoordinateBounds) jsonToCoordinateBounds:(NSArray*)coordBounds {
    MGLCoordinateBounds cb;
    CLLocationCoordinate2D ne;
    CLLocationCoordinate2D sw;
    ne.latitude = 12.4;
    ne.longitude = 12.4;
    sw.latitude = 12.4;
    sw.longitude = 12.4;
    
    cb.ne = ne;
    cb.sw = sw;

    NSError *jsonError;
    
    NSLog(@"jsonToCoordinateBounds, data...");
    NSLog(@"jsonToCoordinateBounds, data...%@", coordBounds);
    NSLog(@"jsonToCoordinateBounds, data type...%@", [coordBounds class]);
//    NSData *data = [coordBoundsStr dataUsingEncoding:NSUTF8StringEncoding];
    
    NSLog(@"jsonToCoordinateBounds, json...");
    
//    id allKeys = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableContainers error:&jsonError];
//    int i = 0;
//
//
//    for (i=0; i<[coordBounds count]; i++) {
//        NSDictionary *arrayResult = [coordBounds objectAtIndex:i];
//        NSLog(@"MWZDirectionWrapper,  %@",[arrayResult description]);
//        NSLog(@"MWZDirectionWrapper,  %@",[arrayResult class]);
//    }
    
    if([coordBounds count] == 4) {
        ne.longitude = [[coordBounds objectAtIndex:0] doubleValue];
        ne.latitude = [[coordBounds objectAtIndex:1] doubleValue];
        sw.longitude = [[coordBounds objectAtIndex:0] doubleValue];
        sw.latitude = [[coordBounds objectAtIndex:1] doubleValue];
    }

    
    return cb;
}






- (MWZDirection*) jsonToDirection:(NSString*)directionStr {
    NSLog(@"jsonToDirection directionStr...%@", directionStr);
    NSLog(@"jsonToDirection directionStr type...%@", [directionStr class]);
    NSError *jsonError;
    
    NSLog(@"jsonToDirection, data...");
    NSData *data = [directionStr dataUsingEncoding:NSUTF8StringEncoding];
    
    NSLog(@"jsonToDirection, json...");
    NSDictionary *json = [NSJSONSerialization JSONObjectWithData:data
                                 options:NSJSONReadingMutableContainers
                                   error:&jsonError];
    NSLog(@"creating dirs...");
    
    NSString* fromStr = json[@"from"];
    MWZDirectionWrapper *from = [self jsonToDirectionWrapper:fromStr];
    
    NSString* toStr = json[@"to"];
    MWZDirectionWrapper *to = [self jsonToDirectionWrapper:toStr];
    
    NSString* routesStr = json[@"routes"];
    NSArray<MWZRoute *> *jsonToRouteArray = [self jsonToRouteArray:routesStr];
    
    
    MGLCoordinateBounds coordBounds;
    NSString* boundsStr = json[@"bounds"];
    if (boundsStr != nil) {
        coordBounds = [self jsonToCoordinateBounds:boundsStr];
    }
    
    NSString* waypointsStr = json[@"waypoints"];
    NSArray<MWZDirectionWrapper *> *waypointsArray = [self jsonToDirectionWrapperArray:waypointsStr];
    
    NSString* subdirectionsStr = json[@"subdirections"];
    NSArray<MWZDirection *> *subdirectionsArray = [self jsonToDirectionArray:subdirectionsStr];
    
    MWZDirection* direction = [[MWZDirection alloc] initWithFrom:from
               to:to
         distance:0.0
           routes:jsonToRouteArray
       traveltime:0.0
           bounds:coordBounds
        waypoints:waypointsArray
    subdirections:subdirectionsArray];

    
    return direction;
}





- (void)closeMapwizeView:(CDVInvokedUrlCommand*)command {
    NSLog(@"closeMapwizeView called...");
    [self.viewController dismissViewControllerAnimated:NO completion:^{
        NSLog(@"closeMapwizeView successfully closed...");
        self->viewCtrl = nil;
        self->navController = nil;
    }];
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)setPlaceStyle:(CDVInvokedUrlCommand*)command {
    NSLog(@"setPlaceStyle called...");
    NSString *identifier = [command.arguments objectAtIndex:0];
    NSString *styleStr = [command.arguments objectAtIndex:1];
    
    [[MWZMapwizeApiFactory getApi] getPlaceWithIdentifier:identifier success:^(MWZPlace *place) {
        NSLog(@"identifier...");
        [self->viewCtrl setPlaceStyle:place style:styleStr callbackId:command.callbackId];
    } failure:^(NSError *error) {
        NSLog(@"error...");
    }];
}

- (void)selectPlace:(CDVInvokedUrlCommand*)command {
    NSLog(@"selectPlace called...");
    NSString *identifier = [command.arguments objectAtIndex:0];
    BOOL    centerOn = [command.arguments objectAtIndex:1]; //TODO: need to wait for the decision on the centerOn param
    
    [[MWZMapwizeApiFactory getApi] getPlaceWithIdentifier:identifier success:^(MWZPlace *place) {
        NSLog(@"identifier...");
        [self->viewCtrl selectPlace:place centerOn:centerOn callbackId:command.callbackId];
    } failure:^(NSError *error) {
        NSLog(@"error...%@", error.localizedDescription);
    }];
}

- (void)selectPlaceList:(CDVInvokedUrlCommand*)command {
    NSLog(@"selectPlaceList called...");
    NSString *identifier = [command.arguments objectAtIndex:0];
    
    [[MWZMapwizeApiFactory getApi] getPlacelistWithIdentifier:identifier success:^(MWZPlacelist *placeList) {
        NSLog(@"identifier...");
        [viewCtrl selectPlaceList:placeList callbackId:command.callbackId];
    } failure:^(NSError *error) {
        NSLog(@"error...%@", error.localizedDescription);
    }];
}

- (void)grantAccess:(CDVInvokedUrlCommand*)command {
    NSLog(@"grantAccess...");
    NSString *accessKey = [command.arguments objectAtIndex:0];
    [viewCtrl grantAccess:accessKey callbackId:command.callbackId];
}

- (void)unselectContent:(CDVInvokedUrlCommand*)command  {
    NSLog(@"unselectContent...");
    [viewCtrl unselectContent:command.callbackId];
}

- (void)setDirection:(CDVInvokedUrlCommand*)command  {
    NSLog(@"native setDirection...");
    NSString* directionStr = [command.arguments objectAtIndex:0];
    MWZDirection* direction = [self jsonToDirection:directionStr];
    
    NSString* fromStr = [command.arguments objectAtIndex:1];
//    MWZDirectionWrapper* from = [self jsonToDirectionWrapper:fromStr];
    id<MWZDirectionPoint> from = [MWZApiResponseParser parseDirectionPoint:fromStr];
    
    NSString* toStr = [command.arguments objectAtIndex:2];
//    MWZDirectionWrapper* to = [self jsonToDirectionWrapper:toStr];
    id<MWZDirectionPoint> to = [MWZApiResponseParser parseDirectionPoint:toStr];
    
    BOOL isAccessible = [command.arguments objectAtIndex:3];
    
    [viewCtrl
        setDirection:direction
                from:from
                  to:to
        isAccessible:isAccessible
          callbackId:command.callbackId];
}

- (NSArray<MWZUniverse*>*) getUniverses:( NSArray * )universesDict {
    NSMutableArray<MWZUniverse*>* universes  = [NSMutableArray array];
    NSEnumerator *e = [universes objectEnumerator];
    id object;
    while (object = [e nextObject]) {
        // do something with object
        MWZUniverse* universe = [self getUniverse:object];
        [universes addObject:universe];
    }
    
    return universes;
}

- (MWZUniverse*) getUniverse:( NSDictionary * )universeDict {
    NSString* identifier = universeDict[@"identifier"];
    NSString* name = universeDict[@"name"];
    MWZUniverse* uv = [[MWZUniverse alloc] initWithIdentifier:identifier name:name];
    return uv;
}

// Offline Manager
- (void) initOfflineManager:(CDVInvokedUrlCommand*)command {
    NSLog(@"initOfflineManager called...");
    NSString *styleURL = [command.arguments objectAtIndex:0];
    NSLog(@"initOfflineManager styleURL %@...", styleURL);
    offlineManager = [[OfflineManager alloc] init];
    [offlineManager initManager:self styleURL:styleURL];
}

- (void) removeDataForVenue:(CDVInvokedUrlCommand*)command {
    NSLog(@"removeDataForVenue called...");
    NSString *venueId = [command.arguments objectAtIndex:0];
    NSString *universeId = [command.arguments objectAtIndex:1];
    [offlineManager removeDataForVenue:venueId universe:universeId callbackId:command.callbackId];
}

- (void) downloadDataForVenue:(CDVInvokedUrlCommand*)command {
    NSLog(@"downloadDataForVenue called...");
    NSString *venueId = [command.arguments objectAtIndex:0];
    NSString *universeId = [command.arguments objectAtIndex:1];
    [offlineManager downloadDataForVenue:venueId universe:universeId callbackId:command.callbackId];
}

- (void) isOfflineForVenue:(CDVInvokedUrlCommand*)command {
    NSLog(@"isOfflineForVenue called...");
    NSString *venueId = [command.arguments objectAtIndex:0];
    NSString *universeId = [command.arguments objectAtIndex:1];
    [offlineManager isOfflineForVenue:venueId universe:universeId callbackId:command.callbackId];
}

- (void) getOfflineVenues:(CDVInvokedUrlCommand*)command {
    NSLog(@"getOfflineVenues called...");
    [offlineManager getOfflineVenues:command.callbackId];
}

- (void) getOfflineUniversesForVenue:(CDVInvokedUrlCommand*)command {
    NSLog(@"getOfflineUniversesForVenue called...");
    NSString *venueId = [command.arguments objectAtIndex:0];
    [offlineManager getOfflineUniversesForVenue:venueId callbackId:command.callbackId];
}


// API Manager
- (void)getVenueWithId:(CDVInvokedUrlCommand*)command {
    NSLog(@"getVenueWithId called...");
    NSString *identifier = [command.arguments objectAtIndex:0];
    
    [ApiManager getVenueWithId:identifier callbackId:command.callbackId];
}
- (void)getVenuesWithFilter:(CDVInvokedUrlCommand*)command {
    NSLog(@"getVenuesWithFilter called...");
    NSString *filterStr = [command.arguments objectAtIndex:0];
    
    [ApiManager getVenuesWithFilter:filterStr callbackId:command.callbackId];
}

- (void)getVenueWithName:(CDVInvokedUrlCommand*)command {
    NSLog(@"getVenueWithName called...");
    NSString *name = [command.arguments objectAtIndex:0];
    
    [ApiManager getVenueWithName:name callbackId:command.callbackId];
}

- (void)getVenueWithAlias:(CDVInvokedUrlCommand*)command {
    NSLog(@"getVenueWithAlias called...");
    NSString *alias = [command.arguments objectAtIndex:0];
    
    [ApiManager getVenueWithAlias:alias callbackId:command.callbackId];
}

- (void)getPlaceWithId:(CDVInvokedUrlCommand*)command {
    NSLog(@"getPlaceWithId called...");
    NSString *identifier = [command.arguments objectAtIndex:0];
    [ApiManager getPlaceWithId:identifier callbackId:command.callbackId];
    
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_NO_RESULT];
    [pluginResult setKeepCallbackAsBool:YES];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)getPlaceWithName:(CDVInvokedUrlCommand*)command {
    NSLog(@"getPlaceWithName called...");
    NSString *name = [command.arguments objectAtIndex:0];
    NSString *venueId = [command.arguments objectAtIndex:1];
    
    [ApiManager getPlaceWithName:name venue:venueId callbackId:command.callbackId];
}

- (void)getPlaceWithAlias:(CDVInvokedUrlCommand*)command {
    NSLog(@"getPlaceWithAlias called...");
    NSString *alias = [command.arguments objectAtIndex:0];
    NSString *venueId = [command.arguments objectAtIndex:1];
    
    [ApiManager getPlaceWithAlias:alias venue:venueId callbackId:command.callbackId];
}

- (void)getPlacesWithFilter:(CDVInvokedUrlCommand*)command {
    NSLog(@"getPlacesWithFilter called...");
    NSString *filter = [command.arguments objectAtIndex:0];
    
    [ApiManager getPlacesWithFilter:filter callbackId:command.callbackId];
}

- (void)getPlaceListWithId:(CDVInvokedUrlCommand*)command {
    NSLog(@"getPlaceListWithId called...");
    NSString *identifier = [command.arguments objectAtIndex:0];
    
    [ApiManager getPlaceListWithId:identifier callbackId:command.callbackId];
}

- (void)getPlaceListWithName:(CDVInvokedUrlCommand*)command {
    NSLog(@"getPlaceListWithName called...");
    NSString *name = [command.arguments objectAtIndex:0];
    NSString *venueId = [command.arguments objectAtIndex:1];
    
    [ApiManager getPlaceListWithName:name venue:venueId callbackId:command.callbackId];
}

- (void)getPlaceListWithAlias:(CDVInvokedUrlCommand*)command {
    NSLog(@"getPlaceListWithAlias called...");
    NSString *alias = [command.arguments objectAtIndex:0];
    NSString *venueId = [command.arguments objectAtIndex:1];
    
    [ApiManager getPlaceListWithAlias:alias venue:venueId callbackId:command.callbackId];
}

- (void)getPlaceListsWithFilter:(CDVInvokedUrlCommand*)command {
    NSLog(@"getPlaceListsWithFilter called...");
    NSString *filterStr = [command.arguments objectAtIndex:0];
    
    [ApiManager getPlaceListsWithFilter:filterStr callbackId:command.callbackId];
}

- (void)getUniverseWithId:(CDVInvokedUrlCommand*)command {
    NSLog(@"getUniverseWithId called...");
    NSString *universeId = [command.arguments objectAtIndex:0];
    
    [ApiManager getUniverseWithId:universeId callbackId:command.callbackId];
}

- (void)getUniversesWithFilter:(CDVInvokedUrlCommand*)command {
    NSLog(@"getUniversesWithFilter called...");
    NSString *filterStr = [command.arguments objectAtIndex:0];
    
    [ApiManager getUniversesWithFilter:filterStr callbackId:command.callbackId];
}

- (void)getAccessibleUniversesWithVenue:(CDVInvokedUrlCommand*)command {
    NSLog(@"getAccessibleUniversesWithVenue called...");
    NSString *venueId = [command.arguments objectAtIndex:0];
    
    [ApiManager getAccessibleUniversesWithVenue:venueId callbackId:command.callbackId];
}

- (void)searchWithParams:(CDVInvokedUrlCommand*)command {
    NSLog(@"searchWithParams called...");
    NSString *searchParams = [command.arguments objectAtIndex:0];
    
    [ApiManager searchWithParams:searchParams callbackId:command.callbackId];
}

- (void)getDirectionWithFrom:(CDVInvokedUrlCommand*)command {
    NSLog(@"getDirectionWithFrom called...");
    NSString *directionPointFrom = [command.arguments objectAtIndex:0];
    NSString *directionPointTo = [command.arguments objectAtIndex:1];
    BOOL     isAccessible = [command.arguments objectAtIndex:2];
    [ApiManager getDirectionWithFrom:directionPointFrom to:directionPointTo isAccessible:isAccessible callbackId:command.callbackId];
}

- (void)getDirectionWithDirectionPointsFrom:(CDVInvokedUrlCommand*)command {
    NSLog(@"getDirectionWithDirectionPointsFrom called...");
    NSString *directionPointFrom = [command.arguments objectAtIndex:0];
    NSString *directionPointsListTo = [command.arguments objectAtIndex:1];
    BOOL     isAccessible = [command.arguments objectAtIndex:2];
    [ApiManager getDirectionWithDirectionPointsFrom:directionPointFrom to:directionPointsListTo isAccessible:isAccessible callbackId:command.callbackId];
}
- (void)getDirectionWithWayPointsFrom:(CDVInvokedUrlCommand*)command {
    NSLog(@"getDirectionWithWayPointsFrom called...");
    NSString *directionPointFrom = [command.arguments objectAtIndex:0];
    NSString *directionPointTo = [command.arguments objectAtIndex:1];
    NSString *wayPointsList = [command.arguments objectAtIndex:2];
    BOOL     isAccessible = [command.arguments objectAtIndex:3];
    [ApiManager getDirectionWithWayPointsFrom:directionPointFrom to:directionPointTo waypointsList:wayPointsList isAccessible:isAccessible callbackId:command.callbackId];
}

- (void)getDirectionWithDirectionAndWayPointsFrom:(CDVInvokedUrlCommand*)command {
    NSLog(@"getDirectionWithWayPointsFrom called...");
    NSString *directionPointFrom = [command.arguments objectAtIndex:0];
    NSString *directionPointListTo = [command.arguments objectAtIndex:1];
    NSString *wayPointsList = [command.arguments objectAtIndex:2];
    BOOL     isAccessible = [command.arguments objectAtIndex:3];
    
    [ApiManager getDirectionWithDirectionAndWayPointsFrom:directionPointFrom tos:directionPointListTo waypointsList:wayPointsList isAccessible:isAccessible callbackId:command.callbackId];
}


- (void)getDistancesWithFrom:(CDVInvokedUrlCommand*)command {
    NSLog(@"getDistancesWithFrom called...");
    NSString *directionPointFrom = [command.arguments objectAtIndex:0];
    NSString *directionPointsListTo = [command.arguments objectAtIndex:1];
    BOOL     isAccessible = [command.arguments objectAtIndex:2];
    BOOL     sortByTravelTime = [command.arguments objectAtIndex:3];
    [ApiManager getDistancesWithFrom:directionPointFrom directionpointsToListStr:directionPointsListTo isAccessible:(BOOL)isAccessible sortByTravelTime:(BOOL)sortByTravelTime callbackId:command.callbackId];
}


+ (UIColor* ) colorWithHexString: (NSString*)hexString {
    NSString *colorString = [[hexString stringByReplacingOccurrencesOfString: @"#" withString: @""] uppercaseString];
    CGFloat alpha, red, blue, green;
    switch ([colorString length]) {
        case 3: // #RGB
            alpha = 1.0f;
            red   = [self colorComponentFrom: colorString start: 0 length: 1];
            green = [self colorComponentFrom: colorString start: 1 length: 1];
            blue  = [self colorComponentFrom: colorString start: 2 length: 1];
            break;
        case 4: // #ARGB
            alpha = [self colorComponentFrom: colorString start: 0 length: 1];
            red   = [self colorComponentFrom: colorString start: 1 length: 1];
            green = [self colorComponentFrom: colorString start: 2 length: 1];
            blue  = [self colorComponentFrom: colorString start: 3 length: 1];
            break;
        case 6: // #RRGGBB
            alpha = 1.0f;
            red   = [self colorComponentFrom: colorString start: 0 length: 2];
            green = [self colorComponentFrom: colorString start: 2 length: 2];
            blue  = [self colorComponentFrom: colorString start: 4 length: 2];
            break;
        case 8: // #AARRGGBB
            alpha = [self colorComponentFrom: colorString start: 0 length: 2];
            red   = [self colorComponentFrom: colorString start: 2 length: 2];
            green = [self colorComponentFrom: colorString start: 4 length: 2];
            blue  = [self colorComponentFrom: colorString start: 6 length: 2];
            break;
        default:
            [NSException raise:@"Invalid color value" format: @"Color value %@ is invalid.  It should be a hex value of the form #RBG, #ARGB, #RRGGBB, or #AARRGGBB", hexString];
            break;
    }
    return [UIColor colorWithRed: red green: green blue: blue alpha: alpha];
}

+ (CGFloat) colorComponentFrom: (NSString *) string start: (NSUInteger) start length: (NSUInteger) length {
    NSString *substring = [string substringWithRange: NSMakeRange(start, length)];
    NSString *fullHex = length == 2 ? substring : [NSString stringWithFormat: @"%@%@", substring, substring];
    unsigned hexComponent;
    [[NSScanner scannerWithString: fullHex] scanHexInt: &hexComponent];
    return hexComponent / 255.0;
}

@end
