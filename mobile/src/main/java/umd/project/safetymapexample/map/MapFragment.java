package umd.project.safetymapexample.map;

import android.app.LoaderManager;
import android.content.Loader;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
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

import static umd.project.safetymapexample.util.MapUtils.addMarkers;

public class MapFragment extends com.google.android.gms.maps.MapFragment
        implements OnMapReadyCallback{

    private static final String TAG = MapFragment.class.getSimpleName();

    private static final CameraPosition CURR_LOC_CAMERA = new CameraPosition.Builder()
            .target(new LatLng(38.9897, 76.9378))
            .build();

    private static final int TOKEN_LOADER_MARKERS = 0;

    protected List<LatLng> mList = new ArrayList<>();

    protected GoogleMap mMap;
    protected TileProvider mProvider;
    protected TileOverlay mOverlay;

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

        setMapStyles(R.raw.map_styles);

        loadMapData();

    }

    private void loadMapData(){
//        LoaderManager lm = getLoaderManager();
//        lm.initLoader(TOKEN_LOADER_MARKERS, null, mMarkerLoader).forceLoad();

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = databaseReference.child("data").limitToFirst(100);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot crimeSnapshot : dataSnapshot.getChildren()){
                    ArrayList<String> crime = (ArrayList<String>) crimeSnapshot.getValue();

                    double lat = Double.valueOf((String) crime.get(20));
                    double lng = Double.valueOf((String) crime.get(21));

                    mList.add(new LatLng(lat, lng));

                    if(Looper.myLooper() == Looper.getMainLooper()) {
                        Log.i(TAG, "Running on Main thread!");
                    }

                    Log.i(TAG, "Crime " + crimeSnapshot.getKey() + ": " + crime);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG, "onCancelled: ");
                databaseError.toException().printStackTrace();
            }
        });

        Log.i(TAG, "loadMapData: closing");
    }

    private void setMapStyles(int resource){
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
        if (list != null) {
            mList = list;
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(list.get(0), 14.0f));
            addHeatmap(mList);
        }
    }

    void displayAsHeatMap(){
        mMap.clear();
        addHeatmap(mList);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mList.get(0), 14.0f));
    }

    void displayAsMarkers(){
        mMap.clear();
        addMarkers(mMap, mList);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mList.get(0), 14.0f));
    }

    private void addClusteredMarkers(List<LatLng> list){

    }

    private void addHeatmap(List<LatLng> list){
        mProvider = new HeatmapTileProvider.Builder()
                .data(list)
                .radius(50)
                .build();

        mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
    }


    private void addCustomOverlay(List<LatLng> list){

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
            onDataLoaded(data);
        }

        @Override
        public void onLoaderReset(Loader<List<LatLng>> loader) {
        }
    };



}
