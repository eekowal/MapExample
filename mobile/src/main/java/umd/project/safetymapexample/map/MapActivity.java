package umd.project.safetymapexample.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import umd.project.safetymapexample.R;
import umd.project.safetymapexample.util.PermissionsUtils;



public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String TAG = MapActivity.class.getSimpleName();

    private static final int REQUEST_LOCATION_PERMISSION = 484;

    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private GoogleMap mMap;
    private MapFragment mMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            // reveal a sheet or something ?

            }
        });

        mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.fragment_map);
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

    /**
     * Enables the 'My Location' feature on the map fragment if the location permission has been
     * granted. If the permission is not available yet, it is requested.
     */
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.heatmap:
                mMapFragment.displayAsHeatMap();
                break;
            case R.id.clustered_markers:
                break;
            case R.id.markers:
                mMapFragment.displayAsMarkers();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
