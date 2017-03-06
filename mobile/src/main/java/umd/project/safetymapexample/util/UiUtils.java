package umd.project.safetymapexample.util;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;



public class UiUtils {

    public static float getPeakHeight(AppCompatActivity context) {
        Point size = new Point();

        context.getWindowManager().getDefaultDisplay().getSize(size);

        int height = context.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT ? size.y : size.x;

        return (height / 5.0f) * 2.0f;
    }
}
