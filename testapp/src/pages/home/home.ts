import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';

declare var Mapwize : any;

@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage {
	url: string;
  token: string;
	syncPeriodInSec: number;

  mapwizeView: any;

  constructor(public navCtrl: NavController) {

  }

  createClicked() {
  	console.log("createClicked...");
  	this.mapwizeView = Mapwize.createMapwizeView(
      {
        floor: 0,
        language: "en",
        universeId: "",
        restrictContentToVenueId: "",
        restrictContentToOrganizationId: "",
        centerOnVenueId: "56b20714c3fa800b00d8f0b5",
        centerOnPlaceId: "5bc49413bf0ed600114db212"
      }, () => {
        console.log("createMapwizeView success...");
      }, (err) => {
        console.log("createMapwizeView failed, err: " + JSON.stringify(err));

      });
    this.setCallbackClicked();
  }

  selectPlaceClicked() {
    console.log("selectPlaceClicked...");
    this.mapwizeView.selectPlace(
        "5bc49413bf0ed600114db212", true, 
        (res) => {console.log("Select place successfully returned: " + JSON.stringify(res))},
        (err) => {console.log("Select place failed err: " + JSON.stringify(err))}
      );
  }

  selectPlaceListClicked() {
    console.log("selectPlaceListClicked...");
    this.mapwizeView.selectPlaceList(
        "5784fc5f7f2a900b0055f603", 
        (res) => {console.log("Select places successfully returned: " + JSON.stringify(res))},
        (err) => {console.log("Select places failed err: " + JSON.stringify(err))}
      );
  } 

  unselectClicked() {
    console.log("unselectClicked...");
    this.mapwizeView.unselectContent(
        true,
        (res) => {console.log("unselectContent successfully returned: " + JSON.stringify(res))},
        (err) => {console.log("unselectContent failed err: " + JSON.stringify(err))}
      );
  } 

  setCallbackClicked() {
    console.log("setCallbackClicked...");
    var dis = this;
    this.mapwizeView.setCallback(
        {
          
          DidLoad: function(arg) {
            console.log("The cordova result(DidLoad): " + JSON.stringify(arg));
            dis.selectPlaceListClicked();   // Wrong
          },
          DidTapOnFollowWithoutLocation: function(arg) {
            console.log("The cordova result(DidTapOnFollowWithoutLocation): " + JSON.stringify(arg));
          },
          DidTapOnMenu: function(arg) {
            console.log("The cordova result(DidTapOnMenu): " + JSON.stringify(arg));
          },
          shouldShowInformationButtonFor: function(arg) {
            console.log("The cordova result(shouldShowInformationButtonFor): " + JSON.stringify(arg));
          },
          TapOnPlaceInformationButton: function(place) {
            console.log("The cordova result: " + JSON.stringify(place));
            console.log("The cordova result: " + place._id);

          },
          TapOnPlacesInformationButton: function(arg) {
            console.log("The cordova result(TapOnPlacesInformationButton): " + JSON.stringify(arg));
          }
        }
      );
  } 

}
