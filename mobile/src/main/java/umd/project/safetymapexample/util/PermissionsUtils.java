package umd.project.safetymapexample.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;


public class PermissionsUtils {

    public static final String TAG = PermissionsUtils.class.getSimpleName();

    public static boolean permissionAlreadyGranted(@NonNull Context context, @NonNull String[] permissions) {
        for (String permission : permissions){
            if (ContextCompat.checkSelfPermission(context, permission) !=
                    PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

}
