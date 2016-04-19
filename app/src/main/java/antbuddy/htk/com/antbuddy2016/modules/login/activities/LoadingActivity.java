package antbuddy.htk.com.antbuddy2016.modules.login.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RObjectManager;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RRoom;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RUser;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RUserMe;
import antbuddy.htk.com.antbuddy2016.modules.center.activities.CenterActivity;
import antbuddy.htk.com.antbuddy2016.modules.center.activities.RecentFragment;
import antbuddy.htk.com.antbuddy2016.service.AntbuddyApplication;
import antbuddy.htk.com.antbuddy2016.setting.ABSharedPreference;
import antbuddy.htk.com.antbuddy2016.util.AndroidHelper;
import antbuddy.htk.com.antbuddy2016.util.BroadcastConstant;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by thanhnguyen on 19/04/2016.
 */
public class LoadingActivity extends Activity {
    private Realm realm;
    private RUserMe userMe;
    private RealmResults<RUser> users;
    private RealmResults<RRoom> rooms;

    private RealmChangeListener userMeListener;
    private RealmChangeListener usersListener;
    private RealmChangeListener roomsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        AntbuddyApplication.getInstance().restartAPIServiceWithDomain(ABSharedPreference.get(ABSharedPreference.KEY_DOMAIN));
        registerReceiver(loadingReceiver, new IntentFilter(BroadcastConstant.CENTER_LOADING_DATA_SUCEESS));

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

        RObjectManager.getInstance().assignRealm(this.realm);
        RObjectManager.getInstance().assignUserMe(this.userMe, this.userMeListener);
        RObjectManager.getInstance().assignUsers(this.users, this.usersListener);
        RObjectManager.getInstance().assignRooms(this.rooms, this.roomsListener);

        RObjectManager.getInstance().loading_UserMe_Users_Rooms();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(loadingReceiver);
        super.onDestroy();
    }

    private final BroadcastReceiver loadingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String result = intent.getStringExtra("loadingResult");
                LogHtk.e(LogHtk.Test1, "result = " + result);

                if (result.contains("yes")) {
                    AndroidHelper.gotoActivity(LoadingActivity.this, CenterActivity.class, true);
                }

                if (result.contains("noUserMe")) {
                    AndroidHelper.gotoActivity(LoadingActivity.this, DomainActivity.class, true);
                }

                if (result.contains("noRooms")) {
                    AndroidHelper.gotoActivity(LoadingActivity.this, DomainActivity.class, true);
                }

                if (result.contains("noUsers")) {
                    AndroidHelper.gotoActivity(LoadingActivity.this, DomainActivity.class, true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
