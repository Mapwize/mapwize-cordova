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
#import <MapwizeUI/MWZUISettings.h>
#import <MapwizeUI/MWZUIView.h>


@class Mapwize;


#import <UIKit/UIKit.h>

@interface ViewController : UIViewController
-(void)deinit;
- (void) setOptions:(MWZOptions*)opts showInformationButtonForPlaces:(BOOL)showInformationButtonForPlaces showInformationButtonForPlaceLists:(BOOL)showInformationButtonForPlaceLists;
- (void) setUiSettings:(MWZUISettings*)uiSettings;
- (void) setPlaceStyle:(MWZPlace*) place style:(NSString*) style callbackId:(NSString*) callbackId;
- (void) selectPlace:(MWZPlace*) place centerOn:(BOOL) centerOn callbackId:(NSString*) callbackId;
- (void) selectPlaceList:(MWZPlacelist*) placeList callbackId:(NSString*) callbackId;
- (void) setPlugin:(Mapwize*) mapwize callbackId:(NSString*) callbackId;
- (void) grantAccess:(NSString*) accessKey callbackId:(NSString*) callbackId;
- (void) unselectContent:(NSString*) callbackId;
- (void) setDirection:(MWZDirection*) direction from:(id<MWZDirectionPoint>) from to:(id<MWZDirectionPoint>) to isAccessible:(BOOL) isAccessible callbackId:(NSString*) callbackId;

- (void)viewDidLoad;
- (void)mapwizeView:(MWZUIView *)mapwizeView didTapOnPlaceInformationButton:(MWZPlace *)place;
- (void)mapwizeView:(MWZUIView *)mapwizeView didTapOnPlacelistInformationButton:(MWZPlacelist *)placeList;
- (void)mapwizeViewDidTapOnFollowWithoutLocation:(MWZUIView *)mapwizeView;
- (void)mapwizeViewDidTapOnMenu:(MWZUIView *)mapwizeView;
- (void)mapwizeViewDidLoad:(MWZUIView*) mapwizeView;
- (BOOL)mapwizeView:(MWZUIView *)mapwizeView shouldShowInformationButtonFor:(id<MWZObject>)mapwizeObject;
- (void) dealloc;

@end

#endif
