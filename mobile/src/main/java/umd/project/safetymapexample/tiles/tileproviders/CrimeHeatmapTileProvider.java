package umd.project.safetymapexample.tiles.tileproviders;

import android.util.Log;
import com.google.android.gms.maps.model.UrlTileProvider;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import static android.content.ContentValues.TAG;
import static umd.project.safetymapexample.app.Config.TILE_PROVIDER_URL;

public class CrimeHeatmapTileProvider extends UrlTileProvider {

  final static String ID = "CRIME_TILE_PROVIDER";

  public CrimeHeatmapTileProvider(int width, int height) {
    super(width, height);
  }

  @Override public URL getTileUrl(int i, int i1, int i2) {
    String s = String.format(Locale.US, TILE_PROVIDER_URL, "%2F" + i2, "%2F" + i, "%2F" + i1);
    URL url = null;
    try {
      url = new URL(s);
    } catch (MalformedURLException e) {
      throw new AssertionError(e);
    }

    Log.i(TAG, "getTileUrl: x:" + i + " y:" + i1 + " z:" + i2);
    Log.i(TAG, "getTileUrl: " + url);

    return url;
  }

}
