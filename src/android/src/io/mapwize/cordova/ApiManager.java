package io.mapwize.cordova;

import android.app.Activity;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import androidx.annotation.Nullable;
import io.mapwize.mapwizeformapbox.api.Api;
import io.mapwize.mapwizeformapbox.api.ApiCallback;
import io.mapwize.mapwizeformapbox.api.Place;
import io.mapwize.mapwizeformapbox.api.Universe;
import io.mapwize.mapwizeformapbox.api.Venue;

import static io.mapwize.cordova.MapwizeCordovaPlugin.CBK_FIELD_ARG;

public class ApiManager {
    private static final String TAG = "ApiManager";
    private static Activity sActivity;


    public ApiManager() {
    }

    public static void initManager(Activity activity) {
        sActivity = activity;
    }

    public static void getPlaceWithAlias(String alias, String venueId, CallbackContext context) {
        Log.d(TAG, "getPlaceWithAlias...");
        Api.getVenue(venueId, new ApiCallback<Venue>() {
            @Override
            public void onSuccess(@Nullable Venue venue) {
                Api.getPlaceWithAlias(alias, venue, new ApiCallback<Place>() {
                    @Override
                    public void onSuccess(@Nullable Place place) {
                        Log.d(TAG, "success, getting venue, getPlaceWithAlias...");
                        String placeStr = place.toJSONString();
                        sendCallbackCmd(placeStr, context);
                    }

                    @Override
                    public void onFailure(@Nullable Throwable throwable) {
                        Log.d(TAG, "failure, getting venue, removeDataForVenue...");
                        sendCallbackCmdError(context);
                    }
                });
            }

            @Override
            public void onFailure(@Nullable Throwable throwable) {
                sendCallbackCmdError(context);
            }
        });

    }

    private String venues2JsonArray(List<Venue> venues) {
        StringBuffer sb = new StringBuffer();
        sb.append('[');

        boolean isFirst = true;
        for (Venue venue: venues) {
            if (!isFirst) {
                sb.append(',');
            } else {
                isFirst = false;
            }

            String venueStr = venue.toJSONString();
            sb.append(venueStr);
        };

        sb.append(']');
        return sb.toString();
    }

    private String universes2JsonArray(List<Universe> universes) {
        StringBuffer sb = new StringBuffer();
        sb.append('[');

        boolean isFirst = true;
        for (Universe universe: universes) {
            if (!isFirst) {
                sb.append(',');
            } else {
                isFirst = false;
            }

            String venueStr = universe.toJSONString();
            sb.append(venueStr);
        };

        sb.append(']');
        return sb.toString();
    }

    private static void sendCallbackCmd(String args, CallbackContext context) {
        sActivity.runOnUiThread(new Runnable() {
            public void run() {
                JSONObject json = new JSONObject();
                try {
                    if (args != null) {
                        json.put(CBK_FIELD_ARG, args);
                    }
                    PluginResult result = new PluginResult(PluginResult.Status.OK, json);
                    context.sendPluginResult(result);
                } catch (JSONException e) {
                    PluginResult result = new PluginResult(PluginResult.Status.ERROR);
                    context.sendPluginResult(result);
                }
            }
        });
    }

    private static void sendCallbackCmdOK(CallbackContext context) {
        sActivity.runOnUiThread(new Runnable() {
            public void run() {
                PluginResult result = new PluginResult(PluginResult.Status.OK);
                context.sendPluginResult(result);
            }
        });
    }

    private static void sendCallbackCmdError(CallbackContext context) {
        sActivity.runOnUiThread(new Runnable() {
            public void run() {
                PluginResult result = new PluginResult(PluginResult.Status.ERROR);
                context.sendPluginResult(result);
            }
        });
    }

}
