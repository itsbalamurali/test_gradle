package com.girmiti.mobilepos.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.girmiti.mobilepos.R;
import com.girmiti.mobilepos.logger.Logger;

import static com.girmiti.mobilepos.logger.Logger.getNewLogger;

/**
 * Author Girmiti Software
 * Created on  12/12/17.
 */
public class DeviceSettingFragment extends android.preference.PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final Logger logger = getNewLogger( DeviceSettingFragment.class.getName() );

    public void setSettingsResource(int id) {
        addPreferencesFromResource( id );
        setupHandlers();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = super.onCreateView( inflater, container, savedInstanceState );

        ListView lv = (ListView) v.findViewById( android.R.id.list );
        lv.setBackgroundColor( getResources().getColor( R.color.colorWhite ) ) ;
        lv.setCacheColorHint( getResources().getColor( R.color.colorWhite ) );
        ViewGroup.LayoutParams lp = lv.getLayoutParams();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lv.setLayoutParams( lp );
        lv.setPadding( 0, 0, 0, 0 );
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        //unregister the preferenceChange listener
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }
    void setupHandlers() {
        ListPreference listPreference = (ListPreference) findPreference( getResources().getString( R.string.key_device_list ) );
        try {
            if (listPreference.getValue().equalsIgnoreCase( getResources().getString( R.string.mobile_device ) )) {
                listPreference.setSummary( getResources().getString( R.string.mobile_device ) );
            } else {
                listPreference.setSummary( getResources().getString( R.string.z90_device ) );
            }
        } catch (Exception e) {
            logger.severe( "Error in displaying summary" + e );
        }

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        ListPreference listPreference = (ListPreference) findPreference( getResources().getString( R.string.key_device_list ) );
        try {
            if (listPreference.getValue().equalsIgnoreCase( getResources().getString( R.string.mobile_device ) )) {
                listPreference.setSummary( getResources().getString( R.string.mobile_device ) );
            } else {
                listPreference.setSummary( getResources().getString( R.string.z90_device ) );
            }
        } catch (Exception e) {
            logger.severe( "Error in displaying summary" + e );
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //unregister the preference change listener
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
