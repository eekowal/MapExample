package umd.project.safetymapexample.fragment;

import android.app.Fragment;
import android.location.Location;
import com.google.android.gms.maps.model.LatLng;
import java.util.Set;

interface ICustomMapFragment {
  void updateMapLocation(LatLng location);
  void updateMapOverlays(Set<String> layers);
}
