package io.mapwize.cordova;

import android.app.Activity;
import android.util.Log;

import com.mapbox.mapboxsdk.Mapbox;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import androidx.annotation.Nullable;

import io.mapwize.mapwizesdk.api.ApiCallback;
import io.mapwize.mapwizesdk.api.Universe;
import io.mapwize.mapwizesdk.api.Venue;
import io.mapwize.mapwizesdk.api.MapwizeApiFactory;
import io.mapwize.mapwizesdk.core.MapwizeConfiguration;

import static io.mapwize.cordova.MapwizeCordovaPlugin.CBK_FIELD_ARG;

public class OfflineManager {
    private static final String TAG = "OfflineManager";
    Activity mActivity;
    io.mapwize.mapwizesdk.api.OfflineManager mOfflineManager;


    public OfflineManager(Activity activity, String styleUrl) {
        Log.d(TAG, "OfflineManager, styleUrl: " + styleUrl);
        mActivity= activity;
        MapwizeConfiguration config = MapwizeConfiguration.getInstance();
        mOfflineManager = new io.mapwize.mapwizesdk.api.OfflineManager(config);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Mapbox.getInstance(activity, "pk.mapwize");
            }
        });

    }

    public void removeDataForVenue(String venueId, String universeId, CallbackContext context) {
        Log.d(TAG, "removeDataForVenue...");
        MapwizeApiFactory.getApi().getVenue(venueId, new ApiCallback<Venue>() {
            @Override
            public void onSuccess(@Nullable Venue venue) {
                Log.d(TAG, "success, getting venue, removeDataForVenue...");
                MapwizeApiFactory.getApi().getUniverse(universeId, new ApiCallback<Universe>() {
                    @Override
                    public void onSuccess(@Nullable Universe universe) {
                        Log.d(TAG, "success, getting universe, removeDataForVenue...");
                        mOfflineManager.removeData(venue, universe);
                        sendCallbackCmdOK(context);
                    }

                    @Override
                    public void onFailure(@Nullable Throwable throwable) {
                        Log.d(TAG, "failure, getting universe, removeDataForVenue...");
                        sendCallbackCmdError(context);
                    }
                });
            }

            @Override
            public void onFailure(@Nullable Throwable throwable) {
                Log.d(TAG, "failure, getting venue, removeDataForVenue...");
                sendCallbackCmdError(context);
            }
        });
    }

    public void downloadDataForVenue(String venueId, String universeId, CallbackContext context) {
        Log.d(TAG, "downloadDataForVenue...");
        MapwizeApiFactory.getApi().getVenue(venueId, new ApiCallback<Venue>() {
            @Override
            public void onSuccess(@Nullable Venue venue) {
                Log.d(TAG, "success, getting venue, downloadDataForVenue...");
                Log.d(TAG, "venue: universe: " + venue.getUniverses().toString() + ", all: " + venue.toJSONString());

                getUniverse(venueId, universeId, new ApiCallback<Universe>() {
                    @Override
                    public void onSuccess(@Nullable Universe universe) {
                        Log.d(TAG, "success, getting universe, downloadDataForVenue...");
                        mOfflineManager.downloadData(venue, universe, new io.mapwize.mapwizesdk.api.OfflineManager.DownloadTaskListener() {

                            @Override
                            public void onSuccess() {
                                Log.d(TAG, "onSuccess...");
                                sendCallbackCmdOK(context);
                            }

                            @Override
                            public void onProgress(int i) {
                                Log.d(TAG, "onProgress...");
                                sendCallbackCmdKeep("" + i, context);
                            }

                            @Override
                            public void onFailure(Exception e) {
                                Log.d(TAG, "onFailure...");
                                sendCallbackCmdError(context);
                            }
                        });
                    }

                    @Override
                    public void onFailure(@Nullable Throwable throwable) {
                        Log.d(TAG, "failure, getting universe, downloadDataForVenue...");
                        sendCallbackCmdError(context);
                    }
                });
            }

            @Override
            public void onFailure(@Nullable Throwable throwable) {
                Log.d(TAG, "failure, getting venue, downloadDataForVenue...");
                sendCallbackCmdError(context);
            }
        });

    }

    public void isOfflineForVenue(String venueId, String universeId, CallbackContext context) {
        Log.d(TAG, "isOfflineForVenue...");
        MapwizeApiFactory.getApi().getVenue(venueId, new ApiCallback<Venue>() {
            @Override
            public void onSuccess(@Nullable Venue venue) {
                Log.d(TAG, "success, getting venue, isOfflineForVenue...");
                MapwizeApiFactory.getApi().getUniverse(universeId, new ApiCallback<Universe>() {
                    @Override
                    public void onSuccess(@Nullable Universe universe) {
                        Log.d(TAG, "success, getting universe, isOfflineForVenue...");
                        boolean isOffline = mOfflineManager.isVenueUniverseOffline(venue, universe);
                        sendCallbackCmd("" + isOffline, context);
                    }

                    @Override
                    public void onFailure(@Nullable Throwable throwable) {
                        Log.d(TAG, "failure, getting universe, isOfflineForVenue...");
                        sendCallbackCmdError(context);
                    }
                });
            }

            @Override
            public void onFailure(@Nullable Throwable throwable) {
                Log.d(TAG, "failure, getting venue, isOfflineForVenue...");
                sendCallbackCmdError(context);
            }
        });
    }

    public void getOfflineVenues(CallbackContext context) {
        Log.d(TAG, "getOfflineVenues...");
        List<Venue> venues = mOfflineManager.getOfflineVenues();
        String venuesStr = venues2JsonArray(venues);
        sendCallbackCmd(venuesStr, context);
    }

    public void getOfflineUniversesForVenue(String venueId, CallbackContext context) {
        Log.d(TAG, "getOfflineUniversesForVenue...");
        MapwizeApiFactory.getApi().getVenue(venueId, new ApiCallback<Venue>() {
            @Override
            public void onSuccess(@Nullable Venue venue) {
                Log.d(TAG, "onSuccess...");
                List<Universe> venues = mOfflineManager.getOfflineUniversesForVenue(venue);
                String venuesStr = universes2JsonArray(venues);
                sendCallbackCmd(venuesStr, context);
            }

            @Override
            public void onFailure(@Nullable Throwable throwable) {
                Log.d(TAG, "onFailure...");
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
        }

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
            // String venueStr = universe.toString();
            sb.append(venueStr);
        }

        sb.append(']');
        return sb.toString();
    }

    private void getUniverse(String venueId, String universeId, ApiCallback<Universe> apiCallback) {
        MapwizeApiFactory.getApi().getAccessibleUniversesForVenue(venueId, new ApiCallback<List<Universe>>() {

            @Override
            public void onSuccess(@Nullable List<Universe> universes) {
                Log.d(TAG, "getAccessibleUniversesForVenue, onSuccess...");
                for (Universe univ: universes) {
                    if (universeId.equals(univ.getId())) {
                        Log.d(TAG, "getAccessibleUniversesForVenue, universe found...");
                        apiCallback.onSuccess(univ);
                        return;
                    }
                }
                Log.d(TAG, "Failed to get Universe...universes: " + universes.toString() + ", universeId: " + universeId);
                apiCallback.onFailure(new Throwable("Failed to get Universe"));
            }

            @Override
            public void onFailure(@Nullable Throwable throwable) {
                Log.d(TAG, "onFailure, Failed to get Universe...universes: " + throwable.getLocalizedMessage());
                apiCallback.onFailure(throwable);
            }
        });
    }

    private void sendCallbackCmd(String args, CallbackContext context) {
        mActivity.runOnUiThread(new Runnable() {
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

    private void sendCallbackCmdKeep(String args, CallbackContext context) {
        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                JSONObject json = new JSONObject();
                try {
                    if (args != null) {
                        json.put(CBK_FIELD_ARG, args);
                    }
                    PluginResult result = new PluginResult(PluginResult.Status.OK, json);
                    result.setKeepCallback(true);
                    context.sendPluginResult(result);
                } catch (JSONException e) {
                    PluginResult result = new PluginResult(PluginResult.Status.ERROR);
                    context.sendPluginResult(result);
                }
            }
        });
    }

    private void sendCallbackCmdOK(CallbackContext context) {
        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                PluginResult result = new PluginResult(PluginResult.Status.OK);
                context.sendPluginResult(result);
            }
        });
    }

    private void sendCallbackCmdError(CallbackContext context) {
        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                PluginResult result = new PluginResult(PluginResult.Status.ERROR);
                context.sendPluginResult(result);
            }
        });
    }

}
