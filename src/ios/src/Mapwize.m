//
//  FlashPlugin.m
//  FlashDemo
//
//  Created by Blum László on 27/07/15.
//  Copyright (c) 2015 Halifone Ltd. All rights reserved.
//

#import <Foundation/Foundation.h>

#import <AVFoundation/AVFoundation.h>
#import <Cordova/CDV.h>
#import "Mapwize.h"
#import "ViewController.h"
#import <MapwizeUI/MapwizeUI.h>


@implementation Mapwize
{
    BOOL enabled;
    NSNotification *_notification;
    UINavigationController* navController;
    ViewController* viewCtrl;
}
NSString* mCallbackId;


/**
 * Initialize the plugin.
 */
- (void) pluginInitialize
{
    NSLog(@"pluginInitialize...");
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
    
    viewCtrl = [[ViewController alloc] init];
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

@end
