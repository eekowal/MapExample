package umd.project.safetymapexample.fragment;

import android.graphics.Color;
import android.os.Bundle;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import umd.project.safetymapexample.tiles.TileOverlayManager;
import umd.project.safetymapexample.tiles.tileproviders.CrimeHeatmapTileProvider;
import umd.project.safetymapexample.util.SharedPreferencesUtil;

import static com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom;
import static umd.project.safetymapexample.app.Config.TILE_OVERLAY_ZINDEX;

public class CustomMapFragment extends com.google.android.gms.maps.MapFragment
    implements OnMapReadyCallback, ICustomMapFragment {

  private static final String TAG = CustomMapFragment.class.getSimpleName();

  private GoogleMap mMap;
  private Circle mLocationCircle;
  private LatLng mQueriedLocation;
  private float mCameraZoomLevel;
  private TileOverlayManager mTileOverlays;
  private List<TileProvider> mTileProviders;

  public CustomMapFragment(){}

  @Override public void onCreate(Bundle bundle) {
    super.onCreate(bundle);
    getMapAsync(this);
    setRetainInstance(true);
    initTileProviders();
  }

  @Override public void onMapReady(GoogleMap googleMap) {
    mMap = googleMap;

    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    mMap.getUiSettings().setCompassEnabled(false);

    for (TileProvider tileProvider : mTileProviders) {
      mTileOverlays.add(mMap.addTileOverlay(
          new TileOverlayOptions().tileProvider(tileProvider).zIndex(TILE_OVERLAY_ZINDEX)));
    }

    mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
      @Override public void onCameraMove() {
        mCameraZoomLevel = mMap.getCameraPosition().zoom;
        updateLocationIconSize();
      }
    });

    mQueriedLocation = SharedPreferencesUtil.getLocation(this);  // TODO: fix
    mCameraZoomLevel = SharedPreferencesUtil.getZoomState(this);
    updateMapLocation();
  }

  @Override public void onPause() {
    SharedPreferencesUtil.saveZoomState(this, mCameraZoomLevel);
    super.onPause();
  }

  private void updateMapLocation(){
    updateLocationIcon();
    moveCamera();
  }

  public void updateMapLocation(LatLng location) {
    mQueriedLocation = location;
    updateMapLocation();
  }

  public void updateLocationIcon() {
    if (mLocationCircle != null) {  // TODO: fix
      mLocationCircle.remove();
    }

    mLocationCircle = mMap.addCircle(new CircleOptions().center(mQueriedLocation)
        .radius(10000 * (22 / mCameraZoomLevel))
        .strokeColor(Color.TRANSPARENT)
        .fillColor(Color.argb(100, 0, 77, 232)));
  }

  private void updateLocationIconSize() {
    if (mLocationCircle != null) {
      mLocationCircle.setRadius(10000 * (30 / mCameraZoomLevel));
    } else {
      mLocationCircle = mMap.addCircle(new CircleOptions().center(mQueriedLocation)
          .radius(10000 * (22 / mCameraZoomLevel))
          .strokeColor(Color.TRANSPARENT)
          .fillColor(Color.argb(100, 0, 77, 232)));
    }
  }

  private void moveCamera() {
    moveCamera(mQueriedLocation, mCameraZoomLevel);
  }

  private void moveCamera(LatLng location, float zoom) {
    mMap.moveCamera(newLatLngZoom(location, zoom));
  }

  private void initTileProviders() {
    mTileOverlays = new TileOverlayManager();  // TODO: edit layer controls to filter, etc.
    mTileProviders = new ArrayList<>();
    mTileProviders.add(new CrimeHeatmapTileProvider(256, 256));
  }

  @Override public void updateMapOverlays(Set<String> layers) {
    // TODO: add layer controls to filter
  }

}
