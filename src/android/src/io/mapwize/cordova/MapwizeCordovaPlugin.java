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

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mapwize.test.MainActivity;

public class MapwizeCordovaPlugin extends CordovaPlugin {

    private static final String TAG = "MapwizeCordovaPlugin";
    private static final String ACTION_CREATE_MAPWIZE_VIEW = "createMapwizeView";

    private static final String ACTION_MAPWIZE_SETCALLBACK = "setCallback";
    private static final String ACTION_MAPWIZE_SELECTPLACE = "selectPlace";
    private static final String ACTION_MAPWIZE_SELECTPLACELIST = "selectPlaceList";
    private static final String ACTION_MAPWIZE_GRANTACCESS = "grantAccess";
    private static final String ACTION_MAPWIZE_UNSELECT_CONTENT = "unselectContent";

    public static final String OPTIONS_STR = "optionStr";

    public static final String CMD = "cmd";
    public static final String CMD_SELECT_PLACE = "selectPlace";
    public static final String CMD_SELECT_PLACE_ID = "identifier";
    public static final String CMD_SELECT_PLACE_CENTERON = "centerOn";

    public static final String CMD_SELECT_PLACELIST = "selectPlaceList";
    public static final String CMD_SELECT_PLACELIST_ID = "identifier";

    public static final String CMD_GRANT_ACCESS = "grantAccess";
    public static final String CMD_GRANT_ACCESS_TOKEN = "token";
    public static final String CMD_GRANT_ACCESS_SUCCESS = "success";

    public static final String CMD_UNSELECT_CONTENT = "unselectContent";

    public static final String CBK_MENU_BUTTONCLICK = "menuButtonClick";
    public static final String CBK_INFORMATION_BUTTONCLICK = "informationButtonClick";
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

    public static final String CBK_SELECT_PLACE = "selectPlaceCbk";
    public static final String CBK_SELECT_PLACE_ID = "identifier";
    public static final String CBK_SELECT_PLACE_CENTERON = "centerOn";

    public static final String CBK_SELECT_PLACELIST = "selectPlaceListCbk";
    public static final String CBK_SELECT_PLACELIST_ID = "identifier";

    public static final String CBK_GRANT_ACCESS = "grantAccessCbk";
    public static final String CBK_GRANT_ACCESS_TOKEN = "token";
    public static final String CBK_GRANT_ACCESS_SUCCESS = "success";

    public static final String CBK_EVENT_DID_LOAD = "DidLoad";
    public static final String CBK_EVENT_DID_TAP_ON_FOLLOW_WITHOUT_LOCATION = "DidTapOnFollowWithoutLocation";
    public static final String CBK_EVENT_DID_TAP_ON_MENU = "DidTapOnMenu";
    public static final String CBK_EVENT_SHOULD_SHOW_INFORMATION_BUTTON_FOR = "shouldShowInformationButtonFor";
    public static final String CBK_EVENT_TAP_ON_PLACE_INFORMATION_BUTTON = "TapOnPlaceInformationButton";
    public static final String CBK_EVENT_TAP_ON_PLACES_INFORMATION_BUTTON = "TapOnPlacesInformationButton";

    BroadcastReceiver mCbkReceiver;

    private CallbackContext mCallback = null;

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
            io.mapwize.mapwizeformapbox.AccountManager.start(cordova.getActivity().getApplication(), value);
        }

        mCbkReceiver = new CbkReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(CBK_MENU_BUTTONCLICK);
        filter.addAction(CBK_INFORMATION_BUTTONCLICK);
        filter.addAction(CBK_EVENT_DID_LOAD);

        filter.addAction(CMD_SELECT_PLACE);
        filter.addAction(CMD_SELECT_PLACELIST);
        filter.addAction(CMD_GRANT_ACCESS);
        filter.addAction(CMD_UNSELECT_CONTENT);

        LocalBroadcastManager.getInstance(cordova.getActivity()).registerReceiver(mCbkReceiver, filter);
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
     * @throws JSONException
     */
    public boolean execute(final String action, JSONArray args,
                           CallbackContext callbackContext) throws JSONException {
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

        } else if (ACTION_MAPWIZE_GRANTACCESS.equals(action)) {
            Log.d(TAG, "MapwizeCordovaPlugin::ACTION_MAPWIZE_GRANTACCESS received: ");
            grantAccess(args, callbackContext);

        } else if (ACTION_MAPWIZE_UNSELECT_CONTENT.equals(action)) {
            Log.d(TAG, "MapwizeCordovaPlugin::ACTION_MAPWIZE_UNSELECT_CONTENT received: ");
            unselectContent(args, callbackContext);

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
            Intent intent = new Intent(cordova.getActivity().getApplication().getApplicationContext(), MapwizeActivity.class);
            intent.putExtra(OPTIONS_STR, optionsStr);


            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    cordova.getActivity().startActivity(intent);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
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
     * Delegates selectPlace function to MapwizeView
     * @param args
     * @param context
     */
    void selectPlace(JSONArray args, CallbackContext context) {
        try {
            String id = args.getString(0);
            Boolean centerOn = args.getBoolean(1);

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
     * Delegates selectPlaceList function to MapwizeView
     * @param args
     * @param context
     */
    void selectPlaceList(JSONArray args, CallbackContext context) {
        try {
            String id = args.getString(0);

            Intent intent = new Intent(CMD_SELECT_PLACELIST);
            intent.putExtra(CMD_SELECT_PLACELIST_ID, id);
            LocalBroadcastManager.getInstance(cordova.getActivity()).sendBroadcast(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delegates the grantAccess function to MapwizeView
     * @param args
     * @param context
     */
    void grantAccess(JSONArray args, CallbackContext context) {
        try {
            String token = args.getString(0);

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
     * Sends the failed callbackEvent to JS
     * @param event  The event for the callback
     * @param args   JSON that contains the argument(s)
     */
    void sendCallbackEventError(String event, String args) {
        sendCallbackEvent(PluginResult.Status.ERROR, event, args);
    }

    /**
     * Sends the callback command back to JS according to the intent.
     * @param intent    Intent returned from MapwizeView
     */
    void sendCallbackCmd(Intent intent) {
        Boolean success = intent.getBooleanExtra(CMD_SUCCESS, false);
        String  args = intent.getStringExtra(CMD_ARGS);
        JSONObject json = new JSONObject();
        try {
            json.put(CBK_FIELD_EVENT, intent.getAction());
            if (args != null && !"".equals(args)) {
                json.put(CBK_FIELD_ARG, args);
            }

            PluginResult result = new PluginResult(success ? PluginResult.Status.OK : PluginResult.Status.ERROR, json);
            result.setKeepCallback(true);
            mCallback.sendPluginResult(result);
        } catch (JSONException e) {
            PluginResult result = new PluginResult(PluginResult.Status.ERROR);
            result.setKeepCallback(true);
            mCallback.sendPluginResult(result);
        }
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
             } else if (CBK_INFORMATION_BUTTONCLICK.equals(action)) {
                 Log.d(TAG, "Received: CBK_INFORMATION_BUTTONCLICK");
                 String json = intent.getStringExtra(CBK_ARGS);
                 sendCallbackEventOK(CBK_EVENT_TAP_ON_PLACE_INFORMATION_BUTTON, json);
             } else if (CBK_EVENT_DID_LOAD.equals(action)) {
                 Log.d(TAG, "Received: CBK_EVENT_DID_LOAD");
                 sendCallbackEventOK(CBK_EVENT_DID_LOAD);
             } else if (CBK_SELECT_PLACE.equals(action)) {
                 Log.d(TAG, "Received: CBK_SELECT_PLACE");
                 sendCallbackCmd(intent);
             } else if (CMD_SELECT_PLACELIST.equals(action)) {
                 Log.d(TAG, "Received: CBK_ON_FRAGMENT_READY");
                 sendCallbackCmd(intent);
             } else if (CMD_GRANT_ACCESS.equals(action)) {
                 Log.d(TAG, "Received: CBK_ON_FRAGMENT_READY");
                 sendCallbackCmd(intent);
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
        Intent intent = new Intent(action);
        intent.putExtra(CMD_SUCCESS, success);
        if(args != null) {
            intent.putExtra(CMD_ARGS, args);
        }
        LocalBroadcastManager.getInstance(cordova.getActivity()).sendBroadcast(intent);
    }

}
