package antbuddy.htk.com.antbuddy2016.modules.login.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

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
        Call<Token> call = AntbuddyApplication.getInstance().getApiService().getToken("thanh.nguyen@htklabs.com", "doipasshoairuatroi");
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Response<Token> response) {
                LogHtk.d(LogHtk.API_TAG, "onSuccess: " + response.toString());

//                Constants.token = "Bearer " + ParseJson.getStringWithKey((JSONObject)response, JSONKey.token);
                Constants.token = "Bearer " + response.body().getToken();

                if (Constants.token.length() > 0) {
                    Intent myIntent = new Intent(LoginActivity.this, DomainActivity.class);
                    startActivity(myIntent);
                    finish();
                }

                AndroidHelper.hideProgressBar(LoginActivity.this, progressBar_Login);
                AndroidHelper.setEnabledWithView(LoginActivity.this, accept_login_Button, true);
            }

            @Override
            public void onFailure(Throwable t) {
                LogHtk.e(LogHtk.API_TAG, "Login error: " + t.toString());
                AndroidHelper.hideProgressBar(LoginActivity.this, progressBar_Login);
                AndroidHelper.setEnabledWithView(LoginActivity.this, accept_login_Button, true);

                AndroidHelper.showToast("Please try again!", LoginActivity.this);
            }
        });
    }
}
