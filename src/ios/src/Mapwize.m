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
#import "MapwizeUI.h"

@implementation Mapwize
{
    BOOL enabled;
    NSNotification *_notification;
    UINavigationController* navController;
    ViewController* viewCtrl;
    OfflineManager* offlineManager;
}
NSString* mCallbackId;

/**
 * Initialize the plugin.
 */
- (void) pluginInitialize
{
    NSLog(@"pluginInitialize...");
    viewCtrl = [[ViewController alloc] init];
    
    NSLog(@"ApiManager initManager called...");
    [ApiManager initManager:self];
}

- (void)dealloc {
    NSLog(@"dealloc...");
    [UIApplication sharedApplication].idleTimerDisabled = NO;
}

- (void)setCallback:(CDVInvokedUrlCommand*)command {
    NSLog(@"setCallback called...");
    mCallbackId = command.callbackId;
    [viewCtrl setPlugin:self callbackId:mCallbackId];
    
    NSLog(@"setCallback called...END");
}

- (void)createMapwizeView:(CDVInvokedUrlCommand*)command {
    NSLog(@"createMapwizeView called...");
    BOOL showCloseButton = YES;
    
    if (showCloseButton == YES) {
        navController = [[UINavigationController alloc] initWithRootViewController:viewCtrl];
    }
    
    NSString *optionsStr = [command.arguments objectAtIndex:0];
    NSData *data = [optionsStr dataUsingEncoding:NSUTF8StringEncoding];

    NSError *jsonError;
    NSLog(@"converting json...");
    NSDictionary *json = [NSJSONSerialization JSONObjectWithData:data
                                 options:NSJSONReadingMutableContainers
                                   error:&jsonError];
    NSLog(@"creating opts...");
    MWZUIOptions *opts = [[MWZUIOptions alloc] init];
    
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
    
    [viewCtrl setOptions:opts];
    
    NSLog(@"getting addChildViewController...");
    NSLog(@"getting presentViewController...");
    
    if (showCloseButton == YES) {
        [self.viewController presentViewController:navController
                                          animated:NO
                                        completion:nil];
    } else {
        [self.viewController presentViewController:viewCtrl
                                          animated:NO
                                        completion:nil];
    }

    NSLog(@"createMapwizeView END...");
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)closeMapwizeView:(CDVInvokedUrlCommand*)command {
    NSLog(@"closeMapwizeView called...");
    [self.viewController dismissViewControllerAnimated:NO completion:nil];
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)setPlaceStyle:(CDVInvokedUrlCommand*)command {
    NSLog(@"setPlaceStyle called...");
    NSString *identifier = [command.arguments objectAtIndex:0];
    NSString *styleStr = [command.arguments objectAtIndex:1];
    
    NSURLSessionDataTask* task = [MWZApi getPlaceWithId:identifier success:^(MWZPlace *place) {
        NSLog(@"identifier...");
        [self->viewCtrl setPlaceStyle:place style:styleStr callbackId:command.callbackId];
    } failure:^(NSError *error) {
        NSLog(@"error...");
    }];
    
    [task resume];
}

- (void)selectPlace:(CDVInvokedUrlCommand*)command {
    NSLog(@"selectPlace called...");
    NSString *identifier = [command.arguments objectAtIndex:0];
    BOOL    centerOn = [command.arguments objectAtIndex:1];
    
    NSURLSessionDataTask* task = [MWZApi getPlaceWithId:identifier success:^(MWZPlace *place) {
        NSLog(@"identifier...");
        [viewCtrl selectPlace:place centerOn:centerOn callbackId:command.callbackId];
    } failure:^(NSError *error) {
        NSLog(@"error...");
    }];
    
    [task resume];
}

- (void)selectPlaceList:(CDVInvokedUrlCommand*)command {
    NSLog(@"selectPlaceList called...");
    NSString *identifier = [command.arguments objectAtIndex:0];
    
    NSURLSessionDataTask* task = [MWZApi getPlaceListWithId:identifier success:^(MWZPlaceList *placeList) {
        NSLog(@"identifier...");
        [viewCtrl selectPlaceList:placeList callbackId:command.callbackId];
    } failure:^(NSError *error) {
        NSLog(@"error...");
    }];
    
    [task resume];
}

- (void)grantAccess:(CDVInvokedUrlCommand*)command {
    NSLog(@"grantAccess...");
    NSString *accessKey = [command.arguments objectAtIndex:0];
    [viewCtrl grantAccess:accessKey callbackId:command.callbackId];
}

- (void)unselectContent:(CDVInvokedUrlCommand*)command  {
    NSLog(@"unselectContent...");
    BOOL closeInfo = [command.arguments objectAtIndex:0];
    [viewCtrl unselectContent:closeInfo callbackId:command.callbackId];
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


@end
