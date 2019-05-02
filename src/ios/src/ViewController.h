//
//  ViewController.h
//  Mapwize Tester
//
//  Created by Laszlo Blum on 2019. 04. 23..
//

#ifndef ViewController_h
#define ViewController_h

#import <Cordova/CDV.h>
#import <MapwizeUI/MapwizeUI.h>

@class Mapwize;


#import <UIKit/UIKit.h>

@interface ViewController : UIViewController
-(void)deinit;
- (void)setOptions:(MWZOptions*)opts showInformationButtonForPlaces:(BOOL)showInformationButtonForPlaces showInformationButtonForPlaceLists:(BOOL)showInformationButtonForPlaceLists;
- (void) setPlaceStyle:(MWZPlace*) place style:(NSString*) style callbackId:(NSString*) callbackId;
- (void) selectPlace:(MWZPlace*) place centerOn:(BOOL) centerOn callbackId:(NSString*) callbackId;
- (void) selectPlaceList:(MWZPlaceList*) placeList callbackId:(NSString*) callbackId;
- (void) setPlugin:(Mapwize*) mapwize callbackId:(NSString*) callbackId;
- (void) grantAccess:(NSString*) accessKey callbackId:(NSString*) callbackId;
- (void) unselectContent:(BOOL) closeInfo callbackId:(NSString*) callbackId;

- (void)viewDidLoad;
- (void)mapwizeView:(MWZMapwizeView *)mapwizeView didTapOnPlaceInformationButton:(MWZPlace *)place;
- (void)mapwizeView:(MWZMapwizeView *)mapwizeView didTapOnPlaceListInformationButton:(MWZPlaceList *)placeList;
- (void)mapwizeViewDidTapOnFollowWithoutLocation:(MWZMapwizeView *)mapwizeView;
- (void)mapwizeViewDidTapOnMenu:(MWZMapwizeView *)mapwizeView;
- (void)mapwizeViewDidLoad:(MWZMapwizeView*) mapwizeView;
- (BOOL)mapwizeView:(MWZMapwizeView *)mapwizeView shouldShowInformationButtonFor:(id<MWZObject>)mapwizeObject;
@end

#endif
