package umd.project.safetymapexample.fragment;

import android.app.Fragment;
import android.location.Location;
import com.google.android.gms.maps.model.LatLng;

public abstract class IDataViewFragment extends Fragment {
  public abstract void updateLocation(LatLng location);
}
