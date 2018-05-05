package com.wawa_applications.wawa_tabor.pref;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import com.example.wojciechkrzywiec.wawa_tabor.R;

/**
 * Created by Wojtek Krzywiec on 06/08/2017.
 */

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}
