package umd.project.safetymapexample.app;

import android.Manifest;

public class Config {

  public static final String[] LOCATION_PERMISSION_GROUP = new String[] {
      Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE
  };

  public static final float TILE_OVERLAY_ZINDEX = 20.0f;

  public final static String TILE_PROVIDER_URL =
      "https://firebasestorage.googleapis.com/v0/b/safetymap-a90dc.appspot.com/o/raster-tiles-test%s%s%s.png?alt=media&token=d85eb445-11f1-4c3c-a3b3-3222d47af17a";

  public static final int LOCATION_REQUEST_CODE = 638;
}
