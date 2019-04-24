//
//  OfflineManager.m
//  Mapwize Tester
//
//  Created by Laszlo Blum on 2019. 04. 22..
//

#import "OfflineManager.h"
#import "MapwizeUI.h"
#import "Constants.h"

@interface OfflineManager ()

//@property (nonatomic, retain) MGLMapView* mapView;
@property (nonatomic, retain) MWZOfflineManager* offlineManager;
@property (nonatomic, retain) Mapwize* plugin;

@end

@implementation OfflineManager

//- (void) initManager:(MGLMapView*) mapView plugin:(Mapwize*) plugin {
- (void) initManager:(Mapwize*) plugin {
    self.plugin = plugin;
//    self.mapView = mapView;
    self.offlineManager = [[MWZOfflineManager alloc] init];
}

- (void) removeDataForVenue:(NSString*) venueId universe:(NSString*) universeId callbackId:(NSString*) callbackId {
    NSLog(@"removeDataForVenue...");
    NSURLSessionDataTask* venueTask = [MWZApi getVenueWithId:venueId success:^(MWZVenue *venue) {
        NSLog(@"venue received...");
        NSURLSessionDataTask* universeTask = [MWZApi getUniverseWithId:universeId success:^(MWZUniverse *universe) {
            NSLog(@"universe received...");
            [self.offlineManager removeDataForVenue:venue universe:universe];
            [self sendCommandCallbackOK:callbackId];
        } failure:^(NSError *error) {
            NSLog(@"Failed to receive universe...");
            [self sendCommandCallbackFailed:callbackId];
        }];
        [universeTask resume];
      } failure:^(NSError *error) {
          NSLog(@"Failed to receive venue...");
          [self sendCommandCallbackFailed:callbackId];
      }];
    
    [venueTask resume];
}

- (void) downloadDataForVenue:(NSString*) venueId
                     universe:(NSString*) universeId
                   callbackId:(NSString*) callbackId {
    NSLog(@"downloadDataForVenue...");
    NSURLSessionDataTask* venueTask = [MWZApi getVenueWithId:venueId success:^(MWZVenue *venue) {
        NSLog(@"venue received...");
        NSURLSessionDataTask* universeTask = [MWZApi getUniverseWithId:universeId success:^(MWZUniverse *universe) {
            NSLog(@"universe received...");
            [self.offlineManager downloadDataForVenue:venue universe:universe success:^{
                NSLog(@"Success...");
                [self sendCommandCallbackOK:callbackId];
            } progress:^(int count) {
                NSLog(@"In progress...");
            } failure:^(NSError * _Nonnull error) {
                NSLog(@"Failue...");
                [self sendCommandCallbackFailed:callbackId];
            }];
            
        } failure:^(NSError *error) {
            NSLog(@"Failed to receive universe...");
            [self sendCommandCallbackFailed:callbackId];
        }];
        
        [universeTask resume];
    } failure:^(NSError *error) {
        NSLog(@"Failed to receive venue...");
        [self sendCommandCallbackFailed:callbackId];
    }];
    [venueTask resume];
}

- (void) isOfflineForVenue:(NSString*) venueId universe:(NSString*) universeId callbackId:(NSString*) callbackId {
    NSLog(@"isOfflineForVenue...");
    NSURLSessionDataTask* venueTask = [MWZApi getVenueWithId:venueId success:^(MWZVenue *venue) {
        NSLog(@"venue received...");
        NSURLSessionDataTask* universeTask = [MWZApi getUniverseWithId:universeId success:^(MWZUniverse *universe) {
            NSLog(@"universe received...");
            BOOL isOffline = [self.offlineManager isOfflineForVenue:venue universe:universe];
            NSMutableDictionary *dict = [NSMutableDictionary dictionary];
            dict[CBK_FIELD_ARG] = isOffline ? @"true" : @"false";
            [self sendCommandDictCallback:dict callbackId:callbackId];
        } failure:^(NSError *error) {
            NSLog(@"Failed to receive universe...");
            [self sendCommandCallbackFailed:callbackId];
        }];
        
        [universeTask resume];
    } failure:^(NSError *error) {
        NSLog(@"Failed to receive venue...");
        [self sendCommandCallbackFailed:callbackId];
    }];
    
    [venueTask resume];
}

- (void) getOfflineVenues:(NSString*) callbackId {
    NSLog(@"getOfflineVenues...");
    NSArray<MWZVenue*>* venues = [self.offlineManager getOfflineVenues];
    
    NSString* result = [self venues2JsonArray:venues];
    NSMutableDictionary *dict = [NSMutableDictionary dictionary];
    dict[CBK_FIELD_ARG] = result;
    [self sendCommandDictCallback:dict callbackId:callbackId];
}

- (void) getOfflineUniversesForVenue:(NSString*) venueId callbackId:(NSString*) callbackId {
    NSURLSessionDataTask* universeTask = [MWZApi getVenueWithId:venueId success:^(MWZVenue *venue) {
        NSLog(@"venue received...");
        NSArray<MWZUniverse*>* universes =[self.offlineManager getOfflineUniversesForVenue:venue];
        NSString* result = [self universes2JsonArray:universes];
        NSMutableDictionary *dict = [NSMutableDictionary dictionary];
        dict[CBK_FIELD_ARG] = result;
        [self sendCommandDictCallback:dict callbackId:callbackId];
    } failure:^(NSError *error) {
        NSLog(@"Failed to receive venue...");
        [self sendCommandCallbackFailed:callbackId];
    }];
    
    [universeTask resume];
}

- (NSString*) venues2JsonArray:(NSArray<MWZVenue*>*) array {
    NSMutableArray* ma = [[NSMutableArray alloc] init];
    for(MWZVenue* venue in array) {
        NSString* vstr = [venue toJSONString];
        [ma addObject:vstr];
    }
    
    NSString* csString = [ma componentsJoinedByString:@","];
    NSString* jsonString = [NSString stringWithFormat:@"[%@]", csString];
    return jsonString;
}

- (NSString*) universes2JsonArray:(NSArray<MWZUniverse*>*) array {
    NSMutableArray* ma = [[NSMutableArray alloc] init];
    for(MWZUniverse* venue in array) {
        NSString* vstr = [venue toJSONString];
        [ma addObject:vstr];
    }
    
    NSString* csString = [ma componentsJoinedByString:@","];
    NSString* jsonString = [NSString stringWithFormat:@"[%@]", csString];
    return jsonString;
}

- (void) sendCommandDictCallback:(NSMutableDictionary*)dict callbackId:(NSString*)callbackId {
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK
                                                  messageAsDictionary:dict];
    [_plugin.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
}

- (void) sendCommandCallbackOK:(NSString*)callbackId  {
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [_plugin.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
}

- (void) sendCommandCallback:(NSDictionary*)args callbackId:(NSString*)callbackId {
    NSMutableDictionary *dict = [NSMutableDictionary dictionary];
    NSError *err;
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:args options:NSJSONWritingPrettyPrinted error:&err];
    NSString* argStr = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
    //    dict[CBK_FIELD_EVENT] = event;
    dict[CBK_FIELD_ARG] = argStr;
    [self sendCommandDictCallback:dict callbackId:callbackId];
}

- (void) sendCommandCallbackFailed:(NSString*)callbackId  {
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
    [_plugin.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
}


@end
