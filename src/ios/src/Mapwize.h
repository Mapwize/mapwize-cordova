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
- (void)selectPlace:(CDVInvokedUrlCommand*)command;
- (void)selectPlaceList:(CDVInvokedUrlCommand*)command;
- (void)grantAccess:(CDVInvokedUrlCommand*)command;
- (void)unselectContent:(CDVInvokedUrlCommand*)command;

- (NSArray<MWZUniverse*>*) getUniverses:( NSArray * )universesDict;
- (MWZUniverse*) getUniverse:( NSArray * )universeDict;


// - (void)initApp:(CDVInvokedUrlCommand*)command;
// - (void)setCallback:(CDVInvokedUrlCommand*)command;
// - (void)isBackgroundPlaying:(CDVInvokedUrlCommand*)command;


@end



#endif
