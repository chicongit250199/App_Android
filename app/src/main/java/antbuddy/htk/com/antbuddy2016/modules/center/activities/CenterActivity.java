package antbuddy.htk.com.antbuddy2016.modules.center.activities;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.api.APIManager;
import antbuddy.htk.com.antbuddy2016.model.ObjectManager;
import antbuddy.htk.com.antbuddy2016.model.UserMe;
import antbuddy.htk.com.antbuddy2016.service.AntbuddyApplication;
import antbuddy.htk.com.antbuddy2016.service.AntbuddyService;
import antbuddy.htk.com.antbuddy2016.setting.ABSharedPreference;
import antbuddy.htk.com.antbuddy2016.util.AndroidHelper;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;

/**
 * Created by thanhnguyen on 29/03/2016.
 */
public class CenterActivity extends AppCompatActivity {
    final String[] titles = {"RECENT", "GROUPS", "MEMBERS"};
    public static final String TAG_THISCLASS = "CenterActivity";
    private ProgressBar progressBar_Center;
    private ViewPager mViewPager;
    private List<Fragment> mTabFragments = new ArrayList<>();
    PagerSlidingTabStrip tabs;

    private Button btnSearch;
    private Button btnAlwaysChange;
    private Button btnSetting;

	// Work with service
    public static AntbuddyService mIRemoteService;
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
        ABSharedPreference.save(ABSharedPreference.KEY_IS_DOMAIN_EXIST, true);

        AntbuddyApplication.getInstance().restartAPIServiceWithDomain(ABSharedPreference.get(ABSharedPreference.KEY_DOMAIN));
        ABSharedPreference.save(ABSharedPreference.KEY_IS_LOGIN, true);

        boolean isConnectService = connectServiceInAndroid();
        if (!isConnectService) {
            LogHtk.i(LogHtk.SERVICE_TAG, "CenterActivity can not get service object in android!");
        }

        initView();
        viewsListener();
        loadData();
    }

    private void initView() {
        btnSearch       = (Button) findViewById(R.id.btnSearch);
        btnAlwaysChange = (Button) findViewById(R.id.btnAlwaysChange);
        btnSetting      = (Button) findViewById(R.id.btnSetting);

        progressBar_Center = (ProgressBar) findViewById(R.id.progressBar_Center);
        mViewPager         = (ViewPager) findViewById(R.id.id_viewpager);

        RecentFragment recentFragment = new RecentFragment();
        mTabFragments.add(recentFragment);

        GroupsFragment groupsFragment = new GroupsFragment();
        mTabFragments.add(groupsFragment);

        MembersFragment membersFragment = new MembersFragment();
        mTabFragments.add(membersFragment);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }

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

        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setTextColorResource(R.color.white_ab);
        tabs.setViewPager(mViewPager);
        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setTitle(titles[position]);
                AndroidHelper.hideSoftKeyboard(CenterActivity.this);

                if (position == 0) {
                    btnAlwaysChange.setBackgroundResource(R.drawable.open_chat_2);
                    btnAlwaysChange.setTag(0);
                }

                if (position == 1) {
                    btnAlwaysChange.setBackgroundResource(R.drawable.call2);
                    btnAlwaysChange.setTag(1);
                    btnAlwaysChange.setWidth(44);
                    btnAlwaysChange.setHeight(44);
                    return;
                }

                if (position == 2) {
                    btnAlwaysChange.setBackgroundResource(R.drawable.addgroup);
                    btnAlwaysChange.setTag(2);
                }

//                if (position == 3) {
//                    btnAlwaysChange.setBackgroundResource(R.drawable.addperson);
//                    btnAlwaysChange.setTag(3);
//                }
                btnAlwaysChange.setWidth(64);
                btnAlwaysChange.setHeight(64);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void viewsListener() {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnAlwaysChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnAlwaysChange.getTag() == 0) {
                    AndroidHelper.gotoActivity(CenterActivity.this, OpeningRoomActivity.class, false);
                }

                if (btnAlwaysChange.getTag() == 1) {
                    AndroidHelper.showToast("This feature will be available soon!", CenterActivity.this);
                }
                if (btnAlwaysChange.getTag() == 2) {
                    AndroidHelper.showToast("This feature will be available soon!", CenterActivity.this);
                }
//                if (btnAlwaysChange.getTag() == 3) {
//                    AndroidHelper.showToast("This feature will be available soon!", CenterActivity.this);
//                }
            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidHelper.gotoActivity(CenterActivity.this, SettingActivity.class, false);
            }
        });
    }

    @Override
    public void setTitle(CharSequence title) {
//        tv_title.setText(title);
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
