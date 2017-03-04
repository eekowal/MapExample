package umd.project.safetymapexample.map;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.util.SparseArray;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.TileOverlay;

import java.util.HashMap;
import java.util.List;

import static umd.project.safetymapexample.util.MapUtils.addMarkers;

public class MapFragment extends com.google.android.gms.maps.MapFragment
        implements OnMapReadyCallback{

    private static final String TAG = MapFragment.class.getSimpleName();

//    private static final CameraPosition CURR_LOC_CAMERA = new CameraPosition.Builder()
//            .target(new LatLng(38.9897, 76.9378))
//            .build();

    private static final int TOKEN_LOADER_MARKERS = 0;

    private SparseArray<TileOverlay> mTileOverlays = new SparseArray<>();

    protected HashMap<String, Marker> mMarkers = new HashMap<>();

    private float mDPI = 0;

    protected GoogleMap mMap;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);

        mMap.setBuildingsEnabled(true);
        mMap.setMinZoomPreference(6.0f);
        mMap.setMaxZoomPreference(14.0f);

        loadMapData();

    }

    private void loadMapData(){
        // load all markers
        LoaderManager lm = getLoaderManager();
        lm.initLoader(TOKEN_LOADER_MARKERS, null, mMarkerLoader).forceLoad();
    }


    private void centerMap(LatLng position){
        //mMap.animateCamera();
    }


    private void clearMap() {
        if (mMap != null) {
            mMap.clear();
        }

        // Clear all map elements
        mTileOverlays.clear();
        mMarkers.clear();
    }


    private void onMarkersLoaded(List<LatLng> list) {
        if (list != null) {
            addMarkers(mMap, list);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(list.get(0), 14.0f));
        }
    }

    private LoaderManager.LoaderCallbacks<List<LatLng>> mMarkerLoader
            = new LoaderManager.LoaderCallbacks<List<LatLng>>() {
        @Override
        public Loader<List<LatLng>> onCreateLoader(int id, Bundle args) {
            return new MarkerLoadingTask(getActivity());
        }

        @Override
        public void onLoadFinished(Loader<List<LatLng>> loader,
                                   List<LatLng> data) {
            onMarkersLoaded(data);
        }

        @Override
        public void onLoaderReset(Loader<List<LatLng>> loader) {
        }
    };

}
