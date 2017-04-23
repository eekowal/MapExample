package umd.project.safetymapexample.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileProvider;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import umd.project.safetymapexample.R;
import umd.project.safetymapexample.util.SharedPreferencesUtil;

import static umd.project.safetymapexample.util.MapUtil.getLocationFromString;


public class CrimeFragment extends IDataViewFragment {

  private static final String TAG = CrimeFragment.class.getSimpleName();

  private ICustomMapFragment mFragment;

  private CustomMapFragment mCustomMapFragment;
  private CustomStreetViewFragment mCustomStreetViewFragment;

  private CurrentLocationCallback mCurrentLocationCallback;

  private boolean mStreetViewEnabled;
  private Set<String> mLayers;
  private List<TileProvider> tileProviders;

  public CrimeFragment(){}

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
    if(savedInstanceState == null){
      mCustomStreetViewFragment = new CustomStreetViewFragment();  // TODO: fragment creation
      mCustomMapFragment = new CustomMapFragment();
      mFragment = mStreetViewEnabled ? mCustomStreetViewFragment : mCustomMapFragment;
      getChildFragmentManager().beginTransaction().replace(R.id.map, (Fragment) mFragment).commit();
    }
  }

  @Override
  public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    return layoutInflater.inflate(R.layout.fragment_crime_map, viewGroup, false);
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    getView().findViewById(R.id.btn_street_view).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        mStreetViewEnabled = !mStreetViewEnabled;
        switchMapFragments();
      }
    });

    getView().findViewById(R.id.btn_current_location)
        .setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            mCurrentLocationCallback.goToCurrentLocation();
          }
        });

    getView().findViewById(R.id.fab_layers).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Snackbar.make(getView(), "Todo", Snackbar.LENGTH_LONG).show();
      }
    });

    getView().findViewById(R.id.fab_geofence).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Snackbar.make(getView(), "Todo", Snackbar.LENGTH_LONG).show();
      }
    });
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    mStreetViewEnabled = SharedPreferencesUtil.getStreetViewEnabled(this);
    mCurrentLocationCallback = (CurrentLocationCallback) getActivity();
  }

  @Override public void onResume() {
    super.onResume();
    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("crime_map", Context.MODE_PRIVATE);
    mStreetViewEnabled = sharedPreferences.getBoolean("street_view_enabled", false);
  }

  @Override public void onPause() {
    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("crime_map", Context.MODE_PRIVATE);
    sharedPreferences.edit().putBoolean("street_view_enabled", mStreetViewEnabled).apply();
    super.onPause();
  }

  public void updateLocation(LatLng location) {
    mFragment.updateMapLocation(location);
  }

  public interface CurrentLocationCallback {
    void goToCurrentLocation();
  }

  private void switchMapFragments() {
    mFragment = mStreetViewEnabled ? mCustomStreetViewFragment : mCustomMapFragment;
    getChildFragmentManager().beginTransaction()
        .replace(R.id.map, (Fragment) mFragment)
        .commit();
  }

}
