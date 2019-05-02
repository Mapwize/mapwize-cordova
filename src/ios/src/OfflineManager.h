//
//  OfflineManager.h
//  Mapwize Tester
//
//  Created by Laszlo Blum on 2019. 04. 22..
//

#import <Foundation/Foundation.h>
#import "MapwizeUI.h"
#import "Mapwize.h"

NS_ASSUME_NONNULL_BEGIN

@interface OfflineManager : NSObject
//- (void) initManager:(MGLMapView*) mapView plugin:(Mapwize*) plugin;
- (void) initManager:(Mapwize*) plugin styleURL:(NSString*) styleURL;
- (void) removeDataForVenue:(NSString*) venueId universe:(NSString*) universeId callbackId:(NSString*) callbackId;
- (void) downloadDataForVenue:(NSString*) venueId
                     universe:(NSString*) universeId
                   callbackId:(NSString*) callbackId;
- (void) isOfflineForVenue:(NSString*) venueId universe:(NSString*) universeId callbackId:(NSString*) callbackId;
- (void) getOfflineVenues:(NSString*) callbackId;
- (void) getOfflineUniversesForVenue:(NSString*) venueId callbackId:(NSString*) callbackId;

@end

NS_ASSUME_NONNULL_END
