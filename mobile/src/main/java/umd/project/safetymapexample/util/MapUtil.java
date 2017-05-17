package umd.project.safetymapexample.util;

import com.google.android.gms.maps.model.LatLng;

class MapUtil {

    private static final String TAG = MapUtil.class.getSimpleName();

    static LatLng getLocationFromString(String location) {
        String[] coords = location.split("\\s*,\\s*");
        return new LatLng(Double.valueOf(coords[0]), Double.valueOf(coords[1]));
    }

    static String getStringFromLocation(LatLng location) {
        return location.latitude + ", " + location.longitude;
    }

}
