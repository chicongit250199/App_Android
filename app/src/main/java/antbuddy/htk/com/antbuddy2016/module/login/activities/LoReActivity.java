package antbuddy.htk.com.antbuddy2016.module.login.activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.tgmcians.crashhandler.ExceptionHandler;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.service.LocalBinder;
import antbuddy.htk.com.antbuddy2016.service.LocalService;
import antbuddy.htk.com.antbuddy2016.util.Constants;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;

/**
 * Created by thanhnguyen on 29/03/2016.
 */
public class LoReActivity extends Activity {

    // Buttons
    private Button login_LoRe_Button;
    private Button createNewAccount_LoRe_Button;

    private boolean mBounded;
    private LocalService mService;

    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = ((LocalBinder<LocalService>) service).getService();
            mBounded = true;
            LogHtk.d("asdf", "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBounded = false;
            LogHtk.d("asdf", "onServiceDisconnected");
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loreactivity);

        // set the default uncaught exception handler
        // whenever any crash occurs which you haven't caught then a user
        // can report to developer about that exception
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        doBindService();
        reset();
        initViews();
        getBunldeAndProcess();

        // Button listner
        login_LoRe_Button.setOnClickListener(welcomeListener);
        createNewAccount_LoRe_Button.setOnClickListener(welcomeListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //doUnbindService();
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
        }
    };

    void doBindService() {
        bindService(new Intent(this, LocalService.class), mConnection,
                Context.BIND_AUTO_CREATE);
    }

    void doUnbindService() {
        if (mConnection != null) {
            unbindService(mConnection);
        }
    }

    public LocalService getmService() {
        return mService;
    }

    // Reset
    private void reset() {
        Constants.token = "";
        Constants.domain = "";
        Constants.USERNAME_XMPP = "";
        Constants.PASSWORD_XMPP = "";
    }
}
