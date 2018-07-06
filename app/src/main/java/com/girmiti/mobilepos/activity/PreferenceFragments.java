package com.girmiti.mobilepos.activity;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.girmiti.mobilepos.R;
import com.girmiti.mobilepos.logger.Logger;

import static com.girmiti.mobilepos.logger.Logger.getNewLogger;

/**
 * Created by Girmiti on 8/11/16.
 */

public class PreferenceFragments extends android.preference.PreferenceFragment {

    private final Logger logger = getNewLogger(PreferenceFragments.class.getName());
    public void setSettingsResource(int id) {
        addPreferencesFromResource(id);
        setupHandlers();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = super.onCreateView(inflater, container, savedInstanceState);

        ListView lv = (ListView) v.findViewById(android.R.id.list);
        lv.setBackgroundColor(0xffffffff);
        lv.setCacheColorHint(0xffffffff);
        ViewGroup.LayoutParams lp = lv.getLayoutParams();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lv.setLayoutParams(lp);
        lv.setPadding(0, 0, 0, 0);
        return v;
    }

    void setupHandlers() {

        CheckBoxPreference checkBoxPreferenceTip = (CheckBoxPreference) findPreference(getResources().getString(R.string.key_tip));
        checkBoxPreferenceTip.setChecked(checkBoxPreferenceTip.isChecked());

        CheckBoxPreference checkBoxPreferenceDiscount = (CheckBoxPreference) findPreference(getResources().getString(R.string.key_discount));
        checkBoxPreferenceDiscount.setChecked(checkBoxPreferenceDiscount.isChecked());

        CheckBoxPreference checkBoxPreferenceBalence = (CheckBoxPreference) findPreference(getResources().getString(R.string.key_balence_enquiry));
        checkBoxPreferenceBalence.setChecked(checkBoxPreferenceBalence.isChecked());

        ListPreference listPreference = (ListPreference) findPreference(getResources().getString(R.string.key_list));
        try {
            if (listPreference.getValue().equalsIgnoreCase(getResources().getString(R.string.http))) {
                listPreference.setSummary(getResources().getString(R.string.http));
            } else {
                listPreference.setSummary(getResources().getString(R.string.https));
            }
        } catch (Exception e) {
            logger.severe("Error in displaying summary" +e);
        }

    }
}
