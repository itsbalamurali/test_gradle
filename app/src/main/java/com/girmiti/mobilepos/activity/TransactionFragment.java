package com.girmiti.mobilepos.activity;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.girmiti.mobilepos.R;
import com.girmiti.mobilepos.logger.Logger;

import static com.girmiti.mobilepos.logger.Logger.getNewLogger;

/**
 * Created by nayan on 18/10/17.
 */
public class TransactionFragment extends android.preference.PreferenceFragment {

    private final Logger logger = getNewLogger( PreferenceFragments.class.getName() );

    public void setSettingsResource(int id) {
        addPreferencesFromResource( id );
        setupHandlers();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = super.onCreateView( inflater, container, savedInstanceState );

        ListView lv = (ListView) v.findViewById( android.R.id.list );
        lv.setBackgroundColor( 0xffffffff );
        lv.setCacheColorHint( 0xffffffff );
        ViewGroup.LayoutParams lp = lv.getLayoutParams();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lv.setLayoutParams( lp );
        lv.setPadding( 0, 0, 0, 0 );
        return v;
    }

    void setupHandlers() {

        EditTextPreference prefs = (EditTextPreference) findPreference( getResources().getString( R.string.key_name ) );
        if (prefs != null) {
            prefs.setShouldDisableView( false );
            prefs.setSummary( prefs.getSharedPreferences()
                    .getString( getResources().getString( R.string.key_name ), "" ) );
            prefs.setOnPreferenceChangeListener( new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference pref, Object name) {
                    pref.setSummary( (String) name );
                    return true;
                }
            } );
        }

        prefs = (EditTextPreference) findPreference( getResources().getString( R.string.key_location ) );
        if (prefs != null) {
            prefs.setSummary( prefs.getSharedPreferences()
                    .getString( getResources().getString( R.string.key_location ), "" ) );
            prefs.setOnPreferenceChangeListener( new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference pref, Object name) {
                    pref.setSummary( (String) name );
                    return true;
                }
            } );
        }
        prefs = (EditTextPreference) findPreference( getResources().getString( R.string.key_email ) );
        if (prefs != null) {
            prefs.setShouldDisableView( false );
            prefs.setSummary( prefs.getSharedPreferences()
                    .getString( getResources().getString( R.string.key_email ), "" ) );
            prefs.setOnPreferenceChangeListener( new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference pref, Object name) {
                    pref.setSummary( (String) name );
                    return true;
                }
            } );
        }
        prefs = (EditTextPreference) findPreference( getResources().getString( R.string.key_terminal ) );
        if (prefs != null) {
            prefs.setShouldDisableView( false );
            prefs.setSummary( prefs.getSharedPreferences()
                    .getString( getResources().getString( R.string.key_terminal ), "" ) );
            prefs.setOnPreferenceChangeListener( new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference pref, Object name) {
                    pref.setSummary( (String) name );
                    return true;
                }
            } );
        }
        prefs = (EditTextPreference) findPreference( getResources().getString( R.string.key_merchant ) );
        if (prefs != null) {
            prefs.setShouldDisableView( false );
            prefs.setSummary( prefs.getSharedPreferences()
                    .getString( getResources().getString( R.string.key_merchant ), "" ) );
            prefs.setOnPreferenceChangeListener( new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference pref, Object name) {
                    pref.setSummary( (String) name );
                    return true;
                }
            } );
        }
        prefs = (EditTextPreference) findPreference( getResources().getString( R.string.key_mobile ) );
        if (prefs != null) {
            prefs.setSummary( prefs.getSharedPreferences()
                    .getString( getResources().getString( R.string.key_mobile ), "" ) );
            prefs.setOnPreferenceChangeListener( new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference pref, Object name) {
                    pref.setSummary( (String) name );
                    return true;
                }
            } );
        }
        prefs = (EditTextPreference) findPreference( getResources().getString( R.string.key_ip ) );
        if (prefs != null) {
            prefs.setSummary( prefs.getSharedPreferences()
                    .getString( getResources().getString( R.string.key_ip ), "" ) );
            prefs.setOnPreferenceChangeListener( new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference pref, Object name) {
                    pref.setSummary( (String) name );
                    return true;
                }
            } );
        }
        prefs = (EditTextPreference) findPreference( getResources().getString( R.string.key_port ) );
        if (prefs != null) {
            prefs.setSummary( prefs.getSharedPreferences()
                    .getString( getResources().getString( R.string.key_port ), "" ) );
            prefs.setOnPreferenceChangeListener( new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference pref, Object name) {
                    pref.setSummary( (String) name );
                    return true;
                }
            } );
        }

        ListPreference listPreference = (ListPreference) findPreference( getResources().getString( R.string.key_list ) );
        try {
            if (listPreference.getValue().equalsIgnoreCase( getResources().getString( R.string.http ) )) {
                listPreference.setSummary( getResources().getString( R.string.http ) );
            } else {
                listPreference.setSummary( getResources().getString( R.string.https ) );
            }
        } catch (Exception e) {
            logger.severe( "Error in displaying summary" + e );
        }
    }
}