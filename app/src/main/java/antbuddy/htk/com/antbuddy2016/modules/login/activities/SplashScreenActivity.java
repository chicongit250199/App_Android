package antbuddy.htk.com.antbuddy2016.modules.login.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RObjectManager;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RRoom;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RUser;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RUserMe;
import antbuddy.htk.com.antbuddy2016.modules.center.activities.CenterActivity;
import antbuddy.htk.com.antbuddy2016.setting.ABSharedPreference;
import antbuddy.htk.com.antbuddy2016.util.AndroidHelper;
import antbuddy.htk.com.antbuddy2016.util.BroadcastConstant;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by thanhnguyen on 29/03/2016.
 */
public class SplashScreenActivity extends Activity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1000;

    private Realm realm;
    private RUserMe userMe;
    private RealmResults<RUser> users;
    private RealmResults<RRoom> rooms;

    private RealmChangeListener userMeListener;
    private RealmChangeListener usersListener;
    private RealmChangeListener roomsListener;

    // Get broadcast from local service
    private BroadcastReceiver loadingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                new Exception().printStackTrace();

                String result = intent.getStringExtra("loadingResult");
                LogHtk.i(LogHtk.Test1, "result = " + result);

                if (result.contains("yes")) {
                    AndroidHelper.gotoActivity(SplashScreenActivity.this, CenterActivity.class, true);
                    unregisterReceiver(loadingReceiver);
                }

                if (result.contains("No address associated with hostname")) {
                    LogHtk.i(LogHtk.Test1, "No address associated with hostname = " + result);
                    if (userMe.isValid() && users.isValid() && rooms.isValid()) {
                        AndroidHelper.gotoActivity(SplashScreenActivity.this, CenterActivity.class, true);
                        unregisterReceiver(loadingReceiver);
                    }
                }

                if (result.contains("noRooms")) {
                    AndroidHelper.gotoActivity(SplashScreenActivity.this, DomainActivity.class, true);
                    unregisterReceiver(loadingReceiver);
                }

                if (result.contains("noUsers")) {
                    AndroidHelper.gotoActivity(SplashScreenActivity.this, DomainActivity.class, true);
                    unregisterReceiver(loadingReceiver);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registerReceiver(loadingReceiver, new IntentFilter(BroadcastConstant.CENTER_LOADING_DATA_SUCEESS));

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
                    loadingCenter();
//                    Intent i = new Intent(SplashScreenActivity.this, CenterActivity.class);
//                    startActivity(i);
//                    finish();
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

    @Override
    protected void onDestroy() {
        try {
            if (loadingReceiver != null) {
                this.unregisterReceiver(loadingReceiver);
            }
        } catch (IllegalArgumentException e) {
            LogHtk.i(LogHtk.Test3,"epicReciver is already unregistered");
            loadingReceiver = null;
        }
        super.onDestroy();
    }

    private void loadingCenter() {
        LogHtk.d(LogHtk.Test3, "---> Vao day!");
        realm  = Realm.getDefaultInstance();
        userMe = realm.where(RUserMe.class).findFirst();
        users  = realm.where(RUser.class).findAllAsync();
        rooms  = realm.where(RRoom.class).findAllAsync();

        userMeListener = new RealmChangeListener() {
            @Override
            public void onChange() {
                LogHtk.d(LogHtk.Test3, "---> UserMe Changed");

            }
        };

        usersListener = new RealmChangeListener() {
            @Override
            public void onChange() {
                LogHtk.d(LogHtk.Test3, "---> Users Changed");
            }
        };

        roomsListener = new RealmChangeListener() {
            @Override
            public void onChange() {
                // ... do something with the updated Dog instance
                LogHtk.i(LogHtk.Test3, "Rooms onChange at Center Activity");

            }
        };

        RObjectManager.getInstance().assignRealm(realm);
        RObjectManager.getInstance().assignUserMe(userMe, userMeListener);
        RObjectManager.getInstance().assignUsers(users, usersListener);
        RObjectManager.getInstance().assignRooms(rooms, roomsListener);

        RObjectManager.getInstance().loading_UserMe_Users_Rooms();
    }
}
