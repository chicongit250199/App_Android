package antbuddy.htk.com.antbuddy2016.modules.login.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.interfaces.HttpRequestReceiver;
import antbuddy.htk.com.antbuddy2016.api.LoginAPI;
import antbuddy.htk.com.antbuddy2016.api.ParseJson;
import antbuddy.htk.com.antbuddy2016.util.AndroidHelper;
import antbuddy.htk.com.antbuddy2016.util.Constants;
import antbuddy.htk.com.antbuddy2016.util.JSONKey;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;

/**
 * Created by thanhnguyen on 28/03/2016.
 */
public class LoginActivity extends Activity {

    private Button accept_login_Button;
    private ProgressBar progressBar_Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressBar_Login = (ProgressBar) findViewById(R.id.progressBar_Login);
        accept_login_Button = (Button) findViewById(R.id.accept_login_Button);
        accept_login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidHelper.hideSoftKeyboard(LoginActivity.this);
                requestAPI();
            }
        });

        AndroidHelper.warningInternetConnection(this);
    }

    private void requestAPI() {
        if (!AndroidHelper.isInternetAvailable(getApplicationContext())) {
            AndroidHelper.showToast("No network connection available!", LoginActivity.this);
            return;
        }

        //antbuddytesting1@gmail.com/111qqq111
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {

                    // Request
                    AndroidHelper.showProgressBar(LoginActivity.this, progressBar_Login);
                    AndroidHelper.setEnabledWithView(LoginActivity.this, accept_login_Button, false);

                    LoginAPI.POSTLogin("antbuddytesting1@gmail.com", "111qqq111", new HttpRequestReceiver() {

                        @Override
                        public void onSuccess(String result) {
                            LogHtk.d(LogHtk.API_TAG, "onSuccess: " + result);

                            Constants.token = "Bearer " + ParseJson.getStringWithKey(result, JSONKey.token);
                            Intent myIntent = new Intent(LoginActivity.this, DomainActivity.class);
                            startActivity(myIntent);
                            finish();

                            AndroidHelper.hideProgressBar(LoginActivity.this, progressBar_Login);
                            AndroidHelper.setEnabledWithView(LoginActivity.this, accept_login_Button, true);
                        }

                        @Override
                        public void onError(String error) {
                            LogHtk.e(LogHtk.API_TAG, "onSuccess: " + error);
                            AndroidHelper.hideProgressBar(LoginActivity.this, progressBar_Login);
                            AndroidHelper.setEnabledWithView(LoginActivity.this, accept_login_Button, true);

                            AndroidHelper.showToast("Please try again!", LoginActivity.this);
                        }
                    }, getApplicationContext());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
