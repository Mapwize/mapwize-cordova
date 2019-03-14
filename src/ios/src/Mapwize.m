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
#import "MapwizeUI.h"



@implementation Mapwize
{
    BOOL enabled;
    NSNotification *_notification;
    ViewController* viewController;
    
}
NSString* mCallbackId;

// NSString *const ACTION_IS_AVAILABLE = @"isAvailable";
// NSString *const ACTION_SET_FLASH = @"setFlash";



// NSString *const kAPPBackgroundJsNamespace = @"cordova.plugins.backgroundMode";
// NSString *const kAPPBackgroundEventActivate = @"activate";
// NSString *const kAPPBackgroundEventDeactivate = @"deactivate";
// NSString *const kAPPBackgroundEventFailure = @"failure";

// FlashPluginImpl* mpPlugin;
// NSString* mCallbackId;
// AVAudioPlayer *audioPlayer;
// BOOL enabled;


/**
 * Initialize the plugin.
 */
- (void) pluginInitialize
{
    NSLog(@"pluginInitialize...");
//    [self disable:NULL];
//    [self configureAudioPlayer];
//    [self configureAudioSession];
//    [self observeLifeCycle];
    viewController = [[ViewController alloc] init];
}

- (void)dealloc {
    NSLog(@"dealloc...");
    [UIApplication sharedApplication].idleTimerDisabled = NO;
//    delete mpPlugin;
}

- (void)setCallback:(CDVInvokedUrlCommand*)command {
    NSLog(@"setCallback called...");
    mCallbackId = command.callbackId;
    [viewController setPlugin:self callbackId:mCallbackId];
}

- (void)createMapwizeView:(CDVInvokedUrlCommand*)command {
    NSLog(@"createMapwizeView called...");
    NSString *optionsStr = [command.arguments objectAtIndex:0];
    NSData *data = [optionsStr dataUsingEncoding:NSUTF8StringEncoding];

    NSError *jsonError;
    NSLog(@"converting json...");
    NSDictionary *json = [NSJSONSerialization JSONObjectWithData:data
                                 options:NSJSONReadingMutableContainers
                                   error:&jsonError];
    NSLog(@"creating opts...");
    MWZOptions *opts = [[MWZOptions alloc] init];
    
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
    
    [viewController setOptions:opts];
    [self.viewController presentViewController:viewController
                                      animated:NO
                                    completion:nil];
    
    NSLog(@"createMapwizeView END...");
}

- (void)selectPlace:(CDVInvokedUrlCommand*)command {
    NSLog(@"selectPlace called...");
    NSString *identifier = [command.arguments objectAtIndex:0];
    BOOL    centerOn = [command.arguments objectAtIndex:1];
    
    NSURLSessionDataTask* task = [MWZApi getPlaceWithId:identifier success:^(MWZPlace *place) {
        NSLog(@"identifier...");
        [viewController selectPlace:place centerOn:centerOn];
    } failure:^(NSError *error) {
        NSLog(@"error...");
    }];
    
    [task resume];
}

- (void)selectPlaceList:(CDVInvokedUrlCommand*)command {
    NSLog(@"selectPlaceList called...");
    NSString *placesStr = [command.arguments objectAtIndex:0];
    
    NSData *data = [placesStr dataUsingEncoding:NSUTF8StringEncoding];
    
    NSError *jsonError;
    NSLog(@"converting json...");
    NSArray *json = [NSJSONSerialization JSONObjectWithData:data
                                             options:NSJSONReadingMutableContainers
                                             error:&jsonError];
    
//    NSURLSessionDataTask* task = [MWZApi getPlaceWithId:identifier success:^(MWZPlace *place) {
//        NSLog(@"identifier...");
//        [viewController selectPlace:place centerOn:centerOn];
//    } failure:^(NSError *error) {
//        NSLog(@"error...");
//    }];
//    
//    [task resume];
}


- (void)grantAccess:(CDVInvokedUrlCommand*)command {
    NSLog(@"grantAccess...");
}
- (void)unselectContent:(CDVInvokedUrlCommand*)command  {
    NSLog(@"unselectContent...");
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
