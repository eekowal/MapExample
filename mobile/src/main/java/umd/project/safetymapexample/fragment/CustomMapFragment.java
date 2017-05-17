package umd.project.safetymapexample.fragment;

import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;

import java.util.ArrayList;
import java.util.List;

import umd.project.safetymapexample.tiles.TileOverlayManager;
import umd.project.safetymapexample.util.SharedPreferencesUtil;

import static com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom;
import static umd.project.safetymapexample.app.Config.TILE_OVERLAY_ZINDEX;

public class CustomMapFragment extends com.google.android.gms.maps.MapFragment
        implements OnMapReadyCallback, ICustomMapFragment {

    private static final String TAG = CustomMapFragment.class.getSimpleName();

    private GoogleMap mMap;
    private Circle mLocationCircle;
    private Marker mLocationMarker;
    private LatLng mQueriedLocation;
    private LatLng mCenterLocation;
    private float mCameraZoomLevel;
    private TileOverlayManager mTileOverlays;
    private List<TileProvider> mTileProviders;

    public CustomMapFragment() {
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getMapAsync(this);
        setRetainInstance(true);

        mCenterLocation = SharedPreferencesUtil.getLocation(this);
        mQueriedLocation = SharedPreferencesUtil.getLocation(this);
        mCameraZoomLevel = SharedPreferencesUtil.getZoomState(this);

        mTileOverlays = new TileOverlayManager();
        mTileProviders = new ArrayList<>();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.getUiSettings().setCompassEnabled(false);

        for (TileProvider tileProvider : mTileProviders) {
            mTileOverlays.add(mMap.addTileOverlay(
                    new TileOverlayOptions().tileProvider(tileProvider)
                            .zIndex(TILE_OVERLAY_ZINDEX)));
        }

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                mCameraZoomLevel = mMap.getCameraPosition().zoom;
                updateLocationIconSize();
            }
        });

        mMap.setMinZoomPreference(10.0f);
        mMap.setMaxZoomPreference(22.0f);

        updateLocationIcon();
        moveCamera();

        if (getParentFragment() instanceof MapReadyListener) {
            ((MapReadyListener) getParentFragment()).onMapReady();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        mCameraZoomLevel = SharedPreferencesUtil.getZoomState(this);
    }

    @Override
    public void onPause() {
        SharedPreferencesUtil.saveZoomState(this, mCameraZoomLevel);
        super.onPause();
    }

    public void updateMapLocation(LatLng location) {
        mQueriedLocation = location;
        updateMapLocation();
    }

    private void updateMapLocation() {
        updateLocationIcon();
        mMap.animateCamera(newLatLngZoom(mQueriedLocation, mCameraZoomLevel));
    }

    public void updateLocationIcon() {
        if (mLocationCircle != null) {
            mLocationCircle.remove();
            mLocationMarker.remove();
        }

        mLocationCircle = mMap.addCircle(new CircleOptions().center(mQueriedLocation)
                .radius(800 * (22 / mCameraZoomLevel))
                .strokeColor(Color.TRANSPARENT)
                .fillColor(Color.argb(30, 0, 77, 232)));

        mLocationMarker =
                mMap.addMarker(new MarkerOptions().position(mQueriedLocation)
                        .flat(true).alpha(.98f));
    }

    private void updateLocationIconSize() {
        if (mLocationCircle != null) {
            mLocationCircle.setRadius(800 * (22 / mCameraZoomLevel));
        } else {
            mLocationCircle = mMap.addCircle(new CircleOptions().center(mQueriedLocation)
                    .radius(800 * (22 / mCameraZoomLevel))
                    .strokeColor(Color.TRANSPARENT)
                    .fillColor(Color.argb(30, 0, 77, 232)));
        }
    }

    private void moveCamera() {
        moveCamera(mQueriedLocation, mCameraZoomLevel);
    }

    private void moveCamera(LatLng location, float zoom) {
        mMap.moveCamera(newLatLngZoom(location, zoom));
    }

    public void updateMapOverlays(TileProvider provider) {
        if (provider != null) {

            if (!mTileProviders.isEmpty()) {
                mTileProviders.remove(0);
            }

            mTileProviders.add(provider);

            mTileOverlays.clear();
            mMap.clear();

            mTileOverlays.add(mMap.addTileOverlay(
                    new TileOverlayOptions().tileProvider(provider)
                            .zIndex(TILE_OVERLAY_ZINDEX)));

            updateLocationIcon();
        }
    }

    public void updateMapOverlays(List<TileProvider> providers) {
        if (!providers.isEmpty()) {

            mTileProviders = providers;

            mTileOverlays.clear();
            mMap.clear();

            int z_idx = 1;
            for (TileProvider tileProvider : providers) {
                mTileOverlays.add(mMap.addTileOverlay(
                        new TileOverlayOptions().tileProvider(tileProvider)
                                .zIndex(TILE_OVERLAY_ZINDEX + z_idx++)));
            }

            updateLocationIcon();
        }
    }

    interface MapReadyListener {
        void onMapReady();
    }

}
