package umd.project.safetymapexample.tiles.tileproviders;

import android.util.Log;

import com.google.android.gms.maps.model.UrlTileProvider;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import static android.content.ContentValues.TAG;
import static umd.project.safetymapexample.app.Config.CRIME_TILE_PROVIDER_URL;


public class CrimeTileProvider extends UrlTileProvider {

    final static String ID = "CRIME_TILE_PROVIDER";

    private String mTileUrl;

    public CrimeTileProvider(int width, int height) {
        super(width, height);
        mTileUrl = CRIME_TILE_PROVIDER_URL;
    }

    public CrimeTileProvider(int width, int height, String crime) {
        super(width, height);
        mTileUrl = CRIME_TILE_PROVIDER_URL + "/" + crime;
    }

    public CrimeTileProvider(int width, int height, String crime, String time) {
        super(width, height);
        mTileUrl = CRIME_TILE_PROVIDER_URL + "/" + crime + "/" + time;
    }

    @Override
    public URL getTileUrl(int i, int i1, int i2) {

        String s = mTileUrl + String.format(Locale.US, "/%s/%s/%s@2x.png", i2, i, i1);

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
