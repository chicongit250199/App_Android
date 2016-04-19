package antbuddy.htk.com.antbuddy2016.modules.login.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.modules.center.activities.CenterActivity;
import antbuddy.htk.com.antbuddy2016.setting.ABSharedPreference;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;

/**
 * Created by thanhnguyen on 29/03/2016.
 */
public class SplashScreenActivity extends Activity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                String currentScreen = ABSharedPreference.get(ABSharedPreference.KEY_CURRENT_SCREEN);
                if (currentScreen.equals(ABSharedPreference.CURRENTSCREEN.WALK_THROUGH_ACTIVITY.toString())) {
                    Intent i = new Intent(SplashScreenActivity.this, WalkThroughActivity.class);
                    startActivity(i);
                    finish();
                }

                if (currentScreen.equals(ABSharedPreference.CURRENTSCREEN.LORE_ACTIVITY.toString())) {
                    Intent i = new Intent(SplashScreenActivity.this, LoReActivity.class);
                    startActivity(i);
                    finish();
                }

                if (currentScreen.equals(ABSharedPreference.CURRENTSCREEN.LOGIN_ACTIVITY.toString())) {
                    Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }

                if (currentScreen.equals(ABSharedPreference.CURRENTSCREEN.DOMAIN_ACTIVITY.toString())) {
                    Intent i = new Intent(SplashScreenActivity.this, DomainActivity.class);
                    startActivity(i);
                    finish();
                }

                if (currentScreen.equals(ABSharedPreference.CURRENTSCREEN.CENTER_ACTIVITY.toString())) {
                    Intent i = new Intent(SplashScreenActivity.this, LoadingActivity.class);
                    startActivity(i);
                    finish();
                }

                if (currentScreen.equals(ABSharedPreference.CURRENTSCREEN.CREATE_ACCOUNT_ACTIVITY.toString())) {
                    Intent i = new Intent(SplashScreenActivity.this, CreateAccountActivity.class);
                    startActivity(i);
                    finish();
                }

                if (currentScreen.equals(ABSharedPreference.CURRENTSCREEN.FORGOT_PASSWORD_ACTIVITY.toString())) {
                    Intent i = new Intent(SplashScreenActivity.this, ForgotPasswordActivity.class);
                    startActivity(i);
                    finish();
                }

                // currentScreen will be empty because the first time run app.
                if (currentScreen.length() == 0) {
                    Intent i = new Intent(SplashScreenActivity.this, WalkThroughActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        }, SPLASH_TIME_OUT);
    }
}
