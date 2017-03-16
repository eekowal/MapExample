package umd.project.safetymapexample.settings;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import umd.project.safetymapexample.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    public static final String FRAGMENT_TAG = "my_preference_fragment";

    public SettingsFragment() {

    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // rootKey is the name of preference sub screen key name , here--customPrefKey
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

}
