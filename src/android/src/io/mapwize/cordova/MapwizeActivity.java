package io.mapwize.cordova;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.mapbox.mapboxsdk.maps.MapboxMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import io.indoorlocation.core.IndoorLocation;
import io.indoorlocation.manual.ManualIndoorLocationProvider;
import io.mapwize.mapwizesdk.api.ApiCallback;
import io.mapwize.mapwizesdk.api.Direction;
import io.mapwize.mapwizesdk.api.DirectionPoint;
import io.mapwize.mapwizesdk.api.Floor;
import io.mapwize.mapwizesdk.api.MapwizeApi;
import io.mapwize.mapwizesdk.api.MapwizeApiFactory;
import io.mapwize.mapwizesdk.api.MapwizeObject;
import io.mapwize.mapwizesdk.api.Parser;
import io.mapwize.mapwizesdk.api.Place;
import io.mapwize.mapwizesdk.api.Placelist;
import io.mapwize.mapwizesdk.api.Style;
import io.mapwize.mapwizesdk.api.Venue;
import io.mapwize.mapwizesdk.map.ClickEvent;
import io.mapwize.mapwizesdk.map.MapOptions;
import io.mapwize.mapwizeui.MapwizeFragment;
import io.mapwize.mapwizeui.MapwizeFragmentUISettings;
import io.mapwize.mapwizesdk.map.MapwizeMap;

import static io.mapwize.cordova.MapwizeCordovaPlugin.CBK_ARGS;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CBK_CREATE_MAPWIZEVIEW;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CBK_EVENT_CLOSE_BUTTON_CLICKED;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CBK_EVENT_DID_LOAD;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CBK_EVENT_DID_TAP_ON_FOLLOW_WITHOUT_LOCATION;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CBK_EVENT_TAP_ON_PLACES_INFORMATION_BUTTON;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CBK_EVENT_TAP_ON_PLACE_INFORMATION_BUTTON;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CBK_FIELD_ERR_LOCALIZED_MESSAGE;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CBK_FIELD_ERR_MESSAGE;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CBK_GRANT_ACCESS;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CBK_GRANT_ACCESS_SUCCESS;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CBK_MENU_BUTTONCLICK;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CBK_SELECT_PLACE;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CBK_SELECT_PLACELIST;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CBK_SELECT_PLACELIST_ID;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CBK_SELECT_PLACE_CENTERON;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CBK_SELECT_PLACE_ID;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CBK_SET_DIRECTION;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CBK_SET_PLACE_STYLE;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CBK_SUCCESS;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CMD_ARGS;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CMD_FIELD_ERR_LOCALIZED_MESSAGE;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CMD_FIELD_ERR_MESSAGE;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CMD_GET_DIRECTION;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CMD_GRANT_ACCESS;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CMD_GRANT_ACCESS_TOKEN;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CMD_SELECT_PLACE;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CMD_SELECT_PLACELIST;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CMD_SELECT_PLACELIST_ID;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CMD_SELECT_PLACE_CENTERON;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CMD_SELECT_PLACE_ID;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CMD_SET_DIRECTION;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CMD_SET_DIRECTION_DIRECTION;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CMD_SET_DIRECTION_FROM;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CMD_SET_DIRECTION_ISACCESSIBLE;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CMD_SET_DIRECTION_TO;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CMD_SET_PLACE_STYLE;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CMD_SET_PLACE_STYLE_ID;
import static io.mapwize.cordova.MapwizeCordovaPlugin.CMD_SET_PLACE_STYLE_STYLE;
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
    private static final String OPT_SHOW_CLOSE_BUTTON = "showCloseButton";
    private static final String OPT_SHOW_INFO_BUTTON_FOR_PLACES = "showInformationButtonForPlaces";
    private static final String OPT_SHOW_INFO_BUTTON_FOR_PLACELISTS = "showInformationButtonForPlaceLists";

    private static final String UIS_MENUBUTTONHIDDEN = "menuButtonHidden";
    private static final String UIS_COMPASSHIDDEN = "compassHidden";
    private static final String UIS_FOLLOWUSERBUTTONHIDDEN = "followUserButtonHidden";
    private static final String UIS_FLOORCONTROLLERHIDDEN = "floorControllerHidden";

    private static final String CORDOVA_SHOW_INFO_BUTTON = "cordovaShowInformationButton";

    private static final String STYLE_MARKERURL = "markerUrl";
    boolean showInformationButtonForPlaces;
    boolean showInformationButtonForPlaceLists;

    MapwizeFragment mapwizeFragment;
    MapwizeMap mMapwizeMap;

    ManualIndoorLocationProvider locationProvider;
    BroadcastReceiver mCbkReceiver;
    Activity          mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate...");
        String package_name = getApplication().getPackageName();
        setContentView(getApplication().getResources().getIdentifier("activity_mapwize", "layout", package_name));

        Intent intent = getIntent();
        String optStr = intent.getStringExtra(MapwizeCordovaPlugin.OPTIONS_STR);
        String uiSettingsStr = intent.getStringExtra(MapwizeCordovaPlugin.UISETTINGS_STR);

        boolean showClose = false;
        MapOptions opts = null;
        JSONObject optJson = null;
        JSONObject uiSettingsJson = null;

        try {
            optJson = new JSONObject(optStr);
            Log.d(TAG, "onCreate...optStr: " + optStr);
            Log.d(TAG, "onCreate...optJson string: " + optJson.toString());
            opts = getOptionsFromStr(optJson);

            Log.d(TAG, "onCreate...uiSettingsStr: " + uiSettingsStr);
            showClose = optJson.optBoolean(OPT_SHOW_CLOSE_BUTTON, false);
            Log.d(TAG, "showClose: " + showClose);

            showInformationButtonForPlaces = optJson.optBoolean(OPT_SHOW_INFO_BUTTON_FOR_PLACES, true);
            showInformationButtonForPlaceLists = optJson.optBoolean(OPT_SHOW_INFO_BUTTON_FOR_PLACELISTS, true);

            uiSettingsJson = new JSONObject(uiSettingsStr);
        } catch (JSONException e) {
            e.printStackTrace();
            sendCmdEventErr(CBK_CREATE_MAPWIZEVIEW, "");
            return;
        }

        MapwizeFragmentUISettings uiSettings = new MapwizeFragmentUISettings.Builder()
        .menuButtonHidden(uiSettingsJson.optBoolean(UIS_MENUBUTTONHIDDEN, true))
        .compassHidden(uiSettingsJson.optBoolean(UIS_COMPASSHIDDEN, true))
        .followUserButtonHidden(uiSettingsJson.optBoolean(UIS_FOLLOWUSERBUTTONHIDDEN, true))
        .floorControllerHidden(uiSettingsJson.optBoolean(UIS_FLOORCONTROLLERHIDDEN, true))
        .build();

        mapwizeFragment = MapwizeFragment.newInstance(opts, uiSettings);
        FragmentManager fm = getSupportFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();
        ft.add(getApplication().getResources().getIdentifier("fragmentContainer", "id", package_name), mapwizeFragment);
        ft.commit();

        mCbkReceiver = new CbkReceiver();

        IntentFilter filter = new IntentFilter();

        filter.addAction(CMD_SELECT_PLACE);
        filter.addAction(CMD_SET_PLACE_STYLE);
        filter.addAction(CMD_SELECT_PLACELIST);
        filter.addAction(CMD_SET_DIRECTION);
        filter.addAction(CMD_GET_DIRECTION);
        filter.addAction(CMD_GRANT_ACCESS);
        filter.addAction(CMD_UNSELECT_CONTENT);

        LocalBroadcastManager.getInstance(this).registerReceiver(mCbkReceiver, filter);

        mActivity = this;

        // Always cast your custom Toolbar here, and set it as the ActionBar.
        // Toolbar tb = (Toolbar) findViewById(R.id.imgtoolbar);
        Toolbar tb = findViewById(getApplication().getResources().getIdentifier("imgtoolbar", "id", package_name));
        setSupportActionBar(tb);

        // Get the ActionBar here to configure the way it behaves.
        final ActionBar ab = getSupportActionBar();
        if (!showClose) {
            ab.hide();
        } else {
            ab.setDisplayShowTitleEnabled(false); // disable the default title element here (for centered title)
        }

        ImageButton button = findViewById(getApplication().getResources().getIdentifier("imageButton", "id", package_name));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                sendCallbackEventOK(CBK_EVENT_CLOSE_BUTTON_CLICKED, null);
            }
        });

        sendCmdEventOK(CBK_CREATE_MAPWIZEVIEW, "");

        Log.d(TAG, "onCreate...END");
    }

    /**
     * Creates a MapOptions from a json object
     * @param json the json object that holds the options
     * @return
     */
    private MapOptions getOptionsFromStr(JSONObject json) throws JSONException {
        Log.d(TAG, "getOptionsFromStr...json: " + json.toString());
        MapOptions.Builder builder = new MapOptions.Builder();
        try {
            Double floor = json.optDouble(OPT_FLOOR);
            if (floor != null) {
                Log.d(TAG, "added floor: " + floor);
                builder.floor(floor);
            }

            String lang = json.optString(OPT_LANG);
            if (!TextUtils.isEmpty(lang)) {
                Log.d(TAG, "added lang: " + lang);
                builder.language(lang);
            }

            Log.d(TAG, "getting universeId...");
            String universeId = json.optString(OPT_UNIVERSE_ID); //TODO: is it and ID???
            if (!TextUtils.isEmpty(universeId)) {
                Log.d(TAG, "added universeId: " + universeId);
                builder.universe(universeId);
            }

            Log.d(TAG, "getting restrictToVenue...");
            String restrictToVenue = json.optString(OPT_RESTRICT_CONTENT_TO_VENUE_ID);
            if (!TextUtils.isEmpty(restrictToVenue)) {
                Log.d(TAG, "added restrictToVenue: " + restrictToVenue);
                Venue venue = Parser.parseVenue(restrictToVenue);
                builder.restrictContentToVenue(venue);
            }

            Log.d(TAG, "getting restrictToOrg...");
            String restrictToOrg = json.optString(OPT_RESTRICT_CONTENT_TO_ORG_ID);
            if (!TextUtils.isEmpty(restrictToOrg)) {
                Log.d(TAG, "added restrictToOrg: " + restrictToOrg);
                builder.restrictContentToOrganization(restrictToOrg);
            }

            Log.d(TAG, "getting centerOnVenueId...");
            String centerOnVenueId = json.optString(OPT_CENTER_ON_VENUE_ID); //TODO: is it and ID???
            if (!TextUtils.isEmpty(centerOnVenueId)) {
                Log.d(TAG, "added centerOnVenueId: " + centerOnVenueId);
                builder.centerOnVenue(centerOnVenueId);
            }

            Log.d(TAG, "getting centerOnPlaceId...");
            String centerOnPlaceId = json.optString(OPT_CENTER_ON_PLACE_ID); //TODO: is it and ID???
            if (!TextUtils.isEmpty(centerOnPlaceId)) {
                Log.d(TAG, "added centerOnPlaceId: " + centerOnPlaceId);
                builder.centerOnPlace(centerOnPlaceId);
            }
        } catch (JSONException e) {
            Log.d(TAG, "getOptionsFromStr, JSON exception:: " + e.getLocalizedMessage());
            throw e;
        }

        MapOptions opt = builder.build();
        Log.d(TAG, "getOptionsFromStr, return...");
        return opt;
    }

    /**
     * Creates a Style object from a json string
     * @param jsonStr the json string that holds the style
     * @return
     */
    private Style getStyleFromStr(String jsonStr) {
        Log.d(TAG, "getStyleFromStr...");
        try {
            Style style = Parser.parseStyle(jsonStr);
            return style;

        } catch (JSONException e) {
            return null;
        }
    }

    @Override
    public void onMenuButtonClick() {
        Log.d(TAG, "onMenuButtonClick...");
        sendCallbackEventOK(CBK_MENU_BUTTONCLICK, null);
    }

    @Override
    public void onInformationButtonClick(io.mapwize.mapwizesdk.api.MapwizeObject mapwizeObject) {
        Log.d(TAG, "onInformationButtonClick...class: " + mapwizeObject.getClass() + ", className: " + mapwizeObject.getClass().getName() + ", Place.class: " + Place.class + ", Place.className: " + Place.class.getName());

        if (mapwizeObject instanceof Place) {
            sendCallbackEventOK(CBK_EVENT_TAP_ON_PLACE_INFORMATION_BUTTON, mapwizeObject.toJSONString());
        } else if (mapwizeObject instanceof Placelist) {
            sendCallbackEventOK(CBK_EVENT_TAP_ON_PLACES_INFORMATION_BUTTON, mapwizeObject.toJSONString());
        } else {
            Log.d(TAG, "onInformationButtonClick, Object is not recognized...");
        }
    }

    @Override
    public void onFragmentReady(MapwizeMap mapwizeMap) {
        Log.d(TAG, "onFragmentReady...");
        this.locationProvider = new ManualIndoorLocationProvider();
        this.mMapwizeMap = mapwizeMap;
        Log.d(TAG, "onFragmentReady...1");
        mapwizeMap.setIndoorLocationProvider(this.locationProvider);
        Log.d(TAG, "onFragmentReady...2");
        mapwizeMap.addOnLongClickListener(new MapwizeMap.OnLongClickListener() {
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
        Log.d(TAG, "onInformationButtonClick...class: " + mapwizeObject.getClass() + ", className: " + mapwizeObject.getClass().getName() + ", Place.class: " + Place.class + ", Place.className: " + Place.class.getName());
        JSONObject data = mapwizeObject.getData();

        if (data != null && data.has(CORDOVA_SHOW_INFO_BUTTON)) {
            return data.optBoolean(CORDOVA_SHOW_INFO_BUTTON);
        }

        if (mapwizeObject instanceof Place) {
            return showInformationButtonForPlaces;
        } else if (mapwizeObject instanceof Placelist) {
            return showInformationButtonForPlaceLists;
        } else {
            Log.d(TAG, "shouldDisplayInformationButton, Object is not recognized...");
            return false;
        }
    }

    @Override
    public boolean shouldDisplayFloorController(List<Floor> floors) {
        return floors != null && floors.size() > 1;
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
     * Sends a success command event intended to be received by CordovaPlugin object
     * @param action The command
     * @param args   JSON string of the return params
     */
    void sendCmdEventErr(String action, String args) {
        sendCmdEvent(action, args, false);
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
            Log.d(TAG, "sendCallbackEventErr, message: " + message);
            
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
        Log.d(TAG, "sendCmdEvent, action: " + action + ", success: " + success);
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
        Log.d(TAG, "sendCallbackEventOK...");
        sendCallbackEvent(action, args, true);
    }

    /**
     * Sends a success callback event intended to be received by CordovaPlugin object
     * @param action  The name of the callback event
     * @param args    JSON string of the return params
     */
    void sendCallbackEventErr(String action, String args) {
        Log.d(TAG, "sendCallbackEventErr...");
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
            Log.d(TAG, "sendCallbackEventErr, message: " + message);
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

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy...");
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mCbkReceiver);
        mCbkReceiver = null;
        this.locationProvider = null;
        mapwizeFragment = null;
        this.locationProvider = null;
    }

    private MapwizeApi getApi() {
        return MapwizeApiFactory.getApi();
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
                getApi().getPlace(id, new ApiCallback<Place>() {
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
                                    sendCmdEventOK(CBK_SELECT_PLACE, json.toString());
                                } catch(JSONException e) {
                                    sendCmdEventErr(CBK_SELECT_PLACE, "");
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(@Nullable Throwable throwable) {
                        Log.d(TAG, "onFailure, failed CMD_SELECT_PLACE...");
                        sendCmdEventErr(CBK_SELECT_PLACE, throwable);
                    }
                });

            } if (CMD_SET_PLACE_STYLE.equals(action)) {
                Log.d(TAG, "Received: CMD_SET_PLACE_STYLE");

                String id = intent.getStringExtra(CMD_SET_PLACE_STYLE_ID);
                String style = intent.getStringExtra(CMD_SET_PLACE_STYLE_STYLE);
                Log.d(TAG, "Received: CMD_SET_PLACE_STYLE, id: " + id);
                getApi().getPlace(id, new ApiCallback<Place>() {
                    @Override
                    public void onSuccess(@Nullable Place place) {
                        Log.d(TAG, "receiver, CMD_SET_PLACE_STYLE...");
                        mActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                Style mapwizeStyle = getStyleFromStr(style);

                                if (mMapwizeMap != null) {
                                    mMapwizeMap.setPlaceStyle(place, mapwizeStyle);                                    
                                }

                                try {
                                    JSONObject json = new JSONObject();
                                    json.put(CMD_SET_PLACE_STYLE_ID, id);
                                    json.put(CMD_SET_PLACE_STYLE_STYLE, style);
                                    sendCmdEventOK(CBK_SET_PLACE_STYLE, json.toString());
                                } catch(JSONException e) {
                                    sendCmdEventErr(CBK_SET_PLACE_STYLE, "");
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(@Nullable Throwable throwable) {
                        Log.d(TAG, "onFailure, failed CMD_SET_PLACE_STYLE...");
                        sendCmdEventErr(CBK_SET_PLACE_STYLE, throwable);
                    }
                });

            } else if (CMD_SET_DIRECTION.equals(action)) {
                Log.d(TAG, "Received: CMD_SET_DIRECTION");

                try {
                    String directionStr = intent.getStringExtra(CMD_SET_DIRECTION_DIRECTION);
                    Log.d(TAG, "CMD_SET_DIRECTION, parsing directionStr..." + directionStr);
                    JSONObject var1 = new JSONObject(directionStr);

                    Log.d(TAG, "CMD_SET_DIRECTION, parsing CMD_SET_DIRECTION_DIRECTION...");
                    Direction direction = Parser.parseDirection(directionStr);
                    Log.d(TAG, "CMD_SET_DIRECTION, getting CMD_SET_DIRECTION_FROM...");
                    String fromStr = intent.getStringExtra(CMD_SET_DIRECTION_FROM);
                    DirectionPoint from = Parser.parseDirectionPoint(fromStr);
                    Log.d(TAG, "CMD_SET_DIRECTION, getting CMD_SET_DIRECTION_TO...");
                    String toStr = intent.getStringExtra(CMD_SET_DIRECTION_TO);
                    DirectionPoint to = Parser.parseDirectionPoint(toStr);
                    Log.d(TAG, "CMD_SET_DIRECTION, getting CMD_SET_DIRECTION_ISACCESSIBLE...");
                    boolean isAccessible = intent.getBooleanExtra(CMD_SET_DIRECTION_ISACCESSIBLE, false);

                    Log.d(TAG, "Received: CMD_SET_DIRECTION...");
                    mActivity.runOnUiThread(new Runnable() {
                        public void run() {
                            Log.d(TAG, "CMD_SET_DIRECTION, calling setDirection...");
                            mapwizeFragment.setDirection(direction, from, to, isAccessible);
                            Log.d(TAG, "CMD_SET_DIRECTION, sendCmdEventOK...");
                            sendCmdEventOK(CBK_SET_DIRECTION, null);
                        }
                    });

                } catch (JSONException e) {
                    Log.d(TAG, "CMD_SET_DIRECTION JSONException...e:  " + e.getLocalizedMessage());
                    sendCmdEventErr(CBK_SET_DIRECTION, (String) e.getLocalizedMessage());
                }  catch (NullPointerException e) {
                    Log.d(TAG, "CMD_SET_DIRECTION NullPointerException...");
                    sendCmdEventErr(CBK_SET_DIRECTION, (String) e.getLocalizedMessage());
                }

            } else if (CMD_SELECT_PLACELIST.equals(action)) {
                Log.d(TAG, "Received: CBK_SELECT_PLACELIST");

                String id = intent.getStringExtra(CMD_SELECT_PLACELIST_ID);
                Log.d(TAG, "Received: CMD_SELECT_PLACELIST_ID, id: " + id);
                getApi().getPlacelist(id, new ApiCallback<Placelist>() {
                    @Override
                    public void onSuccess(@Nullable Placelist placeList) {
                        Log.d(TAG, "receiver, CMD_SELECT_PLACELIST_ID...");
                        mActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                Log.d(TAG, "receiver, CMD_SELECT_PLACELIST_ID, run...");
                                mapwizeFragment.selectPlacelist(placeList);
                                Log.d(TAG, "receiver, CMD_SELECT_PLACELIST_ID, end...");
                                try {
                                    JSONObject json = new JSONObject();
                                    json.put(CBK_SELECT_PLACELIST_ID, id);

                                    sendCmdEventOK(CBK_SELECT_PLACELIST, json.toString());
                                } catch(JSONException e) {
                                    sendCmdEventErr(CBK_SELECT_PLACELIST, "");
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(@Nullable Throwable throwable) {
                        Log.d(TAG, "onFailure, failed CBK_SELECT_PLACELIST...");
                        sendCmdEventErr(CBK_SELECT_PLACELIST, throwable);
                    }
                });

            } else if (CMD_GRANT_ACCESS.equals(action)) {
                Log.d(TAG, "Received: CMD_GRANT_ACCESS");
                String token = intent.getStringExtra(CMD_GRANT_ACCESS_TOKEN);
                Log.d(TAG, "Received: CMD_GRANT_ACCESS_TOKEN: " + token);
                mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        mapwizeFragment.grantAccess(token, new ApiCallback<Boolean>() {

                            @Override
                            public void onSuccess(@Nullable Boolean aBoolean) {
                                Log.d(TAG, "onSuccess, grantAccess success...");
                                try {
                                    JSONObject json = new JSONObject();
                                    json.put(CBK_GRANT_ACCESS_SUCCESS, aBoolean);
                                    sendCmdEventOK(CBK_GRANT_ACCESS, json.toString());
                                } catch (JSONException e) {
                                    sendCmdEventErr(CBK_GRANT_ACCESS, "");
                                }
                            }

                            @Override
                            public void onFailure(@Nullable Throwable throwable) {
                                Log.d(TAG, "onFailure, grantAccess failure...");
                                sendCmdEventErr(CBK_GRANT_ACCESS, throwable);
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
