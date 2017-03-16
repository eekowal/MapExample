package umd.project.safetymapexample.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

public class MapUtil {
    private static final String TAG = MapUtil.class.getSimpleName();

    public static LatLng getLocationFromString(String location) {
        String[] coords = location.split("\\s*,\\s*");
        return new LatLng(Double.valueOf(coords[0]), Double.valueOf(coords[1]));
    }

    public static void addMarkers(GoogleMap map, HashMap<String, LatLng> list) {
        for (LatLng item : list.values()) {
           map.addMarker(new MarkerOptions().position(item));
        }
    }

    public static void setMapStyles(Context context, GoogleMap map, int resource) {
        try {

            boolean success = map.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(context, resource));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }

        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
    }
}
