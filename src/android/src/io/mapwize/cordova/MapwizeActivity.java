package io.mapwize.cordova;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.mapbox.mapboxsdk.maps.MapboxMap;

import io.indoorlocation.core.IndoorLocation;
import io.indoorlocation.manual.ManualIndoorLocationProvider;
import io.mapwize.mapwizecomponents.ui.MapwizeFragment;
import io.mapwize.mapwizecomponents.ui.MapwizeFragmentUISettings;
import io.mapwize.mapwizeformapbox.api.Api;
import io.mapwize.mapwizeformapbox.api.ApiCallback;
import io.mapwize.mapwizeformapbox.api.MapwizeObject;
import io.mapwize.mapwizeformapbox.api.Place;
import io.mapwize.mapwizeformapbox.api.PlaceList;
import io.mapwize.mapwizeformapbox.map.ClickEvent;
import io.mapwize.mapwizeformapbox.map.MapOptions;
import io.mapwize.mapwizeformapbox.map.MapwizePlugin;

import org.json.JSONException;
import org.json.JSONObject;
import org.mapwize.test.R;

import java.util.List;

import static io.mapwize.cordova.MapwizeCordovaPlugin.CBK_ARGS;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CBK_EVENT_DID_LOAD;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CBK_EVENT_DID_TAP_ON_FOLLOW_WITHOUT_LOCATION;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CBK_EVENT_TAP_ON_PLACES_INFORMATION_BUTTON;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CBK_EVENT_TAP_ON_PLACE_INFORMATION_BUTTON;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CBK_FIELD_ERR_LOCALIZED_MESSAGE;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CBK_FIELD_ERR_MESSAGE;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CBK_GRANT_ACCESS;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CBK_GRANT_ACCESS_SUCCESS;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CBK_INFORMATION_BUTTONCLICK;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CBK_MENU_BUTTONCLICK;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CBK_SELECT_PLACE;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CBK_SELECT_PLACELIST;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CBK_SELECT_PLACELIST_ID;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CBK_SELECT_PLACE_CENTERON;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CBK_SELECT_PLACE_ID;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CBK_SUCCESS;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CMD_ARGS;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CMD_FIELD_ERR_LOCALIZED_MESSAGE;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CMD_FIELD_ERR_MESSAGE;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CMD_GRANT_ACCESS;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CMD_GRANT_ACCESS_SUCCESS;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CMD_GRANT_ACCESS_TOKEN;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CMD_SELECT_PLACE;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CMD_SELECT_PLACELIST;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CMD_SELECT_PLACELIST_ID;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CMD_SELECT_PLACE_CENTERON;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CMD_SELECT_PLACE_ID;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CMD_SUCCESS;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CMD_UNSELECT_CONTENT;

public class MapwizeActivity extends AppCompatActivity implements MapwizeFragment.OnFragmentInteractionListener {
    private static final String TAG = "MapwizeActivity";

    private static final String OPT_FLOOR = "floor";
    private static final String OPT_LANG = "language";
    private static final String OPT_UNIVERSE_ID = "universeId";
    private static final String OPT_RESTRICT_CONTENT_TO_VENUE_ID = "restrictContentToVenueId";
    private static final String OPT_RESTRICT_CONTENT_TO_ORG_ID = "restrictContentToOrganizationId";
    private static final String OPT_CENTER_ON_VENUE_ID = "centerOnVenueId";
    private static final String OPT_CENTER_ON_PLACE_ID = "centerOnPlaceId";

    MapwizeFragment mapwizeFragment;
    MapboxMap mapboxMap;
    MapwizePlugin mapwizePlugin;
    ManualIndoorLocationProvider locationProvider;
    BroadcastReceiver mCbkReceiver;
    Activity          mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate...");
        setContentView(R.layout.activity_mapwize);
        Intent intent = getIntent();
        String optStr = intent.getStringExtra(MapwizeCordovaPlugin.OPTIONS_STR);
        Log.d(TAG, "onCreate...optStr: " + optStr);
        MapOptions opts = getOptionsFromStr(optStr);

        MapwizeFragmentUISettings uiSettings = new MapwizeFragmentUISettings.Builder().build();
        mapwizeFragment = MapwizeFragment.newInstance(opts, uiSettings);
        FragmentManager fm = getSupportFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fragmentContainer, mapwizeFragment);
        ft.commit();

        mCbkReceiver = new CbkReceiver();

        IntentFilter filter = new IntentFilter();

        filter.addAction(CMD_SELECT_PLACE);
        filter.addAction(CMD_SELECT_PLACELIST);
        filter.addAction(CMD_GRANT_ACCESS);
        filter.addAction(CMD_UNSELECT_CONTENT);

        LocalBroadcastManager.getInstance(this).registerReceiver(mCbkReceiver, filter);

        mActivity = this;

        Log.d(TAG, "onCreate...END");
    }

    /**
     * Creates a MapOptions from a json string
     * @param optStr the json string
     * @return
     */
    private MapOptions getOptionsFromStr(String optStr) {
        Log.d(TAG, "getOptionsFromStr...");
        try {
            JSONObject json = new JSONObject(optStr);
            MapOptions.Builder builder = new MapOptions.Builder();

            Double floor = json.getDouble(OPT_FLOOR);
            if (floor != null) {
                builder.floor(floor);
            }

            String lang = json.getString(OPT_LANG);
            if (lang != null) {
                builder.language(lang);
            }

            String universeId = json.getString(OPT_UNIVERSE_ID); //TODO: is it and ID???
            if (universeId != null) {
                builder.universe(universeId);
            }

            String restrictToVenue = json.getString(OPT_RESTRICT_CONTENT_TO_VENUE_ID);
            if (restrictToVenue != null) {
                builder.restrictContentToVenue(restrictToVenue);
            }

            String restrictToOrg = json.getString(OPT_RESTRICT_CONTENT_TO_ORG_ID);
            if (restrictToOrg != null) {
                builder.restrictContentToOrganization(restrictToOrg);
            }

            String centerOnVenueId = json.getString(OPT_CENTER_ON_VENUE_ID); //TODO: is it and ID???
            if (centerOnVenueId != null) {
                builder.centerOnVenue(centerOnVenueId);
            }

            String centerOnPlaceId = json.getString(OPT_CENTER_ON_PLACE_ID); //TODO: is it and ID???
            if (centerOnPlaceId != null) {
                builder.centerOnPlace(centerOnPlaceId);
            }

            MapOptions opt = builder.build();
            Log.d(TAG, "getOptionsFromStr, return...");
            return opt;
        } catch (JSONException e) {
            Log.d(TAG, "getOptionsFromStr, exception...");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void onMenuButtonClick() {
        Log.d(TAG, "onMenuButtonClick...");
        sendCallbackEventOK(CBK_MENU_BUTTONCLICK, null);
    }

    @Override
    public void onInformationButtonClick(MapwizeObject mapwizeObject) {
        if (mapwizeObject.getClass() == Place.class) {
            sendCallbackEventOK(CBK_EVENT_TAP_ON_PLACE_INFORMATION_BUTTON, ((Place)mapwizeObject).toJSONString());
        } else if (mapwizeObject.getClass() == PlaceList.class) {
            sendCallbackEventOK(CBK_EVENT_TAP_ON_PLACES_INFORMATION_BUTTON, ((PlaceList)mapwizeObject).toJSONString());
        } else {
            Log.d(TAG, "onInformationButtonClick, Object is not recognized...");
        }

    }

    @Override
    public void onFragmentReady(MapboxMap mapboxMap, MapwizePlugin mapwizePlugin) {
        Log.d(TAG, "onFragmentReady...");
        this.mapboxMap = mapboxMap;
        this.mapwizePlugin = mapwizePlugin;
        this.locationProvider = new ManualIndoorLocationProvider();
        Log.d(TAG, "onFragmentReady...1");
        this.mapwizePlugin.setLocationProvider(this.locationProvider);
        Log.d(TAG, "onFragmentReady...2");
        this.mapwizePlugin.addOnLongClickListener(new MapwizePlugin.OnLongClickListener() {
            @Override
            public void onLongClickEvent(@NonNull ClickEvent clickEvent) {
                Log.d(TAG, "onLongClickEvent...");
                IndoorLocation indoorLocation = new IndoorLocation("manual_provider",
                        clickEvent.getLatLngFloor().getLatitude(), clickEvent.getLatLngFloor().getLongitude(),
                        clickEvent.getLatLngFloor().getFloor(), System.currentTimeMillis());
                locationProvider.setIndoorLocation(indoorLocation);
            }
        });
        sendCallbackEventOK(CBK_EVENT_DID_LOAD, "");
        Log.d(TAG, "onFragmentReady...3");
    }

    @Override
    public void onFollowUserButtonClickWithoutLocation() {
        Log.d(TAG, "onFollowUserButtonClickWithoutLocation...");
        sendCallbackEventOK(CBK_EVENT_DID_TAP_ON_FOLLOW_WITHOUT_LOCATION, "");
    }

    @Override
    public boolean shouldDisplayInformationButton(MapwizeObject mapwizeObject) {
        if(mapwizeObject.getClass() == Place.class) {
            return true;
        } else if(mapwizeObject.getClass() == PlaceList.class) {
            return true;
        }

        return false;
    }

    @Override
    public boolean shouldDisplayFloorController(List<Double> floors) {
        if (floors == null || floors.size() <= 1) {
            return false;
        }
        return true;
    }


    /**
     * Sends a success command event intended to be received by CordovaPlugin object
     * @param action The command
     * @param args   JSON string of the return params
     */
    void sendCmdEventOK(String action, String args) {
        sendCmdEvent(action, args, true);
    }

    /**
     * Sends a failure command event intended to be received by CordovaPlugin object
     * @param action    The command
     * @param throwable The throwable to extract the error messages
     */
    void sendCmdEventErr(String action, @Nullable Throwable throwable) {
        try {
            JSONObject json = new JSONObject();
            String message = throwable.getMessage();
            String locMessage = throwable.getLocalizedMessage();
            if(message != null) {
                json.put(CMD_FIELD_ERR_MESSAGE, message);
            }

            if(locMessage != null) {
                json.put(CMD_FIELD_ERR_LOCALIZED_MESSAGE, locMessage);
            }

            sendCmdEvent(action, json.toString(), false);
        } catch(JSONException e) {
            sendCmdEvent(action, "", false);
        }

    }

    /**
     * Sends a success/failure command event intended to be received by CordovaPlugin object
     * @param action  The command
     * @param args    JSON string of the return params
     * @param success The success of the command
     */
    void sendCmdEvent(String action, String args, boolean success) {
        Intent intent = new Intent(action);
        intent.putExtra(CMD_SUCCESS, success);
        if(args != null) {
            intent.putExtra(CMD_ARGS, args);
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    /**
     * Sends a success callback event intended to be received by CordovaPlugin object
     * @param action  The name of the callback event
     * @param args    JSON string of the return params
     */
    void sendCallbackEventOK(String action, String args) {
        sendCallbackEvent(action, args, true);
    }

    /**
     * Sends a success callback event intended to be received by CordovaPlugin object
     * @param action  The name of the callback event
     * @param args    JSON string of the return params
     */
    void sendCallbackEventErr(String action, String args) {
        sendCallbackEvent(action, args, false);
    }

    /**
     * Sends a failure command event intended to be received by CordovaPlugin object
     * @param action    The command
     * @param throwable The throwable to extract the error messages
     */
    void sendCallbackEventErr(String action, @Nullable Throwable throwable) {
        try {
            JSONObject json = new JSONObject();
            String message = throwable.getMessage();
            String locMessage = throwable.getLocalizedMessage();
            if(message != null) {
                json.put(CBK_FIELD_ERR_MESSAGE, message);
            }

            if(locMessage != null) {
                json.put(CBK_FIELD_ERR_LOCALIZED_MESSAGE, locMessage);
            }

            sendCallbackEvent(action, json.toString(), false);
        } catch(JSONException e) {
            sendCallbackEvent(action, "", false);
        }

    }

    /**
     * Sends a success//failure callback event intended to be received by CordovaPlugin object
     * @param action  The name of the callback event
     * @param args    JSON string of the return params
     * @param success The success of the command
     */
    void sendCallbackEvent(String action, String args, boolean success) {
        Intent intent = new Intent(action);
        intent.putExtra(CBK_SUCCESS, success);
        if(args != null) {
            intent.putExtra(CBK_ARGS, args);
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


    /**
     * Broadcast receiver for return values from MapwizeView
     */
    public class CbkReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "CbkReceiver onReceive...");
            final String action = intent.getAction();
            if (CMD_SELECT_PLACE.equals(action)) {
                Log.d(TAG, "Received: CMD_SELECT_PLACE");

                String id = intent.getStringExtra(CMD_SELECT_PLACE_ID);
                boolean centerOn = intent.getBooleanExtra(CMD_SELECT_PLACE_CENTERON, false);
                Log.d(TAG, "Received: CMD_SELECT_PLACE, id: " + id + " centerOn: " + centerOn);
                Api.getPlace(id, new ApiCallback<Place>() {
                    @Override
                    public void onSuccess(@Nullable Place place) {
                        Log.d(TAG, "receiver, CMD_SELECT_PLACE...");
                        mActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                mapwizeFragment.selectPlace(place, centerOn);
                                try {
                                    JSONObject json = new JSONObject();
                                    json.put(CBK_SELECT_PLACE_ID, id);
                                    json.put(CBK_SELECT_PLACE_CENTERON, centerOn);
                                    sendCallbackEventOK(CBK_SELECT_PLACE, json.toString());
                                } catch(JSONException e) {
                                    sendCallbackEventErr(CBK_SELECT_PLACE, "");
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(@Nullable Throwable throwable) {
                        Log.d(TAG, "onFailure, failed CMD_SELECT_PLACE...");
                        sendCallbackEventErr(CBK_SELECT_PLACE, throwable);
                    }
                });

            } else if (CMD_SELECT_PLACELIST.equals(action)) {
                Log.d(TAG, "Received: CBK_SELECT_PLACELIST");

                String id = intent.getStringExtra(CMD_SELECT_PLACELIST_ID);
                Log.d(TAG, "Received: CMD_SELECT_PLACELIST_ID, id: " + id);
                Api.getPlaceList(id, new ApiCallback<PlaceList>() {
                    @Override
                    public void onSuccess(@Nullable PlaceList placeList) {
                        Log.d(TAG, "receiver, CMD_SELECT_PLACELIST_ID...");
                        mActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                mapwizeFragment.selectPlaceList(placeList);
                                try {
                                    JSONObject json = new JSONObject();
                                    json.put(CBK_SELECT_PLACELIST_ID, id);

                                    sendCallbackEventOK(CBK_SELECT_PLACELIST, json.toString());
                                } catch(JSONException e) {
                                    sendCallbackEventErr(CBK_SELECT_PLACELIST, "");
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(@Nullable Throwable throwable) {
                        Log.d(TAG, "onFailure, failed CMD_SELECT_PLACE...");
                        sendCallbackEventErr(CBK_SELECT_PLACELIST, throwable);
                    }
                });

            } else if (CMD_GRANT_ACCESS.equals(action)) {
                Log.d(TAG, "Received: CMD_GRANT_ACCESS");
                String token = intent.getStringExtra(CMD_GRANT_ACCESS_TOKEN);
                Log.d(TAG, "Received: CMD_GRANT_ACCESS_TOKEN: " + token);
                mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        mapwizePlugin.grantAccess(token, new ApiCallback<Boolean>() {

                            @Override
                            public void onSuccess(@Nullable Boolean aBoolean) {
                                Log.d(TAG, "onSuccess, grantAccess success...");
                                try {
                                    JSONObject json = new JSONObject();
                                    json.put(CBK_GRANT_ACCESS_SUCCESS, aBoolean);
                                    sendCallbackEventOK(CBK_GRANT_ACCESS, json.toString());
                                } catch (JSONException e) {
                                    sendCallbackEventOK(CBK_GRANT_ACCESS, "");
                                }
                            }

                            @Override
                            public void onFailure(@Nullable Throwable throwable) {
                                Log.d(TAG, "onFailure, grantAccess failure...");
                                sendCallbackEventErr(CBK_GRANT_ACCESS, throwable);
                            }
                        });
                    }
                });


            } else if (CMD_UNSELECT_CONTENT.equals(action)) {
                Log.d(TAG, "Received: CMD_UNSELECT_CONTENT");
                mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        mapwizeFragment.unselectContent();
                    }
                });

            } else {
                Log.d(TAG, "Received: CMD_SELECT_PLACE");
            }
        }
    }


}


