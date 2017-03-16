package umd.project.safetymapexample.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.PreferenceScreen;

import umd.project.safetymapexample.R;
import umd.project.safetymapexample.util.SettingsUtil;

import static umd.project.safetymapexample.util.SettingsUtil.KEY_SETTINGS_CHANGED;
import static umd.project.safetymapexample.util.SettingsUtil.getDisplayTypeFromString;

public class SettingsActivity extends AppCompatActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener,
        PreferenceFragmentCompat.OnPreferenceStartScreenCallback {
    private static final String TAG = SettingsActivity.class.getSimpleName();

    private Bundle mSettingsChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(SettingsFragment.FRAGMENT_TAG);
            if (fragment == null) {
                fragment = new SettingsFragment();
            }

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(android.R.id.content, fragment, SettingsFragment.FRAGMENT_TAG);
            fragmentTransaction.commit();
        }

        mSettingsChanged = new Bundle();
    }

    @Override
    protected void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtras(mSettingsChanged);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case (SettingsUtil.KEY_PREF_BENCHMARK):
                mSettingsChanged.putBoolean(key, sharedPreferences.getBoolean(key, true));
                break;
            case (SettingsUtil.KEY_PREF_DISPLAY_TYPE):
                int displayType = getDisplayTypeFromString(sharedPreferences.getString(key, getString(R.string.heatmap)));
                mSettingsChanged.putInt(key, displayType);
                break;
            case (SettingsUtil.KEY_PREF_LOCATION):
                mSettingsChanged.putString(key, sharedPreferences.getString(key, getString(R.string.location1)));
                break;
            case (SettingsUtil.KEY_PREF_RADIUS):
            case (SettingsUtil.KEY_PREF_ZOOM):
                mSettingsChanged.putFloat(key, (float) sharedPreferences.getInt(key, 10));
                break;
        }
        mSettingsChanged.putBoolean(KEY_SETTINGS_CHANGED, true);
    }


    @Override
    public boolean onPreferenceStartScreen(PreferenceFragmentCompat caller, PreferenceScreen pref) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(PreferenceFragmentCompat.ARG_PREFERENCE_ROOT, pref.getKey());
        fragment.setArguments(args);
        fragmentTransaction.replace(android.R.id.content, fragment, pref.getKey());
        fragmentTransaction.addToBackStack(pref.getKey());
        fragmentTransaction.commit();
        return true;
    }
}
