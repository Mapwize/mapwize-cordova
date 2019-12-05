//
//  ApiManager.m
//  Mapwize Tester
//
//  Created by Laszlo Blum on 2019. 04. 23..
//

#import "ApiManager.h"
#import "Constants.h"


@interface ApiManager ()
+ (NSString*) venues2JsonArray:(NSArray<MWZVenue*>*) array;
+ (void) sendCommandDictCallback:(NSMutableDictionary*)dict callbackId:(NSString*)callbackId;
+ (void) sendCommandCallback:(NSDictionary*)args callbackId:(NSString*)callbackId;

+ (void) sendCommandCallbackOK:(NSString*) callbackId;
+ (void) sendCommandCallbackFailed:(NSString*)callbackId;
+ (MWZApiFilter*) deserializeApiFilter:(NSString*)filterStr;
+ (NSString*) placeLists2JsonArray:(NSArray<MWZPlacelist*>*) array;
+ (NSString*) universes2JsonArray:(NSArray<MWZUniverse*>*) array;

@end


@implementation ApiManager
static Mapwize* plugin;

+ (void) initManager:(Mapwize*) mapwizePlugin {
    NSLog(@"ApiManager, initManager...");
    plugin = mapwizePlugin;
    NSLog(@"ApiManager, initManager...END...");
}

+ (void)getVenueWithId:(NSString*) identifier callbackId:(NSString*) callbackId {
    [[MWZMapwizeApiFactory getApi] getVenueWithIdentifier:identifier success:^(MWZVenue *venue) {
        NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
        NSString* json = [venue toJSONString];
        dict[CBK_FIELD_ARG] = json;
        [ApiManager sendCommandDictCallback:dict callbackId:callbackId];
    } failure:^(NSError *error) {
        [ApiManager sendCommandCallbackFailed:callbackId];
    }];
}

+ (void)getVenuesWithFilter:(NSString*) filterStr callbackId:(NSString*) callbackId {
    MWZApiFilter* filter = [self deserializeApiFilter:filterStr];
    [[MWZMapwizeApiFactory getApi] getVenuesWithFilter:filter success:^(NSArray<MWZVenue *> *venues) {
        NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
        NSString* json = [ApiManager venues2JsonArray:venues];
        dict[CBK_FIELD_ARG] = json;
        [ApiManager sendCommandDictCallback:dict callbackId:callbackId];
    } failure:^(NSError *error) {
        [ApiManager sendCommandCallbackFailed:callbackId];
    }];
}

 + (void)getVenueWithName:(NSString*) name callbackId:(NSString*) callbackId {
     [[MWZMapwizeApiFactory getApi] getVenueWithName:name success:^(MWZVenue *venue) {
         NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
         NSString* json = [venue toJSONString];
         dict[CBK_FIELD_ARG] = json;
         [ApiManager sendCommandDictCallback:dict callbackId:callbackId];
     } failure:^(NSError *error) {
         [ApiManager sendCommandCallbackFailed:callbackId];
     }];
 }

+ (void)getVenueWithAlias:(NSString*) alias callbackId:(NSString*) callbackId {
    [[MWZMapwizeApiFactory getApi] getVenueWithAlias:alias success:^(MWZVenue *venue) {
        NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
        NSString* json = [venue toJSONString];
        dict[CBK_FIELD_ARG] = json;
        [ApiManager sendCommandDictCallback:dict callbackId:callbackId];
    } failure:^(NSError *error) {
        [ApiManager sendCommandCallbackFailed:callbackId];
    }];
}

+ (void)getPlaceWithId:(NSString*) identifier callbackId:(NSString*) callbackId {
    NSLog(@"ApiManager::getPlaceWithId...callbackId: %@", callbackId);
    [plugin.commandDelegate runInBackground:^{
        [[MWZMapwizeApiFactory getApi] getPlaceWithIdentifier:identifier success:^(MWZPlace *place) {
            NSLog(@"ApiManager::getPlaceWithId returns...callbackId: %@", callbackId);
            NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
            NSString* json = [place toJSONString];
            dict[CBK_FIELD_ARG] = json;
            NSLog(@"ApiManager::getPlaceWithId returns, sending to cordova...callbackId: %@", callbackId);
            [ApiManager sendCommandDictCallback:dict callbackId:callbackId];
        } failure:^(NSError *error) {
            NSLog(@"ApiManager::getPlaceWithId...error: %@", [error description]);
            [ApiManager sendCommandCallbackFailed:callbackId];
        }];
    }];
    
}

+ (void)getPlaceWithName:(NSString*) name venue:(NSString*) venueId callbackId:(NSString*) callbackId {
    [[MWZMapwizeApiFactory getApi] getVenueWithIdentifier:venueId success:^(MWZVenue *venue) {
        [[MWZMapwizeApiFactory getApi] getPlaceWithName:name venue:venue success:^(MWZPlace *place) {
            NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
            NSString* json = [venue toJSONString];
            dict[CBK_FIELD_ARG] = json;
            [ApiManager sendCommandDictCallback:dict callbackId:callbackId];
        } failure:^(NSError *error) {
            [ApiManager sendCommandCallbackFailed:callbackId];
        }];
    } failure:^(NSError *error) {
        [ApiManager sendCommandCallbackFailed:callbackId];
    }];
}

+ (void)getPlaceWithAlias:(NSString*) alias venue:(NSString*) venueId callbackId:(NSString*) callbackId {
    NSLog(@"getPlaceWithAlias...");
    [[MWZMapwizeApiFactory getApi] getVenueWithIdentifier:venueId success:^(MWZVenue *venue) {
        NSLog(@"getPlaceWithAlias, venue received...");
        [[MWZMapwizeApiFactory getApi] getPlaceWithAlias:alias venue:venue success:^(MWZPlace *place) {
            NSLog(@"getPlaceWithAlias, getPlacesWithAlias received...");
            NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
            NSString* json = [place toJSONString];
            dict[CBK_FIELD_ARG] = json;
            [ApiManager sendCommandDictCallback:dict callbackId:callbackId];
        } failure:^(NSError *error) {
            [ApiManager sendCommandCallbackFailed:callbackId];
        }];
    } failure:^(NSError *error) {
        [ApiManager sendCommandCallbackFailed:callbackId];
    }];
}

+ (void)getPlacesWithFilter:(NSString*) filterStr callbackId:(NSString*) callbackId {
    MWZApiFilter* filter = [ApiManager deserializeApiFilter:filterStr];
    [[MWZMapwizeApiFactory getApi] getVenuesWithFilter:filter success:^(NSArray<MWZVenue *> *venues) {
        NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
        NSString* json = [ApiManager venues2JsonArray:venues];
        dict[CBK_FIELD_ARG] = json;
        [ApiManager sendCommandDictCallback:dict callbackId:callbackId];
    } failure:^(NSError *error) {
        [ApiManager sendCommandCallbackFailed:callbackId];
    }];
}

+ (void)getPlaceListWithId:(NSString*) identifier callbackId:(NSString*) callbackId {
    [[MWZMapwizeApiFactory getApi] getPlacelistWithIdentifier:identifier success:^(MWZPlacelist *placeList) {
        NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
        NSString* json = [placeList toJSONString];
        dict[CBK_FIELD_ARG] = json;
        [ApiManager sendCommandDictCallback:dict callbackId:callbackId];
    } failure:^(NSError *error) {
        [ApiManager sendCommandCallbackFailed:callbackId];
    }];
}

+ (void)getPlaceListWithName:(NSString*) name venue:(NSString*) venueId callbackId:(NSString*) callbackId {
    [[MWZMapwizeApiFactory getApi] getVenueWithIdentifier:venueId success:^(MWZVenue *venue) {
        [[MWZMapwizeApiFactory getApi] getPlacelistWithName:name venue:venue success:^(MWZPlacelist *placeList) {
            NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
            NSString* json = [placeList toJSONString];
            dict[CBK_FIELD_ARG] = json;
            [ApiManager sendCommandDictCallback:dict callbackId:callbackId];
        } failure:^(NSError *error) {
            [ApiManager sendCommandCallbackFailed:callbackId];
        }];
    } failure:^(NSError *error) {
        [ApiManager sendCommandCallbackFailed:callbackId];
    }];
}

+ (void)getPlaceListWithAlias:(NSString*) alias venue:(NSString*) venueId callbackId:(NSString*) callbackId {
    [[MWZMapwizeApiFactory getApi] getVenueWithIdentifier:venueId success:^(MWZVenue *venue) {
        [[MWZMapwizeApiFactory getApi] getPlacelistWithAlias:alias venue:venue success:^(MWZPlacelist *placeList) {
            NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
            NSString* json = [placeList toJSONString];
            dict[CBK_FIELD_ARG] = json;
            [ApiManager sendCommandDictCallback:dict callbackId:callbackId];
        } failure:^(NSError *error) {
            [ApiManager sendCommandCallbackFailed:callbackId];
        }];
    } failure:^(NSError *error) {
        [ApiManager sendCommandCallbackFailed:callbackId];
    }];
}

+ (void)getPlaceListsWithFilter:(NSString*) filterStr callbackId:(NSString*) callbackId {
    MWZApiFilter* filter = [ApiManager deserializeApiFilter:filterStr];
    [[MWZMapwizeApiFactory getApi] getPlacelistsWithFilter:filter success:^(NSArray<MWZPlacelist *> *placeLists) {
        NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
        NSString* json = [ApiManager placeLists2JsonArray:placeLists];
        dict[CBK_FIELD_ARG] = json;
        [ApiManager sendCommandDictCallback:dict callbackId:callbackId];
    } failure:^(NSError *error) {
        [ApiManager sendCommandCallbackFailed:callbackId];
    }];
}

+ (void)getUniverseWithId:(NSString*) identifier callbackId:(NSString*) callbackId {
    [[MWZMapwizeApiFactory getApi] getUniverseWithIdentifier:identifier success:^(MWZUniverse *universe) {
        NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
        NSString* json = [universe toJSONString];
        dict[CBK_FIELD_ARG] = json;
        [ApiManager sendCommandDictCallback:dict callbackId:callbackId];
        
    } failure:^(NSError *error) {
        [ApiManager sendCommandCallbackFailed:callbackId];
    }];
}

+ (void)getUniversesWithFilter:(NSString*) filterStr callbackId:(NSString*) callbackId {
    MWZApiFilter* filter = [ApiManager deserializeApiFilter:filterStr];
    [[MWZMapwizeApiFactory getApi] getUniversesWithFilter:filter success:^(NSArray<MWZUniverse *> *universes) {
        NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
        NSString* json = [ApiManager universes2JsonArray:universes];
        dict[CBK_FIELD_ARG] = json;
        [ApiManager sendCommandDictCallback:dict callbackId:callbackId];
    } failure:^(NSError *error) {
        [ApiManager sendCommandCallbackFailed:callbackId];
    }];
}

+ (void)getAccessibleUniversesWithVenue:(NSString*) venueId callbackId:(NSString*) callbackId {
    [[MWZMapwizeApiFactory getApi] getVenueWithIdentifier:venueId success:^(MWZVenue *venue) {
        [[MWZMapwizeApiFactory getApi] getAccessibleUniversesWithVenue:venue success:^(NSArray<MWZUniverse *> *universes) {
                NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
                NSString* json = [ApiManager universes2JsonArray:universes];
                dict[CBK_FIELD_ARG] = json;
                [ApiManager sendCommandDictCallback:dict callbackId:callbackId];
            } failure:^(NSError *error) {
                [ApiManager sendCommandCallbackFailed:callbackId];
            }];
        } failure:^(NSError *error) {
            [ApiManager sendCommandCallbackFailed:callbackId];
        }];
}

+ (void)searchWithParams:(NSString*) searchParamsStr callbackId:(NSString*) callbackId {
    MWZSearchParams* searchParams = [MWZApiResponseParser parseSearchParams:searchParamsStr];
    [[MWZMapwizeApiFactory getApi] searchWithSearchParams:searchParams success:^(NSArray<id<MWZObject>> *searchResponse) {
        NSLog(@"Search succeeded...");
        NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
        NSString* json = [ApiManager objectList2JsonArray:searchResponse];
        dict[CBK_FIELD_ARG] = json;
        [ApiManager sendCommandDictCallback:dict callbackId:callbackId];
    } failure:^(NSError *error) {
        NSLog(@"Search failed...");
    }];
}

+ (void)getDirectionWithFrom:(NSString*) directionPointFromStr to:(NSString*) directionPointToStr isAccessible:(BOOL)isAccessible callbackId:(NSString*) callbackId {
    id<MWZDirectionPoint> from = [MWZApiResponseParser parseDirectionPoint:directionPointFromStr];
    id<MWZDirectionPoint> to = [MWZApiResponseParser parseDirectionPoint:directionPointToStr];
    [[MWZMapwizeApiFactory getApi] getDirectionWithFrom:from to:to isAccessible:isAccessible success:^(MWZDirection *direction) {
        NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
        NSString* json = [direction toJSONString];
        dict[CBK_FIELD_ARG] = json;
        [ApiManager sendCommandDictCallback:dict callbackId:callbackId];
    } failure:^(NSError *error) {
        [ApiManager sendCommandCallbackFailed:callbackId];
    }];
}

+ (void)getDirectionWithDirectionPointsFrom:(NSString*) directionPointFromStr to:(NSString*) directionPointsListToStr isAccessible:(BOOL)isAccessible callbackId:(NSString*) callbackId {
    id<MWZDirectionPoint> from = [MWZApiResponseParser parseDirectionPoint:directionPointFromStr];
    NSArray<id<MWZDirectionPoint>>* toList = [MWZApiResponseParser parseDirectionPoints:directionPointsListToStr];
    [[MWZMapwizeApiFactory getApi] getDirectionWithFrom:from tos:toList isAccessible:isAccessible success:^(MWZDirection *direction) {
        NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
        NSString* json = [direction toJSONString];
        dict[CBK_FIELD_ARG] = json;
        [ApiManager sendCommandDictCallback:dict callbackId:callbackId];
    } failure:^(NSError *error) {
        [ApiManager sendCommandCallbackFailed:callbackId];
    }];
}

+ (void)getDirectionWithWayPointsFrom:(NSString*) directionPointFromStr to:(NSString*) directionPointToStr waypointsList:(NSString*) wayPointsListToStr isAccessible:(BOOL)isAccessible callbackId:(NSString*) callbackId {
    id<MWZDirectionPoint> from = [MWZApiResponseParser parseDirectionPoint:directionPointFromStr];
    id<MWZDirectionPoint> to = [MWZApiResponseParser parseDirectionPoint:directionPointToStr];
    NSArray<id<MWZDirectionPoint>>* waypointsList = [MWZApiResponseParser parseDirectionPoints:wayPointsListToStr];
    [[MWZMapwizeApiFactory getApi] getDirectionWithFrom:from to:to waypoints:waypointsList isAccessible:isAccessible success:^(MWZDirection *direction) {
        NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
        NSString* json = [direction toJSONString];
        dict[CBK_FIELD_ARG] = json;
        [ApiManager sendCommandDictCallback:dict callbackId:callbackId];
    } failure:^(NSError *error) {
        [ApiManager sendCommandCallbackFailed:callbackId];
    }];
}

+ (void)getDirectionWithDirectionAndWayPointsFrom:(NSString*) directionPointFromStr tos:(NSString*) directionPointsListToStr waypointsList:(NSString*) wayPointsListToStr isAccessible:(BOOL)isAccessible callbackId:(NSString*) callbackId {
    id<MWZDirectionPoint> from = [MWZApiResponseParser parseDirectionPoint:directionPointFromStr];
    NSArray<id<MWZDirectionPoint>>* toList = [MWZApiResponseParser parseDirectionPoints:directionPointsListToStr];
    NSArray<id<MWZDirectionPoint>>* waypointsList = [MWZApiResponseParser parseDirectionPoints:wayPointsListToStr];
    [[MWZMapwizeApiFactory getApi] getDirectionWithFrom:from tos:toList waypoints:waypointsList isAccessible:isAccessible success:^(MWZDirection *direction) {
        NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
        NSString* json = [direction toJSONString];
        dict[CBK_FIELD_ARG] = json;
        [ApiManager sendCommandDictCallback:dict callbackId:callbackId];
    } failure:^(NSError *error) {
        [ApiManager sendCommandCallbackFailed:callbackId];
    }];
}

+ (void)getDistancesWithFrom:(NSString*) directionPointFromStr directionpointsToListStr:(NSString*) directionpointsToListStr bool1:(BOOL)bool1 bool2:(BOOL)bool2 callbackId:(NSString*) callbackId {
    NSLog(@"getDistancesWithFrom...");
    id<MWZDirectionPoint> from = [MWZApiResponseParser parseDirectionPoint:directionPointFromStr];
    NSArray<id<MWZDirectionPoint>>* toList = [MWZApiResponseParser parseDirectionPoints:directionpointsToListStr];
    [[MWZMapwizeApiFactory getApi] getDistancesWithFrom:from tos:toList isAccessible:bool1 sortByTravelTime:bool2 success:^(MWZDistanceResponse *distance) {
        NSLog(@"getDistancesWithFrom returned...");
        NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
        NSString* json = [distance toJSONString];
        dict[CBK_FIELD_ARG] = json;
        [ApiManager sendCommandDictCallback:dict callbackId:callbackId];
    } failure:^(NSError *error) {
        [ApiManager sendCommandCallbackFailed:callbackId];
    }];
}


+ (NSString*) objectList2JsonArray:(NSArray<id<MWZObject>> *) array {
    NSMutableArray* ma = [[NSMutableArray alloc] init];
    for(id<MWZObject> object in array) {
        NSString* vstr = [object toJSONString];
        [ma addObject:vstr];
    }
    NSString* csString = [ma componentsJoinedByString:@","];
    NSString* jsonString = [NSString stringWithFormat:@"[%@]", csString];
    return jsonString;
}

 + (NSString*) placeLists2JsonArray:(NSArray<MWZPlacelist*>*) array {
     NSMutableArray* ma = [[NSMutableArray alloc] init];
     for(MWZPlacelist* placeList in array) {
         NSString* vstr = [placeList toJSONString];
         [ma addObject:vstr];
     }
     NSString* csString = [ma componentsJoinedByString:@","];
     NSString* jsonString = [NSString stringWithFormat:@"[%@]", csString];
     return jsonString;
 }

     
+ (NSString*) venues2JsonArray:(NSArray<MWZVenue*>*) array {
    NSMutableArray* ma = [[NSMutableArray alloc] init];
    for(MWZVenue* venue in array) {
        NSString* vstr = [venue toJSONString];
        [ma addObject:vstr];
    }
    NSString* csString = [ma componentsJoinedByString:@","];
    NSString* jsonString = [NSString stringWithFormat:@"[%@]", csString];
    return jsonString;
}

+ (NSString*) universes2JsonArray:(NSArray<MWZUniverse*>*) array {
    NSMutableArray* ma = [[NSMutableArray alloc] init];
    for(MWZUniverse* venue in array) {
        NSString* vstr = [venue toJSONString];
        [ma addObject:vstr];
    }
    NSString* csString = [ma componentsJoinedByString:@","];
    NSString* jsonString = [NSString stringWithFormat:@"[%@]", csString];
    return jsonString;
}

- (NSString*) places2JsonArray:(NSArray<MWZVenue*>*) array {
    NSMutableArray* ma = [[NSMutableArray alloc] init];
    for(MWZVenue* venue in array) {
        NSString* vstr = [venue toJSONString];
        [ma addObject:vstr];
    }
    NSString* csString = [ma componentsJoinedByString:@","];
    NSString* jsonString = [NSString stringWithFormat:@"[%@]", csString];
    return jsonString;
}

 + (void) sendCommandDictCallback:(NSMutableDictionary*)dict callbackId:(NSString*)callbackId {
     NSLog(@"ApiManager::sendCommandDictCallback...");
     
     
     dispatch_async(dispatch_get_main_queue(), ^{
           NSError *error;
//           NSData *jsonData = [NSJSONSerialization dataWithJSONObject:dict
//                                                              options:NSJSONWritingPrettyPrinted // Pass 0 if you don't care about the readability of the generated string
//                                                                error:&error];
//           NSString *jsonString;
//           if (! jsonData) {
//               NSLog(@"Got an error: %@", error);
//           } else {
//               jsonString = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
//           }
           
//           NSLog(@"ApiManager::sendCommandDictCallback...jsonString: %@", jsonString);
//           CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK
//                                                         messageAsString:jsonString];
         
           CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK
           messageAsDictionary:dict];
         
      //     [pluginResult setKeepCallback: [NSNumber numberWithBool:NO]];
           NSLog(@"ApiManager::sendCommandDictCallback...callbackId: %@", callbackId);
           [plugin.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
          
          
    //         NSString* payload = nil;
    //         // Some blocking logic...
    //         CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:payload];
    //         // The sendPluginResult method is thread-safe.
    //         [plugin.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
     });
         
     
 }

+ (void) sendCommandCallbackOK:(NSString*)callbackId  {
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [pluginResult setKeepCallbackAsBool:NO];
    [plugin.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
}

+ (void) sendCommandCallback:(NSDictionary*)args callbackId:(NSString*)callbackId {
    NSMutableDictionary *dict = [NSMutableDictionary dictionary];
    NSError *err;
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:args options:NSJSONWritingPrettyPrinted error:&err];
    NSString* argStr = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
    dict[CBK_FIELD_ARG] = argStr;
    NSLog(@"ApiManager::sendCommandCallback...callbackId: %@", callbackId);
    [self sendCommandDictCallback:dict callbackId:callbackId];
}

+ (void) sendCommandCallbackFailed:(NSString*)callbackId  {
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
    [pluginResult setKeepCallbackAsBool:NO];
    [plugin.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
}

+ (MWZApiFilter*) deserializeApiFilter:(NSString*)filterStr  {
    MWZApiFilter* filter = [MWZApiResponseParser parseApiFilter:filterStr];
    return filter;
}

+ (MWZSearchParams*) deserializeSearchParams:(NSString*)paramsStr  {
    MWZSearchParams* searchParams = [MWZApiResponseParser parseSearchParams:paramsStr];
    return searchParams;
}

@end
     
