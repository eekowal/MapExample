package umd.project.safetymapexample.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import umd.project.safetymapexample.R;
import umd.project.safetymapexample.fragment.dialog.WeatherLayersDialogFragment;
import umd.project.safetymapexample.tiles.tileproviders.WeatherTileProvider;
import umd.project.safetymapexample.util.SharedPreferencesUtil;

public class WeatherFragment extends IDataViewFragment implements CustomMapFragment.MapReadyListener,
        WeatherLayersDialogFragment.OnMapLayersDialogListener {

    private static final String TAG = WeatherFragment.class.getSimpleName();

    private CustomMapFragment mCustomMapFragment;

    private CurrentLocationCallback mCurrentLocationCallback;

    private Set<String> mLayers;

    public WeatherFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (savedInstanceState == null) {
            mCustomMapFragment = new CustomMapFragment();
        }
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_weather, viewGroup, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

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
                WeatherLayersDialogFragment weatherLayersDialogFragment =
                        new WeatherLayersDialogFragment();
                weatherLayersDialogFragment.setTargetFragment(WeatherFragment.this, 0);
                weatherLayersDialogFragment.show(fm, "fragment_layers");
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getChildFragmentManager().beginTransaction().replace(R.id.map,
                (Fragment) mCustomMapFragment).commit();
        mCurrentLocationCallback = (CurrentLocationCallback) getActivity();
    }


    public void updateLocation(LatLng location) {
        mCustomMapFragment.updateMapLocation(location);
    }

    @Override
    public void onLayersChanged(Set<String> selectedLayers) {
        mCustomMapFragment.updateMapOverlays(getTileProvider(selectedLayers));
    }

    @Override
    public void onMapReady() {
        Set<String> selectedLayers = SharedPreferencesUtil
                .getSelectedWeatherMapLayers(this);
        mCustomMapFragment.updateMapOverlays(getTileProvider(selectedLayers));
    }

    public interface CurrentLocationCallback {
        void goToCurrentLocation();
    }

    private List<TileProvider> getTileProvider(Set<String> layers) {

        List<TileProvider> selectedTileProvider = new ArrayList<>();
        for (Object obj : layers.toArray()) {
            String selected = (String) obj;
            switch (selected) {
                case WeatherLayersDialogFragment.Type.TEMP:
                    selectedTileProvider.add(new WeatherTileProvider(256, 256, getString(R.string.temp)));
                    break;
                case WeatherLayersDialogFragment.Type.WINDS:
                    selectedTileProvider.add(new WeatherTileProvider(256, 256, getString(R.string.wind)));
                    break;
                case WeatherLayersDialogFragment.Type.RAIN:
                    selectedTileProvider.add(new WeatherTileProvider(256, 256, getString(R.string.rain)));
                    break;
            }
        }

        return selectedTileProvider;
    }

}
