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

import static android.R.attr.x;
import static umd.project.safetymapexample.util.MapUtils.addMarkers;

public class MapFragment extends com.google.android.gms.maps.MapFragment
        implements OnMapReadyCallback {

    private static final String TAG = MapFragment.class.getSimpleName();

    private static final int TOKEN_LOADER_MARKERS = 0;

    protected List<LatLng> mList = new ArrayList<>();

    protected GoogleMap mMap;
    protected TileProvider mProvider;
    protected TileOverlay mOverlay;

    private final double OFFSET=.01;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i(TAG, "onMapReady: ");
        mMap = googleMap;

        UiSettings uiSettings = mMap.getUiSettings();

        mMap.setBuildingsEnabled(true);

        setMapStyles(R.raw.map_styles);

        loadMapData();
    }

    private Query makeQueryCrimeTypeQuery(){
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        return databaseReference.child("data").child("").equalTo("THEFT").limitToFirst(x);
    }

    private Query loadFirstXCrimes(int x){
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        return databaseReference.child("data").limitToFirst(x);
    }

    private void loadMapData() {
        Log.i(TAG, "loadMapData: ");
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = databaseReference.child("data").limitToFirst(1000);
        Log.i(TAG, "loadMapData: ");
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                Log.i(TAG, "onDataChange: ");
                LoaderManager loaderManager = getLoaderManager();
                loaderManager.initLoader(TOKEN_LOADER_MARKERS, null, new LoaderManager.LoaderCallbacks<List<LatLng>>() {
                    @Override
                    public Loader<List<LatLng>> onCreateLoader(int id, Bundle args) {
                        Log.i(TAG, "onCreateLoader: ");
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
                Log.i(TAG, "onCancelled: ");
                databaseError.toException().printStackTrace();
            }

        });
        
    }

    private void setMapStyles(int resource) {
        try {
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getContext(), resource));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
    }

    private void clearMap() {
        if (mMap != null) {
            mMap.clear();
        }
    }

    private void onDataLoaded(List<LatLng> list) {
        Log.i(TAG, "onDataLoaded: ");
        if (list != null) {
            mList = list;
            LatLng loc = list.get(0);
            changeCameraLocation(new LatLng(loc.latitude, loc.longitude   + OFFSET), 10.0f);
            addHeatmap(mList);
        }
    }

    void displayAsHeatMap() {
        Log.i(TAG, "displayAsHeatMap: ");
        if (mMap != null){
            mMap.clear();
            addHeatmap(mList);
            if (mList.size() > 0){
                LatLng loc = mList.get(0);
                changeCameraLocation(new LatLng(loc.latitude, loc.longitude   + OFFSET), 10.0f);
            }
        }
    }

    void displayAsMarkers() {
        Log.i(TAG, "displayAsMarkers: ");
        if (mMap != null){
            mMap.clear();
            addMarkers(mMap, mList);
            if (mList.size() > 0){
                LatLng loc = mList.get(0);
                changeCameraLocation(new LatLng(loc.latitude, loc.longitude   + OFFSET), 10.0f);
            }
        }
    }

    public void changeCameraLocation(LatLng location, float zoom){
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, zoom));
    }

    private void addHeatmap(List<LatLng> list) {
        mProvider = new HeatmapTileProvider.Builder()
                .data(list)
                .radius(20)
                .build();

        mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
    }





}
