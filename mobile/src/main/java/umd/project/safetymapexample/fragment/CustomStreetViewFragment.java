package umd.project.safetymapexample.fragment;

import android.content.Context;
import android.os.Bundle;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.model.LatLng;
import java.util.Set;

import static umd.project.safetymapexample.util.SharedPreferencesUtil.getLocation;

public class CustomStreetViewFragment extends com.google.android.gms.maps.StreetViewPanoramaFragment
    implements OnStreetViewPanoramaReadyCallback, ICustomMapFragment {

  public static final String TAG = CustomStreetViewFragment.class.getSimpleName();

  private StreetViewPanorama mStreetViewPanorama;

  public CustomStreetViewFragment(){}

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    getStreetViewPanoramaAsync(this);
  }

  @Override public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
    mStreetViewPanorama = streetViewPanorama;
    mStreetViewPanorama.setStreetNamesEnabled(true);
    mStreetViewPanorama.setUserNavigationEnabled(true);
    updateMapLocation(getLocation(getActivity()));
  }

  private void moveCamera(LatLng location) {
    if (location != null && mStreetViewPanorama != null){
      mStreetViewPanorama.setPosition(location, 10000);
    }
  }

  @Override public void updateMapLocation(LatLng location) {
    moveCamera(location);
  }

  @Override public void updateMapOverlays(Set<String> layers) {
    // TODO: time permitting, add layers.. eh
  }

}
