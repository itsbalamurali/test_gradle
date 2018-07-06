package com.girmiti.mobilepos.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.girmiti.mobilepos.R;
import com.girmiti.mobilepos.logger.Logger;
import com.girmiti.mobilepos.net.ServerCommunication;
import com.girmiti.mobilepos.net.model.ChangePasswordData;
import com.girmiti.mobilepos.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import static com.girmiti.mobilepos.R.id.img_left_toolbar_arrow;
import static com.girmiti.mobilepos.logger.Logger.getNewLogger;

/**
 * Created by nayan on 18/2/17.
 */
public class ForgotPasswordActivity extends BaseActivity {

    private EditText forgotEmail;
    private String errorMessage;
    private ProgressDialog progress;
    private String userName;
    public static final int MIN = 8;
    public static final int MAX = 16;
    private Logger logger = getNewLogger(ForgotPasswordActivity.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ImageView leftArrow;
        TextView tvToolbar;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        tvToolbar = (TextView) findViewById(R.id.tvToolbar_left);
        tvToolbar.setText(R.string.forgot_password);

        leftArrow = (ImageView) findViewById(img_left_toolbar_arrow);
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
            }
        });


        forgotEmail = (EditText) findViewById(R.id.email);

        findViewById(R.id.forgot_pass_cancel).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.forgot_pass_submit).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                userName = forgotEmail.getText().toString();
                if (userName == null ||"".equals(userName)) {
                    displayErrorDialogInHandler(getResources().getString(R.string.error_message), null);
                } else {
                    if (userName.length() >= MIN && userName.length() <= MAX) {
                        new ForgotPasswordTask().execute();
                    } else {
                        displayErrorDialogInHandler(getResources().getString(R.string.toast_value), null);
                    }
                }
            }
        });
    }

    private class ForgotPasswordTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            progress = new ProgressDialog(ForgotPasswordActivity.this);
            progress.setMessage(getResources().getString(R.string.processing));
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            String response = null;
            try {
                ChangePasswordData obj = new ChangePasswordData();
                obj.setUserName(userName);
                response = ServerCommunication.getInstance().forgotPasswordPostRequest(obj);
            } catch (Exception e) {
                logger.severe("Error in forgotPassword Post Request" + e);
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            if (progress != null)
                progress.dismiss();
               progress = null;

            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    errorMessage = jsonObject.getString("errorMessage");
                    if ((Constants.SUCCESS).equalsIgnoreCase(errorMessage)) {
                        Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        displaySuccesDialogInHandler(getResources().getString(R.string.forgot_password_msg), null);
                        forgotEmail.getText().clear();
                    } else {
                        displayErrorDialogInHandler(getResources().getString(R.string.toast_value), null);
                    }
                } catch (JSONException e) {
                    logger.severe("Error in Json Response" + e);
                }
            } else {
                displayErrorDialogInHandler(getResources().getString(R.string.error_generic), null);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
    }
}