package umd.project.safetymapexample.fragment;

import android.content.Context;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileProvider;

import java.util.List;

import static umd.project.safetymapexample.util.SharedPreferencesUtil.getLocation;

public class CustomStreetViewFragment extends com.google.android.gms.maps.StreetViewPanoramaFragment
        implements OnStreetViewPanoramaReadyCallback, ICustomMapFragment {

    public static final String TAG = CustomStreetViewFragment.class.getSimpleName();

    private StreetViewPanorama mStreetViewPanorama;

    public CustomStreetViewFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getStreetViewPanoramaAsync(this);
    }

    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
        mStreetViewPanorama = streetViewPanorama;
        mStreetViewPanorama.setStreetNamesEnabled(true);
        mStreetViewPanorama.setUserNavigationEnabled(true);

        if (getActivity() != null) {
            updateMapLocation(getLocation(getActivity()));
        }
    }

    @Override
    public void updateMapLocation(LatLng location) {
        moveCamera(location);
    }

    private void moveCamera(LatLng location) {
        if (location != null && mStreetViewPanorama != null) {
            mStreetViewPanorama.setPosition(location, 10000);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mStreetViewPanorama != null) {
            updateMapLocation(getLocation(getActivity()));
        }

        if (getParentFragment() instanceof StreetViewReadyListener) {
            ((StreetViewReadyListener) getParentFragment()).onStreetViewReady();
        }
    }

    @Override
    public void updateMapOverlays(TileProvider layers) {
        // no overlays
    }

    @Override
    public void updateMapOverlays(List<TileProvider> providers) {
        // no overlays
    }

    interface StreetViewReadyListener {
        void onStreetViewReady();
    }
}
