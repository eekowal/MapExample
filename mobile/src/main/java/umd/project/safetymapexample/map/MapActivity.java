package umd.project.safetymapexample.map;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import umd.project.safetymapexample.R;
import umd.project.safetymapexample.settings.SettingsActivity;
import umd.project.safetymapexample.util.SettingsUtil;
import umd.project.safetymapexample.util.UiUtil;

import static umd.project.safetymapexample.util.UiUtil.getPeakHeightForDevice;


public class MapActivity extends AppCompatActivity  {

    private static final String TAG = MapActivity.class.getSimpleName();

    private static final int REQUEST_SETTINGS = 33;

    private MapFragment mMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_map_activity);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapActivity.this, SettingsActivity.class);
                startActivityForResult(intent, REQUEST_SETTINGS);
            }
        });

        mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.fragment_map);
        mMapFragment.setRetainInstance(true);

        setupBottomSheet(fab);

        adjustHeightOfMap();
    }

    private void setupBottomSheet(final FloatingActionButton fab) {
        int mPeakHeight = getPeakHeightForDevice(this);

        android.support.v4.app.Fragment bottomSheetFragment = getSupportFragmentManager()
                .findFragmentById(R.id.bottom_sheet);

        if (bottomSheetFragment != null) {

            View view = bottomSheetFragment.getView();

            BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(view);
            bottomSheetBehavior.setPeekHeight(mPeakHeight);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }
    }

    private void adjustHeightOfMap(){
        ViewGroup.LayoutParams params = mMapFragment.getView().getLayoutParams();
        params.height = (int) UiUtil.getHeightOfMap(this);
        mMapFragment.getView().setLayoutParams(params);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_SETTINGS && resultCode == RESULT_OK && data != null){
            if (data.getBooleanExtra(SettingsUtil.KEY_SETTINGS_CHANGED, false)){
                mMapFragment.updateMap(data);  // if there were changes to the settings, update map
            }
        }
    }
}
