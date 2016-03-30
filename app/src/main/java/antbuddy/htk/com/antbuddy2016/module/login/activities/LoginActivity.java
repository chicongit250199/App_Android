package antbuddy.htk.com.antbuddy2016.module.login.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.api.HttpRequestReceiver;
import antbuddy.htk.com.antbuddy2016.api.LoginAPI;
import antbuddy.htk.com.antbuddy2016.api.Request;
import antbuddy.htk.com.antbuddy2016.module.center.activities.CenterActivity;
import antbuddy.htk.com.antbuddy2016.util.AndroidHelper;

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
    }

    private void requestAPI() {
        //antbuddytesting1@gmail.com/111qqq111
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {

                    // Request
                    AndroidHelper.showProgressBar(LoginActivity.this, progressBar_Login);
                    LoginAPI.login("antbuddytesting1@gmail.com", "111qqq111", new HttpRequestReceiver() {

                        @Override
                        public void onSuccess(String result) {
                            Log.d("DaiThanh", "Thanh Cong: " + result);
//                            Intent myIntent = new Intent(LoginActivity.this, DomainActivity.class);
                            Intent myIntent = new Intent(LoginActivity.this, CenterActivity.class);
                            startActivity(myIntent);

                            AndroidHelper.hideProgressBar(LoginActivity.this, progressBar_Login);
                        }

                        @Override
                        public void onError(String error) {
                            Log.d("DaiThanh", "That bai: " + error);
                            AndroidHelper.hideProgressBar(LoginActivity.this, progressBar_Login);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
