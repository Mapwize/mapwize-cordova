package io.mapwize.cordova;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.onehilltech.metadata.ManifestMetadata;

import io.mapwize.mapwizesdk.core.MapwizeConfiguration;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapwizeCordovaPlugin extends CordovaPlugin {

    private static final String TAG = "MapwizeCordovaPlugin";
    private static final String ACTION_CREATE_MAPWIZE_VIEW = "createMapwizeView";

    private static final String ACTION_MAPWIZE_SETCALLBACK = "setCallback";
    private static final String ACTION_MAPWIZE_SELECTPLACE = "selectPlace";
    private static final String ACTION_MAPWIZE_SELECTPLACELIST = "selectPlaceList";
    private static final String ACTION_MAPWIZE_GET_DIRECTION = "getDirection";
    private static final String ACTION_MAPWIZE_SET_DIRECTION = "setDirection";

    private static final String ACTION_MAPWIZE_GRANTACCESS = "grantAccess";
    private static final String ACTION_MAPWIZE_UNSELECT_CONTENT = "unselectContent";
    private static final String ACTION_MAPWIZE_CLOSE = "closeMapwizeView";
    private static final String ACTION_MAPWIZE_SETPLACESTYLE = "setPlaceStyle";

    private static final String ACTION_OFFLINEMANAGER_INIT = "initOfflineManager";
    private static final String ACTION_OFFLINEMANAGER_REMOVE_DATA_FOR_VENUE = "removeDataForVenue";
    private static final String ACTION_OFFLINEMANAGER_DOWNLOAD_DATA_FOR_VENUE = "downloadDataForVenue";
    private static final String ACTION_OFFLINEMANAGER_IS_OFFLINE_FOR_VENUE = "isOfflineForVenue";
    private static final String ACTION_OFFLINEMANAGER_GET_OFFLINE_VENUES = "getOfflineVenues";
    private static final String ACTION_OFFLINEMANAGER_GET_OFFLINE_UNIVERSES_FOR_VENUE = "getOfflineUniversesForVenue";

    private static final String ACTION_API_GET_PLACE_WITH_ALIAS = "getPlaceWithAlias";

    private static final String ACTION_API_GETVENUEWITHID = "getVenueWithId";
    private static final String ACTION_API_GETVENUESWITHFILTER = "getVenuesWithFilter";
    private static final String ACTION_API_GETVENUEWITHNAME = "getVenueWithName";
    private static final String ACTION_API_GETVENUEWITHALIAS = "getVenueWithAlias";
    private static final String ACTION_API_GETPLACEWITHID = "getPlaceWithId";
    private static final String ACTION_API_GETPLACEWITHNAME = "getPlaceWithName";
    private static final String ACTION_API_GETPLACEWITHALIAS = "getPlaceWithAlias";
    private static final String ACTION_API_GETPLACESWITHFILTER = "getPlacesWithFilter";
    private static final String ACTION_API_GETPLACELISTWITHID = "getPlaceListWithId";
    private static final String ACTION_API_GETPLACELISTWITHNAME = "getPlaceListWithName";
    private static final String ACTION_API_GETPLACELISTWITHALIAS = "getPlaceListWithAlias";
    private static final String ACTION_API_GETPLACELISTSWITHFILTER = "getPlaceListsWithFilter";
    private static final String ACTION_API_GETUNIVERSEWITHID = "getUniverseWithId";
    private static final String ACTION_API_GETUNIVERSESWITHFILTER = "getUniversesWithFilter";
    private static final String ACTION_API_GETACCESSIBLEUNIVERSESWITHVENUE = "getAccessibleUniversesWithVenue";
    private static final String ACTION_API_SEARCHWITHPARAMS = "searchWithParams";

    private static final String ACTION_API_GETDIRECTIONWITHFROM = "getDirectionWithFrom";
    private static final String ACTION_API_GETDIRECTIONWITHDIRECTIONPOINTSFROM = "getDirectionWithDirectionPointsFrom";
    private static final String ACTION_API_GETDIRECTIONWITHWAYPOINTSFROM = "getDirectionWithWayPointsFrom";
    private static final String ACTION_API_GETDIRECTIONWITHDIRECTIONANDWAYPOINTSFROM = "getDirectionWithDirectionAndWayPointsFrom";
    private static final String ACTION_API_GETDISTANCESWITHFROM = "getDistancesWithFrom";

    public static final String OPTIONS_STR = "optionStr";
    public static final String UISETTINGS_STR = "uiSettingsStr";

    public static final String CMD = "cmd";
    public static final String CMD_SELECT_PLACE = "selectPlace";
    public static final String CMD_SELECT_PLACE_ID = "identifier";
    public static final String CMD_SELECT_PLACE_CENTERON = "centerOn";

    public static final String CMD_SET_PLACE_STYLE = "setPlace";
    public static final String CMD_SET_PLACE_STYLE_ID = "identifier";
    public static final String CMD_SET_PLACE_STYLE_STYLE = "style";

    public static final String CMD_SELECT_PLACELIST = "selectPlaceList";
    public static final String CMD_SELECT_PLACELIST_ID = "identifier";

    public static final String CMD_GET_DIRECTION = "getDirection";
    public static final String CMD_GET_DIRECTION_FROM = "getDirectionFrom";
    public static final String CMD_GET_DIRECTION_TO = "getDirectionTo";
    public static final String CMD_GET_DIRECTION_ISACCESSIBLE = "getDirectionIsAccessible";

    public static final String CMD_SET_DIRECTION = "setDirection";
    public static final String CMD_SET_DIRECTION_DIRECTION = "setDirectionDirection";
    public static final String CMD_SET_DIRECTION_FROM = "setDirectionFrom";
    public static final String CMD_SET_DIRECTION_TO = "setDirectionTo";
    public static final String CMD_SET_DIRECTION_ISACCESSIBLE = "setDirectionIsAccessible";

    public static final String CMD_GRANT_ACCESS = "grantAccess";
    public static final String CMD_GRANT_ACCESS_TOKEN = "token";
    public static final String CMD_GRANT_ACCESS_SUCCESS = "success";

    public static final String CMD_UNSELECT_CONTENT = "unselectContent";

    public static final String CBK_MENU_BUTTONCLICK = "menuButtonClick";
    public static final String CBK_ON_FRAGMENT_READY = "onFragmentReady";

    public static final String CBK_ARGS = "args";
    public static final String CBK_SUCCESS = "success";

    public static final String CMD_ARGS = "args";
    public static final String CMD_SUCCESS = "success";

    public static final String CMD_FIELD_ERR_MESSAGE = "message";
    public static final String CMD_FIELD_ERR_LOCALIZED_MESSAGE = "localizedMessage";

    public static final String CBK_FIELD_EVENT = "event";
    public static final String CBK_FIELD_ARG = "arg";

    public static final String CBK_FIELD_ERR_MESSAGE = "message";
    public static final String CBK_FIELD_ERR_LOCALIZED_MESSAGE = "localizedMessage";

    public static final String CBK_CREATE_MAPWIZEVIEW = "createMapwizeViewCbk";

    public static final String CBK_SELECT_PLACE = "selectPlaceCbk";
    public static final String CBK_SELECT_PLACE_ID = "identifier";
    public static final String CBK_SELECT_PLACE_CENTERON = "centerOn";

    public static final String CBK_SET_PLACE_STYLE = "setPlaceStyleCbk";
    public static final String CBK_SET_PLACE_STYLE_ID = "identifier";
    public static final String CBK_SET_PLACE_STYLE_STYLE = "style";

    public static final String CBK_SELECT_PLACELIST = "selectPlaceListCbk";
    public static final String CBK_SELECT_PLACELIST_ID = "identifier";

    public static final String CBK_SET_DIRECTION = "setDirectionCbk";

    public static final String CBK_DIRECTION = "directionCbk";
    public static final String CBK_DIRECTION_ID = "directionCbkId";

    public static final String CBK_GRANT_ACCESS = "grantAccessCbk";
    public static final String CBK_GRANT_ACCESS_TOKEN = "token";
    public static final String CBK_GRANT_ACCESS_SUCCESS = "success";

    public static final String CBK_UNSELECT_CONTENT = "unselectContentCbk";

    public static final String CBK_EVENT_DID_LOAD = "DidLoad";
    public static final String CBK_EVENT_DID_TAP_ON_FOLLOW_WITHOUT_LOCATION = "DidTapOnFollowWithoutLocation";
    public static final String CBK_EVENT_DID_TAP_ON_MENU = "DidTapOnMenu";
    public static final String CBK_EVENT_TAP_ON_PLACE_INFORMATION_BUTTON = "TapOnPlaceInformationButton";
    public static final String CBK_EVENT_TAP_ON_PLACES_INFORMATION_BUTTON = "TapOnPlaceListInformationButton";
    public static final String CBK_EVENT_CLOSE_BUTTON_CLICKED = "TapOnCloseButton";

    public static final int MAPWIZEVIEW_REQUEST_ID = 12122; // Number random enough

    BroadcastReceiver mCbkReceiver;

    private CallbackContext mCallback = null;
    private CallbackContext mGrantAccessCallback = null;
    private CallbackContext mSelectPlaceCallback = null;
    private CallbackContext mSelectPlaceListCallback = null;

    private CallbackContext mGetDirectionCallback = null;

    private CallbackContext mCreateMapwizeViewCallback = null;
    private CallbackContext mSetPlaceStyleCallback = null;
    private Intent mMapwizeViewIntent = null;

    OfflineManager mOfflineManager;

    /**
     * Constructor.
     */
    public MapwizeCordovaPlugin() {
    }

    /**
     * Sets the context of the Command. This can then be used to do things like
     * get file paths associated with the Activity.
     *
     * @param cordova
     *            The context of the main Activity.
     * @param webView
     *            The CordovaWebView Cordova is running in.
     */
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        String value = getApiKey();
        Log.d(TAG, "MapwizeCordovaPlugin, initialize MWZMAPWIZEAPIKEY: " + value);

        if (value != null) {
            MapwizeConfiguration config = new MapwizeConfiguration.Builder(cordova.getActivity().getApplication(), value).build();
            MapwizeConfiguration.start(config);
        }

        mCbkReceiver = new CbkReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(CBK_MENU_BUTTONCLICK);
        filter.addAction(CBK_EVENT_DID_LOAD);
        filter.addAction(CBK_EVENT_DID_TAP_ON_FOLLOW_WITHOUT_LOCATION);
        filter.addAction(CBK_EVENT_DID_TAP_ON_MENU);
        filter.addAction(CBK_EVENT_TAP_ON_PLACE_INFORMATION_BUTTON);
        filter.addAction(CBK_EVENT_TAP_ON_PLACES_INFORMATION_BUTTON);
        filter.addAction(CBK_EVENT_CLOSE_BUTTON_CLICKED);
        filter.addAction(CBK_CREATE_MAPWIZEVIEW);
        filter.addAction(CBK_SELECT_PLACE);
        filter.addAction(CBK_SELECT_PLACELIST);
        filter.addAction(CBK_SET_PLACE_STYLE);
        filter.addAction(CBK_GRANT_ACCESS);
        filter.addAction(CBK_UNSELECT_CONTENT);

        LocalBroadcastManager.getInstance(cordova.getActivity()).registerReceiver(mCbkReceiver, filter);
        cordova.setActivityResultCallback(this);
        ApiManager.initManager(cordova.getActivity());
    }

    public void onActivityResult(int requestCode, int resultCode,
                                 Intent intent) {
        Log.d(TAG, "onActivityResult...");

        super.onActivityResult(requestCode, resultCode, intent);
    }

    /**
     * Returns the api key stored in AndroidManifest.xml
     * @return
     */
    String getApiKey() {
        try{
            ManifestMetadata metadata = ManifestMetadata.get (cordova.getActivity().getApplicationContext());
            if (metadata != null) {
                String value = metadata.getValue ("MWZMAPWIZEAPIKEY");
                Log.d(TAG, "MapwizeCordovaPlugin, MWZMAPWIZEAPIKEY: " + value);
                return value;
            }
        } catch(NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param action          The action to execute.
     * @param args            The exec() arguments.
     * @param callbackContext The callback context used when calling back into JavaScript.
     * @return
     */
    public boolean execute(final String action, JSONArray args,
                           CallbackContext callbackContext) {
        // Shows a toast
        Log.d(TAG, "MapwizeCordovaPlugin received: " + action);
        if (ACTION_CREATE_MAPWIZE_VIEW.equals(action)) {
            Log.d(TAG, "MapwizeCordovaPlugin::ACTION_CREATE_MAPWIZE_VIEW received: ");
            createMapwizeView(args, callbackContext);

        } else if (ACTION_MAPWIZE_SETCALLBACK.equals(action)) {
            Log.d(TAG, "MapwizeCordovaPlugin::ACTION_MAPWIZE_SETCALLBACK received: ");
            setCallback(callbackContext);

        } else if (ACTION_MAPWIZE_SELECTPLACE.equals(action)) {
            Log.d(TAG, "MapwizeCordovaPlugin::ACTION_MAPWIZE_SELECTPLACE received: ");
            selectPlace(args, callbackContext);
        } else if (ACTION_MAPWIZE_SELECTPLACELIST.equals(action)) {
            Log.d(TAG, "MapwizeCordovaPlugin::ACTION_MAPWIZE_SELECTPLACELIST received: ");
            selectPlaceList(args, callbackContext);

        } else if (ACTION_MAPWIZE_GET_DIRECTION.equals(action)) {
            Log.d(TAG, "MapwizeCordovaPlugin::ACTION_MAPWIZE_GET_DIRECTION received: ");
            getDirection(args, callbackContext);
        } else if (ACTION_MAPWIZE_SET_DIRECTION.equals(action)) {
            Log.d(TAG, "MapwizeCordovaPlugin::ACTION_MAPWIZE_SET_DIRECTION received: ");
            setDirection(args, callbackContext);

        } else if (ACTION_MAPWIZE_GRANTACCESS.equals(action)) {
            Log.d(TAG, "MapwizeCordovaPlugin::ACTION_MAPWIZE_GRANTACCESS received: ");
            grantAccess(args, callbackContext);

        } else if (ACTION_MAPWIZE_UNSELECT_CONTENT.equals(action)) {
            Log.d(TAG, "MapwizeCordovaPlugin::ACTION_MAPWIZE_UNSELECT_CONTENT received: ");
            unselectContent(args, callbackContext);

        } else if (ACTION_MAPWIZE_CLOSE.equals(action)) {
            Log.d(TAG, "MapwizeCordovaPlugin::ACTION_MAPWIZE_CLOSE received: ");
            closeMapwizeView();

        } else if (ACTION_MAPWIZE_SETPLACESTYLE.equals(action)) {
            Log.d(TAG, "MapwizeCordovaPlugin::ACTION_MAPWIZE_SETPLACESTYLE received: ");
            setPlaceStyle(args, callbackContext);
        } else if (ACTION_OFFLINEMANAGER_INIT.equals(action)) {
            Log.d(TAG, "MapwizeCordovaPlugin::ACTION_OFFLINEMANAGER_INIT received: ");
            initOfflineManager(args, callbackContext);
        } else if (ACTION_OFFLINEMANAGER_REMOVE_DATA_FOR_VENUE.equals(action)) {
            Log.d(TAG, "MapwizeCordovaPlugin::ACTION_OFFLINEMANAGER_REMOVE_DATA_FOR_VENUE received: ");
            removeDataForVenue(args, callbackContext);

        } else if (ACTION_OFFLINEMANAGER_DOWNLOAD_DATA_FOR_VENUE.equals(action)) {
            Log.d(TAG, "MapwizeCordovaPlugin::ACTION_OFFLINEMANAGER_DOWNLOAD_DATA_FOR_VENUE received: ");
            downloadDataForVenue(args, callbackContext);

        } else if (ACTION_OFFLINEMANAGER_IS_OFFLINE_FOR_VENUE.equals(action)) {
            Log.d(TAG, "MapwizeCordovaPlugin::ACTION_OFFLINEMANAGER_IS_OFFLINE_FOR_VENUE received: ");
            isOfflineForVenue(args, callbackContext);

        } else if (ACTION_OFFLINEMANAGER_GET_OFFLINE_VENUES.equals(action)) {
            Log.d(TAG, "MapwizeCordovaPlugin::ACTION_OFFLINEMANAGER_GET_OFFLINE_VENUES received: ");
            getOfflineVenues(callbackContext);

        } else if (ACTION_OFFLINEMANAGER_GET_OFFLINE_UNIVERSES_FOR_VENUE.equals(action)) {
            Log.d(TAG, "MapwizeCordovaPlugin::ACTION_OFFLINEMANAGER_GET_OFFLINE_UNIVERSES_FOR_VENUE received: ");
            getOfflineUniversesForVenue(args, callbackContext);

        } else if (ACTION_API_GET_PLACE_WITH_ALIAS.equals(action)) {
            Log.d(TAG, "MapwizeCordovaPlugin::ACTION_API_GET_PLACE_WITH_ALIAS received: ");
            getPlaceWithAlias(args, callbackContext);

        } else if (ACTION_API_GETVENUEWITHID.equals(action)) {
            Log.d(TAG, "MapwizeCordovaPlugin::ACTION_API_GETVENUEWITHID received: ");
            getVenueWithId(args, callbackContext);

        } else if (ACTION_API_GETVENUESWITHFILTER.equals(action)) {
            Log.d(TAG, "MapwizeCordovaPlugin::if received: ");
            getVenuesWithFilter(args, callbackContext);

        } else if (ACTION_API_GETVENUEWITHNAME.equals(action)) {
            Log.d(TAG, "MapwizeCordovaPlugin::ACTION_API_GETVENUEWITHNAME received: ");
            getVenueWithName(args, callbackContext);

        } else if (ACTION_API_GETVENUEWITHALIAS.equals(action)) {
            Log.d(TAG, "MapwizeCordovaPlugin::ACTION_API_GETVENUEWITHALIAS received: ");
            getVenueWithAlias(args, callbackContext);

        } else if (ACTION_API_GETPLACEWITHID.equals(action)) {
            Log.d(TAG, "MapwizeCordovaPlugin::ACTION_API_GETPLACEWITHID received: ");
            getPlaceWithId(args, callbackContext);

        } else if (ACTION_API_GETPLACEWITHNAME.equals(action)) {
            Log.d(TAG, "MapwizeCordovaPlugin::ACTION_API_GETPLACEWITHNAME received: ");
            getPlaceWithName(args, callbackContext);

        } else if (ACTION_API_GETPLACEWITHALIAS.equals(action)) {
            Log.d(TAG, "MapwizeCordovaPlugin::ACTION_API_GETPLACEWITHALIAS received: ");
            getPlaceWithAlias(args, callbackContext);

        } else if (ACTION_API_GETPLACESWITHFILTER.equals(action)) {
            Log.d(TAG, "MapwizeCordovaPlugin::if received: ");
            getPlacesWithFilter(args, callbackContext);

        } else if (ACTION_API_GETPLACELISTWITHID.equals(action)) {
            Log.d(TAG, "MapwizeCordovaPlugin::if  received: ");
            getPlaceListWithId(args, callbackContext);

        } else if (ACTION_API_GETPLACELISTWITHNAME.equals(action)) {
            Log.d(TAG, "MapwizeCordovaPlugin::if received: ");
            getPlaceListWithName(args, callbackContext);

        } else if (ACTION_API_GETPLACELISTWITHALIAS.equals(action)) {
            Log.d(TAG, "MapwizeCordovaPlugin::if received: ");
            getPlaceListWithAlias(args, callbackContext);

        } else if (ACTION_API_GETPLACELISTSWITHFILTER.equals(action)) {
            Log.d(TAG, "MapwizeCordovaPlugin::if received: ");
            getPlaceListsWithFilter(args, callbackContext);

        } else if (ACTION_API_GETUNIVERSEWITHID.equals(action)) {
            Log.d(TAG, "MapwizeCordovaPlugin::ACTION_API_GETUNIVERSEWITHID received: ");
            getUniverseWithId(args, callbackContext);

        } else if (ACTION_API_GETUNIVERSESWITHFILTER.equals(action)) {
            Log.d(TAG, "MapwizeCordovaPlugin::if received: ");
            getUniversesWithFilter(args, callbackContext);

        } else if (ACTION_API_GETACCESSIBLEUNIVERSESWITHVENUE.equals(action)) {
            Log.d(TAG, "MapwizeCordovaPlugin::else received: ");
            getAccessibleUniversesWithVenue(args, callbackContext);

        } else if (ACTION_API_SEARCHWITHPARAMS.equals(action)) {
            Log.d(TAG, "MapwizeCordovaPlugin::ACTION_API_SEARCHWITHPARAMS received: ");
            searchWithParams(args, callbackContext);

        } else if (ACTION_API_GETDIRECTIONWITHFROM.equals(action)) {
            Log.d(TAG, "MapwizeCordovaPlugin::ACTION_API_GETDIRECTIONWITHFROM received: ");
            getDirectionWithFrom(args, callbackContext);

        } else if (ACTION_API_GETDIRECTIONWITHDIRECTIONPOINTSFROM.equals(action)) {
            Log.d(TAG, "MapwizeCordovaPlugin::ACTION_API_GETDIRECTIONWITHDIRECTIONPOINTSFROM received: ");
            getDirectionWithDirectionPointsFrom(args, callbackContext);

        } else if (ACTION_API_GETDIRECTIONWITHWAYPOINTSFROM.equals(action)) {
            Log.d(TAG, "MapwizeCordovaPlugin::ACTION_API_GETDIRECTIONWITHWAYPOINTSFROM received: ");
            getDirectionWithWayPointsFrom(args, callbackContext);

        } else if (ACTION_API_GETDIRECTIONWITHDIRECTIONANDWAYPOINTSFROM.equals(action)) {
            Log.d(TAG, "MapwizeCordovaPlugin::ACTION_API_GETDIRECTIONWITHDIRECTIONANDWAYPOINTSFROM received: ");
            getDirectionWithDirectionAndWayPointsFrom(args, callbackContext);

        } else if (ACTION_API_GETDISTANCESWITHFROM.equals(action)) {
            Log.d(TAG, "MapwizeCordovaPlugin::ACTION_API_GETDISTANCESWITHFROM received: ");
            getDistancesWithFrom(args, callbackContext);
            
        } else {
            Log.d(TAG, String.format("Action is not handled %s ", action));
        }

        return true;
    }

    /**
     * Launches the activity for MapwizeView
     * @param args
     * @param context
     */
    void createMapwizeView(JSONArray args, CallbackContext context) {
        try {
            String apiKey = getApiKey();
            String optionsStr = args.getString(0);
            String uiSettingsStr = args.getString(1);

            mCreateMapwizeViewCallback = context;

            mMapwizeViewIntent = new Intent(cordova.getActivity().getApplication().getApplicationContext(), MapwizeActivity.class);
            mMapwizeViewIntent.putExtra(OPTIONS_STR, optionsStr);
            mMapwizeViewIntent.putExtra(UISETTINGS_STR, uiSettingsStr);

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    cordova.getActivity().startActivityForResult(mMapwizeViewIntent, MapwizeCordovaPlugin.MAPWIZEVIEW_REQUEST_ID);
                    sendCallbackCmdOK(null, mCreateMapwizeViewCallback);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
            sendCallbackCmdErr("Failed to create MapwizeView", "Failed to create MapwizeView", mCreateMapwizeViewCallback);
        }
    }

    /**
     * Sets the CallbackContext for callback functions of the JS part
     * @param context
     */
    void setCallback(CallbackContext context) {
        Log.d(TAG, "MapwizeCordovaPlugin setCallback...");
        mCallback = context;
        PluginResult result = new PluginResult(PluginResult.Status.OK);
        result.setKeepCallback(true);
        mCallback.sendPluginResult(result);
    }

    /**
     * Select place
     * @param args          array of arguments: 
     *                      [0]: id of the place string
     *                      [1]: if the selection assign to the center of the screen boolean
     * @param context       callback context
     */
    void selectPlace(JSONArray args, CallbackContext context) {
        try {
            String id = args.getString(0);
            Boolean centerOn = args.getBoolean(1);
            mSelectPlaceCallback = context;

            Log.d(TAG, "selectPlace, id: " + id);

            Intent intent = new Intent(CMD_SELECT_PLACE);
            intent.putExtra(CMD_SELECT_PLACE_ID, id);
            intent.putExtra(CMD_SELECT_PLACE_CENTERON, centerOn);
            LocalBroadcastManager.getInstance(cordova.getActivity()).sendBroadcast(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the place style
     * @param args          array of arguments: 
     *                      [0]: id of the place string
     *                      [1]: serialized style string
     * @param context       callback context
     */
    void setPlaceStyle(JSONArray args, CallbackContext context) {
        try {
            String id = args.getString(0);
            String style = args.getString(1);
            mSetPlaceStyleCallback = context;

            Log.d(TAG, "setPlace, id: " + id);

            Intent intent = new Intent(CMD_SET_PLACE_STYLE);
            intent.putExtra(CMD_SET_PLACE_STYLE_ID, id);
            intent.putExtra(CMD_SET_PLACE_STYLE_STYLE, style);
            LocalBroadcastManager.getInstance(cordova.getActivity()).sendBroadcast(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Selects the placelist
     * @param args          array of arguments: 
     *                      [0]: id of the placelist string
     * @param context       callback context
     */
    void selectPlaceList(JSONArray args, CallbackContext context) {
        try {
            String id = args.getString(0);
            mSelectPlaceListCallback = context;

            Intent intent = new Intent(CMD_SELECT_PLACELIST);
            intent.putExtra(CMD_SELECT_PLACELIST_ID, id);
            LocalBroadcastManager.getInstance(cordova.getActivity()).sendBroadcast(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Gets the direction
     * @param args          array of arguments: 
     *                      [0]: serialized directionpoint from string
     *                      [1]: serialized directionpoint to string
     *                      [2]: considers accessibility boolean
     * @param context       callback context
     */
    void getDirection(JSONArray args, CallbackContext context) {
        String from = args.optString(0, "");
        String to = args.optString(1, "");
        boolean isAccessible = args.optBoolean(2, false);
        mGetDirectionCallback = context;
        Intent intent = new Intent(CMD_GET_DIRECTION);
        intent.putExtra(CMD_GET_DIRECTION_FROM, from);
        intent.putExtra(CMD_GET_DIRECTION_TO, to);
        intent.putExtra(CMD_GET_DIRECTION_ISACCESSIBLE, isAccessible);
        LocalBroadcastManager.getInstance(cordova.getActivity()).sendBroadcast(intent);
    }

    /**
     * Displays the direction
     * @param args          array of arguments: 
     *                      [0]: serialized direction string
     *                      [1]: serialized directionpoint from string
     *                      [2]: serialized directionpoint to string
     *                      [3]: considers accessibility boolean
     * @param context       callback context
     */
    void setDirection(JSONArray args, CallbackContext context) {
        String directionStr = args.optString(0, "");
        Log.d(TAG, "The directionStr: " + directionStr);
        String fromStr = args.optString(1, "");
        String toStr = args.optString(2, "");
        boolean isAccessible = args.optBoolean(3, false);
        mGetDirectionCallback = context;
        Intent intent = new Intent(CMD_SET_DIRECTION);
        intent.putExtra(CMD_SET_DIRECTION_DIRECTION, directionStr);
        intent.putExtra(CMD_SET_DIRECTION_FROM, fromStr);
        intent.putExtra(CMD_SET_DIRECTION_TO, toStr);
        intent.putExtra(CMD_SET_DIRECTION_ISACCESSIBLE, isAccessible);
        LocalBroadcastManager.getInstance(cordova.getActivity()).sendBroadcast(intent);
    }

    /**
     * Delegates the grantAccess function to MapwizeView
     * @param args
     * @param context
     */
    void grantAccess(JSONArray args, CallbackContext context) {
        try {
            String token = args.getString(0);
            mGrantAccessCallback = context;

            Intent intent = new Intent(cordova.getActivity().getApplication().getApplicationContext(), MapwizeActivity.class);
            intent.putExtra(CMD, CMD_GRANT_ACCESS);
            intent.putExtra(CMD_GRANT_ACCESS_TOKEN, token);
            cordova.getActivity().startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delegates the unselectContent function to MapwizeView
     */
    void closeMapwizeView() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                cordova.getActivity().finishActivity(MAPWIZEVIEW_REQUEST_ID);
                PluginResult result = new PluginResult(PluginResult.Status.OK);
                context.sendPluginResult(result);
            }
        });
    }

    /**
     * Delegates the unselectContent function to MapwizeView
     * @param args
     * @param context
     */
    void unselectContent(JSONArray args, CallbackContext context) {
        mCallback = context;

        PluginResult result = new PluginResult(PluginResult.Status.OK);
        result.setKeepCallback(true);
        mCallback.sendPluginResult(result);
    }

    /**
     * Sends the callbackEvent to JS
     * @param status OK or ERROR
     * @param event  The event for the callback
     * @param args   JSON that contains the argument(s)
     */
    void sendCallbackEvent(PluginResult.Status status, String event, String args) {
        cordova.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                JSONObject json = new JSONObject();
                try {
                    json.put(CBK_FIELD_EVENT, event);
                    if (args != null) {
                        json.put(CBK_FIELD_ARG, args);
                    }

                    PluginResult result = new PluginResult(status, json);
                    result.setKeepCallback(true);
                    mCallback.sendPluginResult(result);
                } catch (JSONException e) {
                    PluginResult result = new PluginResult(PluginResult.Status.ERROR);
                    result.setKeepCallback(true);
                    mCallback.sendPluginResult(result);
                }
            }
        });
    }

    /**
     * Sends the successfull callbackEvent to JS
     * @param event  The event for the callback
     * @param args   JSON that contains the argument(s)
     */
    void sendCallbackEventOK(String event, String args) {
        sendCallbackEvent(PluginResult.Status.OK, event, args);
    }

    /**
     * Sends the successfull callbackEvent to JS
     * @param event  The event for the callback
     */
    void sendCallbackEventOK(String event) {
        sendCallbackEvent(PluginResult.Status.OK, event, null);
    }

    /**
     * Sends the callback command back to JS according to the intent.
     * @param intent    Intent returned from MapwizeView
     * @param context   CallbackContext to use for returning Cordova result
     */
    void sendCallbackCmd(Intent intent, CallbackContext context) {
        Boolean success = intent.getBooleanExtra(CMD_SUCCESS, false);
        String  args = intent.getStringExtra(CMD_ARGS);
        JSONObject json = new JSONObject();
        try {
            json.put(CBK_FIELD_EVENT, intent.getAction());
            if (args != null && !"".equals(args)) {
                json.put(CBK_FIELD_ARG, args);
            }

            PluginResult result = new PluginResult(success ? PluginResult.Status.OK : PluginResult.Status.ERROR, json);
            context.sendPluginResult(result);
        } catch (JSONException e) {
            PluginResult result = new PluginResult(PluginResult.Status.ERROR);
            context.sendPluginResult(result);
        }
    }

    /**
     * Sends the failed callbackEvent to JS
     * @param message  Error message
     * @param locMessage  Localized error message
     * @param context   CallbackContext to use for returning Cordova result
     */
    void sendCallbackCmdErr(String message, String locMessage, CallbackContext context) {
        JSONObject json = new JSONObject();
        try {
            json.put(CBK_FIELD_ERR_MESSAGE, message);
            json.put(CBK_FIELD_ERR_LOCALIZED_MESSAGE, locMessage);

            PluginResult result = new PluginResult(PluginResult.Status.ERROR, json);
            context.sendPluginResult(result);
        } catch (JSONException e) {
            e.printStackTrace();
            PluginResult result = new PluginResult(PluginResult.Status.ERROR);
            context.sendPluginResult(result);
        }
    }

    /**
     * Sends the failed callbackEvent to JS
     * @param args   JSON that contains the argument(s)
     * @param context  @param context   CallbackContext to use for returning Cordova result
     */
    void sendCallbackCmdOK(JSONObject args, CallbackContext context) {
        PluginResult result;

        if (args == null) {
            result = new PluginResult(PluginResult.Status.OK);
        } else {
            result = new PluginResult(PluginResult.Status.OK, args);
        }

        context.sendPluginResult(result);
    }

    /**
     * Broadcast receiver for return values from MapwizeView
     */
    public class CbkReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "CbkReceiver onReceive...");
            final String action = intent.getAction();
            if (CBK_MENU_BUTTONCLICK.equals(action)) {
                Log.d(TAG, "Received: CBK_MENU_BUTTONCLICK");
                sendCallbackEventOK(CBK_EVENT_DID_TAP_ON_MENU);
            } else if (CBK_EVENT_TAP_ON_PLACE_INFORMATION_BUTTON.equals(action)) {
                Log.d(TAG, "Received: CBK_EVENT_TAP_ON_PLACE_INFORMATION_BUTTON");
                String json = intent.getStringExtra(CBK_ARGS);
                sendCallbackEventOK(CBK_EVENT_TAP_ON_PLACE_INFORMATION_BUTTON, json);
            } else if (CBK_EVENT_TAP_ON_PLACES_INFORMATION_BUTTON.equals(action)) {
                Log.d(TAG, "Received: CBK_EVENT_TAP_ON_PLACES_INFORMATION_BUTTON");
                String json = intent.getStringExtra(CBK_ARGS);
                sendCallbackEventOK(CBK_EVENT_TAP_ON_PLACES_INFORMATION_BUTTON, json);
            } else if (CBK_EVENT_DID_LOAD.equals(action)) {
                Log.d(TAG, "Received: CBK_EVENT_DID_LOAD");
                sendCallbackEventOK(CBK_EVENT_DID_LOAD);
            } else if (CBK_EVENT_DID_TAP_ON_FOLLOW_WITHOUT_LOCATION.equals(action)) {
                Log.d(TAG, "Received: CBK_EVENT_DID_TAP_ON_FOLLOW_WITHOUT_LOCATION");
                String json = intent.getStringExtra(CBK_ARGS);
                sendCallbackEventOK(CBK_EVENT_DID_TAP_ON_FOLLOW_WITHOUT_LOCATION, json);
            } else if (CBK_EVENT_CLOSE_BUTTON_CLICKED.equals(action)) {
                Log.d(TAG, "Received: CBK_EVENT_DID_TAP_ON_FOLLOW_WITHOUT_LOCATION");
                String json = intent.getStringExtra(CBK_ARGS);
                sendCallbackEventOK(CBK_EVENT_CLOSE_BUTTON_CLICKED, json);
            } else if (CBK_SELECT_PLACE.equals(action)) {
                Log.d(TAG, "Received: CBK_SELECT_PLACE");
                sendCallbackCmd(intent, mSelectPlaceCallback);
            } else if (CBK_SET_PLACE_STYLE.equals(action)) {
                Log.d(TAG, "Received: CBK_SET_PLACE_STYLE");
                sendCallbackCmd(intent, mSetPlaceStyleCallback);
            } else if (CBK_SELECT_PLACELIST.equals(action)) {
                Log.d(TAG, "Received: CBK_SELECT_PLACELIST");
                sendCallbackCmd(intent, mSelectPlaceListCallback);
            } else if (CBK_GRANT_ACCESS.equals(action)) {
                Log.d(TAG, "Received: CBK_GRANT_ACCESS");
                sendCallbackCmd(intent, mGrantAccessCallback);
            } else if (CBK_CREATE_MAPWIZEVIEW.equals(action)) {
                Log.d(TAG, "Received: CBK_CREATE_MAPWIZEVIEW");
                sendCallbackCmd(intent, mCreateMapwizeViewCallback);
            } else {
                Log.d(TAG, "Received: action not recognized action: " + action);
            }
        }
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
        Log.d(TAG, "sendCmdEvent, action: " + action + ", success: " + success);
        Intent intent = new Intent(action);
        intent.putExtra(CMD_SUCCESS, success);
        if(args != null) {
            intent.putExtra(CMD_ARGS, args);
        }
        LocalBroadcastManager.getInstance(cordova.getActivity()).sendBroadcast(intent);
    }

    // OfflineManager

    /**
     * Delegates the initOfflineManager function to OfflineManager
     * @param  args          array of arguments: 
     *                       [0]: styleUrl string
     * @param  context       callback context
     */
    void initOfflineManager(JSONArray args, CallbackContext context) {
        try {
            String styleUrl = args.getString(0);
            mOfflineManager = new OfflineManager(cordova.getActivity(), styleUrl);
        } catch (JSONException e) {
            sendCallbackCmdErr("Init failed", "Init failed", context);
        }
    }

    /**
     * Removes data for the venue of the universe
     * @param  args          array of arguments: 
     *                       [0]: venueId string
     *                       [1]: universeId string
     * @param  context       callback context
     */
    void removeDataForVenue(JSONArray args, CallbackContext context) {
        try {
            String venueId = args.getString(0);
            String universeId = args.getString(1);
            mOfflineManager.removeDataForVenue(venueId, universeId, context);
        } catch(JSONException e) {
            sendCallbackCmdErr("Error", "error", context);
        }
    }

    /**
     * Download data for the venue of the given universe
     * @param  args          array of arguments: 
     *                       [0]: venueId string
     *                       [1]: universeId string
     * @param  context       callback context
     */
    void downloadDataForVenue(JSONArray args, CallbackContext context) {
        try {
            String venueId = args.getString(0);
            String universeId = args.getString(1);
            mOfflineManager.downloadDataForVenue(venueId, universeId, context);
        } catch(JSONException e) {
            sendCallbackCmdErr("Error", "error", context);
        }
    }

    /**
     * Check if the venue is offline
     * @param args          array of arguments: 
     *                      [0]: venueId string
     *                      [1]: universeId string
     * @param context       callback context
     */
    void isOfflineForVenue(JSONArray args, CallbackContext context) {
        try {
            String venueId = args.getString(0);
            String universeId = args.getString(1);
            mOfflineManager.isOfflineForVenue(venueId, universeId, context);
        } catch(JSONException e) {
            sendCallbackCmdErr("Error", "error", context);
        }
    }

    /**
     * Gets the offline venues
     * @param context       callback context
     */
    void getOfflineVenues(CallbackContext context) {
        mOfflineManager.getOfflineVenues(context);
    }

    /**
     * Gets the list of offline universes for the venue
     * @param args          array of arguments: 
     *                      [0]: venueId string
     * @param context       callback context
     */
    void getOfflineUniversesForVenue(JSONArray args, CallbackContext context) {
        try {
            String venueId = args.getString(0);
            mOfflineManager.getOfflineUniversesForVenue(venueId, context);
        } catch(JSONException e) {
            sendCallbackCmdErr("Error", "error", context);
        }
    }

    /**
     * Gets the place for an alias
     * @param args          array of arguments: 
     *                      [0]: alias string
     *                      [1]: venueId string
     * @param context       callback context
     */
    void getPlaceWithAlias(JSONArray args, CallbackContext context) {
        try {
            String alias = args.getString(0);
            String venueId = args.getString(1);
            ApiManager.getPlaceWithAlias(alias, venueId, context);
        } catch(JSONException e) {
            sendCallbackCmdErr("Error", "error", context);
        }
    }

    /**
     * Gets the venue with an id
     * @param args          array of arguments: 
     *                      [0]: venueId string
     * @param context       callback context
     */
    void getVenueWithId(JSONArray args, CallbackContext context) {
        try {
            String venueId = args.getString(0);
            ApiManager.getVenueWithId(venueId, context);
        } catch(JSONException e) {
            sendCallbackCmdErr("Error", "error", context);
        }
    }

    /**
     * Gets the venues fulfilling the api filter
     * @param args          array of arguments: 
     *                      [0]: serialized api filter string
     * @param context       callback context
     */
    void getVenuesWithFilter(JSONArray args, CallbackContext context) {
        try {
            String filterStr = args.getString(0);
            ApiManager.getVenuesWithFilter(filterStr, context);
        } catch(JSONException e) {
            sendCallbackCmdErr("Error", "error", context);
        }
    }

    /**
     * Gets the venue for a name
     * @param args          array of arguments: 
     *                      [0]: name of the venue string
     * @param context       callback context
     */
    void getVenueWithName(JSONArray args, CallbackContext context) {
        try {
            String name = args.getString(0);
            ApiManager.getVenueWithName(name, context);
        } catch(JSONException e) {
            sendCallbackCmdErr("Error", "error", context);
        }
    }

    /**
     * Gets the venue for a name
     * @param args          array of arguments: 
     *                      [0]: name of the venue string
     * @param context       callback context
     */
    void getVenueWithAlias(JSONArray args, CallbackContext context) {
        try {
            String alias = args.getString(0);
            ApiManager.getVenueWithAlias(alias, context);
        } catch(JSONException e) {
            sendCallbackCmdErr("Error", "error", context);
        }
    }

    /**
     * Gets the place with an id
     * @param args          array of arguments:
     *                      [0]: place id string
     * @param context       callback context
     */
    void getPlaceWithId(JSONArray args, CallbackContext context) {
        try {
            Log.d(TAG, "getPlaceWithId: " + args.toString());
            String placeId = args.getString(0);
            ApiManager.getPlaceWithId(placeId, context);
        } catch(JSONException e) {
            sendCallbackCmdErr("Error", "error", context);
        }
    }

    /**
     * Gets the place with a name
     * @param args          array of arguments: 
     *                      [0]: name string 
     *                      [1]: venue id string
     * @param context       callback context
     */
    void getPlaceWithName(JSONArray args, CallbackContext context) {
        try {
            String name = args.getString(0);
            String venueId = args.getString(1);
            ApiManager.getPlaceWithName(name, venueId, context);
        } catch(JSONException e) {
            sendCallbackCmdErr("Error", "error", context);
        }
    }

    /**
     * Gets the places fulfilling the api filter
     * @param args          array of arguments:
     *                      [0]: serialized api filter string
     * @param context       callback context
     */
    void getPlacesWithFilter(JSONArray args, CallbackContext context) {
        try {
            String filterStr = args.getString(0);
            ApiManager.getPlacesWithFilter(filterStr, context);
        } catch(JSONException e) {
            sendCallbackCmdErr("Error", "error", context);
        }
    }

    /**
     * Gets the place list with the id
     * @param args          array of arguments: 
     *                      [0]: id string
     * @param context       callback context
     */
    void getPlaceListWithId(JSONArray args, CallbackContext context) {
        try {
            String placeListId = args.getString(0);
            ApiManager.getPlaceListWithId(placeListId, context);
        } catch(JSONException e) {
            sendCallbackCmdErr("Error", "error", context);
        }
    }

    /**
     * Gets the place list with the name
     * @param args          array of arguments:
     *                      [0]: name of the placelist string
     *                      [1]: venueId of the placelist string
     * @param context       callback context
     */
    void getPlaceListWithName(JSONArray args, CallbackContext context) {
        try {
            String name = args.getString(0);
            String venueId = args.getString(1);
            ApiManager.getPlaceListWithName(name, venueId, context);
        } catch(JSONException e) {
            sendCallbackCmdErr("Error", "error", context);
        }
    }

    /**
     * Gets the place list with the alias
     * @param args          array of arguments: 
     *                      [0]: alias string 
     *                      [1]: venue id string
     * @param context       callback context
     */
    void getPlaceListWithAlias(JSONArray args, CallbackContext context) {
        try {
            String alias = args.getString(0);
            String venueId = args.getString(1);
            ApiManager.getPlaceListWithAlias(alias, venueId, context);
        } catch(JSONException e) {
            sendCallbackCmdErr("Error", "error", context);
        }
    }

    /**
     * Gets the list of placelists fulfills the api filter
     * @param args          array of arguments: 
     *                      [0]: serialized api filter string
     * @param context       callback context
     */
    void getPlaceListsWithFilter(JSONArray args, CallbackContext context) {
        try {
            String fileStr = args.getString(0);
            ApiManager.getPlaceListsWithFilter(fileStr, context);
        } catch(JSONException e) {
            sendCallbackCmdErr("Error", "error", context);
        }
    }

    /**
     * Gets the universe with id
     * @param args          array of arguments: 
     *                      [0]: universe id string
     * @param context       callback context
     */
    void getUniverseWithId(JSONArray args, CallbackContext context) {
        try {
            String universeId = args.getString(0);
            ApiManager.getUniverseWithId(universeId, context);
        } catch(JSONException e) {
            sendCallbackCmdErr("Error", "error", context);
        }
    }

    /**
     * Gets the list of universes fulfills the api filter
     * @param args          array of arguments:
     *                      [0]: serialized api filter string
     * @param context       callback context
     */
    void getUniversesWithFilter(JSONArray args, CallbackContext context) {
        try {
            String filterStr = args.getString(0);
            ApiManager.getUniversesWithFilter(filterStr, context);
        } catch(JSONException e) {
            sendCallbackCmdErr("Error", "error", context);
        }
    }

    /**
     * Gets the list of accessible universes of a venue
     * @param args          array of arguments:
     *                      [0]: venue id string
     * @param context       callback context
     */
    void getAccessibleUniversesWithVenue(JSONArray args, CallbackContext context) {
        try {
            String venueId = args.getString(0);
            ApiManager.getAccessibleUniversesWithVenue(venueId, context);
        } catch(JSONException e) {
            sendCallbackCmdErr("Error", "error", context);
        }
    }

    /**
     * Search with params
     * @param args          array of arguments:
     *                      [0]: serialized search params string
     * @param context       callback context
     */
    void searchWithParams(JSONArray args, CallbackContext context) {
        try {
            String searchWithParams = args.getString(0);
            ApiManager.searchWithParams(searchWithParams, context);
        } catch(JSONException e) {
            sendCallbackCmdErr("Error", "error", context);
        }
    }

    /**
     * Gets a direction from two direction points
     * @param args          array of arguments:
     *                      [0]: serialized from direction point string
     *                      [1]: serialized to direction point string
     *                      [2]: considers accessibility
     * @param context       callback context
     */
    void getDirectionWithFrom(JSONArray args, CallbackContext context) {
        try {
            String directionPointFrom = args.getString(0);
            String directionPointTo = args.getString(1);
            boolean isAccessible = args.getBoolean(2);
            ApiManager.getDirectionWithFrom(directionPointFrom, directionPointTo, isAccessible, context);
        } catch(JSONException e) {
            Log.d(TAG, "getDirectionWithFrom, error: " + e.getLocalizedMessage());
            sendCallbackCmdErr("Error", "error", context);
        }
    }

    /**
     * Gets the list of directions starts from a direction point 
     * ends with one of the direction points a list contains.
     * @param args          array of arguments:
     *                      [0]: serialized from direction point string
     *                      [1]: serialized to list of direction point string
     *                      [2]: considers accessibility
     * @param context       callback context
     */
    void getDirectionWithDirectionPointsFrom(JSONArray args, CallbackContext context) {
        try {
            String directionPointFrom = args.getString(0);
            String directionPointTo = args.getString(1);
            boolean isAccessible = args.getBoolean(2);
            ApiManager.getDirectionWithDirectionPointsFrom(directionPointFrom, directionPointTo, isAccessible, context);
        } catch(JSONException e) {
            sendCallbackCmdErr("Error", "error", context);
        }
    }

    /**
     * Gets the list of directions starts from a direction point 
     * ends with the to direction point and reaching one of the waypoints.
     * @param args          array of arguments:
     *                      [0]: serialized from direction point string
     *                      [1]: serialized to direction point string
     *                      [2]: serialized list of waypoints string
     *                      [3]: considers accessibility
     * @param context       callback context
     */
    void getDirectionWithWayPointsFrom(JSONArray args, CallbackContext context) {
        Log.d(TAG, "getDirectionWithWayPointsFrom...");
        try {
            String directionPointFrom = args.getString(0);
            String directionPointTo = args.getString(1);
            String wayPointToList = args.getString(2);
            boolean isAccessible = args.getBoolean(3);
            Log.d(TAG, "calling, ApiManager.getDirectionWithWayPointsFrom...");
            ApiManager.getDirectionWithWayPointsFrom(directionPointFrom, directionPointTo, wayPointToList, isAccessible, context);
        } catch(JSONException e) {
            Log.d(TAG, "getDirectionWithWayPointsFrom...JSONException: " + e.getLocalizedMessage());
            sendCallbackCmdErr("Error", "error", context);
        }
    }

    /**
     * Gets the list of directions starts from a direction point 
     * ends with one of the direction points and reaching one of the waypoints.
     * @param args          array of arguments:
     *                      [0]: serialized from direction point string
     *                      [1]: serialized list of "to" direction points string
     *                      [2]: serialized list of waypoints string
     *                      [3]: considers accessibility
     * @param context       callback context
     */
    void getDirectionWithDirectionAndWayPointsFrom(JSONArray args, CallbackContext context) {
        try {
            String directionPointFrom = args.getString(0);
            String directionPointToList = args.getString(1);
            String wayPointToList = args.getString(2);
            boolean isAccessible = args.getBoolean(3);
            ApiManager.getDirectionWithDirectionAndWayPointsFrom(directionPointFrom, directionPointToList, wayPointToList, isAccessible, context);
        } catch(JSONException e) {
            sendCallbackCmdErr("Error", "error", context);
        }
    }

    /**
     * Gets the list of distances for all the directions starts from a direction point 
     * ends with one of the direction points
     * @param args          array of arguments:
     *                      [0]: serialized from direction point string
     *                      [1]: serialized list of "to" direction points string
     *                      [2]: considers accessibility
     *                      [4]: the list is sorted by travel time
     * @param context       callback context
     */
    void getDistancesWithFrom(JSONArray args, CallbackContext context) {
        try {
            String directionPointFrom = args.getString(0);
            String directionPointToList = args.getString(1);
            boolean isAccessible = args.getBoolean(2);
            boolean sortByTraveltime = args.getBoolean(3);
            ApiManager.getDistancesWithFrom(directionPointFrom, directionPointToList, isAccessible, sortByTraveltime, context);
        } catch(JSONException e) {
            sendCallbackCmdErr("Error", "error", context);
        }
    }
}
