package umd.project.safetymapexample.map;

import android.app.LoaderManager;
import android.content.Loader;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.util.ArrayList;
import java.util.List;

import umd.project.safetymapexample.R;
import umd.project.safetymapexample.util.MapUtils;

import static android.R.attr.x;
import static umd.project.safetymapexample.util.MapUtils.addMarkers;
import static umd.project.safetymapexample.util.MapUtils.setMapStyles;

public class MapFragment extends com.google.android.gms.maps.MapFragment
        implements OnMapReadyCallback {

    private static final String TAG = MapFragment.class.getSimpleName();

    private static final int TOKEN_LOADER_MARKERS = 0;

    private final DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();

    protected List<LatLng> mList = new ArrayList<>();

    protected GoogleMap mMap;
    protected TileProvider mProvider;
    protected TileOverlay mOverlay;

    private final double OFFSET = .01;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        UiSettings uiSettings = mMap.getUiSettings();

        mMap.setBuildingsEnabled(true);

        setMapStyles(getContext(), mMap, R.raw.map_styles);

        loadMapData();
    }

    private void loadMapData() {

        Query query = mDatabaseReference.child("data").limitToFirst(1000);

        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                LoaderManager loaderManager = getLoaderManager();
                loaderManager.initLoader(TOKEN_LOADER_MARKERS, null, new LoaderManager.LoaderCallbacks<List<LatLng>>() {
                    @Override
                    public Loader<List<LatLng>> onCreateLoader(int id, Bundle args) {
                        return new FirebaseMarkerLoadingTask(getActivity(), dataSnapshot);
                    }

                    @Override
                    public void onLoadFinished(Loader<List<LatLng>> loader, List<LatLng> data) {
                        onDataLoaded(data);
                    }

                    @Override
                    public void onLoaderReset(Loader<List<LatLng>> loader) {

                    }

                }).forceLoad();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
            }

        });

    }

    private void onDataLoaded(List<LatLng> list) {
        if (list != null && !list.isEmpty()) {
            mList = list;
            LatLng loc = list.get(0);
            changeCameraLocation(new LatLng(loc.latitude, loc.longitude + OFFSET), 10.0f);
            addHeatmap(mList);
        }
    }

    private void addHeatmap(List<LatLng> list) {
        mProvider = new HeatmapTileProvider.Builder()
                .data(list)
                .radius(20)
                .build();

        mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
    }

    private void clearMap() {
        if (mMap != null) {
            mMap.clear();
        }
    }

    void displayAsHeatMap() {
        if (mMap != null) {
            mMap.clear();
            if (!mList.isEmpty()) {
                addHeatmap(mList);
                LatLng loc = mList.get(0);
                changeCameraLocation(new LatLng(loc.latitude, loc.longitude + OFFSET), 10.0f);
            }
        }
    }

    void displayAsMarkers() {
        if (mMap != null) {
            mMap.clear();
            if (!mList.isEmpty()) {
                addMarkers(mMap, mList);
                LatLng loc = mList.get(0);
                changeCameraLocation(new LatLng(loc.latitude, loc.longitude + OFFSET), 10.0f);
            }
        }
    }

    public void changeCameraLocation(LatLng location, float zoom) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, zoom));
    }


}
