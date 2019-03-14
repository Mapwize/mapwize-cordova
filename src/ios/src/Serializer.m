//
//  Serializer.m
//  Mapwize Tester
//
//  Created by Laszlo Blum on 2019. 03. 14..
//

#import <Foundation/Foundation.h>
#import "Serializer.h"
#import <MapwizeUI/MapwizeUI.h>

@implementation Serializer
+ (NSString*) serializePlace:(MWZPlace *)place {
    NSLog(@"serializePlace");
    NSDictionary *dict = @{ @"identifier" : place.identifier, @"name" : place.name};
    return [self dict2JsonString:dict];
}

+ (NSString*) serializePlaces:(MWZPlaceList *)placeList {
    NSLog(@"serializePlaces");
    NSArray<NSString*>* placeIds = placeList.placeIds;
    NSData *json = [NSJSONSerialization dataWithJSONObject:placeIds options:0 error:nil];
    
    return [[NSString alloc] initWithData:json encoding:NSUTF8StringEncoding];
}


+ (NSString*) dict2JsonString:(NSDictionary*)dict {
    NSLog(@"converting json...");
    NSError *jsonError;
    NSData *json = [NSJSONSerialization dataWithJSONObject:dict options:0 error:&jsonError];
    
    if (! json) {
        NSLog(@"%s: error: %@", __func__, jsonError.localizedDescription);
        return @"{}";
    } else {
        return [[NSString alloc] initWithData:json encoding:NSUTF8StringEncoding];
    }
}
@end
