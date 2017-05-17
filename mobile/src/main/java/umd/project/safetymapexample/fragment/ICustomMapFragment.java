package umd.project.safetymapexample.fragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileProvider;

import java.util.List;

interface ICustomMapFragment {
    void updateMapLocation(LatLng location);

    void updateMapOverlays(TileProvider provider);

    void updateMapOverlays(List<TileProvider> providers);
}
