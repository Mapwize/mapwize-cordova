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

  mapwiseView: any;

  constructor(public navCtrl: NavController) {

  }

  createClicked() {
  	console.log("createClicked...");
  	this.mapwiseView = Mapwize.createMapwizeView(
      {
        floor: 0,
        language: "en",
        universeId: "",
        restrictContentToVenueId: "",
        restrictContentToOrganizationId: "",
        centerOnVenueId: "56b20714c3fa800b00d8f0b5",
        centerOnPlaceId: "5bc49413bf0ed600114db212"
      });
    this.setCallbackClicked();

    this.mapwiseView.selectPlace(
        "5bc49413bf0ed600114db214", "false", (arg) => {
          console.log("place selected successfully");
        },
        (err) => {
          console.log("place selection failed: " + JSON.stringify(err));
        }
      );
    this.mapwiseView.selectPlace(
        "5bc49413bf0ed600114db21c", "false", (arg) => {
          console.log("place selected successfully");
        },
        (err) => {
          console.log("place selection failed: " + JSON.stringify(err));
        }
      );
  }

  selectPlaceClicked() {
    console.log("selectPlaceClicked...");
    this.mapwiseView.selectPlace(
        "5bc49413bf0ed600114db212", true
      );
  } 

  setCallbackClicked() {
    console.log("setCallbackClicked...");
    this.mapwiseView.setCallback(
        {
          
          DidLoad: function(arg) {
            console.log("The cordova result(DidLoad): " + JSON.stringify(arg));
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
          TapOnPlaceInformationButton: function(arg) {
            console.log("The cordova result: " + JSON.stringify(arg));
          },
          TapOnPlacesInformationButton: function(arg) {
            console.log("The cordova result(TapOnPlacesInformationButton): " + JSON.stringify(arg));
          }
        }
      );
  } 

}
