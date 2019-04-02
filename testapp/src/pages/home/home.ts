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
      }, (result) => {
        console.log("result: " + JSON.stringify(result));
        // this.selectPlaceListClicked();   // Wrong
        this.selectPlaceClicked(); // Correct

      }, (err) => {
        console.log("err: " + JSON.stringify(err));

      });
    this.setCallbackClicked();


  }

  selectPlaceClicked() {
    console.log("selectPlaceClicked...");
    this.mapwiseView.selectPlace(
        "5bc49413bf0ed600114db212", true, 
        (res) => {console.log("Select place successfully returned: " + JSON.stringify(res))},
        (err) => {console.log("Select place failed err: " + JSON.stringify(err))}
      );
  }

  selectPlaceListClicked() {
    console.log("selectPlaceListClicked...");
    this.mapwiseView.selectPlaceList(
        "5784fc5f7f2a900b0055f603", 
        (res) => {console.log("Select places successfully returned: " + JSON.stringify(res))},
        (err) => {console.log("Select places failed err: " + JSON.stringify(err))}
      );
  } 

  unselectClicked() {
    console.log("unselectClicked...");
    this.mapwiseView.unselectContent(
        true,
        (res) => {console.log("unselectContent successfully returned: " + JSON.stringify(res))},
        (err) => {console.log("unselectContent failed err: " + JSON.stringify(err))}
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
