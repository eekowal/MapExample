package umd.project.safetymapexample.tiles.tileproviders;

import android.util.Log;

import com.google.android.gms.maps.model.UrlTileProvider;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import static android.content.ContentValues.TAG;
import static umd.project.safetymapexample.app.Config.WEATHER_API_KEY;
import static umd.project.safetymapexample.app.Config.WEATHER_TILE_PROVIDER_URL;

public class WeatherTileProvider extends UrlTileProvider {

    final static String ID = "WEATHER_TILE_PROVIDER";

    private String mLayer;

    public WeatherTileProvider(int width, int height) {
        super(width, height);
    }

    public WeatherTileProvider(int width, int height, String layer) {
        super(width, height);
        mLayer = layer;
    }

    @Override
    public URL getTileUrl(int i, int i1, int i2) {

        String s = WEATHER_TILE_PROVIDER_URL + String.format(Locale.US,
                "/%s/%s/%s/%s@2x.png?appid=%s", mLayer, i2, i, i1, WEATHER_API_KEY);

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
