//
//  Constants.m
//  Mapwize Tester
//
//  Created by Laszlo Blum on 2019. 03. 14..
//

#import <Foundation/Foundation.h>


NSString *const CBK_FIELD_EVENT = @"event";
NSString *const CBK_FIELD_ARG = @"arg";

NSString *const CBK_EVENT_DID_LOAD = @"DidLoad";
NSString *const CBK_EVENT_DID_TAP_ON_FOLLOW_WITHOUT_LOCATION = @"DidTapOnFollowWithoutLocation";
NSString *const CBK_EVENT_DID_TAP_ON_MENU = @"DidTapOnMenu";
NSString *const CBK_EVENT_SHOULD_SHOW_INFORMATION_BUTTON_FOR = @"shouldShowInformationButtonFor";
NSString *const CBK_EVENT_TAP_ON_PLACE_INFORMATION_BUTTON = @"TapOnPlaceInformationButton";
NSString *const CBK_EVENT_TAP_ON_PLACES_INFORMATION_BUTTON = @"TapOnPlacesInformationButton";

NSString *const CBK_FIELD_ERR_MESSAGE = @"message";
NSString *const CBK_FIELD_ERR_LOCALIZED_MESSAGE = @"localizedMessage";

NSString *const CBK_SELECT_PLACE = @"selectPlaceCbk";
NSString *const CBK_SELECT_PLACE_ID = @"identifier";
NSString *const CBK_SELECT_PLACE_CENTERON = @"centerOn";

NSString *const CBK_SELECT_PLACELIST = @"selectPlaceListCbk";
NSString *const CBK_SELECT_PLACELIST_ID = @"identifier";

NSString *const CBK_GRANT_ACCESS = @"grantAccessCbk";
NSString *const CBK_GRANT_ACCESS_TOKEN = @"token";
NSString *const CBK_GRANT_ACCESS_SUCCESS = @"success";

NSString *const CBK_UNSELECT_CONTENT = @"unselectContentCbk";
