package antbuddy.htk.com.antbuddy2016.modules.center.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center);

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
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        AndroidHelper.alertDialogShow(this, "Do you want to switch company?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
                            String[] fields = chatToken.split(":");
                            Constants.USERNAME_XMPP = fields[0];
                            Constants.PASSWORD_XMPP = fields[1];

                            Pattern p = Pattern.compile(".*\\/\\/([^:^\\/]+).*");
                            Matcher m = p.matcher(chatURLXMPP);
                            if (m.matches()) {
                                Constants.HOST_XMPP = m.group(1);
                            }

                            // LOGIN XMPP

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
}
