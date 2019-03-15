//
//  FlashPlugin.h
//  FlashDemo
//
//  Created by Blum László on 27/07/15.
//  Copyright (c) 2015 Halifone Ltd. All rights reserved.
//

#ifndef ViewController_h
#define ViewController_h

#import <Cordova/CDV.h>
#import <MapwizeUI/MapwizeUI.h>

@class Mapwize;


#import <UIKit/UIKit.h>

@interface ViewController : UIViewController
- (void)setOptions:(MWZOptions*)opts;
- (void) selectPlace:(MWZPlace*) place centerOn:(BOOL) centerOn;
- (void) setPlugin:(Mapwize*) mapwize callbackId: (NSString*) callbackId;
- (void) grantAccess:(NSString*) accessKey;
- (void) unselectContent:(BOOL) closeInfo;


- (void)viewDidLoad;
- (void)mapwizeView:(MWZMapwizeView *)mapwizeView didTapOnPlaceInformationButton:(MWZPlace *)place;
- (void)mapwizeView:(MWZMapwizeView *)mapwizeView didTapOnPlaceListInformationButton:(MWZPlaceList *)placeList;
- (void)mapwizeViewDidTapOnFollowWithoutLocation:(MWZMapwizeView *)mapwizeView;
- (void)mapwizeViewDidTapOnMenu:(MWZMapwizeView *)mapwizeView;
- (void) mapwizeViewDidLoad:(MWZMapwizeView*) mapwizeView;
- (BOOL) mapwizeView:(MWZMapwizeView *)mapwizeView shouldShowInformationButtonFor:(id<MWZObject>)mapwizeObject;
@end

#endif
