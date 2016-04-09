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
import antbuddy.htk.com.antbuddy2016.service.AntbuddyService;
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
        ABSharedPreference.triggerCurrentScreen(ABSharedPreference.CURRENTSCREEN.LOGIN_ACTIVITY);
        if(AntbuddyService.mAntbuddyService == null) {
            startService(new Intent(this, AntbuddyService.class));
        }
        emailStr    = ABSharedPreference.getAccountConfig().getEmail();
        passwordStr = ABSharedPreference.getAccountConfig().getPassword();
        ABSharedPreference.save(ABSharedPreference.KEY_IS_LOGIN, false);

        LogHtk.i(LogHtk.Test1, "emailStr=" + emailStr);
        LogHtk.i(LogHtk.Test1, "passwordStr=" + passwordStr);

        initViews();
        registerListeners();
        setUpUIState();

        ABSharedPreference.resetAccountInSharedPreferences();
        ABSharedPreference.resetXMPP();

        AndroidHelper.warningInternetConnection(this);
    }

    private void initViews() {
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        cbRememberPassword = (CheckBox) findViewById(R.id.cbRememberPassword);
        cbShowPassword = (CheckBox) findViewById(R.id.cbShowPassword);
        btnForgotPassword = (Button) findViewById(R.id.btnForgotPassword);
        progressBar_Login = (ProgressBar) findViewById(R.id.progressBar_Login);
        accept_login_Button = (Button) findViewById(R.id.accept_login_Button);
    }

    private void registerListeners() {
        etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (etEmail.length() > 0 && etPassword.length() > 0) {
                        accept_login_Button.performClick();
                    }
                }
                return false;
            }
        });

        cbShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    etPassword.setInputType(129);
                }
            }
        });

        cbRememberPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ABSharedPreference.save(ABSharedPreference.KEY_REMEMBER_PASSWORD, true);
                } else {
                    ABSharedPreference.save(ABSharedPreference.KEY_REMEMBER_PASSWORD, false);
                }
            }
        });

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
    }

    private void setUpUIState() {
        if (emailStr.length() > 0) {
            etEmail.setText(emailStr);
        }
        if (ABSharedPreference.getBoolean(ABSharedPreference.KEY_REMEMBER_PASSWORD)) {
            cbRememberPassword.setChecked(true);
            etPassword.setText(passwordStr);
        }
    }

    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(LoginActivity.this, LoReActivity.class);
        startActivity(myIntent);
        finish();
    }

    private void requestAPI() {
        if (!AndroidHelper.isInternetAvailable(getApplicationContext())) {
            AndroidHelper.showToast("No network connection available!", LoginActivity.this);
            return;
        }

        AndroidHelper.showProgressBar(LoginActivity.this, progressBar_Login);
        AndroidHelper.setEnabledWithView(LoginActivity.this, accept_login_Button, false);

        emailStr    = etEmail.getText().toString().trim();
        passwordStr = etPassword.getText().toString().trim();
        APIManager.GETLogin(emailStr, passwordStr, new HttpRequestReceiver<Token>() {
            @Override
            public void onSuccess(Token token) {
                tokenStr = "Bearer " + token.getToken();
                if (tokenStr.length() > 0) {
                    ABSharedPreference.saveABAcountConfig(emailStr, passwordStr, tokenStr, null);

                    Intent myIntent = new Intent(LoginActivity.this, DomainActivity.class);
                    startActivity(myIntent);
                    finish();
                }

                AndroidHelper.hideProgressBar(LoginActivity.this, progressBar_Login);
                AndroidHelper.setEnabledWithView(LoginActivity.this, accept_login_Button, true);
            }

            @Override
            public void onError(String error) {
                AndroidHelper.hideProgressBar(LoginActivity.this, progressBar_Login);
                AndroidHelper.setEnabledWithView(LoginActivity.this, accept_login_Button, true);
                APIManager.showToastWithCode(error, LoginActivity.this);
            }
        });
    }

    private void warningTryLogin() {
        AndroidHelper.hideProgressBar(LoginActivity.this, progressBar_Login);
        AndroidHelper.setEnabledWithView(LoginActivity.this, accept_login_Button, true);
        AndroidHelper.showToast("Please try again!", LoginActivity.this);
    }
}
