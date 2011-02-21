package org.liberty.android.anyunlock;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.content.pm.ActivityInfo;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.content.res.Configuration;

public class SettingsScreen extends PreferenceActivity{

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.settings);
    	SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
    }

}
