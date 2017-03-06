package umd.project.safetymapexample.map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import umd.project.safetymapexample.R;
import umd.project.safetymapexample.SettingsActivity;
import umd.project.safetymapexample.util.PermissionsUtils;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String TAG = MapActivity.class.getSimpleName();

    private static final int REQUEST_LOCATION_PERMISSION = 484;

    private static final int SETTINGS_REQUEST = 33;

    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private GoogleMap mMap;
    private MapFragment mMapFragment;

    private int mPeakHeight;

    // state of bottom sheet when rotated

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_bottomsheet);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_map_activity);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MapActivity.this, SettingsActivity.class);
                String transitionName = getString(R.string.settings_transition);
                ActivityOptionsCompat options =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(MapActivity.this,
                                fab,
                                transitionName
                        );

                ActivityCompat.startActivityForResult(MapActivity.this, intent, SETTINGS_REQUEST, options.toBundle());

            }
        });


        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.bottom_sheet);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(linearLayout);

        mPeakHeight = (int) getPeakHeight();

        Log.i(TAG, "onCreate: " + mPeakHeight);
        bottomSheetBehavior.setPeekHeight(mPeakHeight);

        mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.fragment_map);

        findViewById(R.id.bottom_sheet).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

    }

    private float getPeakHeight(){
        // 2/5 of the screen height
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);

        int height = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? size.y : size.x;

        return (height/5.0f) * 2.0f;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        attemptEnableMyLocation();
    }

    public void attemptEnableMyLocation() {
        // Check if the permission has already been granted.
        if (PermissionsUtils.permissionAlreadyGranted(this, PERMISSIONS)) {
            // Permission has been granted.
            if (mMapFragment != null) {
                //mMapFragment.setMyLocationEnabled(true);
                return;
            }
        }

        // The permissions have not been granted yet. Request them.
        ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_LOCATION_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode,
                                           @NonNull final String[] permissions,
                                           @NonNull final int[] grantResults) {

        if (requestCode != REQUEST_LOCATION_PERMISSION) {
            return;
        }

        if (permissions.length == PERMISSIONS.length &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission has been granted.
            if (mMapFragment != null) {
                //mMapFragment.setMyLocationEnabled(true);
            }
        } else {
            // Permission was denied. Display error message that disappears after a short while.
            Toast.makeText(this, "Permission was denied.", Toast.LENGTH_SHORT).show();

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void setDisplay(int displayType){
        switch(displayType){
            case(R.id.heatmap):
                mMapFragment.displayAsHeatMap();
                break;
            case(R.id.markers):
                mMapFragment.displayAsMarkers();
                break;
            default:
                Toast.makeText(this, "Display type not implemented!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setLocation(double[] location){
        LatLng loc = new LatLng(location[0], location[1]);
        mMapFragment.changeCameraLocation(loc, 10.0f);
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        Log.i(TAG, "onActivityReenter: ");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTINGS_REQUEST && resultCode == RESULT_OK && data != null){
            int displayType = data.getIntExtra("DISPLAY_TYPE", -1);
            double[] loc = data.getDoubleArrayExtra("LOCATION");

            setDisplay(displayType);

            if (loc != null){
                setLocation(loc);
            }
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_map_activity);
        Log.i(TAG, "onActivityResult: " + fab.isShown());
        Log.i(TAG, "onActivityResult: " + fab.getElevation());

        fab.invalidate();

        Log.i(TAG, "onActivityResult: " + fab.isShown());
        Log.i(TAG, "onActivityResult: " + fab.getElevation());


    }

}
