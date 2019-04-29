//
//  ApiManager.m
//  Mapwize Tester
//
//  Created by Laszlo Blum on 2019. 04. 23..
//

#import "ApiManager.h"
#import "MapwizeUI.h"
#import "Constants.h"


@interface ApiManager ()
+ (NSString*) venues2JsonArray:(NSArray<MWZVenue*>*) array;
+ (void) sendCommandDictCallback:(NSMutableDictionary*)dict callbackId:(NSString*)callbackId;
+ (void) sendCommandCallback:(NSDictionary*)args callbackId:(NSString*)callbackId;

+ (void) sendCommandCallbackOK:(NSString*) callbackId;
+ (void) sendCommandCallbackFailed:(NSString*)callbackId;
+ (MWZApiFilter*) deserializeApiFilter:(NSString*)filterStr;
+ (NSString*) placeLists2JsonArray:(NSArray<MWZPlaceList*>*) array;
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
    NSURLSessionDataTask* task = [MWZApi getVenueWithId:identifier success:^(MWZVenue *venue) {
        NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
        NSString* json = [venue toJSONString];
        dict[CBK_FIELD_ARG] = json;
        [ApiManager sendCommandDictCallback:dict callbackId:callbackId];
    } failure:^(NSError *error) {
        [ApiManager sendCommandCallbackFailed:callbackId];
    }];
    [task resume];
}

+ (void)getVenuesWithFilter:(NSString*) filterStr callbackId:(NSString*) callbackId {
    MWZApiFilter* filter = [self deserializeApiFilter:filterStr];
    NSURLSessionDataTask* task = [MWZApi getVenuesWithFilter:filter success:^(NSArray<MWZVenue *> *venues) {
        NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
        NSString* json = [ApiManager venues2JsonArray:venues];
        dict[CBK_FIELD_ARG] = json;
        [ApiManager sendCommandDictCallback:dict callbackId:callbackId];
    } failure:^(NSError *error) {
        [ApiManager sendCommandCallbackFailed:callbackId];
    }];
    [task resume];
}

 + (void)getVenueWithName:(NSString*) name callbackId:(NSString*) callbackId {
     NSURLSessionDataTask* task = [MWZApi getVenueWithName:name success:^(MWZVenue *venue) {
         NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
         NSString* json = [venue toJSONString];
         dict[CBK_FIELD_ARG] = json;
         [ApiManager sendCommandDictCallback:dict callbackId:callbackId];
     } failure:^(NSError *error) {
         [ApiManager sendCommandCallbackFailed:callbackId];
     }];
     [task resume];
 }

+ (void)getVenueWithAlias:(NSString*) alias callbackId:(NSString*) callbackId {
    NSURLSessionDataTask* task = [MWZApi getVenueWithAlias:alias success:^(MWZVenue *venue) {
        NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
        NSString* json = [venue toJSONString];
        dict[CBK_FIELD_ARG] = json;
        [ApiManager sendCommandDictCallback:dict callbackId:callbackId];
    } failure:^(NSError *error) {
        [ApiManager sendCommandCallbackFailed:callbackId];
    }];
    [task resume];
}

+ (void)getPlaceWithId:(NSString*) identifier callbackId:(NSString*) callbackId {
    NSURLSessionDataTask* task = [MWZApi getPlaceWithId:identifier success:^(MWZPlace *place) {
        NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
        NSString* json = [place toJSONString];
        dict[CBK_FIELD_ARG] = json;
        [ApiManager sendCommandDictCallback:dict callbackId:callbackId];
    } failure:^(NSError *error) {
        [ApiManager sendCommandCallbackFailed:callbackId];
    }];
    [task resume];
}

+ (void)getPlaceWithName:(NSString*) name venue:(NSString*) venueId callbackId:(NSString*) callbackId {
    NSURLSessionDataTask* task = [MWZApi getVenueWithId:venueId success:^(MWZVenue *venue) {
        NSURLSessionDataTask* subtask = [MWZApi getPlacesWithName:name venue:venue success:^(MWZPlace *place) {
            NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
            NSString* json = [venue toJSONString];
            dict[CBK_FIELD_ARG] = json;
            [ApiManager sendCommandDictCallback:dict callbackId:callbackId];
        } failure:^(NSError *error) {
            [ApiManager sendCommandCallbackFailed:callbackId];
        }];
        [subtask resume];
    } failure:^(NSError *error) {
        [ApiManager sendCommandCallbackFailed:callbackId];
    }];
    [task resume];
}

+ (void)getPlaceWithAlias:(NSString*) alias venue:(NSString*) venueId callbackId:(NSString*) callbackId {
    NSLog(@"getPlaceWithAlias...");
    NSURLSessionDataTask* task = [MWZApi getVenueWithId:venueId success:^(MWZVenue *venue) {
        NSLog(@"getPlaceWithAlias, venue received...");
        NSURLSessionDataTask* subtask = [MWZApi getPlacesWithAlias:alias venue:venue success:^(MWZPlace *place) {
            NSLog(@"getPlaceWithAlias, getPlacesWithAlias received...");
            NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
            NSString* json = [venue toJSONString];
            dict[CBK_FIELD_ARG] = json;
            [ApiManager sendCommandDictCallback:dict callbackId:callbackId];
        } failure:^(NSError *error) {
            [ApiManager sendCommandCallbackFailed:callbackId];
        }];
        [subtask resume];
    } failure:^(NSError *error) {
        [ApiManager sendCommandCallbackFailed:callbackId];
    }];
    [task resume];
}

+ (void)getPlacesWithFilter:(NSString*) filterStr callbackId:(NSString*) callbackId {
    MWZApiFilter* filter = [ApiManager deserializeApiFilter:filterStr];
    NSURLSessionDataTask* task = [MWZApi getVenuesWithFilter:filter success:^(NSArray<MWZVenue *> *venues) {
        NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
        NSString* json = [ApiManager venues2JsonArray:venues];
        dict[CBK_FIELD_ARG] = json;
        [ApiManager sendCommandDictCallback:dict callbackId:callbackId];
    } failure:^(NSError *error) {
        [ApiManager sendCommandCallbackFailed:callbackId];
    }];
    [task resume];
}

+ (void)getPlaceListWithId:(NSString*) identifier callbackId:(NSString*) callbackId {
    NSURLSessionDataTask* task = [MWZApi getPlaceListWithId:identifier success:^(MWZPlaceList *placeList) {
        NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
        NSString* json = [placeList toJSONString];
        dict[CBK_FIELD_ARG] = json;
        [ApiManager sendCommandDictCallback:dict callbackId:callbackId];
    } failure:^(NSError *error) {
        [ApiManager sendCommandCallbackFailed:callbackId];
    }];
    [task resume];
    
}

+ (void)getPlaceListWithName:(NSString*) name venue:(NSString*) venueId callbackId:(NSString*) callbackId {
    NSURLSessionDataTask* task = [MWZApi getVenueWithId:venueId success:^(MWZVenue *venue) {
        NSURLSessionDataTask* subtask = [MWZApi getPlaceListsWithName:name venue:venue success:^(MWZPlaceList *placeList) {
            NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
            NSString* json = [placeList toJSONString];
            dict[CBK_FIELD_ARG] = json;
            [ApiManager sendCommandDictCallback:dict callbackId:callbackId];
        } failure:^(NSError *error) {
            [ApiManager sendCommandCallbackFailed:callbackId];
        }];
        [subtask resume];
    } failure:^(NSError *error) {
        [ApiManager sendCommandCallbackFailed:callbackId];
    }];
    
    [task resume];
}

+ (void)getPlaceListWithAlias:(NSString*) alias venue:(NSString*) venueId callbackId:(NSString*) callbackId {
    NSURLSessionDataTask* task = [MWZApi getVenueWithId:venueId success:^(MWZVenue *venue) {
        NSURLSessionDataTask* subtask = [MWZApi getPlaceListsWithAlias:alias venue:venue success:^(MWZPlaceList *placeList) {
            NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
            NSString* json = [placeList toJSONString];
            dict[CBK_FIELD_ARG] = json;
            [ApiManager sendCommandDictCallback:dict callbackId:callbackId];
        } failure:^(NSError *error) {
            [ApiManager sendCommandCallbackFailed:callbackId];
        }];
        [subtask resume];
    } failure:^(NSError *error) {
        [ApiManager sendCommandCallbackFailed:callbackId];
    }];
    [task resume];
}

+ (void)getPlaceListsWithFilter:(NSString*) filterStr callbackId:(NSString*) callbackId {
    MWZApiFilter* filter = [ApiManager deserializeApiFilter:filterStr];
    NSURLSessionDataTask* task = [MWZApi getPlaceListsWithFilter:filter success:^(NSArray<MWZPlaceList *> *placeLists) {
        NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
        NSString* json = [ApiManager placeLists2JsonArray:placeLists];
        dict[CBK_FIELD_ARG] = json;
        [ApiManager sendCommandDictCallback:dict callbackId:callbackId];
    } failure:^(NSError *error) {
        [ApiManager sendCommandCallbackFailed:callbackId];
    }];
    [task resume];
}

+ (void)getUniverseWithId:(NSString*) identifier callbackId:(NSString*) callbackId {
    NSURLSessionDataTask* task = [MWZApi getUniverseWithId:identifier success:^(MWZUniverse *universe) {
        NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
        NSString* json = [universe toJSONString];
        dict[CBK_FIELD_ARG] = json;
        [ApiManager sendCommandDictCallback:dict callbackId:callbackId];
        
    } failure:^(NSError *error) {
        [ApiManager sendCommandCallbackFailed:callbackId];
    }];
    [task resume];
}

+ (void)getUniversesWithFilter:(NSString*) filterStr callbackId:(NSString*) callbackId {
    MWZApiFilter* filter = [ApiManager deserializeApiFilter:filterStr];
    NSURLSessionDataTask* task = [MWZApi getUniversesWithFilter:filter success:^(NSArray<MWZUniverse *> *universes) {
        NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
        NSString* json = [ApiManager universes2JsonArray:universes];
        dict[CBK_FIELD_ARG] = json;
        [ApiManager sendCommandDictCallback:dict callbackId:callbackId];
    } failure:^(NSError *error) {
        [ApiManager sendCommandCallbackFailed:callbackId];
    }];
    [task resume];
}

+ (void)getAccessibleUniversesWithVenue:(NSString*) venueId callbackId:(NSString*) callbackId {
    NSURLSessionDataTask* task = [MWZApi getVenueWithId:venueId success:^(MWZVenue *venue) {
        NSURLSessionDataTask* subtask = [MWZApi getAccessibleUniversesWithVenue:venue success:^(NSArray<MWZUniverse *> *universes) {
                NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
                NSString* json = [ApiManager universes2JsonArray:universes];
                dict[CBK_FIELD_ARG] = json;
                [ApiManager sendCommandDictCallback:dict callbackId:callbackId];
            } failure:^(NSError *error) {
                [ApiManager sendCommandCallbackFailed:callbackId];
            }];
            [subtask resume];
        } failure:^(NSError *error) {
            [ApiManager sendCommandCallbackFailed:callbackId];
        }];
    [task resume];
}

+ (void)searchWithParams:(NSString*) searchParamsStr callbackId:(NSString*) callbackId {
    MWZSearchParams* searchParams = [MWZApiResponseParser parseSearchParams:searchParamsStr];
    [MWZApi searchWithParams:searchParams success:^(NSArray<id<MWZObject>> *searchResponse) {
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
    [MWZApi getDirectionWithFrom:from to:to isAccessible:isAccessible success:^(MWZDirection *direction) {
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
    [MWZApi getDirectionWithFrom:from tos:toList isAccessible:isAccessible success:^(MWZDirection *direction) {
        NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
        NSString* json = [direction toJSONString];
        dict[CBK_FIELD_ARG] = json;
        [ApiManager sendCommandDictCallback:dict callbackId:callbackId];
    } failure:^(NSError *error) {
        [ApiManager sendCommandCallbackFailed:callbackId];
    }];
}

+ (void)getDirectionWithWayPointsFrom:(NSString*) directionPointFromStr to:(NSString*) directionPointToStr waypointsList:(NSString*) wayPointsListToStr bool1:(BOOL)bool1 bool2:(BOOL)bool2 callbackId:(NSString*) callbackId {
    id<MWZDirectionPoint> from = [MWZApiResponseParser parseDirectionPoint:directionPointFromStr];
    id<MWZDirectionPoint> to = [MWZApiResponseParser parseDirectionPoint:directionPointToStr];
    NSArray<id<MWZDirectionPoint>>* waypointsList = [MWZApiResponseParser parseDirectionPoints:wayPointsListToStr];
    [MWZApi getDirectionWithFrom:from to:to waypoints:waypointsList isAccessible:bool1 success:^(MWZDirection *direction) {
        NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
        NSString* json = [direction toJSONString];
        dict[CBK_FIELD_ARG] = json;
        [ApiManager sendCommandDictCallback:dict callbackId:callbackId];
    } failure:^(NSError *error) {
        [ApiManager sendCommandCallbackFailed:callbackId];
    }];
}

+ (void)getDirectionWithDirectionAndWayPointsFrom:(NSString*) directionPointFromStr tos:(NSString*) directionPointsListToStr waypointsList:(NSString*) wayPointsListToStr bool1:(BOOL)bool1 bool2:(BOOL)bool2 callbackId:(NSString*) callbackId {
    id<MWZDirectionPoint> from = [MWZApiResponseParser parseDirectionPoint:directionPointFromStr];
    NSArray<id<MWZDirectionPoint>>* toList = [MWZApiResponseParser parseDirectionPoints:directionPointsListToStr];
    NSArray<id<MWZDirectionPoint>>* waypointsList = [MWZApiResponseParser parseDirectionPoints:wayPointsListToStr];
    [MWZApi getDirectionWithFrom:from tos:toList waypoints:waypointsList isAccessible:bool1 success:^(MWZDirection *direction) {
        NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
        NSString* json = [direction toJSONString];
        dict[CBK_FIELD_ARG] = json;
        [ApiManager sendCommandDictCallback:dict callbackId:callbackId];
    } failure:^(NSError *error) {
        [ApiManager sendCommandCallbackFailed:callbackId];
    }];
}

+ (void)getDistanceWithFrom:(NSString*) directionPointFromStr directionpointsToListStr:(NSString*) directionpointsToListStr bool1:(BOOL)bool1 bool2:(BOOL)bool2 callbackId:(NSString*) callbackId {
    NSLog(@"getDistanceWithFrom...");
    id<MWZDirectionPoint> from = [MWZApiResponseParser parseDirectionPoint:directionPointFromStr];
    NSArray<id<MWZDirectionPoint>>* toList = [MWZApiResponseParser parseDirectionPoints:directionpointsToListStr];
    [MWZApi getDistanceWithFrom:from tos:toList isAccessible:bool1 sortByTravelTime:bool2 success:^(MWZDistanceResponse *distance) {
        NSLog(@"getDistanceWithFrom returned...");
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

 + (NSString*) placeLists2JsonArray:(NSArray<MWZPlaceList*>*) array {
     NSMutableArray* ma = [[NSMutableArray alloc] init];
     for(MWZPlaceList* placeList in array) {
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
     CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK
                                                   messageAsDictionary:dict];
     [plugin.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
 }

+ (void) sendCommandCallbackOK:(NSString*)callbackId  {
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [plugin.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
}

+ (void) sendCommandCallback:(NSDictionary*)args callbackId:(NSString*)callbackId {
    NSMutableDictionary *dict = [NSMutableDictionary dictionary];
    NSError *err;
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:args options:NSJSONWritingPrettyPrinted error:&err];
    NSString* argStr = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
    dict[CBK_FIELD_ARG] = argStr;
    [self sendCommandDictCallback:dict callbackId:callbackId];
}

+ (void) sendCommandCallbackFailed:(NSString*)callbackId  {
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
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
     
