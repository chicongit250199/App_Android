package antbuddy.htk.com.antbuddy2016.modules.login.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.interfaces.HttpRequestReceiver;
import antbuddy.htk.com.antbuddy2016.api.LoginAPI;
import antbuddy.htk.com.antbuddy2016.api.ParseJson;
import antbuddy.htk.com.antbuddy2016.model.Token;
import antbuddy.htk.com.antbuddy2016.service.AntbuddyApplication;
import antbuddy.htk.com.antbuddy2016.util.AndroidHelper;
import antbuddy.htk.com.antbuddy2016.util.Constants;
import antbuddy.htk.com.antbuddy2016.util.JSONKey;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * Created by thanhnguyen on 28/03/2016.
 */
public class LoginActivity extends Activity {

    private Button accept_login_Button;
    private ProgressBar progressBar_Login;

    private EditText etEmail;
    private EditText etPassword;
    private CheckBox cbRememberPassword;
    private CheckBox cbShowPassword;
    private Button btnForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);

        etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                   if (etEmail.length() > 0 && etPassword.length() > 0) {
                       accept_login_Button.performClick();
                   }
                }
                return false;
            }
        });

        cbRememberPassword = (CheckBox) findViewById(R.id.cbRememberPassword);

        if (Constants.password.length() > 0) {
            cbRememberPassword.setChecked(true);
        }

        cbShowPassword = (CheckBox) findViewById(R.id.cbShowPassword);
        cbShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    etPassword.setInputType(129);
                }
            }
        });

        btnForgotPassword = (Button) findViewById(R.id.btnForgotPassword);

        if (Constants.email.length() > 0) {
            etEmail.setText(Constants.email);
        }

        if (Constants.password.length() > 0) {
            etPassword.setText(Constants.password);
        }

        progressBar_Login = (ProgressBar) findViewById(R.id.progressBar_Login);
        accept_login_Button = (Button) findViewById(R.id.accept_login_Button);
        accept_login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.email = etEmail.getText().toString().trim();

                if (cbRememberPassword.isChecked()) {
                    Constants.password = etPassword.getText().toString().trim();
                } else {
                    Constants.password = "";
                }

                AndroidHelper.hideSoftKeyboard(LoginActivity.this);
                requestAPI();
            }
        });

        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidHelper.showToast("This feature will be available soon!", LoginActivity.this);
            }
        });

        LoReActivity.reset();
        LoReActivity.resetXMPP();

        AndroidHelper.warningInternetConnection(this);


    }

    private void requestAPI() {
        if (!AndroidHelper.isInternetAvailable(getApplicationContext())) {
            AndroidHelper.showToast("No network connection available!", LoginActivity.this);
            return;
        }

        AndroidHelper.showProgressBar(LoginActivity.this, progressBar_Login);
        AndroidHelper.setEnabledWithView(LoginActivity.this, accept_login_Button, false);
        Call<Token> call = AntbuddyApplication.getInstance().getApiService().GETLogin(etEmail.getText().toString().trim(), etPassword.getText().toString().trim());
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Response<Token> response) {
                Token token = response.body();
                if (token != null && response.isSuccess()) {
                    Constants.token = "Bearer " + response.body().getToken();

                    if (Constants.token.length() > 0) {
                        Intent myIntent = new Intent(LoginActivity.this, DomainActivity.class);
                        startActivity(myIntent);
                        finish();
                    }

                    AndroidHelper.hideProgressBar(LoginActivity.this, progressBar_Login);
                    AndroidHelper.setEnabledWithView(LoginActivity.this, accept_login_Button, true);
                } else {
                    warningTryLogin();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                LogHtk.e(LogHtk.API_TAG, "Login error: " + t.toString());
                warningTryLogin();
            }
        });
    }

    private void warningTryLogin() {
        AndroidHelper.hideProgressBar(LoginActivity.this, progressBar_Login);
        AndroidHelper.setEnabledWithView(LoginActivity.this, accept_login_Button, true);
        AndroidHelper.showToast("Please try again!", LoginActivity.this);
    }
}
