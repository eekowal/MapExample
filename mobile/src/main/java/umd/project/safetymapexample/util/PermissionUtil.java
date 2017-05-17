package umd.project.safetymapexample.util;

import android.content.pm.PackageManager;

import umd.project.safetymapexample.app.Config;

import static umd.project.safetymapexample.app.Config.LOCATION_REQUEST_CODE;

public class PermissionUtil {

    public static boolean locationPermissionGranted(int requestCode, String[] permissions,
                                                    int[] grantResults) {
        return (requestCode == LOCATION_REQUEST_CODE && permissions.length >= 1
                && permissions[0].equals(Config.LOCATION_PERMISSION_GROUP[0])
                && grantResults[0] == PackageManager.PERMISSION_GRANTED);
    }
}
