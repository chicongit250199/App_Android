package antbuddy.htk.com.antbuddy2016.modules.login.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.api.APIManager;
import antbuddy.htk.com.antbuddy2016.interfaces.HttpRequestReceiver;
import antbuddy.htk.com.antbuddy2016.model.NewAccount;
import antbuddy.htk.com.antbuddy2016.model.OrganizationExist;
import antbuddy.htk.com.antbuddy2016.setting.ABSharedPreference;
import antbuddy.htk.com.antbuddy2016.util.AndroidHelper;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;

/**
 * Created by thanhnguyen on 28/03/2016.
 */
public class CreateAccountActivity extends Activity {

    private ProgressBar progressBar_CreateAccount;
    private Button btnCreateAccount;

    private EditText etUserName;
    private EditText etFullName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etCompanyName;
    private EditText etCompanyDomain;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ABSharedPreference.triggerCurrentScreen(ABSharedPreference.CURRENTSCREEN.CREATE_ACCOUNT_ACTIVITY);

        setContentView(R.layout.activity_createaccount);
        initViews();
        viewListeners();

        AndroidHelper.warningInternetConnection(this);
    }

    private void initViews() {
        etUserName      = (EditText) findViewById(R.id.etUserName);
        etFullName      = (EditText) findViewById(R.id.etFullName);
        etEmail         = (EditText) findViewById(R.id.etEmail);
        etPassword      = (EditText) findViewById(R.id.etPassword);
        etCompanyName   = (EditText) findViewById(R.id.etCompanyName);
        etCompanyDomain = (EditText) findViewById(R.id.etCompanyDomain);

        progressBar_CreateAccount = (ProgressBar) findViewById(R.id.progressBar_CreateAccount);
        btnCreateAccount          = (Button) findViewById(R.id.btnCreateAccount);
    }

    private void viewListeners() {
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //requestAPI();
                createNewAccount();
            }
        });

        etUserName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                etUserName.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                return false;
            }
        });

        etFullName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                etFullName.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                return false;
            }
        });

        etEmail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                etEmail.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                return false;
            }
        });

        etPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                etPassword.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                return false;
            }
        });

        etCompanyName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                etCompanyName.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                return false;
            }
        });

        etCompanyDomain.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                etCompanyDomain.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                return false;
            }
        });

        etCompanyDomain.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                btnCreateAccount.performClick();
                return false;
            }
        });
    }

    private void requestAPI() {
        if (!AndroidHelper.isInternetAvailable(getApplicationContext())) {
            AndroidHelper.showToast("No network connection available!", CreateAccountActivity.this);
            return;
        }

        AndroidHelper.showProgressBar(CreateAccountActivity.this, progressBar_CreateAccount);
        APIManager.POSTCheckOrganizationExist(etCompanyDomain.getText().toString().trim(), new HttpRequestReceiver<OrganizationExist>() {
            @Override
            public void onSuccess(OrganizationExist orgExist) {
                if (orgExist.isExist()) {
                    createNewAccount();
                } else {
                    AndroidHelper.showToast("Company domain is existed!", CreateAccountActivity.this);
                    AndroidHelper.hideProgressBar(CreateAccountActivity.this, progressBar_CreateAccount);
                }
            }

            @Override
            public void onError(String error) {
                LogHtk.e(LogHtk.API_TAG, "Error to check Organization Exist!!" + error);
                AndroidHelper.hideProgressBar(CreateAccountActivity.this, progressBar_CreateAccount);
                APIManager.showToastWithCode(error, CreateAccountActivity.this);
            }
        });
    }

    private void createNewAccount() {
        if (!AndroidHelper.isInternetAvailable(getApplicationContext())) {
            AndroidHelper.showToast("No network connection available!", CreateAccountActivity.this);
            return;
        }

        String userName      = etUserName.getText().toString().trim();
        String fullName      = etFullName.getText().toString().trim();
        String email         = etEmail.getText().toString().trim();
        String password      = etPassword.getText().toString().trim();
        String companyName   = etCompanyName.getText().toString().trim();
        String companyDomain = etCompanyDomain.getText().toString().trim();

        APIManager.POSTCreateNewAccount(userName, fullName, email, password, companyName, companyDomain, new HttpRequestReceiver<NewAccount>() {
            @Override
            public void onSuccess(NewAccount newAccount) {
                LogHtk.i(LogHtk.API_TAG, "newAccount: " + newAccount.toString());
                if (newAccount.isOk()) {
                    AndroidHelper.showToast("Please activate this account in your email: " + etEmail.getText().toString().trim(), CreateAccountActivity.this);

                    ABSharedPreference.save(ABSharedPreference.KEY_EMAIL, etEmail.getText().toString().trim());
                    ABSharedPreference.save(ABSharedPreference.KEY_PASSWORD, etPassword.getText().toString().trim());
                    ABSharedPreference.save(ABSharedPreference.KEY_REMEMBER_PASSWORD, true);

                    LogHtk.i(LogHtk.API_TAG, "Created new Account: " + newAccount.toString());
                    Intent myIntent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                    startActivity(myIntent);
                    finish();
                } else {
                    NewAccount.MessageNewAccount message = newAccount.getMessage();
                    if (message != null) {
                        String email = message.getEmail();
                        if (email != null && email.length() > 0) {
                            AndroidHelper.showToast(email, CreateAccountActivity.this);
                            etEmail.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.ab_red));
                        }

                        String password = message.getPassword();
                        if (password != null && password.length() > 0) {
                            AndroidHelper.showToast(password, CreateAccountActivity.this);
                            etPassword.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.ab_red));
                        }

                        String organization = message.getOrganization();
                        if (organization != null && organization.length() > 0) {
                            AndroidHelper.showToast(organization, CreateAccountActivity.this);
                            etCompanyName.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.ab_red));
                        }

                        String domain = message.getDomain();
                        if (domain != null && domain.length() > 0) {
                            AndroidHelper.showToast(domain, CreateAccountActivity.this);
                            etCompanyDomain.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.ab_red));
                        }

                        String username = message.getUsername();
                        if (username != null && username.length() > 0) {
                            AndroidHelper.showToast(username, CreateAccountActivity.this);
                            etUserName.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.ab_red));
                        }

                        String name = message.getName();
                        if (name != null && name.length() > 0) {
                            AndroidHelper.showToast(name, CreateAccountActivity.this);
                            etFullName.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.ab_red));
                        }

                        LogHtk.e(LogHtk.API_TAG, "Warning! Cannot create new account: " + newAccount.toString());
                    }
                }
            }

            @Override
            public void onError(String error) {
                LogHtk.e(LogHtk.API_TAG, "Error to check Organization Exist!!" + error);
                AndroidHelper.hideProgressBar(CreateAccountActivity.this, progressBar_CreateAccount);
                APIManager.showToastWithCode(error, CreateAccountActivity.this);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(CreateAccountActivity.this, LoReActivity.class);
        startActivity(myIntent);
        finish();
    }
}
