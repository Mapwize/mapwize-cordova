//
//  ViewController.m
//  Mapwize Tester
//
//  Created by Laszlo Blum on 2019. 04. 23..
//

#import "Constants.h"
#import "ViewController.h"
#import "Mapwize.h"
#import <MapwizeSDK/MWZPlacelist.h>

@interface ViewController () <MWZUIViewDelegate>

@property (nonatomic, retain) MWZUIView* mapwizeView;
@property (nonatomic, retain) MWZUIOptions* opts;
@property (nonatomic, retain) MWZUISettings* uiSettings;
@property (nonatomic, retain) Mapwize* plugin;
@property (nonatomic, retain) NSString* callbackId;

@end

@implementation ViewController

BOOL showCloseButton;
BOOL showInfoButtonForPlaces;
BOOL showInfoButtonForPlaceLists;


-(void)deinit {
    NSLog(@"ViewController, deinit...");
}

-(UIBarPosition)positionForBar:(id<UIBarPositioning>)bar {
    NSLog(@"positionForBar called...");
    return UIBarPositionTop;
}

- (void)setOptions:(MWZOptions*)opts showInformationButtonForPlaces:(BOOL)showInformationButtonForPlaces showInformationButtonForPlaceLists:(BOOL)showInformationButtonForPlaceLists {
    NSLog(@"setOptions, viewController...");
    _opts = [self getUIOptions:opts];
    showInfoButtonForPlaces = showInformationButtonForPlaces;
    showInfoButtonForPlaceLists = showInformationButtonForPlaceLists;
}

- (MWZUIOptions*) getUIOptions:(MWZOptions*)options {
    NSLog(@"viewController, getUIOptions...");
    MWZUIOptions* uiOptions = [[MWZUIOptions alloc] init];

    uiOptions.floor = options.floor;
    uiOptions.language = options.language;
    uiOptions.universeId = options.universeId;
    uiOptions.restrictContentToVenueId = options.restrictContentToVenueId;
    uiOptions.restrictContentToVenueIds = options.restrictContentToVenueIds;
    uiOptions.restrictContentToOrganizationId = options.restrictContentToOrganizationId;
    uiOptions.centerOnVenueId = options.centerOnVenueId;
    uiOptions.centerOnPlaceId = options.centerOnPlaceId;
    uiOptions.mainColor = options.mainColor;
    
    return uiOptions;
}

- (void) setUiSettings:(MWZUISettings*)uiSettings {
    NSLog(@"setUiSettings, viewController...");
    _uiSettings = uiSettings;
}

- (void) setPlaceStyle:(MWZPlace*) place style:(NSString*) style callbackId:(NSString*) callbackId {
    MWZStyle* styleObj = [MWZApiResponseParser parseStyleWith:style];
    dispatch_async(dispatch_get_main_queue(), ^{
        [self.mapwizeView.mapView setStyle:styleObj forPlace:place];
        NSDictionary *dict = @{ CBK_SET_PLACE_STYLE_ID : place.identifier};
        [self sendCommandCallback:dict callbackId:callbackId];
    });
}

- (void) selectPlace:(MWZPlace*) place centerOn:(BOOL) centerOn callbackId: (NSString*) callbackId {
    dispatch_async(dispatch_get_main_queue(), ^{
        [self.mapwizeView selectPlace:place centerOn:centerOn];
        NSDictionary *dict = @{ CBK_SELECT_PLACE_ID : place.identifier, CBK_SELECT_PLACE_CENTERON : centerOn ? @"true" : @"false"};
        [self sendCommandCallback:dict callbackId:callbackId];
    });
}

- (void) selectPlaceList:(MWZPlacelist*) placeList callbackId:(NSString*) callbackId {
    dispatch_async(dispatch_get_main_queue(), ^{
        [self.mapwizeView selectPlaceList:placeList];
        NSDictionary *dict = @{ CBK_SELECT_PLACELIST_ID : placeList.identifier};
        [self sendCommandCallback:dict callbackId:callbackId];
    });
}

- (void) grantAccess:(NSString*) accessKey callbackId:(NSString*) callbackId {
    dispatch_async(dispatch_get_main_queue(), ^{
        [self.mapwizeView grantAccess:accessKey success:^{
            [self sendCommandCallbackOK:callbackId];
        } failure:^(NSError * _Nonnull error) {
            [self sendCommandCallbackFailed:callbackId];
        }];
    });
}

- (void) unselectContent:(NSString*) callbackId {
    dispatch_async(dispatch_get_main_queue(), ^{
        [self.mapwizeView unselectContent];
        [self sendCommandCallbackOK:callbackId];
    });
}

- (void) setDirection:(MWZDirection*) direction from:(id<MWZDirectionPoint>) from to:(id<MWZDirectionPoint>) to isAccessible:(BOOL) isAccessible callbackId:(NSString*) callbackId {
    dispatch_async(dispatch_get_main_queue(), ^{
        [self.mapwizeView
         setDirection:direction
         from:from
         to:to
         isAccessible:isAccessible
         ];
        [self sendCommandCallbackOK:callbackId];
    });
}

- (void) setPlugin:(Mapwize*) plugin callbackId:(NSString*) callbackId {
    NSLog(@"setPlugin...");
    _plugin = plugin;
    _callbackId = callbackId;
    NSLog(@"setPlugin...END");
}

- (void)viewWillAppear:(BOOL)animated {
    self.modalPresentationStyle = UIModalPresentationFullScreen;
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    NSLog(@"viewDidAppear...");
    UINavigationBar* navibar = self.navigationController.navigationBar;
    
    if (navibar != nil) {
        NSLog(@"Navi is not null...");
    }
}

- (void)viewDidLoad {
    [super viewDidLoad];
    NSLog(@"viewDidLoad...");
    self.mapwizeView = [[MWZUIView alloc] initWithFrame:self.view.frame
                                              mapwizeOptions:_opts
                                                  uiSettings:_uiSettings];
    self.mapwizeView.delegate = self;
    self.mapwizeView.translatesAutoresizingMaskIntoConstraints = NO;
    
    self.modalPresentationStyle = UIModalPresentationOverFullScreen;
    
    NSLog(@"self.topLayoutGuide.length: %lf", self.topLayoutGuide.length);
    UIBarButtonItem* doneBtn = [[UIBarButtonItem alloc]
                                initWithTitle:NSLocalizedString(@"Back", comment: nil)
                                style:UIBarButtonItemStylePlain
                                target:self
                                action:@selector(onTapDone:)];
    
    self.navigationItem.rightBarButtonItem = doneBtn;
    [self.view addSubview:self.mapwizeView];

    [[NSLayoutConstraint constraintWithItem:self.mapwizeView
                                  attribute:NSLayoutAttributeLeft
                                  relatedBy:NSLayoutRelationEqual
                                     toItem:self.view
                                  attribute:NSLayoutAttributeLeft
                                 multiplier:1.0f
                                   constant:0.0f] setActive:YES];
    [[NSLayoutConstraint constraintWithItem:self.mapwizeView
                                  attribute:NSLayoutAttributeTop
                                  relatedBy:NSLayoutRelationEqual
                                     toItem:self.view
                                  attribute:NSLayoutAttributeTop
                                 multiplier:1.0f
                                   constant:0.0f] setActive:YES];
    [[NSLayoutConstraint constraintWithItem:self.mapwizeView
                                  attribute:NSLayoutAttributeBottom
                                  relatedBy:NSLayoutRelationEqual
                                     toItem:self.view
                                  attribute:NSLayoutAttributeBottom
                                 multiplier:1.0f
                                   constant:0.0f] setActive:YES];
    [[NSLayoutConstraint constraintWithItem:self.mapwizeView
                                  attribute:NSLayoutAttributeRight
                                  relatedBy:NSLayoutRelationEqual
                                     toItem:self.view
                                  attribute:NSLayoutAttributeRight
                                 multiplier:1.0f
                                   constant:0.0f] setActive:YES];
    NSLog(@"viewDidLoad...END");
}

- (void)onTapDone:(id)sender {
    NSLog(@"onTapDone");
    UINavigationBar* navibar = self.navigationController.navigationBar;
    if (navibar != nil) {
        [self.navigationController dismissViewControllerAnimated:TRUE completion:nil];
    } else {
        [self dismissViewControllerAnimated:NO completion:nil];
    }
    [self sendCallbackEvent:CBK_EVENT_CLOSE_BUTTON_CLICKED];
}

- (void)mapwizeView:(MWZUIView *)mapwizeView didTapOnPlaceInformationButton:(MWZPlace *)place {
    NSLog(@"didTapOnPlaceInformations");
    NSString* json = [place toJSONString];
    [self sendCallbackEvent:CBK_EVENT_TAP_ON_PLACE_INFORMATION_BUTTON arg:json];
}

- (void)mapwizeViewDidTapOnFollowWithoutLocation:(MWZUIView *)mapwizeView {
    NSLog(@"mapwizeViewDidTapOnFollowWithoutLocation");
    [self sendCallbackEvent:CBK_EVENT_DID_TAP_ON_FOLLOW_WITHOUT_LOCATION];
}

- (void)mapwizeViewDidTapOnMenu:(MWZUIView *)mapwizeView {
    NSLog(@"mapwizeViewDidTapOnMenu");
    [self sendCallbackEvent:CBK_EVENT_DID_TAP_ON_MENU];
}

- (void) mapwizeViewDidLoad:(MWZUIView*) mapwizeView {
    NSLog(@"mapwizeViewDidLoad...");
    [self sendCallbackEvent:CBK_EVENT_DID_LOAD];
}

- (BOOL) mapwizeView:(MWZUIView *)mapwizeView shouldShowInformationButtonFor:(id<MWZObject>)mapwizeObject {
    NSLog(@"shouldShowInformationButtonFor...");
    NSDictionary* data = [mapwizeObject data];
    
    NSLog(@"shouldShowInformationButtonFor, CORDOVA_SHOW_INFORMATION_BUTTON...");
    if ([data objectForKey:CORDOVA_SHOW_INFORMATION_BUTTON] != nil) {
        NSLog(@"shouldShowInformationButtonFor, CORDOVA_SHOW_INFORMATION_BUTTON, getting value...");
        BOOL cordovaOn = [[data valueForKey:CORDOVA_SHOW_INFORMATION_BUTTON] boolValue];
        NSLog(@"shouldShowInformationButtonFor, CORDOVA_SHOW_INFORMATION_BUTTON, returning...value: %s", cordovaOn ? "true" : "false");
        return cordovaOn;
    }
    
    NSLog(@"shouldShowInformationButtonFor, ...");
    if ([mapwizeObject isKindOfClass:MWZPlace.class]) {
        NSLog(@"shouldShowInformationButtonFor, MWZPlace...");
        return showInfoButtonForPlaces;

    } else if ([mapwizeObject isKindOfClass:MWZPlacelist.class]) {
        NSLog(@"shouldShowInformationButtonFor, MWZPlacelist...");
        return showInfoButtonForPlaceLists;
    }
    return NO;
}

- (void)mapwizeView:(MWZUIView *)mapwizeView didTapOnPlacelistInformationButton:(MWZPlacelist *)placeList {
    NSLog(@"didTapOnPlaceListInformations");
    NSString* json = [placeList toJSONString];
    [self sendCallbackEvent:CBK_EVENT_TAP_ON_PLACES_INFORMATION_BUTTON arg:json];
}

- (void) sendCommandCallbackOK:(NSString*)callbackId  {
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [_plugin.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
}

- (void) sendCommandCallbackFailed:(NSString*)callbackId  {
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
    [_plugin.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
}


- (void) sendCallback:(NSMutableDictionary*)dict callbackId:(NSString*)callbackId {
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK
                                                  messageAsDictionary:dict];
    [pluginResult setKeepCallbackAsBool:YES];
    [_plugin.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
}

- (void) sendCallbackEvent:(NSString*)event {
    NSMutableDictionary *dict = [NSMutableDictionary dictionary];
    dict[CBK_FIELD_EVENT] = event;
    [self sendCallback:dict callbackId:_callbackId];
}

- (void) sendCallbackEvent:(NSString*)event arg:(NSString*)arg {
    NSMutableDictionary *dict = [NSMutableDictionary dictionary];
    dict[CBK_FIELD_EVENT] = event;
    dict[CBK_FIELD_ARG] = arg;
    [self sendCallback:dict callbackId:_callbackId];
}

- (void) sendCommandCallback:(NSDictionary*)args callbackId:(NSString*)callbackId {
    NSMutableDictionary *dict = [NSMutableDictionary dictionary];
    NSError *err;
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:args options:NSJSONWritingPrettyPrinted error:&err];
    NSString* argStr = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
    dict[CBK_FIELD_ARG] = argStr;
    [self sendCallback:dict callbackId:callbackId];
}

- (void) dealloc {
    NSLog(@"ViewController, dealloc...");
    [self.view willRemoveSubview:self.mapwizeView];
    self.navigationItem.rightBarButtonItem = nil;
    self.mapwizeView.delegate = nil;
    self.mapwizeView = nil;
    
}
@end
