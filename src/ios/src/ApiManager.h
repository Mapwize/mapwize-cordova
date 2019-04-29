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

//+ (void)getDirectionWithFrom:(id<MWZDirectionPoint>) from to:(id<MWZDirectionPoint>) to isAccessible:(BOOL) isAccessible callbackId:(NSString*) callbackId;
//+ (void)getDirectionWithFrom:(id<MWZDirectionPoint>) from tos:(NSArray<id<MWZDirectionPoint>>*) tos isAccessible:(BOOL) isAccessible callbackId:(NSString*) callbackId;
//+ (void)getDirectionWithFrom:(id<MWZDirectionPoint>) from to:(id<MWZDirectionPoint>) to waypoints:(NSArray<id<MWZDirectionPoint>>*) waypoints isAccessible:(BOOL) isAccessible callbackId:(NSString*) callbackId;
//+ (void)getDirectionWithFrom:(id<MWZDirectionPoint>) from tos:(NSArray<id<MWZDirectionPoint>>*) tos waypoints:(NSArray<id<MWZDirectionPoint>>*) waypoints isAccessible:(BOOL) isAccessible callbackId:(NSString*) callbackId;
//+ (void)getDistanceWithFrom:(id<MWZDirectionPoint>) from tos:(NSArray<id<MWZDirectionPoint>>*) tos isAccessible:(BOOL) isAccessible sortByTravelTime:(BOOL) sort callbackId:(NSString*) callbackId;

@end

NS_ASSUME_NONNULL_END
