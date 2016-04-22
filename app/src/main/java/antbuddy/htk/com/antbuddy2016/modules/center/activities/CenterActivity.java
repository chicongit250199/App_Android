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
import antbuddy.htk.com.antbuddy2016.RealmObjects.RObjectManager;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RObjectManagerBackGround;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RObjectManagerOne;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RRoom;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RUser;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RUserMe;
import antbuddy.htk.com.antbuddy2016.api.APIManager;
import antbuddy.htk.com.antbuddy2016.interfaces.HttpRequestReceiver;
import antbuddy.htk.com.antbuddy2016.model.Room;
import antbuddy.htk.com.antbuddy2016.model.User;
import antbuddy.htk.com.antbuddy2016.model.UserMe;
import antbuddy.htk.com.antbuddy2016.service.AntbuddyApplication;
import antbuddy.htk.com.antbuddy2016.service.AntbuddyService;
import antbuddy.htk.com.antbuddy2016.setting.ABSharedPreference;
import antbuddy.htk.com.antbuddy2016.util.AndroidHelper;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

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

    private RObjectManagerOne realmManager;
    private Realm realm;
    private RUserMe userMe;
    private RealmResults<RUser> users;
    private RealmResults<RRoom> rooms;

    private RealmChangeListener userMeListener;
    private RealmChangeListener usersListener;
    private RealmChangeListener roomsListener;

	// Work with service
//    public static AntbuddyService mIRemoteService;
//    private boolean mBound;
//    private final ServiceConnection mConnection = new ServiceConnection() {
//        public void onServiceConnected(ComponentName className, IBinder service) {
//            AntbuddyService.LocalBinder binder = (AntbuddyService.LocalBinder) service;
//            mIRemoteService = binder.getService();
//            mBound = true;
//            LogHtk.d(LogHtk.SERVICE_TAG, "CenterActivity/onServiceConnected");
//        }
//
//        public void onServiceDisconnected(ComponentName className) {
//            mIRemoteService = null;
//            mBound = false;
//            LogHtk.e(LogHtk.SERVICE_TAG, "CenterActivity/onServiceDisconnected");
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        LogHtk.i(LogHtk.Test3, "CenterActivity onCreate!");
        setContentView(R.layout.activity_center);

        ABSharedPreference.triggerCurrentScreen(ABSharedPreference.CURRENTSCREEN.CENTER_ACTIVITY);

        //AntbuddyApplication.getInstance().restartAPIServiceWithDomain(ABSharedPreference.get(ABSharedPreference.KEY_DOMAIN));
        ABSharedPreference.save(ABSharedPreference.KEY_IS_LOGIN, true);


        realmManager = new RObjectManagerOne();
        setupRealmData();

//        boolean isConnectService = connectServiceInAndroid();
//        if (!isConnectService) {
//            LogHtk.i(LogHtk.SERVICE_TAG, "CenterActivity can not get service object in android!");
//        }

        AntbuddyService.getInstance().loginXMPP();

        initView();
        viewsListener();
    }

    private void setupRealmData() {
        realm  = Realm.getDefaultInstance();

        userMe = realm.where(RUserMe.class).findFirst();
        users  = realm.where(RUser.class).findAllAsync();
        rooms  = realm.where(RRoom.class).findAllAsync();

        userMeListener = new RealmChangeListener() {
            @Override
            public void onChange() {
                //AntbuddyService.getInstance().loginXMPP();

                RecentFragment recentFragment = isRecentFragmentExist();
                if (recentFragment == null) {

                } else {

                }
            }
        };

        usersListener = new RealmChangeListener() {
            @Override
            public void onChange() {
                RecentFragment recentFragment = (RecentFragment) mTabFragments.get(0);
                if (recentFragment != null && recentFragment.isVisible()) {
                    recentFragment.updateUI();
                } else {
                    LogHtk.e(LogHtk.WarningHTK, "RecentFragment is not exist!");
                }
            }
        };

        roomsListener = new RealmChangeListener() {
            @Override
            public void onChange() {
            }
        };

        RObjectManager.getInstance().assignRealm(this.realm);
        RObjectManager.getInstance().assignUserMe(this.userMe, this.userMeListener);
        RObjectManager.getInstance().assignUsers(this.users, this.usersListener);
        RObjectManager.getInstance().assignRooms(this.rooms, this.roomsListener);

        RObjectManager.getInstance().loading_UserMe_Users_Rooms();
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
//                if (btnAlwaysChange.getTag() == 0) {
//                    AndroidHelper.gotoActivity(CenterActivity.this, OpeningRoomActivity.class, false);
//                }

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
//        if(mBound) {
//            unbindService(mConnection);
//        }

        if (userMe != null) {
            userMe.removeChangeListener(userMeListener);
        }

        realm.close();
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

        APIManager.GETUserMe(new HttpRequestReceiver<UserMe>() {
            @Override
            public void onSuccess(UserMe me) {
                //RObjectManager.saveUserMeOrUpdate(me);
                //AntbuddyApplication.getInstance().setUserme(RObjectManager.getUserMe());
                AndroidHelper.hideProgressBar(CenterActivity.this, progressBar_Center);
            }

            @Override
            public void onError(String error) {
                LogHtk.e(LogHtk.API_TAG, "Load UserMe Error!!" + error);
                AndroidHelper.hideProgressBar(CenterActivity.this, progressBar_Center);
                APIManager.showToastWithCode(error, CenterActivity.this);
            }
        });

//        ObjectManager.getInstance().getUserMe(new ObjectManager.OnObjectManagerListener<RUserMe>() {
//            @Override
//            public void onSuccess(RUserMe me) {
//                //connectXMPP(me);
//                AndroidHelper.hideProgressBar(CenterActivity.this, progressBar_Center);
//            }
//
//            @Override
//            public void onError(String error) {
//                LogHtk.e(LogHtk.API_TAG, "Load UserMe Error!!" + error);
//                AndroidHelper.hideProgressBar(CenterActivity.this, progressBar_Center);
//                APIManager.showToastWithCode(error, CenterActivity.this);
//            }
//        });
    }


    protected void loading_UserMe_Users_Rooms() {
        Boolean isdataLoadedFromDB = false;
        AntbuddyApplication application = AntbuddyApplication.getInstance();
//        if (application.isUserMeExist() && application.isUsersExist() && application.isRoomsExist()) {
////            updateUI();
//            isdataLoadedFromDB = true;
//        }

        if (AndroidHelper.isInternetAvailable(getApplicationContext())) {
            APIManager.GETUserMe(new HttpRequestReceiver<UserMe>() {
                @Override
                public void onSuccess(UserMe me) {
                    //RObjectManager.saveUserMeOrUpdate(me);
                    //AntbuddyApplication.getInstance().setUserme(RObjectManager.getUserMe());
                    loadUsers();
                }

                @Override
                public void onError(String error) {
                    LogHtk.e(LogHtk.RecentFragment, "Error! Can not load UserMe from server!");
                    APIManager.showToastWithCode(error, CenterActivity.this);
//                    processUIWhenError();
                }
            });
        } else if (!isdataLoadedFromDB) {    // No connection and No data in DB
//            processUIWhenError();
        }
    }

    private void loadUsers() {
        APIManager.GETUsers(new HttpRequestReceiver<List<User>>() {
            @Override
            public void onSuccess(List<User> users) {
//                RObjectManager.saveUsersOrUpdate(users);
//                AntbuddyApplication.getInstance().setUsers(RObjectManager.getUsers());
                loadRooms();
            }

            @Override
            public void onError(String error) {
                LogHtk.e(LogHtk.RecentFragment, "Error! Can not load Users from server!");
                APIManager.showToastWithCode(error, CenterActivity.this);
//                processUIWhenError();
            }
        });
    }

    private void loadRooms() {
        APIManager.GETGroups(new HttpRequestReceiver<List<Room>>() {
            @Override
            public void onSuccess(List<Room> rooms) {
//                RObjectManager.saveRoomsOrUpdate(rooms);
//                AntbuddyApplication.getInstance().setRooms(RObjectManager.getRooms());

//                connectXMPP(AntbuddyApplication.getInstance().getUserme());
//                updateUI();
            }

            @Override
            public void onError(String error) {
                LogHtk.e(LogHtk.RecentFragment, "Error! Can not load Rooms from server!");
                APIManager.showToastWithCode(error, CenterActivity.this);
//                processUIWhenError();

            }
        });
    }

//    private boolean connectServiceInAndroid() {
//        if(AntbuddyService.mAntbuddyService == null) {
//            Intent intent = new Intent(this, AntbuddyService.class);
//            bindService(intent, mConnection, Service.BIND_AUTO_CREATE);
//        } else {
//            mIRemoteService = AntbuddyService.mAntbuddyService;
//        }
//        return mIRemoteService != null;
//    }

//    protected static void connectXMPP(RUserMe me) {
//        LogHtk.i(LogHtk.API_TAG, "Log UserMe success: " + me.getUsername());
//
//        String[] accountXMPP = me.getChatToken().split(":");
//        String username = accountXMPP[0];
//        String password = accountXMPP[1];
//
//        Pattern p = Pattern.compile(".*\\/\\/([^:^\\/]+).*");
//        Matcher m = p.matcher(me.getChatUrl());
//        String hostXMPP = "";
//        if (m.matches()) {
//            hostXMPP = m.group(1);
//        }
//
//        int portXMPP = 5222;    // Default
//        String domainXMPP = me.getChatDomain();
//
//        LogHtk.i(LogHtk.Test1, "----");
//        LogHtk.i(LogHtk.Test1, "getDOMAIN_XMPP = " + domainXMPP);
//        LogHtk.i(LogHtk.Test1, "getHOST_XMPP = " + hostXMPP);
//        LogHtk.i(LogHtk.Test1, "getPASSWORD_XMPP = " + password);
//        LogHtk.i(LogHtk.Test1, "getUSERNAME_XMPP = " + username);
//        LogHtk.i(LogHtk.Test1, "getPORT_XMPP = " + portXMPP);
//        LogHtk.i(LogHtk.Test1, "----");
//
//        if (hostXMPP.length() > 0 && username.length() > 0 && password.length() > 0 && domainXMPP.length() > 0) {
//            ABSharedPreference.save(ABSharedPreference.KEY_XMPP_HOST, hostXMPP);
//            ABSharedPreference.save(ABSharedPreference.KEY_XMPP_PORT, portXMPP);
//            ABSharedPreference.save(ABSharedPreference.KEY_XMPP_DOMAIN, domainXMPP);
//            ABSharedPreference.save(ABSharedPreference.KEY_XMPP_USERNAME, username);
//            ABSharedPreference.save(ABSharedPreference.KEY_XMPP_PASSWORD, password);
//
//            // LOGIN XMPP
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    mIRemoteService.loginXMPP();
//                }
//            }).start();
//        }
//    }

    private RecentFragment isRecentFragmentExist() {
        RecentFragment recentFragment = (RecentFragment) mTabFragments.get(0);
        if (recentFragment != null && recentFragment.isVisible()) {
            return recentFragment;
        } else {
            return null;
        }
    }
}
