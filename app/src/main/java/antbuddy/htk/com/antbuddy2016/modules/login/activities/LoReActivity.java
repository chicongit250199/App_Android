package antbuddy.htk.com.antbuddy2016.modules.login.activities;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.service.AntbuddyService;
import antbuddy.htk.com.antbuddy2016.setting.ABSharedPreference;
import antbuddy.htk.com.antbuddy2016.util.Constants;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;

/**
 * Created by thanhnguyen on 29/03/2016.
 */
public class LoReActivity extends Activity {

    // Buttons
    private Button login_LoRe_Button;
    private Button createNewAccount_LoRe_Button;

    private boolean isBindService = false;
    AntbuddyService mService;
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            AntbuddyService.LocalBinder binder = (AntbuddyService.LocalBinder) service;
            mService = (AntbuddyService) binder.getService();
            isBindService = true;
            LogHtk.d(LogHtk.SERVICE_TAG, "LoReActivity/onServiceConnected");
        }

        public void onServiceDisconnected(ComponentName className) {
            mService = null;
            isBindService = false;
            LogHtk.e(LogHtk.SERVICE_TAG, "LoReActivity/onServiceDisconnected");
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loreactivity);
        ABSharedPreference.triggerCurrentScreen(ABSharedPreference.CURRENTSCREEN.LORE_ACTIVITY);

        ABSharedPreference.resetAccountInSharedPreferences();
        initViews();
        getBunldeAndProcess();

        // Button listner
        login_LoRe_Button.setOnClickListener(welcomeListener);
        createNewAccount_LoRe_Button.setOnClickListener(welcomeListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService();
    }

    @Override
    protected void onDestroy() {
        if (isBindService) {
            unbindService(mConnection);
        }
        mService = null;

        //unregisterReceiver(bcWifiReceiver);
        super.onDestroy();
    }

    void initViews() {
        login_LoRe_Button = (Button) findViewById(R.id.login_LoRe_Button);
        createNewAccount_LoRe_Button = (Button) findViewById(R.id.createNewAccount_LoRe_Button);
    }

    void getBunldeAndProcess() {
        // Get bundle from WalkThroughActivity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int typeValue = extras.getInt(Constants.LOGIN_TYPE);

            if (typeValue == 1) {
                Intent myIntent = new Intent(LoReActivity.this, CreateAccountActivity.class);
                startActivity(myIntent);
            } else if (typeValue == 2) {
                Intent myIntent = new Intent(LoReActivity.this, LoginActivity.class);
                startActivity(myIntent);
            }
            finish();
        }
    }

    View.OnClickListener welcomeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.createNewAccount_LoRe_Button:
                    Intent signUpIntent = new Intent(LoReActivity.this, CreateAccountActivity.class);
                    startActivity(signUpIntent);
                    break;

                case R.id.login_LoRe_Button:
                    Intent loginIntent = new Intent(LoReActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                    break;

                default:
                    break;
            }
            finish();
        }
    };

    // Service process
    private void bindService() {
        try {
            if(AntbuddyService.getInstance() == null) {
                startService(new Intent(this, AntbuddyService.class));
                Intent intent = new Intent(this, Class.forName(AntbuddyService.class.getName()));
                // binding to remote service
                bindService(intent, mConnection, Service.BIND_AUTO_CREATE);
            } else {
                mService = AntbuddyService.mAntbuddyService;
            }
        } catch (ClassNotFoundException e) {
            LogHtk.e(LogHtk.SERVICE_TAG, "Bind service ERROR!");
            e.printStackTrace();
        }
    }
}
