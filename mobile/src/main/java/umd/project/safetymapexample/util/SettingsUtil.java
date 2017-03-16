package umd.project.safetymapexample.util;


import android.content.Intent;

import umd.project.safetymapexample.settings.Settings;

public class SettingsUtil {

    public static final String KEY_SETTINGS_CHANGED = "CHANGED";
    public static final String KEY_PREF_LOCATION = "LOCATION";
    public static final String KEY_PREF_DISPLAY_TYPE = "DISPLAY_TYPE";
    public static final String KEY_PREF_BENCHMARK = "BENCHMARK";
    public static final String KEY_PREF_ZOOM = "ZOOM_LEVEL";
    public static final String KEY_PREF_RADIUS = "RADIUS";

    private static final String HEATMAP = "Heatmap";
    private static final String MARKERS = "Markers";

    public static int getDisplayTypeFromString(String displayTypeString) {
        int displayType;
        switch (displayTypeString) {
            case (HEATMAP):
                displayType = Settings.Overlay.HEATMAP;
                break;
            case (MARKERS):
                displayType = Settings.Overlay.MARKERS;
                break;
            default:
                displayType = Settings.Overlay.HEATMAP;
                break;
        }
        return displayType;
    }

    public static boolean radiusChanged(Intent changes){
        return changes.getFloatExtra(KEY_PREF_RADIUS, -1f) != -1f;
    }

    public static boolean locationChanged(Intent changes){
        return changes.getStringExtra(KEY_PREF_LOCATION) != null;
    }

}


