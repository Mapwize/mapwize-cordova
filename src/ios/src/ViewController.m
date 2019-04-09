#import "Constants.h"
#import "ViewController.h"
#import "Mapwize.h"
#import <MapwizeUI/MapwizeUI.h>

@interface ViewController () <MWZMapwizeViewDelegate>

@property (nonatomic, retain) MWZMapwizeView* mapwizeView;
@property (nonatomic, retain) MWZOptions* opts;
@property (nonatomic, retain) Mapwize* plugin;
@property (nonatomic, retain) NSString* callbackId;

@end

@implementation ViewController

- (void)setOptions:(MWZOptions*)opts {
    NSLog(@"setOptions, viewController...");
    _opts = opts;
}

- (void) selectPlace:(MWZPlace*) place centerOn:(BOOL) centerOn callbackId: (NSString*) callbackId {
    dispatch_async(dispatch_get_main_queue(), ^{
        [self.mapwizeView selectPlace:place centerOn:centerOn];
        NSDictionary *dict = @{ CBK_SELECT_PLACE_ID : place.identifier, CBK_SELECT_PLACE_CENTERON : centerOn ? @"true" : @"false"};
        [self sendCommandCallback:dict callbackId:callbackId];
    });
}

//- (void) selectPlace:(MWZPlace*) place centerOn:(BOOL) centerOn {
//    dispatch_async(dispatch_get_main_queue(), ^{
//        [self.mapwizeView selectPlace:place centerOn:centerOn];
//        NSString* json = [place toJSONString];
//        NSDictionary *dict = @{ CBK_SELECT_PLACE_ID : place.identifier, CBK_SELECT_PLACE_CENTERON : centerOn ? @"true" : @"false"};
//        [self sendCallbackEvent:CBK_EVENT_TAP_ON_PLACE_INFORMATION_BUTTON args:dict];
//    });
//
//}

- (void) selectPlaceList:(MWZPlaceList*) placeList callbackId:(NSString*) callbackId {
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

- (void) unselectContent:(BOOL) closeInfo callbackId:(NSString*) callbackId {
    dispatch_async(dispatch_get_main_queue(), ^{
        [self.mapwizeView unselectContent:closeInfo];
        [self sendCommandCallbackOK:callbackId];
    });
}

- (void) setPlugin:(Mapwize*) plugin callbackId:(NSString*) callbackId {
    _plugin = plugin;
    _callbackId = callbackId;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    MWZMapwizeViewUISettings* settings = [[MWZMapwizeViewUISettings alloc] init];
    settings.followUserButtonIsHidden = NO;
    settings.menuButtonIsHidden = NO;
    //settings.mainColor = [UIColor orangeColor];           // Change main color to Orange
    
    self.mapwizeView = [[MWZMapwizeView alloc] initWithFrame:self.view.frame
                                               mapwizeOptions:_opts
                                               uiSettings:settings];
    self.mapwizeView.delegate = self;
    self.mapwizeView.translatesAutoresizingMaskIntoConstraints = NO;
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
}

- (void)mapwizeView:(MWZMapwizeView *)mapwizeView didTapOnPlaceInformationButton:(MWZPlace *)place {
    NSLog(@"didTapOnPlaceInformations");
    NSString* json = [place toJSONString];
    [self sendCallbackEvent:CBK_EVENT_TAP_ON_PLACE_INFORMATION_BUTTON arg:json];
}

- (void)mapwizeView:(MWZMapwizeView *)mapwizeView didTapOnPlaceListInformationButton:(MWZPlaceList *)placeList {
    NSLog(@"didTapOnPlaceListInformations");
    NSString* json = [placeList toJSONString];
    [self sendCallbackEvent:CBK_EVENT_TAP_ON_PLACES_INFORMATION_BUTTON arg:json];
}

- (void)mapwizeViewDidTapOnFollowWithoutLocation:(MWZMapwizeView *)mapwizeView {
    NSLog(@"mapwizeViewDidTapOnFollowWithoutLocation");
    [self sendCallbackEvent:CBK_EVENT_DID_TAP_ON_FOLLOW_WITHOUT_LOCATION];
}

- (void)mapwizeViewDidTapOnMenu:(MWZMapwizeView *)mapwizeView {
    NSLog(@"mapwizeViewDidTapOnMenu");
    [self sendCallbackEvent:CBK_EVENT_DID_TAP_ON_MENU];
}

- (void) mapwizeViewDidLoad:(MWZMapwizeView*) mapwizeView {
    NSLog(@"mapwizeViewDidLoad");
    [self sendCallbackEvent:CBK_EVENT_DID_LOAD];
}

- (BOOL) mapwizeView:(MWZMapwizeView *)mapwizeView shouldShowInformationButtonFor:(id<MWZObject>)mapwizeObject {
    [self sendCallbackEvent:CBK_EVENT_SHOULD_SHOW_INFORMATION_BUTTON_FOR]; //TODO: What to pass for argument? (type, object) ?
    if ([mapwizeObject isKindOfClass:MWZPlace.class]) {
        return YES;
    } else if ([mapwizeObject isKindOfClass:MWZPlaceList.class]) {
        return YES;
    }
    return NO;
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
    [pluginResult setKeepCallback: [NSNumber numberWithBool:YES]];
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
//    dict[CBK_FIELD_EVENT] = event;
    dict[CBK_FIELD_ARG] = argStr;
    [self sendCallback:dict callbackId:callbackId];
}


@end
