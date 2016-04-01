package antbuddy.htk.com.antbuddy2016.modules.login.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.util.AndroidHelper;

/**
 * Created by thanhnguyen on 28/03/2016.
 */
public class CreateAccountActivity extends Activity {

    private ProgressBar progressBar_CreateAccount;
    private Button accept_CreateAccount_Button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_createaccount);
        initViews();


        AndroidHelper.warningInternetConnection(this);
    }

    private void initViews() {
        progressBar_CreateAccount = (ProgressBar) findViewById(R.id.progressBar_CreateAccount);
        accept_CreateAccount_Button = (Button) findViewById(R.id.accept_CreateAccount_Button);
        accept_CreateAccount_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Request API
                requestAPI();

            }
        });
    }

    private void requestAPI() {
        if (!AndroidHelper.isInternetAvailable(getApplicationContext())) {
            AndroidHelper.showToast("No network connection available!", CreateAccountActivity.this);
            return;
        }

        //antbuddytesting1@gmail.com/111qqq111
        new Thread(new Runnable(){
            @Override
            public void run() {

//                // Check su ton tai Domain
//                https://antbuddy.com/api/organizations/checkexist POST
//                POST /api/organizations/checkexist
//                Posting Data:
//                {
//                    name: "htk inc"
//                }
//
//                /api/users/create




//                try {
//
//                    // Request
//                    AndroidHelper.showProgressBar(LoginActivity.this, progressBar_Login);
//                    AndroidHelper.setEnabledWithView(LoginActivity.this, accept_login_Button, false);
//
//                    LoginAPI.POSTLogin("antbuddytesting1@gmail.com", "111qqq111", new HttpRequestReceiver() {
//
//                        @Override
//                        public void onSuccess(String result) {
//                            Constants.token = "Bearer " + ParseJson.getStringWithKey(result, JSONKey.token);
//                            Log.d("DaiThanh", "Thanh Cong/ Token: " + Constants.token);
//
//                            Intent myIntent = new Intent(LoginActivity.this, DomainActivity.class);
//                            startActivity(myIntent);
//                            finish();
//
//                            AndroidHelper.hideProgressBar(LoginActivity.this, progressBar_Login);
//                            AndroidHelper.setEnabledWithView(LoginActivity.this, accept_login_Button, true);
//                        }
//
//                        @Override
//                        public void onError(String error) {
//                            AndroidHelper.hideProgressBar(LoginActivity.this, progressBar_Login);
//                            AndroidHelper.setEnabledWithView(LoginActivity.this, accept_login_Button, true);
//
//                            AndroidHelper.showToast("Please try again!", LoginActivity.this);
//                        }
//                    });
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        }).start();
    }
}
