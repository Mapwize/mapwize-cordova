package io.mapwize.cordova;

import android.app.Activity;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import io.mapwize.mapwizeformapbox.api.Api;
import io.mapwize.mapwizeformapbox.api.ApiCallback;
import io.mapwize.mapwizeformapbox.api.ApiFilter;
import io.mapwize.mapwizeformapbox.api.Direction;
import io.mapwize.mapwizeformapbox.api.DirectionPoint;
import io.mapwize.mapwizeformapbox.api.DistanceResponse;
import io.mapwize.mapwizeformapbox.api.MapwizeObject;
import io.mapwize.mapwizeformapbox.api.Parser;
import io.mapwize.mapwizeformapbox.api.Place;
import io.mapwize.mapwizeformapbox.api.PlaceList;
import io.mapwize.mapwizeformapbox.api.SearchParams;
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


    public static void getVenueWithId(String identifier, CallbackContext context) {
        Log.d(TAG, "getVenueWithId...");
        Api.getVenue(identifier, new ApiCallback<Venue>() {
            @Override
            public void onSuccess(@Nullable Venue venue) {
                Log.d(TAG, "success, getting venue, getVenueWithId...");
                String venueStr = venue.toJSONString();
                sendCallbackCmd(venueStr, context);
            }

            @Override
            public void onFailure(@Nullable Throwable throwable) {
                Log.d(TAG, "failure, getting venue, getVenueWithId...");
                sendCallbackCmdError(context);
            }
        });
    }


    public static void getVenuesWithFilter(String filterStr, CallbackContext context) {
        Log.d(TAG, "getVenuesWithFilter...");
        try {
            ApiFilter filter = Parser.parseApiFilter(filterStr);
            Api.getVenues(filter, new ApiCallback<List<Venue>>() {
                @Override
                public void onSuccess(@Nullable List<Venue> venues) {
                    Log.d(TAG, "success, getting venue, getVenuesWithFilter...");
                    String venuesStr = ApiManager.venues2JsonArray(venues);
                    sendCallbackCmd(venuesStr, context);
                }

                @Override
                public void onFailure(@Nullable Throwable throwable) {
                    sendCallbackCmdError(context);
                }
            });

        } catch(JSONException e) {
            sendCallbackCmdError(context);
        }
    }

    public static void getVenueWithName(String name, CallbackContext context) {
        Log.d(TAG, "getVenueWithName...");
        Api.getVenueWithName(name, new ApiCallback<Venue>() {
            @Override
            public void onSuccess(@Nullable Venue venue) {
                Log.d(TAG, "success, getting venue, getVenueWithName...");
                String venueStr = venue.toJSONString();
                sendCallbackCmd(venueStr, context);
            }

            @Override
            public void onFailure(@Nullable Throwable throwable) {
                sendCallbackCmdError(context);
            }
        });
    }

    public static void getVenueWithAlias(String alias, CallbackContext context) {
        Log.d(TAG, "getVenueWithAlias...");
        Api.getVenueWithAlias(alias, new ApiCallback<Venue>() {
            @Override
            public void onSuccess(@Nullable Venue venue) {
                Log.d(TAG, "success, getting venue, getVenueWithAlias...");
                String venueStr = venue.toJSONString();
                sendCallbackCmd(venueStr, context);
            }

            @Override
            public void onFailure(@Nullable Throwable throwable) {
                sendCallbackCmdError(context);
            }
        });
    }

    public static void getPlaceWithId(String identifier, CallbackContext context) {
        Log.d(TAG, "getPlaceWithId...");
        Api.getPlace(identifier, new ApiCallback<Place>() {
            @Override
            public void onSuccess(@Nullable Place place) {
                Log.d(TAG, "success, getting venue, getPlaceWithId...");
                String placeStr = place.toJSONString();
                sendCallbackCmd(placeStr, context);
            }

            @Override
            public void onFailure(@Nullable Throwable throwable) {
                sendCallbackCmdError(context);
            }
        });
    }

    public static void getPlaceWithName(String name, String venueId, CallbackContext context) {
        Log.d(TAG, "getPlaceWithName...");
        Api.getVenue(venueId, new ApiCallback<Venue>() {
            @Override
            public void onSuccess(@Nullable Venue venue) {
                Api.getPlaceWithName(name, venue, new ApiCallback<Place>() {
                    @Override
                    public void onSuccess(@Nullable Place place) {
                        Log.d(TAG, "success, getting venue, getPlaceWithName...");
                        String placeStr = place.toJSONString();
                        sendCallbackCmd(placeStr, context);
                    }

                    @Override
                    public void onFailure(@Nullable Throwable throwable) {
                        Log.d(TAG, "failure, getting venue, getPlaceWithName...");
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

    public static void getPlacesWithFilter(String filterStr, CallbackContext context) {
        Log.d(TAG, "getPlacesWithFilter...");
        try {
            ApiFilter filter = Parser.parseApiFilter(filterStr);
            Api.getPlaces(filter, new ApiCallback<List<Place>>() {
                @Override
                public void onSuccess(@Nullable List<Place> places) {
                    Log.d(TAG, "success, getting venue, getPlacesWithFilter...");
                    String placesStr = places2JsonArray(places);
                    sendCallbackCmd(placesStr, context);
                }

                @Override
                public void onFailure(@Nullable Throwable throwable) {
                    sendCallbackCmdError(context);
                }
            });
        } catch(JSONException e) {
            sendCallbackCmdError(context);
        }

    }

    public static void getPlaceListWithId(String identifier, CallbackContext context) {
        Log.d(TAG, "getPlaceListWithId...");
        Api.getPlaceList(identifier, new ApiCallback<PlaceList>() {
            @Override
            public void onSuccess(@Nullable PlaceList placeList) {
                Log.d(TAG, "success, getting placeList, getPlaceListWithId...");
                String placeListStr = placeList.toJSONString();
                sendCallbackCmd(placeListStr, context);
            }

            @Override
            public void onFailure(@Nullable Throwable throwable) {
                sendCallbackCmdError(context);
            }
        });
    }

    public static void getPlaceListWithName(String name, String venueId, CallbackContext context) {
        Log.d(TAG, "callbackId;getPlaceListWithName...");
        Api.getVenue(venueId, new ApiCallback<Venue>() {
            @Override
            public void onSuccess(@Nullable Venue venue) {
                Api.getPlaceListWithName(name, venue, new ApiCallback<PlaceList>() {
                    @Override
                    public void onSuccess(@Nullable PlaceList placeList) {
                        Log.d(TAG, "success, getting place, getPlaceListWithName...");
                        String placeListStr = placeList.toJSONString();
                        sendCallbackCmd(placeListStr, context);
                    }

                    @Override
                    public void onFailure(@Nullable Throwable throwable) {
                        Log.d(TAG, "failure, getting venue, getPlaceListWithName...");
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

    public static void getPlaceListWithAlias(String alias, String venueId, CallbackContext context) {
        Log.d(TAG, "callbackId;getPlaceListsWithAlias...");
        Api.getVenue(venueId, new ApiCallback<Venue>() {
            @Override
            public void onSuccess(@Nullable Venue venue) {
                Api.getPlaceListWithAlias(alias, venue, new ApiCallback<PlaceList>() {
                    @Override
                    public void onSuccess(@Nullable PlaceList placeList) {
                        Log.d(TAG, "success, getting venue, getPlaceWithAlias...");
                        String placeStr = placeList.toJSONString();
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

    public static void getPlaceListsWithFilter(String filterStr, CallbackContext context) {
        Log.d(TAG, "getPlaceListsWithFilter...");
        try {
            ApiFilter filter = Parser.parseApiFilter(filterStr);
            Api.getPlaceLists(filter, new ApiCallback<List<PlaceList>>() {
                @Override
                public void onSuccess(@Nullable List<PlaceList> placeLists) {
                    Log.d(TAG, "success, getting venue, getPlaceListsWithFilter...");
                    String placeListStr = placeList2JsonArray(placeLists);
                    sendCallbackCmd(placeListStr, context);
                }

                @Override
                public void onFailure(@Nullable Throwable throwable) {
                    Log.d(TAG, "failure, getting venue, getPlaceListsWithFilter...");
                    sendCallbackCmdError(context);
                }
            });

        } catch (JSONException e) {
            sendCallbackCmdError(context);
        }

    }

    public static void getUniverseWithId(String identifier, CallbackContext context) {
        Log.d(TAG, "getUniverseWithId...");

        Api.getUniverse(identifier, new ApiCallback<Universe>() {
            @Override
            public void onSuccess(@Nullable Universe universe) {
                Log.d(TAG, "success, getting venue, getUniverseWithId...");
                String universeStr = universe.toJSONString();
                sendCallbackCmd(universeStr, context);
            }

            @Override
            public void onFailure(@Nullable Throwable throwable) {
                Log.d(TAG, "failure, getting venue, getUniverseWithId...");
                sendCallbackCmdError(context);
            }
        });
    }

    public static void getUniversesWithFilter(String filterStr, CallbackContext context) {
        Log.d(TAG, "getUniversesWithFilter...");
        try {
            ApiFilter filter = Parser.parseApiFilter(filterStr);
            Api.getUniverses(filter, new ApiCallback<List<Universe>>() {
                @Override
                public void onSuccess(@Nullable List<Universe> universes) {
                    Log.d(TAG, "success, getting venue, getUniversesWithFilter...");
                    String universesStr = universes2JsonArray(universes);
                    sendCallbackCmd(universesStr, context);
                }

                @Override
                public void onFailure(@Nullable Throwable throwable) {
                    sendCallbackCmdError(context);
                }
            });
        } catch (JSONException e) {
            sendCallbackCmdError(context);
        }
    }


    public static void getAccessibleUniversesWithVenue(String venueId, CallbackContext context) {
        Log.d(TAG, "getAccessibleUniversesWithVenue...");
        Api.getAccessibleUniversesForVenue(venueId, new ApiCallback<List<Universe>>() {
            @Override
            public void onSuccess(@Nullable List<Universe> universes) {
                Log.d(TAG, "success, getting venue, getAccessibleUniversesWithVenue...");
                String universesStr = universes2JsonArray(universes);
                sendCallbackCmd(universesStr, context);
            }

            @Override
            public void onFailure(@Nullable Throwable throwable) {
                Log.d(TAG, "failure, getting venue, getAccessibleUniversesWithVenue...");
                sendCallbackCmdError(context);
            }
        });

    }

    public static void getDirectionWithFrom(String directionPointFrom, String directionPointTo, boolean isAccessible, CallbackContext context) {
        Log.d(TAG, "searchWithParams...");
        try {
            DirectionPoint from = Parser.parseDirectionPoint(directionPointFrom);
            DirectionPoint to = Parser.parseDirectionPoint(directionPointTo);
            Api.getDirection(from, to, isAccessible, new ApiCallback<Direction>() {
                @Override
                public void onSuccess(@Nullable Direction direction) {
                    Log.d(TAG, "success, getting venue, searchWithParams...");
                    String directionStr = direction.toJSONString();
                    sendCallbackCmd(directionStr, context);
                }

                @Override
                public void onFailure(@Nullable Throwable throwable) {
                    sendCallbackCmdError(context);
                }
            });
        } catch (JSONException e) {
            sendCallbackCmdError(context);
        }
    }

    public static void getDirectionWithDirectionPointsFrom(String directionPointFrom, String directionPointsListTo, boolean isAccessible, CallbackContext context) {
        Log.d(TAG, "getDirectionWithFrom...");
        try {
            DirectionPoint from = Parser.parseDirectionPoint(directionPointFrom);
            List<DirectionPoint> toList = stringList2DirectionPointList(directionPointsListTo);
            Api.getDirection(from, toList, isAccessible, new ApiCallback<Direction>() {
                @Override
                public void onSuccess(@Nullable Direction direction) {
                    Log.d(TAG, "success, getting venue, getDirectionWithFrom...");
                    String directionStr = direction.toJSONString();
                    sendCallbackCmd(directionStr, context);
                }

                @Override
                public void onFailure(@Nullable Throwable throwable) {
                    sendCallbackCmdError(context);
                }
            });
        } catch (JSONException e) {
            sendCallbackCmdError(context);
        }

    }

    public static void getDirectionWithWayPointsFrom(String directionPointFrom, String directionPointTo, String waypointsList, boolean bool1, boolean bool2, CallbackContext context) {
        Log.d(TAG, "getDirectionWithFrom...");
        try {
            DirectionPoint from = Parser.parseDirectionPoint(directionPointFrom);
            DirectionPoint to = Parser.parseDirectionPoint(directionPointTo);
            List<DirectionPoint> waypointsObj = stringList2DirectionPointList(waypointsList);
            Api.getDirection(from, to, waypointsObj, bool1, bool2, new ApiCallback<Direction>() {
                @Override
                public void onSuccess(@Nullable Direction direction) {
                    Log.d(TAG, "success, getting venue, getDirectionWithFrom...");
                    String directionStr = direction.toJSONString();
                    sendCallbackCmd(directionStr, context);
                }

                @Override
                public void onFailure(@Nullable Throwable throwable) {
                    sendCallbackCmdError(context);
                }
            });
        } catch (JSONException e) {
            sendCallbackCmdError(context);
        }

    }

    public static void getDirectionWithDirectionAndWayPointsFrom(String directionPointFrom, String directionpointsToList, String waypointsList, boolean bool1, boolean bool2, CallbackContext context) {
        Log.d(TAG, "getDirectionWithFrom...");
        try {
            DirectionPoint from = Parser.parseDirectionPoint(directionPointFrom);
            List<DirectionPoint> to = stringList2DirectionPointList(directionpointsToList);
            List<DirectionPoint> waypointsObj = stringList2DirectionPointList(waypointsList);

            Api.getDirection(from, to, waypointsObj, bool1, bool2, new ApiCallback<Direction>() {
                @Override
                public void onSuccess(@Nullable Direction direction) {
                    Log.d(TAG, "success, getting venue, getDirectionWithFrom...");
                    String directionStr = direction.toJSONString();
                    sendCallbackCmd(directionStr, context);
                }

                @Override
                public void onFailure(@Nullable Throwable throwable) {
                    sendCallbackCmdError(context);
                }
            });
        } catch (JSONException e) {
            sendCallbackCmdError(context);
        }

    }

    public static void getDistanceWithFrom(String directionPointFrom, String directionpointsToList, boolean bool1, boolean bool2, CallbackContext context) {
        Log.d(TAG, "getDistanceWithFrom...");
        try {
            DirectionPoint from = Parser.parseDirectionPoint(directionPointFrom);
            List<DirectionPoint> to = stringList2DirectionPointList(directionpointsToList);

            Api.getDistances(from, to, bool1, bool2, new ApiCallback<DistanceResponse>() {
                @Override
                public void onSuccess(@Nullable DistanceResponse response) {
                    Log.d(TAG, "success, getting venue, getDistanceWithFrom...");
                    String responseStr = response.toJSONString();
                    sendCallbackCmd(responseStr, context);
                }

                @Override
                public void onFailure(@Nullable Throwable throwable) {
                    sendCallbackCmdError(context);
                }
            });
        } catch (JSONException e) {
            sendCallbackCmdError(context);
        }

    }

    public static void searchWithParams(String searchParamsStr, CallbackContext context) {
        Log.d(TAG, "searchWithParams..." + searchParamsStr);
        try {
            SearchParams searchParams = Parser.parseSearchParams(searchParamsStr);
            Api.search(searchParams, new ApiCallback<List<MapwizeObject>>() {
                @Override
                public void onSuccess(@Nullable List<MapwizeObject> mapwizeObjects) {
                    Log.d(TAG, "success, getting venue, searchWithParams...");
                    String placeStr = mapwizeObjects2JsonArray(mapwizeObjects);
                    sendCallbackCmd(placeStr, context);
                }

                @Override
                public void onFailure(@Nullable Throwable throwable) {
                    sendCallbackCmdError(context);
                }
            });
        } catch (JSONException e) {
            sendCallbackCmdError(context);
        }
    }

    private static String mapwizeObjects2JsonArray(List<MapwizeObject> mapwizeObjects) { // TODO: fake converter!
        StringBuffer sb = new StringBuffer();
        sb.append('[');

        boolean isFirst = true;
        for (MapwizeObject mapwizeObject: mapwizeObjects) {
            if (!isFirst) {
                sb.append(',');
            } else {
                isFirst = false;
            }

            String objectStr = mapwizeObject.toJSONString();
            sb.append(objectStr);
        };

        sb.append(']');
        return sb.toString();
    }

    private static String venues2JsonArray(List<Venue> venues) {
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

    private static String places2JsonArray(List<Place> places) {
        StringBuffer sb = new StringBuffer();
        sb.append('[');

        boolean isFirst = true;
        for (Place place: places) {
            if (!isFirst) {
                sb.append(',');
            } else {
                isFirst = false;
            }

            String placeStr = place.toJSONString();
            sb.append(placeStr);
        };

        sb.append(']');
        return sb.toString();
    }

    private static String placeList2JsonArray(List<PlaceList> placeLists) {
        StringBuffer sb = new StringBuffer();
        sb.append('[');

        boolean isFirst = true;
        for (PlaceList placeList: placeLists) {
            if (!isFirst) {
                sb.append(',');
            } else {
                isFirst = false;
            }

            String placeListStr = placeList.toJSONString();
            sb.append(placeListStr);
        };

        sb.append(']');
        return sb.toString();
    }

    private static String universes2JsonArray(List<Universe> universes) {
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

    private static List<DirectionPoint> stringList2DirectionPointList(String directionsList) {
        try {
            JSONArray directions = new JSONArray(directionsList);
            List<DirectionPoint> dpl = new ArrayList<>();

            for (int i = 0; i < directions.length(); i++) {
                JSONObject direction = directions.getJSONObject(i);
                dpl.add(Parser.parseDirectionPoint(direction.toString()));

            }

            return dpl;
        } catch (JSONException e) {
            return null;
        }
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
