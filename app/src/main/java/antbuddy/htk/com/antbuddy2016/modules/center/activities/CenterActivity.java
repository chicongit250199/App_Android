package antbuddy.htk.com.antbuddy2016.modules.center.activities;

import android.app.Service;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.ProgressBar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import antbuddy.htk.com.antbuddy2016.interfaces.HttpRequestReceiver;
import antbuddy.htk.com.antbuddy2016.api.LoginAPI;
import antbuddy.htk.com.antbuddy2016.api.ParseJson;
import antbuddy.htk.com.antbuddy2016.customview.TabBarView;
import antbuddy.htk.com.antbuddy2016.model.ObjectManager;
import antbuddy.htk.com.antbuddy2016.modules.login.activities.DomainActivity;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.modules.login.activities.LoReActivity;
import antbuddy.htk.com.antbuddy2016.service.AntbuddyApplication;
import antbuddy.htk.com.antbuddy2016.service.AntbuddyService;
import antbuddy.htk.com.antbuddy2016.util.AndroidHelper;
import antbuddy.htk.com.antbuddy2016.util.Constants;
import antbuddy.htk.com.antbuddy2016.util.JSONKey;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;
import antbuddy.htk.com.antbuddy2016.modules.center.activities.RecentFragment;

/**
 * Created by thanhnguyen on 29/03/2016.
 */
public class CenterActivity extends AppCompatActivity {

    public static final String TAG_THISCLASS = "CenterActivity";
    private ProgressBar progressBar_Center;
    private ViewPager mViewPager;
    private List<Fragment> mTabFragments = new ArrayList<>();
    private TabBarView tabBarView;

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

        initView();
        loadData();
    }
    private void initView() {
        progressBar_Center = (ProgressBar) findViewById(R.id.progressBar_Center);

        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
        tabBarView = (TabBarView) findViewById(R.id.change_color_tab);

        RecentFragment recentFragment = new RecentFragment();
        mTabFragments.add(recentFragment);
        MembersFragment membersFragment = new MembersFragment();
        mTabFragments.add(membersFragment);
        GroupsFragment groupsFragment = new GroupsFragment();
        mTabFragments.add(groupsFragment);
        ProfileFragment profileFragment = new ProfileFragment();
        mTabFragments.add(profileFragment);

        tabBarView.setViewpager(mViewPager);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mTabFragments.size();
            }

            @Override
            public Fragment getItem(int position) {
                return mTabFragments.get(position);
            }
        });
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

                // Reset Object Manager
                ObjectManager.getInstance().clear();

                AntbuddyApplication.getInstance().cancelPendingRequests(AntbuddyApplication.TAG);

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
                    AndroidHelper.showToast("Loading user profile...", CenterActivity.this);
                    AndroidHelper.showProgressBar(CenterActivity.this, progressBar_Center);
                    LoginAPI.GETUserProfile(new HttpRequestReceiver() {

                        @Override
                        public void onSuccess(Object response) {

                            String chatToken = ParseJson.getStringWithKey((JSONObject) response, JSONKey.chatToken);
                            String chatURLXMPP = ParseJson.getStringWithKey((JSONObject) response, JSONKey.chatUrl);
                            Constants.DOMAIN_XMPP = ParseJson.getStringWithKey((JSONObject) response, JSONKey.chatDomain);
                            String[] fields = chatToken.split(":");
                            Constants.USERNAME_XMPP = fields[0];
                            Constants.PASSWORD_XMPP = fields[1];

                            Pattern p = Pattern.compile(".*\\/\\/([^:^\\/]+).*");
                            Matcher m = p.matcher(chatURLXMPP);
                            if (m.matches()) {
                                Constants.HOST_XMPP = m.group(1);
                            }

                            // LOGIN XMPP
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    mIRemoteService.loginXMPP(Constants.USERNAME_XMPP, Constants.PASSWORD_XMPP);
                                }
                            }).start();
                            ;

                            LogHtk.d(LogHtk.API_TAG, "chatURLXMPP = " + chatURLXMPP);

                            // Load List Users
                            requestAPIGetListUsers();

                            // Save data into single ton
                            ObjectManager.getInstance().parseUserMe((JSONObject) response);

                            AndroidHelper.hideProgressBar(CenterActivity.this, progressBar_Center);
                        }

                        @Override
                        public void onError(String error) {
                            LogHtk.d(LogHtk.API_TAG, "Center ERROR!" + error);
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

    private void requestAPIGetListUsers() {
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    AndroidHelper.showToast("Loading users...", CenterActivity.this);
                    LoginAPI.GETListUsers(new HttpRequestReceiver() {

                        @Override
                        public void onSuccess(Object response) {
                            LogHtk.i(LogHtk.API_TAG, "List Users: " + response.toString());

                            // Get List Groups
                            requestAPIGetListGroups();

                            ObjectManager.getInstance().parseUsers((JSONArray) response);
                        }

                        @Override
                        public void onError(String error) {
                            LogHtk.e(LogHtk.API_TAG, "Can not get list Users!");
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void requestAPIGetListGroups() {
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    AndroidHelper.showToast("Loading groups...", CenterActivity.this);
                    LoginAPI.GETListGroups(new HttpRequestReceiver() {

                        @Override
                        public void onSuccess(Object response) {
                            LogHtk.i(LogHtk.API_TAG, "List Groups: " + response.toString());

                            ObjectManager.getInstance().parseRooms((JSONArray) response);
                        }

                        @Override
                        public void onError(String error) {
                            LogHtk.e(LogHtk.API_TAG, "Can not get list Groups!");
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
        } else {
            mIRemoteService = AntbuddyService.mAntbuddyService;
        }
        return mIRemoteService != null;
    }
}
