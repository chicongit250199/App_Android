package antbuddy.htk.com.antbuddy2016.modules.login.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.setting.ABSharedPreference;

/**
 * Created by thanhnguyen on 28/03/2016.
 */
public class ForgotPasswordActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        ABSharedPreference.triggerCurrentScreen(ABSharedPreference.CURRENTSCREEN.FORGOT_PASSWORD_ACTIVITY);
    }

    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
        startActivity(myIntent);
        finish();
    }
}
