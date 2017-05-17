package umd.project.safetymapexample.app;

import android.Manifest;

public class Config {

    public static final String[] LOCATION_PERMISSION_GROUP = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    public static final int LOCATION_REQUEST_CODE = 638;

    public static final float TILE_OVERLAY_ZINDEX = 200.0f;

    public static final String WEATHER_API_KEY = "db7644c1d871bc40c9c5c09e105fd149";

    public static final String WEATHER_TILE_PROVIDER_URL = "http://tile.openweathermap.org/map";

    public final static String CRIME_TILE_PROVIDER_URL = "http://35.185.45.122/crime";

}
