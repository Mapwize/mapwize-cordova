//
//  Serializer.h
//  Mapwize Tester
//
//  Created by Laszlo Blum on 2019. 03. 14..
//

#ifndef Serializer_h
#define Serializer_h
@class MWZPlace;
@class MWZPlaceList;


@interface Serializer: NSObject
+ (NSString*) serializePlace:(MWZPlace *)place;
+ (NSString*) serializePlaces:(MWZPlaceList *)placeList;



@end

#endif /* Serializer_h */
