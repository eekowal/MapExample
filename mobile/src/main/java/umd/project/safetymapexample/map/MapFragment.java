package umd.project.safetymapexample.map;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.util.HashMap;

import umd.project.safetymapexample.R;
import umd.project.safetymapexample.util.SettingsUtil;

import static com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom;
import static umd.project.safetymapexample.settings.Settings.Overlay.HEATMAP;
import static umd.project.safetymapexample.settings.Settings.Overlay.MARKERS;
import static umd.project.safetymapexample.util.MapUtil.addMarkers;
import static umd.project.safetymapexample.util.MapUtil.getLocationFromString;
import static umd.project.safetymapexample.util.MapUtil.setMapStyles;
import static umd.project.safetymapexample.util.SettingsUtil.getDisplayTypeFromString;
import static umd.project.safetymapexample.util.UiUtil.displaySimpleSnackBar;

public class MapFragment extends com.google.android.gms.maps.MapFragment
        implements OnMapReadyCallback {

    private static final String TAG = MapFragment.class.getSimpleName();

    private final static String DATABASE_URL = "https://safetymap-a90dc.firebaseio.com";
    private final static String GEOFIRE_URL = DATABASE_URL + "/_geofire";

    private GoogleMap mMap;

    private HashMap<String, LatLng> mCoords = new HashMap<>();
    private DatabaseReference mDatabaseReference;

    private LatLng mLocation;
    private int mOverlayType;
    private float mZoomLevel;
    private float mRadius;

    private boolean mBenchmarkDbQuery;
    private long startTime;
    private long endTime;

    public MapFragment() {
        // needed empty constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mDatabaseReference = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(GEOFIRE_URL);

        getMapAsync(this);  // initiates google map. when map is ready calls onMapReady() below.
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // update zoom level if user has zoomed in
        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                CameraPosition cameraPosition = mMap.getCameraPosition();
                mZoomLevel = cameraPosition.zoom;
            }
        });

        // style map
        mMap.setBuildingsEnabled(true);
        setMapStyles(getContext(), mMap, R.raw.map_styles);

        // restore map to previous state
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getActivity().getApplicationContext());
        updateMap(sharedPreferences);
    }

    private void loadGeofireMapData() {
        loadGeofireMapData(mLocation);
    }


    private void loadGeofireMapData(LatLng location) {
        if (mBenchmarkDbQuery) {
            startTime = System.currentTimeMillis();
        }

        displaySimpleSnackBar(getView(), "Collecting crimes in a "
                + (mRadius) + " km radius.");

        GeoFire geoFire = new GeoFire(mDatabaseReference);

        // gets all crimes at the passed location within radius
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(location.latitude,
                location.longitude), mRadius);

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {

            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                mCoords.put(key, new LatLng(location.latitude, location.longitude));
            }

            @Override
            public void onKeyExited(String key) {
                mCoords.remove(key);
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                // do nothing
            }

            @Override
            public void onGeoQueryReady() {
                setOverlayType();
                changeCamera();

                if (mBenchmarkDbQuery) {
                    endTime = System.currentTimeMillis();
                    displaySimpleSnackBar(getView(), "Total execution time: "
                            + (endTime - startTime) + "ms");
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                error.toException().printStackTrace();  // print error to logcat (std-out/err)
            }

        });
    }

    private void setOverlayType() {
        switch (mOverlayType) {
            case (HEATMAP):
                displayWithHeatMapOverlay();
                break;
            case (MARKERS):
                displayWithMarkers();
                break;
            default:
                displaySimpleSnackBar(getView(), "Display type not implemented!");
        }
    }

    private void addHeatmapOverlay(HashMap<String, LatLng> locations) {
        HeatmapTileProvider provider = new HeatmapTileProvider.Builder()
                .data(locations.values())
                .build();

        mMap.addTileOverlay(new TileOverlayOptions().tileProvider(provider));
    }

    void displayWithHeatMapOverlay() {
        if (mMap != null) {
            mMap.clear();
            if (!mCoords.isEmpty()) {
                addHeatmapOverlay(mCoords);
                changeCamera(mLocation, mZoomLevel);
            }
        }
    }

    void displayWithMarkers() {
        if (mMap != null) {
            mMap.clear();
            if (!mCoords.isEmpty()) {
                addMarkers(mMap, mCoords);
                changeCamera(mLocation, mZoomLevel);
            }
        }
    }

    void changeCamera() {
        changeCamera(mLocation, mZoomLevel);
    }

    void changeCamera(LatLng location, float zoom) {
        mMap.animateCamera(newLatLngZoom(location, zoom));
    }

    public void updateMap(Intent settings) {

        mBenchmarkDbQuery = settings.getBooleanExtra(getString(R.string.pref_benchmark), mBenchmarkDbQuery);
        mOverlayType = settings.getIntExtra(getString(R.string.pref_overlay_type), mOverlayType);
        mZoomLevel = settings.getFloatExtra(getString(R.string.pref_zoom_level), mZoomLevel);

        if (SettingsUtil.radiusChanged(settings) || SettingsUtil.locationChanged(settings)) {

            mRadius = settings.getFloatExtra(getString(R.string.pref_radius), mRadius);

            String loc = settings.getStringExtra(getString(R.string.pref_location));

            if (loc != null) {
                mLocation = getLocationFromString(loc);
            }

            displaySimpleSnackBar(getView(), "Updating screen to new settings. This may take awhile!");

            mCoords.clear();

            loadGeofireMapData();

        } else {
            changeCamera(mLocation, mZoomLevel);
            setOverlayType();
        }
    }

    private void updateMap(SharedPreferences sharedPref) {

        boolean benchmark = sharedPref.getBoolean(getString(R.string.pref_benchmark), true);

        int overlayType = getDisplayTypeFromString(sharedPref.getString(
                getString(R.string.pref_overlay_type), getString(R.string.heatmap)));

        float zoomLevel = (float) sharedPref.getInt(getString(R.string.pref_zoom_level), 14);

        float radius = (float) sharedPref.getInt(getString(R.string.pref_radius), 1);

        String loc = sharedPref.getString(getString(R.string.pref_location),
                getString(R.string.location1));
        LatLng location = getLocationFromString(loc);

        setMapSettings(location, overlayType, zoomLevel, radius, benchmark);

        mCoords.clear();

        loadGeofireMapData();
    }


    private void setMapSettings(LatLng location, int overlayType,
                                float zoomLevel, float radius, boolean benchmark) {
        mBenchmarkDbQuery = benchmark;
        mLocation = location;
        mRadius = radius;
        mOverlayType = overlayType;
        mZoomLevel = zoomLevel;
    }


}
