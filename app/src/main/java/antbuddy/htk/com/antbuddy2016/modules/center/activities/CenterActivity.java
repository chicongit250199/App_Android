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
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import antbuddy.htk.com.antbuddy2016.api.APIManager;
import antbuddy.htk.com.antbuddy2016.interfaces.HttpRequestReceiver;
import antbuddy.htk.com.antbuddy2016.customview.TabBarView;
import antbuddy.htk.com.antbuddy2016.model.ObjectManager;
import antbuddy.htk.com.antbuddy2016.model.Room;
import antbuddy.htk.com.antbuddy2016.model.User;
import antbuddy.htk.com.antbuddy2016.model.UserMe;
import antbuddy.htk.com.antbuddy2016.modules.login.activities.DomainActivity;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.modules.login.activities.LoReActivity;
import antbuddy.htk.com.antbuddy2016.service.AntbuddyApplication;
import antbuddy.htk.com.antbuddy2016.service.AntbuddyService;
import antbuddy.htk.com.antbuddy2016.setting.ABSharedPreference;
import antbuddy.htk.com.antbuddy2016.util.AndroidHelper;
import antbuddy.htk.com.antbuddy2016.util.Constants;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;

/**
 * Created by thanhnguyen on 29/03/2016.
 */
public class CenterActivity extends AppCompatActivity {
    final String[] titles = {"Recent", "Members", "Groups", "Profile"};
    public static final String TAG_THISCLASS = "CenterActivity";
    private ProgressBar progressBar_Center;
    private ViewPager mViewPager;
    private List<Fragment> mTabFragments = new ArrayList<>();
    private TabBarView tabBarView;
    private TextView tv_title;

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
        ABSharedPreference.triggerCurrentScreen(ABSharedPreference.CURRENTSCREEN.CENTER_ACTIVITY);

        AntbuddyApplication.getInstance().restartAPIServiceWithDomain(ABSharedPreference.get(ABSharedPreference.KEY_DOMAIN));
        ABSharedPreference.save(ABSharedPreference.KEY_IS_LOGIN, true);

        boolean isConnectService = connectServiceInAndroid();
        if (!isConnectService) {
            LogHtk.i(LogHtk.SERVICE_TAG, "CenterActivity can not get service object in android!");
        }

        initView();
        loadData();
    }

    private void initView() {
        progressBar_Center = (ProgressBar) findViewById(R.id.progressBar_Center);
        mViewPager         = (ViewPager) findViewById(R.id.id_viewpager);
        tabBarView         = (TabBarView) findViewById(R.id.change_color_tab);
        tv_title           = (TextView) findViewById(R.id.tv_title);

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

        //set title
        setTitle(titles[0]);
        tabBarView.setOnPageSelectedListener(new TabBarView.OnPageSelectedListener() {
            @Override
            public void onPageSelected(int position) {
                setTitle(titles[position]);
                AndroidHelper.hideSoftKeyboard(CenterActivity.this);
            }
        });
    }

    @Override
    public void setTitle(CharSequence title) {
        tv_title.setText(title);
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
        finish();
    }

    protected void loadData() {
        if (!AndroidHelper.isInternetAvailable(getApplicationContext())) {
            AndroidHelper.showToast("No network connection available!", CenterActivity.this);
            return;
        }
        getUserMe();
    }

    private void getUserMe() {
        AndroidHelper.showProgressBar(CenterActivity.this, progressBar_Center);
        ObjectManager.getInstance().getUserMe(new ObjectManager.OnObjectManagerListener<UserMe>() {
            @Override
            public void onSuccess(UserMe me) {
                //connectXMPP(me);
                AndroidHelper.hideProgressBar(CenterActivity.this, progressBar_Center);
            }

            @Override
            public void onError(String error) {
                LogHtk.e(LogHtk.API_TAG, "Load UserMe Error!!" + error);
                AndroidHelper.hideProgressBar(CenterActivity.this, progressBar_Center);
                APIManager.showToastWithCode(error, CenterActivity.this);
            }
        });
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

    protected static void connectXMPP(UserMe me) {
        LogHtk.i(LogHtk.API_TAG, "Log UserMe success: " + me.getUsername());

        String[] accountXMPP = me.getChatToken().split(":");
        String username = accountXMPP[0];
        String password = accountXMPP[1];

        Pattern p = Pattern.compile(".*\\/\\/([^:^\\/]+).*");
        Matcher m = p.matcher(me.getChatUrl());
        String hostXMPP = "";
        if (m.matches()) {
            hostXMPP = m.group(1);
        }

        int portXMPP = 5222;    // Default
        String domainXMPP = me.getChatDomain();
        if (hostXMPP.length() > 0 && username.length() > 0 && password.length() > 0 && domainXMPP.length() > 0) {
            ABSharedPreference.save(ABSharedPreference.KEY_XMPP_HOST, hostXMPP);
            ABSharedPreference.save(ABSharedPreference.KEY_XMPP_PORT, portXMPP);
            ABSharedPreference.save(ABSharedPreference.KEY_XMPP_DOMAIN, domainXMPP);
            ABSharedPreference.save(ABSharedPreference.KEY_XMPP_USERNAME, username);
            ABSharedPreference.save(ABSharedPreference.KEY_XMPP_PASSWORD, password);

            // LOGIN XMPP
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mIRemoteService.loginXMPP();
                }
            }).start();
        }
    }
}
