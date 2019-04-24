//
//  FlashPlugin.h
//  FlashDemo
//
//  Created by Blum László on 27/07/15.
//  Copyright (c) 2015 Halifone Ltd. All rights reserved.
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
- (NSArray<MWZUniverse*>*) getUniverses:( NSArray * )universesDict;
- (MWZUniverse*) getUniverse:( NSArray * )universeDict;

// Offline manager
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
- (void)getPlacesWithName:(CDVInvokedUrlCommand*)command;
- (void)getPlacesWithAlias:(CDVInvokedUrlCommand*)command;
- (void)getPlacesWithFilter:(CDVInvokedUrlCommand*)command;
- (void)getPlaceListWithId:(CDVInvokedUrlCommand*)command;
- (void)getPlaceListsWithName:(CDVInvokedUrlCommand*)command;
- (void)getPlaceListsWithAlias:(CDVInvokedUrlCommand*)command;
- (void)getPlaceListsWithFilter:(CDVInvokedUrlCommand*)command;
- (void)getUniverseWithId:(CDVInvokedUrlCommand*)command;
- (void)getUniversesWithFilter:(CDVInvokedUrlCommand*)command;
- (void)getAccessibleUniversesWithVenue:(CDVInvokedUrlCommand*)command;
- (void)searchWithParams:(CDVInvokedUrlCommand*)command;

@end



#endif
