//
//  Mapwize.h
//  Mapwize Tester
//
//  Created by Laszlo Blum on 2019. 04. 23..
//

#ifndef Mapwize_h
#define Mapwize_h

#import <Cordova/CDV.h>


@class MWZUniverse;


@interface Mapwize : CDVPlugin <UIApplicationDelegate>

@property (nonatomic, strong) NSString* fetchCallbackId;

- (void)setCallback:(CDVInvokedUrlCommand*)command;
- (void)createMapwizeView:(CDVInvokedUrlCommand*)command;

- (void)closeMapwizeView:(CDVInvokedUrlCommand*)command;
- (void)setPlaceStyle:(CDVInvokedUrlCommand*)command;
- (void)selectPlace:(CDVInvokedUrlCommand*)command;
- (void)selectPlaceList:(CDVInvokedUrlCommand*)command;
- (void)grantAccess:(CDVInvokedUrlCommand*)command;
- (void)unselectContent:(CDVInvokedUrlCommand*)command;

- (void)destroyMapwizeView:(CDVInvokedUrlCommand*)command;

- (NSArray<MWZUniverse*>*) getUniverses:( NSArray * )universesDict;
- (MWZUniverse*) getUniverse:( NSArray * )universeDict;

// Offline manager
- (void) initOfflineManager:(CDVInvokedUrlCommand*)command;
- (void) removeDataForVenue:(CDVInvokedUrlCommand*)command;
- (void) downloadDataForVenue:(CDVInvokedUrlCommand*)command;
- (void) isOfflineForVenue:(CDVInvokedUrlCommand*)command;
- (void) getOfflineVenues:(CDVInvokedUrlCommand*)command;
- (void) getOfflineUniversesForVenue:(CDVInvokedUrlCommand*)command;

// API Manager
- (void)getVenueWithId:(CDVInvokedUrlCommand*)command;
- (void)getVenuesWithFilter:(CDVInvokedUrlCommand*)command;
- (void)getVenueWithName:(CDVInvokedUrlCommand*)command;
- (void)getVenueWithAlias:(CDVInvokedUrlCommand*)command;
- (void)getPlaceWithId:(CDVInvokedUrlCommand*)command;
- (void)getPlaceWithName:(CDVInvokedUrlCommand*)command;
- (void)getPlaceWithAlias:(CDVInvokedUrlCommand*)command;
- (void)getPlacesWithFilter:(CDVInvokedUrlCommand*)command;
- (void)getPlaceListWithId:(CDVInvokedUrlCommand*)command;
- (void)getPlaceListWithName:(CDVInvokedUrlCommand*)command;
- (void)getPlaceListWithAlias:(CDVInvokedUrlCommand*)command;
- (void)getPlaceListsWithFilter:(CDVInvokedUrlCommand*)command;
- (void)getUniverseWithId:(CDVInvokedUrlCommand*)command;
- (void)getUniversesWithFilter:(CDVInvokedUrlCommand*)command;
- (void)getAccessibleUniversesWithVenue:(CDVInvokedUrlCommand*)command;
- (void)searchWithParams:(CDVInvokedUrlCommand*)command;


- (void)getDirectionWithFrom:(CDVInvokedUrlCommand*)command;
- (void)getDirectionWithDirectionPointsFrom:(CDVInvokedUrlCommand*)command;
- (void)getDirectionWithWayPointsFrom:(CDVInvokedUrlCommand*)command;
- (void)getDirectionWithDirectionAndWayPointsFrom:(CDVInvokedUrlCommand*)command;
- (void)getDistanceWithFrom:(CDVInvokedUrlCommand*)command;

@end



#endif
