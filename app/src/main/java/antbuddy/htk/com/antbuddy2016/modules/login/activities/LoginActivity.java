package antbuddy.htk.com.antbuddy2016.modules.login.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.api.APIManager;
import antbuddy.htk.com.antbuddy2016.interfaces.HttpRequestReceiver;
import antbuddy.htk.com.antbuddy2016.model.Token;
import antbuddy.htk.com.antbuddy2016.setting.ABSharedPreference;
import antbuddy.htk.com.antbuddy2016.util.AndroidHelper;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;

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

    private String emailStr;
    private String passwordStr;
    private String tokenStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailStr    = ABSharedPreference.getAccoungConfig().getEmail();
        passwordStr = ABSharedPreference.getAccoungConfig().getPassword();


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

        if (passwordStr.length() > 0) {
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

        if (emailStr.length() > 0) {
            etEmail.setText(emailStr);
        }

        if (passwordStr.length() > 0) {
            etPassword.setText(passwordStr);
        }

        progressBar_Login = (ProgressBar) findViewById(R.id.progressBar_Login);
        accept_login_Button = (Button) findViewById(R.id.accept_login_Button);
        accept_login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

        LoReActivity.resetAccountInSharedPreferences();
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

//      etEmail.getText().toString().trim()
//      etPassword.getText().toString().trim());
        APIManager.GETLogin("thanh.nguyen@htklabs.com", "doipasshoairuatroi", new HttpRequestReceiver<Token>() {
            @Override
            public void onSuccess(Token token) {
                if (token != null) {
                    //Constants.token = "Bearer " + response.body().getToken();
                    tokenStr = "Bearer " + token.getToken();
                    if (tokenStr.length() > 0) {
                        emailStr = etEmail.getText().toString().trim();
                        passwordStr = etPassword.getText().toString().trim();

                        saveInShared(emailStr, passwordStr, tokenStr);

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
            public void onError(String error) {
                LogHtk.e(LogHtk.API_TAG, "Login error: " + error.toString());
                warningTryLogin();
            }
        });
    }

    private void warningTryLogin() {
        AndroidHelper.hideProgressBar(LoginActivity.this, progressBar_Login);
        AndroidHelper.setEnabledWithView(LoginActivity.this, accept_login_Button, true);
        AndroidHelper.showToast("Please try again!", LoginActivity.this);
    }

    private void saveInShared(String email, String password, String token) {
        ABSharedPreference.saveABAcountConfig(email, password, token, null);
    }
}
