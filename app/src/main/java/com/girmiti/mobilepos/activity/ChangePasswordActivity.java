package com.girmiti.mobilepos.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.girmiti.mobilepos.R;
import com.girmiti.mobilepos.logger.Logger;
import com.girmiti.mobilepos.net.ServerCommunication;
import com.girmiti.mobilepos.net.model.ChangePasswordData;
import com.girmiti.mobilepos.util.Constants;
import com.girmiti.mobilepos.util.FacadeApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.girmiti.mobilepos.R.id.img_left_toolbar_arrow;
import static com.girmiti.mobilepos.logger.Logger.getNewLogger;

/**
 * Created by nayan on 17/2/17.
 */
public class ChangePasswordActivity extends BaseActivity {
    private EditText existingPwdEditTxt;
    private EditText newPwdEditTxt;
    private EditText confirmPwdEditTxt;
    private ProgressDialog progress;
    private String oldpassword;
    private String newpassword;
    private String confirmpassword;
    private Logger logger = getNewLogger(ChangePasswordActivity.class.getName());
    private static final String PASSWORDREGEX = "(^(?=.*[0-9])(?=.*[a-z" + "0-9" + "A-Z])(?=.*[a-z" + "A-Z])(?=.*[@#$%`!^&()\\\\-_+=\\\\[\\\\]{};:'\\\\\\\",.<>\\\\/?|\\\\\\\\\\\\\\\\*])(?=\\S+$).{5,}$)";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        TextView toolBar;
        ImageView leftArrow;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_change_password);
        toolBar = (TextView) findViewById(R.id.tvToolbar_left);
        toolBar.setText("Change Password");
        leftArrow = (ImageView) findViewById(img_left_toolbar_arrow);
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
            }
        });

        existingPwdEditTxt = (EditText) findViewById(R.id.existing_pwd_edittxt);
        existingPwdEditTxt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constants.TWENTY)});
        newPwdEditTxt = (EditText) findViewById(R.id.new_pwd_edittxt);
        newPwdEditTxt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constants.TWENTY)});
        confirmPwdEditTxt = (EditText) findViewById(R.id.confirm_new_pwd_edittxt);
        confirmPwdEditTxt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constants.TWENTY)});

        findViewById(R.id.save_pwd_btn).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isValid()) {
                    oldpassword = existingPwdEditTxt.getText().toString();
                    newpassword = newPwdEditTxt.getText().toString();
                    confirmpassword = confirmPwdEditTxt.getText().toString();
                    new ChangePasswordTask().execute();
                }
            }
        });
        settingMenu(ChangePasswordActivity.this);
    }

    private class ChangePasswordTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            progress = new ProgressDialog(ChangePasswordActivity.this);
            progress.setMessage(getResources().getString(R.string.processing));
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            String response = null;
            try {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(FacadeApplication.getAppContext());
                ChangePasswordData obj = new ChangePasswordData();
                obj.setUserName(prefs.getString("email", ""));
                obj.setCurrentPassword(oldpassword);
                obj.setNewPassword(newpassword);
                obj.setConfirmPassword(confirmpassword);
                response = ServerCommunication.getInstance().changePasswordPostRequest(obj);

            } catch (Exception e) {
                logger.severe("Error while Changing the Password" + e);
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            if (progress != null)
                progress.dismiss();
            progress = null;
            String errorMessage = null;
            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    errorMessage = jsonObject.getString("errorMessage");
                    if ("success".equalsIgnoreCase(errorMessage)) {
                        displaySuccesDialogInHandler(getResources().getString(R.string.password_change), null);
                        clearAllData();
                    }
                } catch (JSONException e) {
                    logger.severe("Error occured While Password Change" + e);
                }
                if (errorMessage!=null && !errorMessage.equalsIgnoreCase("success")) {
                    displayErrorDialogInHandler(errorMessage, null);
                }

            }
        }
    }

    public void clearAllData() {
        existingPwdEditTxt.getText().clear();
        newPwdEditTxt.getText().clear();
        confirmPwdEditTxt.getText().clear();
    }

    public boolean isValid() {
        try {
            checkEmptyFields();
        } catch (Exception e) {
            if (e.getMessage().equals(getResources().getString(R.string.old_password))) {
                displayErrorDialogInHandler(getResources().getString(R.string.old_password), null);
                logger.info(getString(R.string.old_password) +e);
            } else if (e.getMessage().equals(getResources().getString(R.string.new_password))) {
                displayErrorDialogInHandler(getResources().getString(R.string.new_password), null);
                logger.info(getString(R.string.new_password) +e);
            } else if (e.getMessage().equals(getResources().getString(R.string.confirm_password))) {
                displayErrorDialogInHandler(getResources().getString(R.string.confirm_password), null);
                logger.info(getString(R.string.confirm_password) +e);
            } else if (e.getMessage().equals(getResources().getString(R.string.pwd_match))) {
                displayErrorDialogInHandler(getResources().getString(R.string.pwd_match), null);
                logger.info(getString(R.string.pwd_match) +e);
            } else if (e.getMessage().equals(getResources().getString(R.string.pwd_pattern))) {
                displayErrorDialogInHandler(getResources().getString(R.string.pwd_pattern), null);
                logger.info(getString(R.string.pwd_pattern) +e);
            } else {
                displayErrorDialogInHandler(getResources().getString(R.string.error_msg), null);
                logger.info(getString(R.string.error_msg) +e);
            }
            return false;
        }
        return true;
    }

    private void checkEmptyFields() {
        Editable editable = existingPwdEditTxt.getText();
        if (editable == null || "".equals(editable.toString()))
            throw new NullPointerException(getResources().getString(R.string.old_password));
        Editable editable1 = newPwdEditTxt.getText();
        if (editable1 == null || "".equals(editable1.toString()))
            throw new NullPointerException(getResources().getString(R.string.new_password));
        Editable editable2 = confirmPwdEditTxt.getText();
        if (editable2 == null || "".equals(editable2.toString()))
            throw new NullPointerException(getResources().getString(R.string.confirm_password));
        if (!newPwdEditTxt.getText().toString().equals(confirmPwdEditTxt.getText().toString())) {
            throw new NullPointerException(getResources().getString(R.string.pwd_match));
        }
        if (newPwdEditTxt.length() < Constants.FIFTEEN) {
            Pattern pattern = Pattern.compile(PASSWORDREGEX);

            Matcher matcher = pattern.matcher(newPwdEditTxt.getText().toString());

            if (!matcher.matches()) {
                throw new NullPointerException(getResources().getString(R.string.pwd_pattern));
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ChangePasswordActivity.this, SettingsListActivity.class);
        startActivity(intent);
        finish();
    }
}
