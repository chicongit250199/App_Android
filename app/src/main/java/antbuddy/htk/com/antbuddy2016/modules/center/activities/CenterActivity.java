package antbuddy.htk.com.antbuddy2016.modules.center.activities;

import android.app.Service;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.widget.ProgressBar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.interfaces.HttpRequestReceiver;
import antbuddy.htk.com.antbuddy2016.api.LoginAPI;
import antbuddy.htk.com.antbuddy2016.api.ParseJson;
import antbuddy.htk.com.antbuddy2016.modules.login.activities.DomainActivity;
import antbuddy.htk.com.antbuddy2016.modules.login.activities.LoReActivity;
import antbuddy.htk.com.antbuddy2016.service.AntbuddyService;
import antbuddy.htk.com.antbuddy2016.util.AndroidHelper;
import antbuddy.htk.com.antbuddy2016.util.Constants;
import antbuddy.htk.com.antbuddy2016.util.JSONKey;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;

/**
 * Created by thanhnguyen on 29/03/2016.
 */
public class CenterActivity extends FragmentActivity {

    public static final String TAG_THISCLASS = "CenterActivity";

    private FragmentTabHost mTabHost;
    private ProgressBar progressBar_Center;

    // Work with service
    public static AntbuddyService mIRemoteService = AntbuddyService.mAntbuddyService;
    private boolean mBound;
    private final ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            AntbuddyService.LocalBinder binder = (AntbuddyService.LocalBinder) service;
            mIRemoteService = binder.getService();
            mBound = true;
            LogHtk.d(LogHtk.SERVICE_TAG, "CenterActivity/onServiceConnected");
        }

        public void onServiceDisconnected(ComponentName className) {
            mIRemoteService = null;
            mBound = false;
            LogHtk.e(LogHtk.SERVICE_TAG, "CenterActivity/onServiceDisconnected");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center);

        boolean isConnectService = connectServiceInAndroid();
        if (!isConnectService) {
            LogHtk.i(LogHtk.SERVICE_TAG, "CenterActivity can not get service object in android!");
        }

        //init
        progressBar_Center = (ProgressBar) findViewById(R.id.progressBar_Center);

        // Load data
        loadData();


        //tabHost = new FragmentTabHost(getActivity());
        if (mTabHost == null) {
            mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);

            // Create the tabs in main_fragment.xml
            mTabHost.setup(CenterActivity.this, getSupportFragmentManager(), R.id.realtabcontent);

            mTabHost.addTab(mTabHost.newTabSpec("RecentTab").setIndicator("Recent"), RecentFragment.class, null);
            mTabHost.addTab(mTabHost.newTabSpec("MembersTab").setIndicator("Members"), MembersFragment.class, null);
            mTabHost.addTab(mTabHost.newTabSpec("GroupsTab").setIndicator("Groups"), GroupsFragment.class, null);
            mTabHost.addTab(mTabHost.newTabSpec("ProfileTab").setIndicator("Profile"), ProfileFragment.class, null);
            mTabHost.setCurrentTab(0);
        }
    }

    @Override
    protected void onDestroy() {
        if(mBound) {
            unbindService(mConnection);
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        AndroidHelper.alertDialogShow(this, "Do you want to switch company?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LoReActivity.resetXMPP();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mIRemoteService.resetXMPP();
                    }
                }).start();

                Intent myIntent = new Intent(CenterActivity.this, DomainActivity.class);
                startActivity(myIntent);
                finish();
            }
        }, null);
    }

    void loadData() {
        if (!AndroidHelper.isInternetAvailable(getApplicationContext())) {
            AndroidHelper.showToast("No network connection available!", CenterActivity.this);
            return;
        }

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    AndroidHelper.showProgressBar(CenterActivity.this, progressBar_Center);
                    LoginAPI.GETOrganizationUserProfile(new HttpRequestReceiver() {

                        @Override
                        public void onSuccess(String result) {
                            String chatToken = ParseJson.getStringWithKey(result, JSONKey.chatToken);
                            String chatURLXMPP = ParseJson.getStringWithKey(result, JSONKey.chatUrl);
                            Constants.DOMAIN_XMPP = ParseJson.getStringWithKey(result, JSONKey.chatDomain);
                            String[] fields = chatToken.split(":");
                            Constants.USERNAME_XMPP = fields[0];
                            Constants.PASSWORD_XMPP = fields[1];

                            Pattern p = Pattern.compile(".*\\/\\/([^:^\\/]+).*");
                            Matcher m = p.matcher(chatURLXMPP);
                            if (m.matches()) {
                                Constants.HOST_XMPP = m.group(1);
                            }

                            // LOGIN XMPP
                            mIRemoteService.loginXMPP(Constants.USERNAME_XMPP, Constants.PASSWORD_XMPP);

                            LogHtk.d(TAG_THISCLASS, "result = " + result);
                            LogHtk.d(TAG_THISCLASS, "Host = " + Constants.HOST_XMPP);
                            AndroidHelper.hideProgressBar(CenterActivity.this, progressBar_Center);
                        }

                        @Override
                        public void onError(String error) {
                            LogHtk.d(TAG_THISCLASS, TAG_THISCLASS + " ERROR!");
                            AndroidHelper.hideProgressBar(CenterActivity.this, progressBar_Center);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private boolean connectServiceInAndroid() {
        if(AntbuddyService.mAntbuddyService == null) {
            Intent intent = new Intent(this, AntbuddyService.class);
            bindService(intent, mConnection, Service.BIND_AUTO_CREATE);
        }
        else
        {
            mIRemoteService = AntbuddyService.mAntbuddyService;
        }
        return mIRemoteService != null;
    }
}
