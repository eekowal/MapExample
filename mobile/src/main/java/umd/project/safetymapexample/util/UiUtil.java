package umd.project.safetymapexample.util;

import android.content.res.Configuration;
import android.graphics.Point;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class UiUtil {

    public static void displaySimpleSnackBar(View view, String message) {
        final Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        }).show();
    }

    public static int getPeakHeightForDevice(AppCompatActivity context) {
        Point size = new Point();

        context.getWindowManager().getDefaultDisplay().getSize(size);

        int height = context.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT ? size.y : size.x;

        int adjustedHeight = (int) (height / 1.25);

        return (int) ((adjustedHeight / 5.0f) * 1.5f);
    }

    public static float getHeightOfMap(AppCompatActivity context) {
        int peakHeight = getPeakHeightForDevice(context);

        Point size = new Point();
        context.getWindowManager().getDefaultDisplay().getSize(size);

        int height = context.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT ? size.y - peakHeight: size.x - peakHeight ;

        return height;
    }
}