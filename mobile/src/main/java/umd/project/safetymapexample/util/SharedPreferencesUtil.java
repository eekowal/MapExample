package umd.project.safetymapexample.util;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import com.google.android.gms.maps.model.LatLng;
import java.util.HashSet;
import java.util.Set;
import umd.project.safetymapexample.R;
import umd.project.safetymapexample.dialog.CrimeMapLayersDialogFragment;

public class SharedPreferencesUtil {


  public static void saveLocation(Activity activity, LatLng location){
    SharedPreferences sharedPreferences = activity.getSharedPreferences("query_location", Context.MODE_PRIVATE);
    sharedPreferences.edit().putString("query_location", MapUtil.getStringFromLocation(location)).apply();
  }

  public static LatLng getLocation(Activity activity){
    String location = activity.getSharedPreferences("query_location",
        Context.MODE_PRIVATE).getString("query_location", activity.getString(R.string.debug_location_1));
    return MapUtil.getLocationFromString(location);
  }

  public static void saveLocation(Fragment fragment, LatLng location){
    SharedPreferences sharedPreferences = fragment.getActivity().getSharedPreferences("query_location", Context.MODE_PRIVATE);
    sharedPreferences.edit().putString("query_location", MapUtil.getStringFromLocation(location)).apply();
  }

  public static LatLng getLocation(Fragment fragment){
    String location = fragment.getActivity().getSharedPreferences("query_location",
        Context.MODE_PRIVATE).getString("query_location", fragment.getActivity().getString(R.string.debug_location_1));
    return MapUtil.getLocationFromString(location);
  }

  public static boolean getStreetViewEnabled(Fragment fragment){
    SharedPreferences sharedPreferences = fragment.getActivity().getSharedPreferences("crime_map", Context.MODE_PRIVATE);
    return sharedPreferences.getBoolean("street_view_enabled", false);
  }

  public static void saveStreetViewEnabled(Fragment fragment, boolean enabled){
    SharedPreferences sharedPreferences = fragment.getActivity().getSharedPreferences("crime_map", Context.MODE_PRIVATE);
    sharedPreferences.edit().putBoolean("street_view_enabled", enabled).apply();
  }

  public static float getZoomState(Fragment fragment){
    SharedPreferences sharedPref = fragment.getActivity().getSharedPreferences("crime_map", Context.MODE_PRIVATE);
    return sharedPref.getFloat("zoom", 10f);
  }

  public static void saveZoomState(Fragment fragment, float zoomLevel){
    SharedPreferences sharedPreferences = fragment.getActivity().getSharedPreferences("crime_map", Context.MODE_PRIVATE);
    sharedPreferences.edit().putFloat("zoom", zoomLevel).apply();
  }

  private Set<String> getSelectedLayersFromSharedPreferences(Fragment fragment){
    Set<String> layers = new HashSet<>();
    if(fragment instanceof CrimeMapLayersDialogFragment){
      layers = fragment.getActivity().getSharedPreferences("crime_map", Context.MODE_PRIVATE).getStringSet("layers", new HashSet<String>());
    }
    return layers;
  }

  public static Set<String> getSelectedLayers(Fragment fragment){
    return fragment.getActivity().getSharedPreferences("crime_map", Context.MODE_PRIVATE).getStringSet("layers", new HashSet<String>());
  }
}

