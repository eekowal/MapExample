package umd.project.safetymapexample.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.maps.model.UrlTileProvider;

import java.util.List;
import java.util.Set;

import umd.project.safetymapexample.R;
import umd.project.safetymapexample.fragment.dialog.CrimeLayersDialogFragment;
import umd.project.safetymapexample.tiles.tileproviders.CrimeTileProvider;
import umd.project.safetymapexample.util.SharedPreferencesUtil;

public class CrimeFragment extends IDataViewFragment implements CustomMapFragment.MapReadyListener,
        CustomStreetViewFragment.StreetViewReadyListener,
        CrimeLayersDialogFragment.OnCrimeLayersDialogListener {

    private static final String TAG = CrimeFragment.class.getSimpleName();

    private ICustomMapFragment mFragment;

    private CustomMapFragment mCustomMapFragment;
    private CustomStreetViewFragment mCustomStreetViewFragment;

    private CurrentLocationCallback mCurrentLocationCallback;

    private boolean mStreetViewEnabled;
    private Set<String> mLayers;
    private List<TileProvider> tileProviders;

    public CrimeFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (savedInstanceState == null) {
            mCustomStreetViewFragment = new CustomStreetViewFragment();
            mCustomMapFragment = new CustomMapFragment();
        }
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_crime, viewGroup, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getView().findViewById(R.id.btn_street_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStreetViewEnabled = !mStreetViewEnabled;
                SharedPreferencesUtil.saveStreetViewEnabled(CrimeFragment.this, mStreetViewEnabled);
                switchMapFragments();
            }
        });

        getView().findViewById(R.id.btn_current_location)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCurrentLocationCallback.goToCurrentLocation();
                    }
                });

        getView().findViewById(R.id.fab_layers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                CrimeLayersDialogFragment crimeLayersDialogFragment = new CrimeLayersDialogFragment();
                crimeLayersDialogFragment.setTargetFragment(CrimeFragment.this, 0);
                crimeLayersDialogFragment.show(fm, "fragment_layers");
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mStreetViewEnabled = SharedPreferencesUtil.getStreetViewEnabled(this);
        mFragment = mStreetViewEnabled ? mCustomStreetViewFragment : mCustomMapFragment;
        getChildFragmentManager().beginTransaction().replace(R.id.map, (Fragment) mFragment).commit();
        mCurrentLocationCallback = (CurrentLocationCallback) getActivity();
    }


    public void updateLocation(LatLng location) {
        mFragment.updateMapLocation(location);
    }

    @Override
    public void onLayersChanged(Set<String> selectedLayers) {
        mFragment.updateMapOverlays(getTileProvider(selectedLayers));
    }

    private void switchMapFragments() {
        mFragment = mStreetViewEnabled ? mCustomStreetViewFragment : mCustomMapFragment;
        getChildFragmentManager().beginTransaction()
                .replace(R.id.map, (Fragment) mFragment)
                .commit();
    }

    @Override
    public void onMapReady() {
        if (mFragment instanceof CustomMapFragment) {
            ((CustomMapFragment) mFragment).updateMapOverlays(
                    getTileProvider(SharedPreferencesUtil.getSelectedCrimeMapLayers(this)));
            showLayersButton();
        }
    }

    @Override
    public void onStreetViewReady() {
        if (mFragment instanceof CustomStreetViewFragment) {
            hideLayersButton();
        }
    }

    private void showLayersButton(){
        getView().findViewById(R.id.fab_layers).setVisibility(View.VISIBLE);
    }

    private void hideLayersButton(){
        getView().findViewById(R.id.fab_layers).setVisibility(View. INVISIBLE);
    }

    public interface CurrentLocationCallback {
        void goToCurrentLocation();
    }

    private UrlTileProvider getTileProvider(Set<String> layers) {
        String selected = (String) layers.toArray()[0];
        TileProvider selectedTileProvider = null;

        switch (selected) {
            case CrimeLayersDialogFragment.Type.CAR_THEFT:
                return new CrimeTileProvider(256, 256, getString(R.string.car_theft));
            case CrimeLayersDialogFragment.Type.ASSAULT:
                return new CrimeTileProvider(256, 256, getString(R.string.assault));
            case CrimeLayersDialogFragment.Type.SEX_OFFENSE:
                return new CrimeTileProvider(256, 256, getString(R.string.sex_offense));
            case CrimeLayersDialogFragment.Type.THEFT:
                return new CrimeTileProvider(256, 256, getString(R.string.theft));
            default:
                return new CrimeTileProvider(256, 256);
        }

    }

}
