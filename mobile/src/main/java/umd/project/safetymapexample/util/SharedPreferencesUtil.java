package umd.project.safetymapexample.util;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashSet;
import java.util.Set;

import umd.project.safetymapexample.R;
import umd.project.safetymapexample.fragment.dialog.CrimeLayersDialogFragment;
import umd.project.safetymapexample.fragment.dialog.WeatherLayersDialogFragment;

public class SharedPreferencesUtil {

    private static final String CRIME_MAP = "crime_map";
    private static final String WEATHER_MAP = "weather_map";
    private static final String QUERY_LOCATION = "query_location";
    private static final String ZOOM = "zoom";
    private static final String STREET_VIEW_ENABLED = "street_view_enabled";
    private static final String ALL_MAPS = "map";
    private static final String LAYERS = "layers";

    public static void saveLocation(Activity activity, LatLng location) {
        SharedPreferences sharedPreferences = activity
                .getSharedPreferences(QUERY_LOCATION, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(QUERY_LOCATION,
                MapUtil.getStringFromLocation(location)).apply();
    }

    public static LatLng getLocation(Activity activity) {
        String location = activity.getSharedPreferences(QUERY_LOCATION,
                Context.MODE_PRIVATE).getString(QUERY_LOCATION, activity.getString(R.string.debug_location_1));
        return MapUtil.getLocationFromString(location);
    }

    public static void saveLocation(Fragment fragment, LatLng location) {
        SharedPreferences sharedPreferences = fragment.getActivity()
                .getSharedPreferences(QUERY_LOCATION, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(QUERY_LOCATION,
                MapUtil.getStringFromLocation(location)).apply();
    }

    public static LatLng getLocation(Fragment fragment) {
        String location = fragment.getActivity().getSharedPreferences(QUERY_LOCATION,
                Context.MODE_PRIVATE).getString(QUERY_LOCATION, fragment.getActivity().getString(R.string.debug_location_1));
        return MapUtil.getLocationFromString(location);
    }

    public static boolean getStreetViewEnabled(Fragment fragment) {
        SharedPreferences sharedPreferences = fragment.getActivity()
                .getSharedPreferences(CRIME_MAP, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(STREET_VIEW_ENABLED, false);
    }

    public static void saveStreetViewEnabled(Fragment fragment, boolean enabled) {
        SharedPreferences sharedPreferences = fragment.getActivity()
                .getSharedPreferences(CRIME_MAP, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(STREET_VIEW_ENABLED, enabled).apply();
    }

    public static float getZoomState(Fragment fragment) {
        SharedPreferences sharedPref = fragment.getActivity()
                .getSharedPreferences(ALL_MAPS, Context.MODE_PRIVATE);
        return sharedPref.getFloat(ZOOM, 14f);
    }

    public static void saveZoomState(Fragment fragment, float zoomLevel) {
        SharedPreferences sharedPreferences = fragment.getActivity()
                .getSharedPreferences(ALL_MAPS, Context.MODE_PRIVATE);
        sharedPreferences.edit().putFloat(ZOOM, zoomLevel).apply();
    }

    public static Set<String> getSelectedCrimeMapLayers(Fragment fragment) {
        Set<String> defaultLayers = new HashSet<String>();
        defaultLayers.add(CrimeLayersDialogFragment.Type.ALL);
        return fragment.getActivity().getSharedPreferences(CRIME_MAP,
                Context.MODE_PRIVATE).getStringSet(LAYERS, defaultLayers);
    }

    public static void saveSelectedCrimeMapLayers(Fragment fragment, Set<String> selected) {
        SharedPreferences sharedPreferences =
                fragment.getActivity().getSharedPreferences(CRIME_MAP, Context.MODE_PRIVATE);
        sharedPreferences.edit().putStringSet(LAYERS, selected).apply();
    }

    public static Set<String> getSelectedWeatherMapLayers(Fragment fragment) {
        Set<String> defaultLayers = new HashSet<String>();
        defaultLayers.add(WeatherLayersDialogFragment.Type.RAIN);
        return fragment.getActivity().getSharedPreferences(WEATHER_MAP,
                Context.MODE_PRIVATE).getStringSet(LAYERS, defaultLayers);
    }

    public static void saveSelectedWeatherMapLayers(Fragment fragment, Set<String> selected) {
        SharedPreferences sharedPreferences =
                fragment.getActivity().getSharedPreferences(WEATHER_MAP, Context.MODE_PRIVATE);
        sharedPreferences.edit().putStringSet(LAYERS, selected).apply();
    }

}

