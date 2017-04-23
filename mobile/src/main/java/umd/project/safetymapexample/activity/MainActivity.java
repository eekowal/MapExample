package umd.project.safetymapexample.activity;

import android.Manifest;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import umd.project.safetymapexample.R;
import umd.project.safetymapexample.fragment.CrimeFragment;
import umd.project.safetymapexample.fragment.FeedFragment;
import umd.project.safetymapexample.fragment.IDataViewFragment;

import static umd.project.safetymapexample.app.Config.LOCATION_PERMISSION_GROUP;
import static umd.project.safetymapexample.app.Config.LOCATION_REQUEST_CODE;
import static umd.project.safetymapexample.util.PermissionUtil.locationPermissionGranted;
import static umd.project.safetymapexample.util.SharedPreferencesUtil.saveLocation;
import static umd.project.safetymapexample.util.SharedPreferencesUtil.getLocation;
import static umd.project.safetymapexample.util.MapUtil.getLocationFromString;

public class MainActivity extends AppCompatActivity
    implements CrimeFragment.CurrentLocationCallback {

  private static final String TAG = MainActivity.class.getSimpleName();

  private IDataViewFragment mFragment;

  private LatLng mQueriedLocation;

  private String[] mDrawerMenuItems;
  private DrawerLayout mDrawerLayout;
  private ListView mDrawerList;

  private GoogleApiClient mGoogleApiClient;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    setupUi(savedInstanceState);

    if (mGoogleApiClient == null) {
      mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).build();
    }
  }

  protected void onStart() {
    super.onStart();
    mGoogleApiClient.connect();
  }

  @Override protected void onResume() {
    super.onResume();
    mQueriedLocation = getLocation(this);
  }

  protected void onStop() {
    mGoogleApiClient.disconnect();
    super.onStop();
  }

  @SuppressWarnings({ "MissingPermission" }) @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    if (locationPermissionGranted(requestCode, permissions, grantResults)) {
      Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
      mQueriedLocation = new LatLng(location.getLatitude(), location.getLongitude());
      saveLocation(this, mQueriedLocation);
      mFragment.updateLocation(mQueriedLocation);
    }
  }

  @Override public void goToCurrentLocation() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED) {
      Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
      mQueriedLocation = new LatLng(location.getLatitude(), location.getLongitude());
      saveLocation(this, mQueriedLocation);
      mFragment.updateLocation(mQueriedLocation);
    } else {
      requestPermissions(LOCATION_PERMISSION_GROUP, LOCATION_REQUEST_CODE);
    }
  }

  private void setupUi(Bundle savedInstanceState) {
    mDrawerMenuItems = getResources().getStringArray(R.array.menu_items);
    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    mDrawerList = (ListView) findViewById(R.id.left_drawer);

    mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

    findViewById(R.id.menu_settings).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        mDrawerLayout.openDrawer(Gravity.START, true);
      }
    });

    SearchView searchView = (SearchView) findViewById(R.id.search_view);
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override public boolean onQueryTextSubmit(String query) {
        switch (query.toLowerCase()) {
          case ("here"):
          case ("current"):
          case ("current location"):
            goToCurrentLocation();
            break;
          default:
            mQueriedLocation = getLocationFromString(query);
            saveLocation(MainActivity.this, mQueriedLocation);
            mFragment.updateLocation(mQueriedLocation);
            break;
        }
        return false;
      }

      @Override public boolean onQueryTextChange(String newText) {
        return false;
      }
    });

    setupBottomNavBar();

    if (savedInstanceState == null) {
      mFragment = (IDataViewFragment) new CrimeFragment();  // TODO: fragment creation
      getFragmentManager().beginTransaction()
          .replace(R.id.main_content, (Fragment) mFragment)
          .commit();
    } else {
      mFragment = (IDataViewFragment) getFragmentManager().findFragmentById(R.id.main_content);
    }
  }

  private void setupBottomNavBar() {
    BottomNavigationView bottomNavigationView =
        (BottomNavigationView) findViewById(R.id.bottom_nav_view);
    bottomNavigationView.setOnNavigationItemSelectedListener(
        new BottomNavigationView.OnNavigationItemSelectedListener() {
          @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
              case R.id.action_feed:
                // TODO: link to RSS feeds
                mFragment = new FeedFragment();
                break;
              case R.id.action_crime:
                mFragment = new CrimeFragment();
                break;
              case R.id.action_weather:
                // TODO: implement weather fragment, time permitting
                Snackbar.make(findViewById(android.R.id.content), "Todo", Snackbar.LENGTH_LONG)
                    .show();
                break;
            }

            getFragmentManager().beginTransaction()
                .replace(R.id.main_content, (Fragment) mFragment)
                .commit();

            return true;
          }
        });
  }

  private class DrawerItemClickListener implements ListView.OnItemClickListener {
    @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      // TODO: add notification settings, time permitting ...
    }
  }
}
