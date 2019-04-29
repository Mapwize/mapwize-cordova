//
//  ApiManager.h
//  Mapwize Tester
//
//  Created by Laszlo Blum on 2019. 04. 23..
//

#import <Foundation/Foundation.h>
#import "MapwizeUI.h"
#import "Mapwize.h"

NS_ASSUME_NONNULL_BEGIN

@interface ApiManager : NSObject
+ (void) initManager:(Mapwize*) mapwizePlugin;

+ (void)getVenueWithId:(NSString*) identifier callbackId:(NSString*) callbackId;
+ (void)getVenuesWithFilter:(NSString*) filterStr callbackId:(NSString*) callbackId;
+ (void)getVenueWithName:(NSString*) name callbackId:(NSString*) callbackId;
+ (void)getVenueWithAlias:(NSString*) alias callbackId:(NSString*) callbackId;
+ (void)getPlaceWithId:(NSString*) identifier callbackId:(NSString*) callbackId;
+ (void)getPlaceWithName:(NSString*) name venue:(NSString*) venueId callbackId:(NSString*) callbackId;
+ (void)getPlaceWithAlias:(NSString*) alias venue:(NSString*) venueId callbackId:(NSString*) callbackId;
+ (void)getPlacesWithFilter:(NSString*) filterStr callbackId:(NSString*) callbackId;
+ (void)getPlaceListWithId:(NSString*) identifier callbackId:(NSString*) callbackId;
+ (void)getPlaceListWithName:(NSString*) name venue:(NSString*) venueId callbackId:(NSString*) callbackId;
+ (void)getPlaceListWithAlias:(NSString*) alias venue:(NSString*) venueId callbackId:(NSString*) callbackId;
+ (void)getPlaceListsWithFilter:(NSString*) filterStr callbackId:(NSString*) callbackId;
+ (void)getUniverseWithId:(NSString*) identifier callbackId:(NSString*) callbackId;
+ (void)getUniversesWithFilter:(NSString*) filterStr callbackId:(NSString*) callbackId;
+ (void)getAccessibleUniversesWithVenue:(NSString*) venueId callbackId:(NSString*) callbackId;
+ (void)searchWithParams:(NSString*) searchParamsStr callbackId:(NSString*) callbackId;


+ (void)getDirectionWithFrom:(NSString*) directionPointFromStr to:(NSString*) directionPointToStr isAccessible:(BOOL)isAccessible callbackId:(NSString*) callbackId;
+ (void)getDirectionWithDirectionPointsFrom:(NSString*) directionPointFromStr to:(NSString*) directionPointsListToStr isAccessible:(BOOL)isAccessible callbackId:(NSString*) callbackId;
+ (void)getDirectionWithWayPointsFrom:(NSString*) directionPointFromStr to:(NSString*) directionPointToStr waypointsList:(NSString*) wayPointsListToStr bool1:(BOOL)bool1 bool2:(BOOL)bool2 callbackId:(NSString*) callbackId;
+ (void)getDirectionWithDirectionAndWayPointsFrom:(NSString*) directionPointFromStr tos:(NSString*) directionPointsListToStr waypointsList:(NSString*) wayPointsListToStr bool1:(BOOL)bool1 bool2:(BOOL)bool2 callbackId:(NSString*) callbackId;
+ (void)getDistanceWithFrom:(NSString*) directionPointFromStr directionpointsToListStr:(NSString*) directionpointsToListStr bool1:(BOOL)bool1 bool2:(BOOL)bool2 callbackId:(NSString*) callbackId;

@end

NS_ASSUME_NONNULL_END
