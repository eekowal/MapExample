package umd.project.safetymapexample.map;

import android.Manifest;
import android.app.Fragment;
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
import umd.project.safetymapexample.util.UiUtils;


public class MapActivity extends AppCompatActivity  {

    private static final String TAG = MapActivity.class.getSimpleName();

    private static final int SETTINGS_REQUEST = 33;

    private GoogleMap mMap;
    private MapFragment mMapFragment;

    private int mPeakHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_map_activity);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MapActivity.this, SettingsActivity.class);

                String transitionName = getResources().getString(R.string.settings_transition);
                ActivityOptionsCompat options =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(MapActivity.this,
                                fab,
                                transitionName
                        );

                ActivityCompat.startActivityForResult(MapActivity.this, intent, SETTINGS_REQUEST, options.toBundle());

            }
        });


        mPeakHeight = (int) UiUtils.getPeakHeight(this);

        mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.fragment_map);

    }

    private void setupBottomSheet(){
        Fragment fragment = (Fragment) getFragmentManager().findFragmentById(R.id.bottom_sheet);



        LinearLayout linearLayout = (LinearLayout) fragment.getView().findViewById(R.id.linearlayout);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(linearLayout);
        bottomSheetBehavior.setPeekHeight(mPeakHeight);

        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTINGS_REQUEST && resultCode == RESULT_OK && data != null){
            int displayType = data.getIntExtra("DISPLAY_TYPE", -1);
            double[] loc = data.getDoubleArrayExtra("LOCATION");

            setDisplay(displayType);

            if (loc != null && loc.length > 1){
                LatLng location = new LatLng(loc[0], loc[1]);
                mMapFragment.changeCameraLocation(location, 10.0f);
            }
        }

    }

}
