package antbuddy.htk.com.antbuddy2016.modules.center.activities;

import android.app.Service;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
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

import antbuddy.htk.com.antbuddy2016.interfaces.HttpRequestReceiver;
import antbuddy.htk.com.antbuddy2016.api.LoginAPI;
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
        tv_title = (TextView) findViewById(R.id.tv_title);
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

    protected void loadData() {
        if (!AndroidHelper.isInternetAvailable(getApplicationContext())) {
            AndroidHelper.showToast("No network connection available!", CenterActivity.this);
            return;
        }
        getUserMe();
    }

    private void getUserMe() {
        AndroidHelper.showToast("Loading user profile...", CenterActivity.this);
        AndroidHelper.showProgressBar(CenterActivity.this, progressBar_Center);
        LoginAPI.GETUserMe(new HttpRequestReceiver<UserMe>() {
            @Override
            public void onSuccess(UserMe me) {
                LogHtk.d(LogHtk.API_TAG, "onSuccess 12");

                // Connect XMPP
                connectXMPP(me);

                // Load List Users
                getListUsers();

                // Save data into single ton
                ObjectManager.getInstance().setUserMe(me);
                AndroidHelper.hideProgressBar(CenterActivity.this, progressBar_Center);
            }

            @Override
            public void onError(String error) {
                LogHtk.e(LogHtk.API_TAG, "Load UserMe Error!!" + error);
            }
        });
    }

    private void getListUsers() {
        AndroidHelper.showToast("Loading users...", CenterActivity.this);
        LoginAPI.GETUsers(new HttpRequestReceiver<List<User>>() {
            @Override
            public void onSuccess(List<User> listUsers) {
                getListGroups();
                ObjectManager.getInstance().setListUsers(listUsers);
            }

            @Override
            public void onError(String error) {
                LogHtk.e(LogHtk.API_TAG, "Can not get list Users!");
            }
        });
    }

    private void getListGroups() {
        AndroidHelper.showToast("Loading groups...", CenterActivity.this);
        LoginAPI.GETGroups(new HttpRequestReceiver<List<Room>>() {
            @Override
            public void onSuccess(List<Room> listRooms) {
                LogHtk.i(LogHtk.API_TAG, "List Groups: " + listRooms.toString());
                ObjectManager.getInstance().setListRooms(listRooms);

                // Update Recent
                RecentFragment recentFragment = (RecentFragment) mTabFragments.get(0);
                recentFragment.updateUI();

//                RecentFragment memberFragment = (RecentFragment) mTabFragments.get(1);
//                memberFragment.updateUI();
            }

            @Override
            public void onError(String error) {
                LogHtk.e(LogHtk.API_TAG, "Can not get list Groups!" + error);
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

	public void loadRoom() {
        ObjectManager.getInstance().setOnListenerRoom(this.getClass(), new ObjectManager.OnListenerGroup() {
            @Override
            public void onResponse(List<Room> listRooms) {
            }
        });
    }

    public void loadUsers() {
        ObjectManager.getInstance().setOnListenerUser(this.getClass(), new ObjectManager.OnListenerUser() {
            @Override
            public void onResponse(List<User> listUsers) {
            }
        });
    }


    private void connectXMPP(UserMe me) {
        LogHtk.i(LogHtk.API_TAG, "Log UserMe success: " + me.getUsername());

        String chatURLXMPP = me.getChatUrl();
        Constants.DOMAIN_XMPP = me.getChatDomain();
        String[] fields = me.getChatToken().split(":");
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
        LogHtk.d(LogHtk.API_TAG, "chatURLXMPP = " + chatURLXMPP);
    }
}
